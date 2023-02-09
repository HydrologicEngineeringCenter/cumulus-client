package mil.army.usace.hec.cumulus.client.controllers;

import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import mil.army.usace.hec.cumulus.client.model.Watershed;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;
import mil.army.usace.hec.cwms.http.client.request.HttpRequestExecutor;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusConstants.ACCEPT_HEADER_V1;

public final class CumulusWatershedController {

    private static final String WATERSHEDS_ENDPOINT = "watersheds";
    private final ExecutorService executorService;

    public CumulusWatershedController(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Retrieve Watershed.
     *
     * @param apiConnectionInfo    - connection info
     * @param watershedEndpointInput - watershed endpoint input containing watershed id
     * @return Watershed
     */
    public CompletableFuture<Watershed> retrieveWatershed(ApiConnectionInfo apiConnectionInfo, WatershedEndpointInput watershedEndpointInput) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequestExecutor executor =
                    new HttpRequestBuilderImpl(apiConnectionInfo, WATERSHEDS_ENDPOINT + "/" + watershedEndpointInput.getWatershedId())
                            .enableHttp2()
                            .get()
                        .withMediaType(ACCEPT_HEADER_V1);
                try (HttpRequestResponse response = executor.execute()) {
                    return CumulusObjectMapper.mapJsonToObject(response.getBody(), Watershed.class);
                }
            } catch (IOException ex) {
                throw new CompletionException(ex);
            }
        }, executorService);
    }

    /**
     * Retrieve All Watersheds.
     *
     * @param apiConnectionInfo    - connection info
     * @return List of Watersheds
     * @throws CompletionException - IOException wrapper thrown if retrieve failed
     */
    public CompletableFuture<List<Watershed>> retrieveAllWatersheds(ApiConnectionInfo apiConnectionInfo) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpRequestExecutor executor = new HttpRequestBuilderImpl(apiConnectionInfo, WATERSHEDS_ENDPOINT)
                    .get()
                    .withMediaType(ACCEPT_HEADER_V1);
                try (HttpRequestResponse response = executor.execute()) {
                    return CumulusObjectMapper.mapJsonToListOfObjects(response.getBody(), Watershed.class);
                }
            } catch (IOException ex) {
                throw new CompletionException(ex);
            }
        }, executorService);
    }


}
