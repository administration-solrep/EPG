package fr.dila.solonepg.core.operation.livraison;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.PathRef;

/**
 * Opération permettant de mettre à jour les mesures du PAN. Les mesures d'application des lois reçoivent leur marqueur,
 * ainsi que celles d'habilitations Les mesures qui sont à la fois applications des lois et habilitations sont
 * dupliquées et séparées dans un de chaque
 *
 */
@Operation(
    id = SplitMesurePANOperation.ID,
    category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY,
    label = "Mise à jour des mesures d'application des lois et d'habilitations du PAN",
    description = SplitMesurePANOperation.DESCRIPTION
)
public class SplitMesurePANOperation {
    /**
     * Identifiant technique de l'opération.
     */
    public static final String ID = "SolonEpg.Livraison.SplitMesurePan";

    public static final String DESCRIPTION =
        "Cette opération initialise les mesures du pan pour séparer les mesures d'habilitations et les mesures d'application des lois";

    @Context
    protected OperationContext context;

    @Context
    protected CoreSession session;

    private static final STLogger LOGGER = STLogFactory.getLog(SplitMesurePANOperation.class);

    @OperationMethod
    public void run() {
        // récupération des lois
        final String queryLois =
            "select a.ecm:uuid as id from ActiviteNormative as a where a.norma:applicationLoi='1' OR a.norma:ordonnance38C = '1'";
        final List<DocumentModel> loisDocList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE,
            queryLois,
            null
        );
        LOGGER.info(session, STLogEnumImpl.COUNT_DOCUMENT_TEC, queryLois + " => " + loisDocList.size());
        final Set<String> idsAlreadyUpdated = new HashSet<String>();

        int compteur = 0;
        for (DocumentModel loiDoc : loisDocList) {
            final TexteMaitre texteMaitre = loiDoc.getAdapter(TexteMaitre.class);
            final List<String> idsMesureLoi = texteMaitre.getMesuresIds();
            final List<String> idsHabilitationLoi = texteMaitre.getHabilitationIds();

            // traitement habilitation
            if (idsHabilitationLoi != null && !idsHabilitationLoi.isEmpty()) {
                boolean mesureListUpdated = false;
                for (String idHabilitation : idsHabilitationLoi) {
                    // gestion des mesures présentes dans les deux listes
                    if (idsMesureLoi != null && idsMesureLoi.contains(idHabilitation)) {
                        // On duplique le document d'habilitation
                        final DocumentModel copyHabilitation = session.copy(
                            new IdRef(idHabilitation),
                            new PathRef(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_PATH),
                            null
                        );
                        // On retire des mesures et remplace par la copie
                        idsMesureLoi.remove(idHabilitation);
                        idsMesureLoi.add(copyHabilitation.getId());
                        mesureListUpdated = true;
                    }
                    // traitement habilitation
                    final IdRef refHabilitationDoc = new IdRef(idHabilitation);
                    if (!idsAlreadyUpdated.contains(idHabilitation) && session.exists(refHabilitationDoc)) {
                        final DocumentModel habilitationDoc = session.getDocument(refHabilitationDoc);
                        final ActiviteNormative habilitation = habilitationDoc.getAdapter(ActiviteNormative.class);
                        habilitation.setOrdonnance38C(ActiviteNormativeConstants.MESURE_FILTER);
                        idsAlreadyUpdated.add(idHabilitation);
                        habilitation.save(session);
                    }
                }
                if (mesureListUpdated) {
                    texteMaitre.setMesuresIds(idsMesureLoi);
                    texteMaitre.save(session);
                }
            }

            // traitement mesure
            if (idsMesureLoi != null && !idsMesureLoi.isEmpty()) {
                for (String idMesure : idsMesureLoi) {
                    // traitement mesure
                    final IdRef refMesureDoc = new IdRef(idMesure);
                    if (!idsAlreadyUpdated.contains(idMesure) && session.exists(refMesureDoc)) {
                        final DocumentModel mesureDoc = session.getDocument(refMesureDoc);
                        final ActiviteNormative mesure = mesureDoc.getAdapter(ActiviteNormative.class);
                        mesure.setApplicationLoi(ActiviteNormativeConstants.MESURE_FILTER);
                        idsAlreadyUpdated.add(idMesure);
                        mesure.save(session);
                    }
                }
            }
            ++compteur;
            if (compteur % 100 == 0) {
                LOGGER.info(session, STLogEnumImpl.COUNT_DOCUMENT_TEC, "Document traités : " + compteur);
            }
        }
        LOGGER.info(session, STLogEnumImpl.COUNT_DOCUMENT_TEC, "Document traités : " + compteur);
        session.save();
    }
}
