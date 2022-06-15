package fr.dila.solonepg.operation.activitenormative;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.util.Properties;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

@Operation(
    id = AddLoiHabilitation.ID,
    category = Constants.CAT_DOCUMENT,
    label = "Add Loi Habilitation",
    description = "Add Loi Habilitation"
)
public class AddLoiHabilitation {
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.AddLoiHabilitation";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "properties")
    protected Properties properties;

    @OperationMethod
    public DocumentModel run() throws Exception {
        String numero = "";
        try {
            String numeroNor = properties.get("numeroNor");
            String titreOfficiel = properties.get("titreOfficiel");
            String observation = properties.get("observation");
            numero = properties.get("numero");

            repriseLog.debug("Ajout LoiHabilitation - numero = " + numero);
            ActiviteNormative activiteNormative = SolonEpgServiceLocator
                .getActiviteNormativeService()
                .findOrCreateActiviteNormativeByNor(session, numeroNor);
            activiteNormative.setOrdonnance38C(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE);
            session.saveDocument(activiteNormative.getDocument());
            TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);
            texteMaitre.setTitreOfficiel(titreOfficiel);
            texteMaitre.setNumero(numero);
            texteMaitre.setNumeroNor(numeroNor);
            texteMaitre.setObservation(observation);
            DocumentModel doc = session.saveDocument(texteMaitre.getDocument());
            session.save();
            repriseLog.debug("Ajout LoiHabilitation is OK  - numero = " + numero);
            return doc;
        } catch (Exception e) {
            repriseLog.error("Ajout LoiHabilitation  KO - numero = " + numero, e);
            throw e;
        }
    }
}
