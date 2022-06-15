package fr.dila.solonepg.core.service;

import static fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils.doCountQuery;
import static fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils.ufnxqlToFnxqlQuery;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.constant.ModeParutionConstants;
import fr.dila.solonepg.api.constant.SolonEpgTableReferenceConstants;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.api.vocabulary.VocabularyEntry;
import fr.dila.st.core.exception.STValidationException;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentNotFoundException;
import org.nuxeo.ecm.core.api.IdRef;

/**
 * Implémentation du service pour la partie Table de référence
 *
 */
public class TableReferenceServiceImpl implements TableReferenceService {
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -5834878799760207202L;

    private static final STLogger LOG = STLogFactory.getLog(TableReferenceServiceImpl.class);

    private static final String QUERY = "SELECT tr.ecm:uuid as id FROM TableReference as tr";

    private static final String MODE_PARUTION_QUERY = "SELECT * FROM ModeParution";

    private static final String MODE_PARUTION_ACTIFS =
        "SELECT m.ecm:uuid as id FROM ModeParution as m WHERE (m.mod:dateDebut <= ? OR m.mod:dateDebut is NULL) AND (m.mod:dateFin >= ? OR m.mod:dateFin is NULL)";

    private static final String MODE_PARUTION_ACTIFS_RECHERCHE_SUR_NOM =
        "SELECT m.ecm:uuid as id FROM ModeParution as m WHERE (m.mod:dateDebut <= ? OR m.mod:dateDebut is NULL) AND (m.mod:dateFin >= ? OR m.mod:dateFin is NULL) AND m.mod:mode LIKE '";

