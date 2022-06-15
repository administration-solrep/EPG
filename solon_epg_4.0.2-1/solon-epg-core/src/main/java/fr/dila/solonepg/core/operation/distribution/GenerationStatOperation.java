package fr.dila.solonepg.core.operation.distribution;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.st.core.service.STServiceLocator;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

/**
 * Opération appellée pour générer un event de génération de stat.
 *
 * @author ahullot
 */
@Operation(
    id = GenerationStatOperation.ID,
    category = "Export",
    label = "Génération d'une statistique d'EPG",
    description = "Génération d'une statistique d'EPG."
)
public class GenerationStatOperation {
    /**
     * Identifiant technique de l'opération.
     */
    public static final String ID = "SolonEpg.Stat.Generation";

    @Context
    protected OperationContext context;

    @Param(name = "idRapport")
    protected String idRapport;

    @OperationMethod
    public void launchEvent() throws Exception {
        CoreSession session = context.getCoreSession();

        // On envoie un évenement qui va générer la stat
        EventProducer eventProducer = STServiceLocator.getEventProducer();
        Map<String, Serializable> eventProperties = new HashMap<>();
        eventProperties.put("idRapport", idRapport);
        InlineEventContext inlineEventContext = new InlineEventContext(
            session,
            session.getPrincipal(),
            eventProperties
        );
        eventProducer.fireEvent(inlineEventContext.newEvent(SolonEpgEventConstant.BATCH_EVENT_STAT_ONE_GENERATION));
    }
}
