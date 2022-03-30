package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusEndpointConstants.ACCEPT_HEADER_V1;
import static mil.army.usace.hec.cumulus.client.controllers.CumulusEndpointConstants.ACCEPT_QUERY_HEADER;

import java.util.Objects;
import mil.army.usace.hec.cwms.http.client.EndpointInput;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilder;

public class WatershedEndpointInput extends EndpointInput {

    static final String WATERSHED_ID_PARAMETER = "watershed_id";
    private final String watershedId;

    /**
     * Endpoint Input for Watershed to handle input parameters.
     *
     * @param watershedId - ID of watershed
     */
    public WatershedEndpointInput(String watershedId) {
        this.watershedId = Objects.requireNonNull(watershedId, "Id of watershed is required");
    }

    String getWatershedId() {
        return watershedId;
    }

    @Override
    protected HttpRequestBuilder addInputParameters(HttpRequestBuilder httpRequestBuilder) {
        return httpRequestBuilder.addQueryParameter(WATERSHED_ID_PARAMETER, watershedId)
            .addQueryHeader(ACCEPT_QUERY_HEADER, ACCEPT_HEADER_V1);
    }
}
