package mil.army.usace.hec.cumulus.client.controllers;

import mil.army.usace.hec.cumulus.client.model.Download;

public interface CumulusDssFileGenerationListener {

    void downloadStatusUpdated(Download downloadStatus, int queryCount, long timeMillis);

}