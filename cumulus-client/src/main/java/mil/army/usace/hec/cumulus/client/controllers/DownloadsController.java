package mil.army.usace.hec.cumulus.client.controllers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.time.ZonedDateTime;
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

    public void createDownloadRequest(ZonedDateTime start, ZonedDateTime end, String watershedId, List<String> productIds) throws IOException {
        DownloadRequest downloadRequest = new DownloadRequest(start, end, watershedId, productIds.toArray(new String[]{}));
        String json = CumulusObjectMapper.mapObjectToJson(downloadRequest);

        //POST with this json string, which will return Download json, which then we map into Download object?
        //THen pass that object into our download method to get Dss file ?
    }
}
