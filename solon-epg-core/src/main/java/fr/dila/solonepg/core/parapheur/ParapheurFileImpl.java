package fr.dila.solonepg.core.parapheur;

import fr.dila.solonepg.api.parapheur.ParapheurFile;
import fr.dila.solonepg.core.documentmodel.FileSolonEpgImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ParapheurFileImpl extends FileSolonEpgImpl implements ParapheurFile {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = 266174955220906350L;

    public ParapheurFileImpl(DocumentModel doc) {
        super(doc);
    }
}
