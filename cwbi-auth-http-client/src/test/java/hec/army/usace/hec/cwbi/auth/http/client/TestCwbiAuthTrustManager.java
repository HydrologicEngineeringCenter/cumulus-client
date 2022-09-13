package hec.army.usace.hec.cwbi.auth.http.client;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hec.army.usace.hec.cwbi.auth.http.client.trustmanagers.CwbiAuthTrustManager;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.net.ssl.X509TrustManager;
import org.junit.jupiter.api.Test;

final class TestCwbiAuthTrustManager {

    @Test
    void testGetTrustManager() {
        X509TrustManager trustManager = CwbiAuthTrustManager.getTrustManager();
        assertNotNull(trustManager);
        X509Certificate[] acceptedIssuers = trustManager.getAcceptedIssuers();
        assertFalse(Arrays.asList(acceptedIssuers).isEmpty());
        List<String> details = Arrays.asList(acceptedIssuers[0].getIssuerDN().toString().split(","));
        details = details.stream().map(String::trim)
                    .collect(toList());
        assertTrue(details.contains("CN=ISRG Root X1"));
        assertTrue(details.contains("O=Internet Security Research Group"));
        assertTrue(details.contains("C=US"));
    }

    @Test
    void testCheckValidity() {
        X509TrustManager trustManager = CwbiAuthTrustManager.getTrustManager();
        assertNotNull(trustManager);
        X509Certificate[] acceptedIssuers = trustManager.getAcceptedIssuers();
        X509Certificate issuer = acceptedIssuers[0];
        assertThrows(CertificateExpiredException.class, () -> issuer.checkValidity(getExpiredDate(issuer.getNotAfter())));
    }

    public static Date getExpiredDate(Date dateOfExpiration) {
        Instant expiredInstant = dateOfExpiration.toInstant().plus(Duration.ofDays(1));
        return Date.from(expiredInstant);
    }

}
