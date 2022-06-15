package fr.dila.solonepg.core.fondedossier;

import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFolder;
import fr.dila.ss.core.fondDeDossier.SSFondDeDossierFolderImpl;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implémentation de l'interface FondDeDossierFolder.
 *
 * @author ARN
 */
public class FondDeDossierFolderImpl extends SSFondDeDossierFolderImpl implements FondDeDossierFolder {
    private static final long serialVersionUID = -3972698901332258706L;

    /**
     * @param document
     */
    public FondDeDossierFolderImpl(DocumentModel document) {
        super(document);
    }

    @Override
    public Boolean isDeletable() {
        return getBooleanProperty(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_SCHEMA,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_IS_SUPPRIMABLE_PROPERTY
        );
    }

    @Override
    public void setIsDeletable(Boolean estSupprimable) {
        setProperty(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_SCHEMA,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_IS_SUPPRIMABLE_PROPERTY,
            estSupprimable
        );
    }

    @Override
    public String isDeletableStr() {
        return getBooleanProperty(
                SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_SCHEMA,
                SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_IS_SUPPRIMABLE_PROPERTY
            )
            .toString();
    }
}
