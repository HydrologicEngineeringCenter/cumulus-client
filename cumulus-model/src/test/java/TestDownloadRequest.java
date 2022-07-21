import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import mil.army.usace.hec.cumulus.client.model.DownloadRequest;
import org.junit.jupiter.api.Test;

class TestDownloadRequest {

    @Test
    void testSerialization() throws IOException, URISyntaxException {
        String watershedId = "95e7713a-ccd6-432d-b2f0-972422511171";
        List<String> productIds = new ArrayList<>();
        String productId = "74756f41-75e2-40ce-b91a-fda5aeb441fc";
        productIds.add(productId);
        ZonedDateTime start = ZonedDateTime.of(2022, 4, 1, 1, 1, 1, 0, ZoneId.of("Z"));
        ZonedDateTime end = ZonedDateTime.of(2022, 4, 1, 1, 6, 1, 0, ZoneId.of("Z"));
        DownloadRequest downloadRequest = new DownloadRequest(start, end, watershedId, productIds);
        String jsonBody = CumulusObjectMapper.mapObjectToJson(downloadRequest);
        String expectedJson = readResourceFile("cumulus/json/downloadRequest.json");
        assertEquals(expectedJson, jsonBody);
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
