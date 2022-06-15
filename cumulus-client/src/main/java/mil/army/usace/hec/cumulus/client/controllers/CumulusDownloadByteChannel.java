package mil.army.usace.hec.cumulus.client.controllers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import mil.army.usace.hec.cumulus.client.model.Download;

final class CumulusDownloadByteChannel implements ReadableByteChannel {

    private final ReadableByteChannel readableByteChannel;
    private final CumulusDssFileDownloadListener listener;
    private final Download downloadData;
    private int bytesReadSoFar;
    private long startTime = 0L;

    CumulusDownloadByteChannel(ReadableByteChannel readableByteChannel, Download downloadData, CumulusDssFileDownloadListener listener) {
        this.readableByteChannel = readableByteChannel;
        this.downloadData = downloadData;
        this.listener = listener;
    }

    @Override
    public int read(ByteBuffer byteBuffer) throws IOException {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        int newBytesRead = readableByteChannel.read(byteBuffer);
        notifyBytesRead(newBytesRead, System.currentTimeMillis() - startTime);
        return newBytesRead;
    }

    private void notifyBytesRead(int newBytesRead, long elapsedTimeMillis) {
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
