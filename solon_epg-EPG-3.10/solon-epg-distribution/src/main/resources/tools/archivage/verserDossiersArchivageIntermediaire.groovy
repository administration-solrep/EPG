import java.util.List;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import org.nuxeo.runtime.transaction.TransactionHelper;
import javax.transaction.Status;


/**
 * Script groovy pour mettre a jour les minister id de l'etape a partir du ministere label
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


class ArchivageIntermediareInjector {    
    public static void run(final CoreSession session) {   
		
      String query = "SELECT d.ecm:uuid as id FROM Dossier as d WHERE d.dos:statutArchivage = '2' AND testAcl(d.ecm:uuid) = 1";

      Long count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(query), null);
      print count + " dossiers trouvees";
      long limit = (long) 100;

      for (Long offset = (long) 0; offset <= count; offset += limit) {
	  Long borne = (count<offset+limit?count:offset+limit);
	  print "Récupération des dossiers de " + offset + " à " + borne;
	  List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, query, null, limit, 0);
		  try {
		      for (DocumentModel dossierDoc : list) {
			  final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			  dossier.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE);
			  session.saveDocument(dossierDoc);
		      }
		      session.save();
		  }finally {		  
		      TransactionHelper.commitOrRollbackTransaction();
		 }
		  
		  
	  print "Fin traitement des dossiers de " + offset + " à " + borne;
      }
    }
  }

print "Début script groovy de versement de dossier candidats au archivage intermediaire dans l'archivage intermediaire";
print "-------------------------------------------------------------------------------";

ArchivageIntermediareInjector.run(Session) ;
    
print "-------------------------------------------------------------------------------";
print "Fin du script groovy de versement de dossier candidats au archivage intermediaire dans l'archivage intermediaire";
return "Fin du script groovy";