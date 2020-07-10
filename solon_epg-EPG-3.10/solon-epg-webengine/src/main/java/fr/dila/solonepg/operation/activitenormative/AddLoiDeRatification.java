package fr.dila.solonepg.operation.activitenormative;

import java.text.SimpleDateFormat;
import java.util.Date;

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

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.dto.LoiDeRatificationDTO;
import fr.dila.solonepg.core.dto.activitenormative.LoiDeRatificationDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

@Operation(id = AddLoiDeRatification.ID, category = Constants.CAT_DOCUMENT, label = "Add Loi De Ratification", description = "Add Loi De Ratification")
public class AddLoiDeRatification {

    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.AddLoiDeRatification";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "properties")
    protected Properties properties;

    @OperationMethod
    public void run(DocumentModel doc) throws Exception {

        String numeroNor = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            numeroNor = properties.get("numeroNor");
            String titreOfficiel = properties.get("titreOfficiel");
            String titreActe = properties.get("titreActe");
            String numeroDepot = properties.get("numeroDepot");
            String chambreDepot = properties.get("chambreDepot");
            repriseLog.debug("------------------Add Loi De Ratification - numeroNor" + numeroNor);

            ActiviteNormative activiteNormative = doc.getAdapter(ActiviteNormative.class);

            LoiDeRatificationDTO loiDeRatification = new LoiDeRatificationDTOImpl();
            loiDeRatification.setNumeroNor(numeroNor);
            loiDeRatification.setNumeroDepot(numeroDepot);
            loiDeRatification.setChambreDepot(chambreDepot);
            loiDeRatification.setTitreActe(titreActe);
            loiDeRatification.setTitreOfficiel(titreOfficiel);

            String dateDepotStr = properties.get("dateDepot");
            if (dateDepotStr != null && !dateDepotStr.trim().equals("")) {
                Date dateDepot = formatter.parse(dateDepotStr);
                loiDeRatification.setDateDepot(dateDepot);
            }
            String dateLimiteDepotStr = properties.get("dateLimiteDepot");
            if (dateLimiteDepotStr != null && !dateLimiteDepotStr.trim().equals("")) {
                Date dateLimiteDepot = formatter.parse(dateLimiteDepotStr);
                loiDeRatification.setDateLimiteDepot(dateLimiteDepot);
            }
            String datePublicationStr = properties.get("datePublication");
            if (datePublicationStr != null && !datePublicationStr.trim().equals("")) {
                Date datePublication = formatter.parse(datePublicationStr);
                loiDeRatification.setDatePublication(datePublication);
            }

            SolonEpgServiceLocator.getActiviteNormativeService().saveProjetsLoiDeRatificationReprise(loiDeRatification, session, activiteNormative);

            repriseLog.debug("Add Loi De Ratification is OK - numeroNor" + numeroNor);
        } catch (Exception e) {
            repriseLog.error("Add Loi De Ratification is KO - numeroNor= " + numeroNor, e);
            throw e;
        }
    }
}
