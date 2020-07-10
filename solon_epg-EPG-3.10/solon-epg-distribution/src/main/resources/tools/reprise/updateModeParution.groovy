import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunVoid;
import fr.dila.st.core.service.STServiceLocator;
import org.nuxeo.ecm.core.api.CoreSession;
import fr.dila.st.api.constant.STConstant;

import fr.dila.st.core.util.ServiceUtil;
import org.nuxeo.ecm.platform.audit.service.NXAuditEventsService;

class UpdateModeParution implements RunVoid {
	
	CoreSession session;
	
	public UpdateModeParution(session) {
		super();
		this.session = session;
	}
	
	private DocumentModel getModeParution(modeLabel) {
		String queryMode = "SELECT * FROM ModeParution WHERE mod:mode = '" + modeLabel + "'";
		DocumentModelList modeList = session.query(queryMode);
		if(modeList == null || modeList.isEmpty()) {
			print "Aucun mode correspondant à ce label " + modeLabel;
			return null;
		} else if (modeList.size() > 1) {
			print "Le mode existe en plusieurs exemplaires. C'est une anomalie";
			return null;
		}
		return modeList.get(0);		
	}
	
	private String getQueryUpdate(modeId, modeLabel) throws ClientException {
		DocumentModel modeParutionDoc = getModeParution(modeLabel);
		if (modeParutionDoc != null) {
			return "UPDATE RETOUR_DILA SET MODEPARUTION = '" + modeParutionDoc.getId() + "' WHERE MODEPARUTION = '" + modeId + "'";
		} else {
			throw new ClientException("Aucun mode de parution");
		}
	}
	
	public void runWith(EntityManager em) {
		Query queryElec = em.createNativeQuery(getQueryUpdate("1", "Electronique"));        
        queryElec.executeUpdate();
        em.flush();

        Query queryMixte = em.createNativeQuery(getQueryUpdate("2", "Mixte"));        
        queryMixte.executeUpdate();
        em.flush();

        Query queryPapier = em.createNativeQuery(getQueryUpdate("3", "Papier"));        
        queryPapier.executeUpdate();
        em.flush();		
	}
}

UpdateModeParution update  = new UpdateModeParution(Session);

try {
   ((NXAuditEventsService) ServiceUtil.getLocalService(org.nuxeo.ecm.platform.audit.api.NXAuditEvents.class)).getOrCreatePersistenceProvider().run(true, update);
} catch (ClientException e) {
	print e.printStackTrace();
}


return "Fin du script groovy";
