package mil.army.usace.hec.cumulus.client.controllers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.army.usace.hec.cumulus.client.model.Download;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;
import mil.army.usace.hec.cwms.http.client.request.HttpRequestExecutor;

public final class CumulusFileDownloader {

    private static final Logger LOGGER = Logger.getLogger(CumulusFileDownloader.class.getName());
    private static final int MAX_ALLOWED_TIME_SECONDS = 300;
    private static final String PROGRESS_INTERVAL_PROPERTY_KEY = "cumulus.progress.interval.millis";
    private final List<CumulusDownloadListener> listeners = new ArrayList<>();
    private final DownloadsEndpointInput downloadsEndpointInput;
    private final ApiConnectionInfo apiConnectionInfo;
    private final Path pathToLocalFile;
    private final int progressInterval;

    /**
     * Cumulus File Downloader.
     *
     * @param apiConnectionInfo - connection info
     * @param download - Initial Download object
     * @param pathToDownloadTo - path of file to download to
     */
    public CumulusFileDownloader(ApiConnectionInfo apiConnectionInfo, Download download, Path pathToDownloadTo) throws IOException {
        this.apiConnectionInfo = apiConnectionInfo;
        this.downloadsEndpointInput = new DownloadsEndpointInput(download.getId());
        try {
            this.pathToLocalFile = Objects.requireNonNull(pathToDownloadTo, "Local file path is required for download");
        } catch (NullPointerException ex) {
            throw new IOException(ex.getMessage());
        }
        String progressIntervalVal = System.getProperty(PROGRESS_INTERVAL_PROPERTY_KEY);
        if (progressIntervalVal == null) {
            progressIntervalVal = "500";
            System.setProperty(PROGRESS_INTERVAL_PROPERTY_KEY, progressIntervalVal);
        }
        this.progressInterval = Integer.parseInt(progressIntervalVal);
    }

    /**
     * Add Listener to CumulusFileDownloader object.
     *
     * @param listener - listener being added to receive notifications of status updates
     */
    public void addListener(CumulusDownloadListener listener) {
        listeners.add(listener);
    }

    Download generateDownloadableFile() {
        long startTime = System.currentTimeMillis();
        long elapsedTimeSinceProgressChanged = 0L;
        long maxStallTimeInMillis = TimeUnit.SECONDS.toMillis(MAX_ALLOWED_TIME_SECONDS);
        Download downloadStatus = null;
        int counter = 0;
        String file = null;
        int progress = 0;
        while (file == null && elapsedTimeSinceProgressChanged <= maxStallTimeInMillis) {
            counter++;
            try {
                downloadStatus = new DownloadsController().retrieveDownload(apiConnectionInfo, downloadsEndpointInput);
                file = downloadStatus.getFile();
                notifyStatusCheckChanged(counter);
                notifyStatusChanged(downloadStatus.getStatus());
                int newProgress = downloadStatus.getProgress();
                if (newProgress > progress) { // if progress increased, set new timer to check for progress stall
                    startTime = System.currentTimeMillis();
                    progress = newProgress;
                    notifyProgressChanged(newProgress);
                }
                elapsedTimeSinceProgressChanged = (new Date()).getTime() - startTime; //elapsed time since progress last changed
                Thread.sleep(progressInterval);
            } catch (IOException e) {
                notifyErrorOccurred(new IOException(e.getLocalizedMessage()));
                LOGGER.log(Level.SEVERE, "Error Generating Download", e);
            } catch (InterruptedException e) {
                LOGGER.log(Level.FINE, "Interrupted wait between progress checks", e);
                Thread.currentThread().interrupt();
            }
        }
        if (downloadStatus != null && downloadStatus.getProgress() < 100) {
            notifyErrorOccurred(new IOException("Download timed out"));
        }
        return downloadStatus; //once completed we return download status object containing URL of file
    }

    void downloadFileToLocal(Download downloadContainingFile) {
        String file = downloadContainingFile.getFile();
        if (file != null) {
            try {
                ApiConnectionInfo connectionInfo = new ApiConnectionInfo(file);
                HttpRequestExecutor httpRequestExecutor = new HttpRequestBuilderImpl(connectionInfo, "")
                    .get()
                    .withMediaType("text/plain");
                executeDownload(httpRequestExecutor);
            } catch (IOException e) {
                notifyErrorOccurred(e);
                LOGGER.log(Level.SEVERE, "Error downloading file to output file location", e);
            }
        }
    }

    private void executeDownload(HttpRequestExecutor httpRequestExecutor) throws IOException {
        try (HttpRequestResponse response = httpRequestExecutor.execute()) {
            InputStream inputStream = response.getStream();
            readFileFromUrlToLocal(inputStream);
        }
    }

    private void readFileFromUrlToLocal(InputStream inputStream) throws IOException {
        try (ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
             FileOutputStream fileOutputStream = new FileOutputStream(pathToLocalFile.toString())) {
            new CumulusDownloadByteChannel(readableByteChannel, this::notifyBytesRead);
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            notifyStatusChanged("COMPLETE");
        }
    }

    private void notifyStatusCheckChanged(int check) {
        for (CumulusDownloadListener listener : listeners) {
            listener.statusCheckChanged(check);
        }
    }

    private void notifyBytesRead(int bytesRead) {
        for (CumulusDownloadListener listener : listeners) {
            listener.bytesReadChanged(bytesRead);
        }
    }

    private void notifyErrorOccurred(IOException e) {
        for (CumulusDownloadListener listener : listeners) {
            listener.errorOccurred(e);
        }
    }

    private void notifyProgressChanged(int progress) {
        for (CumulusDownloadListener listener : listeners) {
            listener.progressChanged(progress);
        }
    }

    private void notifyStatusChanged(String status) {
        for (CumulusDownloadListener listener : listeners) {
            listener.statusChanged(status);
        }
    }

}
