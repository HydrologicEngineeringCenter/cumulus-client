package mil.army.usace.hec.cumulus.client.controllers;

import java.io.IOException;
import java.util.List;
import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import mil.army.usace.hec.cumulus.client.model.Download;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;

public class DownloadsController {

    private static final String DOWNLOADS_ENDPOINT = "downloads";
    private static final String MY_DOWNLOADS_ENDPOINT = "my_downloads";

    /**
     * Retrieve Download.
     *
     * @param apiConnectionInfo    - connection info
     * @param downloadsEndpointInput - download endpoint input containing download id
     * @return Download
     * @throws IOException - thrown if retrieve failed
     */
    public Download retrieveDownload(ApiConnectionInfo apiConnectionInfo, DownloadsEndpointInput downloadsEndpointInput)
        throws IOException {
        HttpRequestResponse response =
            new HttpRequestBuilderImpl(apiConnectionInfo, DOWNLOADS_ENDPOINT + "/" + downloadsEndpointInput.getDownloadId())
                .execute();
        return CumulusObjectMapper.mapJsonToObject(response.getBody(), Download.class);
    }

    /**
     * Retrieve Downloads associated with logged-in user's account.
     *
     * @param apiConnectionInfo    - connection info
     * @return List of Downloads
     * @throws IOException - thrown if retrieve failed
     */
    public List<Download> retrieveMyDownloads(ApiConnectionInfo apiConnectionInfo)
        throws IOException {
        HttpRequestResponse response =
            new HttpRequestBuilderImpl(apiConnectionInfo, MY_DOWNLOADS_ENDPOINT)
                .execute();
        return CumulusObjectMapper.mapJsonToListOfObjects(response.getBody(), Download.class);
    }
}
