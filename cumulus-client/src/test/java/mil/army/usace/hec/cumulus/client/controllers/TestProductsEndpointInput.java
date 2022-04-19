package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusEndpointConstants.ACCEPT_HEADER_V1;
import static mil.army.usace.hec.cumulus.client.controllers.CumulusEndpointConstants.ACCEPT_QUERY_HEADER;
import static mil.army.usace.hec.cumulus.client.controllers.ProductsEndpointInput.PRODUCT_ID_PARAMETER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class TestProductsEndpointInput {

    @Test
    void testQueryRequest() {
        MockHttpRequestBuilder mockHttpRequestBuilder = new MockHttpRequestBuilder();
        String productId = "dbe7626e-e793-4637-b3ca-8b80ab1a570a";
        ProductsEndpointInput input = new ProductsEndpointInput(productId);
        input.addInputParameters(mockHttpRequestBuilder);

        assertEquals(productId, mockHttpRequestBuilder.getQueryParameter(PRODUCT_ID_PARAMETER));
        assertEquals(ACCEPT_HEADER_V1, mockHttpRequestBuilder.getQueryHeader(ACCEPT_QUERY_HEADER));
        assertThrows(NullPointerException.class, () -> new ProductsEndpointInput(null));
    }
}
