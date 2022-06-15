package fr.dila.solonepg.core.cm.work;

import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_SCHEMA;
import static fr.dila.st.core.query.QueryHelper.toPrefetchInfo;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.service.EpgDossierDistributionService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.work.SolonWork;
import java.util.ArrayList;
import java.util.Collection;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentNotFoundException;
import org.nuxeo.ecm.core.api.LifeCycleException;

public class TraitementDossiersTerminesWork extends SolonWork {
    private static final long serialVersionUID = 1859102340944514657L;

    private static final STLogger LOG = STLogFactory.getLog(TraitementDossiersTerminesWork.class);

    private final Collection<String> idDossiersTermines;

    public TraitementDossiersTerminesWork(Collection<String> idDossiersFin) {
        super();
        this.idDossiersTermines = new ArrayList<>(idDossiersFin);
    }

    @Override
    public String getTitle() {
        return "TraitementDossiersTerminesWork";
    }

    @Override
    protected void doWork() {
        openSystemSession();

        LOG.info(STLogEnumImpl.DEFAULT, "Dossiers à traiter : " + idDossiersTermines);

        DocumentModelList dossiers = session.getDocuments(idDossiersTermines, toPrefetchInfo(DOSSIER_SCHEMA));
        EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        dossiers.forEach(dos -> terminateFeuilleRoute(dossierDistributionService, session, dos));
    }

    private void terminateFeuilleRoute(
        EpgDossierDistributionService dossierDistributionService,
        CoreSession session,
        DocumentModel dossier
    ) {
        if (dossierDistributionService.processCanceledRoute(session, dossier)) {
            return;
        }

        try {
            dossierDistributionService.terminateFeuilleRoute(session, dossier);
        } catch (LifeCycleException | DocumentNotFoundException e) {
            // LifeCycleException : les étapes associées aux dossiers links peuvent être annulées ou déjà validées
            // DocumentNotFoundException : les étapes associées aux dossiers links n'existent plus

            String nor = dossier.getAdapter(Dossier.class).getNumeroNor();
            LOG.warn(
                STLogEnumImpl.WARNING_TEC,
                String.format(
                    "Une erreur est survenu lors du traitement du dossier [%s]. Message d'erreur : %s",
                    nor,
                    e.getMessage()
                )
            );
            LOG.debug(STLogEnumImpl.LOG_EXCEPTION_TEC, e);
        }
    }

    @Override
    public String getCategory() {
        return TraitementDossiersFinScheduleWork.TRAITEMENT_DOSSIERS_FIN_CATEGORY;
    }
}
