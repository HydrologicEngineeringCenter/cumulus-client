package hec.army.usace.hec.cwbi.auth.http.client;

import javax.net.ssl.X509TrustManager;

public interface RequestTrustManager {
    TokenRequestExecutor withX509TrustManager(X509TrustManager x509TrustManager);
}
