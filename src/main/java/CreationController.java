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

        signup.setOnAction(e-> {
            //slight duplicate measure. it's kinda a security hazard, but I don't think we need to worry about it
            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()){
                prompt.setText("invalid username or password");
            }else {
                int userid = -1;
                userid = db.readUser(usernameField.getText(), passwordField.getText());
                if (userid!=-1){
                    prompt.setText("Sorry, that is already in use. Please try another username");
                }else {
                    prompt.setText("Sign up successful. Welcome " + usernameField.getText());
                    db.newUser(usernameField.getText(), passwordField.getText());
                    signup.setText("Continue");

                    //todo: make this lead to the next scene
                    signup.setOnAction(event ->{
                            prompt.setText("Please enter a username and a password");
                            SceneManager.getInstance().navigateTo(SceneType.MAIN);
                    });
                }
            }
        });

        login.setOnAction(e->SceneManager.getInstance().navigateTo(SceneType.LOGIN));

        root.getChildren().addAll(prompt,username,usernameField,password,passwordField,login,signup);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(20));
        return new Scene(root,400,300);
    }
}
