package hec.army.usace.hec.cwbi.auth.http.client;

import java.io.IOException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilder;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;

public class AuthenticatedHttpRequestBuilder implements CustomSslHttpRequestBuilder {

    private final ApiConnectionInfo apiConnectionInfo;
    private final String endpoint;
    private SslSocketData sslSocketData;
    private Authenticator authenticator;

    public AuthenticatedHttpRequestBuilder(ApiConnectionInfo apiConnectionInfo) {
        this.apiConnectionInfo = apiConnectionInfo;
        this.endpoint = "";
    }

    public AuthenticatedHttpRequestBuilder(ApiConnectionInfo apiConnectionInfo, String endpoint) {
        this.apiConnectionInfo = apiConnectionInfo;
        this.endpoint = endpoint;
    }

    @Override
    public HttpRequestBuilder withSslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager x509TrustManager)
        throws IOException {
        this.sslSocketData = new SslSocketData(sslSocketFactory, x509TrustManager);
        return new CustomHttpRequestBuilderImpl(apiConnectionInfo, endpoint);
    }

    @Override
    public CustomSslHttpRequestBuilder withAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
        return this;
    }

    private class CustomHttpRequestBuilderImpl extends HttpRequestBuilderImpl {

        public CustomHttpRequestBuilderImpl(ApiConnectionInfo apiConnectionInfo, String endpoint) throws IOException {
            super(apiConnectionInfo, endpoint);
        }

        @Override
        protected OkHttpClient buildOkHttpClient() {
            //newBuilder builds a client that shares the same connection pool, thread pools, and configuration.
            OkHttpClient retVal = super.buildOkHttpClient().newBuilder()
                .sslSocketFactory(sslSocketData.getSslSocketFactory(), sslSocketData.getX509TrustManager())
                .build();
            if (authenticator != null) {
                retVal = retVal.newBuilder()
                    .authenticator(authenticator)
                    .build();
            }
            return retVal;
        }
    }

}
