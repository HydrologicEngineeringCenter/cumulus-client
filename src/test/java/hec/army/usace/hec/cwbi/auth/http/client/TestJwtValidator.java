package hec.army.usace.hec.cwbi.auth.http.client;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2TokenException;
import org.junit.jupiter.api.Test;

class TestJwtValidator {

    @Test
    void testInvalidJwtString() {
        String invalidJwtString = "..:::::////";
        assertThrows(OAuth2TokenException.class, () -> new JwtValidator().validateJwtString(invalidJwtString));
    }

    @Test
    void testExpiredJwtString() {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        String expiredToken = JWT.create()
            .withExpiresAt(Date.from(LocalDate.parse("1776-07-04").atStartOfDay(ZoneId.of("UTC")).toInstant()))
            .withIssuer("auth0")
            .sign(algorithm);
        assertThrows(OAuth2TokenException.class, () -> new JwtValidator().validateJwtString(expiredToken));
    }
}
