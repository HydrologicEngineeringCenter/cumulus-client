package mil.army.usace.hec.cumulus.client.controllers;

import hec.army.usace.hec.cwbi.auth.http.client.JwtValidator;
import java.io.IOException;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2TokenValidator;

final class CumulusHttpRequestBuilderImpl extends HttpRequestBuilderImpl {
    /**
     * Authenticated Http Request Builder.
     *
     * @param apiConnectionInfo - connection info
     * @param endpoint          - endpoint for request
     * @throws IOException      - thrown if request failed
     */
    CumulusHttpRequestBuilderImpl(ApiConnectionInfo apiConnectionInfo, String endpoint) throws IOException {

        super(apiConnectionInfo, endpoint);
    }

    @Override
    protected OAuth2TokenValidator createOAuth2TokenValidator() {
        return new JwtValidator();
    }
}
