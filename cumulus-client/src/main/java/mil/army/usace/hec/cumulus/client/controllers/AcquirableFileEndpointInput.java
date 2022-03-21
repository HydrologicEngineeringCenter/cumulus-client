package mil.army.usace.hec.cumulus.client.controllers;

import java.time.Instant;

public final class AcquirableFileEndpointInput extends FileEndpointInput {

    static final String ACQUIRABLE_ID_PARAMETER = "acquirable_id";

    /**
     * Endpoint Input for Acquirable Files to handle input parameters.
     *
     * @param acquirableId - ID of acquirable file
     * @param after  - beginning of date range
     * @param before - end of date range
     */
    protected AcquirableFileEndpointInput(String acquirableId, Instant after, Instant before) {
        super(acquirableId, after, before);
    }

    @Override
    String getIdParameter() {
        return ACQUIRABLE_ID_PARAMETER;
    }

}
