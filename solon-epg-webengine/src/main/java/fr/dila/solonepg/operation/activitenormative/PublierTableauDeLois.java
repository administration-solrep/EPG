package fr.dila.solonepg.operation.activitenormative;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

@Operation(
    id = PublierTableauDeLois.ID,
    category = Constants.CAT_DOCUMENT,
    label = "Publier Tableau De Lois",
    description = "Publier Tableau De Lois"
)
public class PublierTableauDeLois {
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.PublierTableauDeLois";

    @Context
    protected CoreSession session;

    @OperationMethod
    public void run() throws Exception {
        repriseLog.debug("Debut - Publier Tableau De Lois");

        //Publier liste des loi + repartition ministere
        SolonEpgServiceLocator.getActiviteNormativeService().updateLoiListePubliee(session, true);
        List<DocumentModel> documentModelList = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .getAllActiviteNormative(session, ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId(), true);
        ActiviteNormative activiteNormative;
        for (DocumentModel documentModel : documentModelList) {
            activiteNormative = documentModel.getAdapter(ActiviteNormative.class);
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .generateANRepartitionMinistereHtml(session, activiteNormative, true);
        }

        repriseLog.debug("Fin - Publier Tableau De Lois");
    }
}
