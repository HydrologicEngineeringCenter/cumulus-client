package mil.army.usace.hec.cumulus.client.controllers;

import hec.army.usace.hec.cwbi.auth.http.client.JwtValidator;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.KeyManager;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2TokenValidator;
import okhttp3.OkHttpClient;

final class CumulusHttpRequestBuilderImpl extends HttpRequestBuilderImpl {

    private static final Logger LOGGER = Logger.getLogger(CumulusHttpRequestBuilderImpl.class.getName());
    private final KeyManager keyManager;

    /**
     * Authenticated Http Request Builder.
     *
     * @param apiConnectionInfo - connection info
     * @param endpoint          - endpoint for request
     * @throws IOException      - thrown if request failed
     */
    CumulusHttpRequestBuilderImpl(KeyManager keyManager, ApiConnectionInfo apiConnectionInfo, String endpoint) throws IOException {

        super(apiConnectionInfo, endpoint);
        this.keyManager = keyManager;
    }

    @Override
    protected OkHttpClient buildOkHttpClient() {
        try {
            return super.buildOkHttpClient().newBuilder()
                .authenticator(new CumulusAccessTokenAuthenticator(keyManager))
                .build();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error building OkHttpClient", e);
        }
        return null;
    }

    @Override
    protected OAuth2TokenValidator createOAuth2TokenValidator() {
        return new JwtValidator();
    }
}
