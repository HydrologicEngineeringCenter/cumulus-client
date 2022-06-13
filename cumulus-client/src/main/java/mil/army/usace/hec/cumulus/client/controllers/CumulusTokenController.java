package mil.army.usace.hec.cumulus.client.controllers;


import hec.army.usace.hec.cwbi.auth.http.client.DirectGrantX509TokenRequestBuilder;
import hec.army.usace.hec.cwbi.auth.http.client.JwtTokenValidator;
import hec.army.usace.hec.cwbi.auth.http.client.RefreshTokenRequestBuilder;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import javax.net.ssl.KeyManager;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;

public class CumulusTokenController {

    private static final String TOKEN_URL = "https://auth.corps.cloud/auth/realms/water/protocol/openid-connect/token";
    private static final String CLIENT_ID = "cumulus";

    /**
     * Retrieve Token via Direct Grant X509.
     *
     * @return OAuth2Token object containing access token string, token type, and expiration
     */
    public CompletableFuture<OAuth2Token> retrieveTokenWithDirectGrantX509(KeyManager keyManager) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return retrieveOAuth2TokenWithDirectGrantX509(keyManager);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    private OAuth2Token retrieveOAuth2TokenWithDirectGrantX509(KeyManager keyManager) throws IOException {
        return new DirectGrantX509TokenRequestBuilder()
            .withKeyManager(keyManager)
            .withUrl(TOKEN_URL)
            .withClientId(CLIENT_ID)
            .fetchToken();
    }

    /**
     * Retrieve Token via Refresh Token.
     *
     * @return OAuth2Token object containing access token string, token type, and expiration
     */
    public CompletableFuture<OAuth2Token> retrieveTokenWithRefreshToken(String refreshToken) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                JwtTokenValidator.validateToken(refreshToken);
                return retrieveOAuth2TokenWithRefreshToken(refreshToken);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    private OAuth2Token retrieveOAuth2TokenWithRefreshToken(String refreshToken) throws IOException {
        return new RefreshTokenRequestBuilder()
            .withRefreshToken(refreshToken)
            .withUrl(TOKEN_URL)
            .withClientId(CLIENT_ID)
            .fetchToken();
    }

}
