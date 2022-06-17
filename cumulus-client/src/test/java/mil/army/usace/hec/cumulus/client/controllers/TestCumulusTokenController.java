package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusTokenController.CLIENT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hec.army.usace.hec.cwbi.auth.http.client.DirectGrantX509TokenRequestBuilder;
import hec.army.usace.hec.cwbi.auth.http.client.RefreshTokenRequestBuilder;
import java.io.IOException;
import java.util.concurrent.CompletionException;
import javax.net.ssl.KeyManager;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;
import org.junit.jupiter.api.Test;

class TestCumulusTokenController extends TestController{

    @Test
    void testRetrieveTokenMissingParams() {
        CompletionException ex = assertThrows(CompletionException.class, () -> {
            OAuth2Token token = new CumulusTokenController().retrieveTokenWithDirectGrantX509(null).join();
            assertNull(token);
        });
        assertEquals("Missing required KeyManager", ex.getCause().getMessage());

        NullPointerException ex2 = assertThrows(NullPointerException.class, () -> {
            OAuth2Token token = new CumulusTokenController().retrieveOAuth2TokenWithRefreshToken(null);
            assertNull(token);
        });
        assertEquals("Missing required refresh token", ex2.getMessage());
    }

    @Test
    void testRetrieveTokenDirectGrantX509() throws IOException {
        String resource = "cumulus/json/oauth2token.json";
        launchMockServerWithResource(resource);

        OAuth2Token token = mockRetrieveOAuth2TokenWithDirectGrantX509(getTestKeyManager());
        assertNotNull(token);
        assertEquals("MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3", token.getAccessToken());
        assertEquals("Bearer", token.getTokenType());
        assertEquals(3600, token.getExpiresIn());
        assertEquals("create", token.getScope());
        assertEquals("IwOGYzYTlmM2YxOTQ5MGE3YmNmMDFkNTVk", token.getRefreshToken());

    }

    @Test
    void testRetrieveTokenWithRefreshToken() throws IOException {
        String resource = "cumulus/json/oauth2token.json";
        String refreshToken = "IwOGYzYTlmM2YxOTQ5MGE3YmNmMDFkNTVk";
        launchMockServerWithResource(resource);
        OAuth2Token token = mockRetrieveOAuth2TokenWithRefreshToken(refreshToken);
        assertNotNull(token);
        assertEquals("MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3", token.getAccessToken());
        assertEquals("Bearer", token.getTokenType());
        assertEquals(3600, token.getExpiresIn());
        assertEquals("create", token.getScope());
        assertEquals("IwOGYzYTlmM2YxOTQ5MGE3YmNmMDFkNTVk", token.getRefreshToken());

    }

   private KeyManager getTestKeyManager() {
        return new KeyManager() {

        };
   }

    private OAuth2Token mockRetrieveOAuth2TokenWithDirectGrantX509(KeyManager keyManager) throws IOException {
        ApiConnectionInfo connectionInfo = buildConnectionInfo();
        return new DirectGrantX509TokenRequestBuilder()
            .withKeyManager(keyManager)
            .withUrl(connectionInfo.getApiRoot())
            .withClientId(CLIENT_ID)
            .fetchToken();
    }

    OAuth2Token mockRetrieveOAuth2TokenWithRefreshToken(String refreshToken) throws IOException {
        ApiConnectionInfo connectionInfo = buildConnectionInfo();
        return new RefreshTokenRequestBuilder()
            .withRefreshToken(refreshToken)
            .withUrl(connectionInfo.getApiRoot())
            .withClientId(CLIENT_ID)
            .fetchToken();
    }

}
