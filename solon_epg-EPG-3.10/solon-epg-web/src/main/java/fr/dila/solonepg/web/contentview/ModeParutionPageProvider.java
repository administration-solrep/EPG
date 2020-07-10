package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.web.admin.parametres.TablesReferenceActionsBean;
import fr.dila.st.web.contentview.AbstractDTOPageProvider;

/**
 * Provider pour les habilitations de l'activite normative (suivi des habilitations)
 * 
 * @author asatre
 * 
 */
public class ModeParutionPageProvider extends AbstractDTOPageProvider {

    /**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 576246731451251365L;
	
	private static final String TABLE_REFERENCE_ACTIONS_BEAN = "tablesReferenceActions";

    @Override
    protected void fillCurrentPageMapList(CoreSession coreSession) throws ClientException {
        currentItems = new ArrayList<Map<String, Serializable>>();
        currentItems.addAll(getBean().getListModeParution());
        resultsCount = currentItems.size();

    }

    private TablesReferenceActionsBean getBean() {
        Map<String, Serializable> props = getProperties();
        return (TablesReferenceActionsBean) props.get(TABLE_REFERENCE_ACTIONS_BEAN);
    }

}
