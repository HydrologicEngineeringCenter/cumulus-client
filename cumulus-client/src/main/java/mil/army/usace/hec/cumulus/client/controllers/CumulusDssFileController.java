package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusEndpointConstants.ACCEPT_HEADER_V1;

import hec.army.usace.hec.cumulus.http.client.AuthenticatedHttpRequestBuilderImpl;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import mil.army.usace.hec.cumulus.client.model.Download;
import mil.army.usace.hec.cumulus.client.model.DownloadRequest;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;
import mil.army.usace.hec.cwms.http.client.model.OAuth2Token;
import mil.army.usace.hec.cwms.http.client.request.HttpRequestExecutor;

public class CumulusDssFileController {

    public static final String DOWNLOADS_ENDPOINT = "downloads";
    private static final int MAX_ALLOWED_TIME_SECONDS = 300;
    private static final String PROGRESS_INTERVAL_PROPERTY_KEY = "cumulus.progress.interval.millis";


    /**
     * Retrieve Download (Used for obtaining download status).
     *
     * @param apiConnectionInfo      - connection info
     * @param downloadsEndpointInput - download endpoint input containing download id
     * @return Download
     */
    Download queryDownloadStatus(ApiConnectionInfo apiConnectionInfo, DownloadsEndpointInput downloadsEndpointInput) throws IOException {
        HttpRequestExecutor executor =
            new HttpRequestBuilderImpl(apiConnectionInfo, DOWNLOADS_ENDPOINT + "/" + downloadsEndpointInput.getDownloadId())
                .enableHttp2()
                .get()
                .withMediaType(ACCEPT_HEADER_V1);
        try (HttpRequestResponse response = executor.execute()) {
            return CumulusObjectMapper.mapJsonToObject(response.getBody(), Download.class);
        }
    }

    /**
     * Retrieve Download (Used for obtaining download status).
     *
     * @param apiConnectionInfo    - connection info
     * @param downloadData         - download object containing download id that we are monitoring status of
     * @return Download            - return Download object containing URL once DSS file is successfully generated
     * @throws CompletionException - thrown if IOException Occurred during monitor process
     */
    public CompletableFuture<Download> monitorDssFileGeneration(ApiConnectionInfo apiConnectionInfo, Download downloadData,
                                                                CumulusDssFileGenerationListener listener) throws CompletionException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return monitorAndRetrieveDownloadDataWhenComplete(apiConnectionInfo, downloadData, listener);
            } catch (IOException ex) {
                throw new CompletionException(ex);
            }
        });
    }

    /**
     * Generate DSS File.
     *
     * @param apiConnectionInfo - connection info
     * @param downloadRequest   - Download Request object containing start, end, watershed ID, and product IDs
     * @return Download         - object that can be queried for status updates
     */
    public CompletableFuture<Download> generateDssFile(ApiConnectionInfo apiConnectionInfo, DownloadRequest downloadRequest,
                                                       OAuth2Token token, CumulusDssFileGenerationListener listener) throws CompletionException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (downloadRequest == null) {
                    throw new IOException("Missing required Download Request in order to generate DSS File");
                }
                Download initialDownloadData = executeInitialDownloadRequest(apiConnectionInfo, downloadRequest, token);
                if (listener != null) {
                    listener.downloadStatusUpdated(initialDownloadData);
                }
                return initialDownloadData;
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    Download executeInitialDownloadRequest(ApiConnectionInfo apiConnectionInfo, DownloadRequest downloadRequest, OAuth2Token token) throws IOException {
        String jsonBody = CumulusObjectMapper.mapObjectToJson(downloadRequest);
        HttpRequestExecutor executor =
            new AuthenticatedHttpRequestBuilderImpl(apiConnectionInfo, DOWNLOADS_ENDPOINT, token)
                .enableHttp2()
                .post()
                .withBody(jsonBody)
                .withMediaType(ACCEPT_HEADER_V1);
        try (HttpRequestResponse response = executor.execute()) {
            return CumulusObjectMapper.mapJsonToObject(response.getBody(), Download.class);
        }
    }

    /**
     * Download File to specified local file location.
     *
     * @param generatedDssFileDownloadData - Download object containing URL of file to download
     * @param pathToLocalFile              - path in which file will be downloaded to
     * @param listener                     - CumulusDssFileDownloadListener implementation that listens for download updates
     * @return CompletableFuture           - Reference to async download process to be handled by caller
     */
    public CompletableFuture<Void> downloadToLocalFile(Download generatedDssFileDownloadData, Path pathToLocalFile,
                                                       CumulusDssFileDownloadListener listener) throws CompletionException {
        if (generatedDssFileDownloadData == null) {
            throw new CompletionException(new IOException("Missing download Data"));
        }
        if (generatedDssFileDownloadData.getFile() == null) {
            throw new CompletionException(new IOException("Missing DSS File URL for Download ID: " + generatedDssFileDownloadData.getId()));
        }
        return CompletableFuture.runAsync(() -> {
            try {
                CumulusFileDownloaderUtil.downloadFileToLocal(generatedDssFileDownloadData, pathToLocalFile, listener);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });

    }

    private Download monitorAndRetrieveDownloadDataWhenComplete(ApiConnectionInfo apiConnectionInfo, Download initialDownloadData,
                                                                CumulusDssFileGenerationListener listener) throws IOException {
        DownloadsEndpointInput downloadsEndpointInput = new DownloadsEndpointInput(initialDownloadData.getId());
        String progressIntervalVal = System.getProperty(PROGRESS_INTERVAL_PROPERTY_KEY);
        if (progressIntervalVal == null) {
            progressIntervalVal = "500";
        }
        int progressInterval = Integer.parseInt(progressIntervalVal);
        long startTime = System.currentTimeMillis();
        long progressStartTime = startTime;
        long elapsedTimeSinceProgressChanged = 0L;
        long maxStallTimeInMillis = TimeUnit.SECONDS.toMillis(MAX_ALLOWED_TIME_SECONDS);
        Download downloadStatus = null;
        int counter = 0;
        String file = null;
        int progress = 0;
        while (file == null && elapsedTimeSinceProgressChanged <= maxStallTimeInMillis) {
            counter++;
            try {
                downloadStatus = queryDownloadStatus(apiConnectionInfo, downloadsEndpointInput);
                file = downloadStatus.getFile();
                long totalElapsedTime = System.currentTimeMillis() - startTime;
                if (listener != null) {
                    listener.downloadStatusUpdated(downloadStatus);
                    listener.downloadStatusUpdated(downloadStatus);
                    listener.downloadStatusQueryCountUpdated(counter);
                    listener.elapsedGenerationTimeUpdated(totalElapsedTime);
                }
                int newProgress = downloadStatus.getProgress();
                if (newProgress > progress) { // if progress increased, set new timer to check for progress stall
                    progressStartTime = System.currentTimeMillis();
                    progress = newProgress;
                }
                elapsedTimeSinceProgressChanged = System.currentTimeMillis() - progressStartTime; //elapsed time since progress last changed

                Thread.sleep(progressInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Unexpected interrupt occurred while monitoring status for Download ID: " + downloadStatus.getId(), e);
            }
        }
        if (downloadStatus != null && downloadStatus.getProgress() < 100) {
            throw new IOException("DSS File Generation timed out for Download ID: " + downloadStatus.getId());
        }
        return downloadStatus; //once completed we return download status object containing URL of file
    }

}
