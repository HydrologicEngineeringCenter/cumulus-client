package hec.army.usace.hec.cwbi.auth.http.client;

import hec.army.usace.hec.cwbi.auth.http.client.token.fluentbuilders.DirectGrantX509TokenRequestFluentBuilder;
import hec.army.usace.hec.cwbi.auth.http.client.token.fluentbuilders.TokenRequestFluentBuilder;
import hec.army.usace.hec.cwbi.auth.http.client.trustmanagers.CwbiAuthTrustManager;
import java.io.IOException;
import java.util.Objects;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLSocketFactory;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;
import mil.army.usace.hec.cwms.http.client.request.HttpRequestExecutor;

public final class DirectGrantX509TokenRequestBuilder implements DirectGrantX509TokenRequestFluentBuilder {

    private SSLSocketFactory sslSocketFactory;

    @Override
    public TokenRequestFluentBuilder withKeyManager(KeyManager keyManager) throws IOException {
        KeyManager[] keyManagers = new KeyManager[] {Objects.requireNonNull(keyManager, "Missing required KeyManager")};
        return withKeyManagers(keyManagers);
    }

    @Override
    public TokenRequestFluentBuilder withKeyManagers(KeyManager[] keyManagers) throws IOException {
        sslSocketFactory = SSLSocketFactoryRegistry.getRegistry()
            .getSslSocketFactoryInstance(Objects.requireNonNull(keyManagers, "Missing required KeyManagers"));
        return new TokenRequestBuilderImpl();
    }

    private class TokenRequestBuilderImpl extends TokenRequestBuilder {

        @Override
        OAuth2Token retrieveToken() throws IOException {
            OAuth2Token retVal = null;
            HttpRequestExecutor executor =
                new AuthenticatedHttpRequestBuilder(new ApiConnectionInfo(getUrl()))
                    .withSslSocketFactory(sslSocketFactory, CwbiAuthTrustManager.getTrustManager())
                    .addQueryHeader("Content-Type", MEDIA_TYPE)
                    .enableHttp2()
                    .post()
                    .withBody(new UrlEncodedFormData()
                        .addPassword("")
                        .addGrantType("password")
                        .addScopes("openid", "profile")
                        .addClientId(getClientId())
                        .addUsername("")
                        .buildEncodedString())
                    .withMediaType(MEDIA_TYPE);
            try (HttpRequestResponse response = executor.execute()) {
                String body = response.getBody();
                if (body != null) {
                    retVal = OAuth2ObjectMapper.mapJsonToObject(body, OAuth2Token.class);
                }
            }
            return retVal;
        }
    }
}
