package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusEndpointConstants.ACCEPT_HEADER_V1;
import static mil.army.usace.hec.cumulus.client.controllers.CumulusEndpointConstants.ACCEPT_QUERY_HEADER;

import java.util.Objects;
import mil.army.usace.hec.cwms.http.client.EndpointInput;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilder;

public class ProductsEndpointInput extends EndpointInput {

    static final String PRODUCT_ID_PARAMETER = "product_id";

    private final String productId;
    /**
     * Endpoint Input for Products to handle input parameters.
     *
     * @param productId - ID of product (or null if retrieving all products)
     */

    public ProductsEndpointInput(String productId) {
        this.productId = Objects.requireNonNull(productId, "Cannot access acquirable file without acquirable id");
    }

    String getProductId() {
        return productId;
    }

    @Override
    protected HttpRequestBuilder addInputParameters(HttpRequestBuilder httpRequestBuilder) {
        return httpRequestBuilder.addQueryParameter(PRODUCT_ID_PARAMETER, productId)
            .addQueryHeader(ACCEPT_QUERY_HEADER, ACCEPT_HEADER_V1);
    }
}
