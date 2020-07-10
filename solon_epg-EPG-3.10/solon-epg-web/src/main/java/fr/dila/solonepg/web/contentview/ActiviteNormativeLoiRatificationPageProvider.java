package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.web.activitenormative.TexteMaitreOrdonnanceActionsBean;
import fr.dila.st.web.contentview.AbstractDTOPageProvider;
import fr.dila.st.web.contentview.HiddenColumnPageProvider;

/**
 * Provider pour les lois de ratification de l'activite normative
 * 
 * @author asatre
 * 
 */
public class ActiviteNormativeLoiRatificationPageProvider extends AbstractDTOPageProvider implements HiddenColumnPageProvider{

    private static final String TEXTE_MAITRE_ORDONNANCES_ACTIONS = "texteMaitreOrdonnanceActions";
    private static final long serialVersionUID = 1L;


    @Override
	protected void fillCurrentPageMapList(CoreSession coreSession) throws ClientException {
        currentItems = new ArrayList<Map<String, Serializable>>();
        currentItems.addAll(getBean().getListLoiRatification());
        resultsCount = currentItems.size();
    }

    private TexteMaitreOrdonnanceActionsBean getBean(){
        Map<String, Serializable> props = getProperties();
        return (TexteMaitreOrdonnanceActionsBean) props.get(TEXTE_MAITRE_ORDONNANCES_ACTIONS);
    }

	@Override
	public Boolean isHiddenColumn(String isHidden) throws ClientException {
		if(StringUtils.isNotEmpty(isHidden)){
			return !getBean().getCurrentOrdonnanceDTO().isDispositionHabilitation().equals(Boolean.parseBoolean(isHidden));
		}
		return Boolean.FALSE;
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
