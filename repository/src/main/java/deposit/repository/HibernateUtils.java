package deposit.repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.SQLException;

public class HibernateUtils {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(Connection connection){
        if ((sessionFactory==null)||(sessionFactory.isClosed()))
            sessionFactory=createNewSessionFactory(connection);
        return sessionFactory;
    }

//    private static  SessionFactory createNewSessionFactory(Connection connection){
//        sessionFactory = new Configuration()
//                .configure()
//                .buildSessionFactory();
//        return sessionFactory;
//    }

    private static SessionFactory createNewSessionFactory(Connection connection) {
        // Create Configuration object and configure it using hibernate.cfg.xml
        Configuration configuration = new Configuration();
        configuration.configure(); // Loads settings from hibernate.cfg.xml

        // Get the URL from the provided Connection object
        String jdbcUrl;
        try {
            jdbcUrl = connection.getMetaData().getURL();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get URL from connection", e);
        }

        // Override the connection URL with the URL from the provided Connection
        configuration.setProperty("hibernate.connection.url", jdbcUrl);

        // Build the SessionFactory with the overridden settings
        sessionFactory = configuration.buildSessionFactory();
        return sessionFactory;
    }

    public static  void closeSessionFactory(){
        if (sessionFactory!=null)
            sessionFactory.close();
    }
}
