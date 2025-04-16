/*
 * MIT License
 *
 * Copyright (c) 2025 Hydrologic Engineering Center
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package mil.army.usace.hec.cumulus.client.auth;

import hec.army.usace.hec.cwbi.auth.http.client.trustmanagers.CwbiAuthTrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;
import mil.army.usace.hec.cumulus.client.controllers.TestCumulusMock;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.SslSocketData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

final class TestCumulusTokenUrlDiscoveryService extends TestCumulusMock {

    @Test
    void testCumulusTokenUrlDiscoveryService() throws IOException {
        String resource = "cumulus/json/idPConfig.json";
        launchMockServerWithResource(resource);
        SslSocketData sslSocketData = new SslSocketData(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager());
        ApiConnectionInfo webServiceUrl = buildConnectionInfo();
        CumulusTokenUrlDiscoveryService discoveryService = new CumulusTokenUrlDiscoveryService(webServiceUrl, sslSocketData);
        assertEquals("https://api.example.com/oauth2/token", discoveryService.discoverTokenUrl().getApiRoot());
    }

    @Test
    void testNulls() {
        SslSocketData sslSocketData = new SslSocketData(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager());
        ApiConnectionInfo webServiceUrl = buildConnectionInfo();
        assertThrows(NullPointerException.class, () -> new CumulusTokenUrlDiscoveryService(null, sslSocketData));
        assertThrows(NullPointerException.class, () -> new CumulusTokenUrlDiscoveryService(webServiceUrl, null));
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
