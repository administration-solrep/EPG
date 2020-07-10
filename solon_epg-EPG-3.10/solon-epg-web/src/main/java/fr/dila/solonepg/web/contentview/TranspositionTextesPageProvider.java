package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.web.activitenormative.TranspositionDirectiveActionsBean;
import fr.dila.st.web.contentview.AbstractDTOPageProvider;

/**
 * Provider pour les textes de transpostion de l'activite normative
 * 
 * @author asatre
 * 
 */
public class TranspositionTextesPageProvider extends AbstractDTOPageProvider{

    private static final String TRANSPOSITION_DIRECTIVE_ACTIONS = "transpositionDirectiveActions";
    private static final long serialVersionUID = 1L;


    @Override
    protected void fillCurrentPageMapList(CoreSession coreSession) throws ClientException {
        currentItems = new ArrayList<Map<String, Serializable>>();
        currentItems.addAll(getBean().getListTexteTranspositionDTO());
        resultsCount = currentItems.size();
        
    }
    
    @Override
    public List<Map<String, Serializable>> getCurrentPage() {
        super.getCurrentPage();
        if(currentItems.size()>0){
        	for(Map<String,Serializable> tmpItem : currentItems){
        		tmpItem.put("validate", true);
        	}
        }
        return currentItems;
    }
    

    private TranspositionDirectiveActionsBean getBean(){
        Map<String, Serializable> props = getProperties();
        return (TranspositionDirectiveActionsBean) props.get(TRANSPOSITION_DIRECTIVE_ACTIONS);
    }
  
}
