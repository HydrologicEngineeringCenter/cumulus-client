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

class TestDirectGrantX509TokenRequestBuilder extends TestAuthenticatedHttpRequestBuilder{
    @Test
    void testRetrieveTokenMissingParams() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new DirectGrantX509TokenRequestBuilder()
                .withKeyManager(getTestKeyManager())
                .withUrl(null);
        });
        assertEquals("Missing required URL", ex.getMessage());

        NullPointerException ex2 = assertThrows(NullPointerException.class, () -> {
            OAuth2Token token = new DirectGrantX509TokenRequestBuilder()
                .withKeyManager(getTestKeyManager())
                .withUrl("https://test.com")
                .withClientId(null)
                .fetchToken();
            assertNull(token);
        });
        assertEquals("Missing required Client ID", ex2.getMessage());


        NullPointerException ex3 = assertThrows(NullPointerException.class, () -> {
            OAuth2Token token = new DirectGrantX509TokenRequestBuilder()
                .withKeyManager(null)
                .withUrl("https://test.com")
                .withClientId("cumulus")
                .fetchToken();
            assertNull(token);
        });
        assertEquals("Missing required KeyManager", ex3.getMessage());

        NullPointerException ex4 = assertThrows(NullPointerException.class, () -> {
            OAuth2Token token = new DirectGrantX509TokenRequestBuilder()
                .withKeyManagers(null)
                .withUrl("https://test.com")
                .withClientId("cumulus")
                .fetchToken();
            assertNull(token);
        });
        assertEquals("Missing required KeyManagers", ex4.getMessage());

    }

    @Test
    void testDirectGrantX509TokenRequestBuilder() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        try {
            String body = readJsonFile("oauth2token.json");
            mockWebServer.enqueue(new MockResponse().setBody(body).setResponseCode(200));
            mockWebServer.start();
            String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
            OAuth2Token token = new DirectGrantX509TokenRequestBuilder()
                .withKeyManager(getTestKeyManager())
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

    private KeyManager getTestKeyManager() {
        return new KeyManager(){

        };
    }

}
