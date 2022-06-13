package hec.army.usace.hec.cwbi.auth.http.client;

import java.io.IOException;

public class ExpiredTokenException extends IOException {

    public ExpiredTokenException() {
        super("Token is expired");
    }
}
