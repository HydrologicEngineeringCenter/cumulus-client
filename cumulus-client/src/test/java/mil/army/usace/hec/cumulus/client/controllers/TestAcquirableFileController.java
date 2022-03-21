package mil.army.usace.hec.cumulus.client.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import mil.army.usace.hec.cumulus.client.model.AcquirableFile;
import org.junit.jupiter.api.Test;

class TestAcquirableFileController extends TestController {

    @Test
    void testRetrieveAcquirableFiles() throws IOException {
        String resource = "cumulus/json/acquirableFiles.json";
        launchMockServerWithResource(resource);

        String acquirableId = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        Instant after = ZonedDateTime.of(2022,3, 17, 0,0,0,0, ZoneId.of("UTC")).toInstant();
        Instant before = ZonedDateTime.of(2022,3, 19, 0,0,0,0, ZoneId.of("UTC")).toInstant();
        AcquirableFileEndpointInput input = new AcquirableFileEndpointInput(acquirableId, after, before);
        List<AcquirableFile> acquirableFiles = new AcquirableFileController().retrieveAcquirableFiles(buildConnectionInfo(), input);
        assertNotNull(acquirableFiles);
        assertEquals(2, acquirableFiles.size());
        AcquirableFile acquirableFile1 = acquirableFiles.get(0);
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", acquirableFile1.getAcquirableId());
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", acquirableFile1.getId());
        assertEquals("2022-03-18T20:41:27.831Z", acquirableFile1.getDateTime().toString());
        assertEquals("test1.file", acquirableFile1.getFile());
        assertEquals("2022-03-18T20:41:28.831Z", acquirableFile1.getCreateDate().toString());
        assertEquals("2022-03-18T20:41:29.831Z", acquirableFile1.getProcessDate().toString());
        AcquirableFile acquirableFile2 = acquirableFiles.get(1);
        assertEquals("4fa85f64-5717-4562-b3fc-2c963f66afa6", acquirableFile2.getAcquirableId());
        assertEquals("4fa85f64-5717-4562-b3fc-2c963f66afa6", acquirableFile2.getId());
        assertEquals("2022-03-18T20:41:27.832Z", acquirableFile2.getDateTime().toString());
        assertEquals("test2.file", acquirableFile2.getFile());
        assertEquals("2022-03-18T20:41:28.833Z", acquirableFile2.getCreateDate().toString());
        assertEquals("2022-03-18T20:41:29.834Z", acquirableFile2.getProcessDate().toString());
    }
}
