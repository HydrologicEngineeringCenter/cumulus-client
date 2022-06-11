package hec.army.usace.hec.cwbi.auth.http.client;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilder;
import mil.army.usace.hec.cwms.http.client.ServerNotFoundException;

interface CustomSslHttpRequestBuilder {
    HttpRequestBuilder withSslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager x509TrustManager) throws ServerNotFoundException;
}
