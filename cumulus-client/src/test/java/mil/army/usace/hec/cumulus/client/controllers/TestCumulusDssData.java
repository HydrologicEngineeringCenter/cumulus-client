package mil.army.usace.hec.cumulus.client.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import mil.army.usace.hec.cumulus.client.model.Product;
import mil.army.usace.hec.cumulus.client.model.Watershed;
import org.junit.jupiter.api.Test;

final class TestCumulusDssData {

    @Test
    void testSerialization() throws IOException, URISyntaxException {
        String watershedJson = readResourceFile("cumulus/json/watershed.json");
        String productJson = readResourceFile("cumulus/json/product.json");
        Watershed watershed = CumulusObjectMapper.mapJsonToObject(watershedJson, Watershed.class);
        Product product = CumulusObjectMapper.mapJsonToObject(productJson, Product.class);
        String dssPath = CumulusDssDataUtil.buildDssPath(watershed, product);
        assertEquals("SHG/Cumberland Basin River/PRECIP/23Aug2021:0601/24Apr2022:1201/MBRFC-FORECAST", dssPath);
    }

    private String readResourceFile(String resource) throws IOException, URISyntaxException {
        URL resourceUrl = getClass().getClassLoader().getResource(resource);
        if (resourceUrl == null) {
            throw new IOException("Failed to get resource: " + resource);
        }
        List<String> lines = Files.readAllLines(Paths.get(resourceUrl.toURI()));
        StringBuilder retVal = new StringBuilder();
        for(String line : lines) {
            retVal.append(line);
        }
        return retVal.toString();
    }
}
