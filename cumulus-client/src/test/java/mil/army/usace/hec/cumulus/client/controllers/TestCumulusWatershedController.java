package mil.army.usace.hec.cumulus.client.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.List;
import mil.army.usace.hec.cumulus.client.model.Watershed;
import org.junit.jupiter.api.Test;

class TestCumulusWatershedController extends TestController {

    @Test
    void testRetrieveWatershed() throws IOException {
        String resource = "cumulus/json/watershed.json";
        launchMockServerWithResource(resource);

        String watershedId = "c785f4de-ab17-444b-b6e6-6f1ad16676e8";
        WatershedEndpointInput input = new WatershedEndpointInput(watershedId);
        Watershed watershed = new CumulusWatershedController(executorService).retrieveWatershed(buildConnectionInfo(), input).join();

        assertNotNull(watershed);
        assertEquals("c785f4de-ab17-444b-b6e6-6f1ad16676e8", watershed.getId());
        assertEquals("LRN", watershed.getOfficeSymbol());
        assertEquals("cumberland-basin-river", watershed.getSlug());
        assertEquals("Cumberland Basin River", watershed.getName());
        assertEquals(1, watershed.getAreaGroups().length);
        assertEquals("e3fd63a1-f19f-4bf3-b436-1c7086b7afe7", watershed.getAreaGroups()[0]);
        assertEquals(4, watershed.getBbox().length);
        assertEquals(662000, watershed.getBbox()[0]);
        assertEquals(1408000, watershed.getBbox()[1]);
        assertEquals(1172000, watershed.getBbox()[2]);
        assertEquals(1678000, watershed.getBbox()[3]);
    }

    @Test
    void testRetrieveAllWatersheds() throws IOException {
        String resource = "cumulus/json/watersheds.json";
        launchMockServerWithResource(resource);

        List<Watershed> watersheds = new CumulusWatershedController(executorService).retrieveAllWatersheds(buildConnectionInfo()).join();

        assertNotNull(watersheds);
        assertEquals(2, watersheds.size());
        Watershed watershed = watersheds.get(0);
        assertEquals("c785f4de-ab17-444b-b6e6-6f1ad16676e8", watershed.getId());
        assertEquals("LRN", watershed.getOfficeSymbol());
        assertEquals("cumberland-basin-river", watershed.getSlug());
        assertEquals("Cumberland Basin River", watershed.getName());
        assertEquals(1, watershed.getAreaGroups().length);
        assertEquals("e3fd63a1-f19f-4bf3-b436-1c7086b7afe7", watershed.getAreaGroups()[0]);
        assertEquals(4, watershed.getBbox().length);
        assertEquals(662000, watershed.getBbox()[0]);
        assertEquals(1408000, watershed.getBbox()[1]);
        assertEquals(1172000, watershed.getBbox()[2]);
        assertEquals(1678000, watershed.getBbox()[3]);

        Watershed watershed2 = watersheds.get(1);
        assertEquals("feda585b-1ba0-4b19-92ed-7195154b8052", watershed2.getId());
        assertEquals("LRN", watershed2.getOfficeSymbol());
        assertEquals("tennessee-cumberland-river", watershed2.getSlug());
        assertEquals("Tennessee \u0026 Cumberland River", watershed2.getName());
        assertEquals(0, watershed2.getAreaGroups().length);
        assertEquals(4, watershed2.getBbox().length);
        assertEquals(642000, watershed2.getBbox()[0]);
        assertEquals(1258000, watershed2.getBbox()[1]);
        assertEquals(1300000, watershed2.getBbox()[2]);
        assertEquals(1682000, watershed2.getBbox()[3]);

    }

}
