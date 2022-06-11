package hec.army.usace.hec.cwbi.auth.http.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class TestUrlEncodedFormData {

    @Test
    void testEncodedFormDataDirectGrantX509() throws IOException {
        String encodedFormData = new UrlEncodedFormData()
            .addPassword("")
            .addGrantType("password")
            .addScopes("openid", "profile")
            .addClientId("cumulus")
            .addUsername("")
            .buildEncodedString();
        String expected = "password=&grant_type=password&scope=openid%20profile&client_id=cumulus&username=";
        assertEquals(expected, encodedFormData);
    }

    @Test
    void testEncodedFormDataRefreshToken() throws IOException {
        String encodedFormData = new UrlEncodedFormData()
            .addRefreshToken("abc123")
            .addGrantType("refresh_token")
            .addClientId("cumulus")
            .buildEncodedString();
        String expected = "refresh_token=abc123&grant_type=refresh_token&client_id=cumulus";
        assertEquals(expected, encodedFormData);
    }

    @Test
    void testEncodedFormDataParameters() throws IOException {
        String encodedFormData = new UrlEncodedFormData()
            .addParameter("Hello", "World")
            .addParameter("GreenEggs", "AndHam")
            .addParameter("spaceTest", "some spaces here")
            .buildEncodedString();
        String expected = "Hello=World&GreenEggs=AndHam&spaceTest=some%20spaces%20here";
        assertEquals(expected, encodedFormData);
    }
}
