package mil.army.usace.hec.cumulus.client.controllers;

import static hec.army.usace.hec.cwbi.auth.http.client.trustmanagers.CwbiAuthTrustManager.TOKEN_URL;
import static mil.army.usace.hec.cumulus.client.controllers.CumulusAuthUtil.CLIENT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import javax.net.ssl.KeyManager;
import org.junit.jupiter.api.Test;

class TestCumulusAuthUtil {

    @Test
    void testBuildTokenProvider() throws IOException {
        CumulusTokenProvider tokenProvider = (CumulusTokenProvider) CumulusAuthUtil.buildCumulusTokenProvider(getTestKeyManager());
        assertEquals(TOKEN_URL, tokenProvider.getUrl());
        assertEquals(CLIENT_ID, tokenProvider.getClientId());
    }

    @Test
    void testNulls() {
        assertThrows(NullPointerException.class, () -> CumulusAuthUtil.buildCumulusTokenProvider(null));
    }

    private KeyManager getTestKeyManager() {
        return new KeyManager() {
        };
    }
}
