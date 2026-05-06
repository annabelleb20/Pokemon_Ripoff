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

public class CreationController {
    public Scene buildScene(){
        VBox root = new VBox();
        DatabaseManager db = DatabaseManager.getInstance();

        Label prompt = new Label("Please enter a username and a password");
        Label username = new Label("Username");
        javafx.scene.control.TextField usernameField = new javafx.scene.control.TextField();
        usernameField.setPromptText("Please enter your username");
        Label password = new Label("Password");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Please enter your password");
        Button signup = new Button("Sign up");
        Button login = new Button("Login");

        signup.setOnAction(e -> {
            String enteredUsername = usernameField.getText().trim();
            String enteredPassword = passwordField.getText().trim();

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                prompt.setText("Invalid username or password.");
                return;
            }

            boolean usernameExists = db.getUsers().stream()
                    .anyMatch(user -> user.getName().equalsIgnoreCase(enteredUsername));

            if (usernameExists) {
                prompt.setText("Sorry, that username is already in use.");
                return;
            }

            db.newUser(enteredUsername, enteredPassword);
            prompt.setText("Sign up successful. Welcome " + enteredUsername);

            signup.setText("Continue");
            signup.setOnAction(event ->
                    SceneManager.getInstance().navigateFresh(SceneType.LOGIN)
            );
        });

        login.setOnAction(e->SceneManager.getInstance().navigateTo(SceneType.LOGIN));

        root.getChildren().addAll(prompt,username,usernameField,password,passwordField,login,signup);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(20));
        return new Scene(root,400,300);
    }
}
