package hec.army.usace.hec.cwbi.auth.http.client;

import java.io.IOException;
import javax.net.ssl.SSLSocketFactory;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;

class AccessTokenProviderImpl implements AccessTokenProvider {

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
    AccessTokenProviderImpl(String url, String clientId, SSLSocketFactory sslSocketFactory) {
        this.url = url;
        this.clientId = clientId;
        this.sslSocketFactory = sslSocketFactory;
    }

    @Override
    public OAuth2Token getToken() throws IOException {
        token = new DirectGrantX509TokenRequestBuilder()
            .withSSlSocketFactory(sslSocketFactory)
            .withUrl(url)
            .withClientId(clientId)
            .fetchToken();
        return token;
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
