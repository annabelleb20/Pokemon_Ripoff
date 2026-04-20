import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneFactory {
    public static Scene create(SceneType type, Stage stage){
        return switch(type){
            case MAIN       -> buildMainStage(stage);
        };
    }

    public static Scene buildMainStage(Stage stage){
        //return scene; make a scene
    }
}
