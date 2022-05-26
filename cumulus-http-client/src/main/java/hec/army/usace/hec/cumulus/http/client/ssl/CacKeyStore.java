/*
 * Copyright (c) 2021
 * United States Army Corps of Engineers - Hydrologic Engineering Center (USACE/HEC)
 * All Rights Reserved.  USACE PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval from HEC
 */

package hec.army.usace.hec.cumulus.http.client.ssl;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.DLTaggedObject;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;

final class CacKeyStore {
    private static final Logger LOGGER = Logger.getLogger(CacKeyStore.class.getName());
    public static final String FILTER_ALIAS_ISSUER = "Filter Alias. Issuer ";
    public static final String FILTER_ALIAS_SUBJECT = "Filter Alias. Subject ";
    public static final String FILTER_ALIAS_ALIAS = "Filter Alias. Alias ";
    public static final String FOUND_PRIVATE_KEY = "Found private key ";
    private static CacKeyStore instance;

    private final KeyStore keyStore;
    private final char[] ksPasswd;

    private CacKeyStore() throws KeyStoreException {
        try {
            char[] keyStorePassword = null;
            if (System.getProperty("javax.net.ssl.cwms.keyStore") != null) {
                //for CAC bypass file
                keyStorePassword = System.getProperty("javax.net.ssl.cwms.keyStorePasswd", "kEyPa$$w0rd").toCharArray();
            }
            this.ksPasswd = keyStorePassword;
            String ksType = System.getProperty("javax.net.ssl.cwms.keyStoreType", "WINDOWS-MY");
            keyStore = KeyStore.getInstance(ksType);
            loadKeyStore();
            fixAliases();
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new KeyStoreException("Unable to initialize keystore", e);
        }
    }

    static CacKeyStore getInstance() throws KeyStoreException {
        if (instance == null) {
            synchronized (CacKeyStore.class) {
                if (instance == null) {
                    instance = new CacKeyStore();
                }
            }
        }
        return instance;
    }

    private void loadKeyStore() throws IOException, NoSuchAlgorithmException, CertificateException {
        if ("WINDOWS-MY".equals(getKeyStore().getType())) {
            getKeyStore().load(null, null);
        } else {
            String ksFile = System.getProperty("javax.net.ssl.cwms.keyStore", "NONE");
            try (FileInputStream fp = new FileInputStream(ksFile)) {
                getKeyStore().load(fp, getKsPasswd());
            }
        }
    }

    private char[] getKsPasswd() {
        return ksPasswd;
    }

    private KeyStore getKeyStore() {
        return keyStore;
    }

    PrivateKey getPrivateKey(String arg0) {
        PrivateKey retval = null;
        try {
            Key k = getKeyStore().getKey(arg0, getKsPasswd());
            if (k instanceof PrivateKey) {
                retval = (PrivateKey) k;
            }
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
            LOGGER.severe("Error getting private key: " + e.getMessage());
        }
        return retval;
    }


