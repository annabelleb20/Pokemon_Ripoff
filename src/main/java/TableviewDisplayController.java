import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TableviewDisplayController {

    public Scene buildScene(Stage stage) {

        VBox root = new VBox();
        root.setSpacing(10);
        root.setStyle("-fx-padding: 20;");

        TableView<Pokemon> table = new TableView<>();

        ObservableList<Pokemon> data = FXCollections.observableArrayList();
        loadData(data);
        table.setItems(data);

        TableColumn<Pokemon, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("poke_name"));

        TableColumn<Pokemon, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("primaryType"));

        TableColumn<Pokemon, Integer> attackCol = new TableColumn<>("Attack");
        attackCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("attack"));

        table.getColumns().addAll(nameCol, typeCol, attackCol);

        Button refreshButton = new Button("Refresh Table");
        refreshButton.setOnAction(e -> {
            // navigate fresh forces a full scene rebuild with new DB data
            SceneManager.getInstance().navigateFresh(SceneType.TABLE_VIEW);
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e ->
                SceneManager.getInstance().navigateFresh(SceneType.TRAINER)
        );

        root.getChildren().addAll(
                new Label("Pokemon TableView"),
                refreshButton,
                backButton,
                table
        );

        return new Scene(root, 600, 400);
    }

    private void loadData(ObservableList<Pokemon> data) {

        String sql = "SELECT * FROM pokemon";

        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Pokemon p = new Pokemon();
                p.setPoke_name(rs.getString("pName"));
                p.setAttack(rs.getInt("attack"));

                String type = rs.getString("type");
                try {
                    p.setPrimaryType(Poke_Type.valueOf(type));
                } catch (Exception e) {
                    p.setPrimaryType(Poke_Type.NORMAL);
                }

                data.add(p);
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}