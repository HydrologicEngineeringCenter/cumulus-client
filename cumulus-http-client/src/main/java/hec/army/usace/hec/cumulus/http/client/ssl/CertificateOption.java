package hec.army.usace.hec.cumulus.http.client.ssl;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class CertificateOption {
    private final Certificate cert;
    private final String alias;

    CertificateOption(String alias, Certificate cert) {
        this.cert = cert;
        this.alias = alias;
    }

    private static String getCN(String dn) {
        String pat = "CN=(.*?),";

        Pattern r = Pattern.compile(pat);
        Matcher m = r.matcher(dn);

        if (m.find() && m.groupCount() == 1) {
            return m.group(1);
        }
        return null;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public String toString() {
        if (cert instanceof X509Certificate) {
            X509Certificate xc = (X509Certificate) cert;
            String subject = getCN(xc.getSubjectX500Principal().getName());
            String issuer = getCN(xc.getIssuerX500Principal().getName());

            if (subject != null && issuer != null) {
                return "Subject: " + subject + " Issuer: " + issuer;
            }
        }
        return alias;
    }

}

