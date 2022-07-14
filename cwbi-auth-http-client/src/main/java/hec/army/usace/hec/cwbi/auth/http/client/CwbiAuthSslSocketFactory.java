package hec.army.usace.hec.cwbi.auth.http.client;

import hec.army.usace.hec.cwbi.auth.http.client.trustmanagers.CwbiAuthTrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public final class CwbiAuthSslSocketFactory {

    private CwbiAuthSslSocketFactory() {
        throw new AssertionError("Utility class");
    }

    /**
     * Builds SSLSocketFactory configured for CWBI Auth and specified KeyManagers.
     * @param keyManagers - KeyManager list
     * @return SSLSocketFactory
     * @throws IOException - thrown if building SSLSocketFactory failed
     */
    public static SSLSocketFactory buildSSLSocketFactory(List<KeyManager> keyManagers) throws IOException {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(keyManagers.toArray(new KeyManager[]{}),
                new TrustManager[] {CwbiAuthTrustManager.getTrustManager()}, null);
            return sc.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new IOException(e);
        }
    }
}
