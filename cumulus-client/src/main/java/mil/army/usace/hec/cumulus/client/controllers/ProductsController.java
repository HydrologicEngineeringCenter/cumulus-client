package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusEndpointConstants.ACCEPT_HEADER_V1;

import java.io.IOException;
import java.util.List;
import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import mil.army.usace.hec.cumulus.client.model.Product;
import mil.army.usace.hec.cumulus.client.model.ProductAvailability;
import mil.army.usace.hec.cumulus.client.model.ProductFile;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;

public class ProductsController {

    private static final String PRODUCTS_ENDPOINT = "products";
    private static final String FILES_ENDPOINT = "files";
    private static final String AVAILABILITY_ENDPOINT = "availability";

    /**
     * Retrieve All Products.
     *
     * @param apiConnectionInfo    - connection info
     * @return List of Products
     * @throws IOException - thrown if retrieve failed
     */
    public List<Product> retrieveAllProducts(ApiConnectionInfo apiConnectionInfo)
        throws IOException {
        HttpRequestResponse response =
            new HttpRequestBuilderImpl(apiConnectionInfo, PRODUCTS_ENDPOINT)
                .get()
                .withMediaType(ACCEPT_HEADER_V1)
                .execute();
        return CumulusObjectMapper.mapJsonToListOfObjects(response.getBody(), Product.class);
    }

    /**
     * Retrieve Product.
     *
     * @param apiConnectionInfo    - connection info
     * @param productsEndpointInput - product endpoint input containing product id
     * @return Product
     * @throws IOException - thrown if retrieve failed
     */
    public Product retrieveProduct(ApiConnectionInfo apiConnectionInfo, ProductsEndpointInput productsEndpointInput)
        throws IOException {
        HttpRequestResponse response =
            new HttpRequestBuilderImpl(apiConnectionInfo, PRODUCTS_ENDPOINT + "/" + productsEndpointInput.getProductId())
                .get()
                .withMediaType(ACCEPT_HEADER_V1)
                .execute();
        return CumulusObjectMapper.mapJsonToObject(response.getBody(), Product.class);
    }

    /**
     * Retrieve Product Files.
     *
     * @param apiConnectionInfo    - connection info
     * @param productsFileEndpointInput - product files endpoint input containing product id and date range
     * @return List of ProductFiles
     * @throws IOException - thrown if retrieve failed
     */
    public List<ProductFile> retrieveProductFiles(ApiConnectionInfo apiConnectionInfo, ProductsFileEndpointInput productsFileEndpointInput)
        throws IOException {
        HttpRequestResponse response =
            new HttpRequestBuilderImpl(apiConnectionInfo, PRODUCTS_ENDPOINT + "/"
                + productsFileEndpointInput.getFileId() + "/" + FILES_ENDPOINT)
                .get()
                .withMediaType(ACCEPT_HEADER_V1)
                .execute();
        return CumulusObjectMapper.mapJsonToListOfObjects(response.getBody(), ProductFile.class);
    }

    /**
     * Retrieve Product Availability.
     *
     * @param apiConnectionInfo    - connection info
     * @param productsEndpointInput - product endpoint input containing product id
     * @return ProductAvailability
     * @throws IOException - thrown if retrieve failed
     */
    public ProductAvailability retrieveProductAvailability(ApiConnectionInfo apiConnectionInfo, ProductsEndpointInput productsEndpointInput)
        throws IOException {
        HttpRequestResponse response =
            new HttpRequestBuilderImpl(apiConnectionInfo, PRODUCTS_ENDPOINT + "/"
                + productsEndpointInput.getProductId() + "/"
                + AVAILABILITY_ENDPOINT)
                .get()
                .withMediaType(ACCEPT_HEADER_V1)
                .execute();

        return CumulusObjectMapper.mapJsonToObject(response.getBody(), ProductAvailability.class);
    }



}
