package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusEndpointConstants.ACCEPT_HEADER_V1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import mil.army.usace.hec.cumulus.client.model.Download;
import mil.army.usace.hec.cumulus.client.model.DownloadRequest;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;

public class DownloadsController {

    private static final String DOWNLOADS_ENDPOINT = "downloads";
    private static final String MY_DOWNLOADS_ENDPOINT = "my_downloads";

    /**
     * Retrieve Download (primarily used for obtaining download status).
     *
     * @param apiConnectionInfo    - connection info
     * @param downloadsEndpointInput - download endpoint input containing download id
     * @return Download
     * @throws IOException - thrown if retrieve failed
     */
    public Download retrieveDownload(ApiConnectionInfo apiConnectionInfo, DownloadsEndpointInput downloadsEndpointInput)
        throws IOException {
        HttpRequestResponse response =
            new HttpRequestBuilderImpl(apiConnectionInfo, DOWNLOADS_ENDPOINT + "/" + downloadsEndpointInput.getDownloadId())
                .get()
                .withMediaType(ACCEPT_HEADER_V1)
                .execute();
        return CumulusObjectMapper.mapJsonToObject(response.getBody(), Download.class);
    }

    /**
     * Retrieve Downloads associated with logged-in user's account.
     *
     * @param apiConnectionInfo    - connection info
     * @return List of Downloads
     * @throws IOException - thrown if retrieve failed
     */
    public List<Download> retrieveMyDownloads(ApiConnectionInfo apiConnectionInfo)
        throws IOException {
        HttpRequestResponse response =
            new HttpRequestBuilderImpl(apiConnectionInfo, MY_DOWNLOADS_ENDPOINT)
                .get()
                .withMediaType(ACCEPT_HEADER_V1)
                .execute();
        return CumulusObjectMapper.mapJsonToListOfObjects(response.getBody(), Download.class);
    }

    @Deprecated
    void downloadDssFile(Download download, String fileName) throws IOException {
        URLConnection connection = new URL(download.getFile()).openConnection();
        try (ReadableByteChannel readableByteChannel = Channels.newChannel(connection.getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
    }

    /**
     * Create a Download request.
     * @param apiConnectionInfo - connection info
     * @param downloadRequest - Download Request object containing start, end, watershed ID, and product IDs
     * @return Download object containing URL to DSS File
     * @throws IOException - thrown if POST request fails
     */
    public Download createDownload(ApiConnectionInfo apiConnectionInfo, DownloadRequest downloadRequest)
        throws IOException {
        String jsonBody = CumulusObjectMapper.mapObjectToJson(downloadRequest);
        HttpRequestResponse response =
            new HttpRequestBuilderImpl(apiConnectionInfo, MY_DOWNLOADS_ENDPOINT)
                .post()
                .withBody(jsonBody)
                .withMediaType(ACCEPT_HEADER_V1)
                .execute();
        return CumulusObjectMapper.mapJsonToObject(response.getBody(), Download.class);
    }


    /**
     * Download File to specified local file location.
     *
     * @param apiConnectionInfo - connection info
     * @param downloadRequest - Download Request object containing start, end, watershed ID, and product IDs
     * @param pathToDownloadTo - path in which file will be downloaded to
     * @throws IOException - thrown if download failed
     */
    public CumulusFileDownloader download(ApiConnectionInfo apiConnectionInfo, DownloadRequest downloadRequest, Path pathToDownloadTo)
        throws IOException {

        Download initialDownloadStatus = createDownload(apiConnectionInfo, downloadRequest);
        final CumulusFileDownloader cumulusFileDownloader = new CumulusFileDownloader(apiConnectionInfo, initialDownloadStatus, pathToDownloadTo);
        CompletableFuture
            .supplyAsync(cumulusFileDownloader::generateDownloadableFile)
            .thenAccept(cumulusFileDownloader::downloadFileToLocal);
        return cumulusFileDownloader;
    }
}
