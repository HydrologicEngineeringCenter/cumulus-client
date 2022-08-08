package hec.army.usace.hec.cwbi.auth.http.client;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import javax.net.ssl.SSLSocketFactory;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2TokenProvider;

public final class CwbiAuthTokenProvider implements OAuth2TokenProvider {

    private static final String REFRESH_EXPIRED_BUFFER_PROPERTY_KEY = "cwbi.auth.token.refresh.buffer.millis";
    private static final long DEFAULT_REFRESH_EXPIRED_BUFFER_SECONDS = 1;
    private OAuth2Token oauth2Token;
    private final String url;
    private final String clientId;
    private final SSLSocketFactory sslSocketFactory;

    /**
     * Provider for OAuth2Tokens.
     *
     * @param url - URL we are fetching token from
     * @param clientId - client name
     * @param sslSocketFactory - ssl socket factory
     */
    public CwbiAuthTokenProvider(String url, String clientId, SSLSocketFactory sslSocketFactory) {
        this.url = url;
        this.clientId = clientId;
        this.sslSocketFactory = sslSocketFactory;
    }

    @Override
    public OAuth2Token getToken() throws IOException {
        if (oauth2Token == null) {
            oauth2Token = getDirectGrantX509Token();
        }
        return oauth2Token;
    }

    private OAuth2Token getDirectGrantX509Token() throws IOException {
        return new DirectGrantX509TokenRequestBuilder()
            .withSSlSocketFactory(sslSocketFactory)
            .withUrl(url)
            .withClientId(clientId)
            .fetchToken();
    }

    @Override
    public OAuth2Token refreshToken() throws IOException {
        OAuth2Token token = new RefreshTokenRequestBuilder()
            .withRefreshToken(oauth2Token.getRefreshToken())
            .withUrl(url)
            .withClientId(clientId)
            .fetchToken();
        DecodedJWT jwt = JWT.decode(token.getAccessToken());
        long bufferMillis = Instant.ofEpochSecond(DEFAULT_REFRESH_EXPIRED_BUFFER_SECONDS)
            .toEpochMilli(); //default 1 second buffer
        String bufferStr = System.getProperty(REFRESH_EXPIRED_BUFFER_PROPERTY_KEY);
        if (bufferStr != null) {
            bufferMillis = Long.parseLong(bufferStr);
        }
        //take current time and subtract buffer. If token is expired at that time, then its no longer valid
        Instant noLongerValid = Instant.now().minusMillis(bufferMillis);
        if (jwt.getExpiresAt().before(Date.from(noLongerValid))) {
            token = getDirectGrantX509Token(); //if expired re-do direct grant x509
        }
        oauth2Token = token;
        return token;
    }

    //package scoped for testing
    String getUrl() {
        return url;
    }

    //package scoped for testing
    String getClientId() {
        return clientId;
    }
}
