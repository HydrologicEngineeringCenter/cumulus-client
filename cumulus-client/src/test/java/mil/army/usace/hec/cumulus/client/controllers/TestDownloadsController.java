package mil.army.usace.hec.cumulus.client.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import mil.army.usace.hec.cumulus.client.model.Download;
import mil.army.usace.hec.cumulus.client.model.DownloadRequest;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
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
    void testRetrieveMyDownloads() throws IOException {
        String resource = "cumulus/json/my_downloads.json";
        launchMockServerWithResource(resource);

        List<Download> myDownloads = new DownloadsController().retrieveMyDownloads(buildConnectionInfo());

        assertNotNull(myDownloads);
        assertEquals(2, myDownloads.size());
        Download download = myDownloads.get(0);
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

        Download download2 = myDownloads.get(1);
        assertEquals("597f6eca-6276-4993-bfeb-53cbbbba6f08", download2.getId());
        assertEquals("853487e7-10bc-4e69-b3b2-4da33721ea3e", download2.getSub());
        assertEquals("2020-12-01T01:00:01Z", download2.getDateTimeStart().toString());
        assertEquals("2020-12-15T01:00:02Z", download2.getDateTimeEnd().toString());
        assertEquals("95e7713a-ccd6-432d-b2f0-972422511171", download2.getWatershedId());
        assertEquals(1, download2.getProductId().length);
        assertEquals("74756f41-75e2-40ce-b91a-fda5aeb441fc", download2.getProductId()[0]);
        assertEquals("5e949624-bc0f-439e-a9f2-25a23938812c", download2.getStatusId());
        assertEquals("SUCCESS", download2.getStatus());
        assertEquals(100, download2.getProgress());
        assertEquals("file:/J:/git/cumulus-client/cumulus-client/build/resources/test/cumulus/dss/input/forecast.dss", download2.getFile());
        assertEquals("2022-03-01T14:15:22Z", download2.getProcessingStart().toString());
        assertEquals("2022-03-01T14:45:50Z", download2.getProcessingEnd().toString());
        assertEquals("fake-river", download2.getWatershedSlug());
        assertEquals("Fake River", download2.getWatershedName());
    }

    @Test
    void testDownloadDss() throws IOException, URISyntaxException {
        Path outputFilePath = null;
        try {
            String myDownloadsResource = "cumulus/json/my_downloads.json";
            launchMockServerWithResource(myDownloadsResource);
            outputFilePath = createOutputDssPath();
            if(!Files.exists(outputFilePath)) {
                Files.createDirectory(outputFilePath);
            }
            String outputFileName = outputFilePath + "/forecast.dss";
            DownloadsController downloadsController = new DownloadsController();
            List<Download> myDownloads = downloadsController.retrieveMyDownloads(buildConnectionInfo());
            Download download = myDownloads.get(1);
            downloadsController.downloadDssFile(download, outputFileName);
            File outputDssFile = new File(outputFileName);
            String outputContents = readFile(outputDssFile.getPath());
            assertTrue(outputDssFile.exists());
            assertEquals("This is a test file", outputContents);
        } finally {
           if(outputFilePath != null) {
               Files.deleteIfExists(Paths.get(outputFilePath + "/forecast.dss"));
           }
        }
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
        assertEquals(100, download.getProgress());
        assertEquals("file:/J:/git/cumulus-client/cumulus-client/build/resources/test/cumulus/dss/input/forecast.dss", download.getFile());
        assertEquals("2022-03-01T14:15:22Z", download.getProcessingStart().toString());
        assertEquals("2022-03-01T14:45:50Z", download.getProcessingEnd().toString());
        assertEquals("fake-river", download.getWatershedSlug());
        assertEquals("Fake River", download.getWatershedName());
    }

    @Test
    void testDownload() throws IOException, URISyntaxException {
        //testing creation of download in conjunction with download of dss
        String resource = "cumulus/json/created_download.json";
        launchMockServerWithResource(resource);
        Path outputFilePath = null;
        try {
            String watershedId = "95e7713a-ccd6-432d-b2f0-972422511171";
            List<String> productIds = new ArrayList<>();
            productIds.add("74756f41-75e2-40ce-b91a-fda5aeb441fc");
            ZonedDateTime start = ZonedDateTime.of(2022, 3, 1, 14, 15, 22, 0, ZoneId.of("UTC"));
            ZonedDateTime end = ZonedDateTime.of(2022, 4, 1, 14, 45, 50, 0, ZoneId.of("UTC"));
            outputFilePath = createOutputDssPath();
            if (!Files.exists(outputFilePath)) {
                Files.createDirectory(outputFilePath);
            }
            String outputFileName = outputFilePath + "/forecast.dss";
            DownloadRequest downloadRequest = new DownloadRequest(start, end, watershedId, productIds);

            downloadToFile(buildConnectionInfo(), downloadRequest, outputFileName);
            File outputDssFile = new File(outputFileName);
            String outputContents = readFile(outputDssFile.getPath());

            assertTrue(outputDssFile.exists());
            assertEquals("This is a test file", outputContents);
        } finally {
            if(outputFilePath != null) {
                Files.deleteIfExists(Paths.get(outputFilePath + "/forecast.dss"));
                Files.deleteIfExists(outputFilePath);
            }
        }
    }

    private void downloadToFile(ApiConnectionInfo apiConnectionInfo, DownloadRequest downloadRequest, String fileName) throws IOException {
        DownloadsController downloadsController = new DownloadsController();
        Download download = downloadsController.createDownload(apiConnectionInfo, downloadRequest);

        downloadsController.downloadDssFile(download, fileName);
    }

    private Path createOutputDssPath() throws URISyntaxException {
        URI outputDssURI = Objects.requireNonNull(getClass().getClassLoader().getResource("cumulus/dss/output")).toURI();
        String mainPath = Paths.get(outputDssURI).toString();
        Path path = Paths.get(mainPath);
        return Paths.get(path.toAbsolutePath().toString());
    }

    private String readFile(String path)
        throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }
}


