package deposit.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.util.Properties;

public class HibernateUtils {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(Connection connection){
        if ((sessionFactory==null)||(sessionFactory.isClosed()))
            sessionFactory=createNewSessionFactory(connection);
        return sessionFactory;
    }

    private static  SessionFactory createNewSessionFactory(Connection connection){
        sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();
        return sessionFactory;
    }

    public static  void closeSessionFactory(){
        if (sessionFactory!=null)
            sessionFactory.close();
    }

    // New method to create a SessionFactory with a custom JDBC URL
    public static SessionFactory createSessionFactoryWithCustomUrl(String jdbcUrl) {
        Configuration configuration = new Configuration();
        configuration.configure();  // Loads settings from hibernate.cfg.xml

        // Override the connection URL property
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.url", jdbcUrl);
        configuration.addProperties(properties);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }
}
