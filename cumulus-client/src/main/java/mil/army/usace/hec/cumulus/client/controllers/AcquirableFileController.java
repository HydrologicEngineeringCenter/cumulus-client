package mil.army.usace.hec.cumulus.client.controllers;

import java.io.IOException;
import java.util.List;
import mil.army.usace.hec.cumulus.client.model.AcquirableFile;
import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;

public class AcquirableFileController {

    private static final String ACQUIRABLES_ENDPOINT = "acquirables";
    private static final String FILES_ENDPOINT = "files";

    /**
     * Retrieve Acquirable Files.
     *
     * @param apiConnectionInfo           - connection info
     * @param acquirableFileEndpointInput - acquirable id, after date, before date
     * @return List of AcquirableFile objects
     * @throws IOException - thrown if retrieve failed
     */
    public List<AcquirableFile> retrieveAcquirableFiles(ApiConnectionInfo apiConnectionInfo, AcquirableFileEndpointInput acquirableFileEndpointInput)
        throws IOException {
        HttpRequestResponse response =
            new HttpRequestBuilderImpl(apiConnectionInfo, ACQUIRABLES_ENDPOINT + "/"
                + acquirableFileEndpointInput.getFileId() + "/"
                + FILES_ENDPOINT)
                .addEndpointInput(acquirableFileEndpointInput)
                .execute();
        return CumulusObjectMapper.mapJsonToListOfObjects(response.getBody(), AcquirableFile.class);
    }
}
