package fr.dila.solonepg.operation.activitenormative;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.dto.TexteMaitreLoiDTO;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreLoiDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.core.util.DateUtil;

@Operation(id = AddLoiToTableauMaitre.ID, category = Constants.CAT_DOCUMENT, label = "Add Loi To Tableau Maitre", description = "Add Loi To Tableau Maitre")
public class AddLoiToTableauMaitre {

	private static final Log	repriseLog	= LogFactory.getLog("reprise-log");

	public static final String	ID			= "EPG.AddLoiToTableauMaitre";

	@Context
	protected OperationContext	ctx;

	@Context
	protected CoreSession		session;

	@Param(name = "properties")
	protected Properties		properties;

	@OperationMethod
	public DocumentModel run() throws Exception {

		String numero = "";
		try {
			SimpleDateFormat formatter = DateUtil.simpleDateFormat("dd/MM/yyyy");
			String numeroNor = properties.get("numeroNor");
			String titreActe = properties.get("titreActe");
			String ministerePilote = properties.get("ministerePilote");
			String motCle = properties.get("motCle");
			String natureTexte = properties.get("natureTexte");
			String procedureVote = properties.get("procedureVote");
			String commissionSenat = properties.get("commissionSenat");
			numero = properties.get("numero");

			repriseLog.debug("Add Loi To Tableau Maitre - numero = " + numero);

			TexteMaitreLoiDTO texteMaitreLoiDTO = new TexteMaitreLoiDTOImpl();
			texteMaitreLoiDTO.setNumeroNor(numeroNor);
			texteMaitreLoiDTO.setTitreOfficiel(titreActe);

			if (ministerePilote != null && ministerePilote.length() >= 3) {
				EpgOrganigrammeService epgOrganigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
				EntiteNode ministere = epgOrganigrammeService.getMinistereFromNor(ministerePilote.substring(0, 3));
				if (ministere == null) {
					repriseLog.warn("ministere rapporteur pour Loi not found (numero Loi " + numero + ") => ministere "
							+ ministerePilote);
				} else {
					texteMaitreLoiDTO.setMinisterePilote(ministere.getId());
				}
			} else {
				repriseLog.warn("ministere rapporteur pour Loi not found (numero Loi " + numero + ") est vide");
			}

			texteMaitreLoiDTO.setMotCle(motCle);
			texteMaitreLoiDTO.setNatureTexte(natureTexte);
			texteMaitreLoiDTO.setProcedureVote(procedureVote);
			texteMaitreLoiDTO.setCommissionSenat(commissionSenat);
			texteMaitreLoiDTO.setNumero(numero);

			String datePublicationStr = properties.get("datePublication");
			if (datePublicationStr != null && !datePublicationStr.trim().equals("")) {
				Date datePublication = formatter.parse(datePublicationStr);
				texteMaitreLoiDTO.setDatePublication(datePublication);
			}

			if (titreActe != null) {
				titreActe = titreActe.replace("1er ", "1 ");
			}
			Calendar datePromulgationCalendar = SolonEpgServiceLocator.getActiviteNormativeService()
					.extractDateFromTitre(titreActe);
			String datePromulgationStr = properties.get("datePromulgation");

			if (datePromulgationCalendar == null) {
				if (datePromulgationStr != null && !datePromulgationStr.trim().equals("")) {
					Date datePromulgation = formatter.parse(datePromulgationStr);
					texteMaitreLoiDTO.setDatePromulgation(datePromulgation);
				}
			} else {
				texteMaitreLoiDTO.setDatePromulgation(datePromulgationCalendar.getTime());
			}

			//
			TexteMaitre texteMaitre = SolonEpgServiceLocator.getActiviteNormativeService()
					.addLoiToTableauMaitreReprise(texteMaitreLoiDTO, session);
			repriseLog.debug("Add Loi To Tableau Maitre is OK");
			return texteMaitre.getDocument();
		} catch (Exception e) {
			repriseLog.error("Ajout Add Loi To Tableau Maitre KO - numero = " + numero, e);
			throw e;
		}
	}
}
