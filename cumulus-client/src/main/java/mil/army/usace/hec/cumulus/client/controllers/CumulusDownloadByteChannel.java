package mil.army.usace.hec.cumulus.client.controllers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.function.IntConsumer;

final class CumulusDownloadByteChannel implements ReadableByteChannel {

    private final ReadableByteChannel readableByteChannel;
    private final IntConsumer bytesReadConsumer;
    private int bytesReadSoFar;

    CumulusDownloadByteChannel(ReadableByteChannel readableByteChannel, IntConsumer bytesReadConsumer) {
        this.readableByteChannel = readableByteChannel;
        this.bytesReadConsumer = bytesReadConsumer;
    }

    @Override
    public int read(ByteBuffer byteBuffer) throws IOException {
        int newBytesRead = readableByteChannel.read(byteBuffer);
        notifyBytesRead(newBytesRead);
        return newBytesRead;
    }

    private void notifyBytesRead(int bytesRead) {
        if (bytesRead <= 0) {
            return;
        }
        bytesReadSoFar += bytesRead;
        bytesReadConsumer.accept(bytesReadSoFar);
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
