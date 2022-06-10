package hec.army.usace.hec.cwbi.auth.http.client;

import java.io.IOException;
import javax.net.ssl.KeyManager;

public interface RequestKeyManagers {

    TokenRequestExecutor withKeyManager(KeyManager keyManager) throws IOException;

    TokenRequestExecutor withKeyManagers(KeyManager[] keyManagers) throws IOException;
}

