package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusEndpointConstants.ACCEPT_HEADER_V1;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import mil.army.usace.hec.cumulus.client.model.Product;
import mil.army.usace.hec.cumulus.client.model.ProductAvailability;
import mil.army.usace.hec.cumulus.client.model.ProductFile;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;
import mil.army.usace.hec.cwms.http.client.request.HttpRequestExecutor;

public final class CumulusProductsController {

    private static final String PRODUCTS_ENDPOINT = "products";
    private static final String FILES_ENDPOINT = "files";
    private static final String AVAILABILITY_ENDPOINT = "availability";
    private final ExecutorService executorService;

    public CumulusProductsController(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Retrieve All Products.
     *
     * @param apiConnectionInfo    - connection info
     * @return List of Products
     */
    public CompletableFuture<List<Product>> retrieveAllProducts(ApiConnectionInfo apiConnectionInfo) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequestExecutor executor = new HttpRequestBuilderImpl(apiConnectionInfo, PRODUCTS_ENDPOINT)
                    .enableHttp2()
                    .get()
                    .withMediaType(ACCEPT_HEADER_V1);
                try (HttpRequestResponse response = executor.execute()) {
                    return CumulusObjectMapper.mapJsonToListOfObjects(response.getBody(), Product.class);
                }
            } catch (IOException ex) {
                throw new CompletionException(ex);
            }
        }, executorService);
    }

    /**
     * Retrieve Product.
     *
     * @param apiConnectionInfo    - connection info
     * @param productsEndpointInput - product endpoint input containing product id
     * @return Product
     */
    public CompletableFuture<Product> retrieveProduct(ApiConnectionInfo apiConnectionInfo, ProductsEndpointInput productsEndpointInput) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequestExecutor executor =
                    new HttpRequestBuilderImpl(apiConnectionInfo, PRODUCTS_ENDPOINT + "/" + productsEndpointInput.getProductId())
                        .enableHttp2()
                        .get()
                        .withMediaType(ACCEPT_HEADER_V1);
                try (HttpRequestResponse response = executor.execute()) {
                    return CumulusObjectMapper.mapJsonToObject(response.getBody(), Product.class);
                }
            } catch (IOException ex) {
                throw new CompletionException(ex);
            }
        }, executorService);
    }

    /**
     * Retrieve Product Files.
     *
     * @param apiConnectionInfo    - connection info
     * @param productsFileEndpointInput - product files endpoint input containing product id and date range
     * @return List of ProductFiles
     */
    public CompletableFuture<List<ProductFile>> retrieveProductFiles(ApiConnectionInfo apiConnectionInfo,
                                                                     ProductsFileEndpointInput productsFileEndpointInput) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequestExecutor executor = new HttpRequestBuilderImpl(apiConnectionInfo, PRODUCTS_ENDPOINT + "/"
                    + productsFileEndpointInput.getFileId() + "/" + FILES_ENDPOINT)
                    .enableHttp2()
                    .get()
                    .withMediaType(ACCEPT_HEADER_V1);
                try (HttpRequestResponse response = executor.execute()) {
                    return CumulusObjectMapper.mapJsonToListOfObjects(response.getBody(), ProductFile.class);
                }
            } catch (IOException ex) {
                throw new CompletionException(ex);
            }
        }, executorService);
    }

    /**
     * Retrieve Product Availability.
     *
     * @param apiConnectionInfo    - connection info
     * @param productsEndpointInput - product endpoint input containing product id
     * @return ProductAvailability
     */
    public CompletableFuture<ProductAvailability> retrieveProductAvailability(ApiConnectionInfo apiConnectionInfo,
                                                                              ProductsEndpointInput productsEndpointInput) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequestExecutor executor = new HttpRequestBuilderImpl(apiConnectionInfo, PRODUCTS_ENDPOINT + "/"
                    + productsEndpointInput.getProductId() + "/"
                    + AVAILABILITY_ENDPOINT)
                    .enableHttp2()
                    .get()
                    .withMediaType(ACCEPT_HEADER_V1);
                try (HttpRequestResponse response = executor.execute()) {
                    return CumulusObjectMapper.mapJsonToObject(response.getBody(), ProductAvailability.class);
                }
            } catch (IOException ex) {
                throw new CompletionException(ex);
            }
        }, executorService);
    }



}
