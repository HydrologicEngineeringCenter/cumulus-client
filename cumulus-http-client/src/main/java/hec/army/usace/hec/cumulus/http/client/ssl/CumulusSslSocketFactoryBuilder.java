package hec.army.usace.hec.cumulus.http.client.ssl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public final class CumulusSslSocketFactoryBuilder {

    private CumulusSslSocketFactoryBuilder() {
        throw new AssertionError("Utility class");
    }

    /**
     * Build SSL Socket Factory.
     * @return SSLSocketFactory
     * @throws IOException - thrown if failed to build SSLSocketFactory
     */
    public static SSLSocketFactory buildSslSocketFactory() throws IOException {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(new KeyManager[] {CacKeyManager.getKeyManager()},
                new TrustManager[] {CumulusCacTrustManager.getTrustManager()}, null);
            return sc.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new IOException(e);
        }
    }
}