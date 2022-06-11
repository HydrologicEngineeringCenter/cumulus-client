package hec.army.usace.hec.cwbi.auth.http.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLSocketFactory;
import org.junit.jupiter.api.Test;

class TestSslSocketFactoryRegistry {
    @Test
    void testSingleInstanceSslFactory() throws IOException {
        KeyManager[] keyManagers = new KeyManager[]{getTestKeyManager()};
        SSLSocketFactory sslSocketFactory1 = SSLSocketFactoryRegistry.getRegistry().getSslSocketFactoryInstance(keyManagers);
        SSLSocketFactory sslSocketFactory2 = SSLSocketFactoryRegistry.getRegistry().getSslSocketFactoryInstance(keyManagers);
        assertEquals(sslSocketFactory1, sslSocketFactory2);

        KeyManager[] keyManagers2 = new KeyManager[]{getTestKeyManager()};
        SSLSocketFactory sslSocketFactory3 = SSLSocketFactoryRegistry.getRegistry().getSslSocketFactoryInstance(keyManagers2);
        assertEquals(sslSocketFactory1, sslSocketFactory3);

        KeyManager[] keyManagers3 = new KeyManager[]{getDifferentTestKeyManager()};
        SSLSocketFactory sslSocketFactory4 = SSLSocketFactoryRegistry.getRegistry().getSslSocketFactoryInstance(keyManagers3);
        assertNotEquals(sslSocketFactory1, sslSocketFactory4);
    }

    private KeyManager getTestKeyManager() {
        return new KeyManager(){
            @Override
            public boolean equals(Object obj) {
                return obj instanceof KeyManager;
            }

            @Override
            public int hashCode() {
                return 1;
            }
        };
    }

    private KeyManager getDifferentTestKeyManager() {
        return new KeyManager(){
            @Override
            public boolean equals(Object obj) {
                return obj instanceof KeyManager;
            }

            @Override
            public int hashCode() {
                return 2;
            }
        };
    }
}
