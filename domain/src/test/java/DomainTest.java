import deposit.domain.Package;
import deposit.domain.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DomainTest {
    @Test
    public void userTest(){
        User user1 = new User();
        assertEquals("", user1.getUsername());
        assertEquals("", user1.getPassword());

        User user2 = new User("vlad", "parola");
        assertEquals("vlad", user2.getUsername());
        assertEquals("parola", user2.getPassword());
    }

    @Test
    public void packageTest(){
        Package pack1 = new Package();
        assertEquals("", pack1.getName());
        assertEquals("", pack1.getP_from());
        assertEquals("", pack1.getP_to());
        assertEquals("", pack1.getDescription());
        assertEquals(0F, pack1.getWeight());
        assertEquals(false, pack1.getFragile());

        Package pack2 = new Package("package", "vlad", "mark", "big", 10F, true);
        assertEquals("package", pack2.getName());
        assertEquals("vlad", pack2.getP_from());
        assertEquals("mark", pack2.getP_to());
        assertEquals("big", pack2.getDescription());
        assertEquals(10F, pack2.getWeight());
        assertEquals(true, pack2.getFragile());
    }
}
