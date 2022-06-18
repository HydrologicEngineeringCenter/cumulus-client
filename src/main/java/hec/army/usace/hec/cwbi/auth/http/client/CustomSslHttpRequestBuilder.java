package hec.army.usace.hec.cwbi.auth.http.client;

import java.io.IOException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilder;
import okhttp3.Authenticator;

interface CustomSslHttpRequestBuilder {

    HttpRequestBuilder withSslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager x509TrustManager) throws IOException;

    CustomSslHttpRequestBuilder withAuthenticator(Authenticator authenticator);
}
