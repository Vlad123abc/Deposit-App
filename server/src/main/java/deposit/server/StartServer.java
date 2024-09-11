package deposit.server;

import deposit.networking.utils.AbstractServer;
import deposit.networking.utils.JsonConcurrentServer;
import deposit.repository.JdbcUtils;
import deposit.repository.UserDBRepo;
import deposit.repository.UserRepository;
import deposit.service.IService;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class StartServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {
        Properties serverProps = new Properties();

        try {
            serverProps.load(StartServer.class.getResourceAsStream("/server.props"));
            System.out.println("Server properties set.");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.props "+e);
            return;
        }

        JdbcUtils dbUtils = new JdbcUtils(serverProps);
        Connection conn = dbUtils.getConnection();

        UserRepository userRepo = new UserDBRepo(conn);

        IService service = new Service(userRepo);

        int ServerPort = defaultPort;
        try {
            ServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + ServerPort);

        AbstractServer server = new JsonConcurrentServer(ServerPort, service);
        try {
            server.start();
        }
        catch (Exception e) {
            System.err.println("Error starting the server " + e.getMessage());
        }
    }
}
