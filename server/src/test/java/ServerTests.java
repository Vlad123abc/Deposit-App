import deposit.domain.Package;
import deposit.domain.User;
import deposit.repository.*;
import deposit.server.Service;
import deposit.service.IObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTests {
    // After each test, close the session factory to ensure a clean slate
    @AfterEach
    public void tearDown() {
        HibernateUtils.closeSessionFactory();
    }

    private Service createTestService()
    {
        var props = new Properties();
        props.setProperty("jdbc.driver", "org.sqlite.JDBC");
        props.setProperty("jdbc.url", "jdbc:sqlite::memory:");
        JdbcUtils dbUtils = new JdbcUtils(props);
        assertNotNull(dbUtils);
        Connection conn = dbUtils.getConnection();

        UserRepository userRepository = new UserDBRepo(conn);
        PackageRepository packageRepository = new PackageDBRepo(conn);

        userRepository.save(new User("vlad", "parola1"));
        userRepository.save(new User("mark", "parola2"));
        userRepository.save(new User("emma", "parola3"));

        packageRepository.save(new Package("pack1", "vlad", "mark", "medium", 10F, false));
        packageRepository.save(new Package("pack2", "mark", "emma", "little", 5F, true));
        packageRepository.save(new Package("pack3", "emma", "vlad", "big", 15F, false));

        return new Service(userRepository, packageRepository);
    }

    @Test
    public void userLoginTest() throws SQLException
    {
        Service service = this.createTestService();
        IObserver client = new IObserver() {
            @Override
            public void packageSaved(Package pack) {}
            @Override
            public void packageUpdated(Package pack) {}
            @Override
            public void packageDeleted(Long id) {}
        };

        try {
            service.login("vlad", "parola1", client);
        }
        catch (Exception e) {
            System.out.println("Exception caught: " + e.getMessage());
            assertEquals(e.getMessage(), "User already logged in.");
        }

        try {
            service.login("vlad", "parolaaa", client);
            fail();
        }
        catch (Exception e) {
            System.out.println("Exception caught: " + e.getMessage());
            assertEquals(e.getMessage(), "Authentication failed.");
        }

        try {
            service.login("vlad", "parola1", client);
            fail();
        }
        catch (Exception e) {
            System.out.println("Exception caught: " + e.getMessage());
            assertEquals(e.getMessage(), "User already logged in.");
        }
    }

    @Test
    public void userLogoutTest() throws Exception
    {
        Service service = this.createTestService();
        IObserver client = new IObserver() {
            @Override
            public void packageSaved(Package pack) {}
            @Override
            public void packageUpdated(Package pack) {}
            @Override
            public void packageDeleted(Long id) {}
        };

        try {
            User user = new User("vlad", "parola1");
            user.setId(1L);
            service.logout(user, client);
            fail();
        }
        catch (Exception e) {
            System.out.println("Exception caught: " + e.getMessage());
            assertEquals(e.getMessage(), "User vlad is not logged in.");
        }

        service.login("vlad", "parola1", client);

        try {
            User user = new User("vlad", "parola1");
            user.setId(1L);
            service.logout(user, client);
        }
        catch (Exception e) {
            fail();
        }
    }

    @Test
    void getAllPackagesTest(){
        Service service = this.createTestService();

        var packages = service.getAllPackages();
        assertEquals(3, packages.size());

        assertEquals("pack1", packages.get(0).getName());
        assertEquals("pack2", packages.get(1).getName());
        assertEquals("pack3", packages.get(2).getName());
    }
}
