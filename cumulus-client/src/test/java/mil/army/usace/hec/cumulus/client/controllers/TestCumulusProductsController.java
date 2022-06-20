package mil.army.usace.hec.cumulus.client.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import mil.army.usace.hec.cumulus.client.model.Product;
import mil.army.usace.hec.cumulus.client.model.ProductAvailability;
import mil.army.usace.hec.cumulus.client.model.ProductFile;
import org.junit.jupiter.api.Test;

class TestCumulusProductsController extends TestController{


    @Test
    void testRetrieveAllProducts() throws IOException {
        String resource = "cumulus/json/products.json";
        launchMockServerWithResource(resource);

        List<Product> products = new CumulusProductsController(executorService).retrieveAllProducts(buildConnectionInfo()).join();
        assertEquals(2, products.size());
        Product product1 = products.get(0);
        Product product2 = products.get(1);
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", product1.getId());
        assertEquals("testSlug", product1.getSlug());
        assertEquals("product1", product1.getName());
        assertEquals(1, product1.getTags().length);
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", product1.getTags()[0]);
        assertEquals(1, product1.getTemporalResolution());
        assertEquals(2, product1.getTemporalDuration());
        assertEquals("MBRFC-FORECAST", product1.getDssFPart());
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa7", product1.getParameterId());
        assertEquals("PARAM", product1.getParameter());
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa8", product1.getUnitId());
        assertEquals("ft", product1.getUnit());
        assertEquals("for testing purposes", product1.getDescription());
        assertEquals("91d87306-8eed-45ac-a41e-16d9429ca14c", product1.getSuiteId());
        assertEquals("Lower Mississippi River Forecast Center 06 hour QPF", product1.getSuite());
        assertEquals("QPF", product1.getLabel());
        assertEquals("2021-08-23T06:01:01Z", product1.getAfter().toString());
        assertEquals("2022-04-24T12:01:01Z", product1.getBefore().toString());
        assertEquals(8500, product1.getProductFileCount());
        assertEquals("null", product1.getLastForecastVersion());

        assertEquals("4fa85f64-5717-4562-b3fc-2c963f66afa6", product2.getId());
        assertEquals("testSlug2", product2.getSlug());
        assertEquals("product2", product2.getName());
        assertEquals(1, product2.getTags().length);
        assertEquals("4fa85f64-5717-4562-b3fc-2c963f66afa6", product2.getTags()[0]);
        assertEquals(2, product2.getTemporalResolution());
        assertEquals(3, product2.getTemporalDuration());
        assertEquals("MBRFC-FORECAST", product2.getDssFPart());
        assertEquals("4fa85f64-5717-4562-b3fc-2c963f66afa7", product2.getParameterId());
        assertEquals("PARAM", product2.getParameter());
        assertEquals("4fa85f64-5717-4562-b3fc-2c963f66afa8", product2.getUnitId());
        assertEquals("ft", product2.getUnit());
        assertEquals("for testing purposes", product2.getDescription());
        assertEquals("74d7191f-7c4b-4549-bf80-5a5de4ba4880", product2.getSuiteId());
        assertEquals("Colorado Basin River Forecast Center (CBRFC)", product2.getSuite());
        assertEquals("MPE", product2.getLabel());
        assertEquals("2021-04-18T21:01:01Z", product2.getAfter().toString());
        assertEquals("2022-04-14T20:01:01Z", product2.getBefore().toString());
        assertEquals(8448, product2.getProductFileCount());
        assertEquals("null", product2.getLastForecastVersion());
    }

    @Test
    void testRetrieveProduct() throws IOException {
        String resource = "cumulus/json/product.json";
        launchMockServerWithResource(resource);

        String productId = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        ProductsEndpointInput input = new ProductsEndpointInput(productId);
        Product product = new CumulusProductsController(executorService).retrieveProduct(buildConnectionInfo(), input).join();
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", product.getId());
        assertEquals("testSlug", product.getSlug());
        assertEquals("product1", product.getName());
        assertEquals(1, product.getTags().length);
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", product.getTags()[0]);
        assertEquals(1, product.getTemporalResolution());
        assertEquals(2, product.getTemporalDuration());
        assertEquals("MBRFC-FORECAST", product.getDssFPart());
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa7", product.getParameterId());
        assertEquals("PARAM", product.getParameter());
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa8", product.getUnitId());
        assertEquals("ft", product.getUnit());
        assertEquals("for testing purposes", product.getDescription());
        assertEquals("91d87306-8eed-45ac-a41e-16d9429ca14c", product.getSuiteId());
        assertEquals("Lower Mississippi River Forecast Center 06 hour QPF", product.getSuite());
        assertEquals("QPF", product.getLabel());
        assertEquals("2021-08-23T06:01:01Z", product.getAfter().toString());
        assertEquals("2022-04-24T12:01:01Z", product.getBefore().toString());
        assertEquals(8500, product.getProductFileCount());
        assertEquals("null", product.getLastForecastVersion());
    }

    @Test
    void testRetrieveProductFiles() throws IOException {
        String resource = "cumulus/json/productFiles.json";
        launchMockServerWithResource(resource);

        String productId = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        Instant after = ZonedDateTime.of(2022,3, 21, 0,0,0,0, ZoneId.of("UTC")).toInstant();
        Instant before = ZonedDateTime.of(2022,3, 22, 0,0,0,0, ZoneId.of("UTC")).toInstant();
        ProductsFileEndpointInput input = new ProductsFileEndpointInput(productId, after, before);
        List<ProductFile> productFiles = new CumulusProductsController(executorService).retrieveProductFiles(buildConnectionInfo(), input).join();
        assertNotNull(productFiles);
        assertFalse(productFiles.isEmpty());
        ProductFile productFile = productFiles.get(0);
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", productFile.getId());
        assertEquals("2022-03-22T20:44:46.758Z", productFile.getDateTime().toString());
        assertEquals("testFile", productFile.getFile());
    }

    @Test
    void testRetrieveProductAvailability() throws IOException {
        String resource = "cumulus/json/productAvailability.json";
        launchMockServerWithResource(resource);

        String productId = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        ProductsEndpointInput input = new ProductsEndpointInput(productId);
        ProductAvailability productAvailability = new CumulusProductsController(executorService).retrieveProductAvailability(buildConnectionInfo(), input).join();
        assertNotNull(productAvailability);
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", productAvailability.getProductId());
        assertEquals(1, productAvailability.getDateCounts().length);
        assertEquals("2022-03-22T21:03:18.341Z", productAvailability.getDateCounts()[0].getDate().toString());
        assertEquals(1, productAvailability.getDateCounts()[0].getCount());

    }

}
