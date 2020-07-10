import java.lang.StringBuilder;
import java.util.List;

import javax.transaction.Status;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.transaction.TransactionHelper;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.api.cases.MajMin;
import fr.dila.solonepg.api.cases.Dossier;

import fr.dila.st.core.query.QueryUtils;

/**
 * script groovy de mise les id des dossiers qui sont a null
 * 
 */
class TransactionUtils {
    public static boolean isTransactionAlive() {
        try {
            return !(TransactionHelper.lookupUserTransaction().getStatus() == Status.STATUS_NO_TRANSACTION);
        } catch (Exception e) {
            return false;
        }
    }
}    

class MajMinUtils {    
    public static void run(final CoreSession session) {     
    
       final StringBuilder query = new StringBuilder();
       query.append("SELECT m.ecm:uuid as id FROM MajMin as m Where m.majm:idDossier is NULL");
       Long count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(query.toString()), null);
       print count + " MajMin trouvés";
       long limit = (long) 50;
       for (Long offset = (long) 0; offset <= count; offset += limit) {
           Long borne = (count<offset+limit?count:offset+limit);
    
           print "Récupération MajMin de " + offset + " à " + borne;
           if (!TransactionUtils.isTransactionAlive()) {
               TransactionHelper.startTransaction();
           }
        
            List<DocumentModel> docModelList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, query.toString(), null, limit, 0);
        
            for (DocumentModel docModel : docModelList) {
                try {
                   MajMin majMin = docModel.getAdapter(MajMin.class);
                   String nor = majMin.getNorDossier();  
                   Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(session, nor);
                   if (dossier == null) {
                       print "Dossier non trouvé : " + nor;
                   } else {
                       majMin.setIdDossier(dossier.getDocument().getId());      
                       session.saveDocument(majMin.getDocument()); 
                   }            
                } catch (ClientException ce) {
                    print "Impossible de mettre à jour l'id : " + docModel.getId();
                }
            }
            session.save();
            TransactionHelper.commitOrRollbackTransaction();
            print "Fin traitement de " + offset + " à " + borne;
        }
    }
}
    
print "Début du script pour remplir les id dossier null de la mise à jour ministeriel";
print "-------------------------------------------------------------------------------";

MajMinUtils.run(Session) ;

print "-------------------------------------------------------------------------------";
print "Fin du script pour remplir les id dossier null de la mise à jour ministeriel";
return "Fin du script groovy";

