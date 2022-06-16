package hec.army.usace.hec.cwbi.auth.http.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hec.army.usace.hec.cwbi.auth.http.client.trustmanagers.CwbiAuthTrustManager;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.net.ssl.SSLSocketFactory;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;
import mil.army.usace.hec.cwms.http.client.NoDataFoundException;
import mil.army.usace.hec.cwms.http.client.ServerNotFoundException;
import mil.army.usace.hec.cwms.http.client.request.HttpRequestExecutor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;

class TestAuthenticatedHttpRequestBuilder {
    static final String ACCEPT_HEADER_V1 = "application/json";

    @Test
    void testHttpRequestBuilderCreateRequestInvalidUrl() {
        String root = "//http://localhost:11524/cwms-data/";
        String endpoint = "timeseries";
        ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(root);
        assertThrows(ServerNotFoundException.class, () -> new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint)
            .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager()));
    }

    @Test
    void testHttpRequestBuilderCreateRequestNullRoot() {
        String root = null;
        String endpoint = "timeseries";
        ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(root);
        assertThrows(NullPointerException.class, () -> new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint)
            .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager()));
    }

    @Test
    void testHttpRequestBuilderCreateRequestNullEndpoint() {
        String root = "http://localhost:11524/cwms-data/";
        String endpoint = null;
        ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(root);
        assertThrows(NullPointerException.class, () -> new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint)
            .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager()));
    }

    @Test
    void testHttpRequestBuilderExecuteGetSuccess() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        try {
            String body = readJsonFile("success.json");
            mockWebServer.enqueue(new MockResponse().setBody(body).setResponseCode(200));
            mockWebServer.start();
            String endpoint = "success";
            String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
            ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(baseUrl);
            HttpRequestExecutor executer = new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint)
                .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
                .get()
                .withMediaType(ACCEPT_HEADER_V1);
            try (HttpRequestResponse response = executer.execute()) {
                assertNotNull(response.getBody());
            }
        } finally {
            mockWebServer.shutdown();
        }
    }

    @Test
    void testHttpRequestBuilderExecuteGetSuccessHttp2() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        try {
            String body = readJsonFile("success.json");
            mockWebServer.enqueue(new MockResponse().setBody(body).setResponseCode(200));
            mockWebServer.start();
            String endpoint = "success";
            String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
            ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(baseUrl);
            HttpRequestExecutor executer = new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint)
                .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
                .enableHttp2()
                .get()
                .withMediaType(ACCEPT_HEADER_V1);
            try (HttpRequestResponse response = executer.execute()) {
                assertNotNull(response.getBody());
            }
        } finally {
            mockWebServer.shutdown();
        }
    }

    @Test
    void testHttpRequestBuilderExecutePostSuccess() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        try {
            String body = readJsonFile("success.json");
            mockWebServer.enqueue(new MockResponse().setBody(body).setResponseCode(200));
            mockWebServer.start();
            String endpoint = "success";
            String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
            ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(baseUrl);
            HttpRequestExecutor executor = new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint)
                .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
                .post()
                .withBody("{test}")
                .withMediaType(ACCEPT_HEADER_V1);
            try (HttpRequestResponse response = executor.execute()) {
                assertNotNull(response.getBody());
            }

        } finally {
            mockWebServer.shutdown();
        }
    }

    @Test
    void testHttpRequestBuilderExecutePostSuccessHttp2() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        try {
            String body = readJsonFile("success.json");
            mockWebServer.enqueue(new MockResponse().setBody(body).setResponseCode(200));
            mockWebServer.start();
            String endpoint = "success";
            String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
            ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(baseUrl);
            HttpRequestExecutor executor = new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint)
                .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
                .enableHttp2()
                .post()
                .withBody("{test}")
                .withMediaType(ACCEPT_HEADER_V1);
            try (HttpRequestResponse response = executor.execute()) {
                assertNotNull(response.getBody());
            }

        } finally {
            mockWebServer.shutdown();
        }
    }

    @Test
    void testHttpRequestBuilderExecuteGetServerError() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        try {
            String body = readJsonFile("servererror.json");
            mockWebServer.enqueue(new MockResponse().setBody(body).setResponseCode(500));
            mockWebServer.start();
            String endpoint = "success";
            String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
            ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(baseUrl);
            AuthenticatedHttpRequestBuilder builder = new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint);
            HttpRequestExecutor executor = builder
                .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
                .get()
                .withMediaType(ACCEPT_HEADER_V1);
            assertThrows(IOException.class, () -> {
                try (HttpRequestResponse response = executor.execute()) {
                    assertNull(response);
                }
            });

        } finally {
            mockWebServer.shutdown();
        }
    }

    @Test
    void testHttpRequestBuilderExecuteGetServerErrorHttp2() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        try {
            String body = readJsonFile("servererror.json");
            mockWebServer.enqueue(new MockResponse().setBody(body).setResponseCode(500));
            mockWebServer.start();
            String endpoint = "success";
            String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
            ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(baseUrl);
            AuthenticatedHttpRequestBuilder builder = new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint);
            HttpRequestExecutor executor = builder
                .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
                .enableHttp2().get().withMediaType(ACCEPT_HEADER_V1);
            assertThrows(IOException.class, () -> {
                try (HttpRequestResponse response = executor.execute()) {
                    assertNull(response);
                }
            });

        } finally {
            mockWebServer.shutdown();
        }
    }

    @Test
    void testHttpRequestBuilderExecutePostServerError() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        try {
            String body = readJsonFile("servererror.json");
            mockWebServer.enqueue(new MockResponse().setBody(body).setResponseCode(500));
            mockWebServer.start();
            String endpoint = "success";
            String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
            ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(baseUrl);
            AuthenticatedHttpRequestBuilder builder = new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint);
            HttpRequestExecutor executor = builder
                .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
                .post().withBody("{test}").withMediaType(ACCEPT_HEADER_V1);
            assertThrows(IOException.class, () -> {
                try (HttpRequestResponse response = executor.execute()) {
                    assertNull(response);
                }
            });

        } finally {
            mockWebServer.shutdown();
        }
    }

    @Test
    void testHttpRequestBuilderExecutePostServerErrorHttp2() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        try {
            String body = readJsonFile("servererror.json");
            mockWebServer.enqueue(new MockResponse().setBody(body).setResponseCode(500));
            mockWebServer.start();
            String endpoint = "success";
            String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
            ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(baseUrl);
            AuthenticatedHttpRequestBuilder builder = new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint);
            HttpRequestExecutor executor = builder
                .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
                .enableHttp2().post().withBody("{test}").withMediaType(ACCEPT_HEADER_V1);
            assertThrows(IOException.class, () -> {
                try (HttpRequestResponse response = executor.execute()) {
                    assertNull(response);
                }
            });

        } finally {
            mockWebServer.shutdown();
        }
    }

    @Test
    void testHttpRequestBuilderExecuteGetNoDataFound() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        try {
            String body = readJsonFile("nodatafound.json");
            mockWebServer.enqueue(new MockResponse().setBody(body).setResponseCode(404));
            mockWebServer.start();
            String endpoint = "success";
            String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
            ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(baseUrl);
            AuthenticatedHttpRequestBuilder httpRequestBuilder = new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint);
            HttpRequestExecutor executor = httpRequestBuilder
                .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
                .get().withMediaType(ACCEPT_HEADER_V1);
            assertThrows(NoDataFoundException.class, () -> {
                try (HttpRequestResponse response = executor.execute()) {
                    assertNull(response);
                }
            });
        } finally {
            mockWebServer.shutdown();
        }
    }

    @Test
    void testHttpRequestBuilderExecuteGetNoDataFoundHttp2() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        try {
            String body = readJsonFile("nodatafound.json");
            mockWebServer.enqueue(new MockResponse().setBody(body).setResponseCode(404));
            mockWebServer.start();
            String endpoint = "success";
            String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
            ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(baseUrl);
            AuthenticatedHttpRequestBuilder httpRequestBuilder = new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint);
            HttpRequestExecutor executor = httpRequestBuilder
                .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
                .enableHttp2().get().withMediaType(ACCEPT_HEADER_V1);
            assertThrows(NoDataFoundException.class, () -> {
                try (HttpRequestResponse response = executor.execute()) {
                    assertNull(response);
                }
            });
        } finally {
            mockWebServer.shutdown();
        }
    }

    @Test
    void testHttpRequestBuilderExecutePostNoDataFound() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        try {
            String body = readJsonFile("nodatafound.json");
            mockWebServer.enqueue(new MockResponse().setBody(body).setResponseCode(404));
            mockWebServer.start();
            String endpoint = "success";
            String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
            ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(baseUrl);
            AuthenticatedHttpRequestBuilder httpRequestBuilder = new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint);
            HttpRequestExecutor executor = httpRequestBuilder
                .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
                .post().withBody("{test}").withMediaType(ACCEPT_HEADER_V1);
            assertThrows(NoDataFoundException.class, () -> {
                try (HttpRequestResponse response = executor.execute()) {
                    assertNull(response);
                }
            });
        } finally {
            mockWebServer.shutdown();
        }
    }

    @Test
    void testHttpRequestBuilderExecutePostNoDataFoundHttp2() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        try {
            String body = readJsonFile("nodatafound.json");
            mockWebServer.enqueue(new MockResponse().setBody(body).setResponseCode(404));
            mockWebServer.start();
            String endpoint = "success";
            String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
            ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(baseUrl);
            AuthenticatedHttpRequestBuilder httpRequestBuilder = new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint);
            HttpRequestExecutor executor = httpRequestBuilder
                .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
                .enableHttp2().post().withBody("{test}").withMediaType(ACCEPT_HEADER_V1);
            assertThrows(NoDataFoundException.class, () -> {
                try (HttpRequestResponse response = executor.execute()) {
                    assertNull(response);
                }
            });
        } finally {
            mockWebServer.shutdown();
        }
    }

    @Test
    void testHttpGetRequestBuilderServerNotFoundUnknownHost() throws IOException {
        String endpoint = "unknownhost";
        String baseUrl = "https://bogus-should-not-exist.rmanet.com";
        ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(baseUrl);
        AuthenticatedHttpRequestBuilder httpRequestBuilder = new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint);
        HttpRequestExecutor executor = httpRequestBuilder
            .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
            .get().withMediaType(ACCEPT_HEADER_V1);
        assertThrows(ServerNotFoundException.class, () -> {
            try (HttpRequestResponse response = executor.execute()) {
                assertNull(response);
            }
        });

        HttpRequestExecutor executor2 = httpRequestBuilder
            .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
            .enableHttp2().get().withMediaType(ACCEPT_HEADER_V1);
        assertThrows(ServerNotFoundException.class, () -> {
            try (HttpRequestResponse response = executor2.execute()) {
                assertNull(response);
            }
        });
    }

    @Test
    void testHttpPostRequestBuilderServerNotFoundUnknownHost() throws IOException {
        String endpoint = "unknownhost";
        String baseUrl = "https://bogus-should-not-exist.rmanet.com";
        ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(baseUrl);
        AuthenticatedHttpRequestBuilder httpRequestBuilder = new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint);
        HttpRequestExecutor executor = httpRequestBuilder
            .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
            .post().withBody("{test}").withMediaType(ACCEPT_HEADER_V1);
        assertThrows(ServerNotFoundException.class, () -> {
            try (HttpRequestResponse response = executor.execute()) {
                assertNull(response);
            }
        });

        HttpRequestExecutor executor2 = httpRequestBuilder
            .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
            .enableHttp2().post().withBody("{test}").withMediaType(ACCEPT_HEADER_V1);
        assertThrows(ServerNotFoundException.class, () -> {
            try (HttpRequestResponse response = executor2.execute()) {
                assertNull(response);
            }
        });
    }

    @Test
    void testHttpGetRequestBuilderServerNotFoundUnknownPort() throws IOException {
        String endpoint = "unknownport";
        String baseUrl = "https://www.hec.usace.army.mil:1000";
        ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(baseUrl);
        AuthenticatedHttpRequestBuilder httpRequestBuilder = new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint);
        HttpRequestExecutor executor = httpRequestBuilder
            .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
            .get().withMediaType(ACCEPT_HEADER_V1);
        assertThrows(ServerNotFoundException.class, () -> {
            try (HttpRequestResponse response = executor.execute()) {
                assertNull(response);
            }
        });

        HttpRequestExecutor executor2 = httpRequestBuilder
            .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
            .enableHttp2().get().withMediaType(ACCEPT_HEADER_V1);
        assertThrows(ServerNotFoundException.class, () -> {
            try (HttpRequestResponse response = executor2.execute()) {
                assertNull(response);
            }
        });
    }

    @Test
    void testHttpPostRequestBuilderServerNotFoundUnknownPort() throws IOException {
        String endpoint = "unknownport";
        String baseUrl = "https://www.hec.usace.army.mil:1000";
        ApiConnectionInfo apiConnectionInfo = new ApiConnectionInfo(baseUrl);
        AuthenticatedHttpRequestBuilder httpRequestBuilder = new AuthenticatedHttpRequestBuilder(apiConnectionInfo, endpoint);
        HttpRequestExecutor executor = httpRequestBuilder
            .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
            .post().withBody("{test}").withMediaType(ACCEPT_HEADER_V1);
        assertThrows(ServerNotFoundException.class, () -> {
            try (HttpRequestResponse response = executor.execute()) {
                assertNull(response);
            }
        });

        HttpRequestExecutor executor2 = httpRequestBuilder
            .withSslSocketFactory(getTestSslSocketFactory(), CwbiAuthTrustManager.getTrustManager())
            .enableHttp2().post().withBody("{test}").withMediaType(ACCEPT_HEADER_V1);
        assertThrows(ServerNotFoundException.class, () -> {
            try (HttpRequestResponse response = executor2.execute()) {
                assertNull(response);
            }
        });
    }

    String readJsonFile(String jsonPath) throws IOException {
        URL resource = getClass().getClassLoader().getResource(jsonPath);
        if (resource == null) {
            throw new IOException("Resource not found: " + jsonPath);
        }
        Path path = new File(resource.getFile()).toPath();
        return String.join("\n", Files.readAllLines(path));
    }

    SSLSocketFactory getTestSslSocketFactory() {
        return new SSLSocketFactory() {
            @Override
            public String[] getDefaultCipherSuites() {
                return new String[0];
            }

            @Override
            public String[] getSupportedCipherSuites() {
                return new String[0];
            }

            @Override
            public Socket createSocket(Socket socket, String s, int i, boolean b) {
                return null;
            }

            @Override
            public Socket createSocket(String s, int i) {
                return null;
            }

            @Override
            public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) {
                return null;
            }

            @Override
            public Socket createSocket(InetAddress inetAddress, int i) {
                return null;
            }

            @Override
            public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) {
                return null;
            }
        };
    }
}
