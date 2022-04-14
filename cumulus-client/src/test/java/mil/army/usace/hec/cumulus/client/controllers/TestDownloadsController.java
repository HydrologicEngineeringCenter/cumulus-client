package mil.army.usace.hec.cumulus.client.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import mil.army.usace.hec.cumulus.client.model.Download;
import mil.army.usace.hec.cumulus.client.model.DownloadRequest;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestDownloadsController extends TestController{

    private static Path outputFilePath;

    @BeforeEach
    void buildFilePath() throws IOException, URISyntaxException {
        outputFilePath = createOutputDssPath();
    }

    @AfterEach
    void clearFilePath() throws IOException {
        if(outputFilePath != null) {
            Files.deleteIfExists(Paths.get(outputFilePath + "/forecast.dss"));
            Files.deleteIfExists(outputFilePath);
        }
    }

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
        String watershedId = "95e7713a-ccd6-432d-b2f0-972422511171";
        List<String> productIds = new ArrayList<>();
        productIds.add("74756f41-75e2-40ce-b91a-fda5aeb441fc");
        ZonedDateTime start = ZonedDateTime.of(2022, 4, 1, 1, 1, 1, 1, ZoneId.of("UTC"));
        ZonedDateTime end = ZonedDateTime.of(2022, 4, 1, 1, 6, 1, 1, ZoneId.of("UTC"));
        DownloadRequest downloadRequest = new DownloadRequest(start, end, watershedId, productIds);

        Download download = new DownloadsController().createDownload(buildConnectionInfo(), downloadRequest);
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
        String watershedId = "95e7713a-ccd6-432d-b2f0-972422511171";
        List<String> productIds = new ArrayList<>();
        productIds.add("74756f41-75e2-40ce-b91a-fda5aeb441fc");
        ZonedDateTime start = ZonedDateTime.of(2022, 3, 1, 14, 15, 22, 0, ZoneId.of("UTC"));
        ZonedDateTime end = ZonedDateTime.of(2022, 4, 1, 14, 45, 50, 0, ZoneId.of("UTC"));
        DownloadRequest downloadRequest = new DownloadRequest(start, end, watershedId, productIds);
        CumulusFileDownloader fileDownloader = new DownloadsController().download(buildConnectionInfo(), downloadRequest, outputFilePath);
        assertNotNull(fileDownloader); // CumulusFileDownloader is tested in its own test class, just ensure returned object is not null
    }

    @Test
    void testMockAsyncDownload() throws IOException, ExecutionException, InterruptedException {
        String outputContents = readFile(outputFilePath);
        CompletableFuture<Void> future = mockDownload(buildConnectionInfo(), outputFilePath);
        assertEquals("", outputContents); //because of async process, nothing has been downloaded yet
        future.get(); //once async download process finishes, we should have downloaded file
        outputContents = readFile(outputFilePath);
        assertEquals("This is a test file", outputContents);
    }

    private CompletableFuture<Void> mockDownload(ApiConnectionInfo apiConnectionInfo, Path pathToDownloadTo)
        throws IOException {
        Download initialDownloadStatus = getDownloadFromResource("cumulus/json/created_download.json");
        CumulusFileDownloader cumulusFileDownloader = new CumulusFileDownloader(apiConnectionInfo, initialDownloadStatus, pathToDownloadTo);
        return mockAsyncDownload(cumulusFileDownloader);
    }


    private CompletableFuture<Void> mockAsyncDownload(CumulusFileDownloader cumulusFileDownloader) throws IOException {
        Download completedDownloadStatus = getDownloadFromResource("cumulus/json/completed_download.json");
        return CompletableFuture
            .supplyAsync(() -> completedDownloadStatus)
            .thenAccept(cumulusFileDownloader::downloadFileToLocal);
    }

    private Download getDownloadFromResource(String resource) throws IOException {
        URL resourceUrl = getClass().getClassLoader().getResource(resource);
        if (resourceUrl == null) {
            throw new IOException("Failed to get resource: " + resource);
        }
        Path path = new File(resourceUrl.getFile()).toPath();
        String createdDownloadJson = String.join("\n", Files.readAllLines(path));
        return CumulusObjectMapper.mapJsonToObject(createdDownloadJson, Download.class);
    }

    private Path createOutputDssPath() throws URISyntaxException, IOException {
        URI outputDssURI = Objects.requireNonNull(getClass().getClassLoader().getResource("cumulus/dss/output")).toURI();
        String mainPath = Paths.get(outputDssURI).toString();
        Path path = Paths.get(mainPath);
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
        path = Paths.get(path + "/downloaded_forecast.dss");
        return Paths.get(path.toAbsolutePath().toString());
    }

    private String readFile(Path path)
    {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded, StandardCharsets.UTF_8);
    }
}


