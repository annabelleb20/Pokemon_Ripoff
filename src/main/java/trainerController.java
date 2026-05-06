import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class trainerController {
    public Scene buildScene(){
        VBox root = new VBox();

        int userId = SceneManager.getInstance().getCurrentUserId();

        Label title = new Label("Trainer Options");

        Button createEditTeam = new Button("Create/Edit Team");
        Button exportTeam = new Button("Export Team");
        Button viewTable = new Button("View Pokemon Table");
        Button logOut = new Button("Log out");

        createEditTeam.setOnAction(e-> SceneManager.getInstance().navigateTo(SceneType.TEAM_BUILD));

//        exportTeam.setOnAction(e->);

        logOut.setOnAction(e-> SceneManager.getInstance().navigateTo(SceneType.MAIN));

        viewTable.setOnAction(e ->
                SceneManager.getInstance().navigateTo(SceneType.TABLE_VIEW)
        );

        root.getChildren().addAll(title, createEditTeam, exportTeam, viewTable, logOut);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(12);
        root.setPadding(new Insets(20));
        return new Scene(root, 400, 300);
    }
}
