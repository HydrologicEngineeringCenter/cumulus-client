package hec.army.usace.hec.cwbi.auth.http.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.net.ssl.KeyManager;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;
import org.junit.jupiter.api.Test;

class TestDirectGrantX509TokenRequestBuilder {
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

    private KeyManager getTestKeyManager() {
        return new KeyManager(){

        };
    }

}
