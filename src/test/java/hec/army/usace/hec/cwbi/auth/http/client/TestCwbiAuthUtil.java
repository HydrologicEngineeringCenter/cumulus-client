package hec.army.usace.hec.cwbi.auth.http.client;

import static hec.army.usace.hec.cwbi.auth.http.client.trustmanagers.CwbiAuthTrustManager.TOKEN_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import javax.net.ssl.KeyManager;
import org.junit.jupiter.api.Test;

class TestCwbiAuthUtil {

    @Test
    void testBuildTokenProvider() throws IOException {
        CwbiAuthTokenProvider tokenProvider = (CwbiAuthTokenProvider) CwbiAuthUtil.buildCwbiAuthTokenProvider("cumulus", getTestKeyManager());
        assertEquals(TOKEN_URL, tokenProvider.getUrl());
        assertEquals("cumulus", tokenProvider.getClientId());
    }

    @Test
    void testNulls() {
        assertThrows(NullPointerException.class, () -> CwbiAuthUtil.buildCwbiAuthTokenProvider("cumulus", null));
    }

    private KeyManager getTestKeyManager() {
        return new KeyManager() {
        };
    }
}
