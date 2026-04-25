import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application  {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Pokemon RoSHAMbrawl");
        stage.show();
    }

    @Override
    public void stop(){
        DatabaseManager.getInstance().close();
    }
}
