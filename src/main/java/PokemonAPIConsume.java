import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;




/**
 * @author Drew Johnson
 * @since 4/25/26
 *Accesses the Pokemon API and creates a pokemon object, and assigns the needed stat values.
 */
public class PokemonAPIConsume {

    public static Pokemon APIPull(int Poke_id) {
        Pokemon pokeOut = new Pokemon();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://pokeapi.co/api/v2/pokemon/" + Poke_id))
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println(response.body());
            // 3. Parse JSON
            String jsonStr = response.body();
            JSONObject jsonObj = new JSONObject(jsonStr);

            // Access nested data

            pokeOut.setPoke_id(Poke_id);

            pokeOut.setPoke_name(jsonObj.getString("name"));

            //System.out.println(jsonObj.getString("name"));
            //System.out.println(pokeOut.poke_name);

            JSONArray types = jsonObj.getJSONArray("types");
            JSONObject type1 = types.getJSONObject(0).getJSONObject("type");
            JSONObject type2 = types.getJSONObject(1).getJSONObject("type");

            pokeOut.setPrimaryType(Poke_Type.valueOf(type1.getString("name").toUpperCase()));
            pokeOut.setSecondaryType(Poke_Type.valueOf(type2.getString("name").toUpperCase()));

            /*
            System.out.println(type1.getString("name"));
            System.out.println(type2.getString("name"));
            System.out.println(pokeOut.primaryType.name());
            System.out.println(pokeOut.secondaryType.name());
            */

            JSONArray stats = jsonObj.getJSONArray("stats");
            JSONObject hp = stats.getJSONObject(0);
            //System.out.println("HP: " + hp.getInt("base_stat"));

            pokeOut.setBaseHp(hp.getInt("base_stat"));

            JSONObject attack = stats.getJSONObject(1);
            //System.out.println("Attack: " + attack.getInt("base_stat"));

            pokeOut.setAttack(attack.getInt("base_stat"));

            JSONObject defense = stats.getJSONObject(2);
            //System.out.println("Defense: " + defense.getInt("base_stat"));

            pokeOut.setDefense(defense.getInt("base_stat"));

            JSONObject specialAttack = stats.getJSONObject(3);
            //System.out.println("Special Attack: " + specialAttack.getInt("base_stat"));

            pokeOut.setSp_attack(specialAttack.getInt("base_stat"));

            JSONObject specialDefense = stats.getJSONObject(4);
            //System.out.println("Special Defense: " + specialDefense.getInt("base_stat"));

            pokeOut.setSp_defense(specialDefense.getInt("base_stat"));
            return pokeOut;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        Pokemon swampert = APIPull(260);
        System.out.println(swampert.toString());
    }
}
