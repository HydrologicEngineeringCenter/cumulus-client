package hec.army.usace.hec.cwbi.auth.http.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;
import org.junit.jupiter.api.Test;

public class TestRefreshTokenRequestBuilder {

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
}
