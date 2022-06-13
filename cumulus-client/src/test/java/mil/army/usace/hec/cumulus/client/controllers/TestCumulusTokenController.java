package mil.army.usace.hec.cumulus.client.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyStoreException;
import java.util.concurrent.CompletionException;
import javax.net.ssl.SSLSocketFactory;
import mil.army.usace.hec.cumulus.client.controllers.ssl.CacKeyManager;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;
import org.junit.jupiter.api.Test;

class TestCumulusTokenController {

    @Test
    void testRetrieveTokenMissingParams() {
        CompletionException ex = assertThrows(CompletionException.class, () -> {
            OAuth2Token token = new CumulusTokenController().retrieveTokenWithDirectGrantX509(null).join();
            assertNull(token);
        });
        assertEquals("Missing required SSLSocketFactory", ex.getCause().getMessage());

    }

    private SSLSocketFactory getTestSSLSocketFactory() {
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

    @Test
    void testLive() throws KeyStoreException {
        OAuth2Token oAuth2Token = new CumulusTokenController().retrieveTokenWithDirectGrantX509(CacKeyManager.getKeyManager()).join();
        String accessToken = oAuth2Token.getAccessToken();
        assertNotNull(accessToken);

        OAuth2Token oAuth2TokenFromRefresh = new CumulusTokenController().retrieveTokenWithRefreshToken(oAuth2Token.getRefreshToken()).join();
        String accessTokenFromRefresh = oAuth2TokenFromRefresh.getAccessToken();
        assertNotNull(accessTokenFromRefresh);
    }
}
