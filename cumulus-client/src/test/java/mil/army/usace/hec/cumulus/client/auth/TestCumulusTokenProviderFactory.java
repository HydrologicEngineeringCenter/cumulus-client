/*
 * MIT License
 *
 * Copyright (c) 2025 Hydrologic Engineering Center
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package mil.army.usace.hec.cumulus.client.auth;

import java.io.IOException;
import javax.net.ssl.KeyManager;
import mil.army.usace.hec.cumulus.client.controllers.TestCumulusMock;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2TokenProvider;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

final class TestCumulusTokenProviderFactory extends TestCumulusMock {

    @Test
    void testNotNull() throws IOException {
        String resource = "cumulus/json/idPConfig.json";
        launchMockServerWithResource(resource);
        ApiConnectionInfo webServiceUrl = buildConnectionInfo();
        OAuth2TokenProvider tokenProvider = CumulusTokenProviderFactory.createTokenProvider(webServiceUrl.getApiRoot(), new KeyManager() {});
        assertNotNull(tokenProvider);
    }

    @Test
    void testNulls() {
        assertThrows(NullPointerException.class, () -> CumulusTokenProviderFactory.createTokenProvider("test", null));
        assertThrows(NullPointerException.class, () -> CumulusTokenProviderFactory.createTokenProvider(null, new KeyManager() {}));
    }
}
