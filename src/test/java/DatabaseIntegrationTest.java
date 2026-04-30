/*
Commented out this file cuz I'm pretty sure DatabaseManagerTest does the same thing ://

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseIntegrationTest {

    @Test
    public void testPokemonInsertAndRead() {

        DatabaseManager db = DatabaseManager.getInstance();

        db.insertPokemon(999, "TestMon", "NORMAL", 10, 10, 10, 10);

        try {
            var conn = db.getConnection();
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery("SELECT * FROM pokemon WHERE pokemonId = 999");

            assertTrue(rs.next());
            assertEquals("TestMon", rs.getString("pName"));

        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }
}
 */