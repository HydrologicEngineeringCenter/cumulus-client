/*
 * MIT License
 *
 * Copyright (c) 2022 Hydrologic Engineering Center
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

package mil.army.usace.hec.cumulus.client.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import mil.army.usace.hec.cwms.http.client.MockHttpServer;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfoBuilder;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2TokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class TestCumulusMock {

    static MockHttpServer mockHttpServer;

    static ExecutorService executorService;

    @BeforeAll
    static void setUpExecutorService() {
        executorService = Executors.newFixedThreadPool(1);
    }

    @BeforeEach
    void setUp() throws IOException {
        mockHttpServer = MockHttpServer.create();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockHttpServer.shutdown();
    }

    protected ApiConnectionInfo buildConnectionInfo() {
        String baseUrl = String.format("http://localhost:%s", mockHttpServer.getPort());
        return new ApiConnectionInfoBuilder(baseUrl).build();
    }

    ApiConnectionInfo buildConnectionInfoWithAuth() {
        String baseUrl = String.format("http://localhost:%s", mockHttpServer.getPort());
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVXNlciIsImlzcyI6IlNpbXBsZSBTb2x1dGlvbiIsInVzZXJuYW1lIjoiVGVzdFVzZXIifQ.jQUKIOxN0KGbIGJx8SU3WfSVPNASOnRtt3DcoMVBeThcWGzEBAnwlHHYRvbzuas-sOeWSvOwrnsvpQ5tywAfWA";
        OAuth2Token oAuth2Token = new OAuth2Token();
        oAuth2Token.setAccessToken(token);
        oAuth2Token.setTokenType("Bearer");
        oAuth2Token.setExpiresIn(3600);
        return new ApiConnectionInfoBuilder(baseUrl)
            .withTokenProvider(getTestTokenProvider())
            .build();
    }

    protected static void enqueueMockServer(String body) {
        mockHttpServer.enqueue(body);
    }

    protected void launchMockServerWithResource(String resource) throws IOException {
        launchMockServerWithResources(resource);
    }

    protected void launchMockServerWithResources(String... resources) throws IOException {
        for (String resource : resources) {
            String body = readResourceAsString(resource);
            mockHttpServer.enqueue(body);
        }
        mockHttpServer.start();
    }

    protected String readResourceAsString(String resource) throws IOException {
        URL resourceUrl = getClass().getClassLoader().getResource(resource);
        if (resourceUrl == null) {
            throw new IOException("Failed to get resource: " + resource);
        }
        Path path = new File(resourceUrl.getFile()).toPath();
        return String.join("\n", Files.readAllLines(path));
    }

    private OAuth2TokenProvider getTestTokenProvider() {
        OAuth2Token oAuth2Token = new OAuth2Token();
        String token =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVXNlciIsImlzcyI6IlNpbXBsZSBTb2x1dGlvbiIsInVzZXJuYW1lIjoiVGVzdFVzZXIifQ.jQUKIOxN0KGbIGJx8SU3WfSVPNASOnRtt3DcoMVBeThcWGzEBAnwlHHYRvbzuas-sOeWSvOwrnsvpQ5tywAfWA";
        oAuth2Token.setAccessToken(token);
        oAuth2Token.setTokenType("Bearer");
        oAuth2Token.setExpiresIn(3600);
        return new OAuth2TokenProvider() {
            @Override
            public void clear() {
                // No-op
            }

            @Override
            public OAuth2Token getToken() {
                return oAuth2Token;
            }

            @Override
            public OAuth2Token refreshToken() {
                return oAuth2Token;
            }

            @Override
            public OAuth2Token newToken() throws IOException {
                return null;
            }
        };
    }

}
