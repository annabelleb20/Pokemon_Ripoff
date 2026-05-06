import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import java.sql.ResultSet;

public class TableviewDisplayController {

    public Scene buildScene(Stage stage) {

        VBox root = new VBox();
        root.setSpacing(10);
        root.setStyle("-fx-padding: 20;");

        TableView<Pokemon> table = new TableView<>();
        ObservableList<Pokemon> data = FXCollections.observableArrayList();

        // initial load
        loadData(data);

        // columns
        TableColumn<Pokemon, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("poke_name"));

        TableColumn<Pokemon, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("primaryType"));

        TableColumn<Pokemon, Integer> attackCol = new TableColumn<>("Attack");
        attackCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("attack"));

        table.getColumns().addAll(nameCol, typeCol, attackCol);
        table.setItems(data);

        // refresh button
        Button refreshButton = new Button("Refresh Table");

        refreshButton.setOnAction(e -> {
            data.clear();
            loadData(data);
        });

        root.getChildren().addAll(
                new Label("Pokemon TableView"),
                refreshButton,
                table
        );

        return new Scene(root, 600, 400);
    }

    // helper method to avoid duplicate DB code
    private void loadData(ObservableList<Pokemon> data) {
        try {
            var conn = DatabaseManager.getInstance().getConnection();
            var stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM pokemon");

            while (rs.next()) {
                Pokemon p = new Pokemon();
                p.setPoke_name(rs.getString("pName"));
                p.setAttack(rs.getInt("attack"));
                p.setPrimaryType(Poke_Type.valueOf(rs.getString("type")));
                data.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}