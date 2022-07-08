package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusConstants.ACCEPT_HEADER_V1;
import static mil.army.usace.hec.cumulus.client.controllers.CumulusConstants.ACCEPT_QUERY_HEADER;

import java.time.Instant;
import java.util.Objects;
import mil.army.usace.hec.cwms.http.client.EndpointInput;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilder;

public abstract class FileEndpointInput extends EndpointInput {

    static final String AFTER_DATE_PARAMETER = "after";
    static final String BEFORE_DATE_PARAMETER = "before";

    private final String fileId;
    private final Instant after;
    private final Instant before;

    protected FileEndpointInput(String fileId, Instant after, Instant before) {
        this.fileId = Objects.requireNonNull(fileId, "Cannot access acquirable file without acquirable id");
        this.after = Objects.requireNonNull(after, "Cannot access acquirable file without specified beginning of date range");
        this.before = Objects.requireNonNull(before, "Cannot access acquirable file without specified end of date range");
    }

    String getFileId() {
        return fileId;
    }

    abstract String getIdParameter();

    @Override
    protected HttpRequestBuilder addInputParameters(HttpRequestBuilder httpRequestBuilder) {
        return httpRequestBuilder.addQueryParameter(getIdParameter(), fileId)
            .addQueryParameter(AFTER_DATE_PARAMETER, after.toString())
            .addQueryParameter(BEFORE_DATE_PARAMETER, before.toString())
            .addQueryHeader(ACCEPT_QUERY_HEADER, ACCEPT_HEADER_V1);
    }
}
