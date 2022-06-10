package hec.army.usace.hec.cwbi.auth.http.client;

import java.io.IOException;
import mil.army.usace.hec.cwms.http.client.model.OAuth2Token;

public interface TokenRequestExecutor {
    OAuth2Token fetchToken() throws IOException;
}
