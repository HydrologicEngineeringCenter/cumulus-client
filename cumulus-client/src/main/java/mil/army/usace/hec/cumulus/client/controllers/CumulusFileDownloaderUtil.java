package mil.army.usace.hec.cumulus.client.controllers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import mil.army.usace.hec.cumulus.client.model.Download;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;
import mil.army.usace.hec.cwms.http.client.request.HttpRequestExecutor;

public final class CumulusFileDownloaderUtil {

    private CumulusFileDownloaderUtil() {
        throw new AssertionError("Utility Class");
    }

    static void downloadFileToLocal(Download downloadContainingFile, Path pathToLocalFile, CumulusDssFileDownloadListener listener)
        throws IOException {
        if (pathToLocalFile == null) {
            throw new IOException("Local file path is required for download. Download failed for: " + downloadContainingFile.getFile());
        }
        String url = downloadContainingFile.getFile();
        if (url != null) {
            ApiConnectionInfo connectionInfo = new ApiConnectionInfo(url);
            HttpRequestExecutor httpRequestExecutor = new HttpRequestBuilderImpl(connectionInfo, "")
                .enableHttp2()
                .get()
                .withMediaType("text/plain");
            executeDownload(httpRequestExecutor, pathToLocalFile, downloadContainingFile, listener);
        }
    }

    private static void executeDownload(HttpRequestExecutor httpRequestExecutor, Path pathToLocalFile,
                                        Download downloadContainingFile, CumulusDssFileDownloadListener listener)
        throws IOException {
        try (HttpRequestResponse response = httpRequestExecutor.execute()) {
            InputStream inputStream = response.getStream();
            readFileFromUrlToLocal(inputStream, pathToLocalFile, downloadContainingFile, listener);
        }
    }

    private static void readFileFromUrlToLocal(InputStream inputStream, Path pathToLocalFile,
                                               Download downloadContainingFile, CumulusDssFileDownloadListener listener)
        throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(pathToLocalFile.toString());
             CumulusDownloadByteChannel byteChannel =
                 new CumulusDownloadByteChannel(Channels.newChannel(inputStream), downloadContainingFile, listener)) {
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileChannel.transferFrom(byteChannel, 0, Long.MAX_VALUE);
            if (listener != null) {
                listener.downloadComplete();
            }
        }
    }

}
