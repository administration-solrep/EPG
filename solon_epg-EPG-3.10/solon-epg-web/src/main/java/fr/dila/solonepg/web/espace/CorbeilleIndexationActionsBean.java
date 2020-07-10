package fr.dila.solonepg.web.espace;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.ss.api.security.principal.SSPrincipal;

/**
 * Bean SEAM permettant de gérer les actions de la corbeille d'indexation.
 * 
 * @author bgamard
 * @author jtremeaux
 */
@Name("corbeilleIndexationActions")
@Scope(ScopeType.EVENT)
public class CorbeilleIndexationActionsBean implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @In(required = true, create = true)
    protected SSPrincipal ssPrincipal;

    public boolean isVisibleCorbeilleIndexation() {
        return ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.CORBEILLE_INDEXATION_READER);
    }
    
    /**
     * Vrai si l'utilisateur peut modifier l'indexation de ce dossier.
     * 
     * @param dossierDoc Dossier
     * @return Vrai si l'utilisateur peut modifier l'indexation de ce dossier
     */
    public boolean isDossierIndexationUpdatable(DocumentModel dossierDoc) {
        return ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.CORBEILLE_INDEXATION_READER);
    }
}
