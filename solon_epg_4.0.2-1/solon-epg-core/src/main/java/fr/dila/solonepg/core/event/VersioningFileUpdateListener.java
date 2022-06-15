package fr.dila.solonepg.core.event;

import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.st.core.event.AbstractFilterEventListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.VersioningOption;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.api.versioning.VersioningService;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

/**
 * Gestionnaire d'évènements qui permet de signaler que l'on doit enregistrer une version du document avant de le sauvegarder.
 *
 * @author arolin
 */
public class VersioningFileUpdateListener extends AbstractFilterEventListener<DocumentEventContext> {
    private static final Log log = LogFactory.getLog(VersioningFileUpdateListener.class);

    public VersioningFileUpdateListener() {
        super(DocumentEventTypes.BEFORE_DOC_UPDATE, DocumentEventContext.class);
    }

    @Override
    protected void doHandleEvent(final Event event, final DocumentEventContext context) {
        // Traite uniquement les évènements de document sur le point d'être sauvegadé

        // Traite uniquement les fichiers de type ParapheruFichier et FondDeDossierFichier
        DocumentModel doc = context.getSourceDocument();
        String docType = doc.getType();
        if (
            docType == null ||
            (
                !SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE.equals(docType) &&
                !SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE.equals(docType)
            )
        ) {
            return;
        }
        log.debug("versioning automatique du fichier");

        if (doc.getContextData(VersioningService.VERSIONING_OPTION) != VersioningOption.NONE) {
            //incrementation du numero de version majeur
            doc.putContextData(VersioningService.VERSIONING_OPTION, VersioningOption.MAJOR);
        }
    }
}
