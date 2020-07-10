import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import org.nuxeo.ecm.core.api.DocumentModel;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.solonepg.api.administration.ParametrageApplication;
import java.util.ArrayList;
import org.nuxeo.runtime.transaction.TransactionHelper;
import org.jboss.seam.core.Events;
import fr.dila.solonepg.api.constant.SolonEpgParametrageApplicationConstants;

	println("lancement du script d'ajout de la priorité dans le paramétrage de l'application");
	
    ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
    DocumentModel parametrageApplication = parametreApplicationService.getParametreApplicationDocument(Session).getDocument();
    ParametrageApplication parametrageApplicationDoc = parametrageApplication.getAdapter(ParametrageApplication.class);
    //Colonnes présentes
    List<String> metaDataDisponible = parametrageApplicationDoc.getMetadonneDisponibleColonneCorbeille();
    println("liste des colonnes déjà présentes dans le paramétrage");
    Boolean prioritePresente = false;
    for(String meta : metaDataDisponible) {
    	println(meta);
    	if(meta.equals("priorite")) {
    		prioritePresente = true;
    	}
    }
    //On récupère toutes les colonnes non présentes
    //Ajout de la priorité
    if(!prioritePresente) {
    	println("Ajout de la priorité");
    	metaDataDisponible.add("priorite");
    }

    parametrageApplicationDoc.setMetadonneDisponibleColonneCorbeille(metaDataDisponible);
    Session.save();
    Session.saveDocument(parametrageApplicationDoc.getDocument());
	TransactionHelper.commitOrRollbackTransaction();
	TransactionHelper.startTransaction();
    
	println("Fin du script");
	
	return "Fin du script groovy";