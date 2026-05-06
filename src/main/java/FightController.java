import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.EnumMap;

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

    public double getTypeEffectiveness(Pokemon attacker, Pokemon defender) {
        EnumMap<Poke_Type, EnumMap<Poke_Type, Double>> typeEffectivness = new EnumMap<>(Poke_Type.class);

        //populate enum map
        //set all to 1 as default
        for(Poke_Type typeRow: Poke_Type.values()) {
            typeEffectivness.put(typeRow,new EnumMap<>(Poke_Type.class));
            for (Poke_Type typeCol : Poke_Type.values()) {
                typeEffectivness.get(typeRow).put(typeCol, 1.0);
            }
        }
        //Fire
        typeEffectivness.get(Poke_Type.FIRE).put(Poke_Type.FIRE, 0.5);
        typeEffectivness.get(Poke_Type.FIRE).put(Poke_Type.WATER, 0.5);
        typeEffectivness.get(Poke_Type.FIRE).put(Poke_Type.GRASS, 2.0);

        //Water
        typeEffectivness.get(Poke_Type.WATER).put(Poke_Type.FIRE, 2.0);
        typeEffectivness.get(Poke_Type.WATER).put(Poke_Type.WATER, 0.5);
        typeEffectivness.get(Poke_Type.WATER).put(Poke_Type.GRASS, 0.5);

        //Grass
        typeEffectivness.get(Poke_Type.GRASS).put(Poke_Type.FIRE, 0.5);
        typeEffectivness.get(Poke_Type.GRASS).put(Poke_Type.WATER, 2.0);
        typeEffectivness.get(Poke_Type.GRASS).put(Poke_Type.GRASS, 0.5);

        return typeEffectivness.get(attacker.primaryType).get(defender.primaryType);
    }

    public double calculateDamage(Pokemon attacker, Pokemon defender) {
        double damage;

        if (attacker.sp_attack > attacker.attack) {
            damage = (double) attacker.sp_attack/defender.sp_defense;
        } else {
            damage = (double) attacker.attack/defender.defense;
        }

        damage = (damage/50) + 2;

        //Same type attack bonus
        if(attacker.primaryType.equals(defender.primaryType)) {
            damage = damage * 1.5;
        }

        return damage * getTypeEffectiveness(attacker, defender);
    }
}
