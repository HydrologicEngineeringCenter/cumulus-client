package hec.army.usace.hec.cwbi.auth.http.client;

import java.io.IOException;

public interface FormData {

    FormData addPassword(String password);

    FormData addGrantType(String grantType);

    FormData addScopes(String... scopes);

    FormData addUsername(String username);

    FormData addClientId(String clientId);

    FormData addRefreshToken(String refreshToken);

    FormData addParameter(String parameterKey, String parameterValue);

    String buildEncodedString() throws IOException;
}
