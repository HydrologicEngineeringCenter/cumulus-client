package mil.army.usace.hec.cumulus.client.controllers;

import java.time.Duration;
import mil.army.usace.hec.cumulus.client.model.Download;

public interface CumulusDssFileDownloadListener {

    void bytesRead(Download downloadData, int currentBytesRead, long totalBytesRead, Duration elapsedTime);
}
