package hec.army.usace.hec.cwbi.auth.http.client;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public final class UrlEncodedFormData implements FormData {

    private static final String PASSWORD_PARAM = "password";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String SCOPE_PARAM = "scope";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String USERNAME_PARAM = "username";
    private static final String REFRESH_TOKEN_PARAM = "refresh_token";

    private final Map<String, String> params = new LinkedHashMap<>();

    @Override
    public FormData addPassword(String password) {
        params.put(PASSWORD_PARAM, password);
        return this;
    }

    @Override
    public FormData addGrantType(String grantType) {
        params.put(GRANT_TYPE_PARAM, grantType);
        return this;
    }

    @Override
    public FormData addScopes(String... scopes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < scopes.length; i++) {
            String scopeParam = scopes[i];
            stringBuilder.append(scopeParam);
            if (i < scopes.length - 1) {
                stringBuilder.append(" ");
            }
        }
        params.put(SCOPE_PARAM, stringBuilder.toString());
        return this;
    }

    @Override
    public FormData addClientId(String clientId) {
        params.put(CLIENT_ID_PARAM, clientId);
        return this;
    }

    @Override
    public FormData addUsername(String username) {
        params.put(USERNAME_PARAM, username);
        return this;
    }

    @Override
    public FormData addRefreshToken(String refreshToken) {
        params.put(REFRESH_TOKEN_PARAM, refreshToken);
        return this;
    }

    @Override
    public FormData addParameter(String parameterKey, String parameterValue) {
        params.put(parameterKey, parameterValue);
        return this;
    }

    /**
     * Returns URL encoded string.
     * @return String that is URL encoded.
     * @throws IOException - thrown is failed to encode parameters
     */
    public String buildEncodedString() throws IOException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString().replace("+", "%20");
    }
}
