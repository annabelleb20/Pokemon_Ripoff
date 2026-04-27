import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {
    @BeforeEach
    void freshDb(){
        System.setProperty("app.db.url", "jdbc:sqlite::memory:");
        DatabaseManager.resetForTesting();
    }

    @AfterEach
    void teardown(){
        DatabaseManager.resetForTesting();
    }

    @Test
    @DisplayName("Test the creation of a new user")
    void testNewUser(){
        DatabaseManager db = DatabaseManager.getInstance();

        String username = "testrun";
        String password = "password1234";

        db.newUser(username,password);

        int result = db.readUser(username, password);

        assertNotEquals(-1, result);
    }

    @Test
    @DisplayName("Test updating a user")
    void testUpdateUser(){
        DatabaseManager db = DatabaseManager.getInstance();

        String username = "user1234";
        String password = "password1234";

        db.newUser(username,password);
        int ID = db.readUser(username, password);

        db.updateUsername("newUsername", ID);
        db.updatePassword("newPassword", ID);

        assertFalse(db.readUserInfo(ID).contains("user1234"));
        assertFalse(db.readUserInfo(ID).contains("password1234"));
        assertTrue(db.readUserInfo(ID).contains("newUsername"));
        assertTrue(db.readUserInfo(ID).contains("newPassword"));
    }

    @Test
    @DisplayName("Test deleting a user")
    void testDeleteUser(){
        DatabaseManager db = DatabaseManager.getInstance();

        String username = "user1234";
        String password = "password1234";

        db.newUser(username,password);
        int ID = db.readUser(username, password);

        db.deleteUser(ID);
        assertEquals(-1, db.readUser(username, password));
    }

    @Test
    @DisplayName("Test creating a team")
    void testTeamCRUD(){
        DatabaseManager db = DatabaseManager.getInstance();

        String username = "user1234";
        String password = "password1234";

        db.newUser(username, password);
        int ID = db.readUser(username, password);

        db.newTeam(ID, "teamName1234");
        List<String> team = db.readTeamInfo(ID);
        assertEquals(db.getTeamId(ID), Integer.parseInt(team.get(0)));
    }
}
