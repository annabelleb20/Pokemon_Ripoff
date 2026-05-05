import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class FightController {
    DatabaseManager db = DatabaseManager.getInstance();
    private final User user1 = AppData.getInstance().getDuel().get(0);
    private final User user2 = AppData.getInstance().getDuel().get(1);

    public Scene buildScene(){
        Label title = new Label(user1.getName() + " vs " + user2.getName());
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox layout = new VBox(12, title);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(16));
        return new Scene(layout, 500, 400);
    }
}
