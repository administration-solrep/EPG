package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants.MAJ_TARGET;
import fr.dila.solonepg.api.service.HistoriqueMajMinisterielleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.web.contentview.AbstractDTOPageProvider;
import fr.dila.st.web.contentview.HiddenColumnPageProvider;

/**
 * Provider pour les mises à jour ministérielles de l'activité normative
 * 
 * @author jgomez
 * 
 */
public class MajMinPageProvider extends AbstractDTOPageProvider implements HiddenColumnPageProvider {
	
	 /**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -3921885974966520546L;
	private static final String TARGET_PROP = "target";


	@Override
	protected void fillCurrentPageMapList(CoreSession session) throws ClientException {
		if (!getProperties().containsKey(TARGET_PROP) || getProperties().get(TARGET_PROP) == null) {
			throw new ClientException("MajMinPageProvider doit avoir dans ses propriétés une clé target passant un objet MAJ_TARGET");
		}
		MAJ_TARGET target = getTarget();
		HistoriqueMajMinisterielleService service = SolonEpgServiceLocator.getHistoriqueMajMinisterielleService();
		currentItems = new ArrayList<Map<String, Serializable>>();
		currentItems.addAll(service.getHistoriqueMajMap(session, target));
		resultsCount = currentItems.size();
	}

	private MAJ_TARGET getTarget() {
		return (MAJ_TARGET) getProperties().get(TARGET_PROP);
	}

	@Override
	public boolean isSortable() {
		return false;
	}

	@Override
	public Boolean isHiddenColumn(String isHidden) throws ClientException {
		// Pour la colonne numOrdre, on la cache quand on est pas dans application loi ou suivi des habilitation ou application des ordonnances
		if (StringUtils.isNotEmpty(isHidden)) {
			return getTarget() != MAJ_TARGET.APPLICATION_LOI && getTarget() != MAJ_TARGET.HABILITATION && getTarget() != MAJ_TARGET.APPLICATION_ORDONNANCE;
		}
		return Boolean.FALSE;
	}
	    	
}
