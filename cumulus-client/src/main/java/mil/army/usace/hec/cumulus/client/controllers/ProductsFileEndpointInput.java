package mil.army.usace.hec.cumulus.client.controllers;

import java.time.Instant;

public class ProductsFileEndpointInput extends FileEndpointInput {

    static final String PRODUCT_ID_PARAMETER = "product_id";

    /**
     * Endpoint Input for Product Files to handle input parameters.
     *
     * @param productId - id of product for which files are being retrieved
     * @param after - beginning of date range
     * @param before - ending of date range
     */
    public ProductsFileEndpointInput(String productId, Instant after, Instant before) {
        super(productId, after, before);
    }

    @Override
    String getIdParameter() {
        return PRODUCT_ID_PARAMETER;
    }
}
