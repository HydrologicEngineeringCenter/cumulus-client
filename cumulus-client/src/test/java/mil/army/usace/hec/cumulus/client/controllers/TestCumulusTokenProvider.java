package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusAuthUtil.CLIENT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;
import org.junit.jupiter.api.Test;

class TestCumulusTokenProvider extends TestController{

    @Test
    void testGetToken() throws IOException {
        String resource = "cumulus/json/oauth2token.json";
        launchMockServerWithResource(resource);
        String url = buildConnectionInfo().getApiRoot();
        CumulusTokenProvider tokenProvider = new CumulusTokenProvider(url, CLIENT_ID, getTestSslSocketFactory());
        OAuth2Token token = tokenProvider.getToken();
        assertEquals("MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3", token.getAccessToken());
        assertEquals("Bearer", token.getTokenType());
        assertEquals(3600, token.getExpiresIn());
        assertEquals("IwOGYzYTlmM2YxOTQ5MGE3YmNmMDFkNTVk", token.getRefreshToken());
        assertEquals("create", token.getScope());
    }

    @Test
    void testRefreshToken() throws IOException {
        String resource = "cumulus/json/oauth2token.json";
        launchMockServerWithResource(resource);
        String url = buildConnectionInfo().getApiRoot();
        MockCumulusTokenProvider tokenProvider = new MockCumulusTokenProvider(url, CLIENT_ID, getTestSslSocketFactory());
        OAuth2Token token = new OAuth2Token();
        token.setAccessToken("abc123");
        token.setTokenType("Bearer");
        token.setExpiresIn(3600);
        token.setRefreshToken("123abc");
        tokenProvider.setOAuth2Token(token);

        OAuth2Token refreshedToken = tokenProvider.refreshToken();
        assertEquals("MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3", refreshedToken.getAccessToken());
        assertEquals("Bearer", refreshedToken.getTokenType());
        assertEquals(3600, refreshedToken.getExpiresIn());
        assertEquals("IwOGYzYTlmM2YxOTQ5MGE3YmNmMDFkNTVk", refreshedToken.getRefreshToken());
        assertEquals("create", refreshedToken.getScope());
    }

    @Test
    void testConstructor() {
        SSLSocketFactory sslSocketFactory = getTestSslSocketFactory();
        MockCumulusTokenProvider tokenProvider = new MockCumulusTokenProvider("test.com", "clientId", sslSocketFactory);
        assertEquals("test.com", tokenProvider.getUrl());
        assertEquals("clientId", tokenProvider.getClientId());
        assertEquals(sslSocketFactory, tokenProvider.getSslSocketFactory());
    }

    private SSLSocketFactory getTestSslSocketFactory() {
        return new SSLSocketFactory() {
            @Override
            public String[] getDefaultCipherSuites() {
                return new String[0];
            }

            @Override
            public String[] getSupportedCipherSuites() {
                return new String[0];
            }

            @Override
            public Socket createSocket(Socket socket, String s, int i, boolean b) {
                return null;
            }

            @Override
            public Socket createSocket(String s, int i) {
                return null;
            }

            @Override
            public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) {
                return null;
            }

            @Override
            public Socket createSocket(InetAddress inetAddress, int i) {
                return null;
            }

            @Override
            public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) {
                return null;
            }
        };
    }
}
