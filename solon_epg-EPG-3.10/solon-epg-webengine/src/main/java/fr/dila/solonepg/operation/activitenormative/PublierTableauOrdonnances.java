package fr.dila.solonepg.operation.activitenormative;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

@Operation(id = PublierTableauOrdonnances.ID, category = Constants.CAT_DOCUMENT, label = "Publier Tableau Des Ordonnances", description = "Publier Tableau Des Ordonnances")
public class PublierTableauOrdonnances {
	private static final Log	repriseLog	= LogFactory.getLog("reprise-log");

	public static final String	ID			= "EPG.PublierTableauOrdonnances";

	@Context
	protected CoreSession		session;

	@OperationMethod
	public void run() throws Exception {
		repriseLog.debug("Debut - Publier Tableau Des Ordonnances");

		// Publier liste des ordonnances + repartition ministere
		SolonEpgServiceLocator.getActiviteNormativeService().updateOrdonnancesListePubliee(session, true);
		List<DocumentModel> documentModelList = SolonEpgServiceLocator.getActiviteNormativeService()
				.getAllAplicationLoiDossiers(session, true);
		ActiviteNormative activiteNormative;
		for (DocumentModel documentModel : documentModelList) {
			activiteNormative = documentModel.getAdapter(ActiviteNormative.class);
			SolonEpgServiceLocator.getActiviteNormativeService().generateANRepartitionMinistereHtml(session,
					activiteNormative, true);
		}

		repriseLog.debug("Fin - Publier Tableau Des Ordonnances");
	}

}
