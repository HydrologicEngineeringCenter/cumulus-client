import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hec.army.usace.hec.cumulus.http.client.CumulusHttpRequestBuilderImpl;
import java.io.IOException;
import mil.army.usace.hec.cwms.htp.client.MockHttpServer;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestCumulusHttpRequestBuilderImpl {

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
        OAuth2Token oAuth2Token = new OAuth2Token();
        oAuth2Token.setAccessToken(invalidToken);
        IOException exception =
            assertThrows(IOException.class, () -> new CumulusHttpRequestBuilderImpl(buildConnectionInfo(), "endpoint", oAuth2Token));
        assertEquals("Invalid JSON Web Token", exception.getMessage());
    }

    @Test
    void testExpiredToken() {
        String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2NTA5ODg1NTksImV4cCI6MTY1MDk4ODU1OSwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0.rGidzf-8vXS_vN-VQt51AFKf-WlJLrS5Q4uvLpg10hU";
        OAuth2Token oAuth2Token = new OAuth2Token();
        oAuth2Token.setAccessToken(expiredToken);
        IOException exception = assertThrows(IOException.class, () -> new CumulusHttpRequestBuilderImpl(buildConnectionInfo(), "endpoint", oAuth2Token));
        assertEquals("Token is expired", exception.getMessage());
    }

    @Test
    void testConstructor() throws IOException {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2NTA5ODg1NTksImV4cCI6MTY3MzUyMTg5NTksImF1ZCI6Ind3dy5leGFtcGxlLmNvbSIsInN1YiI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJHaXZlbk5hbWUiOiJKb2hubnkiLCJTdXJuYW1lIjoiUm9ja2V0IiwiRW1haWwiOiJqcm9ja2V0QGV4YW1wbGUuY29tIiwiUm9sZSI6WyJNYW5hZ2VyIiwiUHJvamVjdCBBZG1pbmlzdHJhdG9yIl19.PpctY-j5nshQWCRqPhP5OAq4wtdBs0OFBcgeNx16E_g";
        OAuth2Token oAuth2Token = new OAuth2Token();
        oAuth2Token.setAccessToken(token);
        CumulusHttpRequestBuilderImpl requestBuilder = new CumulusHttpRequestBuilderImpl(buildConnectionInfo(), "endpoint", oAuth2Token);
        assertNotNull(requestBuilder);
    }
}
