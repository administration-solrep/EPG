package fr.dila.solonepg.web.filter;

import java.io.Serializable;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

/**
 * WebBean qui permet de filtrer les types d'acte de type Décret.
 *
 * @author arolin
 */
@Name("typeActeDecretFilter")
@Scope(ScopeType.EVENT)
public class TypeActeDecretFilterBean implements Filter, Serializable {
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    @In(required = true, create = true)
    protected NuxeoPrincipal currentUser;

    @Override
    public boolean accept(DocumentModel doc) {
        final ActeService acteService = SolonEpgServiceLocator.getActeService();
        
        //filtre sur les décrets
        if(acteService.isDecret(doc.getId())){
            
            //filtre sur les mesures nominatives
            if (acteService.hasTypeActeMesureNominative(doc.getId())) {
                final List<String> groups = currentUser.getGroups();
                return groups.contains(SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_READER);
            }   
            return true;
        }
        return false;
    }
}
