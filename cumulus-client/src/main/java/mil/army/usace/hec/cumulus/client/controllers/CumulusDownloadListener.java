package mil.army.usace.hec.cumulus.client.controllers;

import java.io.IOException;

public interface CumulusDownloadListener {

    void statusChanged(String status);

    void progressChanged(int progress);

    void bytesReadChanged(int bytesRead);

    void errorOccurred(IOException e);

    void statusCheckChanged(int check);
}
