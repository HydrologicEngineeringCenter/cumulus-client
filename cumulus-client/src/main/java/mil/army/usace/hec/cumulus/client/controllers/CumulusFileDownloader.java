package mil.army.usace.hec.cumulus.client.controllers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import mil.army.usace.hec.cumulus.client.model.Download;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;

public final class CumulusFileDownloader {

    private static final int MAX_TRIES = 30;
    private static final int MAX_ALLOWED_TIME_IN_SECONDS = 300;
    private final List<CumulusDownloadListener> listeners = new ArrayList<>();
    private final DownloadsEndpointInput downloadsEndpointInput;
    private final ApiConnectionInfo apiConnectionInfo;
    private final Path pathToLocalFile;

    public CumulusFileDownloader(ApiConnectionInfo apiConnectionInfo, Download download, Path pathToDownloadTo) {
        this.apiConnectionInfo = apiConnectionInfo;
        this.downloadsEndpointInput = new DownloadsEndpointInput(download.getId());
        this.pathToLocalFile = Objects.requireNonNull(pathToDownloadTo, "Local file path is required for download");
    }

    public void addListener(CumulusDownloadListener listener) {
        listeners.add(listener);
    }

    Download generateDownloadableFile() {
        long startTime = System.currentTimeMillis();
        long elapsedTime;
        long maxTimeInMillis = TimeUnit.SECONDS.toMillis(MAX_ALLOWED_TIME_IN_SECONDS);
        Download downloadStatus = null;
        for (int counter = 1; counter < MAX_TRIES; counter++) {
            downloadStatus = null;
            try {
                downloadStatus = new DownloadsController().retrieveDownload(apiConnectionInfo, downloadsEndpointInput);
            } catch (IOException e) {
                notifyErrorOccurred(e);
                break;
            }
            int progress = downloadStatus.getProgress();
            notifyStatusChanged(downloadStatus.getStatus());
            notifyProgressChanged(progress);
            elapsedTime = (new Date()).getTime() - startTime;
            if (progress == 100 || elapsedTime > maxTimeInMillis) {
                break;
            }
        }
        return downloadStatus;
    }

    void downloadFileToLocal(Download downloadContainingFile) {
        try {
            URLConnection connection = new URL(downloadContainingFile.getFile()).openConnection();
            notifyFileSizeSpecified(connection.getContentLength());
            readFileFromUrlToLocal(connection);
        } catch (IOException e) {
            notifyErrorOccurred(e);
        }
    }

    private void readFileFromUrlToLocal(URLConnection connection) throws IOException {
        try (ReadableByteChannel readableByteChannel = Channels.newChannel(connection.getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream(pathToLocalFile.toString())) {
            new CumulusDownloadByteChannel(readableByteChannel, this::notifyBytesRead);
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
    }

    private void notifyFileSizeSpecified(int fileSize) {
        for (CumulusDownloadListener listener : listeners) {
            listener.fileSizeSpecified(fileSize);
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

    private void notifyProgressChanged(Integer progress) {
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
