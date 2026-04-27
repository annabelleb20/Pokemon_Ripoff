import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application  {

    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.init(stage);
        stage.setTitle("Pokemon RoSHAMbrawl");

        //saying get the instance of scene manager then navigate to the main scene
        SceneManager.getInstance().navigateTo(SceneType.MAIN);
        stage.show();
    }

    @Override
    public void stop(){
        DatabaseManager.getInstance().close();
    }
}
