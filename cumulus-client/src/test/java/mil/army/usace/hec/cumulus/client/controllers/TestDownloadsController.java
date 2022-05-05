package mil.army.usace.hec.cumulus.client.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import mil.army.usace.hec.cumulus.client.model.Download;
import mil.army.usace.hec.cumulus.client.model.DownloadRequest;
import mil.army.usace.hec.cumulus.client.model.Product;
import mil.army.usace.hec.cumulus.client.model.Watershed;
import org.junit.jupiter.api.Test;

class TestDownloadsController extends TestController{

    @Test
    void testRetrieveDownload() throws IOException {
        String resource = "cumulus/json/download.json";
        launchMockServerWithResource(resource);

        String downloadId = "497f6eca-6276-4993-bfeb-53cbbbba6f08";
        DownloadsEndpointInput input = new DownloadsEndpointInput(downloadId);
        Download download = new DownloadsController().retrieveDownload(buildConnectionInfo(), input);

        assertNotNull(download);
        assertEquals("497f6eca-6276-4993-bfeb-53cbbbba6f08", download.getId());
        assertEquals("753487e7-10bc-4e69-b3b2-4da33721ea3e", download.getSub());
        assertEquals("2020-12-01T01:00:01Z", download.getDateTimeStart().toString());
        assertEquals("2020-12-15T01:00:02Z", download.getDateTimeEnd().toString());
        assertEquals("94e7713a-ccd6-432d-b2f0-972422511171", download.getWatershedId());
        assertEquals(1, download.getProductId().length);
        assertEquals("64756f41-75e2-40ce-b91a-fda5aeb441fc", download.getProductId()[0]);
        assertEquals("4e949624-bc0f-439e-a9f2-25a23938812c", download.getStatusId());
        assertEquals("SUCCESS", download.getStatus());
        assertEquals(100, download.getProgress());
        assertEquals("https://cumulus-api.corps.cloud/path/to/file.dss", download.getFile());
        assertEquals("2022-03-01T14:15:22Z", download.getProcessingStart().toString());
        assertEquals("2022-03-01T14:45:50Z", download.getProcessingEnd().toString());
        assertEquals("genesee-river", download.getWatershedSlug());
        assertEquals("Genesee River", download.getWatershedName());
    }

    @Test
    void testCreateDownload() throws IOException {
        String resource = "cumulus/json/created_download.json";
        launchMockServerWithResource(resource);
        Watershed watershed = new Watershed();
        watershed.setId("95e7713a-ccd6-432d-b2f0-972422511171");
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId("74756f41-75e2-40ce-b91a-fda5aeb441fc");
        products.add(product);
        ZonedDateTime start = ZonedDateTime.of(2022, 4, 1, 1, 1, 1, 1, ZoneId.of("UTC"));
        ZonedDateTime end = ZonedDateTime.of(2022, 4, 1, 1, 6, 1, 1, ZoneId.of("UTC"));
        DownloadRequest downloadRequest = new DownloadRequest(start, end, watershed, products);
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVXNlciIsImlzcyI6IlNpbXBsZSBTb2x1dGlvbiIsInVzZXJuYW1lIjoiVGVzdFVzZXIifQ.jQUKIOxN0KGbIGJx8SU3WfSVPNASOnRtt3DcoMVBeThcWGzEBAnwlHHYRvbzuas-sOeWSvOwrnsvpQ5tywAfWA";
        Download download = new DownloadsController().createDownload(buildConnectionInfo(), downloadRequest, token);
        assertNotNull(download);
        assertEquals("597f6eca-6276-4993-bfeb-53cbbbba6f08", download.getId());
        assertEquals("853487e7-10bc-4e69-b3b2-4da33721ea3e", download.getSub());
        assertEquals("2020-12-01T01:00:01Z", download.getDateTimeStart().toString());
        assertEquals("2020-12-15T01:00:02Z", download.getDateTimeEnd().toString());
        assertEquals("95e7713a-ccd6-432d-b2f0-972422511171", download.getWatershedId());
        assertEquals(1, download.getProductId().length);
        assertEquals("74756f41-75e2-40ce-b91a-fda5aeb441fc", download.getProductId()[0]);
        assertEquals("5e949624-bc0f-439e-a9f2-25a23938812c", download.getStatusId());
        assertEquals("SUCCESS", download.getStatus());
        assertEquals(0, download.getProgress());
        assertEquals("file:/J:/git/cumulus-client/cumulus-client/build/resources/test/cumulus/dss/input/forecast.dss", download.getFile());
        assertEquals("2022-03-01T14:15:22Z", download.getProcessingStart().toString());
        assertEquals("2022-03-01T14:45:50Z", download.getProcessingEnd().toString());
        assertEquals("fake-river", download.getWatershedSlug());
        assertEquals("Fake River", download.getWatershedName());
    }

    @Test
    void testDownload() throws IOException {
        String resource = "cumulus/json/created_download.json";
        launchMockServerWithResource(resource);
        Watershed watershed = new Watershed();
        watershed.setId("95e7713a-ccd6-432d-b2f0-972422511171");
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId("74756f41-75e2-40ce-b91a-fda5aeb441fc");
        products.add(product);
        ZonedDateTime start = ZonedDateTime.of(2022, 4, 1, 1, 1, 1, 1, ZoneId.of("UTC"));
        ZonedDateTime end = ZonedDateTime.of(2022, 4, 1, 1, 6, 1, 1, ZoneId.of("UTC"));
        DownloadRequest downloadRequest = new DownloadRequest(start, end, watershed, products);
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVXNlciIsImlzcyI6IlNpbXBsZSBTb2x1dGlvbiIsInVzZXJuYW1lIjoiVGVzdFVzZXIifQ.jQUKIOxN0KGbIGJx8SU3WfSVPNASOnRtt3DcoMVBeThcWGzEBAnwlHHYRvbzuas-sOeWSvOwrnsvpQ5tywAfWA";
        IOException exception =
            assertThrows(IOException.class, () -> new DownloadsController().download(buildConnectionInfo(), downloadRequest, null, token));
        assertEquals("Local file path is required for download", exception.getMessage());
    }

}


