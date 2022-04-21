import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hec.army.usace.hec.cumulus.http.client.AuthenticatedHttpRequestBuilderImpl;
import java.io.IOException;
import mil.army.usace.hec.cwms.htp.client.MockHttpServer;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestAuthenticatedHttpRequestBuilderImpl {

    static MockHttpServer mockHttpServer;

    @BeforeEach
    void setUp() throws IOException {
        mockHttpServer = MockHttpServer.create();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockHttpServer.shutdown();
    }

    ApiConnectionInfo buildConnectionInfo() {
        String baseUrl = String.format("http://localhost:%s", mockHttpServer.getPort());
        return new ApiConnectionInfo(baseUrl);
    }


    @Test
    void testAddTokenIfValid() {
        String invalidToken = "abc123";
        assertThrows(IOException.class, () -> new AuthenticatedHttpRequestBuilderImpl(buildConnectionInfo(), "endpoint", invalidToken));
    }

    @Test
    void testContstructor() throws IOException {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVXNlciIsImlzcyI6IlNpbXBsZSBTb2x1dGlvbiIsInVzZXJuYW1lIjoiVGVzdFVzZXIifQ.jQUKIOxN0KGbIGJx8SU3WfSVPNASOnRtt3DcoMVBeThcWGzEBAnwlHHYRvbzuas-sOeWSvOwrnsvpQ5tywAfWA";
        AuthenticatedHttpRequestBuilderImpl requestBuilder = new AuthenticatedHttpRequestBuilderImpl(buildConnectionInfo(), "endpoint", token);
        assertNotNull(requestBuilder);
    }
}
