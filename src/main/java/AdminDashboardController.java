import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class AdminDashboardController {
    DatabaseManager db = DatabaseManager.getInstance();

    public Scene buildScene(){
        GridPane grid = new GridPane();
        grid.setVgap(5.0);
        grid.setHgap(5.0);

        Label title = new Label("Welcome Gym Leader!");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        grid.add(title, 3, 0);

        Button editBtn = new Button("Select a trainer to edit");
        editBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.add(editBtn, 3, 1);

        Button selectBtn = new Button("Select 2 Teams to Battle");
        selectBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.add(selectBtn, 3, 3);

        Button logOut = new Button("Log Out");
        logOut.setMaxSize(Double.MAX_VALUE, 1);
        GridPane.setValignment(logOut, VPos.BOTTOM);
        grid.add(logOut, 3, 7);

        TableView<User> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ObservableList<User> data = FXCollections.observableArrayList();
        data.addAll(db.getUsers());

        Label tableTitle = new Label("Users");
        TableColumn<User, Integer> idCol = new TableColumn<>("Id");
        idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        TableColumn<User, String> passCol = new TableColumn<>("Password");
        passCol.setCellValueFactory(new PropertyValueFactory<>("Pass"));
        passCol.setMaxWidth(Double.MAX_VALUE);

        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getColumns().addAll(idCol, nameCol, passCol);

        table.setItems(data);
        grid.add(table, 4, 0, 2, 8);

        editBtn.setOnAction(e -> handleEdit(table.getSelectionModel().getSelectedItem(), editBtn));

        logOut.setOnAction(e -> SceneManager.getInstance().navigateTo(SceneType.MAIN));

        return new Scene(grid, 500, 400);
    }

    private void handleEdit(User selected, Button btn){
        if(selected != null){
            AppData.getInstance().setSelectedUser(selected);
            SceneManager.getInstance().navigateFresh(SceneType.ADMIN_USER_EDIT);
        } else{
            btn.setText("Select a User from the table");
        }
    }
}
