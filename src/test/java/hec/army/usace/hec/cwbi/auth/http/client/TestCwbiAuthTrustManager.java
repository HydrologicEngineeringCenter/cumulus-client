package hec.army.usace.hec.cwbi.auth.http.client;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import hec.army.usace.hec.cwbi.auth.http.client.trustmanagers.CwbiAuthTrustManager;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import javax.net.ssl.X509TrustManager;
import org.junit.jupiter.api.Test;

final class TestCwbiAuthTrustManager {

    @Test
    void testGetTrustManager() {
        X509TrustManager trustManager = CwbiAuthTrustManager.getTrustManager();
        assertNotNull(trustManager);
        X509Certificate[] acceptedIssuers = trustManager.getAcceptedIssuers();
        assertFalse(Arrays.asList(acceptedIssuers).isEmpty());
    }

}
