import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    @BeforeEach
    void freshDb2(){
        System.setProperty("app.db.url", "jdbc:sqlite::memory:");
        DatabaseManager.resetForTesting();
    }

    @AfterEach
    void teardown(){
        DatabaseManager.resetForTesting();
    }

    @Test
    @DisplayName("Test getInstance returns same instance")
    void testSingleton(){
        DatabaseManager one = DatabaseManager.getInstance();
        DatabaseManager two = DatabaseManager.getInstance();
        assertSame(one, two);
    }

    @Test
    @DisplayName("Test the creation of a new user")
    void testNewUser(){
        DatabaseManager db = DatabaseManager.getInstance();

        db.newUser("user1234","password1234");

        int result = db.readUser("user1234","password1234");

        assertNotEquals(-1, result);
    }

    @Test
    @DisplayName("Test the creation of a new admin")
    void testNewAdmin(){
        DatabaseManager db = DatabaseManager.getInstance();

        db.newAdmin("user1234","password1234");

        int result = db.readUser("user1234","password1234");

        assertNotEquals(-1, result);
    }

    @Test
    @DisplayName("Test reading admin")
    void testReadAdmin(){
        DatabaseManager db = DatabaseManager.getInstance();

        db.newUser("notAdmin","pass");
        int userResult = db.readUser("notAdmin","pass");
        assertFalse(db.isAdmin(userResult));

        db.newAdmin("isadmin","pass");
        int adminResult = db.readUser("isadmin","pass");
        assertTrue(db.isAdmin(adminResult));
    }

    @Test
    @DisplayName("Test reading a user")
    void testReadUser(){
        DatabaseManager db = DatabaseManager.getInstance();

        db.newUser("user1234","password1234");

        int ID = db.readUser("user1234","password1234");

        List<String> info = db.readUserInfo(ID);

        assertEquals(ID, Integer.parseInt(info.get(0)));
        assertEquals("user1234", info.get(1));
        assertEquals("password1234", info.get(2));
        assertEquals(0, Integer.parseInt(info.get(3)));
    }

    @Test
    @DisplayName("Test updating a user")
    void testUpdateUser(){
        DatabaseManager db = DatabaseManager.getInstance();

        db.newUser("user1234","password1234");
        int ID = db.readUser("user1234","password1234");

        db.updateUsername("newUsername", ID);
        db.updatePassword("newPassword", ID);

        List<String> info = db.readUserInfo(ID);

        assertTrue(info.contains("newUsername"));
        assertTrue(info.contains("newPassword"));
    }

    @Test
    @DisplayName("Test deleting a user")
    void testDeleteUser(){
        DatabaseManager db = DatabaseManager.getInstance();

        db.newUser("user1234","password1234");
        int ID = db.readUser("user1234","password1234");

        db.deleteUser(ID);

        assertEquals(-1, db.readUser("user1234","password1234"));
    }

    @Test
    @DisplayName("Test creating a team")
    void testNewTeam(){
        DatabaseManager db = DatabaseManager.getInstance();

        db.newUser("user1234","password1234");
        int ID = db.readUser("user1234","password1234");

        db.newTeam(ID, "teamName1234");

        List<String> team = db.readTeamInfo(ID);

        assertEquals(String.valueOf(db.getTeamId(ID)), team.get(0));
    }

    @Test
    @DisplayName("Test reading a team")
    void testReadTeam(){
        DatabaseManager db = DatabaseManager.getInstance();

        db.newUser("user1234","password1234");
        int ID = db.readUser("user1234","password1234");

        db.newTeam(ID, "teamName1234");

        int teamId = db.getTeamId(ID);
        List<String> team = db.readTeamInfo(ID);

        assertEquals(teamId, Integer.parseInt(team.get(0)));
        assertEquals(ID, Integer.parseInt(team.get(1)));
        assertEquals("teamName1234", team.get(2));
        assertEquals("NoCode", team.get(3));
    }

    @Test
    @DisplayName("Test updating a team")
    void testUpdateTeam(){
        DatabaseManager db = DatabaseManager.getInstance();

        db.newUser("user1234","password1234");
        int ID = db.readUser("user1234","password1234");

        db.newTeam(ID, "teamName1234");

        int teamID = db.getTeamId(ID);
        db.updateExport("newExport", teamID);

        assertTrue(db.readTeamInfo(ID).contains("newExport"));
    }

    @Test
    @DisplayName("Test deleting a team")
    void testDeleteTeam(){
        DatabaseManager db = DatabaseManager.getInstance();

        db.newUser("user1234","password1234");
        int ID = db.readUser("user1234","password1234");

        db.newTeam(ID, "teamName1234");

        int teamID = db.getTeamId(ID);
        db.deleteTeam(teamID);

        assertEquals(-1, db.getTeamId(ID));
    }
}