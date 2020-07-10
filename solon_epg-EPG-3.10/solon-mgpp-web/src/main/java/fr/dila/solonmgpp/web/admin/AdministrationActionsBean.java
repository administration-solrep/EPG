package fr.dila.solonmgpp.web.admin;

import static org.jboss.seam.annotations.Install.FRAMEWORK;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.web.admin.tablereferenceepp.TableReferenceEppTreeBean;

/**
 * Bean Seam de l'espace d'administration.
 * 
 * @author asatre
 */
@Name("administrationActions")
@Scope(ScopeType.SESSION)
@Install(precedence = FRAMEWORK + 2)
public class AdministrationActionsBean extends fr.dila.solonepg.web.admin.AdministrationActionsBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 779661162141314134L;

    @In(create = true)
    protected transient TableReferenceEppTreeBean tableReferenceEppTree;
    /**
     * Navigue vers l'ecran de configuration d'epp.
     * 
     * @return
     * @throws ClientException
     */
    public String navigateToParametreMGPP() throws ClientException {
        initEspaceAdministration();

        return SolonMgppViewConstant.ESPACE_PARAMETRAGE_MGPP;
    }

    public String navigateToTableRefEPP() throws ClientException {
      // Initialise le contexte de l'espace d'administration
      initEspaceAdministration();
      return tableReferenceEppTree.navigateToTableRefEPP();
  }
}
