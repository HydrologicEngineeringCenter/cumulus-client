package hec.army.usace.hec.cwbi.auth.http.client;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilder;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.ServerNotFoundException;
import okhttp3.OkHttpClient;

public final class AuthenticatedHttpRequestBuilder implements CustomSslHttpRequestBuilder {

    private final ApiConnectionInfo apiConnectionInfo;
    private final String endpoint;
    private SslSocketData sslSocketData;

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
        throws ServerNotFoundException {
        this.sslSocketData = new SslSocketData(sslSocketFactory, x509TrustManager);
        return new CustomHttpRequestBuilderImpl(apiConnectionInfo, endpoint);
    }

    private class CustomHttpRequestBuilderImpl extends HttpRequestBuilderImpl {

        public CustomHttpRequestBuilderImpl(ApiConnectionInfo apiConnectionInfo, String endpoint) throws ServerNotFoundException {
            super(apiConnectionInfo, endpoint);
        }

        @Override
        protected OkHttpClient buildOkHttpClient() {
            return CustomSslOkHttpClientInstanceRegistry.getRegistry()
                .getOkHttpClientInstance(sslSocketData);
        }
    }

}
