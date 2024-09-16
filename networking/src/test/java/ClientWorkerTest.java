import com.google.gson.Gson;
import deposit.domain.Package;
import deposit.networking.jsonProtocol.ClientWorker;
import deposit.networking.jsonProtocol.Response;
import deposit.networking.jsonProtocol.ResponseType;
import deposit.service.IService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientWorkerTest {
    @Test
    public void loginOk() throws Exception
    {
        // setting up input that will be sent
        String r = "{'type'='LOGIN', 'data'={'username'='vlad', 'password'='parola', 'id'=0}}" + System.lineSeparator();
        // setting up input reader - just as we were reading from the socket
        StringReader sr = new StringReader(r);
        var inputBufferedReader = new BufferedReader(sr);

        // setting up writer - our ClientWorker will write responses here. We will assert later what the test output was.
        StringWriter outputWriter = new StringWriter();
        var outputPrinter = new PrintWriter(outputWriter);

        // Mock objects - these implement interfaces, and they do nothing yet.
        IService mockService = Mockito.mock(IService.class);
        Closeable mockSocket = Mockito.mock(Closeable.class);
        // create the worker with input, output and the mock objects - nothing is real here, we test the CW in isolation.
        ClientWorker cw = new ClientWorker(mockService, mockSocket, inputBufferedReader, outputPrinter);
        Thread t = new Thread(cw);
        // rulam clientworker care va face pasii 2,3,4,5
        t.start();
        try {
            Thread.sleep(1000);
        }
        catch (Exception ignored) {}
        cw.stop();
        try {
            t.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        // verificare pas 3 (s-a apelat login pe service?)
        Mockito.verify(mockService).login(Mockito.eq("vlad"), Mockito.eq("parola"),Mockito.any());

        // pas 6.
        // now we assert the output
        // we read line by line from the response object
        String allResponsesOneResponsePerLine = new String(outputWriter.getBuffer());

        StringReader responseReader = new StringReader(allResponsesOneResponsePerLine);
        var responseInput = new BufferedReader(responseReader);
        var responseLine = responseInput.readLine();
        Gson gson = new Gson();
        // was the login response ok?
        var responseData = gson.fromJson(responseLine, Response.class);
        assertEquals(responseData.getType(), ResponseType.OK);
    }

    @Test
    public void loginFail() throws Exception
    {
        // input
        String r = "{'type'='LOGIN', 'data'={'username'='vladaaa', 'password'='parola', 'id'=0}}" + System.lineSeparator();
        // setting up stuff
        StringReader sr = new StringReader(r);
        StringWriter sw = new StringWriter();
        var input = new BufferedReader(sr);
        var output = new PrintWriter(sw);
        IService mockService = Mockito.mock(IService.class);

        // now we say login will be throw an error from our mock service
        Mockito.doThrow(new Exception()).when(mockService).login(Mockito.eq("vladaaa"), Mockito.eq("parola"),Mockito.any());
        Closeable mockSocket = Mockito.mock(Closeable.class);
        ClientWorker cw = new ClientWorker(mockService, mockSocket, input, output);
        Thread t = new Thread(cw);
        t.start();
        try {
            Thread.sleep(1000);
        }
        catch (Exception ignored) {}
        cw.stop();
        try {
            t.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        Mockito.verify(mockService).login(Mockito.eq("vladaaa"), Mockito.eq("parola"),Mockito.any());

        String response = new String(sw.getBuffer());

        StringReader responseReader = new StringReader(response);
        var responseInput = new BufferedReader(responseReader);
        var responseLine = responseInput.readLine();
        Gson gson = new Gson();
        var responseData = gson.fromJson(responseLine, Response.class);
        // assert that the exception was transformed to reject
        assertEquals(responseData.getType(), ResponseType.ERROR);
    }

    @Test
    public void logoutTest() throws Exception
    {
        // setting up input that will be sent
        String r = "{'type'='LOGOUT', 'data'={'username'='vlad', 'password'='parola', 'id'=0}}" + System.lineSeparator();
        // setting up input reader - just as we were reading from the socket
        StringReader sr = new StringReader(r);
        var input = new BufferedReader(sr);

        // setting up writer - our ClientWorker will write responses here. We will assert later what the test output was.
        StringWriter sw = new StringWriter();
        var output = new PrintWriter(sw);

        // Mock objects - these implement interfaces, and they do nothing yet.
        IService mockService = Mockito.mock(IService.class);
        Closeable mockSocket = Mockito.mock(Closeable.class);
        // create the worker with input, output and the mock objects - nothing is real here, we test the CW in isolation.
        ClientWorker cw = new ClientWorker(mockService, mockSocket, input, output);
        Thread t = new Thread(cw);
        t.start();
        try {
            Thread.sleep(1000);
        }
        catch (Exception ignored) {}
        cw.stop();
        try {
            t.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        Mockito.verify(mockService).logout(Mockito.any(), Mockito.any());

        // now we assert the output
        // we read line by line from the response object
        String response = new String(sw.getBuffer());

        StringReader responseReader = new StringReader(response);
        var responseInput = new BufferedReader(responseReader);
        var responseLine = responseInput.readLine();
        Gson gson = new Gson();
        // was the logout response ok?
        var responseData = gson.fromJson(responseLine, Response.class);
        assertEquals(responseData.getType(), ResponseType.OK);
    }

    @Test
    public void getAllPackagesTest() throws Exception {
        // setting up input that will be sent
        String r = "{'type'='GET_ALL_PACKAGES', 'data'=null}" + System.lineSeparator();
        // setting up input reader - just as we were reading from the socket
        StringReader sr = new StringReader(r);
        var input = new BufferedReader(sr);

        // setting up writer - our ClientWorker will write responses here. We will assert later what the test output was.
        StringWriter sw = new StringWriter();
        var output = new PrintWriter(sw);

        // Mock objects - these implement interfaces, and they do nothing yet.
        IService mockService = Mockito.mock(IService.class);
        // Mockito.when(mockList.size()).thenReturn(100);
        List<Package> packages = new ArrayList<>();
        Package pack1 = new Package("pack1", "a", "b", "des", 1F, false);
        pack1.setId(1L);
        Package pack2 = new Package("pack2", "a", "b", "des", 1F, false);
        pack2.setId(2L);

        packages.add(pack1);
        packages.add(pack2);
        Mockito.when(mockService.getAllPackages()).thenReturn(packages);

        Closeable mockSocket = Mockito.mock(Closeable.class);
        // create the worker with input, output and the mock objects - nothing is real here, we test the CW in isolation.
        ClientWorker cw = new ClientWorker(mockService, mockSocket, input, output);
        Thread t = new Thread(cw);
        t.start();
        try {
            Thread.sleep(1000);
        }
        catch (Exception ignored) {}
        cw.stop();
        try {
            t.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        Mockito.verify(mockService).getAllPackages();

        // now we assert the output
        // we read line by line from the response object
        String response = new String(sw.getBuffer());

        StringReader responseReader = new StringReader(response);
        var responseInput = new BufferedReader(responseReader);
        var responseLine = responseInput.readLine();
        Gson gson = new Gson();
        // was the response ok?
        var responseData = gson.fromJson(responseLine, Response.class);
        assertEquals(responseData.getType(), ResponseType.OK);

        List<Package> packageList = new ArrayList<>();
        var list = gson.fromJson(responseData.getData().toString(), packages.getClass());
        for (var pack : list)
        {
            Package p = gson.fromJson(pack.toString(), Package.class);
            packageList.add(p);
        }
        assertEquals(packageList, packages);
    }

    @Test
    public void SavePackageTest() throws Exception
    {
        // setting up input that will be sent
        String r = "{'type'='SAVE_PACKAGE', 'data'={'name'='pack', 'p_from'='a', 'p_to'='b', 'description'='des', 'weight'=1, 'fragile'=false, 'id'=0}}" + System.lineSeparator();
        // setting up input reader - just as we were reading from the socket
        StringReader sr = new StringReader(r);
        var input = new BufferedReader(sr);

        // setting up writer - our ClientWorker will write responses here. We will assert later what the test output was.
        StringWriter sw = new StringWriter();
        var output = new PrintWriter(sw);

        // Mock objects - these implement interfaces, and they do nothing yet.
        IService mockService = Mockito.mock(IService.class);
        Closeable mockSocket = Mockito.mock(Closeable.class);
        // create the worker with input, output and the mock objects - nothing is real here, we test the CW in isolation.
        ClientWorker cw = new ClientWorker(mockService, mockSocket, input, output);
        Thread t = new Thread(cw);
        t.start();
        try {
            Thread.sleep(1000);
        }
        catch (Exception ignored) {}
        cw.stop();
        try {
            t.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        Mockito.verify(mockService).savePackage("pack", "a", "b", "des", 1F, false);

        // now we assert the output
        // we read line by line from the response object
        String response = new String(sw.getBuffer());

        StringReader responseReader = new StringReader(response);
        var responseInput = new BufferedReader(responseReader);
        var responseLine = responseInput.readLine();
        Gson gson = new Gson();
        // was the response ok?
        var responseData = gson.fromJson(responseLine, Response.class);
        assertEquals(responseData.getType(), ResponseType.OK);
    }
}
