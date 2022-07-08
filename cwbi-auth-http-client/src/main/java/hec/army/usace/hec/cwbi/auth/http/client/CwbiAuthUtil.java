package hec.army.usace.hec.cwbi.auth.http.client;


import static hec.army.usace.hec.cwbi.auth.http.client.trustmanagers.CwbiAuthTrustManager.TOKEN_URL;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLSocketFactory;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2TokenProvider;

public final class CwbiAuthUtil {

    private CwbiAuthUtil() {
        throw new AssertionError("Utility class");
    }

    /**
     * Builds CumulusTokenProvider for retrieving and refreshing tokens for cumulus authentication.
     * @param keyManager - KeyManager for client
     * @return OAuth2TokenProvider - CumulusTokenProvider
     * @throws IOException - thrown if failed to build CumulusTokenProvider
     */
    public static OAuth2TokenProvider buildCumulusTokenProvider(String clientId, KeyManager keyManager) throws IOException {
        SSLSocketFactory sslSocketFactory = CwbiAuthSslSocketFactory.buildSSLSocketFactory(
            Collections.singletonList(Objects.requireNonNull(keyManager, "Missing required KeyManager")));
        return new CwbiAuthTokenProvider(TOKEN_URL, clientId, sslSocketFactory);
    }

}
