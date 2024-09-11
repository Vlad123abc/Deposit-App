package deposit.networking.jsonProtocol;

import com.google.gson.Gson;
import deposit.domain.Package;
import deposit.service.IObserver;
import deposit.service.IService;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.PrintWriter;

public class ClientWorker implements Runnable, IObserver {
    private IService server;

    private Closeable connection;
    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;

    private volatile boolean connected;

    public ClientWorker(IService server, Closeable connection, BufferedReader input, PrintWriter output)
    {
        this.server = server;
        this.connection = connection;

        gsonFormatter = new Gson();
        this.output = output;
        this.input = input;
        connected = true;
    }

    @Override
    public void packageSaved(Package pack) {

    }

    @Override
    public void packageUpdated(Package pack) {

    }

    @Override
    public void packageDeleted(Long id) {

    }

    @Override
    public void run() {

    }
}
