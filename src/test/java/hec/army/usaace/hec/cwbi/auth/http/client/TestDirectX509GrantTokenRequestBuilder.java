package hec.army.usaace.hec.cwbi.auth.http.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hec.army.usace.hec.cwbi.auth.http.client.DirectX509GrantTokenRequestBuilder;
import mil.army.usace.hec.cwms.http.client.model.OAuth2Token;
import org.junit.jupiter.api.Test;

final class TestDirectX509GrantTokenRequestBuilder {

    @Test
    void testRetrieveTokenMissingParams() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new DirectX509GrantTokenRequestBuilder()
                .withUrl(null);
        });
        assertEquals("Missing required URL", ex.getMessage());

        NullPointerException ex2 = assertThrows(NullPointerException.class, () -> {
            OAuth2Token token = new DirectX509GrantTokenRequestBuilder()
            .withUrl("https://test.com")
                .withClientId(null)
                    .withKeyManager(null)
                            .fetchToken();
            assertNull(token);
        });
        assertEquals("Missing required Client ID", ex2.getMessage());


        NullPointerException ex3 = assertThrows(NullPointerException.class, () -> {
            OAuth2Token token = new DirectX509GrantTokenRequestBuilder()
                .withUrl("https://test.com")
                .withClientId("cumulus")
                .withKeyManager(null)
                .fetchToken();
            assertNull(token);
        });
        assertEquals("Missing required KeyManager", ex3.getMessage());

        NullPointerException ex4 = assertThrows(NullPointerException.class, () -> {
            OAuth2Token token = new DirectX509GrantTokenRequestBuilder()
                .withUrl("https://test.com")
                .withClientId("cumulus")
                .withKeyManagers(null)
                .fetchToken();
            assertNull(token);
        });
        assertEquals("Missing required KeyManagers", ex4.getMessage());
    }
}
