package deposit.restServices;

import deposit.server.Service;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    private Service service;

    public void setService(Service service) {
        this.service = service;
    }
}
