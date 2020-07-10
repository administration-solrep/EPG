package fr.dila.solonepg.core.content.template.factories;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.st.api.service.JournalService;

/**
 * 
 * Une fabrique pour les squelettes de feuilles de route.
 * 
 * @author tlombard
 */
public class FeuilleRouteSqueletteFactory extends FeuilleRouteFactory {
	@Override
	protected void journaliserCreationElement(DocumentModel eventDoc, final JournalService journalService)
			throws ClientException {
		String comment = "Création du squelette de feuille de route [" + eventDoc.getTitle() + "]";
		journalService.journaliserActionAdministration(session, SolonEpgEventConstant.CREATE_SQUELETTE_FDR_EVENT,
				comment);
	}
}
