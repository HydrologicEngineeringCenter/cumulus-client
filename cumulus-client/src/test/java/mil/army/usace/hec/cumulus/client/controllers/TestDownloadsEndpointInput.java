package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusConstants.ACCEPT_HEADER_V1;
import static mil.army.usace.hec.cumulus.client.controllers.CumulusConstants.ACCEPT_QUERY_HEADER;
import static mil.army.usace.hec.cumulus.client.controllers.DownloadsEndpointInput.DOWNLOAD_ID_PARAMETER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class TestDownloadsEndpointInput {

    @Test
    void testQueryRequest() {
        MockHttpRequestBuilder mockHttpRequestBuilder = new MockHttpRequestBuilder();
        String downloadId = "497f6eca-6276-4993-bfeb-53cbbbba6f08";
        DownloadsEndpointInput input = new DownloadsEndpointInput(downloadId);
        input.addInputParameters(mockHttpRequestBuilder);

        assertEquals(downloadId, mockHttpRequestBuilder.getQueryParameter(DOWNLOAD_ID_PARAMETER));
        assertEquals(ACCEPT_HEADER_V1, mockHttpRequestBuilder.getQueryHeader(ACCEPT_QUERY_HEADER));
        assertThrows(NullPointerException.class, () -> new DownloadsEndpointInput(null));
    }

}
