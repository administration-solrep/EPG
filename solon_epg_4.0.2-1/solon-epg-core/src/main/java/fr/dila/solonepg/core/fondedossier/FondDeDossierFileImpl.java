package fr.dila.solonepg.core.fondedossier;

import fr.dila.solonepg.api.fonddossier.FondDeDossierFile;
import fr.dila.solonepg.core.documentmodel.FileSolonEpgImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

public class FondDeDossierFileImpl extends FileSolonEpgImpl implements FondDeDossierFile {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -5741339659707266310L;

    public FondDeDossierFileImpl(DocumentModel doc) {
        super(doc);
    }
}
