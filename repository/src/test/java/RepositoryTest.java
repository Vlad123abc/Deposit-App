import deposit.domain.User;
import deposit.repository.JdbcUtils;
import deposit.repository.UserDBRepo;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RepositoryTest {
    private Connection createTestConnection() {
        var props = new Properties();
        props.setProperty("jdbc.driver", "org.sqlite.JDBC");
        props.setProperty("jdbc.url", "jdbc:sqlite::memory:");
        JdbcUtils dbUtils = new JdbcUtils(props);
        assertNotNull(dbUtils);
        return dbUtils.getConnection();
    }

    @Test
    public void userRepoSmokeTest()  throws SQLException {
        try (Connection conn = createTestConnection()) {
            UserDBRepo repo = new UserDBRepo(conn);
            assertNotNull(repo);
        }
    }

    @Test
    public void UserRepo_getAllTest() throws SQLException
    {
        try (Connection conn = createTestConnection())
        {
            UserDBRepo repo = new UserDBRepo(conn);
            assertNotNull(repo);

            repo.save(new User("vlad", "parola1"));
            repo.save(new User("mark", "parola2"));
            repo.save(new User("emma", "parola3"));

            var users = repo.getAll();

            assertEquals(3, users.size());
        }
    }
}
