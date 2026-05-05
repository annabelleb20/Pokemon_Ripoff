import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class TeamCreateController {

    private int regionSelected;
    private int typeSelected;
    int slotSelected;
    List<Pokemon> team = new ArrayList<>();


    void setSlotSelected(int in) {
        slotSelected = in;
    }
    public Scene buildScene(){
        VBox root = new VBox();
        Label prompt = new Label("Assemble your team!");
        Label regionPrompt = new Label("Select a region (enter 1-9)");
        javafx.scene.control.TextField regionField = new javafx.scene.control.TextField();
        regionField.setPromptText("enter a region number");



        Label typePrompt = new Label("Select a primary type: 1 for Grass, 2 for grass, 3 for water");
        javafx.scene.control.TextField typeField = new javafx.scene.control.TextField();
        typeField.setPromptText("enter a type");

        Button addButton = new Button("No slot selected");


        //slot selection
        Button slot1 = new Button("None, select and add");
        slot1.setOnAction(e -> {setSlotSelected(1); addButton.setText("Add to slot 1");});


        Button slot2 = new Button("None, select and add");
        slot2.setOnAction(e -> {setSlotSelected(2); addButton.setText("Add to slot 2");});


        Button slot3 = new Button("None, select and add");
        slot3.setOnAction(e ->{setSlotSelected(3); addButton.setText("Add to slot 3");});



        //Add button logic, checks region and type field to be valid, updates the slot buttons, adds to team list
        addButton.setOnAction(e -> {

            if (regionField.getText() != null) {
                try {
                    regionSelected = Integer.parseInt(regionField.getText());
                } catch (NumberFormatException error) {
                    // Handle invalid input
                    regionSelected = 0;
                }
            }

            if (typeField.getText() != null) {
                try {
                    typeSelected = Integer.parseInt(typeField.getText());
                } catch (NumberFormatException error) {
                    // Handle invalid input
                    typeSelected = 0;
                }
            }

            Pokemon pokemonToAdd = addByRegion(regionSelected, typeSelected);
            switch(slotSelected) {
                case 1 -> slot1.setText(pokemonToAdd.poke_name);
                case 2 -> slot2.setText(pokemonToAdd.poke_name);
                case 3 -> slot3.setText(pokemonToAdd.poke_name);
            }
            team.add(slotSelected-1, pokemonToAdd);


        });



        Button saveButton = new Button("Save team and exit");
        //todo: attach to user info
        saveButton.setOnAction(e -> {
            SceneManager.getInstance().navigateTo(SceneType.TRAINER);
        });

        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(20));

        root.getChildren().addAll(prompt,regionPrompt,regionField,typePrompt,typeField,slot1,slot2,slot3,addButton,
                saveButton);


        return new Scene(root, 500, 500);
    }

    //create structure for id's
    //9 rows, 3 columns
    public int[][] pokeList = {
            {3,9,12}, // Kanto
            {154,157,160}, // Johto
            {254,257,260},// Hoenn
            {389,392,395}, // Sinnoh
            {497,500,503}, // Unova
            {652,655,658}, // Kalos
            {724,727,730}, // Alola
            {810, 813, 816}, //Galar
            {908, 911, 914} //Paldea
    };


    public void setPokeList(int r, int c, int input) {
        pokeList[r][c] = input;
    }

    public int getPokeList(int r, int c) {
        return pokeList[r][c];
    }


    public Pokemon addByRegion(int region, int inType) {

        //1 is grass, 2 is fire, 3 is water
        region -= 1;
        inType -= 1;
        int idOut = getPokeList(region,inType);
        return PokemonAPIConsume.APIPull(idOut);
    }




    }

