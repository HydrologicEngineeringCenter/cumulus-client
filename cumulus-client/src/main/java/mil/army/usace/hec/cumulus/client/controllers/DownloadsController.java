package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusEndpointConstants.ACCEPT_HEADER_V1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
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

    /**
     * Download DSS to specified file.
     *
     * @param download - Download object containing dss file
     * @param fileName - name of file to download to
     * @throws IOException - thrown if download failed
     */
    public void downloadDssFile(Download download, String fileName) throws IOException {
        URL url = new URL(download.getFile());
        try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
    }

    /**
     * Create a Download request
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
}
