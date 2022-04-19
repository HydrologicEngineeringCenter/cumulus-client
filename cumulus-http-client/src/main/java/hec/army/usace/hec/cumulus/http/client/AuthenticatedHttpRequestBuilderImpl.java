package hec.army.usace.hec.cumulus.http.client;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import java.io.IOException;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;

public class AuthenticatedHttpRequestBuilderImpl extends HttpRequestBuilderImpl {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Authenticated Http Request Builder.
     *
     * @param apiConnectionInfo - connection info
     * @param endpoint          - endpoint for request
     * @param token             - JWT token
     * @throws IOException      - thrown if request failed
     */
    public AuthenticatedHttpRequestBuilderImpl(ApiConnectionInfo apiConnectionInfo, String endpoint, String token)
        throws IOException {

        super(apiConnectionInfo, endpoint);
        addTokenIfValid(token);
    }

    //package scoped for testing
    void addTokenIfValid(String token) throws IOException {
        try {
            JWT.decode(token); // needs more verification here
            addQueryHeader(AUTHORIZATION_HEADER, token);
        } catch (JWTDecodeException ex) {
            throw new IOException("Invalid JSON Web Token");
        }
    }


}
