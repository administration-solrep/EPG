/**
 * 
 */
package fr.dila.solonmgpp.core.operation.reprise;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.core.dto.CritereRechercheDTOImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.rest.client.WSProxyFactoryException;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epp.EvenementType;
import fr.sword.xsd.solon.epp.Message;
import fr.sword.xsd.solon.epp.NatureLoi;
import fr.sword.xsd.solon.epp.RechercherEvenementResponse;

/**
 * @author ahullot Cette classe permet de reprendre les données natures de loi issues de EPP pour les rajouter aux
 *         fiches lois présentes dans MGPP quand elles sont vides
 * 
 */
@Operation(id = RepriseFicheLoi.ID, category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY, label = RepriseFicheLoi.LABEL, description = RepriseFicheLoi.DESCRIPTION)
public class RepriseFicheLoi {

	public final static String		ID			= "SolonEpg.Reprise.RepriseFichesLoi";

	public final static String		LABEL		= "Reprise des fiches lois sans nature de MGPP";

	public final static String		DESCRIPTION	= "Cette opération reprend les données de nature de loi présentes dans EPP pour les insérer dans les fiches loi de MGPP";

	private static final STLogger	LOGGER		= STLogFactory.getLog(RepriseFicheLoi.class);

	@Context
	protected CoreSession			session;

	@OperationMethod
	public void reprise() {

		try {
			List<FicheLoi> lstFiches = getFichesLoiSansNature();
			LOGGER.debug(session, STLogEnumImpl.LOG_DEBUG_TEC, "On s'apprète à traiter " + lstFiches.size()
					+ " fiches loi");
			int nbDone = 0;
			int nbNotFound = 0;

			for (FicheLoi fiche : lstFiches) {
				NatureLoi nature = findNature(fiche.getIdDossier());
				if (nature != null) {
					fiche.setNatureLoi(nature);
					SolonMgppServiceLocator.getDossierService().saveFicheLoi(session, fiche.getDocument());
					++nbDone;
				} else {
					LOGGER.debug(session, STLogEnumImpl.LOG_DEBUG_TEC, "Dossier " + fiche.getIdDossier() + " ignoré");
					++nbNotFound;
				}
			}

			LOGGER.debug(session, STLogEnumImpl.LOG_DEBUG_TEC, "Le traitement est terminé et a traité " + nbDone
					+ " et ignoré " + nbNotFound + " fiches loi");
		} catch (Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_DOSSIER_TEC,
					"Une erreur est survenue lors du traitement de la reprise", e);
		}
	}

	private List<FicheLoi> getFichesLoiSansNature() throws ClientException {
		return SolonMgppServiceLocator.getDossierService().findFicheLoiWithoutNature(session);
	}

	private NatureLoi findNature(String idDossier) throws WSProxyFactoryException, ClientException, Exception {
		NatureLoi nature = null;
		CritereRechercheDTO criteres = new CritereRechercheDTOImpl();
		List<String> lstTypeEvents = new ArrayList<String>();
		lstTypeEvents.add(EvenementType.EVT_01.value());
		lstTypeEvents.add(EvenementType.EVT_02.value());

		criteres.setIdDossier(idDossier);
		criteres.setTypeEvenement(lstTypeEvents);

		RechercherEvenementResponse response = SolonMgppServiceLocator.getRechercheService().findMessageWS(criteres,
				session);

		if (response != null && response.getStatut() == TraitementStatut.OK) {
			if (response.getMessage() != null && !response.getMessage().isEmpty()) {
				// Le WS n'est sensé retourner qu'un seul évènement
				Message msg = response.getMessage().get(0);

				if (msg.getTypeEvenement() == EvenementType.EVT_01) {
					nature = NatureLoi.PROJET;
				} else if (msg.getTypeEvenement() == EvenementType.EVT_02) {
					nature = NatureLoi.PROPOSITION;
				}
			}
		}

		return nature;
	}

}
