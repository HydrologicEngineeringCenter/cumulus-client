package hec.army.usace.hec.cwbi.auth.http.client;

import javax.net.ssl.SSLSocketFactory;

public interface DirectGrantX509TokenRequestFluentBuilder {

    TokenRequestFluentBuilder withSSlSocketFactory(SSLSocketFactory sslSocketFactory);
}
