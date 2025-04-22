package mil.army.usace.hec.cumulus.client.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import hec.army.usace.hec.cwbi.auth.http.client.trustmanagers.CwbiAuthTrustManager;
import java.io.IOException;
import javax.net.ssl.SSLSocketFactory;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.SslSocketData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

final class TestCumulusIdentityProviderController extends TestCumulusMock {

    @Test
    void testRetrieveTokenUrl() throws IOException {
        SSLSocketFactory mockSslSocketFactory = Mockito.mock(SSLSocketFactory.class);
        String resource = "cumulus/json/idPConfig.json";
        String openIdConfig = "cumulus/json/openIdConfig.json";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = (ObjectNode) mapper.readTree(readResourceAsString(resource));
        ApiConnectionInfo webServiceUrl = buildConnectionInfo();
        node.put("well_known_endpoint", webServiceUrl.getApiRoot() + "/.well-known/openid-configuration");
        String updatedIdpConfig = mapper.writeValueAsString(node);
        enqueueMockServer(updatedIdpConfig);
        enqueueMockServer(readResourceAsString(openIdConfig));
        SslSocketData sslSocketData = new SslSocketData(mockSslSocketFactory, CwbiAuthTrustManager.getTrustManager());
        ApiConnectionInfo tokenUrl = new CumulusIdentityProviderController().retrieveTokenUrl(buildConnectionInfo(), sslSocketData);
        assertEquals("https://api.example.com/auth/realms/cwbi/protocol/openid-connect/token", tokenUrl.getApiRoot());
    }
}
