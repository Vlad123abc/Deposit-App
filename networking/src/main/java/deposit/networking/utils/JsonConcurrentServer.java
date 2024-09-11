package deposit.networking.utils;

import deposit.networking.jsonProtocol.ClientWorker;
import deposit.service.IService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class JsonConcurrentServer extends AbstractConcurrentServer{
    private IService server;
    public JsonConcurrentServer(int port, IService server) {
        super(port);
        this.server = server;
        System.out.println("Json Concurrent Server");
    }

    @Override
    protected Thread createWorker(Socket client) {
        try {
            var output = new PrintWriter(client.getOutputStream());
            var input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            ClientWorker worker = new ClientWorker(server, client, input, output );
            return new Thread(worker);
        } catch (Exception e) {
            System.out.println("Error creating worker thread: " + e.getMessage());
        }
        return null;
    }
}
