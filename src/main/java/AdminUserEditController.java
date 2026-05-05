import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AdminUserEditController {
    private final User user = AppData.getInstance().getSelectedUser();
    DatabaseManager db = DatabaseManager.getInstance();

    public Scene buildScene(){
        Label title = new Label("Editing Trainer: " + user.getName());
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label username = new Label("Edit Username (current username: " + user.getName() + ")");
        TextField userInput = new TextField();
        userInput.setPromptText("New username");
        Button nameBtn  = new Button("Change");
        nameBtn.setOnAction(e -> handleUserChange(userInput.getText(), title, username));
        HBox nameInputRow = new HBox(8, userInput, nameBtn);
        HBox.setHgrow(userInput, Priority.ALWAYS);

        Label password = new Label("Edit Password (current password: " + user.getPass() + ")");
        TextField passInput = new TextField();
        passInput.setPromptText("New password");
        Button passBtn  = new Button("Change");
        passBtn.setOnAction(e -> handlePassChange(userInput.getText(), password));
        HBox passInputRow = new HBox(8, passInput, passBtn);
        HBox.setHgrow(passInput, Priority.ALWAYS);

        Button delete = new Button("Delete User");
        delete.setOnAction(e -> handleDelete());
        Button back = new Button("Back to Dashboard");
        back.setOnAction(e -> SceneManager.getInstance().navigateFresh(SceneType.ADMIN_DASHBOARD));

        VBox layout = new VBox(12, title, username, nameInputRow, password, passInputRow, delete, back);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(16));
        return new Scene(layout, 500, 400);
    }

    private void handleUserChange(String newUser, Label title, Label username){
        db.updateUsername(newUser, user.getId());
        title.setText("Editing Trainer: " + newUser);
        username.setText("Successfully updated username! Current username: " + newUser);
    }

    private void handlePassChange(String newPass, Label password){
        db.updatePassword(newPass, user.getId());
        password.setText("Successfully updated password! Current password: " + newPass);
    }

    private void handleDelete(){
        db.deleteUser(user.getId());
    }
}