    @Override
    public DocumentModel getTableReference(final CoreSession session) {
        final List<DocumentModel> documentList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE,
            QUERY,
            null
        );
        DocumentModel document = null;
        if (documentList == null || documentList.isEmpty()) {
            // creation d'une table de reference par defaut
            document =
                session.createDocumentModel(
                    SolonEpgTableReferenceConstants.TABLE_REFERENCE_PATH,
                    SolonEpgTableReferenceConstants.TABLE_REFERENCE_ID,
                    SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE
                );
            document = session.createDocument(document);
            session.save();
        } else {
            // il n'y a qu'un seul document Table de référence : on prend le premier élément de la liste
            document = documentList.get(0);
        }
        return document;
    }

    @Override
    public String getMinisterePrm(final CoreSession session) {
        final DocumentModel tableRef = getTableReference(session);
        final TableReference tableRefDoc = tableRef.getAdapter(TableReference.class);

        return tableRefDoc.getMinisterePM();
    }

    @Override
    public List<VocabularyEntry> getNorDirectionsForCreation(final CoreSession session) {
        final DocumentModel tableRef = getTableReference(session);
        final TableReference tableRefDoc = tableRef.getAdapter(TableReference.class);
        final List<String> directionsPM = tableRefDoc.getDirectionsPM();
        if (CollectionUtils.isEmpty(directionsPM)) {
            return new ArrayList<>();
        }
        // récupération des 3 premières lettres : par défaut PRM
        final String ministereId = tableRefDoc.getMinisterePM();
        if (StringUtils.isEmpty(ministereId)) {
            return new ArrayList<>();
        }
        final EntiteNode ministere = STServiceLocator.getSTMinisteresService().getEntiteNode(ministereId);
        String ministereNor = Optional.ofNullable(ministere).map(EntiteNode::getNorMinistere).orElse(null);
        if (ministereNor == null) {
            return new ArrayList<>();
        }
        return directionsPM
            .stream()
            .map(idDirection -> createNorDirection(idDirection, ministereNor, ministereId))
            .collect(Collectors.toList());
    }

    private static VocabularyEntry createNorDirection(String idDirection, String ministereNor, String ministereId) {
        UniteStructurelleNode usEpg = STServiceLocator
            .getSTUsAndDirectionService()
            .getUniteStructurelleNode(idDirection);
        String norDirectionFull = ministereNor + usEpg.getNorDirectionForMinistereId(ministereId);
        return new VocabularyEntry(idDirection, norDirectionFull);
    }

    @Override
    public List<VocabularyEntry> getRetoursDAN(final CoreSession session) {
        final DocumentModel tableRef = getTableReference(session);
        final TableReference tableRefDoc = tableRef.getAdapter(TableReference.class);
        final List<String> retoursDANID = tableRefDoc.getRetourDAN();

        if (retoursDANID == null) {
            return Collections.emptyList();
        }
        LOG.info(session, STLogEnumImpl.LOG_INFO_TEC, "retoursDAN Size : " + retoursDANID.size());
        final List<VocabularyEntry> vocRetoursDAN = new ArrayList<>();
        STPostesService stPostesService = STServiceLocator.getSTPostesService();

        for (final String idPosteDAN : retoursDANID) {
            final PosteNode posteNode = stPostesService.getPoste(idPosteDAN);

            LOG.info(session, STLogEnumImpl.LOG_INFO_TEC, "Poste ID en paramètre : " + idPosteDAN);

            if (posteNode != null) {
                final String posteLabel = posteNode.getLabel();
                LOG.info(session, STLogEnumImpl.LOG_INFO_TEC, posteNode);
                vocRetoursDAN.add(new VocabularyEntry(idPosteDAN, posteLabel));
            } else {
                LOG.warn(session, STLogEnumImpl.FAIL_GET_POSTE_TEC, "Le poste " + idPosteDAN + " n'a pas été trouvé");
            }
        }

        return vocRetoursDAN;
    }

    @Override
    public List<DocumentModel> getModesParutionList(CoreSession session) {
        // On récupère tous les modes de parution existants en base
        return new ArrayList<>(QueryUtils.doQuery(session, MODE_PARUTION_QUERY, 0, 0));
    }

    @Override
    public List<DocumentModel> getModesParutionActifsList(CoreSession session) {
        StringBuilder query = new StringBuilder(MODE_PARUTION_ACTIFS);
        Calendar today = Calendar.getInstance();
        Object[] params = { today, today };
        // On récupère tous les modes de parution existants en base
        List<String> modesParutionsIdsActifs = QueryUtils.doUFNXQLQueryForIdsList(session, query.toString(), params);
        return QueryUtils.retrieveDocuments(
            session,
            ModeParutionConstants.MODE_PARUTION_DOCUMENT_TYPE,
            modesParutionsIdsActifs
        );
    }

    @Override
    public DocumentModel initModeParution(CoreSession session) {
        DocumentModel modelDesired = session.createDocumentModel(ModeParutionConstants.MODE_PARUTION_DOCUMENT_TYPE);
        modelDesired.setPathInfo(SolonEpgTableReferenceConstants.TABLE_REFERENCE_PATH, UUID.randomUUID().toString());
        return modelDesired;
    }

    @Override
    public DocumentModel createModeParution(CoreSession session, ModeParution mode) {
        return saveModeParution(mode, session::createDocument);
    }

    @Override
    public String getModeParutionLabel(CoreSession session, String id) {
        if (StringUtils.isBlank(id)) {
            return "";
        }

        try {
            DocumentModel modeDoc = session.getDocument(new IdRef(id));
            return modeDoc.getAdapter(ModeParution.class).getMode();
        } catch (DocumentNotFoundException e) {
            LOG.warn(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, e);
            return id;
        }
    }

    @Override
    public DocumentModel updateModeParution(CoreSession session, ModeParution mode) {
        return saveModeParution(mode, session::saveDocument);
    }

    private DocumentModel saveModeParution(ModeParution mode, UnaryOperator<DocumentModel> operator) {
        if (StringUtils.isBlank(mode.getMode())) {
            throw new STValidationException(
                ResourceHelper.getString("admin.tables.reference.mode.parution.mode.obligatoire")
            );
        }

        return operator.apply(mode.getDocument());
    }

    @Override
    public void deleteModeParution(CoreSession session, ModeParution mode) {
        if (this.isModeParutionUtilise(session, mode)) {
            throw new STValidationException(ResourceHelper.getString("admin.tables.reference.mode.parution.utilise"));
        }

        session.removeDocument(new IdRef(mode.getId()));
    }

    private boolean isModeParutionUtilise(CoreSession session, ModeParution mode) {
        return (
            doCountQuery(
                session,
                ufnxqlToFnxqlQuery("SELECT d.ecm:uuid as id FROM Dossier as d WHERE d.retdila:modeParution = ?"),
                new Object[] { mode.getId() }
            ) >
            0L
        );
    }

    @Override
    public String getPostePublicationId(CoreSession session) {
        final DocumentModel tableRef = getTableReference(session);
        final TableReference tableRefDoc = tableRef.getAdapter(TableReference.class);
        return tableRefDoc.getPostePublicationId();
    }

    @Override
    public String getPosteDanId(CoreSession session) {
        final DocumentModel tableRef = getTableReference(session);
        final TableReference tableRefDoc = tableRef.getAdapter(TableReference.class);
        return tableRefDoc.getPosteDanId();
    }

    @Override
    public String getPosteDanIdAvisRectificatifCE(CoreSession session) {
        final DocumentModel tableRef = getTableReference(session);
        final TableReference tableRefDoc = tableRef.getAdapter(TableReference.class);
        return tableRefDoc.getPosteDanIdAvisRectificatifCE();
    }

    @Override
    public List<String> getModesParutionListFromName(CoreSession session, String name) {
        // On récupère tous les modes de parution existants en base qui ont comme nom le name qu'on passe dans la
        // méthode
        final String query = MODE_PARUTION_ACTIFS_RECHERCHE_SUR_NOM + name + "'";
        Calendar today = Calendar.getInstance();
        Object[] params = { today, today };
        // On récupère tous les modes de parution existants en base
        List<String> modesParutionsIdsActifs = QueryUtils.doUFNXQLQueryForIdsList(session, query.toString(), params);
        return modesParutionsIdsActifs;
    }
}
