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

package fr.dila.solonmgpp.core;

import org.nuxeo.ecm.directory.DirectoryException;
import org.nuxeo.ecm.directory.ldap.LDAPDirectoryFactory;
import org.nuxeo.runtime.model.Extension;

import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;

public class MGPPLDAPDirectoryFactory extends LDAPDirectoryFactory {

	private static final STLogger	LOG	= STLogFactory.getLog(MGPPLDAPDirectoryFactory.class);

	// protected CoreSession session ;
	/**
	 * L'exception est caché pour les logs de test.
	 */
	@Override
	public void unregisterDirectoryExtension(Extension extension) throws DirectoryException {
		try {
			super.unregisterDirectoryExtension(extension);
		} catch (Exception e) {
			LOG.debug(null, STLogEnumImpl.FAIL_UNREGISTER_LDAP_DIRECTORY_TEC, e);
		}
	}

}
