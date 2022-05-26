package mil.army.usace.hec.cumulus.client.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import mil.army.usace.hec.cwms.http.client.model.OAuth2Token;
import org.junit.jupiter.api.Test;

public class TestCumulusTokenController {

    @Test
    void testRetrieveToken() throws IOException {
        OAuth2Token token = new CumulusTokenController().retrieveToken();
        assertNotNull(token);
    }
}
