/*
 * (C) Copyright 2010 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Nuxeo - initial API and implementation
 */

package fr.dila.cm.core.service;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject("distributionType")
public class CaseManagementDistributionTypeDescriptor {

    @XNode("name")
    protected String name;

    @XNode("allRecipientsProperty")
    protected String allRecipientsProperty;

    @XNode("externalRecipientsProperty")
    protected String externalRecipientsProperty;

    @XNode("internalRecipientsProperty")
    protected String internalRecipientsProperty;

}
