import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Annabelle Baltes
 * @since 4/26/2026
 */
public class DatabaseManager {
    public static String DB_URL = System.getProperty("app.db.url", "jdbc:sqlite:app.db");
    private static DatabaseManager instance;
    private Connection connection;

    /**
     * Default constructor for DatabaseManager
     */
    private DatabaseManager(){
        try{
            connection = DriverManager.getConnection(DB_URL);
            connection.createStatement().execute("PRAGMA foreign_keys = ON");
            System.out.println("Database connected.");
            createTables();
            //seedData();

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
                    exportCode  TEXT    DEFAULT 'NoCode',
                    FOREIGN KEY (userId) REFERENCES user(userId)
                )
                """;
        String sql3 = """
                CREATE TABLE IF NOT EXISTS pokemon (
                    pokemonId   INTEGER PRIMARY KEY,
                    pName       TEXT    NOT NULL,
                    type        TEXT    NOT NULL,
                    type2       TEXT    NOT NULL,
                    hp          REAL,
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
     * USER - Create
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
     * USER - Read 1
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
     * USER - Read 2
     * SHOULD check to see if a username/password is valid...
     * @param userId - user ID (int)
     * @return - List<String> with user data in (hopefully) the order they appear on the table
     */
    public List<String> readUserInfo(int userId){
        List<String> items = new ArrayList<>();
        String sql = "SELECT * FROM user WHERE userId = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                items.add(String.valueOf(rs.getInt("userId")));
                items.add(rs.getString("name"));
                items.add(rs.getString("pass"));
                items.add(String.valueOf(rs.getInt("isAdmin")));
            }
        } catch (SQLException e) {
            System.err.println("GetUsers Failed : " + e.getMessage());
        }
        return items;
    }

