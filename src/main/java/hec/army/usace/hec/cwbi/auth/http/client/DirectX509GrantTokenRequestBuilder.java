package hec.army.usace.hec.cwbi.auth.http.client;

import hec.army.usace.hec.cwbi.auth.http.client.trustmanagers.CwbiAuthTrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;
import mil.army.usace.hec.cwms.http.client.model.OAuth2Token;
import mil.army.usace.hec.cwms.http.client.request.HttpRequestExecutor;

public class DirectX509GrantTokenRequestBuilder implements DirectX509GrantTokenRequestFluentBuilder {

    private static final String MEDIA_TYPE = "application/x-www-form-urlencoded";
    private String url;
    private String clientId;
    private SSLSocketFactory sslSocketFactory;

    @Override
    public RequestClientId withUrl(String url) {
        this.url = Objects.requireNonNull(url, "Missing required URL");
        return new RequestClientIdImpl();
    }

    private class RequestClientIdImpl implements RequestClientId {

        @Override
        public RequestKeyManagers withClientId(String clientIdString) {
            clientId = Objects.requireNonNull(clientIdString, "Missing required Client ID");
            return new RequestKeyManagersImpl();
        }
    }

    private class RequestKeyManagersImpl implements RequestKeyManagers {

        @Override
        public TokenRequestExecutor withKeyManager(KeyManager keyManager) throws IOException {
            KeyManager[] keyManagers = new KeyManager[] {Objects.requireNonNull(keyManager, "Missing required KeyManager")};
            return withKeyManagers(keyManagers);
        }

        @Override
        public TokenRequestExecutor withKeyManagers(KeyManager[] keyManagers) throws IOException {
            sslSocketFactory = buildSslSocketFactory(Objects.requireNonNull(keyManagers, "Missing required KeyManagers"));
            return new TokenRequestExecutorImpl();
        }

        private SSLSocketFactory buildSslSocketFactory(KeyManager[] keyManagers) throws IOException {
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(keyManagers,
                    new TrustManager[] {CwbiAuthTrustManager.getTrustManager()}, null);
                return sc.getSocketFactory();
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                throw new IOException(e);
            }
        }
    }

    private class TokenRequestExecutorImpl implements TokenRequestExecutor {

        @Override
        public OAuth2Token fetchToken() throws IOException {
            OAuth2Token retVal = null;
            HttpRequestExecutor executor =
                new HttpRequestBuilderImpl(new ApiConnectionInfo(url),"")
                    .addQueryHeader("Content-Type", MEDIA_TYPE)
                    .enableHttp2()
                    .withSslSocketFactory(sslSocketFactory, CwbiAuthTrustManager.getTrustManager())
                    .post()
                    .withBody("password=&grant_type=password&scope=openid%20profile&client_id=" + clientId + "&username=")
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
