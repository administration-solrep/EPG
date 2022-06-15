package fr.dila.solonepg.core.operation.livraison;

import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.service.ParametrageParapheurService;
import fr.dila.solonepg.api.service.vocabulary.TypeActeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.operation.STVersion;
import fr.dila.st.core.service.AbstractPersistenceDefaultComponent;
import java.util.List;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

/**
 * Operation pour nettoyer les postes qui ont plusieurs mailboxes
 *
 */
@STVersion(version = "4.0.0")
@Operation(id = SolonEpgUpdateParapheurParametreOperation.ID, category = STConstant.ORGANIGRAMME_BASE_FUNCTION_DIR)
public class SolonEpgUpdateParapheurParametreOperation extends AbstractPersistenceDefaultComponent {
    public static final String ID = "SolonEpg.Update.ParapheurParametre";

    private static final STLogger LOGGER = STLogFactory.getLog(SolonEpgUpdateParapheurParametreOperation.class);

    @Context
    protected CoreSession session;

    @Context
    private NuxeoPrincipal principal;

    public SolonEpgUpdateParapheurParametreOperation() {}

    @OperationMethod
    public void run() {
        LOGGER.info(STLogEnumImpl.LOG_INFO_TEC, "Début du traitement : updateParapheurParametre");

        TypeActeService typeActeService = SolonEpgServiceLocator.getTypeActeService();
        List<String> typesActe = typeActeService.getTypeActesSuggestion(session, "", null, null);

        for (String typeActe : typesActe) {
            ParametrageParapheurService parametrageParapheurService = SolonEpgServiceLocator.getParametrageParapheurService();
            List<ParapheurFolder> parapheurFolderList = parametrageParapheurService.getAllParapheurFolders(
                session,
                typeActe
            );
            parapheurFolderList
                .stream()
                .filter(
                    parapheurFolder ->
                        SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EPREUVES_NAME.equals(parapheurFolder.getName())
                )
                .forEach(
                    parapheurFolder -> {
                        LOGGER.info(
                            STLogEnumImpl.LOG_INFO_TEC,
                            "Suppression répertoire Epreuve pour le type acte : " + typeActe
                        );
                        session.removeDocument(parapheurFolder.getDocument().getRef());
                    }
                );
        }
        session.save();

        LOGGER.info(STLogEnumImpl.LOG_INFO_TEC, "Fin du traitement : updateParapheurParametre");
    }
}
