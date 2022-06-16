package hec.army.usace.hec.cwbi.auth.http.client;

import java.io.IOException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilder;

interface CustomSslHttpRequestBuilder {
    HttpRequestBuilder withSslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager x509TrustManager) throws IOException;
}
