import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TableviewDisplayControllerTest {

    @BeforeEach
    void setup() {
        System.setProperty("app.db.url", "jdbc:sqlite::memory:");
        DatabaseManager.resetForTesting();
    }

    @AfterEach
    void teardown() {
        DatabaseManager.resetForTesting();
    }

    @Test
    @DisplayName("Test that pokemon table is empty on fresh DB")
    void testTableIsEmptyOnFreshDB() {
        DatabaseManager db = DatabaseManager.getInstance();

        ObservableList<Pokemon> data = FXCollections.observableArrayList();

        try {
            java.sql.Connection conn = db.getConnection();
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery("SELECT * FROM pokemon");

            while (rs.next()) {
                Pokemon p = new Pokemon();
                p.setPoke_name(rs.getString("pName"));
                p.setAttack(rs.getInt("attack"));
                data.add(p);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            fail("Exception during data load: " + e.getMessage());
        }

        assertEquals(0, data.size());
    }

    @Test
    @DisplayName("Test that pokemon saved to DB appears in table data")
    void testPokemonAppearsInTableAfterSave() {
        DatabaseManager db = DatabaseManager.getInstance();

        db.newPokemon(1, "bulbasaur", "GRASS", "POISON", 45.0, 49, 65, 49, 65);

        ObservableList<Pokemon> data = FXCollections.observableArrayList();

        try {
            java.sql.Connection conn = db.getConnection();
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery("SELECT * FROM pokemon");

            while (rs.next()) {
                Pokemon p = new Pokemon();
                p.setPoke_name(rs.getString("pName"));
                p.setAttack(rs.getInt("attack"));

                String type = rs.getString("type");
                try {
                    p.setPrimaryType(Poke_Type.valueOf(type));
                } catch (Exception e) {
                    p.setPrimaryType(Poke_Type.NORMAL);
                }

                data.add(p);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            fail("Exception during data load: " + e.getMessage());
        }

        assertEquals(1, data.size());
        assertEquals("bulbasaur", data.get(0).getPoke_name());
        assertEquals(49, data.get(0).getAttack());
        assertEquals(Poke_Type.GRASS, data.get(0).getPrimaryType());
    }

    @Test
    @DisplayName("Test that multiple pokemon saved to DB all appear in table data")
    void testMultiplePokemonAppearInTable() {
        DatabaseManager db = DatabaseManager.getInstance();

        // insert 3 pokemon
        db.newPokemon(1, "bulbasaur", "GRASS", "POISON", 45.0, 49, 65, 49, 65);
        db.newPokemon(4, "charmander", "FIRE", "NONE", 39.0, 52, 60, 43, 50);
        db.newPokemon(7, "squirtle", "WATER", "NONE", 44.0, 48, 50, 65, 64);

        ObservableList<Pokemon> data = FXCollections.observableArrayList();

        try {
            java.sql.Connection conn = db.getConnection();
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery("SELECT * FROM pokemon");

            while (rs.next()) {
                Pokemon p = new Pokemon();
                p.setPoke_name(rs.getString("pName"));
                p.setAttack(rs.getInt("attack"));

                String type = rs.getString("type");
                try {
                    p.setPrimaryType(Poke_Type.valueOf(type));
                } catch (Exception e) {
                    p.setPrimaryType(Poke_Type.NORMAL);
                }

                data.add(p);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            fail("Exception during data load: " + e.getMessage());
        }

        // should have exactly 3 pokemon
        assertEquals(3, data.size());
        assertEquals("bulbasaur", data.get(0).getPoke_name());
        assertEquals("charmander", data.get(1).getPoke_name());
        assertEquals("squirtle", data.get(2).getPoke_name());
    }
}