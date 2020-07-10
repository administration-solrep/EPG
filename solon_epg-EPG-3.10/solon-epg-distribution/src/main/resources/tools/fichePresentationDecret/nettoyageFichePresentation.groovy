import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;

import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.core.dto.CritereRechercheDTOImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.core.query.QueryUtils;

    //STEP1 : Get All Event Names of MGPP DECRET 
	
    String procedure = "mgpp_decret" ;
    List<String> evtNames = new ArrayList<String>() ; 
    List<EvenementTypeDescriptor> evenementTypeList = SolonMgppServiceLocator.getEvenementTypeService().findEvenementTypeByProcedure(procedure);
    for(EvenementTypeDescriptor evenementTypeDescriptor : evenementTypeList){
      evtNames.add(evenementTypeDescriptor.getName()) ;
    }
    
    //STEP2 : Prepare The search criteria
    
    CritereRechercheDTO critereRechercheDTO = new CritereRechercheDTOImpl();
    critereRechercheDTO.setTypeEvenement(evtNames);

    //STEP3 : Call the WS to get all the messages in order to prepare the of available nor list
    
    List<MessageDTO> list = SolonMgppServiceLocator.getRechercheService().findMessage(critereRechercheDTO, Session);
    List<String> norList = new ArrayList<String>();

    for (MessageDTO messageDTO : list) {
      String nor = messageDTO.getIdDossier();
      if (!norList.contains(nor)) {
        norList.add(nor);
      }
    }

    //STEP4 : Get all available FichePresentationDecret
    
    StringBuilder query = new StringBuilder("SELECT d.ecm:uuid as id FROM FichePresentationDecret as d");

    final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(Session, query.toString(), null);


    //STEP5 : Filter the documents and remove   
    int index = 0 ;
    for (DocumentModel documentModel : docs) {
      FichePresentationDecret  fichePresentationDecret = documentModel.getAdapter(FichePresentationDecret.class);
    
      if (!norList.contains(fichePresentationDecret.getNor())) {        
        index++ ;
        //STEP6 : Remove The File
        try{
          Session.removeDocument(documentModel.getRef()) ;
        }catch (Exception e) {
            print "erreur de suppression "+e.getMessage();
    	}         
      }
    }
    

print "-------------------------------------------------------------------------------";
print "Fin du script groovy de netoyage des fiches de presentation Decret";
print "........." + index + "  Documents ont ete supprimes ..........";
return "return Fin du script groovy" ;
