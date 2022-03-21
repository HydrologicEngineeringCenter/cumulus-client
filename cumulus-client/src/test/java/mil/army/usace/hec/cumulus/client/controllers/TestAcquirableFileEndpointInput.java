package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.AcquirableFileEndpointInput.ACQUIRABLE_ID_PARAMETER;
import static mil.army.usace.hec.cumulus.client.controllers.AcquirableFileEndpointInput.AFTER_DATE_PARAMETER;
import static mil.army.usace.hec.cumulus.client.controllers.AcquirableFileEndpointInput.BEFORE_DATE_PARAMETER;
import static mil.army.usace.hec.cumulus.client.controllers.CumulusEndpointConstants.ACCEPT_HEADER_V1;
import static mil.army.usace.hec.cumulus.client.controllers.CumulusEndpointConstants.ACCEPT_QUERY_HEADER;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

class TestAcquirableFileEndpointInput {

    @Test
    void testQueryRequest() {
        MockHttpRequestBuilder mockHttpRequestBuilder = new MockHttpRequestBuilder();
        String acquirableId = "dbe7626e-e793-4637-b3ca-8b80ab1a570a";
        Instant after = ZonedDateTime.of(2022,3, 1, 0,0,0,0, ZoneId.of("UTC")).toInstant();
        Instant before = ZonedDateTime.of(2022,3, 5, 0,0,0,0, ZoneId.of("UTC")).toInstant();
        AcquirableFileEndpointInput input = new AcquirableFileEndpointInput(acquirableId, after, before);
        input.addInputParameters(mockHttpRequestBuilder);

        assertEquals(acquirableId, mockHttpRequestBuilder.getQueryParameter(ACQUIRABLE_ID_PARAMETER));
        assertEquals(after.toString(), mockHttpRequestBuilder.getQueryParameter(AFTER_DATE_PARAMETER));
        assertEquals(before.toString(), mockHttpRequestBuilder.getQueryParameter(BEFORE_DATE_PARAMETER));
        assertEquals(ACCEPT_HEADER_V1, mockHttpRequestBuilder.getQueryHeader(ACCEPT_QUERY_HEADER));
    }
}
