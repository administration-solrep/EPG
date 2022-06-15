package fr.dila.solonepg.core.event;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.event.AbstractFilterEventListener;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

/**
 * Listener des événements de chargement d'un document (dossier / modèle de feuille de route / utilisateur)
 * depuis la recherche qui permet de mettre à jour les favoris de consultation.
 *
 * @author asatre
 */
public class DocumentSearchListener extends AbstractFilterEventListener<InlineEventContext> {

    public DocumentSearchListener() {
        super(SolonEpgEventConstant.CURRENT_DOCUMENT_SEARCH_CHANGED_EVENT, InlineEventContext.class);
    }

    @Override
    protected void doHandleEvent(final Event event, final InlineEventContext context) {
        // Traite uniquement les évènements de chargement d'un document depuis la recherche

        final EspaceRechercheService.DocKind kind = (EspaceRechercheService.DocKind) context.getProperty(
            SolonEpgEventConstant.RESULTAT_CONSULTE_TYPE
        );
        final String docId = (String) context.getProperty(SolonEpgEventConstant.RESULTAT_CONSULTE_ID);
        final String path = (String) context.getProperty(SolonEpgEventConstant.RESULTAT_CONSULTE_CURRENT_PATH);

        if (kind != null && docId != null) {
            final EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
            espaceRechercheService.addDocumentModelToResultatsConsultes(context.getCoreSession(), path, docId, kind);
        }
    }
}
