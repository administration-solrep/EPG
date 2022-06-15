package fr.dila.solonepg.operation.activitenormative;

import fr.dila.solonepg.api.dto.DecretDTO;
import fr.dila.solonepg.core.dto.activitenormative.DecretDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.util.Properties;
import org.nuxeo.ecm.core.api.CoreSession;

@Operation(
    id = AddDecretToMesure.ID,
    category = Constants.CAT_DOCUMENT,
    label = "Attach Decret To Loi",
    description = "Attach Decret To Loi"
)
public class AddDecretToMesure {
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.AddDecretToMesure";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "texteMaitreLoiId", required = true, order = 1)
    protected String texteMaitreLoiId;

    @Param(name = "mesureId", required = true, order = 2)
    protected String mesureId;

    @Param(name = "properties")
    protected Properties properties;

    @OperationMethod
    public void run() throws OperationException {
        String numeroNor = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            numeroNor = properties.get("numeroNor");
            String titreOfficiel = properties.get("titreOfficiel");
            String typeActe = properties.get("typeActe");
            String sectionCE = properties.get("sectionCE");
            String numerosTextes = properties.get("numerosTextes");
            //sString numeroJOPublication = properties.get("numeroJOPublication");
            repriseLog.debug("------------------Add Decert To Mesure - numeroNor" + numeroNor);
            DecretDTO decretDTO = new DecretDTOImpl();
            decretDTO.setNumeroNor(numeroNor);
            decretDTO.setTitreOfficiel(titreOfficiel);
            decretDTO.setTypeActe(typeActe);
            decretDTO.setSectionCE(sectionCE);
            decretDTO.setNumerosTextes(numerosTextes);
            //decretDTO.setNumeroJOPublication(numeroJOPublication);

            String dateSaisineCEStr = properties.get("dateSaisineCE");
            if (dateSaisineCEStr != null && !dateSaisineCEStr.trim().equals("")) {
                Date dateSaisineCE = formatter.parse(dateSaisineCEStr);
                decretDTO.setDateSaisineCE(dateSaisineCE);
            }
            String dateSignatureStr = properties.get("dateSignature");
            if (dateSignatureStr != null && !dateSignatureStr.trim().equals("")) {
                Date dateSignature = formatter.parse(dateSignatureStr);
                decretDTO.setDateSignature(dateSignature);
            }
            String datePublicationStr = properties.get("datePublication");
            if (datePublicationStr != null && !datePublicationStr.trim().equals("")) {
                Date datePublication = formatter.parse(datePublicationStr);
                decretDTO.setDatePublication(datePublication);
            }
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .addDecretToMesureReprise(texteMaitreLoiId, mesureId, decretDTO, session);
            repriseLog.debug("Add Decert To Mesure is OK - numeroNor" + numeroNor);
        } catch (ParseException pe) {
            repriseLog.error("Add Decert To Mesure is KO due to wrong date format - numeroNor= " + numeroNor, pe);
            throw new OperationException(pe);
        }
    }
}
