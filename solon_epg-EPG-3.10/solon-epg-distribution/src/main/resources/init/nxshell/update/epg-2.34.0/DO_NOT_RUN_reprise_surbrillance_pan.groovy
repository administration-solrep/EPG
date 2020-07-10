import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Habilitation;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.Ordonnance;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;

import fr.dila.st.core.query.QueryUtils;

class RepriseSurbrillanceActNormative {
	
    public static TexteMaitre getTexteMaitreByUid(CoreSession session, String uid) {
        IdRef refDoc = new IdRef(uid);
    	if (session.exists(refDoc)) {
    		return session.getDocument(new IdRef(uid)).getAdapter(TexteMaitre.class);
    	} else {
    		return null;
    	}
    }
    
    public static MesureApplicative getMesureApplicativeByUid(CoreSession session, String uid) {
        IdRef refDoc = new IdRef(uid);
    	if (session.exists(refDoc)) {
    		return session.getDocument(new IdRef(uid)).getAdapter(MesureApplicative.class);
    	} else {
    		return null;
    	}
    }
    
    public static Habilitation getHabilitationByUid(CoreSession session, String uid) {
        IdRef refDoc = new IdRef(uid);
    	if (session.exists(refDoc)) {
    		return session.getDocument(new IdRef(uid)).getAdapter(Habilitation.class);
    	} else {
    		return null;
    	}
    }
    
    public static void run(CoreSession session) {        
    	final String queryAppl = "SELECT d.ecm:uuid as id FROM ActiviteNormative as d WHERE d.norma:applicationLoi = '1'";
    	final String queryHab = "SELECT d.ecm:uuid as id FROM ActiviteNormative as d WHERE d.norma:ordonnance38C = '1'";
    	final String queryOrdo = "SELECT d.ecm:uuid as id FROM ActiviteNormative as d WHERE d.norma:ordonnance = '1'";
    	
    	final List<DocumentModel> docsAppl = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE, queryAppl, null);
    	
    	print docsAppl.size() + " textes maitres application des lois à vérifier.";
    	
    	int mesuresUpdated = 0;
    	
    	for (DocumentModel loiDoc : docsAppl) {
    		TexteMaitre texteMaitre = loiDoc.getAdapter(TexteMaitre.class);
    		List<String> mesuresIds = texteMaitre.getMesuresIds();
    		for (String mesureId : mesuresIds) {
    			MesureApplicative mesure = getMesureApplicativeByUid(session, mesureId);
    			if (mesure != null) {
    				List<String> decretsIds = mesure.getDecretIds();
    				Set<String> decretsIdsInvalidated = new HashSet<String>(mesure.getDecretsIdsInvalidated());
    				for (String decretId : decretsIds) {
    					TexteMaitre decret = getTexteMaitreByUid(session, decretId);
    					if (!decret.hasValidation()) {
    						decretsIdsInvalidated.add(decretId)
    					}
    				}
    				mesure.setDecretsIdsInvalidated(new ArrayList<String>(decretsIdsInvalidated));
    				mesure.save(session);
    				mesuresUpdated++;
    			}
    		}
    	}
    	print mesuresUpdated + " mesures applicatives mises à jour.";

    	session.save();    	
    	
    	final List<DocumentModel> docsHab = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE, queryHab, null);
    	
    	print docsHab.size() + " textes maitres suivi des habilitations à vérifier.";
    	
    	int mesuresHabilitationUpdated = 0;
    	
    	for (DocumentModel loiHabDoc : docsHab) {
    		TexteMaitre texteMaitre = loiHabDoc.getAdapter(TexteMaitre.class);
    		List<String> habilitationsIds = texteMaitre.getHabilitationIds();
    		for (String habilitationId : habilitationsIds) {
    			Habilitation mesureHab = getHabilitationByUid(session, habilitationId);
    			if (mesureHab != null) {
    				List<String> ordonnancesIds = mesureHab.getOrdonnanceIds();
    				Set<String> ordonnancesIdsInvalidated = new HashSet<String>(mesureHab.getOrdonnanceIdsInvalidated());
    				for (String ordonnanceId : ordonnancesIds) {
    					TexteMaitre ordonnance = getTexteMaitreByUid(session, ordonnanceId);
    					if (!ordonnance.hasValidation()) {
    						ordonnancesIdsInvalidated.add(ordonnanceId)
    					}
    				}
    				mesureHab.setOrdonnanceIdsInvalidated(new ArrayList<String>(ordonnancesIdsInvalidated));
    				mesureHab.save(session);
    				mesuresHabilitationUpdated++;
    			}
    		}
    	}
    	
    	print mesuresHabilitationUpdated + " mesures d'habilitations mises à jour.";
    	
    	session.save();
    	
    	final List<DocumentModel> docsOrdo = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE, queryOrdo, null);
    	
    	print docsOrdo.size() + " textes maitres ratification des ordonnances à vérifier.";
    	
    	int ordonnancesUpdated = 0;
    	
    	for (DocumentModel ordoDoc : docsOrdo) {
    		Ordonnance ordonnance = ordoDoc.getAdapter(Ordonnance.class);
    		List<String> decretsIds = ordonnance.getDecretIds();
    		Set<String> decretsIdsInvalidated = new HashSet<String>(ordonnance.getDecretsIdsInvalidated());
    		for (String decretId : decretsIds) {
				TexteMaitre decret = getTexteMaitreByUid(session, decretId);
				if (!decret.hasValidation()) {
					decretsIdsInvalidated.add(decretId)
				}
    		}
    		ordonnance.setDecretsIdsInvalidated(new ArrayList<String>(decretsIdsInvalidated));
    		ordonnance.save(session);
    		ordonnancesUpdated++;
    	}
    	
    	print ordonnancesUpdated + " ordonnances mises à jour.";
    	
    	session.save();
    }
}


print "Début du script";
print "-------------------------------------------------------------------------------";

RepriseSurbrillanceActNormative.run (Session)
print "-------------------------------------------------------------------------------";
print "Fin du script ";
return "Fin du script groovy";

