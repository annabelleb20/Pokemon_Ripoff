import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class AdminDashboardController {
    public Scene buildScene(){
        GridPane grid = new GridPane();
        grid.setVgap(5.0);
        grid.setHgap(5.0);

        Label title = new Label("Welcome Gym Leader!");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        grid.add(title, 2, 0);

        Button editBtn = new Button("Edit Trainers");
        editBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.add(editBtn, 2, 1);

        Button selectBtn = new Button("Select 2 Teams to Battle");
        selectBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.add(selectBtn, 2, 3);

        return new Scene(grid, 400, 300);
    }
}
