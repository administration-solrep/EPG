package fr.dila.solonepg.operation.activitenormative;

import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.dto.MesureApplicativeDTO;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.core.dto.activitenormative.MesureApplicativeDTOImpl;
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
    id = AddMesureToLoi.ID,
    category = Constants.CAT_DOCUMENT,
    label = "Add mesure to loi",
    description = "Add mesure to loi"
)
public class AddMesureToLoi {
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.AddMesureToLoi";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "texteMaitreLoiId", required = true, order = 1)
    protected String texteMaitreLoiId;

    @Param(name = "properties")
    protected Properties properties;

    @OperationMethod
    public DocumentModel run() throws Exception {
        String numeroOrdre = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            numeroOrdre = properties.get("numeroOrdre");
            String article = properties.get("article");
            String codeModifie = properties.get("codeModifie");
            String baseLegale = properties.get("baseLegale");
            String objetRIM = properties.get("objetRIM");
            String typeMesure = properties.get("typeMesure");
            String consultationsHCE = properties.get("consultationsHCE");
            String calendrierConsultationsHCE = properties.get("calendrierConsultationsHCE");
            String observation = properties.get("observation");
            String ministerePilote = properties.get("ministerePilote");
            String directionResponsable = properties.get("directionResponsable");
            String applicationRecu = properties.get("applicationRecu");

            repriseLog.debug("Add Mesure To Loi - numeroOrdre = " + numeroOrdre);

            MesureApplicativeDTO mesureApplicativeDTO = new MesureApplicativeDTOImpl();
            mesureApplicativeDTO.setNumeroOrdre(numeroOrdre);
            mesureApplicativeDTO.setArticle(article);
            mesureApplicativeDTO.setCodeModifie(codeModifie);
            mesureApplicativeDTO.setBaseLegale(baseLegale);
            mesureApplicativeDTO.setObjetRIM(objetRIM);
            mesureApplicativeDTO.setTypeMesure(typeMesure);
            mesureApplicativeDTO.setConsultationsHCE(consultationsHCE);
            mesureApplicativeDTO.setCalendrierConsultationsHCE(calendrierConsultationsHCE);
            String datePrevisionnelleSaisineCEStr = properties.get("datePrevisionnelleSaisineCE");
            if (datePrevisionnelleSaisineCEStr != null && !datePrevisionnelleSaisineCEStr.trim().equals("")) {
                Date datePrevisionnelleSaisineCE = formatter.parse(datePrevisionnelleSaisineCEStr);
                mesureApplicativeDTO.setDatePrevisionnelleSaisineCE(datePrevisionnelleSaisineCE);
            }
            String dateObjectifPublicationStr = properties.get("dateObjectifPublication");
            if (dateObjectifPublicationStr != null && !dateObjectifPublicationStr.trim().equals("")) {
                Date dateObjectifPublication = formatter.parse(dateObjectifPublicationStr);
                mesureApplicativeDTO.setDateObjectifPublication(dateObjectifPublication);
            }
            mesureApplicativeDTO.setApplicationRecu(Boolean.valueOf(applicationRecu));
            mesureApplicativeDTO.setDirectionResponsable(directionResponsable);

            if (ministerePilote != null && ministerePilote.length() >= 3) {
                EpgOrganigrammeService epgOrganigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
                String ministereNor = ministerePilote.substring(0, 3);
                EntiteNode ministere = epgOrganigrammeService.getMinistereFromNor(ministereNor);
                if (ministere == null) {
                    repriseLog.warn(
                        "ministere rapporteur pour mesure applicative not found (numeroOrdre " +
                        numeroOrdre +
                        " - article = " +
                        mesureApplicativeDTO.getArticle() +
                        " ) => ministere " +
                        ministerePilote
                    );
                } else {
                    mesureApplicativeDTO.setMinisterePilote(ministere.getId());
                }
            } else {
                repriseLog.warn(
                    "ministere rapporteur pour mesure applicative not found (numeroOrdre " +
                    numeroOrdre +
                    " - article = " +
                    mesureApplicativeDTO.getArticle() +
                    ") est vide"
                );
            }

            mesureApplicativeDTO.setObservation(observation);

            MesureApplicative mesureApplicative = SolonEpgServiceLocator
                .getActiviteNormativeService()
                .saveMesureReprise(texteMaitreLoiId, mesureApplicativeDTO, session);
            repriseLog.debug("Add Mesure To Loi OK - numeroOrdre = " + numeroOrdre);
            return mesureApplicative.getDocument();
        } catch (Exception e) {
            repriseLog.error("Add Mesure To Loi KO - numeroOrdre= " + numeroOrdre, e);
            throw e;
        }
    }
}
