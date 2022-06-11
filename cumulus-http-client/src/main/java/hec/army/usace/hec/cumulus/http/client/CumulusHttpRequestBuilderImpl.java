package hec.army.usace.hec.cumulus.http.client;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Date;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;

public class CumulusHttpRequestBuilderImpl extends HttpRequestBuilderImpl {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String EXIPIRATION_BUFFER_KEY = "cumulus.http.client.token.expiration.buffer.millis";
    private static final int DEFAULT_EXPIRATION_BUFFER_MILLIS = 1000;

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
        try {
            int bufferMillis = getBuffer();
            DecodedJWT decodedToken = JWT.decode(token);
            Date expiration = decodedToken.getExpiresAt();
            Date now = Date.from(ZonedDateTime.now().toInstant());
            long adjustedTime = now.getTime() + bufferMillis;
            Date adjustedDate = new Date(adjustedTime);
            if (expiration != null && expiration.before(adjustedDate)) {
                throw new IOException("Token is expired");
            }
            addQueryHeader(AUTHORIZATION_HEADER, "Bearer " + token);
        } catch (JWTDecodeException ex) {
            throw new IOException(token + " is not a JSON Web Token");
        }
    }

    private int getBuffer() {
        String bufferMillisStr = System.getProperty(EXIPIRATION_BUFFER_KEY);
        if (bufferMillisStr == null) {
            bufferMillisStr = Integer.toString(DEFAULT_EXPIRATION_BUFFER_MILLIS);
        }
        return Integer.parseInt(bufferMillisStr);
    }

}
