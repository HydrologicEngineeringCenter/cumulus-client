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
import java.util.Objects;
import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import mil.army.usace.hec.cumulus.client.model.Download;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestCumulusFileDownloader extends TestController{

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
    void testGenerateDownloadableFile() throws IOException {
        String initialCreatedDownload = "cumulus/json/created_download.json";
        String completedDownloadResource = "cumulus/json/completed_download.json";
        launchMockServerWithResource(completedDownloadResource);
        Download createdDownload = getDownloadFromResource(initialCreatedDownload);
        CumulusFileDownloader fileDownloader = new CumulusFileDownloader(
            buildConnectionInfo(), createdDownload, outputFilePath);
        Download downloadStatus = fileDownloader.generateDownloadableFile();
        assertNotNull(downloadStatus);
        assertEquals(createdDownload.getId(), downloadStatus.getId());
        assertEquals(100, downloadStatus.getProgress());
        assertEquals("SUCCESS", downloadStatus.getStatus());
        assertEquals("file:/J:/git/cumulus-client/cumulus-client/build/resources/test/cumulus/dss/input/forecast.dss", downloadStatus.getFile());
    }

    @Test
    void testDownloadToLocalFile() throws IOException {
        String initialCreatedDownload = "cumulus/json/created_download.json";
        String completedDownloadResource = "cumulus/json/completed_download.json";

        Download createdDownload = getDownloadFromResource(initialCreatedDownload);
        Download completedDownload = getDownloadFromResource(completedDownloadResource);
        CumulusFileDownloader fileDownloader = new CumulusFileDownloader(
            buildConnectionInfo(), createdDownload, outputFilePath);

        fileDownloader.downloadFileToLocal(completedDownload);
        String outputContents = readFile(outputFilePath);

        assertEquals("This is a test file", outputContents);
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
        throws IOException
    {
        byte[] encoded = Files.readAllBytes(path);
        return new String(encoded, StandardCharsets.UTF_8);
    }
}
