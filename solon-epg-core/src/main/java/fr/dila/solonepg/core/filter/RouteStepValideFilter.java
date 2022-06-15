package fr.dila.solonepg.core.filter;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.ss.api.constant.SSFeuilleRouteConstant;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;

/**
 * Ce filtre conserve uniquement les étapes terminées "avec validation", et où une action a été nécessaire
 * (hormis "pour information", "pour impression" etc).
 *
 * @author jtremeaux
 */
public class RouteStepValideFilter implements Filter {
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    @Override
    public boolean accept(DocumentModel doc) {
        SSRouteStep routeStep = doc.getAdapter(SSRouteStep.class);

        // Vérifie le type de validation
        final String validationStatus = routeStep.getValidationStatus();
        if (!SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE.equals(validationStatus)) {
            return false;
        }

        // Vérifie le type d'étape
        final String routingTaskType = routeStep.getType();
        if (
            VocabularyConstants.ROUTING_TASK_TYPE_INITIALISATION.equals(routingTaskType) ||
            VocabularyConstants.ROUTING_TASK_TYPE_IMPRESSION.equals(routingTaskType) ||
            VocabularyConstants.ROUTING_TASK_TYPE_INFORMATION.equals(routingTaskType)
        ) {
            return false;
        }

        return true;
    }
}
