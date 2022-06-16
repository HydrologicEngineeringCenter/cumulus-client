package hec.army.usace.hec.cwbi.auth.http.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import javax.net.ssl.KeyManager;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;

class TestRefreshTokenRequestBuilder extends TestAuthenticatedHttpRequestBuilder{

    @Test
    void testDirectGrantX509TokenRequestBuilder() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        try {
            String body = readJsonFile("oauth2token.json");
            mockWebServer.enqueue(new MockResponse().setBody(body).setResponseCode(200));
            mockWebServer.start();
            String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
            OAuth2Token token = new RefreshTokenRequestBuilder()
                .withRefreshToken("abcdefghijklmnopqrstuvwxyz0123456789")
                .withUrl(baseUrl)
                .withClientId("cumulus")
                .fetchToken();
            assertNotNull(token);
            assertEquals("MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3", token.getAccessToken());
            assertEquals("Bearer", token.getTokenType());
            assertEquals(3600, token.getExpiresIn());
            assertEquals("create", token.getScope());
            assertEquals("IwOGYzYTlmM2YxOTQ5MGE3YmNmMDFkNTVk", token.getRefreshToken());
        } finally {
            mockWebServer.shutdown();
        }
    }

    @Test
    void testRetrieveTokenMissingParams() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new RefreshTokenRequestBuilder()
                .withRefreshToken("testToken")
                .withUrl(null);
        });
        assertEquals("Missing required URL", ex.getMessage());

        NullPointerException ex2 = assertThrows(NullPointerException.class, () -> {
            OAuth2Token token = new RefreshTokenRequestBuilder()
                .withRefreshToken("testToken")
                .withUrl("https://test.com")
                .withClientId(null)
                .fetchToken();
            assertNull(token);
        });
        assertEquals("Missing required Client ID", ex2.getMessage());


        NullPointerException ex3 = assertThrows(NullPointerException.class, () -> {
            OAuth2Token token = new RefreshTokenRequestBuilder()
                .withRefreshToken(null)
                .withUrl("https://test.com")
                .withClientId("cumulus")
                .fetchToken();
            assertNull(token);
        });
        assertEquals("Missing required refresh token", ex3.getMessage());
    }

    private KeyManager getTestKeyManager() {
        return new KeyManager(){

        };
    }
}
