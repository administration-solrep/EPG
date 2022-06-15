/*
 * (C) Copyright 2016 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Kevin Leturc
 */
package org.nuxeo.ecm.liveconnect.onedrive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.nuxeo.ecm.platform.oauth2.providers.OAuth2ServiceProviderRegistry;
import org.nuxeo.onedrive.client.OneDriveAPI;
import org.nuxeo.runtime.test.runner.Deploy;

/**
 * @since 8.2
 */
@Deploy("org.nuxeo.ecm.liveconnect.onedrive.core.tests:OSGI-INF/test-onedrive-business-config.xml")
public class TestOneDriveOAuth2ServiceProviderForBusiness extends OneDriveTestCase {

    @Inject
    private OAuth2ServiceProviderRegistry providerRegistry;

    private OneDriveOAuth2ServiceProvider provider;

    @Before
    public void before() {
        provider = (OneDriveOAuth2ServiceProvider) providerRegistry.getProvider(SERVICE_ID);
        assertNotNull(provider);
    }

    @Test
    public void testGetOneDriveUrlForBusiness() throws Exception {
        Optional<String> businessResource = provider.getOneDriveForBusinessResource();
        assertTrue(businessResource.isPresent());
        assertEquals("https://nuxeofr-my.sharepoint.com/", businessResource.get());
    }

    @Test
    public void testGetOneDriveAPIInitializerForBusiness() {
        OneDriveAPI api = provider.getAPIInitializer().apply("ACCESS_TOKEN");
        assertNotNull(api);
        assertTrue(api.isBusinessConnection());
        assertEquals("ACCESS_TOKEN", api.getAccessToken());
        assertEquals("https://nuxeofr-my.sharepoint.com/_api/v2.0", api.getBaseURL());
    }

}
