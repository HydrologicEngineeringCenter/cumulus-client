package mil.army.usace.hec.cumulus.client.controllers;

import mil.army.usace.hec.cumulus.client.model.Download;

public interface CumulusDssFileDownloadListener {

    void bytesRead(Download downloadData, int currentBytesRead, int totalBytesRead, long elapsedTime);

    void downloadComplete();

}
