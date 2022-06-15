/*
 * (C) Copyright 2006-2012 Nuxeo SA (http://nuxeo.com/) and others.
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
 *     Thierry Delprat
 */
package org.nuxeo.platform.scanimporter.tests;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.impl.EventContextImpl;
import org.nuxeo.ecm.platform.scanimporter.listener.IngestionTrigger;

public class TestImportListener extends ImportTestCase {

    @Test
    public void testTrigger() throws Exception {

        EventContext ctx = new EventContextImpl(null, null);
        ctx.setProperty("Testing", true);
        Event evt = ctx.newEvent(IngestionTrigger.START_EVENT);

        IngestionTrigger listener = new IngestionTrigger();
        listener.handleEvent(evt);

        assertNotNull(evt.getContext().getProperty("Tested"));
    }
}
