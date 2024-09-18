package deposit.restServices;

import deposit.repository.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

@Configuration
public class RepositoryConfig {
    @Bean
    public Properties serverProperties() {
        Properties properties = new Properties();
        try {
            properties.load(RepositoryConfig.class.getResourceAsStream("/server.props"));
            System.out.println("Server properties set");
            properties.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties! " + e);
        }
        return properties;
    }

    @Bean
    public Connection connection(Properties serverProperties) {
        // Create a new instance of JdbcUtils with the loaded properties
        JdbcUtils jdbcUtils = new JdbcUtils(serverProperties);
        return jdbcUtils.getConnection();
    }

    @Bean
    public UserRepository userRepository(Connection connection) {
        // Create a new instance of UserDBRepo, passing the Connection object
        return new UserDBRepo(connection);
    }
    @Bean
    public PackageRepository packageRepository(Connection connection) {
        // Create a new instance of UserDBRepo, passing the Connection object
        return new PackageDBRepo(connection);
    }
}
