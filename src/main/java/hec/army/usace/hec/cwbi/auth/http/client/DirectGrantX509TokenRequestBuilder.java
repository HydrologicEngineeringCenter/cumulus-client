package hec.army.usace.hec.cwbi.auth.http.client;

import hec.army.usace.hec.cwbi.auth.http.client.trustmanagers.CwbiAuthTrustManager;
import java.io.IOException;
import java.util.Objects;
import javax.net.ssl.SSLSocketFactory;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfoBuilder;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;
import mil.army.usace.hec.cwms.http.client.SslSocketData;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;
import mil.army.usace.hec.cwms.http.client.request.HttpRequestExecutor;

public final class DirectGrantX509TokenRequestBuilder implements DirectGrantX509TokenRequestFluentBuilder {

    private SslSocketData sslSocketData;

    @Override
    public TokenRequestFluentBuilder withSSlSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketData = new SslSocketData(Objects.requireNonNull(sslSocketFactory, "Missing required SSLSocketFactory"),
            CwbiAuthTrustManager.getTrustManager());
        return new TokenRequestBuilderImpl();
    }

    private class TokenRequestBuilderImpl extends TokenRequestBuilder {

        @Override
        OAuth2Token retrieveToken() throws IOException {
            OAuth2Token retVal = null;
            String formBody = new UrlEncodedFormData()
                .addPassword("")
                .addGrantType("password")
                .addScopes("openid", "profile")
                .addClientId(getClientId())
                .addUsername("")
                .buildEncodedString();
            HttpRequestExecutor executor =
                new HttpRequestBuilderImpl(new ApiConnectionInfoBuilder(getUrl())
                    .withSslSocketData(sslSocketData).build())
                    .addQueryHeader("Content-Type", MEDIA_TYPE)
                    .enableHttp2()
                    .post()
                    .withBody(formBody)
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
