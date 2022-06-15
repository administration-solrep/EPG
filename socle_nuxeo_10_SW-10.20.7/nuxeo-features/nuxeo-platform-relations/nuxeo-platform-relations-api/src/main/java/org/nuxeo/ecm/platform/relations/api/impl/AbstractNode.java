/*
 * (C) Copyright 2006-2007 Nuxeo SA (http://nuxeo.com/) and others.
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
 *     Nuxeo - initial API and implementation
 *
 * $Id: AbstractNode.java 20645 2007-06-17 13:16:54Z sfermigier $
 */

package org.nuxeo.ecm.platform.relations.api.impl;

import org.nuxeo.ecm.platform.relations.api.Node;

/**
 * Abstract class for nodes.
 * <p>
 * Nodes can be resources, blank nodes or literals.
 *
 * @author <a href="mailto:at@nuxeo.com">Anahide Tchertchian</a>
 */
public abstract class AbstractNode implements Node {

    private static final long serialVersionUID = 1L;

    public boolean isLiteral() {
        return false;
    }

    public boolean isBlank() {
        return false;
    }

    public boolean isResource() {
        return false;
    }

    public boolean isQNameResource() {
        return false;
    }

    public int compareTo(Node o) {
        // dummy override, just used to compare statements lists
        return toString().compareTo(o.toString());
    }

}
