package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusConstants.ACCEPT_HEADER_V1;
import static mil.army.usace.hec.cumulus.client.controllers.CumulusConstants.ACCEPT_QUERY_HEADER;

import java.util.Objects;
import mil.army.usace.hec.cwms.http.client.EndpointInput;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilder;

public class DownloadsEndpointInput extends EndpointInput {

    static final String DOWNLOAD_ID_PARAMETER = "download_id";

    private final String downloadId;

    public DownloadsEndpointInput(String downloadId) {
        this.downloadId = Objects.requireNonNull(downloadId, "ID for download is required");
    }

    String getDownloadId() {
        return downloadId;
    }

    @Override
    protected HttpRequestBuilder addInputParameters(HttpRequestBuilder httpRequestBuilder) {
        return httpRequestBuilder.addQueryParameter(DOWNLOAD_ID_PARAMETER, downloadId)
            .addQueryHeader(ACCEPT_QUERY_HEADER, ACCEPT_HEADER_V1);
    }
}
