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
    void testHourly() throws IOException, URISyntaxException {
        String watershedJson = readResourceFile("cumulus/json/watershed.json");
        String productJson = readResourceFile("cumulus/json/product.json");
        Watershed watershed = CumulusObjectMapper.mapJsonToObject(watershedJson, Watershed.class);
        Product product = CumulusObjectMapper.mapJsonToObject(productJson, Product.class);
        String dssPath = CumulusDssDataUtil.buildDssPath(watershed, product);
        assertEquals("/SHG/Cumberland Basin River/PRECIP/21Sep2022:1800/21Sep2022:1900/ABRFC-QPE/", dssPath);
    }

    @Test
    void testInst() throws IOException, URISyntaxException {
        String watershedJson = readResourceFile("cumulus/json/watershed.json");
        String productJson = readResourceFile("cumulus/json/productInst.json");
        Watershed watershed = CumulusObjectMapper.mapJsonToObject(watershedJson, Watershed.class);
        Product product = CumulusObjectMapper.mapJsonToObject(productJson, Product.class);
        String dssPath = CumulusDssDataUtil.buildDssPath(watershed, product);
        assertEquals("/SHG/Cumberland Basin River/AIRTEMP/26Sep2022:1900//MARFC-NBM/", dssPath);
    }

    @Test
    void testEmpty() {
        Watershed emptyWatershed = new Watershed();
        Product emptyProduct = new Product();
        String dssPathForEmpty = CumulusDssDataUtil.buildDssPath(emptyWatershed, emptyProduct);
        assertEquals("/SHG//////", dssPathForEmpty);
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
