package mil.army.usace.hec.cumulus.client.controllers;


import hec.army.usace.hec.cumulus.http.client.ssl.CumulusCacTrustManager;
import hec.army.usace.hec.cumulus.http.client.ssl.CumulusSslSocketFactoryBuilder;
import java.io.IOException;
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
    public OAuth2Token retrieveToken() throws IOException {
        OAuth2Token retVal = null;
        HttpRequestExecutor executor =
            new HttpRequestBuilderImpl(new ApiConnectionInfo(TOKEN_URL), TOKEN_ENDPOINT)
                .addQueryHeader("Content-Type", "application/x-www-form-urlencoded")
                .enableHttp2()
                .withSslSocketFactory(CumulusSslSocketFactoryBuilder.buildSslSocketFactory(), CumulusCacTrustManager.getTrustManager())
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
