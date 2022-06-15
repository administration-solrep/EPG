package fr.dila.solonepg.core.parapheur;

import fr.dila.solonepg.api.parapheur.ParapheurInstance;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ParapheurInstanceImpl extends ParapheurImpl implements ParapheurInstance {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 622777111584918158L;

    public ParapheurInstanceImpl(DocumentModel doc) {
        super(doc);
    }
}
