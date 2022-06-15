package fr.dila.solonepg.elastic.batch.work;

import com.google.common.base.Joiner;
import fr.dila.cm.cases.CaseConstants;
import fr.dila.cm.cases.CaseDistribConstants;
import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.elastic.batch.IIndexationService;
import fr.dila.solonepg.elastic.models.indexing.IndexationDossiersBatchStatistics;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.work.SolonWork;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.schema.PrefetchInfo;
import org.nuxeo.runtime.api.Framework;

public class IndexationWork extends SolonWork {
    /**
     *
     */
    private static final long serialVersionUID = 1146299980000833612L;

    public static final String QUEUE_ID = "IndexationQueue";

    private static final String INDEXATION_WORK_TITLE = "Indexation d'un lot de dossier";

    /**
     * Logger géré dans un fichier à part.
     */
    private static final STLogger LOGGER = STLogFactory.getLog("INDEXATION-BATCH");

    /** prefetch des champs pour les dossiers */
    private final String prefetchDossier = Joiner
        .on(",")
        .join(
            new String[] {
                "common",
                CaseConstants.CASE_SCHEMA,
                STSchemaConstant.DUBLINCORE_SCHEMA,
                STSchemaConstant.NOTE_SCHEMA,
                CaseDistribConstants.DISTRIBUTION_SCHEMA,
                ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
                DossierSolonEpgConstants.DOSSIER_SCHEMA,
                RetourDilaConstants.RETOUR_DILA_SCHEMA,
                TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA
            }
        );

    private final List<String> ids;

    private final boolean deletion;

    public IndexationWork(List<String> ids, String login) {
        this(ids, login, false);
    }

    public IndexationWork(List<String> ids, String login, boolean deletion) {
        super();
        Objects.requireNonNull(ids);
        this.deletion = deletion;
        this.ids = new ArrayList<>(ids);
        setOriginatingUsername(login);
    }

    @Override
    public String getTitle() {
        return INDEXATION_WORK_TITLE;
    }

    @Override
    protected void doWork() {
        openUserSession();
        doReindexLot(this.ids, deletion);
    }

    /**
     * Traitement d'un lot ; initialisation du contexte thread (session, statistiques),
     * récupération des données ( {@link DocumentModel} ), traitement dossier par dossier,
     * et nettoyage du contexte.
     *
     * Pour chaque lot, on ouvre un contexte login, une transaction et une session qu'on ferme.
     */
    private void doReindexLot(List<String> lot, boolean deletion) {
        IIndexationService indexationService = Framework.getService(IIndexationService.class);
        long startTick = System.currentTimeMillis();
        try {
            // contexte statistiques
            //IndexationDossiersBatchStatistics.set(IndexationDossiersBatchStatistics.create());

            // contexte session + récupération des données dossier
            DocumentModelList documentModelList = getListDocument(session, lot);

            BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
            Map<String, String> vecteurPublications = Stream
                .of(
                    bulletinOfficielService.getAllActifVecteurPublicationIdIntitulePair(session),
                    bulletinOfficielService.getAllBulletinOfficielIdIntitulePair(session)
                )
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1));

            // traitement dossier par dossier
            for (DocumentModel documentModel : documentModelList) {
                // sortie rapide si thread interrompu
                if (Thread.interrupted()) {
                    LOGGER.warn(
                        EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC,
                        "Interruption d'un lot d'indexation; arrêt du traitement du lot"
                    );
                    //                    // restore interrupt flag
                    Thread.currentThread().interrupt();
                    //                    return false;
                }

                indexationService.doIndexDossier(session, vecteurPublications, documentModel, deletion);
            }

            // logging du lot
            doLogLot(startTick, documentModelList.size());
        } catch (RuntimeException e) {
            LOGGER.warn(
                EpgLogEnumImpl.FAIL_PROCESS_B_EXPORT_DOSSIERS_TEC,
                e,
                "Interruption d'un lot par une exception non attendue"
            );
        } finally {
            IndexationDossiersBatchStatistics.unset();
        }
    }

    /**
     * Logging sur l'indexation d'un lot
     */
    private void doLogLot(long startTick, int lotSize) {
        long stopTick = System.currentTimeMillis();
        long lotElapsed = TimeUnit.MILLISECONDS.toSeconds(stopTick - startTick);
        LOGGER.info(
            EpgLogEnumImpl.PROCESS_B_EXPORT_DOSSIERS_TEC,
            String.format(
                "Export des dossiers: %d dossiers - %d s. - %.2f/s. (lot)",
                lotSize,
                lotElapsed, // durée du lot
                (float) lotSize / lotElapsed // vitesse du lot
            )
        ); // vitesse moyenne
    }

    private DocumentModelList getListDocument(CoreSession session, List<String> lot) {
        PrefetchInfo prefetch = new PrefetchInfo(prefetchDossier);
        return session.getDocuments(lot, prefetch);
    }
}
