package fr.dila.solonepg.core.cm.work;

import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getEPGFeuilleRouteService;

import fr.dila.solonepg.api.service.EPGFeuilleRouteService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.work.SolonWork;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteElement;
import java.util.ArrayList;
import java.util.Collection;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

public class TraitementDossiersAbandonnesWork extends SolonWork {
    private static final long serialVersionUID = -7636074249624809728L;

    private static final STLogger LOG = STLogFactory.getLog(TraitementDossiersAbandonnesWork.class);

    private final Collection<String> idsFeuilleRoutes;

    public TraitementDossiersAbandonnesWork(Collection<String> idsFeuilleRoutes) {
        super();
        this.idsFeuilleRoutes = new ArrayList<>(idsFeuilleRoutes);
    }

    @Override
    public String getTitle() {
        return "TraitementDossiersAbandonnesWork";
    }

    @Override
    protected void doWork() {
        openSystemSession();

        LOG.info(
            STLogEnumImpl.DEFAULT,
            "Feuilles de route à traiter pour les dossiers abandonnés : " + idsFeuilleRoutes
        );

        EPGFeuilleRouteService feuilleRouteService = getEPGFeuilleRouteService();
        idsFeuilleRoutes.forEach(idRoute -> processFeuilleRoute(feuilleRouteService, idRoute));
    }

    private void processFeuilleRoute(EPGFeuilleRouteService feuilleRouteService, String idRoute) {
        IdRef routeRef = new IdRef(idRoute);
        if (idRoute == null || !session.exists(routeRef)) {
            return;
        }

        DocumentModel route = session.getDocument(routeRef);
        if (route.getAdapter(FeuilleRouteElement.class).isCanceled()) {
            SolonEpgServiceLocator.getDossierDistributionService().processCanceledRoute(session, route);
        } else {
            feuilleRouteService.getRunningSteps(session, idRoute).forEach(this::setStepBackToReady);
        }
    }

    private void setStepBackToReady(DocumentModel step) {
        step.getAdapter(FeuilleRouteElement.class).backToReady(session);
    }

    @Override
    public String getCategory() {
        return TraitementDossiersFinScheduleWork.TRAITEMENT_DOSSIERS_FIN_CATEGORY;
    }
}
