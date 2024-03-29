/*
 * (C) Copyright 2010 Nuxeo SA (http://nuxeo.com/) and contributors.
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
 *     Alexandre Russel
 */
package fr.dila.cm.caselink;

import fr.dila.st.api.caselink.STDossierLink;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.actionable.ActionableObject;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * @author <a href="mailto:arussel@nuxeo.com">Alexandre Russel</a>
 *
 */
public interface ActionableCaseLink extends STDossierLink, ActionableObject {
    void validate(CoreSession session);

    void refuse(CoreSession session);

    void setRefuseOperationChainId(String refuseChainId);

    void setValidateOperationChainId(String validateChainId);

    void setStepId(String id);

    boolean isTodo();

    boolean isDone();

    void setDone(CoreSession session);

    String getStepId();
}
