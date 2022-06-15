package fr.dila.solonepg.operation.activitenormative;

import fr.dila.solonepg.api.cases.Ordonnance;
import fr.dila.solonepg.api.dto.OrdonnanceDTO;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.core.dto.activitenormative.OrdonnanceDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.organigramme.EntiteNode;
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

@Operation(
    id = AddOrdonnance.ID,
    category = Constants.CAT_DOCUMENT,
    label = "Add Ordonnance",
    description = "Add Ordonnance"
)
public class AddOrdonnance {
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.AddOrdonnance";

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
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String numeroNor = properties.get("numeroNor");
            String titreOfficiel = properties.get("titreOfficiel");
            String ministerePilote = properties.get("ministerePilote");
            String observation = properties.get("observation");
            // String conventionDepot = properties.get("conventionDepot");
            String ratifieStr = properties.get("ratifie");
            boolean ratifie = false;
            if (("oui").equalsIgnoreCase(ratifieStr)) {
                ratifie = true;
            }
            numero = properties.get("numero");

            OrdonnanceDTO ordonnanceDTO = new OrdonnanceDTOImpl();
            ordonnanceDTO.setRatifie(ratifie);
            ordonnanceDTO.setNumeroNor(numeroNor);
            ordonnanceDTO.setNumero(numero);
            ordonnanceDTO.setTitreOfficiel(titreOfficiel);
            ordonnanceDTO.setObservation(observation);
            ordonnanceDTO.setDispositionHabilitation(false);
            // ordonnanceDTO.set
            if (ministerePilote != null && ministerePilote.length() >= 3) {
                EpgOrganigrammeService epgOrganigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
                EntiteNode ministere = epgOrganigrammeService.getMinistereFromNor(ministerePilote.substring(0, 3));
                if (ministere == null) {
                    repriseLog.warn(
                        "ministere rapporteur pour ordonnance not found (numero ordonnance " +
                        numero +
                        ") => ministere " +
                        ministerePilote
                    );
                } else {
                    ordonnanceDTO.setMinisterePilote(ministere.getId());
                }
            } else {
                repriseLog.warn(
                    "ministere rapporteur pour ordonnance not found (numero ordonnance " + numero + ") est vide"
                );
            }

            String datePublicationStr = properties.get("datePublication");
            if (datePublicationStr != null && !datePublicationStr.trim().equals("")) {
                Date datePublication = formatter.parse(datePublicationStr);
                ordonnanceDTO.setDatePublication(datePublication);
            }

            repriseLog.debug("Ajout Ordonnance - numero = " + numero);
            Ordonnance ordonnance = SolonEpgServiceLocator
                .getActiviteNormativeService()
                .createOrdonnanceReprise(ordonnanceDTO, session);
            repriseLog.debug("Ajout Ordonnance is OK  - numero = " + numero);
            return ordonnance.getDocument();
        } catch (Exception e) {
            repriseLog.error("Ajout Ordonnance  KO - numero = " + numero, e);
            throw e;
        }
    }
}
