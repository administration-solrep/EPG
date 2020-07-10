import java.util.List;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator ;
import org.nuxeo.runtime.transaction.TransactionHelper;
import javax.transaction.Status;


/**
 * Script groovy pour mettre a jour la date des promulgation des lois
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


class UpdateDatePromulgation {    
    public static void run(final CoreSession session) {   
	
      ActiviteNormativeService activiteNormativeService = SolonEpgServiceLocator.getActiviteNormativeService();		
		
      String query = "SELECT a.ecm:uuid as id FROM ActiviteNormative as a WHERE a.texm:datePromulgation IS NULL AND a.texm:titreOfficiel IS NOT NULL AND a.norma:applicationLoi=1";

      Long count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(query), null);
      print count + " Documents trouvés";
      long limit = (long) 100;

      for (Long offset = (long) 0; offset <= count; offset += limit) {
		  Long borne = (count<offset+limit?count:offset+limit);
		  print "Récupération des textes maitres de " + offset + " à " + borne;
		  List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, "ActiviteNormative", QueryUtils.ufnxqlToFnxqlQuery(query), null, limit, 0);
		  try {
		      for (DocumentModel textMaitreDoc : list) {
			  final TexteMaitre texteMaitre = textMaitreDoc.getAdapter(TexteMaitre.class);
			  
			  print "commencer à réparer la loi : " + texteMaitre.getTitreOfficiel() ;
			  
			  
			  Calendar datePromulgation  = activiteNormativeService.extractDateFromTitre(texteMaitre.getTitreOfficiel()) ;
			  String msg = datePromulgation == null ? "nulle" : datePromulgation.getTime().toString() ;
			  print "La date extraite est " + msg ;  
			  texteMaitre.setDatePromulgation(datePromulgation) ;
			  session.saveDocument(textMaitreDoc);
			  
			  print "Fin réparation" ;
		      }
		      session.save();
		  } finally {		  
		      TransactionHelper.commitOrRollbackTransaction();
		      TransactionHelper.startTransaction();
		  }
		  print "Fin traitement des lois de " + offset + " à " + borne;
      }
    }
  }

print "Début script groovy de mise a jour de la date promulgation des lois";
print "-------------------------------------------------------------------------------";

UpdateDatePromulgation.run(Session) ;
    
print "-------------------------------------------------------------------------------";
print "Fin du script groovy de mise a jour de la date promulgation des lois";
return "Fin du script groovy";
