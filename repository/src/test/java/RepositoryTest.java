import deposit.domain.Package;
import deposit.domain.User;
import deposit.repository.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
            UserRepository repo = new UserDBRepo(conn);
            assertNotNull(repo);
        }
    }

    @Test
    public void PackageRepoSmokeTest()  throws SQLException {
        try (Connection conn = createTestConnection()) {
            PackageRepository repo = new PackageDBRepo(conn);
            assertNotNull(repo);
        }
    }

    @Test
    public void UserRepo_getAllTest() throws SQLException
    {
        try (Connection conn = createTestConnection())
        {
            UserRepository repo = new UserDBRepo(conn);
            assertNotNull(repo);

            User user1 = new User("vlad", "parola1");
            User user2 = new User("mark", "parola2");
            User user3 = new User("emma", "parola3");

            repo.save(user1);
            repo.save(user2);
            repo.save(user3);

            assertEquals(3, repo.getAll().size());

            List<User> userList = repo.getAll();
            assertEquals(userList.get(0), user1);
            assertEquals(userList.get(1), user2);
            assertEquals(userList.get(2), user3);

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
            UserRepository repo = new UserDBRepo(conn);
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
            UserRepository repo = new UserDBRepo(conn);
            assertNotNull(repo);

            User user1 = new User("vlad", "parola1");
            User user2 = new User("mark", "parola2");
            User user3 = new User("emma", "parola3");
            User user4 = new User("vlad", "parola4");

            repo.save(user1);
            repo.save(user2);
            repo.save(user3);
            repo.save(user4);

            assertEquals(user1, repo.getByUsername("vlad"));
            assertEquals(user2, repo.getByUsername("mark"));
            assertEquals(user3, repo.getByUsername("emma"));
        }
    }

    @Test
    public void PackageRepo_getAllTest() throws SQLException
    {
        try (Connection conn = createTestConnection())
        {
            PackageRepository repo = new PackageDBRepo(conn);
            assertNotNull(repo);

            Package pack1 = new Package("pack1", "vlad", "mark", "big", 10F, false);
            Package pack2 = new Package("pack1", "mark", "emma", "small", 5F, true);
            Package pack3 = new Package("pack2", "emma", "vlad", "big", 15F, false);

            repo.save(pack1);
            repo.save(pack2);
            repo.save(pack3);

            assertEquals(3, repo.getAll().size());

            List<Package> packageList = repo.getAll();
            assertEquals(packageList.get(0), pack1);
            assertEquals(packageList.get(1), pack2);
            assertEquals(packageList.get(2), pack3);

            List<Package> packageList_pack1 = new ArrayList<>();
            packageList_pack1.add(pack1);
            packageList_pack1.add(pack2);
            assertEquals(packageList_pack1, repo.getByName("pack1"));

            pack1.setDescription("medium");
            repo.update(pack1);

            assertEquals(1L, pack1.getId());
            assertEquals("medium", repo.getById(1L).getDescription());

            repo.delete(1L);

            assertEquals(2, repo.getAll().size());

            assertNull(repo.getById(1L));
            assertNotNull(repo.getById(2L));
            assertNotNull(repo.getById(3L));
        }
    }

    @Test
    public void PackageRepo_getByIdTest() throws SQLException
    {
        try (Connection conn = createTestConnection())
        {
            PackageRepository repo = new PackageDBRepo(conn);
            assertNotNull(repo);

            Package pack1 = new Package("pack1", "vlad", "mark", "big", 10F, false);
            Package pack2 = new Package("pack1", "mark", "emma", "small", 5F, true);
            Package pack3 = new Package("pack2", "emma", "vlad", "big", 15F, false);

            repo.save(pack1);
            repo.save(pack2);
            repo.save(pack3);

            assertEquals(3, repo.getAll().size());

            assertEquals(pack1, repo.getById(1L));
            assertEquals(pack2, repo.getById(2L));
            assertEquals(pack3, repo.getById(3L));
        }
    }

    @Test
    void PackageRepo_GetByNameTest() throws SQLException
    {
        try (Connection conn = createTestConnection())
        {
            PackageRepository repo = new PackageDBRepo(conn);
            assertNotNull(repo);

            Package pack1 = new Package("a", "vlad", "mark", "big", 10F, false);
            Package pack2 = new Package("a", "mark", "emma", "small", 5F, true);
            Package pack3 = new Package("b", "emma", "vlad", "big", 15F, false);

            repo.save(pack1);
            repo.save(pack2);
            repo.save(pack3);

            List<Package> packageList_a = new ArrayList<>();
            packageList_a.add(pack1);
            packageList_a.add(pack2);

            List<Package> packageList_b = new ArrayList<>();
            packageList_b.add(pack3);

            assertEquals(packageList_a, repo.getByName("a"));
            assertEquals(packageList_b, repo.getByName("b"));
        }
    }

    @Test
    void PackageRepo_GetFromToTest() throws SQLException
    {
        try (Connection conn = createTestConnection())
        {
            PackageRepository repo = new PackageDBRepo(conn);
            assertNotNull(repo);

            Package pack1 = new Package("a", "a", "c", "big", 10F, false);
            Package pack2 = new Package("b", "a", "b", "small", 5F, true);
            Package pack3 = new Package("c", "c", "b", "big", 15F, false);

            repo.save(pack1);
            repo.save(pack2);
            repo.save(pack3);

            List<Package> packageList_from_a = new ArrayList<>();
            packageList_from_a.add(pack1);
            packageList_from_a.add(pack2);

            List<Package> packageList_to_b = new ArrayList<>();
            packageList_to_b.add(pack2);
            packageList_to_b.add(pack3);

            assertEquals(packageList_from_a, repo.getByFrom("a"));
            assertEquals(packageList_to_b, repo.getByTo("b"));
        }
    }
}
