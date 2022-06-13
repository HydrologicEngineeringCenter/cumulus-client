package hec.army.usace.hec.cwbi.auth.http.client;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Date;

public class JwtTokenValidator {

    private static final String EXIPIRATION_BUFFER_KEY = "cwbi.auth.http.client.token.expiration.buffer.millis";
    private static final int DEFAULT_EXPIRATION_BUFFER_MILLIS = 1000;

    private JwtTokenValidator() {
        throw new AssertionError("Utility class");
    }

    /**
     * Validates a JWT Token String to determine if it is an un-expired valid JWT token.
     *
     * @param token - JWT String
     * @throws IOException - thrown if token is NOT Valid
     */
    public static void validateToken(String token) throws IOException {
        try {
            int bufferMillis = getBuffer();
            DecodedJWT decodedToken = JWT.decode(token);
            Date expiration = decodedToken.getExpiresAt();
            Date now = Date.from(ZonedDateTime.now().toInstant());
            long adjustedTime = now.getTime() + bufferMillis;
            Date adjustedDate = new Date(adjustedTime);
            if (expiration != null && expiration.before(adjustedDate)) {
                throw new ExpiredTokenException();
            }
        } catch (JWTDecodeException ex) {
            throw new IOException(token + " is not a JSON Web Token");
        }
    }

    private static int getBuffer() {
        String bufferMillisStr = System.getProperty(EXIPIRATION_BUFFER_KEY);
        if (bufferMillisStr == null) {
            bufferMillisStr = Integer.toString(DEFAULT_EXPIRATION_BUFFER_MILLIS);
        }
        return Integer.parseInt(bufferMillisStr);
    }
}
