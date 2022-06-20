package mil.army.usace.hec.cumulus.client.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import mil.army.usace.hec.cumulus.client.model.Download;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestCumulusFileDownloaderUtil extends TestController{

    private static Path outputFilePath;

    @BeforeEach
    void buildFilePath() throws IOException {
        outputFilePath = createOutputFilePath();
    }

    @AfterEach
    void clearFilePath() throws IOException {
        if(outputFilePath != null) {
            Files.deleteIfExists(outputFilePath);
            Path cumulusDir = Paths.get(System.getProperty("user.dir"), "cumulus");
            Path dssDir = Paths.get(System.getProperty("user.dir"), "cumulus/dss");
            Path outputDir = Paths.get(System.getProperty("user.dir"), "cumulus/dss/output");
            Files.deleteIfExists(outputDir);
            Files.deleteIfExists(dssDir);
            Files.deleteIfExists(cumulusDir);
        }
    }

    @Test
    void testDownloadToLocalFile() throws IOException {
        String completedDownloadResource = "cumulus/json/completed_download.json";
        Download completedDownload = getDownloadFromResource(completedDownloadResource);

        mockDownloadFileToLocal(completedDownload, outputFilePath, buildDownloadListener());
        String outputContents = readFile(outputFilePath);

        assertEquals("This is a test file", outputContents);
    }

    private void mockDownloadFileToLocal(Download downloadContainingFile, Path pathToLocalFile, CumulusDssFileDownloadListener listener) throws IOException {
        String file = getMockedGeneratedFile(downloadContainingFile.getFile());
        if (pathToLocalFile == null) {
            throw new IOException("Local file path is required for download");
        }
        URL url = new File(file).toURI().toURL();
        InputStream inputStream = url.openStream();
        mockExecuteDownload(inputStream, downloadContainingFile, listener);
    }

    private static void mockExecuteDownload(InputStream inputStream, Download downloadContainingFile, CumulusDssFileDownloadListener listener)
        throws IOException {
            mockReadFileFromUrlToLocal(inputStream, downloadContainingFile, listener);
    }

    private static void mockReadFileFromUrlToLocal(InputStream inputStream, Download downloadContainingFile, CumulusDssFileDownloadListener listener) throws IOException {
        try (ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
             FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath.toString())) {
            CumulusDownloadByteChannel byteChannel = new CumulusDownloadByteChannel(readableByteChannel, downloadContainingFile, listener);
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileChannel.transferFrom(byteChannel, 0, Long.MAX_VALUE);
        }
    }

    private String getMockedGeneratedFile(String resource) throws IOException {
        URL resourceUrl = getClass().getClassLoader().getResource(resource);
        if (resourceUrl == null) {
            throw new IOException("Failed to get resource: " + resource);
        }
        return new File(resourceUrl.getFile()).toString();
    }

    static Download getDownloadFromResource(String resource) throws IOException {
        URL resourceUrl = TestCumulusFileDownloaderUtil.class.getClassLoader().getResource(resource);
        if (resourceUrl == null) {
            throw new IOException("Failed to get resource: " + resource);
        }
        Path path = new File(resourceUrl.getFile()).toPath();
        String createdDownloadJson = String.join("\n", Files.readAllLines(path));
        return CumulusObjectMapper.mapJsonToObject(createdDownloadJson, Download.class);
    }

    private Path createOutputFilePath() throws IOException {
        String mainPath = Paths.get(System.getProperty("user.dir"), "cumulus/dss/output").toString();
        Path path = Paths.get(mainPath);
        if (!Files.exists(path)) {
            boolean success = path.toFile().mkdirs();
            if(!success) {
                throw new IOException("Failed to create output file path");
            }
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

    private CumulusDssFileDownloadListener buildDownloadListener() {
        return (downloadData, currentBytesRead, totalBytesRead, elapsedTime) -> {
            //noop
        };
    }
}
