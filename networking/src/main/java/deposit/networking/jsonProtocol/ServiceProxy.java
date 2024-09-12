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
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServiceProxy implements IService {
    private IObserver client;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private Closeable connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServiceProxy(Closeable connection, BufferedReader input, PrintWriter output) {
        this.connection = connection;
        this.input = input;
        this.output = output;

        this.gsonFormatter = new Gson();

        this.qresponses = new LinkedBlockingQueue<Response>();
    }

    private void initializeConnection() {
        this.output.flush();
        this.finished = false;

        startReader();
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    public void closeConnection() {
        this.finished = true;
        System.out.println("finished is " + finished);
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request) throws Exception {
        String reqLine = gsonFormatter.toJson(request);
        try {
            output.println(reqLine);
            output.flush();
        } catch (Exception e) {
            throw new Exception("Error sending object " + e);
        }
    }

    private Response readResponse() throws Exception {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public void login(String username, String password, IObserver client) throws Exception {
        initializeConnection();

        Request req = new Request.Builder().setType(RequestType.LOGIN).setData(new User(username, password)).build();
        System.out.println("Sending Login Request: " + req.toString());
        sendRequest(req);
        Response response = readResponse();
        System.out.println("Recived Login Response: " + response.toString());
        if (response.getType() == ResponseType.OK) {
            System.out.println("Login OK");
            this.client = client;
        }
        if (response.getType() == ResponseType.ERROR) {
            String err = (String) response.getData();
            System.out.println("Closing connection...");
            closeConnection();
            throw new Exception(err);
        }
    }

    @Override
    public void logout(User user, IObserver client) throws Exception {
        Request req = new Request.Builder().setType(RequestType.LOGOUT).setData(user).build();
        System.out.println("Sending Logout Request: " + req.toString());
        sendRequest(req);
        Response response = readResponse();
        System.out.println("Recived Logout Response: " + response.toString());
        this.closeConnection();
        if (response.getType() == ResponseType.OK) {
            System.out.println("Logout OK");
            // close connection ?
        }
        if (response.getType() == ResponseType.ERROR) {
            String err = (String) response.getData();
            System.out.println("Closing connection...");
            throw new Exception(err);
        }
    }

    @Override
    public User getUserByUsername(String username) throws Exception {
        Request req = new Request.Builder().setType(RequestType.GET_USER_BY_USERNAME).setData(username).build();

        System.out.println("Sending getUserByUsername Request: " + req.toString());
        sendRequest(req);
        Response response = readResponse();
        System.out.println("Recived getUserByUsername Response: " + response.toString());

        if (response.getType() == ResponseType.OK) {
            System.out.println("getUserByUsername OK");
            return gsonFormatter.fromJson(response.getData().toString(), User.class);
        }
        if (response.getType() == ResponseType.ERROR) {
            String err = (String) response.getData();
            System.out.println("Closing connection...");
            closeConnection();
            throw new Exception(err);
        }
        return null;
    }

    @Override
    public List<Package> getAllPackages() {
        return null;
    }

    private void handleUpdate(Response response) {
        if (response.getType() == ResponseType.SAVE_PACKAGE) {
            Package pack = gsonFormatter.fromJson(response.getData().toString(), Package.class);
            System.out.println("The package: " + pack);
            try {
                client.packageSaved(pack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (response.getType() == ResponseType.UPDATE_PACKAGE) {
            Package pack = gsonFormatter.fromJson(response.getData().toString(), Package.class);
            System.out.println("The package: " + pack);
            try {
                client.packageUpdated(pack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (response.getType() == ResponseType.DELETE_PACKAGE) {
            Long id = gsonFormatter.fromJson(response.getData().toString(), Long.class);
            System.out.println("The id of the package: " + id);
            try {
                client.packageDeleted(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response) {
        return response.getType() == ResponseType.SAVE_PACKAGE ||
                response.getType() == ResponseType.UPDATE_PACKAGE ||
                response.getType() == ResponseType.DELETE_PACKAGE;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                System.out.println("finished is " + finished);
                try {
                    String responseLine = input.readLine();
                    System.out.println("response received " + responseLine);
                    Response response = gsonFormatter.fromJson(responseLine, Response.class);
                    if (response != null) {
                        if (isUpdate(response)) {
                            handleUpdate(response);
                        } else {
                            try {
                                qresponses.put(response);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error, going to close, error is: " + e);
                    finished = true;
                }
            }
        }
    }
}
