package fr.dila.solonepg.api.tablereference;

import java.io.Serializable;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface pour les modes de parution
 *
 */
public interface ModeParution extends Serializable {
    /**
     * Récupération du documentModel.
     *
     * @return DocumentModel
     */
    DocumentModel getDocument();

    /**
     * Sauvegarde le document et sauvegarde la session.
     *
     * @param session CoreSession
     * @return DocumentModel
     */
    DocumentModel save(CoreSession session);

    String getId();

    String getMode();

    void setMode(String mode);

    Calendar getDateDebut();

    void setDateDebut(Calendar dateDebut);

    Calendar getDateFin();

    void setDateFin(Calendar dateFin);

    static ModeParution getAdapter(DocumentModel doc) {
        return doc.getAdapter(ModeParution.class);
    }
}
