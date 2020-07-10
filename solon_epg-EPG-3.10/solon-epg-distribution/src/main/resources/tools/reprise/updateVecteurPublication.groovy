import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunVoid;
import org.nuxeo.ecm.core.api.CoreSession;
import fr.dila.st.api.constant.STConstant;

import fr.dila.st.core.util.ServiceUtil;
import org.nuxeo.ecm.platform.audit.service.NXAuditEventsService;

class UpdateVecteurPublication implements RunVoid {
	
	CoreSession session;
	
	public UpdateVecteurPublication(session) {
		super();
		this.session = session;
	}
	
	private DocumentModel getVecteurPublication(vecteurLabel) {
		String queryMode = "SELECT * FROM VecteurPublication WHERE vp:vpIntitule = '" + vecteurLabel + "'";
		DocumentModelList vecteurList = session.query(queryMode);
		if(vecteurList == null || vecteurList.isEmpty()) {
			print "Aucun vecteur correspondant à ce label " + vecteurLabel;
			return null;
		} else if (vecteurList.size() > 1) {
			print "Le vecteur existe en plusieurs exemplaires. C'est une anomalie";
			return null;
		}
		return vecteurList.get(0);		
	}
	
	private String getQueryUpdate(vectLabel) throws ClientException {
		DocumentModel vectDoc = getVecteurPublication(vectLabel);
		if (vectDoc != null) {
			
			return "UPDATE DOS_VECTEURPUBLICATION SET ITEM = '" + vectDoc.getId() + "' WHERE ITEM = '" + vectLabel + "'";
			
		} else {
			throw new ClientException("Aucun vecteur de publication n'a été trouvé");
		}
	}
	
	public void runWith(EntityManager em) {
		print "Début de la mise à jour des dossiers";
		
		try {
			Query queryJO = em.createNativeQuery(getQueryUpdate("Journal Officiel"));        
	        int nbResultJo =  queryJO.executeUpdate();
	        print "Il y a eu " + nbResultJo + " Dossiers mis à jours et qui possédaient le vecteur Journal Officiel";
	        em.flush();
        } catch (Exception ex) {
        	ex.printStackTrace();
        }

		try {
	        Query queryBODMR = em.createNativeQuery(getQueryUpdate("BODMR"));        
	        int nbResultBodmr = queryBODMR.executeUpdate();
	        print "Il y a eu " + nbResultBodmr + " Dossiers mis à jours et qui possédaient le vecteur BODMR";
	        em.flush();
        } catch (Exception ex) {
        	ex.printStackTrace();
        }

		try {
	        Query queryDoc = em.createNativeQuery(getQueryUpdate("Documents administratifs"));        
	        int nbResultDoc = queryDoc.executeUpdate();
	        print "Il y a eu " + nbResultDoc + " Dossiers mis à jours et qui possédaient le vecteur Documents administratifs";
	        em.flush();
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        try {
	        Query queryJoDoc = em.createNativeQuery(getQueryUpdate("JO + Documents administratifs"));        
	        int nbResultJoDoc = queryJoDoc.executeUpdate();
	        print "Il y a eu " + nbResultJoDoc + " Dossiers mis à jours et qui possédaient le vecteur JO + Documents administratifs";
	        em.flush();
        } catch (Exception ex) {
        	ex.printStackTrace();
        }			
	}
}

UpdateVecteurPublication update  = new UpdateVecteurPublication(Session);

try {
   ((NXAuditEventsService) ServiceUtil.getLocalService(org.nuxeo.ecm.platform.audit.api.NXAuditEvents.class)).getOrCreatePersistenceProvider().run(true, update);
} catch (ClientException e) {
	print e.printStackTrace();
}


return "Fin du script groovy";
