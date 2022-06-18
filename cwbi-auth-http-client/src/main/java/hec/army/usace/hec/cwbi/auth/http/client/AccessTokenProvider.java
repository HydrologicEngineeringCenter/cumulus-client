package hec.army.usace.hec.cwbi.auth.http.client;

import java.io.IOException;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;

interface AccessTokenProvider {

    OAuth2Token getToken() throws IOException;

    OAuth2Token refreshToken() throws IOException;
}
