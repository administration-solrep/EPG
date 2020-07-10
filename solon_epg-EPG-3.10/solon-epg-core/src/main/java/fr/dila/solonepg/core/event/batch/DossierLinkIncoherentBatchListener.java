package fr.dila.solonepg.core.event.batch;

import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;

import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.core.util.ExcelUtil;
import fr.dila.ss.core.event.batch.AbstractDossierLinkIncoherentBatchListener;

/**
 * Batch de dossierLink incohérent
 *
 */
public class DossierLinkIncoherentBatchListener extends AbstractDossierLinkIncoherentBatchListener {
    
	/**
	 * Requête dossier link en todo mais étape état diff de running
	 */
	private static final String STEP_NOT_RUNNING_QUERY = "select id from dossier_solon_epg where lastdocumentroute in (select documentrouteid from routing_task "
			+ " where id in (select routingtaskid from actionnable_case_link where id in (select id from misc where lifecyclestate = 'todo')) "
			+ " and id not in (select id from misc where lifecyclestate ='running'))";
	
	/**
	 * Requête étape en running mais pas de dossier link à l'état todo
	 */
	private static final String DOSSIER_LINK_MISSING_QUERY = "select id from dossier_solon_epg where lastdocumentroute in (select documentrouteid from routing_task "
			+ "	where id not in (select routingtaskid from actionnable_case_link where id in (select id from misc where lifecyclestate = 'todo')) "
			+ "	and id in (select id from misc where lifecyclestate ='running'))";
	
	/**
	 * Requête des dossiers dont la feuille de route est terminée, mais pour lesquels des dossiers link existent
	 */
	private static final String DOSSIER_LINK_EXISTING_QUERY = "select id from dossier_solon_epg where lastdocumentroute in (select id from misc where lifecyclestate ='done') " 
			+ " and id in (select caseDocumentId from actionnable_case_link where id in (select id from misc where lifecyclestate = 'todo'))";
	
	/**
	 * Default constructor
	 */
	public DossierLinkIncoherentBatchListener() {
        super();
    }

    @Override
    protected DataSource generateData(CoreSession session, List<String> dossiersIds) {        
        return ExcelUtil.creationListDossierExcelFromIds(session, dossiersIds);
    }

    @Override
	protected List<String> getQueries() {
    	ArrayList<String> queries = new ArrayList<String>();
    	queries.add(STEP_NOT_RUNNING_QUERY);
    	queries.add(DOSSIER_LINK_MISSING_QUERY);
    	queries.add(DOSSIER_LINK_EXISTING_QUERY);
		return queries;
	}
}
