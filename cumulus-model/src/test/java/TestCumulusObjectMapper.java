import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

final class TestCumulusObjectMapper {

    @Test
    void testGetValueForKey() throws IOException, URISyntaxException {
        String json = readResourceFile("cumulus/json/downloadRequest.json");
        String expectedValue = "95e7713a-ccd6-432d-b2f0-972422511171";
        String actualValue = CumulusObjectMapper.getValueForKey(json, "watershed_id");
        assertEquals(expectedValue, actualValue);

        assertThrows(IOException.class, () -> CumulusObjectMapper.getValueForKey(json, "non_existent_key"));
    }

    private String readResourceFile(String resource) throws IOException, URISyntaxException {
        URL resourceUrl = getClass().getClassLoader().getResource(resource);
        if (resourceUrl == null) {
            throw new IOException("Failed to get resource: " + resource);
        }
        List<String> lines = Files.readAllLines(Paths.get(resourceUrl.toURI()));
        StringBuilder retVal = new StringBuilder();
        for(String line : lines) {
            retVal.append(line.replace(" ", ""));
        }
        return retVal.toString();
    }
}
