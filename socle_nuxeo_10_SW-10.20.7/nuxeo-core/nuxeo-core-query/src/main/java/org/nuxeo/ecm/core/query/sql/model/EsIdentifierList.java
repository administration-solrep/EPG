/*
 * (C) Copyright 2015 Nuxeo SA (http://nuxeo.com/) and others.
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
 *     Benoit Delbosc
 */

package org.nuxeo.ecm.core.query.sql.model;

import java.util.ArrayList;

public class EsIdentifierList extends ArrayList<String> implements Operand {

    private static final long serialVersionUID = 4590324482296853715L;

    public EsIdentifierList() {
        super();
    }

    public EsIdentifierList(String identifiers) {
        for (String index: identifiers.split(",")) {
            this.add(index);
        }
    }

    @Override
    public void accept(IVisitor visitor) {
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        if (isEmpty()) {
            return "";
        }
        buf.append(get(0).toString());
        for (int i = 1, size = size(); i < size; i++) {
            buf.append(",").append(get(i).toString());
        }
        return buf.toString();
    }

}
