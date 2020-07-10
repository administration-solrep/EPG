package fr.dila.solonepg.web.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.StringUtil;

@Name("selectManyCheckboxActions")
@Scope(ScopeType.CONVERSATION)
public class SelectManyCheckboxActionsBean implements Serializable {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 3852952231303589949L;

	private static final STLogger LOGGER = STLogFactory.getLog(SelectManyCheckboxActionsBean.class);
		
	private static final String LISTE_LEGISLATURES_TYPE = "listeLegislatures";
    
	@In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected transient NavigationContext navigationContext;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true)
    protected transient ResourcesAccessor resourcesAccessor;
    
    
    /**
     * Retourne le label qui sera affiché dans le selectManyMenu
     * @param type
     * @param object
     * @return
     */
    public String getItemLabel(String type, Object object) {
    	String label = "";
    	if(object != null) {
	    	if(LISTE_LEGISLATURES_TYPE.equals(type)) {
	    		label = object.toString();
	    	}
    	}
    	
    	return label;
    }
    
    /**
     * Retourne la value qui sera utilisée pour un objet pour le selectManyMenu
     * @param type
     * @param object
     * @return
     */
    public String getItemValue(String type, Object object) {
    	String value = "";
    	if(LISTE_LEGISLATURES_TYPE.equals(type)){
    		value= (String) object;
    	}
    	
    	return value;
    }
    
    /**
     * Retourne la liste des objets pour peupler le selectManyMenu
     * @param type
     * @return
     */
    public List<Object> getValues(String type) {
    	List<Object> values = new ArrayList<Object>();
    	if(LISTE_LEGISLATURES_TYPE.equals(type)){
    		try {
    			SolonEpgParametreService paramEPGservice = SolonEpgServiceLocator.getSolonEpgParametreService();
				ParametrageAN myDoc = paramEPGservice.getDocAnParametre(documentManager);
				if (myDoc != null) {
					values.addAll(myDoc.getLegislatures());
				}
			} catch (ClientException e) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, "Echec de récupération du paramétrage", e);
			}
    	}
    	
    	return values;
    }
    
    /**
     * Retourne une chaine de caractère lisible pour l'utilisateur d'une donnée en base pour l'affichage
     * @param type
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
	public String getItemLabelFromId(String type, Object object) {
    	List<String> ids = new ArrayList<String>();
    	List<String> values = new ArrayList<String>();
    	if (object instanceof String) {
    		ids.add((String) object);
    	} else if (object instanceof List) {
    		ids.addAll((List<String>) object);
    	}
    	for (String id : ids) {
	    	if(LISTE_LEGISLATURES_TYPE.equals(type)){
		    		return id;
	    	}
	    	
    	}
    	return StringUtil.join(values, ", ", "");
    }
    
        
}