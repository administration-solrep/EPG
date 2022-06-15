package fr.dila.solonmgpp.core.event;

import fr.dila.solonepg.api.client.InjectionEpgGvtDTO;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonmgpp.api.constant.SolonMgppEventConstants;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.ss.api.client.InjectionGvtDTO;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitEventListener;

public class InjectionEPPListener implements PostCommitEventListener {
    private static final STLogger LOGGER = STLogFactory.getLog(InjectionEPPListener.class);

    @Override
    public void handleEvent(EventBundle events) {
        if (!events.containsEventName(SolonMgppEventConstants.INJECTION_EPP_PREPARATION)) {
            return;
        }

        Iterator<Event> iterator = events.iterator();
        boolean foundEvent = false;
        Event event = null;
        while (iterator.hasNext() && !foundEvent) {
            Event curEvent = iterator.next();
            if (SolonMgppEventConstants.INJECTION_EPP_PREPARATION.equalsIgnoreCase(curEvent.getName())) {
                event = curEvent;
                foundEvent = true;
            }
        }

        if (event != null) {
            final EventContext ctx = event.getContext();
            final CoreSession session = ctx.getCoreSession();
            StringBuilder injectionError = (StringBuilder) ctx
                .getProperties()
                .get(SolonMgppEventConstants.INJECTION_ERROR);

            try {
                LOGGER.info(
                    session,
                    STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC,
                    "Début de l'injection via évènement asynchrone (partie mapping epp)"
                );
                @SuppressWarnings("unchecked")
                List<InjectionGvtDTO> listInjection = (List<InjectionGvtDTO>) ctx
                    .getProperties()
                    .get(SolonMgppEventConstants.LIST_INJECTION_EPP_PARAM);
                @SuppressWarnings("unchecked")
                List<InjectionEpgGvtDTO> listCompared = (List<InjectionEpgGvtDTO>) ctx
                    .getProperties()
                    .get(SolonMgppEventConstants.LIST_COMPARAISON_EPP_PARAM);
                SolonMgppServiceLocator
                    .getMGPPInjectionGouvernementService()
                    .createComparedDTO(session, listInjection, listCompared);
                LOGGER.info(
                    session,
                    STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC,
                    "Fin de préparation de l'injection (partie mapping epp) "
                );
            } catch (Exception e) {
                LOGGER.info(
                    session,
                    STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC,
                    "Fin de préparation de l'injection (partie mapping epp) suite à une erreur"
                );
                if (e instanceof EPGException) {
                    LOGGER.warn(session, STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC, e.getMessage());
                    LOGGER.debug(session, STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC, e.getStackTrace());
                } else {
                    LOGGER.warn(session, STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC, e);
                }

                if (!StringUtils.isBlank(injectionError.toString())) {
                    injectionError.delete(0, injectionError.length());
                }
                injectionError.append(e.getMessage());
            }
        }
    }
}
