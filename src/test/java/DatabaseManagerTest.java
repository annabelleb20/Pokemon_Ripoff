import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {
    @Test
    void testNewUser(){
        DatabaseManager db = DatabaseManager.getInstance();

        String username = "user1234";
        String password = "password1234";

        db.newUser(username,password);

        int result = db.readUser(username, password);

        assertNotEquals(-1, result);
    }
}