    /**
     * USER - Update 1
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
     * USER - Update 2
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
     * USER - Delete
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
     * TEAMS - Create
     * SHOULD add new team data , userID needs to be same as returned from readUser
     * @param userId - userId (foreign key returned from reading user table)
     * @param teamName - teamName (string)
     */
    public void newTeam(int userId, String teamName){
        String sql = "INSERT INTO teams (userId,teamName) VALUES (?,?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, userId);
            pstmt.setString(2, teamName);
            pstmt.executeUpdate();
        } catch(SQLException e){
            System.err.println("InsertItem Failed: " + e.getMessage());
        }
    }

    /**
     * TEAMS - Read 1
     * Reads and returns entire team row as a String list
     * This is for when u need export code or team name ideally I think
     * @param userId - userId (foreign key returned from reading user table)
     * @return - List<String> with team data in (hopefully) the order they appear on the table
     */
    public List<String> readTeamInfo(int userId){
        List<String> items = new ArrayList<>();
        String sql = "SELECT * FROM teams WHERE userId = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                items.add(String.valueOf(rs.getInt("teamId")));
                items.add(String.valueOf(rs.getInt("userId")));
                items.add(rs.getString("teamName"));
                items.add(rs.getString("exportCode"));
            }
        } catch (SQLException e) {
            System.err.println("GetUsers Failed : " + e.getMessage());
        }
        return items;
    }
    /**
     * TEAMS - Read 2
     * Similar to readUser, it checks if team is valid and returns teamId
     * @param userId - userId (foreign key returned from reading user table)
     * @return - TeamId if valid (-1 if not)
     */
    public int getTeamId(int userId){
        String sql = "SELECT * FROM teams WHERE userId = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, userId);

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
     * TEAMS - Update
     * Updates export code
     * @param exportCode - new exportCode (String)
     * @param teamId - team ID of team being changed (int)
     */
    public void updateExport(String exportCode, int teamId){
        String sql = "UPDATE teams SET exportCode = ? WHERE teamId = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, exportCode);
            pstmt.setInt(2, teamId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("updateUsername failed: " + e.getMessage());
        }
    }

    /**
     * TEAMS - Delete
     * Gets team ID and deletes team
     * @param teamId - team ID
     */
    public void deleteTeam(int teamId){
        String sql = "DELETE FROM teams WHERE teamId = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, teamId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("deleteUser failed: " + e.getMessage());
        }
    }



    /**
     * POKEMON - Create
     * Created under the intention that all items will be manually added to table idk
     * @param pkId - PokemonID (int)
     * @param pkName - Pokemon Name (String)
     * @param t1 - Pokemon main type (String)
     * @param t2 - Pokemon secondary type (String)
     * @param hp - Pokemon hp (Double)
     * @param Ak - Pokemon attack (int)
     * @param spAk - Pokemon special attack (int)
     * @param Df - Pokemon defence (int)
     * @param spDf - Pokemon special defence (int)
     */
    public void newPokemon(int pkId, String pkName, String t1, String t2, Double hp, int Ak, int spAk, int Df, int spDf){
        String sql = "INSERT INTO pokemon (pokemonId,pName,type,type2,hp,attack,spAttack,defence,spDefence) VALUES (?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, pkId);
            pstmt.setString(2, pkName);
            pstmt.setString(3, t1);
            pstmt.setString(4, t2);
            pstmt.setDouble(5, hp);
            pstmt.setInt(6, Ak);
            pstmt.setInt(7, spAk);
            pstmt.setInt(8, Df);
            pstmt.setInt(9, spDf);
            pstmt.executeUpdate();
        } catch(SQLException e){
            System.err.println("InsertItem Failed: " + e.getMessage());
        }
    }

    /**
     * POKEMON - Read 1
     * Reads and returns all Pokemon info as a String list
     * For outputting a Pokemon or smth
     * @param pkId - PokemonId (Int)
     * @return - List<String> with Pokemon data in (hopefully) the order they appear on the table
     */
    public List<String> readPokemonInfo(int pkId){
        List<String> items = new ArrayList<>();
        String sql = "SELECT * FROM pokemon WHERE pokemonId = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, pkId);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                items.add(String.valueOf(rs.getInt("pokemonId")));
                items.add(rs.getString("pName"));
                items.add(rs.getString("type"));
                items.add(rs.getString("type2"));
                items.add(String.valueOf(rs.getInt("hp")));
                items.add(String.valueOf(rs.getInt("attack")));
                items.add(String.valueOf(rs.getInt("spAttack")));
                items.add(String.valueOf(rs.getInt("defence")));
                items.add(String.valueOf(rs.getInt("spDefence")));
            }
        } catch (SQLException e) {
            System.err.println("GetUsers Failed : " + e.getMessage());
        }
        return items;
    }
    /**
     * POKEMON - Read 2
     * Similar to readUser, it checks if pokemon name is valid and returns pokemonId
     * @param pkName - Pokemon Name (String)
     * @return - PokemonId if valid (-1 if not)
     */
    public int getPokemonId(String pkName){
        String sql = "SELECT * FROM pokemon WHERE pName = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, pkName);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                return rs.getInt("pokemonId");
            } else{
                return -1;
            }
        } catch (SQLException e) {
            System.err.println("GetUsers Failed : " + e.getMessage());
            return -1;
        }
    }

    /**
     * POKEMON - Update 1
     * Updates info based on column entered (string version)
     * @param column - String (needs to be same as in the table)
     * @param newInfo - new information (String)
     * @param pkId - Pokemon Id (int)
     */
    public void updatePokemon(String column, String newInfo, int pkId){
        String sql = "UPDATE pokemon SET " + column +  " = ? WHERE pokemonId = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, newInfo);
            pstmt.setInt(2, pkId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("updateUsername failed: " + e.getMessage());
        }
    }
    /**
     * POKEMON - Update 2
     * Updates info based on column entered (int version)
     * @param column - String (needs to be same as in the table)
     * @param newInfo - new information (Int)
     * @param pkId - Pokemon Id (int)
     */
    public void updatePokemon(String column, int newInfo, int pkId){
        String sql = "UPDATE pokemon SET " + column +  " = ? WHERE pokemonId = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, newInfo);
            pstmt.setInt(2, pkId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("updateUsername failed: " + e.getMessage());
        }
    }

    /**
     * POKEMON - Delete
     * Gets pokemon ID and deletes pokemon
     * @param pkId - pokemon ID
     */
    public void deletePokemon(int pkId){
        String sql = "DELETE FROM pokemon WHERE pokemonId = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, pkId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("deleteUser failed: " + e.getMessage());
        }
    }



    /**
     * TEAMSLOTS - Create
     * Under the impression that everything is manual again
     * @param slotId - slotId (int)
     * @param teamId - teamId (foreign key from team table)
     * @param pkId - pokemonId (foreign key from pokemon table)
     * @param slotNum - slotNum (int)
     */
    public void newTeamSlot(int slotId, int teamId, int pkId, int slotNum){
        String sql = "INSERT INTO teamSlots (slotId,teamId,pokemonId,slotNum) VALUES (?,?,?,?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, slotId);
            pstmt.setInt(2, teamId);
            pstmt.setInt(3, pkId);
            pstmt.setInt(4, slotNum);
            pstmt.executeUpdate();
        } catch(SQLException e){
            System.err.println("InsertItem Failed: " + e.getMessage());
        }
    }

    /**
     * TEAMSLOTS - Read 1
     * Reads and returns entire teamSlot row as an Integer list
     * This is for when u need team slot info idk
     * @param teamId - teamId (foreign key returned from team table)
     * @return - List<Integer> with team slot data in (hopefully) the order they appear on the table
     */
    public List<Integer> readSlotInfo(int teamId){
        List<Integer> items = new ArrayList<>();
        String sql = "SELECT * FROM teamSlots WHERE teamId = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, teamId);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                items.add(rs.getInt("slotId"));
                items.add(rs.getInt("teamId"));
                items.add(rs.getInt("pokemonId"));
                items.add(rs.getInt("slotNum"));
            }
        } catch (SQLException e) {
            System.err.println("GetUsers Failed : " + e.getMessage());
        }
        return items;
    }
    /**
     * TEAMSLOTS - Read 2
     * Similar to readUser, it checks if teamslot is valid and returns slotId
     * @param teamId - teamId (foreign key returned from reading team table)
     * @return - SlotId if valid (-1 if not)
     */
    public int getSlotId(int teamId){
        String sql = "SELECT * FROM teamSlots WHERE teamId = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, teamId);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                return rs.getInt("slotId");
            } else{
                return -1;
            }
        } catch (SQLException e) {
            System.err.println("GetUsers Failed : " + e.getMessage());
            return -1;
        }
    }

    /**
     * TEAMSLOTS - Update
     * Updates info based on column entered
     * @param column - String (needs to be same as in the table)
     * @param newInfo - new information (Int)
     * @param slotId - slot Id (int)
     */
    public void updateTeamSlots(String column, int newInfo, int slotId){
        String sql = "UPDATE teamSlots SET " + column +  " = ? WHERE slotId = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, newInfo);
            pstmt.setInt(2, slotId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("updateUsername failed: " + e.getMessage());
        }
    }

    /**
     * TEAMSLOTS - Delete
     * Gets slot Id and deletes the slot
     * @param slotId - slot Id
     */
    public void deleteTeamSlot(int slotId){
        String sql = "DELETE FROM teamSlots WHERE slotId = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, slotId);
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

    /*
    Commented this out cuz idk what seedData is supposed to do
    private void seedData() {
        try (Statement stmt = connection.createStatement()) {

            stmt.executeUpdate("INSERT OR IGNORE INTO pokemon (pokemonId, pName, type, attack, spAttack, defence, spDefence) VALUES (1, 'Pikachu', 'ELECTRIC', 55, 50, 40, 50)");
            stmt.executeUpdate("INSERT OR IGNORE INTO pokemon (pokemonId, pName, type, attack, spAttack, defence, spDefence) VALUES (2, 'Charizard', 'FIRE', 84, 109, 78, 85)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    Also I made this method already I think
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
    */

    /**
     * TEST ONLY
     */
    static void resetForTesting(){
        if (instance != null) instance.close();
        instance = null;
    }
}
