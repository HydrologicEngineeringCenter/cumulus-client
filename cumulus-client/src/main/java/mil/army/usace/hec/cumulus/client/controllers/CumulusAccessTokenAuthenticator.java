package mil.army.usace.hec.cumulus.client.controllers;

import static mil.army.usace.hec.cumulus.client.controllers.CumulusTokenController.CLIENT_ID;
import static mil.army.usace.hec.cumulus.client.controllers.CumulusTokenController.TOKEN_URL;

import hec.army.usace.hec.cwbi.auth.http.client.AccessTokenAuthenticator;
import hec.army.usace.hec.cwbi.auth.http.client.AccessTokenProvider;
import hec.army.usace.hec.cwbi.auth.http.client.AccessTokenProviderImpl;
import hec.army.usace.hec.cwbi.auth.http.client.CwbiAuthSslSocketFactory;
import java.io.IOException;
import java.util.Collections;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLSocketFactory;

final class CumulusAccessTokenAuthenticator extends AccessTokenAuthenticator {

    private final KeyManager keyManager;

    CumulusAccessTokenAuthenticator(KeyManager keyManager) throws IOException {
        this.keyManager = keyManager;
    }

    @Override
    public AccessTokenProvider getAccessTokenProvider() throws IOException {
        SSLSocketFactory sslSocketFactory = CwbiAuthSslSocketFactory.buildSSLSocketFactory(Collections.singletonList(keyManager));
        return new AccessTokenProviderImpl(TOKEN_URL, CLIENT_ID, sslSocketFactory);
    }

}
