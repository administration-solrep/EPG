package fr.dila.solonepg.ui.services.actions;

import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.ss.ui.services.actions.SSDocumentRoutingActionService;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface EpgDocumentRoutingActionService extends SSDocumentRoutingActionService {
    boolean checkValiditySteps(SpecificContext context, SolonEpgFeuilleRoute route, List<DocumentModel> stepsDoc);

    boolean isStepRetourModification(SpecificContext context);

    boolean hasStepPublicationBO(SpecificContext context);

    boolean isStepAvis(SpecificContext context);

    boolean isStepContreseign(SpecificContext context);

    boolean isActiveStepInitialisation(SpecificContext context);

    boolean isActiveStepPourBonATirer(SpecificContext context);

    boolean canRetourModification(SpecificContext context);

    boolean canValideNonConcerne(SpecificContext context);

    boolean canRefusValidation(SpecificContext context);

    boolean isDossierLance(SpecificContext context);

    boolean isTypeActeTexteNonPublie(SpecificContext context);

    boolean isRapportAuParlement(SpecificContext context);

    String getPosteBdcIdFromRoute(CoreSession session, DocumentModel routeDoc);

    boolean hasRunningStep(SpecificContext context);

    boolean isStepExecutable(SpecificContext context);
}
