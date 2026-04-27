import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PokemonAPIConsumeTest {
    @Test
    void PokemonAPINullTest() {
        assertNull(PokemonAPIConsume.APIPull(-1));
    }

}
