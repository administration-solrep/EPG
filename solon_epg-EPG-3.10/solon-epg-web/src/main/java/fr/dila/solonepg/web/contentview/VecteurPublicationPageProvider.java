package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.web.admin.parametres.BulletinOfficielActionsBean;
import fr.dila.st.web.contentview.AbstractDTOPageProvider;

/**
 * Provider pour les vecteurs de bulications (présents dans la page des bulletins officiel)
 * 
 * @author ahullot
 * 
 */
public class VecteurPublicationPageProvider extends AbstractDTOPageProvider{

    private static final String VECTEUR_PUBLICATION_ACTIONS = "bulletinOfficielActions";
    private static final long serialVersionUID = 1L;


    protected void fillCurrentPageMapList(CoreSession coreSession) throws ClientException {
    	BulletinOfficielActionsBean vecteurPubliBean = getBean();
        currentItems = new ArrayList<Map<String, Serializable>>();
        
        currentItems.addAll(vecteurPubliBean.getLstVecteurs());
        
        resultsCount = currentItems.size();
    }

    private BulletinOfficielActionsBean getBean(){
        Map<String, Serializable> props = getProperties();
        return (BulletinOfficielActionsBean) props.get(VECTEUR_PUBLICATION_ACTIONS);
    }    
  
}
