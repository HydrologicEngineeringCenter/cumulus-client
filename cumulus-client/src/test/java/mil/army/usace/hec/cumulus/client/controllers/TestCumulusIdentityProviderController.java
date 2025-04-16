package mil.army.usace.hec.cumulus.client.controllers;

import hec.army.usace.hec.cwbi.auth.http.client.trustmanagers.CwbiAuthTrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;
import mil.army.usace.hec.cumulus.client.model.IdentityProviderConfiguration;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.SslSocketData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

final class TestCumulusIdentityProviderController extends TestCumulusMock {

    @Test
    void testRetrieveTokenUrl() throws IOException {
        String resource = "cumulus/json/idPConfig.json";
        launchMockServerWithResource(resource);
        SslSocketData sslSocketData = new SslSocketData(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager());
        ApiConnectionInfo tokenUrl = new CumulusIdentityProviderController().retrieveTokenUrl(buildConnectionInfo(), sslSocketData);
        assertEquals("https://api.example.com/oauth2/token", tokenUrl.getApiRoot());
    }

    @Test
    void testRetrieveConfig() throws IOException {
        String resource = "cumulus/json/idPConfig.json";
        launchMockServerWithResource(resource);
        IdentityProviderConfiguration config = new CumulusIdentityProviderController().retrieveConfiguration(buildConnectionInfo());
        assertEquals("https://api.example.com/oauth2/token", config.getTokenEndpoint());
        assertEquals("https://api.example.com/.well-known/openid-configuration", config.getWellKnownEndpoint());
    }

    private SSLSocketFactory getTestSslSocketFactory() {
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
