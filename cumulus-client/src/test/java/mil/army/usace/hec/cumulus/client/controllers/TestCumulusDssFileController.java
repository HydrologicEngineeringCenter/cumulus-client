package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.TestCumulusFileDownloaderUtil.getDownloadFromResource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import mil.army.usace.hec.cumulus.client.model.Download;
import mil.army.usace.hec.cumulus.client.model.DownloadRequest;
import mil.army.usace.hec.cumulus.client.model.Product;
import mil.army.usace.hec.cumulus.client.model.Watershed;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;
import org.junit.jupiter.api.Test;

class TestCumulusDssFileController extends TestController{

    @Test
    void testQueryDownloadStatus() throws IOException {
        String resource = "cumulus/json/download.json";
        launchMockServerWithResource(resource);

        String downloadId = "497f6eca-6276-4993-bfeb-53cbbbba6f08";
        DownloadsEndpointInput input = new DownloadsEndpointInput(downloadId);
        Download download = new CumulusDssFileController().queryDownloadStatus(buildConnectionInfo(), input);

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
    void testMonitorDssFileGeneration() throws IOException {
        String initialCreatedDownload = "cumulus/json/created_download.json";
        String completedDownloadResource = "cumulus/json/completed_download.json";
        launchMockServerWithResource(completedDownloadResource);
        CumulusDssFileController controller = new CumulusDssFileController();
        Download createdDownload = getDownloadFromResource(initialCreatedDownload);
        Download downloadStatus = controller.monitorDssFileGeneration(buildConnectionInfo(), createdDownload, buildGenerationListener()).join();
        assertNotNull(downloadStatus);
        assertEquals(createdDownload.getId(), downloadStatus.getId());
        assertEquals(100, downloadStatus.getProgress());
        assertEquals("SUCCESS", downloadStatus.getStatus());
        assertEquals("cumulus/dss/input/forecast.dss", downloadStatus.getFile());
    }

    @Test
    void testGenerateDssFile() throws IOException {
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
        CumulusDssFileController controller = new CumulusDssFileController();
        OAuth2Token oAuth2Token = new OAuth2Token();
        oAuth2Token.setAccessToken(token);
        Download download = controller.generateDssFile(buildConnectionInfo(), downloadRequest, oAuth2Token, buildGenerationListener()).join();
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
        AtomicReference<Throwable> exception = new AtomicReference<>();
        controller.generateDssFile(buildConnectionInfo(), null, oAuth2Token, buildGenerationListener())
            .exceptionally(ex -> {
                if (ex != null) {
                    exception.set(ex.getCause());
                }
                return null;
            })
            .join();
        assertNotNull(exception.get());
        assertEquals("Missing required Download Request in order to generate DSS File", exception.get().getMessage());
    }

    @Test
    void testDownloadToLocalFileNullFilePath() throws IOException {
        String resource = "cumulus/json/completed_download.json";
        CumulusDssFileController controller = new CumulusDssFileController();
        Download completedDownloadData = getDownloadFromResource(resource);
        assertNotNull(completedDownloadData);
        AtomicReference<Throwable> exception = new AtomicReference<>();
        controller.downloadToLocalFile(completedDownloadData, null, buildDownloadListener())
            .exceptionally(ex -> {
                exception.set(ex.getCause());
                return null;
                })
            .join();
        assertNotNull(exception.get());
        assertEquals("Local file path is required for download. Download failed for: cumulus/dss/input/forecast.dss", exception.get().getMessage());
    }

    private CumulusDssFileDownloadListener buildDownloadListener() {
        return new CumulusDssFileDownloadListener() {
            @Override
            public void bytesRead(Download downloadData, int currentBytesRead, int totalBytesRead) {
                //noop
            }

            @Override
            public void downloadComplete() {
                //noop
            }

            @Override
            public void elapsedDownloadTimeUpdated(long elapsedTime) {

            }
        };
    }

    static CumulusDssFileGenerationListener buildGenerationListener() {
        return new CumulusDssFileGenerationListener() {

            @Override
            public void downloadStatusUpdated(Download downloadStatus) {
                //noop
            }

            @Override
            public void downloadStatusQueryCountUpdated(int queryCount) {
                //noop
            }

            @Override
            public void elapsedGenerationTimeUpdated(long timeMillis) {
                //noop
            }
        };
    }

}


