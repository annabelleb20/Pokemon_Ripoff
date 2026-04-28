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

        String username = "user1234";
        String password = "password1234";

        db.newUser(username,password);

        int result = db.readUser(username, password);

        assertNotEquals(-1, result);
    }

    @Test
    @DisplayName("Test reading a user")
    void testReadUser(){
        DatabaseManager db = DatabaseManager.getInstance();

        String username = "user1234";
        String password = "password1234";

        db.newUser(username,password);

        int ID = db.readUser(username, password);
        assertNotEquals(-1, ID);

        List<String> info = db.readUserInfo(ID);

        // Tests if each information is in the spot it should be
        assertEquals(ID, Integer.parseInt(info.get(0)));
        assertEquals(username, info.get(1));
        assertEquals(password, info.get(2));
        assertEquals(0, Integer.parseInt(info.get(3)));
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
    void testNewTeam(){
        DatabaseManager db = DatabaseManager.getInstance();

        String username = "user1234";
        String password = "password1234";

        db.newUser(username, password);
        int ID = db.readUser(username, password);

        db.newTeam(ID, "teamName1234");
        List<String> team = db.readTeamInfo(ID);
        assertEquals(db.getTeamId(ID), Integer.parseInt(team.get(0)));
    }

    @Test
    @DisplayName("Test reading a team")
    void testReadTeam(){
        DatabaseManager db = DatabaseManager.getInstance();

        String username = "user1234";
        String password = "password1234";

        db.newUser(username, password);
        int ID = db.readUser(username, password);

        db.newTeam(ID, "teamName1234");
        int teamId = db.getTeamId(ID);
        assertNotEquals(-1, teamId);

        List<String> team = db.readTeamInfo(ID);

        // Tests if each information is in the spot it should be
        assertEquals(teamId, Integer.parseInt(team.get(0)));
        assertEquals(ID, Integer.parseInt(team.get(1)));
        assertEquals("teamName1234", team.get(2));
        assertEquals("NoCode", team.get(3));
    }

    @Test
    @DisplayName("Test updating a team")
    void testUpdateTeam(){
        DatabaseManager db = DatabaseManager.getInstance();

        String username = "user1234";
        String password = "password1234";

        db.newUser(username, password);
        int ID = db.readUser(username, password);

        db.newTeam(ID, "teamName1234");
        int teamID = db.getTeamId(ID);
        db.updateExport("newExport", teamID);

        assertTrue(db.readTeamInfo(ID).contains("newExport"));
    }

    @Test
    @DisplayName("Test deleting a team")
    void testDeleteTeam(){
        DatabaseManager db = DatabaseManager.getInstance();

        String username = "user1234";
        String password = "password1234";

        db.newUser(username, password);
        int ID = db.readUser(username, password);

        db.newTeam(ID, "teamName1234");
        int teamID = db.getTeamId(ID);

        db.deleteTeam(teamID);

        assertEquals(-1, db.getTeamId(ID));
    }

    @Test
    @DisplayName("Test creating a pokemon")
    void testNewPokemon(){
        DatabaseManager db = DatabaseManager.getInstance();

        db.newPokemon(123, "name", "fire", "rock", 12.0, 12, 12, 12, 12);

        assertNotEquals(-1, db.getPokemonId("name"));
    }

    @Test
    @DisplayName("Test reading a pokemon")
    void testReadPokemon(){
        DatabaseManager db = DatabaseManager.getInstance();

        db.newPokemon(123, "name", "fire", "rock", 12.0, 13, 14, 15, 16);

        int pkID = db.getPokemonId("name");
        assertNotEquals(-1, pkID);

        List<String> info = db.readPokemonInfo(pkID);

        // Tests if each info is in the spot it should be
        assertEquals(pkID, Integer.parseInt(info.get(0)));
        assertEquals("name", info.get(1));
        assertEquals("fire", info.get(2));
        assertEquals("rock", info.get(3));
        assertEquals(12.0, Double.parseDouble(info.get(4)));
        assertEquals(13, Integer.parseInt(info.get(5)));
        assertEquals(14, Integer.parseInt(info.get(6)));
        assertEquals(15, Integer.parseInt(info.get(7)));
        assertEquals(16, Integer.parseInt(info.get(8)));
    }

    @Test
    @DisplayName("Test updating a pokemon")
    void testUpdatePokemon(){
        DatabaseManager db = DatabaseManager.getInstance();

        db.newPokemon(123, "name", "fire", "rock", 12.0, 13, 14, 15, 16);

        db.updatePokemon("pName", "newName", 123);
        db.updatePokemon("attack", 25, 123);

        assertTrue(db.readPokemonInfo(123).contains("newName"));
        assertTrue(db.readPokemonInfo(123).contains("25"));
    }

    @Test
    @DisplayName("Test deleting a pokemon")
    void testDeletePokemon(){
        DatabaseManager db = DatabaseManager.getInstance();

        db.newPokemon(123, "name", "fire", "rock", 12.0, 12, 12, 12, 12);

        db.deletePokemon(123);

        assertEquals(-1, db.getPokemonId("name"));
    }

    @Test
    @DisplayName("Test new team slot")
    void testNewTeamSlot(){
        DatabaseManager db = DatabaseManager.getInstance();

        String username = "user1234";
        String password = "password1234";

        db.newUser(username, password);
        int ID = db.readUser(username, password);

        db.newTeam(ID, "teamName1234");
        db.newPokemon(123, "name", "fire", "rock", 12.0, 12, 12, 12, 12);

        db.newTeamSlot(1, db.getTeamId(ID), 123, 1);
        assertNotEquals(-1, db.getSlotId(db.getTeamId(ID)));
    }

    @Test
    @DisplayName("Test reading a team slot")
    void testReadTeamSlot(){
        DatabaseManager db = DatabaseManager.getInstance();

        String username = "user1234";
        String password = "password1234";

        db.newUser(username, password);
        int ID = db.readUser(username, password);

        db.newTeam(ID, "teamName1234");
        db.newPokemon(123, "name", "fire", "rock", 12.0, 12, 12, 12, 12);

        db.newTeamSlot(1, db.getTeamId(ID), 123, 1);
        int slotID = db.getSlotId(db.getTeamId(ID));
        assertNotEquals(-1, slotID);

        List<Integer> info = db.readSlotInfo(db.getTeamId(ID));

        //Tests if all the info is in the right spots
        assertEquals(slotID, info.get(0));
        assertEquals(db.getTeamId(ID), info.get(1));
        assertEquals(123, info.get(2));
        assertEquals(1, info.get(3));
    }

    @Test
    @DisplayName("Test updating team slot")
    void testUpdateTeamSlot(){
        DatabaseManager db = DatabaseManager.getInstance();

        String username = "user1234";
        String password = "password1234";

        db.newUser(username, password);
        int ID = db.readUser(username, password);

        db.newTeam(ID, "teamName1234");
        db.newPokemon(123, "name", "fire", "rock", 12.0, 12, 12, 12, 12);

        db.newTeamSlot(1, db.getTeamId(ID), 123, 1);
        db.updateTeamSlots("slotId", 2, 1);

        assertEquals(2, db.getSlotId(db.getTeamId(ID)));
    }

    @Test
    @DisplayName("Test deleting team slot")
    void testDeleteTeamSlot(){
        DatabaseManager db = DatabaseManager.getInstance();

        String username = "user1234";
        String password = "password1234";

        db.newUser(username, password);
        int ID = db.readUser(username, password);

        db.newTeam(ID, "teamName1234");
        db.newPokemon(123, "name", "fire", "rock", 12.0, 12, 12, 12, 12);

        db.newTeamSlot(1, db.getTeamId(ID), 123, 1);
        db.deleteTeamSlot(db.getSlotId(db.getTeamId(ID)));

        assertEquals(-1, db.getSlotId(db.getTeamId(ID)));
    }
}
