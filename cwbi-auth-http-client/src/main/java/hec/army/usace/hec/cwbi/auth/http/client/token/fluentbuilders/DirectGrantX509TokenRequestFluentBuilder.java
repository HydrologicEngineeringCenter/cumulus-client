package hec.army.usace.hec.cwbi.auth.http.client.token.fluentbuilders;

import javax.net.ssl.SSLSocketFactory;

public interface DirectGrantX509TokenRequestFluentBuilder {

    TokenRequestFluentBuilder withSSlSocketFactory(SSLSocketFactory sslSocketFactory);
}
