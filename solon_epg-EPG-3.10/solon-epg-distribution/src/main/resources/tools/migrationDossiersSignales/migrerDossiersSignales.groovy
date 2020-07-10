import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.DocumentModelList;
import java.lang.String;
import org.apache.commons.lang.StringUtils;
import java.util.Collections;
import org.nuxeo.common.utils.*;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.common.utils.IdUtils;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.util.UnrestrictedQueryRunner;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.solonepg.api.cases.DossiersSignales;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.service.DossierSignaleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

// Script groovy pour migrer les dossiers signales qui existe deja dans l'application vers la nouvelle structure

  public  List<String> getOldDossierSignaleForUser( session,  dossierSignaleRoot) {
         StringBuilder query = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
         query.append(DossierSolonEpgConstants.DOSSIER_SIGNALE_DOCUMENT_TYPE);
         query.append(" as ds, Dossier as d");
         query.append(" WHERE d.ecm:uuid = ds.ecm:name and");
         query.append(" ds.ecm:parentId =  '"+dossierSignaleRoot.getId()+"'");
         List<String> ids = QueryUtils.doUFNXQLQueryForIdsList(session, query.toString(), null);
         return ids;
    }
    
  public List<String> addOldDocumentToDossierSignales( session, currentDocumentIdList,  ids)  {
  
        Set<String> set = new LinkedHashSet<String>(currentDocumentIdList);
        List<String> result = new ArrayList<String>();
        set.addAll(ids);
        result.addAll(set);
        return result;
    }
    
    
        println("lancement du script de migration des dossiers signales");

        String query = "select * from DossiersSignalesRoot";
        DossierSignaleService dossierSignaleService = SolonEpgServiceLocator.getDossierSignaleService();
            
        DocumentModelList dossierSignaleRootList = new UnrestrictedQueryRunner(Session, query).findAll();
        if (dossierSignaleRootList != null && dossierSignaleRootList.size() > 0) {
         for (DocumentModel dossierSignaleRoot : dossierSignaleRootList) {
             DocumentModel parent = Session.getDocument(dossierSignaleRoot.getParentRef());
             DocumentModel dossiersSignalesDoc = dossierSignaleService.getDossiersSignales(parent.getPathAsString(), Session);
             List<String>  list = getOldDossierSignaleForUser(Session, dossierSignaleRoot);
             DossiersSignales dossiersSignales = dossiersSignalesDoc.getAdapter(DossiersSignales.class);
             List<String> currentDocumentIdList = dossiersSignales.getDossiersIds();
             List<String> newDocumentIdList = addOldDocumentToDossierSignales(Session, currentDocumentIdList, list);
             dossiersSignales.setDossiersIds(newDocumentIdList);
             dossiersSignales.save(Session);
         }
        } 
        else {
         println("dossiers signalés not found");
        }
 

		println("script ok");
		return "Fin du script de migration des dossiers signales";
