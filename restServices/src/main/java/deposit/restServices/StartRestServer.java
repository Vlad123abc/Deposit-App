package deposit.restServices;

import deposit.repository.*;
import deposit.server.Service;
import deposit.server.StartServer;
import deposit.service.IService;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class StartRestServer {
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
        PackageRepository packageRepo = new PackageDBRepo(conn);

        IService service = new Service(userRepo, packageRepo);

        int ServerPort = defaultPort;
        try {
            ServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + ServerPort);

        RestServer restServer = new RestServer();
        try {
            restServer.start((Service) service);
        }
        catch (Exception e) {
            System.err.println("Error starting the server " + e.getMessage());
        }
    }
}
