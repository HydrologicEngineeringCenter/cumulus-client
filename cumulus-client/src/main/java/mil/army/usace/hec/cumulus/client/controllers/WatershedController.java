package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusEndpointConstants.ACCEPT_HEADER_V1;

import java.io.IOException;
import java.util.List;
import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import mil.army.usace.hec.cumulus.client.model.Watershed;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;

public class WatershedController {

    private static final String WATERSHEDS_ENDPOINT = "watersheds";

    /**
     * Retrieve Watershed.
     *
     * @param apiConnectionInfo    - connection info
     * @param watershedEndpointInput - watershed endpoint input containing watershed id
     * @return Watershed
     * @throws IOException - thrown if retrieve failed
     */
    public Watershed retrieveWatershed(ApiConnectionInfo apiConnectionInfo, WatershedEndpointInput watershedEndpointInput)
        throws IOException {
        HttpRequestResponse response =
            new HttpRequestBuilderImpl(apiConnectionInfo, WATERSHEDS_ENDPOINT + "/" + watershedEndpointInput.getWatershedId())
                .get()
                .withMediaType(ACCEPT_HEADER_V1)
                .execute();
        return CumulusObjectMapper.mapJsonToObject(response.getBody(), Watershed.class);
    }

    /**
     * Retrieve All Watersheds.
     *
     * @param apiConnectionInfo    - connection info
     * @return List of Watersheds
     * @throws IOException - thrown if retrieve failed
     */
    public List<Watershed> retrieveAllWatersheds(ApiConnectionInfo apiConnectionInfo)
        throws IOException {
        HttpRequestResponse response =
            new HttpRequestBuilderImpl(apiConnectionInfo, WATERSHEDS_ENDPOINT)
                .get()
                .withMediaType(ACCEPT_HEADER_V1)
                .execute();
        return CumulusObjectMapper.mapJsonToListOfObjects(response.getBody(), Watershed.class);
    }


}
