package fr.dila.solonepg.core.event;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.event.RollbackEventListener;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.event.CoreEventConstants;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

/**
 * Listener pour la gestion des droits sur la modification du ministère/direction de rattachement
 */
public class DossierRattachementModifiedListener extends RollbackEventListener {

    @Override
    public void handleDocumentEvent(Event event, DocumentEventContext ctx) {
        final DocumentModel dossierDoc = ctx.getSourceDocument();

        String docType = dossierDoc.getType();
        if (!DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE.equals(docType)) {
            return;
        }

        DocumentModel oldDossierDoc = (DocumentModel) ctx.getProperty(CoreEventConstants.PREVIOUS_DOCUMENT_MODEL);
        Dossier oldDossier = oldDossierDoc.getAdapter(Dossier.class);
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        if (
            !StringUtils.equals(oldDossier.getDirectionAttache(), dossier.getDirectionAttache()) ||
            !StringUtils.equals(oldDossier.getMinistereAttache(), dossier.getMinistereAttache())
        ) {
            DossierService dossierService = SolonEpgServiceLocator.getDossierService();
            // on met à jour les droits
            dossierService.initDossierCreationAclUnrestricted(ctx.getCoreSession(), Collections.singletonList(dossier));
        }
    }
}
