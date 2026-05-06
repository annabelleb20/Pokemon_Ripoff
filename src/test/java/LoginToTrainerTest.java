import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

public class LoginToTrainerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) {

        // ✅ initialize SceneManager FIRST
        SceneManager.init(stage);

        // ✅ OPTION 2 FIX: seed test user directly in test setup
        DatabaseManager db = DatabaseManager.getInstance();
        db.newUser("testuser", "testpass");

        // optional safety: ensures login is fresh every test
        SceneManager.getInstance().clear();

        // open login scene
        SceneManager.getInstance().navigateFresh(SceneType.LOGIN);

        stage.show();
        stage.toFront();
        stage.requestFocus();
    }

    @Test
    void login_to_trainer_scene_transition() {

        sleep(500);

        // login input
        clickOn("#usernameField");
        write("testuser");

        clickOn("#passwordField");
        write("testpass");

        clickOn("#loginButton");

        // allow scene transition
        sleep(800);

        // verify trainer screen loaded
        verifyThat("Trainer Options", isVisible());
    }
}