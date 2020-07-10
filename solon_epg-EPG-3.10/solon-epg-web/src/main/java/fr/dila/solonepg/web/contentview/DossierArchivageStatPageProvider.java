package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.SortInfo;

import fr.dila.solonepg.adamant.SolonEpgAdamantServiceLocator;
import fr.dila.solonepg.adamant.service.BordereauService;
import fr.dila.solonepg.core.dto.DossierArchivageStatDTO;
import fr.dila.st.web.contentview.AbstractDTOPageProvider;
import fr.dila.st.web.contentview.CorePageProviderUtil;

public class DossierArchivageStatPageProvider extends AbstractDTOPageProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5247614064823946189L;

	private Map<String, DossierArchivageStatDTO> mapDossierIdDTO;

	private List<DossierArchivageStatDTO> listFullResultDossierDTO;

	@Override
	protected void fillCurrentPageMapList(CoreSession coreSession) throws ClientException {

		final Map<String, Serializable> props = getProperties();
		final BordereauService bordereauService = SolonEpgAdamantServiceLocator.getBordereauService();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date dateDebutInterval = (Date)getParameters()[0];
		Date dateFinInterval = (Date)getParameters()[1];
		String statutArchivage = (String)getParameters()[2];

		currentItems = new ArrayList<Map<String, Serializable>>();
		mapDossierIdDTO = new HashMap<String, DossierArchivageStatDTO>();

		if(dateDebutInterval != null && dateFinInterval != null && statutArchivage != null) {

			SortInfo[] sortArray = null;
			StringBuilder sortInfo = new StringBuilder();
			if (sortInfos != null) {
				sortArray = CorePageProviderUtil.getSortinfoForQuery(sortInfos);
				sortInfo.append(sortArray[0].getSortColumn()).append(sortArray[0].getSortAscending() ? " asc" : " desc");
				for(int i=1; i<sortArray.length; i++) {
					sortInfo.append(", ").append(sortArray[i].getSortColumn()).append(sortArray[i].getSortAscending() ? " asc" : " desc");
				}
			}
			
			// On formate les dates pour les inclure dans l'intervale 
			String formatedDateDebut = sdf.format(dateDebutInterval) + " 00:00:00";
			String formatedDateFin = sdf.format(dateFinInterval) + " 23:59:59";

			resultsCount = bordereauService.getCountDossierArchivageStatResult(statutArchivage, 
					formatedDateDebut, formatedDateFin);

			List<DossierArchivageStatDTO> resultList = bordereauService.getDossierArchivageStatResultList(statutArchivage, 
					formatedDateDebut, formatedDateFin, offset, pageSize, sortInfo.toString());

			for (DossierArchivageStatDTO dto : resultList) {
				mapDossierIdDTO.put(dto.getDocIdForSelection(), dto);
				currentItems.add(dto);
			}
		}
	}

	@Override
	public void setSearchDocumentModel(DocumentModel searchDocumentModel) {
		// remise en place du bug nuxeo pour forcer tout le temps le refresh
		this.searchDocumentModel = searchDocumentModel;
		refresh();
	}

	@Override
	protected void buildQuery() {
		query = "";
	}

	public List<DossierArchivageStatDTO> getListFullResultDossierDTO() throws ClientException {

		final BordereauService bordereauService = SolonEpgAdamantServiceLocator.getBordereauService();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SortInfo[] sortArray = null;
		StringBuilder sortInfo = new StringBuilder();
		
		Date dateDebutInterval = (Date)getParameters()[0];
		Date dateFinInterval = (Date)getParameters()[1];
		String statutArchivage = (String)getParameters()[2];
		
		// On formate les dates pour les inclure dans l'intervale 
		String formatedDateDebut = sdf.format(dateDebutInterval) + " 00:00:00";
		String formatedDateFin = sdf.format(dateFinInterval) + " 23:59:59";
		
		if (sortInfos != null) {
			sortArray = CorePageProviderUtil.getSortinfoForQuery(sortInfos);
			sortInfo.append(sortArray[0].getSortColumn()).append(sortArray[0].getSortAscending() ? " asc" : " desc");
			for(int i=1; i<sortArray.length; i++) {
				sortInfo.append(", ").append(sortArray[i].getSortColumn()).append(sortArray[i].getSortAscending() ? " asc" : " desc");
			}
		}
		return bordereauService.getDossierArchivageStatFullResultList(statutArchivage, 
				formatedDateDebut, formatedDateFin, sortInfo.toString());
	}
}
