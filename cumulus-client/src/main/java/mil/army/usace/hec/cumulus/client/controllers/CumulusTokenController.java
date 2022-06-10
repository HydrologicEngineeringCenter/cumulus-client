package mil.army.usace.hec.cumulus.client.controllers;


import hec.army.usace.hec.cwbi.auth.http.client.DirectX509GrantTokenRequestBuilder;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import javax.net.ssl.KeyManager;
import mil.army.usace.hec.cwms.http.client.model.OAuth2Token;

public class CumulusTokenController {

    private static final String TOKEN_URL = "https://auth.corps.cloud/auth/realms/water/protocol/openid-connect/token";

    /**
     * Retrieve Token.
     *
     * @return OAuth2Token object containing access token string, token type, and expiration
     * @throws IOException - thrown if token retrieval failed
     */
    public CompletableFuture<OAuth2Token> retrieveToken(KeyManager keyManager) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return retrieveOAuth2Token(keyManager);
            } catch (IOException e) {
                throw new CompletionException(e.getCause());
            }
        });
    }

    private OAuth2Token retrieveOAuth2Token(KeyManager keyManager) throws IOException {
        return new DirectX509GrantTokenRequestBuilder()
            .withUrl(TOKEN_URL)
            .withClientId("cumulus")
            .withKeyManager(keyManager)
            .fetchToken();
    }

}
