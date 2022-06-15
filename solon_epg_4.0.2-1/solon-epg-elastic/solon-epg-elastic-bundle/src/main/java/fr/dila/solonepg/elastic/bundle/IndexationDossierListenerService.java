package fr.dila.solonepg.elastic.bundle;

import static fr.dila.st.core.service.STServiceLocator.getEtatApplicationService;
import static fr.dila.st.core.util.CoreSessionUtil.getRepo;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.elastic.batch.IIndexationService;
import fr.dila.solonepg.elastic.batch.work.IndexationWork;
import fr.dila.solonepg.elastic.persistence.service.IIndexationPersistenceService;
import fr.dila.st.api.constant.STConfigConstants;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.constant.FeuilleRouteConstant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.nuxeo.ecm.core.api.CoreInstance;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentNotFoundException;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.core.work.api.WorkManager;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * Traitement d'un événement d'indexation
 */
public class IndexationDossierListenerService extends DefaultComponent implements IIndexationDossierListenerService {
    private static final STLogger LOGGER = STLogFactory.getLog(IndexationDossierListenerService.class);

    private static final List<OrganigrammeType> ALLOWED_ORGANIGRAMME_TYPES = ImmutableList.of(
        OrganigrammeType.DIRECTION,
        OrganigrammeType.MINISTERE
    );

    private IIndexationPersistenceService indexationPersistenceService;

    private IIndexationService indexationService;

    public IndexationDossierListenerService() {
        super();
    }

    @Override
    public void handleEvent(EventBundle events) {
        // L'ouverture de la transaction est gérée par l'appelant

        Set<String> dossiersModifies = new HashSet<>();
        Set<String> dossiersSupprimes = new HashSet<>();

        CoreInstance.doPrivileged(
            getRepo(),
            coreSession -> {
                StreamSupport
                    .stream(events.spliterator(), false)
                    .filter(evt -> evt.getContext() instanceof DocumentEventContext)
                    .forEach(evt -> handleEvent(coreSession, evt, dossiersModifies, dossiersSupprimes));
            }
        );

        CoreInstance.doPrivileged(
            getRepo(),
            coreSession -> {
                StreamSupport
                    .stream(events.spliterator(), false)
                    .filter(evt -> STEventConstant.ORGANIGRAMME_NODE_MODIFIED.equals(evt.getName()))
                    .forEach(evt -> handleOrganigrammeModifiedEvent(coreSession, evt, dossiersModifies));
            }
        );

        if (!dossiersModifies.isEmpty()) {
            LOGGER.info(
                EpgLogEnumImpl.INFO_INDEXATION_TEC,
                String.format("indexation post-commit event: %d dossier(s) marqué(s)", dossiersModifies.size())
            );
        }
        if (dossiersModifies.isEmpty()) {
            LOGGER.debug(EpgLogEnumImpl.INFO_INDEXATION_TEC, "indexation post-commit event: aucun dossier marqué");
        }

        dossiersModifies.removeAll(dossiersSupprimes);

        if (!dossiersSupprimes.isEmpty()) {
            LOGGER.debug(
                EpgLogEnumImpl.INFO_INDEXATION_TEC,
                String.format("indexation post-commit event: dossiers marqué supprimé: %s", dossiersSupprimes)
            );
            dossiersSupprimes.forEach(indexationPersistenceService::markIndexationContinueSuppression);
            indexDossiers(dossiersSupprimes, true);
        }

        if (!dossiersModifies.isEmpty()) {
            LOGGER.debug(
                EpgLogEnumImpl.INFO_INDEXATION_TEC,
                String.format("indexation post-commit event: dossiers à indexés: %s", dossiersModifies)
            );
            dossiersModifies.forEach(indexationPersistenceService::markIndexationContinueModification);
            indexDossiers(dossiersModifies, false);
        }
    }

    private void indexDossiers(Collection<String> ids, boolean deletion) {
        if (
            indexationService.isIndexationContinueEnabled() &&
            !getEtatApplicationService().isApplicationTechnicallyRestricted()
        ) {
            ConfigService configService = STServiceLocator.getConfigService();

            WorkManager workManager = Framework.getService(WorkManager.class);
            Iterables
                .partition(ids, indexationService.getQueryLimit())
                .forEach(
                    listIds ->
                        workManager.schedule(
                            new IndexationWork(listIds, configService.getValue(STConfigConstants.NUXEO_BATCH_USER)),
                            deletion
                        )
                );
        } else {
            LOGGER.debug(EpgLogEnumImpl.INFO_INDEXATION_TEC, "Indexation continue désactivé");
        }
    }

