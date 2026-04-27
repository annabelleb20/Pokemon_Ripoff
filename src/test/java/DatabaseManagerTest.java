import org.junit.jupiter.api.Test;

import java.util.List;

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

    /**
     * Tests if Team table can create, read, update, and delete
     */
    @Test
    void testTeamCRUD(){
        DatabaseManager db = DatabaseManager.getInstance();
        db.newUser("user1234", "password1234");
        int userId = db.readUser("user1234", "password1234");

        db.newTeam(userId, "teamName1234");
        db.updateExport("export123", db.getTeamId(userId));
        List<String> team = db.readTeamInfo(userId);
        assertEquals(db.getTeamId(userId), Integer.parseInt(team.get(0)));
        assertEquals("export123", team.get(3));

        db.deleteTeam(db.getTeamId(userId));
        assertEquals(-1, db.getTeamId(userId));
    }
}
