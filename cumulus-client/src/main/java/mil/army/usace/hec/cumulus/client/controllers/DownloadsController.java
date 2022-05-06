package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusEndpointConstants.ACCEPT_HEADER_V1;

import hec.army.usace.hec.cumulus.http.client.AuthenticatedHttpRequestBuilderImpl;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import mil.army.usace.hec.cumulus.client.model.Download;
import mil.army.usace.hec.cumulus.client.model.DownloadRequest;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;

public class DownloadsController {

    private static final String DOWNLOADS_ENDPOINT = "downloads";

    /**
     * Retrieve Download (Used for obtaining download status).
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
     * Create a Download request.
     * @param apiConnectionInfo - connection info
     * @param downloadRequest - Download Request object containing start, end, watershed ID, and product IDs
     * @return Download object containing URL to DSS File
     * @throws IOException - thrown if POST request fails
     */
    public Download createDownload(ApiConnectionInfo apiConnectionInfo, DownloadRequest downloadRequest, String token)
        throws IOException {
        String jsonBody = CumulusObjectMapper.mapObjectToJson(downloadRequest);
        HttpRequestResponse response =
            new AuthenticatedHttpRequestBuilderImpl(apiConnectionInfo, DOWNLOADS_ENDPOINT, token)
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
     * @return CumulusFileDownloader - downloader object that can be listened to for status updates
     * @throws IOException - thrown if download failed
     */
    public CumulusFileDownloader download(ApiConnectionInfo apiConnectionInfo, DownloadRequest downloadRequest, Path pathToDownloadTo, String token)
        throws IOException {

        Download initialDownloadStatus = createDownload(apiConnectionInfo, downloadRequest, token);
        CumulusFileDownloader cumulusFileDownloader = new CumulusFileDownloader(apiConnectionInfo, initialDownloadStatus, pathToDownloadTo);
        CompletableFuture.runAsync(() -> performDownload(cumulusFileDownloader));
        return cumulusFileDownloader;
    }

    private void performDownload(CumulusFileDownloader cumulusFileDownloader) {
        Download download = cumulusFileDownloader.generateDownloadableFile();
        cumulusFileDownloader.downloadFileToLocal(download);
    }
}
