package hec.army.usace.hec.cwbi.auth.http.client.token.fluentbuilders;

import java.io.IOException;
import javax.net.ssl.KeyManager;

public interface DirectGrantX509TokenRequestFluentBuilder {
    TokenRequestFluentBuilder withKeyManager(KeyManager keyManager) throws IOException;

    TokenRequestFluentBuilder withKeyManagers(KeyManager[] keyManagers) throws IOException;
}
