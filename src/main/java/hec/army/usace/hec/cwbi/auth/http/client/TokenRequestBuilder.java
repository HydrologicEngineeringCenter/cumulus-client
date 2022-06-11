package hec.army.usace.hec.cwbi.auth.http.client;

import hec.army.usace.hec.cwbi.auth.http.client.token.fluentbuilders.RequestClientId;
import hec.army.usace.hec.cwbi.auth.http.client.token.fluentbuilders.TokenRequestExecutor;
import hec.army.usace.hec.cwbi.auth.http.client.token.fluentbuilders.TokenRequestFluentBuilder;
import java.io.IOException;
import java.util.Objects;
import mil.army.usace.hec.cwms.http.client.auth.OAuth2Token;

abstract class TokenRequestBuilder implements TokenRequestFluentBuilder {

    static final String CUMULUS_CLIENT_ID = "cumulus";
    static final String MEDIA_TYPE = "application/x-www-form-urlencoded";
    private String url;
    private String clientId;

    abstract OAuth2Token retrieveToken() throws IOException;

    String getUrl() {
        return url;
    }

    String getClientId() {
        return clientId;
    }

    @Override
    public RequestClientId withUrl(String url) {
        this.url = Objects.requireNonNull(url, "Missing required URL");
        return new RequestClientIdImpl();
    }

    private class RequestClientIdImpl implements RequestClientId {

        @Override
        public TokenRequestExecutor withClientId(String clientIdString) {
            clientId = Objects.requireNonNull(clientIdString, "Missing required Client ID");
            return new TokenRequestExecutorImpl();
        }
    }

    private class TokenRequestExecutorImpl implements TokenRequestExecutor {

        @Override
        public OAuth2Token fetchToken() throws IOException {
            return retrieveToken();
        }
    }
}
