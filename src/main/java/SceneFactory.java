import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SceneFactory {
    public static Scene create(SceneType type, Stage stage){
        return switch(type){
            case MAIN       -> buildMainStage(stage);
        };
    }

    public static Scene buildMainStage(Stage stage){
        VBox root = new VBox();
        Label title = new Label("Welcome to RoSHAMbrawl");

        Button loginButton = new Button("Login");
        Button createButton = new Button("Create account");

        root.getChildren().addAll(title,loginButton,createButton);

        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);
        root.setPadding(new Insets(20));

        return new Scene(root, 400, 300);
    }
}
