package mil.army.usace.hec.cumulus.client.controllers;


import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;
import mil.army.usace.hec.cwms.http.client.model.OAuth2Token;
import mil.army.usace.hec.cwms.http.client.request.HttpRequestExecutor;

public class CumulusTokenController {

    private static final String TOKEN_URL = "https://auth.corps.cloud/auth/realms/water/protocol/openid-connect/";
    private static final String TOKEN_ENDPOINT = "token";

    /**
     * Retrieve Token.
     *
     * @return OAuth2Token object containing access token string, token type, and expiration
     * @throws IOException - thrown if token retrieval failed
     */
    public CompletableFuture<OAuth2Token> retrieveToken(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return retrieveOAuth2Token(sslSocketFactory, trustManager);
            } catch (IOException e) {
                throw new CompletionException(e.getCause());
            }
        });
    }

    private OAuth2Token retrieveOAuth2Token(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) throws IOException {
        if (sslSocketFactory == null) {
            throw new IOException("Missing required SSLSocketFactory");
        }
        if (trustManager == null) {
            throw new IOException("Missing required X509TrustManager");
        }
        OAuth2Token retVal = null;
        HttpRequestExecutor executor =
            new HttpRequestBuilderImpl(new ApiConnectionInfo(TOKEN_URL), TOKEN_ENDPOINT)
                .addQueryHeader("Content-Type", "application/x-www-form-urlencoded")
                .enableHttp2()
                .withSslSocketFactory(sslSocketFactory, trustManager)
                .post()
                .withBody("password=&grant_type=password&scope=openid%20profile&client_id=cumulus&username=")
                .withMediaType("application/x-www-form-urlencoded");
        try (HttpRequestResponse response = executor.execute()) {
            String body = response.getBody();
            if (body != null) {
                retVal = CumulusObjectMapper.mapJsonToObject(body, OAuth2Token.class);
            }
        }
        return retVal;
    }
}
