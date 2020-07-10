/*
 * (C) Copyright 2006-2007 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 *
 * $Id$
 */

package fr.dila.solonepg.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.directory.DirectoryException;
import org.nuxeo.ecm.directory.ldap.LDAPDirectoryFactory;
import org.nuxeo.runtime.model.Extension;

public class EPGLDAPDirectoryFactory extends LDAPDirectoryFactory{

    private static final Log LOGGER = LogFactory.getLog(EPGLDAPDirectoryFactory.class);
    
    /**
     * L'exception est caché pour les logs de test.
     */
    public void unregisterDirectoryExtension(Extension extension)
            throws DirectoryException {
        try{
            super.unregisterDirectoryExtension(extension);
        }
        catch (Exception e) {
            LOGGER.debug("DirectoryFactory a rencontré une erreur ",e);
        }
    }

}
