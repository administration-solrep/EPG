/**
 *
 * Surcharge le beam seam SmartNXQLQueryActions du package smartSearch de Nuxeo,
 * afin d'apporter d'avantages de fonctionnalité.
 * Correction de problème d'échappement dans la requête et traduction d'une requête en language
 * utilisateur.
 *
 */
package fr.dila.solonepg.ui.services;

import fr.dila.st.ui.services.actions.STSmartNXQLQueryActionService;
import org.nuxeo.ecm.core.api.CoreSession;

public interface EPGSmartNXQLQueryUIService extends STSmartNXQLQueryActionService {
    String getFullQuery(CoreSession session, String queryPart);
}
