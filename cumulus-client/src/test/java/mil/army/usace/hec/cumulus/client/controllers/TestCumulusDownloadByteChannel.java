package mil.army.usace.hec.cumulus.client.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class TestCumulusDownloadByteChannel {

    @Test
    void testReadWithNotify() {
        AtomicInteger currentBytesRead = new AtomicInteger();
        AtomicLong totalBytesRead = new AtomicLong();
        AtomicReference<Duration> elapsedTime = new AtomicReference<>();
        CumulusDssFileDownloadListener listener = (downloadData, cbr, tbr, elapsed) -> {
            currentBytesRead.set(cbr);
            totalBytesRead.set(tbr);
            elapsedTime.set(elapsed);
        };
        CumulusDownloadByteChannel cumulusByteChannel = new CumulusDownloadByteChannel(null, null, listener);
        cumulusByteChannel.notifyBytesRead(100, Instant.MIN);
        assertEquals(100, currentBytesRead.get());
        assertEquals(100, totalBytesRead.get());
        assertTrue(elapsedTime.get().compareTo(Duration.ZERO) > 0);

    }

}
