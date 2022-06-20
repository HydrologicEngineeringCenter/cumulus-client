package mil.army.usace.hec.cumulus.client.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.army.usace.hec.cwms.http.client.auth.DefaultOAuth2TokenValidator;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2TokenException;

final class JwtValidator extends DefaultOAuth2TokenValidator {

    private static final Logger LOGGER = Logger.getLogger(JwtValidator.class.getName());
    private static final String EXPIRATION_BUFFER_KEY = "cumulus.http.client.token.expiration.buffer.millis";
    private static final int DEFAULT_EXPIRATION_BUFFER_MILLIS = 1000;

    @Override
    public void validateToken(OAuth2Token oauth2Token) throws OAuth2TokenException {
        super.validateToken(oauth2Token);
        try {
            int bufferMillis = getBuffer();
            DecodedJWT decodedToken = JWT.decode(oauth2Token.getAccessToken());
            Date expiration = decodedToken.getExpiresAt();
            long adjustedTime = Instant.now().toEpochMilli() + bufferMillis;
            Date adjustedDate = new Date(adjustedTime);
            if (expiration != null && expiration.before(adjustedDate)) {
                throw new OAuth2TokenException("Token is expired");
            }
        } catch (JWTDecodeException ex) {
            throw new OAuth2TokenException("Invalid JSON Web Token");
        }
    }

    private int getBuffer() {
        String bufferMillisStr = System.getProperty(EXPIRATION_BUFFER_KEY);
        if (bufferMillisStr == null) {
            bufferMillisStr = Integer.toString(DEFAULT_EXPIRATION_BUFFER_MILLIS);
            LOGGER.log(Level.CONFIG, () -> "Property " + EXPIRATION_BUFFER_KEY + " is not defined. Defaulting to " + DEFAULT_EXPIRATION_BUFFER_MILLIS);
        } else {
            String buffer = bufferMillisStr;
            LOGGER.log(Level.CONFIG, () -> "Property " + EXPIRATION_BUFFER_KEY + " is defined. Using property value of "
                + buffer + " as token-expiration buffer");
        }
        return Integer.parseInt(bufferMillisStr);
    }
}
