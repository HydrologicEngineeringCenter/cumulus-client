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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.net.ssl.KeyManager;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfoBuilder;
import mil.army.usace.hec.cwms.http.client.MockHttpServer;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2TokenProvider;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

final class TestCumulusTokenProviderFactory {

    static MockHttpServer mockCumulusServer;
    static MockHttpServer mockAuthServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockCumulusServer = MockHttpServer.create();
        mockAuthServer = MockHttpServer.create();
        mockCumulusServer.start();
        mockAuthServer.start();

        mockCumulusServer.getMockServer().setDispatcher(new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                final HttpUrl url = request.getRequestUrl();
                final String path = url.encodedPath();
                System.out.println(path);
                try {
                    if (path.endsWith("configuration")) {
                        return new MockResponse().setBody(getResource("cumulus/json/idPConfig.json")
                                                .replace("PORT", ""+mockAuthServer.getPort()));
                    }
                } catch (IOException ex) {
                    fail("Couldn't process mocked request", ex);
                }
                return new MockResponse().setResponseCode(404).setBody("Request not mocked.");
            }
        });

        mockAuthServer.getMockServer().setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                final HttpUrl url = request.getRequestUrl();
                final String path = url.encodedPath();
                System.out.println("Got request for url: " + url);
                System.out.println("path: " + path);
                try {
                    if (path.endsWith("openid-configuration")) {
                        return new MockResponse().setBody(getResource("cumulus/json/openIdConfig.json")
                                                .replace("PORT", ""+mockAuthServer.getPort()));
                    }
                } catch (IOException ex) {
                    fail("Couldn't process mocked request", ex);
                }
                return new MockResponse().setResponseCode(404).setBody("Request not mocked.");
            }
        });
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockCumulusServer.shutdown();
        mockAuthServer.shutdown();
    }

    ApiConnectionInfo buildCumulusInfo() {
        String baseUrl = String.format("http://localhost:%s", mockCumulusServer.getPort());
        return new ApiConnectionInfoBuilder(baseUrl).build();
    }

    ApiConnectionInfo buildAuthInfo() {
        String baseUrl = String.format("http://localhost:%s", mockAuthServer.getPort());
        return new ApiConnectionInfoBuilder(baseUrl).build();
    }

    @Test
    void testNotNull() throws IOException {
        ApiConnectionInfo webServiceUrl = buildCumulusInfo();
        System.out.println("URL: " + webServiceUrl.getApiRoot());
        OAuth2TokenProvider tokenProvider = CumulusTokenProviderFactory.createTokenProvider(webServiceUrl.getApiRoot(), new KeyManager() {});
        assertNotNull(tokenProvider);
    }

    @Test
    void testNulls() {
        assertThrows(NullPointerException.class, () -> CumulusTokenProviderFactory.createTokenProvider("test", null));
        assertThrows(NullPointerException.class, () -> CumulusTokenProviderFactory.createTokenProvider(null, new KeyManager() {}));
    }

    protected static String getResource(String resource) throws IOException {
        URL resourceUrl = TestCumulusTokenProviderFactory.class.getClassLoader().getResource(resource);
        if (resourceUrl == null) {
            throw new IOException("Failed to get resource: " + resource);
        }
        Path path = new File(resourceUrl.getFile()).toPath();
        return String.join("\n", Files.readAllLines(path));
    }
}
