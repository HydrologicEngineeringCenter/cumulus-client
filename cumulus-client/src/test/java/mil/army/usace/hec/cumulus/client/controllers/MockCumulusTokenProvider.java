package mil.army.usace.hec.cumulus.client.controllers;

import hec.army.usace.hec.cwbi.auth.http.client.DirectGrantX509TokenRequestBuilder;
import hec.army.usace.hec.cwbi.auth.http.client.RefreshTokenRequestBuilder;
import java.io.IOException;
import javax.net.ssl.SSLSocketFactory;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2TokenProvider;

public class MockCumulusTokenProvider implements OAuth2TokenProvider {

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
    public MockCumulusTokenProvider(String url, String clientId, SSLSocketFactory sslSocketFactory) {
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
        oauth2Token = token;
        return token;
    }

    void setOAuth2Token(OAuth2Token token) {
        oauth2Token = token;
    }

    //package scoped for testing
    String getUrl() {
        return url;
    }

    //package scoped for testing
    String getClientId() {
        return clientId;
    }

    SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }
}
