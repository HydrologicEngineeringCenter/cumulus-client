package mil.army.usace.hec.cumulus.client.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
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

    ApiConnectionInfo buildConnectionInfoWithToken(OAuth2Token oAuth2Token) {
        String baseUrl = String.format("http://localhost:%s", mockHttpServer.getPort());
        return new ApiConnectionInfo(baseUrl, oAuth2Token);
    }

    @Test
    void testInvalidJwtToken() {
        String invalidToken = "abc123::://////";
        OAuth2Token oAuth2Token = new OAuth2Token();
        oAuth2Token.setAccessToken(invalidToken);
        oAuth2Token.setTokenType("Bearer");
        oAuth2Token.setExpiresIn(3600);
        IOException exception =
            assertThrows(IOException.class, () -> new CumulusHttpRequestBuilderImpl(buildConnectionInfoWithToken(oAuth2Token), "endpoint"));
        assertEquals("Invalid JSON Web Token", exception.getMessage());
    }

    @Test
    void testExpiredJwtToken() {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        String expiredToken = JWT.create()
            .withExpiresAt(Date.from(LocalDate.parse("1776-07-04").atStartOfDay(ZoneId.of("UTC")).toInstant()))
            .withIssuer("auth0")
            .sign(algorithm);
        OAuth2Token oAuth2Token = new OAuth2Token();
        oAuth2Token.setAccessToken(expiredToken);
        oAuth2Token.setTokenType("Bearer");
        oAuth2Token.setExpiresIn(3600);
        IOException exception = assertThrows(IOException.class, () -> new CumulusHttpRequestBuilderImpl(buildConnectionInfoWithToken(oAuth2Token), "endpoint"));
        assertEquals("Token is expired", exception.getMessage());
    }
    
}
