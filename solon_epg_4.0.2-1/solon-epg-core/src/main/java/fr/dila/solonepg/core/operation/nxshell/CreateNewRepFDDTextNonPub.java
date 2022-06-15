package fr.dila.solonepg.core.operation.nxshell;

import static fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE;
import static fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER;
import static fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE;
import static fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_PDF_ORIGINAL_SIGNE;
import static fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_TEXT_NON_PUB_FOLDER;
import static fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_ROOT_FOLDER;

import fr.dila.solonepg.api.fonddossier.FondDeDossierFolder;
import fr.dila.st.core.operation.STVersion;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;

/**
 * Opération pour ajouter un nouveau répertoire par defaut au fond de dossier des textes non publiés.
 *
 */
@Operation(id = CreateNewRepFDDTextNonPub.ID, category = FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)
@STVersion(version = "4.0.0")
public class CreateNewRepFDDTextNonPub {
    public static final String ID = "FDD.TextNonPub.Folder.Creation";

    private static final String PATH_REP_DOC_JOINTE_TEXT_NON_PUB =
        FOND_DE_DOSSIER_ROOT_FOLDER +
        "/" +
        FOND_DE_DOSSIER_MODEL_TEXT_NON_PUB_FOLDER +
        "/" +
        FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER +
        "/" +
        FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE;

    @Context
    private OperationContext context;

    @OperationMethod
    public void createFolder() {
        CoreSession session = context.getCoreSession();
        DocumentModel doc;
        if (
            !session.exists(
                new PathRef(PATH_REP_DOC_JOINTE_TEXT_NON_PUB + "/" + FOND_DE_DOSSIER_FOLDER_NAME_PDF_ORIGINAL_SIGNE)
            )
        ) {
            doc =
                session.createDocumentModel(
                    PATH_REP_DOC_JOINTE_TEXT_NON_PUB,
                    FOND_DE_DOSSIER_FOLDER_NAME_PDF_ORIGINAL_SIGNE,
                    FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE
                );
            FondDeDossierFolder folder = doc.getAdapter(FondDeDossierFolder.class);
            folder.setTitle(FOND_DE_DOSSIER_FOLDER_NAME_PDF_ORIGINAL_SIGNE);
            folder.setIsDeletable(false);
            session.createDocument(doc);
        }
    }

    public CreateNewRepFDDTextNonPub() {}
}
