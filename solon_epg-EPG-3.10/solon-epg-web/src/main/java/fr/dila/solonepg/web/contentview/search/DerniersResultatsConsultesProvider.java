package fr.dila.solonepg.web.contentview.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

import fr.dila.solonepg.api.recherche.ResultatConsulte;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.web.contentview.PaginatedPageDocumentProvider;

/**
 * 
 */
public class DerniersResultatsConsultesProvider extends PaginatedPageDocumentProvider {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1889783173278060442L;
	
	private static final STLogger LOGGER = STLogFactory.getLog(DerniersResultatsConsultesProvider.class);
	
	private static final String DOCUMENT_CATEGORY_PROPERTY = "documentCategory";
	private static final String FDR_CATEGORY = "fdr";

	/**
	 * Default constructor
	 */
	public DerniersResultatsConsultesProvider() {
		super();
	}

	@Override
	public List<DocumentModel> getCurrentPage() {
		super.getCurrentPage();
		CoreSession coreSession = getCoreSession();
		SSPrincipal principal = (SSPrincipal) coreSession.getPrincipal();
		StringBuilder queryBuilder = new StringBuilder(" SELECT * FROM ResultatConsulte WHERE dc:creator = '");
        queryBuilder.append(principal.getName());
        queryBuilder.append("'");
        
        try {
			DocumentModelList resultatsConsultesDocs = QueryUtils.doQuery(coreSession, queryBuilder.toString(), 1, 0);
			if (!resultatsConsultesDocs.isEmpty()) {
				ResultatConsulte resultatsConsultes = resultatsConsultesDocs.get(0).getAdapter(ResultatConsulte.class);
				
				Map<String, Serializable> props = getProperties();
		        final String docCategory = (String) props.get(DOCUMENT_CATEGORY_PROPERTY);
		        if (FDR_CATEGORY.equals(docCategory)) {
		    		List<String> fdrIds = resultatsConsultes.getFdrsId();
		    		currentPageDocuments = reorderDocs(fdrIds, currentPageDocuments);
		        }
			}
		} catch (ClientException e) {
			LOGGER.error(coreSession, STLogEnumImpl.LOG_EXCEPTION_TEC, e.getMessage(), e);
		}
		return currentPageDocuments;
	}
	
	private List<DocumentModel> reorderDocs(List<String> idsOrder, List<DocumentModel> workingList) {
		List<DocumentModel> pageDocuments = new ArrayList<DocumentModel>();
		for (String id : idsOrder) {
			for (DocumentModel fdr : workingList) {
				if (fdr.getId().equals(id)) {
					pageDocuments.add(fdr);
				}
			}
		}
		return pageDocuments;
	}
}
