package fr.dila.solonepg.operation.activitenormative;

import fr.dila.solonepg.api.cases.Ordonnance;
import fr.dila.solonepg.api.dto.OrdonnanceHabilitationDTO;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.core.dto.activitenormative.OrdonnanceHabilitationDTOImpl;
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
    id = AddOrdonnanceHabilitation.ID,
    category = Constants.CAT_DOCUMENT,
    label = "Add Ordonnance Habilitation",
    description = "Add Ordonnance Habilitation"
)
public class AddOrdonnanceHabilitation {
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.AddOrdonnanceHabilitation";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "properties")
    protected Properties properties;

    @OperationMethod
    public DocumentModel run() throws Exception {
        String article = "";
        DocumentModel result = null;
        String numeroNor = "";
        try {
            repriseLog.debug("enter Ajout OrdonnanceHabilitation  - article = " + article);
            String loiHabilitationId = properties.get("loiHabilitationId");
            article = properties.get("article");
            repriseLog.debug("Ajout Habilitation - numero = " + article);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            numeroNor = properties.get("numeroNor");
            String titreOfficiel = properties.get("titreOfficiel");
            String ministerePilote = properties.get("ministerePilote");
            String observation = properties.get("observation");
            String conventionDepot = properties.get("conventionDepot");
            String numero = properties.get("numero");
            String ratifieStr = properties.get("ratifie");
            boolean ratifie = false;
            if (("oui").equalsIgnoreCase(ratifieStr)) {
                ratifie = true;
            }

            OrdonnanceHabilitationDTO ordonnanceHabilitationDTO = new OrdonnanceHabilitationDTOImpl(numeroNor);
            ordonnanceHabilitationDTO.setNumero(numero);
            ordonnanceHabilitationDTO.setTitreOfficiel(titreOfficiel);
            ordonnanceHabilitationDTO.setConventionDepot(conventionDepot);
            ordonnanceHabilitationDTO.setObservation(observation);

            // ordonnanceHabilitationDTO.setDispositionHabilitation(true);
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
                    ordonnanceHabilitationDTO.setMinisterePilote(ministere.getId());
                }
            } else {
                repriseLog.warn(
                    "ministere rapporteur pour ordonnance not found (numero ordonnance " + numero + ") est vide"
                );
            }

            String datePublicationStr = properties.get("datePublication");
            if (datePublicationStr != null && !datePublicationStr.trim().equals("")) {
                Date datePublication = formatter.parse(datePublicationStr);
                ordonnanceHabilitationDTO.setDatePublication(datePublication);
            }

            Ordonnance ordonnace = SolonEpgServiceLocator
                .getActiviteNormativeService()
                .saveOrdonnanceHabilitationReprise(
                    ordonnanceHabilitationDTO,
                    article,
                    loiHabilitationId,
                    session,
                    ratifie
                );

            session.save();
            if (ordonnace != null) {
                result = ordonnace.getDocument();
                repriseLog.debug(
                    "Ajout OrdonnanceHabilitation is OK  -> article = " + article + " - numeroNor " + numeroNor
                );
            } else {
                repriseLog.debug(
                    "Ajout OrdonnanceHabilitation is KO  -> article = " + article + " - numeroNor " + numeroNor
                );
            }

            return result;
        } catch (Exception e) {
            repriseLog.debug(
                "Ajout OrdonnanceHabilitation is KO  -> article = " + article + " - numeroNor " + numeroNor
            );
            throw e;
        }
    }
}
