package deposit.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private Properties props;
    public JdbcUtils(Properties props){
        this.props=props;
    }

    private static final Logger logger = LogManager.getLogger();

    private Connection getNewConnection() {
        logger.traceEntry();

        String driver = props.getProperty("jdbc.driver");
        String url = props.getProperty("jdbc.url");

        logger.info("trying to connect to database ... {}", url);

        Connection con = null;

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url);
        }
        catch (ClassNotFoundException e) {
            logger.error(e);
            System.out.println("Error loading driver " + e);
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting connection " + e);
        }

        return con;
    }

    public Connection getConnection()
    {
        return getNewConnection();
    }
}
