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
}
