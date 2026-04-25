import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    public static final String DB_URL = "jdbc:sqlite:app.db";
    private static DatabaseManager instance;
    private Connection connection;

    /**
     * Default constructor for DatabaseManager
     */
    private DatabaseManager(){
        try{
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database connected.");
            createTables();
        } catch (SQLException e) {
            System.err.println("Database connection failed :(" + e.getMessage());
        }
    }

    /**
     * Gets the instance of DatabaseManager
     * @return DatabaseManager instance
     */
    public static DatabaseManager getInstance(){
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Makes the Database tables .
     */
    private void createTables(){
        String sql = """
                CREATE TABLE IF NOT EXISTS user (
                    id      INTEGER PRIMARY KEY AUTOINCREMENT,
                    name    TEXT    NOT NULL,
                    pass    TEXT    NOT NULL,
                    isAdmin INTEGER NOT NULL DEFAULT 0
                )
                """;
        try(Statement stmt = connection.createStatement()){
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("CreateTables failed: " + e.getMessage());
        }
    }

    /**
     * SHOULD add new user data into table
     * @param name - username (string)
     * @param pass - password (string)
     */
    public void insertData(String name, String pass){
        String sql = "INSERT INTO user (name,pass) VALUES (?,?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, name);
            pstmt.setString(2, pass);
            pstmt.executeUpdate();
        } catch(SQLException e){
            System.err.println("InsertItem Failed: " + e.getMessage());
        }
    }

    /**
     * Ok there is prolly a way better way to do this but this SHOULD check if a user is valid.
     * Also checks if user is admin . Cuz that's important
     * @param username - username (string)
     * @param password - password (string)
     * @return - int (0 if not valid, 1 if valid, 2 if admin)
     */
    public int validUser(String username, String password){
        List<String> users = new ArrayList<>();
        String sql = "SELECT * FROM user WHERE name = ? AND pass = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                int isAdmin = rs.getInt("isAdmin");
                if(isAdmin == 1){
                    return 2;
                } else{
                    return 1;
                }
            } else{
                return 0;
            }
        } catch (SQLException e) {
            System.err.println("GetUsers Failed : " + e.getMessage());
            return 0;
        }
    }

    /*
    At some point we need an update user method for the CRUD stuff (update username/password thing?)
     */

    /**
     * Delete user method
     * Actually now that I think about it, I think a read method that returns the ID would be more useful.
     * Especially for something like this.
     * I'll deal w it later I'm tired...
     * (If I change any methods here I'll deal w fixing any issues that use any methods here)
     * @param username - username (String)
     */
    public void deleteUser(String username){
        String sql = "DELETE FROM user WHERE name = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("deleteUser failed: " + e.getMessage());
        }
    }

    /**
     * Closes ts
     */
    public void close(){
        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        instance = null;
    }
}
