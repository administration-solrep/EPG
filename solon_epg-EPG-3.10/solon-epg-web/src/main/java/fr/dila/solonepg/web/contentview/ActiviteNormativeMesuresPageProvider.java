package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.query.api.PageSelections;

import fr.dila.solonepg.web.activitenormative.TexteMaitreActionsBean;
import fr.dila.st.web.contentview.AbstractDTOPageProvider;

/**
 * Provider pour les mesures de l'activite normative
 * 
 * @author asatre
 * 
 */
public class ActiviteNormativeMesuresPageProvider extends AbstractDTOPageProvider {

	private static final String	TEXTE_MAITRE_ACTIONS	= "texteMaitreActions";
	private static final long	serialVersionUID		= 1L;

	@Override
	protected void fillCurrentPageMapList(CoreSession coreSession) throws ClientException {
		currentItems = new ArrayList<Map<String, Serializable>>();
		resultsCount = getBean().getListMesure().size();
		if (offset > resultsCount) {
			offset = 0;
		}
		int maxIndex = (int) (offset + pageSize);
		currentItems.addAll(getBean().getListMesure().subList((int) offset,
				(int) (resultsCount < maxIndex ? resultsCount : maxIndex)));

		// Si on a seulement une ligne vide dans la liste des mesures, on passe le result count à zéro pour éviter
		// d'afficher qu'on a 1 mesure (M154491)
		if (resultsCount == 1 && currentItems.get(0).get("numeroOrdre") == null
				&& currentItems.get(0).get("codeModifie") == null
				&& currentItems.get(0).get("directionResponsable") == null
				&& currentItems.get(0).get("objetRIM") == null && currentItems.get(0).get("baseLegale") == null
				&& currentItems.get(0).get("ministerePilote") == null && currentItems.get(0).get("article") == null
				&& currentItems.get(0).get("consultationsHCE") == null
				&& currentItems.get(0).get("calendrierConsultationsHCE") == null
				&& currentItems.get(0).get("datePrevisionnelleSaisineCE") == null
				&& currentItems.get(0).get("typeMesure") == null && currentItems.get(0).get("numeroQuestion") == null
				&& currentItems.get(0).get("responsableAmendement") == null
				&& currentItems.get(0).get("dateDisponnibiliteAvantProjet") == null
				&& currentItems.get(0).get("consultationsHCE") == null
				&& currentItems.get(0).get("poleChargeMission") == null
				&& currentItems.get(0).get("datePassageCM") == null
				&& currentItems.get(0).get("dateObjectifPublication") == null
				&& currentItems.get(0).get("dateReunionProgrammation") == null
				&& currentItems.get(0).get("dateCirculationCompteRendu") == null
				&& currentItems.get(0).get("dateDateMiseApplication") == null) {
			resultsCount = 0;
		}

	}

	@Override
	public List<Map<String, Serializable>> getCurrentPage() {
		currentItems = null;
		super.getCurrentPage();
		if (currentItems.size() > 0) {
			for (Map<String, Serializable> tmpItem : currentItems) {
				tmpItem.put("validate", true);
			}
		}
		return currentItems;
	}

	@Override
	public PageSelections<Map<String, Serializable>> getCurrentSelectPage() {
		currentSelectPage = null;
		return super.getCurrentSelectPage();
	}

	private TexteMaitreActionsBean getBean() {
		Map<String, Serializable> props = getProperties();
		return (TexteMaitreActionsBean) props.get(TEXTE_MAITRE_ACTIONS);
	}

}
