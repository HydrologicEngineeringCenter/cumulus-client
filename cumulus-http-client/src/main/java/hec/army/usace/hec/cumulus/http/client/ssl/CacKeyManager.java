package hec.army.usace.hec.cumulus.http.client.ssl;

import hec.security.CACUtil;
import hec.security.CertificateOption;
import java.net.Socket;
import java.security.KeyStoreException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;
import javax.net.ssl.KeyManager;
import javax.net.ssl.X509KeyManager;


final class CacKeyManager implements X509KeyManager {
    private static final Logger LOGGER = Logger.getLogger(CacKeyManager.class.getName());
    private final CacKeyStore cacKeyStore;

    private CacKeyManager(CacKeyStore cacKeyStore) {
        this.cacKeyStore = cacKeyStore;
    }

    static KeyManager getKeyManager() throws KeyStoreException {
        return new CacKeyManager(CacKeyStore.getInstance());
    }

    @Override
    public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
        LOGGER.fine("Choosing client alias ");
        String retval = null;
        try {
            String alias = CACUtil.getPreferredCertificateOption().map(CertificateOption::getAlias).orElse("");
            boolean certExists = cacKeyStore.getCertificateOptions().stream().anyMatch(c -> alias.equals(c.getAlias()));
            if (certExists) {
                retval = alias;
            } else {
                retval = cacKeyStore.chooseClientAlias();
            }
        } catch (KeyStoreException e) {
            LOGGER.severe("Error reading keystore: " + e.getMessage());
        }
        return retval;
    }

    @Override
    public String chooseServerAlias(String arg0, Principal[] arg1, Socket arg2) {
        LOGGER.warning("Returning null for server alias");
        return null;
    }

    @Override
    public X509Certificate[] getCertificateChain(String arg0) {
        LOGGER.fine(() -> "Getting certificate chain for: " + arg0);
        try {
            return cacKeyStore.getCertificateChain(arg0);
        } catch (KeyStoreException e) {
            LOGGER.severe("Error getting certificate chain for " + arg0 + ":" + e.getMessage());
        }
        LOGGER.severe("Returning empty certificate chain");
        return new X509Certificate[0];
    }

    @Override
    public String[] getClientAliases(String arg0, Principal[] arg1) {
        LOGGER.fine("Getting client  aliases");
        try {
            return cacKeyStore.getClientAliases();
        } catch (KeyStoreException e) {
            LOGGER.severe("Error getting aliases:" + e.getMessage());
        }
        return new String[0];
    }

    @Override
    public PrivateKey getPrivateKey(String arg0) {
        LOGGER.fine(() -> "Getting private key for: " + arg0);
        if (arg0 != null) {
            return cacKeyStore.getPrivateKey(arg0);
        }
        LOGGER.severe(() -> "Returning null private key for " + arg0);
        return null;
    }

    @Override
    public String[] getServerAliases(String arg0, Principal[] arg1) {
        LOGGER.warning("Returning null for server aliases");
        return new String[0];
    }

}
