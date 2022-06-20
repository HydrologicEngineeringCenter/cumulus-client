package hec.army.usace.hec.cwbi.auth.http.client;

import java.io.IOException;
import javax.net.ssl.SSLSocketFactory;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;

public final class AccessTokenProviderImpl implements AccessTokenProvider {

    private final String url;
    private final String clientId;
    private final SSLSocketFactory sslSocketFactory;
    private OAuth2Token token;

    /**
     * Provider for OAuth2Tokens.
     *
     * @param url - URL we are fetching token from
     * @param clientId - client name
     * @param sslSocketFactory - ssl socket factory
     */
    public AccessTokenProviderImpl(String url, String clientId, SSLSocketFactory sslSocketFactory) {
        this.url = url;
        this.clientId = clientId;
        this.sslSocketFactory = sslSocketFactory;
    }

    @Override
    public OAuth2Token getToken() throws IOException {
        return getDirectGrantX509Token();
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
        token = new RefreshTokenRequestBuilder()
            .withRefreshToken(token.getRefreshToken())
            .withUrl(url)
            .withClientId(clientId)
            .fetchToken();
        return token;
    }
}
