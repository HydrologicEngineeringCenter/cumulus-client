package hec.army.usace.hec.cwbi.auth.http.client;

import java.io.IOException;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AccessTokenAuthenticator implements Authenticator {

    static final String AUTHORIZATION_HEADER = "Authorization";
    private final AccessTokenProvider tokenProvider;

    protected AccessTokenAuthenticator() throws IOException {
        this.tokenProvider = getAccessTokenProvider();
    }

    public abstract AccessTokenProvider getAccessTokenProvider() throws IOException;

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {
        OAuth2Token token = tokenProvider.getToken();
        if (token != null) {
            synchronized (this) {
                OAuth2Token newToken = tokenProvider.getToken();
                // Check if the request made was previously made as an authenticated request.
                if (response.request().header(AUTHORIZATION_HEADER) != null) {
                    // If the token has changed since the request was made, use the new token.
                    String newAccessToken = newToken.getAccessToken();
                    if (!newAccessToken.equals(tokenProvider.getToken().getAccessToken())) {
                        return response.request()
                            .newBuilder()
                            .removeHeader(AUTHORIZATION_HEADER)
                            .addHeader(AUTHORIZATION_HEADER, "Bearer " + newAccessToken)
                            .build();
                    }
                    OAuth2Token updatedToken = tokenProvider.refreshToken();

                    // Retry the request with the new token.
                    return response.request()
                        .newBuilder()
                        .removeHeader(AUTHORIZATION_HEADER)
                        .addHeader(AUTHORIZATION_HEADER, "Bearer " + updatedToken.getAccessToken())
                        .build();
                }
            }
        }
        return null;
    }
}
