import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TableviewDisplayControllerTest {

    @BeforeEach
    void setup() {
        // MUST set property before reset so new instance uses memory DB
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

        // insert a pokemon directly into DB
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
}