    private void handleEvent(
        CoreSession coreSession,
        Event event,
        Collection<String> dossiersModifies,
        Collection<String> dossiersSupprimes
    ) {
        EventContext ctx = event.getContext();
        if (ctx instanceof DocumentEventContext) {
            DocumentEventContext docCtx = (DocumentEventContext) ctx;
            DocumentModel doc = docCtx.getSourceDocument();

            try {
                filterEvent(coreSession, event, dossiersModifies, dossiersSupprimes, doc);
            } catch (NuxeoException e) {
                if (e.getCause() instanceof DocumentNotFoundException) {
                    // catch if document is not existing
                    // example: when documents are cleaned, they are updated
                    // then deleted
                    LOGGER.info(
                        EpgLogEnumImpl.INFO_INDEXATION_TEC,
                        String.format(
                            "Traitement de l'événement interrompu car document %s non existant",
                            doc.getRef().reference()
                        )
                    );
                } else {
                    LOGGER.error(EpgLogEnumImpl.INFO_INDEXATION_TEC, e, "Error during message processing");
                }
            }
        }
    }

    private void filterEvent(
        CoreSession coreSession,
        Event event,
        Collection<String> dossiersModifies,
        Collection<String> dossiersSupprimes,
        DocumentModel doc
    ) {
        if (
            DocumentEventTypes.DOCUMENT_REMOVED.equals(event.getName()) &&
            doc.getType().equals(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE)
        ) {
            dossiersSupprimes.add(doc.getId());
        } else {
            DocumentRef ref = doc.getRef();
            DocumentModel document = coreSession.getDocument(ref);
            DocumentModel dossier = null;
            while (!document.getPath().isRoot()) {
                if (document.getType().equals(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE)) {
                    dossier = document;
                    break;
                } else if (document.getType().equals(FeuilleRouteConstant.TYPE_FEUILLE_ROUTE)) {
                    FeuilleRoute feuilleRoute = document.getAdapter(FeuilleRoute.class);
                    List<String> dossiersIds = coreSession
                        .getDocuments(feuilleRoute.getAttachedDocuments(), null)
                        .stream()
                        .filter(
                            attachedDoc -> attachedDoc.getType().equals(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE)
                        )
                        .map(DocumentModel::getId)
                        .collect(Collectors.toList());
                    dossiersModifies.addAll(dossiersIds);
                    return;
                } else {
                    ref = document.getParentRef();
                    document = coreSession.getDocument(ref);
                }
            }
            if (dossier != null) {
                dossiersModifies.add(dossier.getId());
            }
        }
    }

    private void handleOrganigrammeModifiedEvent(
        CoreSession coreSession,
        Event event,
        Collection<String> dossiersModifies
    ) {
        EventContext ctx = event.getContext();
        OrganigrammeNode node = (OrganigrammeNode) ctx
            .getProperties()
            .get(STEventConstant.ORGANIGRAMME_NODE_MODIFIED_PARAM_NODE);
        OrganigrammeNode oldNode = (OrganigrammeNode) ctx
            .getProperties()
            .get(STEventConstant.ORGANIGRAMME_NODE_MODIFIED_PARAM_OLD_NODE);

        if (
            node == null ||
            oldNode == null ||
            !ALLOWED_ORGANIGRAMME_TYPES.contains(node.getType()) ||
            Objects.equals(node.getLabel(), oldNode.getLabel())
        ) {
            return;
        }

        if (node.getType() == OrganigrammeType.DIRECTION) {
            DossierService dossierService = SolonEpgServiceLocator.getDossierService();
            dossiersModifies.addAll(dossierService.findDossierIdsFromDirectionAttache(coreSession, node));
            dossiersModifies.addAll(dossierService.findDossierIdsFromDirectionResp(coreSession, node));
        }

        if (node.getType() == OrganigrammeType.MINISTERE) {
            DossierService dossierService = SolonEpgServiceLocator.getDossierService();
            dossiersModifies.addAll(dossierService.findDossierIdsFromMinistereAttache(coreSession, node));
            dossiersModifies.addAll(dossierService.findDossierIdsFromMinistereResp(coreSession, node));
        }
    }

    @Override
    public void activate(ComponentContext arg0) {
        indexationService = Framework.getService(IIndexationService.class);
        indexationPersistenceService = Framework.getService(IIndexationPersistenceService.class);
    }

    @Override
    public void deactivate(ComponentContext arg0) {
        indexationService = null;
        indexationPersistenceService = null;
    }
}