    private void fixAliases() {
        try {
            LOGGER.fine("Fixing duplicate aliases");
            Field field = getKeyStore().getClass().getDeclaredField("keyStoreSpi");
            field.setAccessible(true);
            KeyStoreSpi keyStoreVeritable = (KeyStoreSpi) field.get(getKeyStore());

            if ("sun.security.mscapi.KeyStore$MY".equals(keyStoreVeritable.getClass().getName())) {
                LOGGER.fine("Found WINDOWS-MY Key Store");

                field = keyStoreVeritable.getClass().getEnclosingClass().getDeclaredField("entries");
                field.setAccessible(true);
                Object entryObj = field.get(keyStoreVeritable);

                Collection<Object> entries;
                entries = ((HashMap<Object, Object>) entryObj).values();

                for (Object entry : entries) {
                    field = entry.getClass().getDeclaredField("certChain");
                    field.setAccessible(true);
                    X509Certificate[] certificates = (X509Certificate[]) field.get(entry);

                    String hashCode = Integer.toString(certificates[0].hashCode());

                    field = entry.getClass().getDeclaredField("alias");
                    field.setAccessible(true);
                    String alias = (String) field.get(entry);

                    if (!alias.equals(hashCode)) {
                        LOGGER.fine("Fixing alias : " + alias + " with hash: " + hashCode);
                        field.set(entry, alias.concat(" - ").concat(hashCode));
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.warning("Error in fixing aliases: " + ex.getMessage());
        }
    }


    private static boolean isPivCertificate(Certificate cr) {

        if (!(cr instanceof X509Certificate)) {
            return false;
        }
        X509Certificate xcr = (X509Certificate) cr;
        Collection<List<?>> anc;
        try {
            anc = JcaX509ExtensionUtils.getSubjectAlternativeNames(xcr);
        } catch (CertificateParsingException e) {
            LOGGER.warning(e.getMessage());
            return false;
        }

        for (List<?> li : anc) {
            for (Object le : li) {
                LOGGER.fine(le.getClass().getName());
                if (le instanceof DLSequence) {
                    DLSequence ds = (DLSequence) le;
                    for (Enumeration<?> en = ds.getObjects(); en.hasMoreElements(); ) {
                        Object obj = en.nextElement();
                        LOGGER.fine(obj.getClass().getName());
                        if (obj instanceof ASN1ObjectIdentifier) {
                            LOGGER.fine(((ASN1ObjectIdentifier) obj).getId());
                        }
                        if (obj instanceof DLTaggedObject) {
                            DLTaggedObject dt = (DLTaggedObject) obj;
                            LOGGER.fine("Tag No: " + dt.getTagNo());
                            LOGGER.fine(dt.getObject().getClass().getName());
                            if (dt.getObject() instanceof DERUTF8String) {
                                DERUTF8String dstr = (DERUTF8String) dt.getObject();
                                LOGGER.fine(dstr.toString());
                                if (dstr.getString().matches("\\d{16}@mil")) {
                                    LOGGER.info(() -> "Found PIV Certificate: " + dstr);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean hasPrivateKey(String alias) {
        LOGGER.fine(() -> "Checking for private key " + alias);

        try {
            if (getKeyStore().getKey(alias, getKsPasswd()) instanceof PrivateKey) {
                return true;
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            LOGGER.warning("Error getting key for " + alias + ":" + e.getMessage());
        }
        LOGGER.fine(() -> "No private key for " + alias);
        return false;
    }

    private boolean hasDigitalSignature(String alias) {
        LOGGER.fine(() -> "Checking for digital signature " + alias);
        try {
            Certificate cr = getKeyStore().getCertificate(alias);

            if (cr == null) {
                LOGGER.warning(() -> "Error getting certificate for " + alias);
            } else {
                Method getKeyUsage = cr.getClass().getMethod("getKeyUsage");
                if (getKeyUsage.getReturnType().equals(boolean[].class)) {
                    boolean[] ku;
                    ku = (boolean[]) getKeyUsage.invoke(cr);
                    /*
                     * KeyUsage ::= BIT STRING { digitalSignature (0), nonRepudiation (1),
                     * keyEncipherment (2), dataEncipherment (3), keyAgreement (4),
                     * keyCertSign (5), cRLSign (6), encipherOnly (7), decipherOnly (8) }
                     */
                    if (ku != null && ku[0] /*Digital Signature*/) {
                        return true;
                    } else {
                        if (ku == null) {
                            LOGGER.fine(() -> "Key Usage returns null for " + alias);
                        } else {
                            for (int idx = 0; idx < ku.length; idx++) {
                                LOGGER.fine("Key Usage [ " + idx + " ] " + ku[idx]);
                            }
                        }
                    }
                } else {
                    LOGGER.warning(() -> "Return type not boolean[] for: " + alias);
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException | KeyStoreException e) {
            LOGGER.warning("Error getting key usage " + e.getMessage());
        }
        return false;
    }

    private static boolean isEmailEdipi(String subject, String issuer) {
        LOGGER.fine(() -> "Checking for edipi " + subject);
        String pat = "\\.(\\d{10}?)";

        Pattern r = Pattern.compile(pat);
        Matcher m = r.matcher(subject);

        if (m.find() && m.groupCount() == 1) {
            LOGGER.fine(() -> "Found edipi " + subject);
            LOGGER.fine(() -> "Checking for EMAIL string " + issuer);
            return issuer.contains("EMAIL");
        }
        return false;
    }

    private List<CertificateOption> getAllCertificates(String alias) throws KeyStoreException {
        List<CertificateOption> allAliases = new ArrayList<>();
        LOGGER.fine(() -> FILTER_ALIAS_ALIAS + alias);
        Certificate cr = getKeyStore().getCertificate(alias);
        if (cr instanceof X509Certificate) {
            X509Certificate xc = (X509Certificate) cr;
            String subject = xc.getSubjectDN().getName();
            String issuer = xc.getIssuerDN().getName();
            LOGGER.fine(() -> FILTER_ALIAS_SUBJECT + subject);
            LOGGER.fine(() -> FILTER_ALIAS_ISSUER + issuer);
            if (hasPrivateKey(alias)) {
                LOGGER.fine(() -> FOUND_PRIVATE_KEY + subject + ":" + issuer);
                allAliases.add(new CertificateOption(alias, cr));
            }
        }
        return allAliases;
    }

    private List<CertificateOption> getPivCertificates(String alias) throws KeyStoreException {
        List<CertificateOption> pivAliases = new ArrayList<>();

        LOGGER.fine(() -> FILTER_ALIAS_ALIAS + alias);
        Certificate cr = getKeyStore().getCertificate(alias);
        if (cr instanceof X509Certificate) {
            X509Certificate xc = (X509Certificate) cr;
            String subject = xc.getSubjectDN().getName();
            String issuer = xc.getIssuerDN().getName();
            LOGGER.fine(() -> FILTER_ALIAS_SUBJECT + subject);
            LOGGER.fine(() -> FILTER_ALIAS_ISSUER + issuer);
            if (hasPrivateKey(alias)) {
                LOGGER.fine(() -> FOUND_PRIVATE_KEY + subject + ":" + issuer);
                if (hasDigitalSignature(alias)) {
                    LOGGER.fine(() -> "Found digital signature " + subject + ":" + issuer);
                    if (isPivCertificate(cr)) {
                        pivAliases.add(new CertificateOption(alias, cr));
                    }
                } else {
                    LOGGER.fine(() -> "Filter Alias (not X509). Full Cert" + cr);
                }
            }
        }
        return pivAliases;
    }

    private List<CertificateOption> getEmailCertificates(String alias) throws KeyStoreException {
        List<CertificateOption> emailAliases = new ArrayList<>();

        LOGGER.fine(() -> FILTER_ALIAS_ALIAS + alias);
        Certificate cr = getKeyStore().getCertificate(alias);
        if (cr instanceof X509Certificate) {
            X509Certificate xc = (X509Certificate) cr;
            String subject = xc.getSubjectDN().getName();
            String issuer = xc.getIssuerDN().getName();
            LOGGER.fine(() -> FILTER_ALIAS_SUBJECT + subject);
            LOGGER.fine(() -> FILTER_ALIAS_ISSUER + issuer);
            if (hasPrivateKey(alias)) {
                LOGGER.fine(() -> FOUND_PRIVATE_KEY + subject + ":" + issuer);
                if (hasDigitalSignature(alias)) {
                    LOGGER.fine(() -> "Found digital signature " + subject + ":" + issuer);
                    if (issuer != null && subject != null && isEmailEdipi(subject, issuer)) {
                        LOGGER.fine(() -> "Found Email/Edipi " + subject + ":" + issuer);
                        emailAliases.add(new CertificateOption(alias, cr));
                    }
                } else {
                    LOGGER.fine(() -> "Filter Alias (not X509). Full Cert" + cr);
                }
            }
        }
        return emailAliases;
    }

    String chooseClientAlias() throws KeyStoreException {
        String retval;
        List<CertificateOption> certificateOptions = getCertificateOptions();
        if (certificateOptions.size() == 1) {
            retval = certificateOptions.get(0).getAlias();
        } else if (certificateOptions.isEmpty()) {
            throw new CacCertificateException("No certificates found for CAC authentication");
        } else {
            retval = chooseAliasFromUi(certificateOptions);
        }
        return retval;
    }

    private String chooseAliasFromUi(List<CertificateOption> certificateOptions) {
        String retval;
        JFrame parent = new JFrame("Choose a certificate");
        parent.setAlwaysOnTop(true);
        CertificateOption chooseACertificate = (CertificateOption) JOptionPane.showInputDialog(parent, "",
            "Choose a certificate", JOptionPane.PLAIN_MESSAGE, null, certificateOptions.toArray(), null);
        if (chooseACertificate != null) {
            retval = chooseACertificate.getAlias();
        } else {
            throw new CacCertificateException("CAC Authentication canceled. No certificates chosen");
        }
        return retval;
    }

    X509Certificate[] getCertificateChain(String arg0) throws KeyStoreException {
        Certificate[] certArray = getKeyStore().getCertificateChain(arg0);
        X509Certificate[] xcArray = new X509Certificate[certArray.length];
        int count = 0;
        for (Certificate cert : certArray) {
            xcArray[count] = (X509Certificate) cert;
            count++;
        }
        return xcArray;
    }

    String[] getClientAliases() throws KeyStoreException {
        Enumeration<String> aliases = getKeyStore().aliases();
        List<String> alstr = Collections.list(aliases);
        return alstr.toArray(new String[0]);
    }

    List<CertificateOption> getCertificateOptions() throws KeyStoreException {
        Enumeration<String> aliases = getKeyStore().aliases();
        List<CertificateOption> allAliases = new ArrayList<>();
        List<CertificateOption> pivAliases = new ArrayList<>();
        List<CertificateOption> emailAliases = new ArrayList<>();
        while (aliases.hasMoreElements()) {
            String element = aliases.nextElement();
            allAliases.addAll(getAllCertificates(element));
            pivAliases.addAll(getPivCertificates(element));
            emailAliases.addAll(getEmailCertificates(element));
        }
        List<CertificateOption> retval = new ArrayList<>();
        if (pivAliases.size() == 1) {
            retval.add(pivAliases.get(0));
        } else if (emailAliases.size() == 1) {
            retval.add(emailAliases.get(0));
        } else if (allAliases.size() == 1) {
            retval.add(allAliases.get(0));
        } else if (emailAliases.isEmpty()) {
            retval.addAll(allAliases);
        } else {
            retval.addAll(emailAliases);
        }
        return retval;
    }

}
