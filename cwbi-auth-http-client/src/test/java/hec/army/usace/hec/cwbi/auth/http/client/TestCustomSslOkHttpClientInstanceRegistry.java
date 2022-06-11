package hec.army.usace.hec.cwbi.auth.http.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hec.army.usace.hec.cwbi.auth.http.client.trustmanagers.CwbiAuthTrustManager;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

class TestCustomSslOkHttpClientInstanceRegistry {

    @Test
    void testSingleInstanceOkHttpClient() {
        SSLSocketFactory testSslSocketFactory = getTestSslSocketFactory();
        OkHttpClient okHttpClient = CustomSslOkHttpClientInstanceRegistry.getRegistry()
            .getOkHttpClientInstance(new SslSocketData(testSslSocketFactory, CwbiAuthTrustManager.getTrustManager()));

        OkHttpClient okHttpClient2 = CustomSslOkHttpClientInstanceRegistry.getRegistry()
            .getOkHttpClientInstance(new SslSocketData(testSslSocketFactory, CwbiAuthTrustManager.getTrustManager()));

        assertEquals(okHttpClient, okHttpClient2);
    }

    SSLSocketFactory getTestSslSocketFactory() {
        return new SSLSocketFactory() {
            @Override
            public String[] getDefaultCipherSuites() {
                return new String[0];
            }

            @Override
            public String[] getSupportedCipherSuites() {
                return new String[0];
            }

            @Override
            public Socket createSocket(Socket socket, String s, int i, boolean b) {
                return null;
            }

            @Override
            public Socket createSocket(String s, int i) {
                return null;
            }

            @Override
            public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) {
                return null;
            }

            @Override
            public Socket createSocket(InetAddress inetAddress, int i) {
                return null;
            }

            @Override
            public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) {
                return null;
            }
        };
    }
}
