package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusConstants.ACCEPT_HEADER_V1;
import static mil.army.usace.hec.cumulus.client.controllers.CumulusConstants.ACCEPT_QUERY_HEADER;
import static mil.army.usace.hec.cumulus.client.controllers.WatershedEndpointInput.WATERSHED_ID_PARAMETER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TestWatershedEndpointInput {

    @Test
    void testQueryRequest() {
        MockHttpRequestBuilder mockHttpRequestBuilder = new MockHttpRequestBuilder();
        String watershedId = "c785f4de-ab17-444b-b6e6-6f1ad16676e8";
        WatershedEndpointInput input = new WatershedEndpointInput(watershedId);
        input.addInputParameters(mockHttpRequestBuilder);

        assertEquals(watershedId, mockHttpRequestBuilder.getQueryParameter(WATERSHED_ID_PARAMETER));
        assertEquals(ACCEPT_HEADER_V1, mockHttpRequestBuilder.getQueryHeader(ACCEPT_QUERY_HEADER));
        assertThrows(NullPointerException.class, () -> new WatershedEndpointInput(null));
    }

}
