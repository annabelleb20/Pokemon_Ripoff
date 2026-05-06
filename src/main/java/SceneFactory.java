import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SceneFactory {

    public static Scene create(SceneType type, Stage stage){
        return switch(type){
            case MAIN -> buildMainStage(stage);
            case TABLE_VIEW -> buildTableViewScene(stage);
            case LOGIN -> buildLoginScene(stage);
            case CREATION -> new CreationController().buildScene();
            case ADMIN_DASHBOARD -> new AdminDashboardController().buildScene();
            case ADMIN_USER_EDIT -> new AdminUserEditController().buildScene();
            case FIGHT -> new FightController().buildScene();
            case TRAINER -> new trainerController().buildScene();
            case TEAM_BUILD -> new TeamCreateController().buildScene();
        };
    }

    public static Scene buildMainStage(Stage stage){
        VBox root = new VBox();
        Label title = new Label("Welcome to Pokemon RoSHAMbrawl");

        Button loginButton = new Button("Login");
        Button createButton = new Button("Create account");

        loginButton.setOnAction(e ->
                SceneManager.getInstance().navigateFresh(SceneType.LOGIN));

        createButton.setOnAction(e ->
                SceneManager.getInstance().navigateFresh(SceneType.CREATION));

        root.getChildren().addAll(title, loginButton, createButton);

        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);
        root.setPadding(new Insets(20));

        return new Scene(root, 400, 300);
    }

    /**
     * ✅ FIXED LOGIN SCENE (THIS IS THE IMPORTANT PART)
     */
    public static Scene buildLoginScene(Stage stage){
        VBox root = new VBox();

        DatabaseManager db = DatabaseManager.getInstance();

        Label prompt = new Label("Please enter your username and password");

        Label username = new Label("Username");
        TextField usernameField = new TextField();
        usernameField.setId("usernameField");   // ✅ FIX

        usernameField.setPromptText("Please enter your username");

        Label password = new Label("Password");
        PasswordField passwordField = new PasswordField();
        passwordField.setId("passwordField");   // ✅ FIX

        passwordField.setPromptText("Please enter your password");

        Button login = new Button("Login");
        login.setId("loginButton");            // ✅ FIX

        Button signup = new Button("Sign up");

        login.setOnAction(e-> {
            int userid = db.readUser(usernameField.getText(), passwordField.getText());

            if (userid == -1) {
                prompt.setText("Sorry, your credentials are wrong. Please try again");
            } else {
                SceneManager.getInstance().setCurrentUserId(userid);

                if(db.isAdmin(userid)){
                    SceneManager.getInstance().navigateFresh(SceneType.ADMIN_DASHBOARD);
                } else {
                    SceneManager.getInstance().navigateTo(SceneType.TRAINER);
                }
            }
        });

        signup.setOnAction(e ->
                SceneManager.getInstance().navigateTo(SceneType.CREATION));

        root.getChildren().addAll(
                prompt,
                username, usernameField,
                password, passwordField,
                signup,
                login
        );

        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(20));

        return new Scene(root, 400, 300);
    }

    public static Scene buildTableViewScene(Stage stage){
        return new TableviewDisplayController().buildScene(stage);
    }
}