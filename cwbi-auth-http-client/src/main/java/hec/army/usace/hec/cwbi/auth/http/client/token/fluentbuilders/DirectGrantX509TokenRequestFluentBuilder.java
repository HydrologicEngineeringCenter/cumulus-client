package hec.army.usace.hec.cwbi.auth.http.client.token.fluentbuilders;

import java.io.IOException;
import java.util.List;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLSocketFactory;

public interface DirectGrantX509TokenRequestFluentBuilder {
    TokenRequestFluentBuilder withKeyManager(KeyManager keyManager) throws IOException;

    TokenRequestFluentBuilder withKeyManagers(List<KeyManager> keyManagers) throws IOException;

    TokenRequestFluentBuilder withSSlSocketFactory(SSLSocketFactory sslSocketFactory);
}
