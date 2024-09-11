import deposit.domain.User;
import deposit.repository.HibernateUtils;
import deposit.repository.JdbcUtils;
import deposit.repository.UserDBRepo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTest {
    // After each test, close the session factory to ensure a clean slate
    @AfterEach
    public void tearDown() {
        HibernateUtils.closeSessionFactory();
    }

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

            User user1 = new User("vlad", "parola1");
            User user2 = new User("mark", "parola2");
            User user3 = new User("emma", "parola3");

            repo.save(user1);
            repo.save(user2);
            repo.save(user3);

            assertEquals(3, repo.getAll().size());

            assertEquals("parola1", repo.getByUsername("vlad").getPassword());

            user1.setPassword("abcd");
            repo.update(user1);

            assertEquals("abcd", repo.getByUsername("vlad").getPassword());

            repo.delete(1L);

            assertEquals(2, repo.getAll().size());

            assertNull(repo.getById(1L));
            assertNotNull(repo.getById(2L));
            assertNotNull(repo.getById(3L));
        }
    }

    @Test
    public void UserRepo_getByIdTest() throws SQLException
    {
        try (Connection conn = createTestConnection())
        {
            UserDBRepo repo = new UserDBRepo(conn);
            assertNotNull(repo);

            User user1 = new User("vlad", "parola1");
            User user2 = new User("mark", "parola2");
            User user3 = new User("emma", "parola3");

            repo.save(user1);
            repo.save(user2);
            repo.save(user3);

            assertEquals(user1, repo.getById(1L));
            assertEquals(user2, repo.getById(2L));
            assertEquals(user3, repo.getById(3L));
        }
    }

    @Test
    public void UserRepo_getByUsernameTest() throws SQLException
    {
        try (Connection conn = createTestConnection())
        {
            UserDBRepo repo = new UserDBRepo(conn);
            assertNotNull(repo);

            User user1 = new User("vlad", "parola1");
            User user2 = new User("mark", "parola2");
            User user3 = new User("emma", "parola3");

            repo.save(user1);
            repo.save(user2);
            repo.save(user3);

            assertEquals(user1, repo.getByUsername("vlad"));
            assertEquals(user2, repo.getByUsername("mark"));
            assertEquals(user3, repo.getByUsername("emma"));
        }
    }
}
