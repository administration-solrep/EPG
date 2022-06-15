package fr.dila.solonmgpp.core.event;

import static fr.dila.solonmgpp.core.service.SolonMgppServiceLocator.getModeleCourrierService;
import static java.util.Optional.ofNullable;
import static org.nuxeo.ecm.core.api.event.CoreEventConstants.PREVIOUS_DOCUMENT_MODEL;

import fr.dila.solonmgpp.api.constant.MgppDocTypeConstants;
import fr.dila.solonmgpp.api.domain.ModeleCourrier;
import fr.dila.st.core.exception.STValidationException;
import fr.sword.naiad.nuxeo.commons.core.listener.AbstractDocumentEventListener;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

public class ModeleCourrierListener extends AbstractDocumentEventListener {

    @Override
    public void handleDocumentEvent(Event event, DocumentEventContext context) {
        DocumentModel doc = context.getSourceDocument();
        if (!accept(doc)) {
            return;
        }

        ModeleCourrier newModele = doc.getAdapter(ModeleCourrier.class);
        DocumentModel oldDoc = (DocumentModel) context.getProperty(PREVIOUS_DOCUMENT_MODEL);
        ModeleCourrier oldModele = ofNullable(oldDoc).map(d -> d.getAdapter(ModeleCourrier.class)).orElse(null);
        try {
            getModeleCourrierService().validateModeleCourrier(context.getCoreSession(), newModele, oldModele);
        } catch (STValidationException e) {
            event.markBubbleException();
            throw e;
        }
    }

    private boolean accept(DocumentModel doc) {
        return MgppDocTypeConstants.MODELE_COURRIER_TYPE.equals(doc.getType());
    }
}
