package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.web.activitenormative.TexteMaitreHabilitationActionsBean;
import fr.dila.st.web.contentview.AbstractDTOPageProvider;

/**
 * Provider pour les habilitations de l'activite normative (suivi des habilitations)
 * 
 * @author asatre
 * 
 */
public class HabilitationsPageProvider extends AbstractDTOPageProvider {

    private static final String TEXTE_MAITRE_HABILITATION_ACTIONS = "texteMaitreHabilitationActions";

    private static final long serialVersionUID = 1L;

    @Override
    protected void fillCurrentPageMapList(CoreSession coreSession) throws ClientException {
        currentItems = new ArrayList<Map<String, Serializable>>();
        currentItems.addAll(getBean().getListHabilitation());
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
    
    private TexteMaitreHabilitationActionsBean getBean() {
        Map<String, Serializable> props = getProperties();
        return (TexteMaitreHabilitationActionsBean) props.get(TEXTE_MAITRE_HABILITATION_ACTIONS);
    }

}
