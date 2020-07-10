package fr.dila.solonmgpp.web.tree;

import java.io.Serializable;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonmgpp.api.organigramme.InstitutionNode;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;

/**
 * Classe de gestion de l'arbre contenant les institutions.
 * 
 * @author asatre
 */
@Name("institutionTree")
@Scope(ScopeType.PAGE)
public class InstitutionTreeBean implements Serializable {

    private static final long serialVersionUID = 5759515357444678567L;

    private Boolean visible = Boolean.FALSE;
    
    private List<InstitutionNode> rootNodes;
    
    /**
     * Méthode qui renvoie l'organigramme complet.
     * 
     * @return l'organigramme chargé
     * @throws ClientException
     */
    public List<InstitutionNode> getRootNodes(boolean  filter) throws ClientException {
        if (rootNodes == null) {
            rootNodes = loadTree(filter);
        }
        return rootNodes;
    }
    
    /**
     * charge l'arbre contenant l'organigramme des institutions
     * 
     * @throws ClientException
     */
    private List<InstitutionNode> loadTree(boolean filter) throws ClientException {
        if(filter) {
          rootNodes = SolonMgppServiceLocator.getInstitutionService().findFilteredInstitution();
        }
        else {
          rootNodes = SolonMgppServiceLocator.getInstitutionService().findAllInstitution();
        }
        return rootNodes;
    }
    
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean isVisible() {
        return visible;
    }
    

}
