package fr.dila.solonepg.web.feuilleroute;

import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;

/**
 * WebBean qui permet de filtrer les types d'étape de feuille de route.
 * Prend en paramètre des documents éléments du vocabulaire routing_task.
 *
 * @author jtremeaux
 */
@Name("routingTaskFilter")
@Scope(ScopeType.EVENT)
public class RoutingTaskFilterBean implements Filter {
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    @In(required = true, create = true)
    protected NuxeoPrincipal currentUser;

    @Override
    public boolean accept(DocumentModel doc) {
        return accept(doc.getId());
    }

    public boolean accept(String routingTaskType) {
        
        boolean result  = true ;
        // L'étape "Pour initialisation" ne peut pas être créée
        if (VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION.equals(routingTaskType)) {
            result = false;
        }else if (VocabularyConstants.ROUTING_TASK_TYPE_SIGNATURE.equals(routingTaskType)) {
         // L'étape "Pour signature" ne peut pas être créée (règle temporaire RG-INS-FDR-26)
            result = false;
        }else if (VocabularyConstants.ROUTING_TASK_TYPE_BON_A_TIRER.equals(routingTaskType)) {
            final List<String> groups = currentUser.getGroups();
            result = groups.contains(SolonEpgBaseFunctionConstant.ETAPE_BON_A_TIRER_CREATOR);
        }else if (VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE.equals(routingTaskType)) {
            final List<String> groups = currentUser.getGroups();
            result =groups.contains(SolonEpgBaseFunctionConstant.ETAPE_FOURNITURE_EPREUVE_CREATOR);
        }else if (VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(routingTaskType)) {
         // Filtre de l'étape "Pour publication à la DILA JO" en fonction des droits
            final List<String> groups = currentUser.getGroups();
            result = groups.contains(SolonEpgBaseFunctionConstant.ETAPE_PUBLICATION_DILA_JO_CREATOR);
        }        
        return result;
    }
}
