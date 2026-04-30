import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.*;



public class SceneFactory {
    public static Scene create(SceneType type, Stage stage){
        return switch(type){
            case MAIN -> buildMainStage(stage);
            case TABLE_VIEW -> buildTableViewScene(stage);
            case LOGIN -> buildLoginScene(stage);
//            case CREATION -> buildCreationScene(stage);
        };
    }

    /**
     * this is the first thing you see, and it asks if you want to create an account or login
     * @param stage
     * @return Scene
     */
    public static Scene buildMainStage(Stage stage){
        VBox root = new VBox();
        Label title = new Label("Welcome to Pokemon RoSHAMbrawl");

        Button loginButton = new Button("Login");
        Button createButton = new Button("Create account");

        //login button
        loginButton.setOnAction(e->
                SceneManager.getInstance().navigateTo(SceneType.LOGIN));

//commented this out so it just looks a lil more neat on the starter screen.

// we'll add it back when we get the login and the account creation working.
//        Button tableButton = new Button("Open TableView");

//        tableButton.setOnAction(e -> {
//            SceneManager.getInstance().navigateTo(SceneType.TABLE_VIEW);
//        });

        root.getChildren().addAll(title,loginButton,createButton/*, tableButton*/);

        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);
        root.setPadding(new Insets(20));

        return new Scene(root, 400, 300);
    }

    /**
     * this is the login screen.
     * @param stage
     * @return
     */
    public static Scene buildLoginScene(Stage stage){
        VBox root = new VBox();

        DatabaseManager db = DatabaseManager.getInstance();
        db.newUser("test","pass");

        //there's gotta be a better way of doing this
        Label prompt = new Label("Please enter your username and password");
        Label username = new Label("Username");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Please enter your username");
        Label password = new Label("Password");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Please enter your password");
        Button login = new Button("Login");

        //login check
        login.setOnAction(e-> {
            int userid = -1;
                userid = db.readUser(usernameField.getText(),passwordField.getText());
                if (userid==-1){
                    prompt.setText("Sorry, your credentials are wrong. Please try again");
                }else {
                    prompt.setText("login successful");
                    login.setText("Continue");
                }
                }
                );
//        login.setOnAction(e-> );
        //TODO: lead to the next scene

        root.getChildren().addAll(prompt,username,usernameField,password,passwordField,login);

        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(20));
        return new Scene(root,400,300);
    }

    public static Scene buildTableViewScene(Stage stage){

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(20));


        TableView<Pokemon> table = new TableView<>();
        ObservableList<Pokemon> data = FXCollections.observableArrayList();

        try {
            var conn = DatabaseManager.getInstance().getConnection();
            var stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM pokemon");

            while (rs.next()) {
                Pokemon p = new Pokemon();

                p.setPoke_name(rs.getString("pName"));
                p.setAttack(rs.getInt("attack"));
                p.setPrimaryType(Poke_Type.valueOf(rs.getString("type")));

                data.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Label title = new Label("Pokemon TableView");
        TableColumn<Pokemon, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("poke_name"));
        TableColumn<Pokemon, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("primaryType"));

        TableColumn<Pokemon, Integer> attackCol = new TableColumn<>("Attack");
        attackCol.setCellValueFactory(new PropertyValueFactory<>("attack"));
        table.getColumns().addAll(nameCol, typeCol, attackCol);

        table.setItems(data);
        root.getChildren().addAll(title, table);

        return new Scene(root, 600, 400);
    }


}

