package hec.army.usace.hec.cwbi.auth.http.client.trustmanagers;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

public final class CwbiAuthTrustManager implements X509TrustManager {

    private final TrustManagerFactory trustManagerFactory;

    private CwbiAuthTrustManager(TrustManagerFactory trustManagerFactory) {
        this.trustManagerFactory = trustManagerFactory;
    }

    /**
     * Get X509TrustManager instance.
     *
     * @return Instance of X509TrustManager
     */
    public static X509TrustManager getTrustManager() throws IOException {
        try (InputStream trustedCertificateAsInputStream = CwbiAuthTrustManager.class.getResourceAsStream("cumulusServer.pem")) {
            KeyStore ts = KeyStore.getInstance("JKS");
            ts.load(null, null);
            Certificate trustedCertificate = CertificateFactory.getInstance("X.509").generateCertificate(trustedCertificateAsInputStream);
            ts.setCertificateEntry("cwbi-auth-server-root-certificate", trustedCertificate);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX");
            trustManagerFactory.init(ts);
            return new CwbiAuthTrustManager(trustManagerFactory);
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
            if (trustManager instanceof X509TrustManager) {
                try {
                    ((X509TrustManager) trustManager).checkClientTrusted(x509Certificates, s);
                } catch (CertificateException e) {
                    String notTrustedMsg = "Certificate chain not part of trusted certificates for this JRE: ";
                    notTrustedMsg = notTrustedMsg + Arrays.stream(x509Certificates)
                        .map(X509Certificate::getSubjectX500Principal)
                        .map(X500Principal::getName)
                        .collect(Collectors.joining(","));
                    throw new CertificateException(notTrustedMsg, e);
                }
            }
        }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
            if (trustManager instanceof X509TrustManager) {
                try {
                    ((X509TrustManager) trustManager).checkServerTrusted(x509Certificates, s);
                } catch (CertificateException e) {
                    String notTrustedMsg = "Certificate chain not part of trusted certificates for this JRE: ";
                    notTrustedMsg = notTrustedMsg + Arrays.stream(x509Certificates)
                        .map(X509Certificate::getSubjectX500Principal)
                        .map(X500Principal::getName)
                        .collect(Collectors.joining(","));
                    throw new CertificateException(notTrustedMsg, e);
                }
            }
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return Arrays.stream(trustManagerFactory.getTrustManagers())
            .filter(X509TrustManager.class::isInstance)
            .map(X509TrustManager.class::cast)
            .map(X509TrustManager::getAcceptedIssuers)
            .flatMap(Arrays::stream)
            .toArray(X509Certificate[]::new);
    }

}
