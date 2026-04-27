import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SceneFactory {
    public static Scene create(SceneType type, Stage stage){
        return switch(type){
            case MAIN       -> buildMainStage(stage);
        };
    }

    public static Scene buildMainStage(Stage stage){
        StackPane root = new StackPane();
        root.getChildren().add(new Label("Starter Screen"));

        return new Scene(root, 400, 300);

    }
}
