package hec.army.usace.hec.cumulus.http.client;

import hec.army.usace.hec.cwbi.auth.http.client.JwtTokenValidator;
import java.io.IOException;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;

public class CumulusHttpRequestBuilderImpl extends HttpRequestBuilderImpl {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Authenticated Http Request Builder.
     *
     * @param apiConnectionInfo - connection info
     * @param endpoint          - endpoint for request
     * @param token             - JWT token
     * @throws IOException      - thrown if request failed
     */
    public CumulusHttpRequestBuilderImpl(ApiConnectionInfo apiConnectionInfo, String endpoint, OAuth2Token token)
        throws IOException {

        super(apiConnectionInfo, endpoint);
        addTokenIfValid(token.getAccessToken());
    }

    //package scoped for testing
    void addTokenIfValid(String token) throws IOException {
        JwtTokenValidator.validateToken(token);
        addQueryHeader(AUTHORIZATION_HEADER, "Bearer " + token);
    }

}
