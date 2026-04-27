import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.*;



public class SceneFactory {
    public static Scene create(SceneType type, Stage stage){
        return switch(type){
            case MAIN -> buildMainStage(stage);
            case TABLE_VIEW -> buildTableViewScene(stage);
        };
    }

    public static Scene buildMainStage(Stage stage){
        VBox root = new VBox();
        Label title = new Label("Welcome to RoSHAMbrawl");

        Button loginButton = new Button("Login");
        Button createButton = new Button("Create account");

        Button tableButton = new Button("Open TableView");

        tableButton.setOnAction(e -> {
            SceneManager.getInstance().navigateTo(SceneType.TABLE_VIEW);
        });

        root.getChildren().addAll(title,loginButton,createButton, tableButton);

        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);
        root.setPadding(new Insets(20));

        return new Scene(root, 400, 300);
    }
    public static Scene buildTableViewScene(Stage stage){

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(20));


        TableView<Pokemon> table = new TableView<>();
        ObservableList<Pokemon> data = FXCollections.observableArrayList();

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



        Label title = new Label("Pokemon TableView");
        TableColumn<Pokemon, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("poke_name"));
        TableColumn<Pokemon, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("primaryType"));

        TableColumn<Pokemon, Integer> attackCol = new TableColumn<>("Attack");
        attackCol.setCellValueFactory(new PropertyValueFactory<>("attack"));
        table.getColumns().addAll(nameCol, typeCol, attackCol);

        table.setItems(data);
        root.getChildren().addAll(title, table);

        return new Scene(root, 600, 400);
    }
}

