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

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TranspositionDirective;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.organigramme.EntiteNode;

@Operation(id = AddTranspositionDirective.ID, category = Constants.CAT_DOCUMENT, label = "Add Transposition Directive", description = "Add Transposition Directive")
public class AddTranspositionDirective {

	private static final Log	repriseLog	= LogFactory.getLog("reprise-log");

	public static final String	ID			= "EPG.AddTranspositionDirective";

	@Context
	protected OperationContext	ctx;

	@Context
	protected CoreSession		session;

	@Param(name = "properties")
	protected Properties		properties;

	@OperationMethod
	public void run() throws Exception {

		String numero = properties.get("numero");

		repriseLog.debug("debut Transposition Directive - numero = " + numero);

		try {
			if (numero == null || "".equals(numero.trim())) {
				repriseLog.warn(" numero is empty ");
			} else {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				String titre = properties.get("titre");
				String dateEcheanceStr = properties.get("dateEcheance");
				String dateDirectiveStr = properties.get("dateDirective");
				// String etat = properties.get("etat");
				String ministerePilote = properties.get("ministerePilote");
				String directionResponsable = properties.get("directionResponsable");

				TranspositionDirective transpositionDirective = SolonEpgServiceLocator
						.getTranspositionDirectiveService().findOrCreateTranspositionDirective(session, numero);

				transpositionDirective.setDirectionResponsable(directionResponsable);
				transpositionDirective.setTitre(titre);

				if (dateEcheanceStr != null && !dateEcheanceStr.trim().equals("")) {
					Date dateEcheance = formatter.parse(dateEcheanceStr);
					Calendar cal = Calendar.getInstance();
					cal.setTime(dateEcheance);
					transpositionDirective.setDateEcheance(cal);
				}
				if (dateDirectiveStr != null && !dateDirectiveStr.trim().equals("")) {
					Date dateDirective = formatter.parse(dateDirectiveStr);
					Calendar cal = Calendar.getInstance();
					cal.setTime(dateDirective);
					transpositionDirective.setDateDirective(cal);
				}

				if (ministerePilote != null && ministerePilote.length() >= 3) {
					EpgOrganigrammeService epgOrganigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
					EntiteNode ministere = epgOrganigrammeService.getMinistereFromNor(ministerePilote.substring(0, 3));
					if (ministere == null) {
						repriseLog.warn("ministere rapporteur pour Loi not found (numero Loi " + numero
								+ ") => ministere " + ministerePilote);
					} else {
						transpositionDirective.setMinisterePilote(ministere.getId());
					}
				} else {
					repriseLog.warn("ministere rapporteur pour Loi not found (numero Loi " + numero + ") est vide");
				}

				transpositionDirective.save(session);

				ActiviteNormative activiteNormative = transpositionDirective.getDocument().getAdapter(
						ActiviteNormative.class);
				activiteNormative.setTransposition(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE);
				activiteNormative.save(session);
				repriseLog.debug("Add Transposition Directive is OK - numero = " + numero);
			}
		} catch (Exception e) {
			repriseLog.error("Add Transposition Directive KO - numero = " + numero, e);
			throw e;
		}
	}
}
