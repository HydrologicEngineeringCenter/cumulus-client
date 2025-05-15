/*
 * MIT License
 *
 * Copyright (c) 2025 Hydrologic Engineering Center
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package mil.army.usace.hec.cumulus.client.controllers;

import hec.army.usace.hec.cwbi.auth.http.client.OpenIdTokenController;
import java.io.IOException;
import static mil.army.usace.hec.cumulus.client.controllers.CumulusConstants.ACCEPT_HEADER_V1;
import mil.army.usace.hec.cumulus.client.model.CumulusObjectMapper;
import mil.army.usace.hec.cumulus.client.model.IdentityProviderConfiguration;
import mil.army.usace.hec.cwms.http.client.ApiConnectionInfo;
import mil.army.usace.hec.cwms.http.client.HttpRequestBuilderImpl;
import mil.army.usace.hec.cwms.http.client.HttpRequestResponse;
import mil.army.usace.hec.cwms.http.client.request.HttpRequestExecutor;

public final class CumulusIdentityProviderController extends OpenIdTokenController {

    private static final String IDENTITY_PROVIDER_ENDPOINT = "identity-provider";
    private static final String CONFIG_ENDPOINT = "configuration";

    @Override
    protected String retrieveWellKnownEndpoint(ApiConnectionInfo apiConnectionInfo) throws IOException {
        IdentityProviderConfiguration configuration = retrieveConfiguration(apiConnectionInfo);
        return configuration.getWellKnownEndpoint();
    }

    /**
     * Retrieve Identity Provider configuration.
     *
     * @param apiConnectionInfo - connection info
     * @return Identity Provider configuration
     */
    private IdentityProviderConfiguration retrieveConfiguration(ApiConnectionInfo apiConnectionInfo) throws IOException {
        HttpRequestExecutor executor = new HttpRequestBuilderImpl(apiConnectionInfo, IDENTITY_PROVIDER_ENDPOINT + "/" + CONFIG_ENDPOINT)
                .get()
                .withMediaType(ACCEPT_HEADER_V1);
        try (HttpRequestResponse response = executor.execute()) {
            return CumulusObjectMapper.mapJsonToObject(response.getBody(), IdentityProviderConfiguration.class);
        }
    }
}
