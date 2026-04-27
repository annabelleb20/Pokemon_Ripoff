import java.sql.*;

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
            seedData();

        } catch (SQLException e) {
            System.err.println("Database connection failed :(" + e.getMessage());
        }
    }

    /**
     * gets the instance of DatabaseManager
     * @return DatabaseManager instance
     */
    public static DatabaseManager getInstance(){
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Creates the tables - should be mildly self-explanatory (I hope)
     */
    private void createTables(){
        String sql = """
                CREATE TABLE IF NOT EXISTS user (
                    userId      INTEGER PRIMARY KEY AUTOINCREMENT,
                    name    TEXT    NOT NULL,
                    pass    TEXT    NOT NULL,
                    isAdmin INTEGER NOT NULL DEFAULT 0
                )
                """;
        String sql2 = """
                CREATE TABLE IF NOT EXISTS teams (
                    teamId      INTEGER PRIMARY KEY AUTOINCREMENT,
                    userId      INTEGER,
                    teamName    TEXT    NOT NULL,
                    exportCode  TEXT    NOT NULL,
                    FOREIGN KEY (userId) REFERENCES user(userId)
                )
                """;
        String sql3 = """
                CREATE TABLE IF NOT EXISTS pokemon (
                    pokemonId   INTEGER PRIMARY KEY,
                    pName       TEXT    NOT NULL,
                    type        TEXT    NOT NULL,
                    attack      INTEGER,
                    spAttack    INTEGER,
                    defence     INTEGER,
                    spDefence   INTEGER
                )
                """;
        String sql4 = """
                CREATE TABLE IF NOT EXISTS teamSlots (
                    slotId  INTEGER PRIMARY KEY,
                    teamId  INTEGER,
                    pokemonId   INTEGER,
                    slotNum INTEGER,
                    FOREIGN KEY (teamId) REFERENCES teams(teamId),
                    FOREIGN KEY (pokemonId) REFERENCES pokemon(pokemonId)
                )
                """;
        try(Statement stmt = connection.createStatement()){
            stmt.execute(sql);
            stmt.execute(sql2);
            stmt.execute(sql3);
            stmt.execute(sql4);
        } catch (SQLException e) {
            System.err.println("CreateTables failed: " + e.getMessage());
        }
    }

    /**
     * SHOULD add new user data into table
     * @param name - username (string)
     * @param pass - password (string)
     */
    public void newUser(String name, String pass){
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
     * SHOULD check to see if a username/password is valid...
     * @param username - username (string)
     * @param password - password (string)
     * @return - user ID if valid (-1 if not)
     */
    public int readUser(String username, String password){
        String sql = "SELECT * FROM user WHERE name = ? AND pass = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                return rs.getInt("userId");
            } else{
                return -1;
            }
        } catch (SQLException e) {
            System.err.println("GetUsers Failed : " + e.getMessage());
            return -1;
        }
    }

    /**
     * Updates username
     * @param newUser - new username (string)
     * @param id - user ID of user being changed (int)
     */
    public void updateUsername(String newUser, int id){
        String sql = "UPDATE user SET name = ? WHERE userId = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, newUser);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("updateUsername failed: " + e.getMessage());
        }
    }
    /**
     * Updates password
     * @param newPass - new password (string)
     * @param id - user ID of pass being changed (int)
     */
    public void updatePassword(String newPass, int id){
        String sql = "UPDATE user SET pass = ? WHERE userId = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, newPass);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("updateUsername failed: " + e.getMessage());
        }
    }

    /**
     * Gets user ID and deletes the user idk y'all are adults you'll figure it out
     * @param id - user ID
     */
    public void deleteUser(int id){
        String sql = "DELETE FROM user WHERE userId = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, id);
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

    public Connection getConnection(){
        return connection;
    }

    private void seedData() {
        try (Statement stmt = connection.createStatement()) {

            stmt.executeUpdate("INSERT OR IGNORE INTO pokemon (pokemonId, pName, type, attack, spAttack, defence, spDefence) VALUES (1, 'Pikachu', 'ELECTRIC', 55, 50, 40, 50)");
            stmt.executeUpdate("INSERT OR IGNORE INTO pokemon (pokemonId, pName, type, attack, spAttack, defence, spDefence) VALUES (2, 'Charizard', 'FIRE', 84, 109, 78, 85)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertPokemon(int id, String name, String type, int attack, int spAttack, int defense, int spDefense) {
        String sql = "INSERT INTO pokemon VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, type);
            pstmt.setInt(4, attack);
            pstmt.setInt(5, spAttack);
            pstmt.setInt(6, defense);
            pstmt.setInt(7, spDefense);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
