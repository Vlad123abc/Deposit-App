package deposit.restServices;

import deposit.server.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestServer {
    static Service serviceSingleton;

    public void start(Service service) {
        serviceSingleton = service;
        Thread t = new Thread(new Runnable() {
            public void run() {
                String[] args = {"server.port", "10333"};
                runApp(args);
            }
        });
        t.start();
    }

    public static void runApp(String[] args) {
        // Register the existing object as a bean
        SpringApplication.run(RestServer.class, args);
    }

    @Bean
    public Service getService(){
        return serviceSingleton;
    }
}
