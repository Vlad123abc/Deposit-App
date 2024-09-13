package deposit.networking.jsonProtocol;

import com.google.gson.Gson;
import deposit.domain.Package;
import deposit.domain.User;
import deposit.service.IObserver;
import deposit.service.IService;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
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
    public void packageSaved(Package pack) throws Exception {
        Response response = new Response.Builder().setType(ResponseType.SAVE_PACKAGE).setData(pack).build();
        System.out.println("Package saved:" + pack.toString());
        try {
            sendResponse(response);
        }
        catch (IOException e) {
            throw new Exception("Worker saving error: " + e);
        }
    }

    @Override
    public void packageUpdated(Package pack) throws Exception {
        Response response = new Response.Builder().setType(ResponseType.UPDATE_PACKAGE).setData(pack).build();
        System.out.println("Package updated:" + pack.toString());
        try {
            sendResponse(response);
        }
        catch (IOException e) {
            throw new Exception("Worker updating error: " + e);
        }
    }

    @Override
    public void packageDeleted(Long id) throws Exception {
        Response response = new Response.Builder().setType(ResponseType.DELETE_PACKAGE).setData(id).build();
        System.out.println("Package deleted:" + id.toString());
        try {
            sendResponse(response);
        }
        catch (IOException e) {
            throw new Exception("Worker deleting error: " + e);
        }
    }

    public void stop() {
        connected = false;
        System.out.printf("Stop requested, Connected is: %s%n", connected);
    }
    @Override
    public void run() {
        while (connected) {
            try {
                String requestLine = input.readLine();
                System.out.println("received line:" + requestLine);
                Request request = gsonFormatter.fromJson(requestLine, Request.class);
                if (request != null) {
                    Response response = handleRequest(request);
                    if (response != null) {
                        sendResponse(response);
                    }
                }
                System.out.printf("Connected is: %s%n", connected);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        }
        catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    private Response handleRequest(Request request)
    {
        if (request.getType() == RequestType.LOGIN)
        {
            System.out.println("Login request ..." + request.getType());
            User user = gsonFormatter.fromJson(request.getData().toString(), User.class);
            try {
                server.login(user.getUsername(), user.getPassword(), this);
                return new Response.Builder().setType(ResponseType.OK).build();
            }
            catch (Exception e) {
                //connected = false;
                return new Response.Builder().setType(ResponseType.ERROR).setData(e.getMessage()).build();
            }
        }

        if (request.getType() == RequestType.LOGOUT)
        {
            System.out.println("Logout request ..." + request.getType());
            User user = gsonFormatter.fromJson(request.getData().toString(), User.class);
            try {
                server.logout(user, this);
                return new Response.Builder().setType(ResponseType.OK).build();
            }
            catch (Exception e) {
                connected = false;
                return new Response.Builder().setType(ResponseType.ERROR).setData(e.getMessage()).build();
            }
        }

        if (request.getType() == RequestType.GET_USER_BY_USERNAME)
        {
            System.out.println("GET_USER_BY_USERNAME request ...");
            try {
                String username = gsonFormatter.fromJson(request.getData().toString(), String.class);
                User user = this.server.getUserByUsername(username);
                return new Response.Builder().setType(ResponseType.OK).setData(user).build();
            }
            catch (Exception e) {
                connected = false;
                return new Response.Builder().setType(ResponseType.ERROR).setData(e.getMessage()).build();
            }
        }

        return null;
    }

    private void sendResponse(Response response) throws IOException
    {
        String responseLine = gsonFormatter.toJson(response);
        System.out.println("sending response " + responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }
}
