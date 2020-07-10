package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.web.activitenormative.TexteMaitreActionsBean;
import fr.dila.st.web.contentview.AbstractDTOPageProvider;

/**
 * Provider pour les mesures de l'activite normative
 * 
 * @author asatre
 * 
 */
public class ActiviteNormativeDecretsPageProvider extends AbstractDTOPageProvider{

    private static final String TEXTE_MAITRE_ACTIONS = "texteMaitreActions";
    private static final long serialVersionUID = 1L;


    protected void fillCurrentPageMapList(CoreSession coreSession) throws ClientException {
        TexteMaitreActionsBean texteMaitreActionsBean = getBean();
        currentItems = new ArrayList<Map<String, Serializable>>();
        currentItems.addAll(texteMaitreActionsBean.getListDecret());
        resultsCount = currentItems.size();
    }

    private TexteMaitreActionsBean getBean(){
        Map<String, Serializable> props = getProperties();
        return (TexteMaitreActionsBean) props.get(TEXTE_MAITRE_ACTIONS);
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
    
}
