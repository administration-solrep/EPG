package fr.dila.solonepg.core.operation.livraison;

import static fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE;
import static fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER;
import static fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE;
import static fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_PDF_ORIGINAL_SIGNE;

import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFolder;
import fr.dila.solonepg.api.service.FondDeDossierModelService;
import fr.dila.solonepg.api.service.ParapheurModelService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.core.operation.STVersion;
import org.nuxeo.common.utils.Path;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

/**
 * Operation pour ajouter des nouveaux paramètres dans EPG
 *
 */
@STVersion(version = "4.0.0")
@Operation(id = EpgInitAccordActeOperation.ID, category = STConstant.PARAMETRE_DOCUMENT_TYPE)
public class EpgInitAccordActeOperation {
    public static final String ID = "SolonEpg.TypeActe.AccordCollectif.Init";

    @Context
    private OperationContext context;

    @Context
    protected CoreSession session;

    @Context
    private NuxeoPrincipal principal;

    public EpgInitAccordActeOperation() {
        this.context = null;
        this.session = null;
        this.principal = null;
    }

    @OperationMethod
    public void run() {
        if (!principal.isAdministrator()) {
            return;
        }

        // Initialisation type acte
        String typeActe = TypeActeConstants.TYPE_ACTE_ACCORD_COLLECTIF_PUBLIC;
        initFondDeDossier(typeActe);
        initParapheur(typeActe);
    }

    private void initFondDeDossier(String typeActe) {
        FondDeDossierModelService fddModelService = SolonEpgServiceLocator.getFondDeDossierModelService();

        if (fddModelService.getFondDossierModelFromTypeActe(session, typeActe) == null) {
            DocumentModel racineModelFdd = fddModelService.getFondDossierModelRoot(session);
            // creatioModel
            DocumentModel modeleFdd = fddModelService.createFondDossierModele(racineModelFdd, session, typeActe);
            fddModelService.createFondDeDossierDefaultRepository(modeleFdd, session);
            Path parentPath = modeleFdd
                .getPath()
                .append(FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER)
                .append(FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE);
            DocumentModel doc = session.createDocumentModel(
                parentPath.toString(),
                FOND_DE_DOSSIER_FOLDER_NAME_PDF_ORIGINAL_SIGNE,
                FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE
            );
            FondDeDossierFolder folder = doc.getAdapter(FondDeDossierFolder.class);
            folder.setTitle(FOND_DE_DOSSIER_FOLDER_NAME_PDF_ORIGINAL_SIGNE);
            folder.setIsDeletable(false);
            session.createDocument(doc);
        }
    }

    private void initParapheur(String typeActe) {
        ParapheurModelService paraphModelService = SolonEpgServiceLocator.getParapheurModelService();

        if (paraphModelService.getParapheurModelFromTypeActe(session, typeActe) == null) {
            DocumentModel parapheurModelRacine = paraphModelService.getParapheurModelRoot(session);
            DocumentModel modeleParaph = paraphModelService.createParapheurModele(
                parapheurModelRacine,
                session,
                typeActe
            );
            paraphModelService.createParapheurDefaultRepository(modeleParaph, session);
        }
    }
}
