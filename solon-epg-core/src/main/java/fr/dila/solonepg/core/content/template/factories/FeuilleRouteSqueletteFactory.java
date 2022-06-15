package fr.dila.solonepg.core.content.template.factories;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.st.api.service.JournalService;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 *
 * Une fabrique pour les squelettes de feuilles de route.
 *
 * @author tlombard
 */
public class FeuilleRouteSqueletteFactory extends FeuilleRouteFactory {

    @Override
    protected void journaliserCreationElement(DocumentModel eventDoc, final JournalService journalService) {
        String comment = "Création du squelette de feuille de route [" + eventDoc.getTitle() + "]";
        journalService.journaliserActionAdministration(
            session,
            SolonEpgEventConstant.CREATE_SQUELETTE_FDR_EVENT,
            comment
        );
    }
}
