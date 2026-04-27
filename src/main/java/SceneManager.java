import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.EnumMap;
import java.util.Map;

public class SceneManager {

    private static SceneManager instance;

    private final Stage stage;
    private final Map<SceneType, Scene> cache =
            new EnumMap<>(SceneType.class);

    private SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void navigateTo(SceneType type) {
        Scene scene = cache.computeIfAbsent (type, t -> SceneFactory.create(t, stage));
        stage.setScene(scene);
    }

    //creates the scene manager itself and gives it a window
    public static void init(Stage stage){
        if (instance == null){
            instance = new SceneManager(stage);
        }
    }

    public static SceneManager getInstance(){
        if (instance == null){
            throw new IllegalStateException("SceneManager not initialized.");
        }
        return instance;
    }

}
