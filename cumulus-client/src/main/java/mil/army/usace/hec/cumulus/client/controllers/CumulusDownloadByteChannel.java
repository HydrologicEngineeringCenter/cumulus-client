package mil.army.usace.hec.cumulus.client.controllers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.time.Duration;
import java.time.Instant;
import mil.army.usace.hec.cumulus.client.model.Download;

final class CumulusDownloadByteChannel implements ReadableByteChannel {

    private final ReadableByteChannel readableByteChannel;
    private final CumulusDssFileDownloadListener listener;
    private final Download downloadData;
    private int bytesReadSoFar;
    private Instant startTime;

    CumulusDownloadByteChannel(ReadableByteChannel readableByteChannel, Download downloadData, CumulusDssFileDownloadListener listener) {
        this.readableByteChannel = readableByteChannel;
        this.downloadData = downloadData;
        this.listener = listener;
    }

    @Override
    public int read(ByteBuffer byteBuffer) throws IOException {
        if (startTime == null) {
            startTime = Instant.now();
        }
        int newBytesRead = readableByteChannel.read(byteBuffer);
        Duration duration = Duration.between(Instant.now(), startTime);
        notifyBytesRead(newBytesRead, duration);
        return newBytesRead;
    }

    private void notifyBytesRead(int newBytesRead, Duration elapsedTimeMillis) {
        if (newBytesRead <= 0) {
            return;
        }
        bytesReadSoFar += newBytesRead;
        listener.bytesRead(downloadData, newBytesRead, bytesReadSoFar, elapsedTimeMillis);
    }

    @Override
    public boolean isOpen() {
        return readableByteChannel.isOpen();
    }

    @Override
    public void close() throws IOException {
        readableByteChannel.close();
    }
}
