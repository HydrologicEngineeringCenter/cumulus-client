package hec.army.usace.hec.cwbi.auth.http.client;

import hec.army.usace.hec.cwbi.auth.http.client.trustmanagers.CwbiAuthTrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

final class SSLSocketFactoryRegistry {

    private static final Map<List<KeyManager>, SSLSocketFactory> REGISTRY = new ConcurrentHashMap<>();

    private SSLSocketFactoryRegistry() {
        //singleton class
    }

    static SSLSocketFactoryRegistry getRegistry() {
        return SSLSocketFactoryRegistry.InstanceHolder.INSTANCE;
    }

    /**
     * Get SSLSocketFactory instance if it already exists for given KeyManagers.
     * @param keyManagers - array of KeyManager
     * @return SSLSocketFactory
     * @throws IOException - thrown is failed to create SSLSocketFactory instance
     */
    SSLSocketFactory getSslSocketFactoryInstance(KeyManager[] keyManagers) throws IOException {
        List<KeyManager> keyManagersList = Arrays.asList(keyManagers);
        SSLSocketFactory sslSocketFactory = REGISTRY.get(keyManagersList);
        if (sslSocketFactory == null) {
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(keyManagers,
                    new TrustManager[] {CwbiAuthTrustManager.getTrustManager()}, null);
                sslSocketFactory = sc.getSocketFactory();
                REGISTRY.put(keyManagersList, sslSocketFactory);
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                throw new IOException(e);
            }
        }
        return sslSocketFactory;
    }

    private static class InstanceHolder {
        static final SSLSocketFactoryRegistry INSTANCE = new SSLSocketFactoryRegistry();
    }
}
