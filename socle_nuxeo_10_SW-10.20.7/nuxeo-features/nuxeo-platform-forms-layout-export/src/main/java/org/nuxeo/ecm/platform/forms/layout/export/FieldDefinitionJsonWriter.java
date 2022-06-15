/*
 * (C) Copyright 2018 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Kevin Leturc <kleturc@nuxeo.com>
 */
package org.nuxeo.ecm.platform.forms.layout.export;

import static org.nuxeo.ecm.core.io.registry.reflect.Instantiations.SINGLETON;
import static org.nuxeo.ecm.core.io.registry.reflect.Priorities.REFERENCE;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.io.marshallers.json.AbstractJsonWriter;
import org.nuxeo.ecm.core.io.registry.reflect.Setup;
import org.nuxeo.ecm.platform.forms.layout.api.FieldDefinition;

import com.fasterxml.jackson.core.JsonGenerator;

/**
 * @since 10.1
 */
@Setup(mode = SINGLETON, priority = REFERENCE)
public class FieldDefinitionJsonWriter extends AbstractJsonWriter<FieldDefinition> {

    @Override
    public void write(FieldDefinition entity, JsonGenerator jg) throws IOException {
        jg.writeStartObject();
        jg.writeStringField("fieldName", entity.getFieldName());
        String schemaName = entity.getSchemaName();
        if (StringUtils.isNotBlank(schemaName)) {
            jg.writeStringField("schemaName", schemaName);
        }
        jg.writeStringField("propertyName", entity.getPropertyName());
        jg.writeEndObject();
    }

}
