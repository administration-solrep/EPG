package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.web.activitenormative.TexteMaitreOrdonnanceActionsBean;
import fr.dila.st.web.contentview.AbstractDTOPageProvider;

/**
 * Provider pour les decrets d'application des lois de ratification de l'activite normative
 * 
 * @author asatre
 * 
 */
public class ActiviteNormativeDecretApplicationPageProvider extends AbstractDTOPageProvider{

    private static final String TEXTE_MAITRE_ORDONNANCES_ACTIONS = "texteMaitreOrdonnanceActions";
    private static final long serialVersionUID = 1L;


    protected void fillCurrentPageMapList(CoreSession coreSession) throws ClientException {
        currentItems = new ArrayList<Map<String, Serializable>>();
        currentItems.addAll(getBean().getListDecret());
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
    

    private TexteMaitreOrdonnanceActionsBean getBean(){
        Map<String, Serializable> props = getProperties();
        return (TexteMaitreOrdonnanceActionsBean) props.get(TEXTE_MAITRE_ORDONNANCES_ACTIONS);
    }
  
}
