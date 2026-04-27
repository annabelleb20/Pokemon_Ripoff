import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Drew Johnson
 * @since 4/25/26
 *
 */
public class PokemonAPIConsume {
     public static String APIPull(int Poke_id){
        // 1. Create the HttpClient
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();

        // 2. Build the HttpRequest with the URL and headers
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://pokeapi.co/api/v2/pokemon/" + Poke_id))
                .header("Accept", "application/json")
                .GET() // Use .POST() or .PUT() with body if needed
                .build();

        // 3. Send the request
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();


            } else {
                System.err.println("Error: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }



    }

}
