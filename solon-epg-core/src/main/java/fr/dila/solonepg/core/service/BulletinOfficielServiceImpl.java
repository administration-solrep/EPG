package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.administration.BulletinOfficiel;
import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.constant.SolonEpgBulletinOfficielConstants;
import fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants;
import fr.dila.solonepg.api.enumeration.EpgVecteurPublication;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.ss.api.migration.MigrationDetailModel;
import fr.dila.ss.api.migration.MigrationDiscriminatorConstants;
import fr.dila.ss.api.migration.MigrationLoggerModel;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.core.exception.STValidationException;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringHelper;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.SortInfo;

public class BulletinOfficielServiceImpl implements BulletinOfficielService {
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 5029667528412231120L;

    private static final String SELECT_ID = "SELECT v.ecm:uuid as id FROM VecteurPublication as v";

    private static final String SELECT_ID_LABEL =
        "SELECT v.ecm:uuid as id, v.vp:vpIntitule as label FROM VecteurPublication as v";

    private static final String GET_ALL_ACTIVE_QUERY =
        "SELECT * FROM BulletinOfficiel where boEtat != 'deleted' and ecm:isProxy = 0 ";

    private static final String SELECT_BO_ID_LABEL =
        "SELECT b.ecm:uuid as id, b.bulletin_officiel:boIntitule as label FROM BulletinOfficiel as b";

    private static final String GET_ALL_ACTIVE_VECTORS_QUERY =
        SELECT_ID + " where (v.vp:vpDebut IS NULL OR v.vp:vpDebut <= ?) AND (v.vp:vpFin IS NULL OR v.vp:vpFin >= ? )";

    private static final String GET_VECTORS_QUERY_FROM_POS = SELECT_ID + " where v.vp:vpPos = ?";

    private static final String GET_VECTORS_QUERY_FROM_LABEL = SELECT_ID + " where v.vp:vpIntitule LIKE ?";

    private static final STLogger LOGGER = STLogFactory.getLog(BulletinOfficielServiceImpl.class);

    @Override
    public DocumentModelList getAllActiveBulletinOfficielOrdered(final CoreSession session) {
        return session.query(GET_ALL_ACTIVE_QUERY);
    }

    @Override
    public DocumentModelList getAllActiveBulletinOfficielOrdered(final CoreSession session, List<SortInfo> sorts) {
        String orders = sorts
            .stream()
            .map(s -> String.format("%s %s", s.getSortColumn(), s.getSortAscending() ? "ASC" : "DESC"))
            .collect(Collectors.joining(" "));

        String query = orders.isEmpty() ? GET_ALL_ACTIVE_QUERY : GET_ALL_ACTIVE_QUERY + "ORDER BY " + orders;
        return session.query(query);
    }

    @Override
    public void create(final CoreSession session, final Integer idMinistere, final String intitule) {
        EntiteNode nodeEntite = STServiceLocator.getSTMinisteresService().getEntiteNode(String.valueOf(idMinistere));
        if (nodeEntite == null) {
            throw new STValidationException("admin.bulletin.officiel.error.nor");
        }

        create(session, nodeEntite.getNorMinistere(), intitule);
    }

    @Override
    public void create(final CoreSession session, final String nor, final String intitule) {
        final DocumentModel modelDesired = session.createDocumentModel(
            SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_PATH,
            UUID.randomUUID().toString(),
            SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_DOCUMENT_TYPE
        );
        DublincoreSchemaUtils.setTitle(modelDesired, intitule.toUpperCase());

        final BulletinOfficiel bulletinOfficiel = modelDesired.getAdapter(BulletinOfficiel.class);
        bulletinOfficiel.setNor(nor);
        bulletinOfficiel.setIntitule(intitule);
        bulletinOfficiel.setEtat("running");

        session.createDocument(bulletinOfficiel.getDocument());
        session.save();
    }

    @Override
    public void delete(final CoreSession session, final String identifier) {
        final DocumentModel docModel = session.getDocument(new IdRef(identifier));
        if (docModel != null) {
            final BulletinOfficiel bulletinOfficiel = docModel.getAdapter(BulletinOfficiel.class);
            // desactivation du document
            bulletinOfficiel.setEtat("deleted");
            session.saveDocument(bulletinOfficiel.getDocument());
            session.save();
        }
    }

    @Override
    public List<DocumentModel> getAllBulletinOfficielOrdered(
        final DocumentModel currentDocument,
        final CoreSession session,
        final List<String> bulletinIds
    ) {
        if (currentDocument != null && currentDocument.getTitle() != null && currentDocument.getTitle().length() >= 3) {
            return getBulletinOfficielQueryFromNor(
                session,
                currentDocument.getTitle().substring(0, 3),
                Boolean.TRUE,
                bulletinIds
            );
        } else {
            return new ArrayList<>();
        }
    }

    private List<DocumentModel> getBulletinOfficielQueryFromNor(
        final CoreSession session,
        final String ministereNor,
        final Boolean ordered,
        final List<String> bulletinIds
    ) {
        final List<Object> params = new ArrayList<>();

        final StringBuffer queryBuilder = new StringBuffer();
        queryBuilder.append("SELECT b.ecm:uuid as id FROM BulletinOfficiel as b where b.bulletin_officiel:boEtat = ? ");
        params.add("running");

        if (ministereNor != null && ministereNor.length() >= 3) {
            queryBuilder.append("AND b.bulletin_officiel:boNor = ? ");
            params.add(ministereNor);
        } else {
            return new ArrayList<>();
        }

        if (CollectionUtils.isNotEmpty(bulletinIds)) {
            queryBuilder.append("OR b.ecm:uuid IN ('");
            queryBuilder.append(String.join("','", bulletinIds));
            queryBuilder.append("') ");
        }

        if (ordered) {
            queryBuilder.append("ORDER BY b.bulletin_officiel:boNor, b.bulletin_officiel:boIntitule");
        }

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            "BulletinOfficiel",
            queryBuilder.toString(),
            params.toArray()
        );
    }

    @Override
    public void migrerBulletinOfficiel(
        final CoreSession session,
        final String ancienNorMinistere,
        final String nouveauNorMinistere,
        final MigrationLoggerModel migrationLoggerModel
    ) {
        migrationLoggerModel.setBulletinOfficiel(0);
        SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationLogger(migrationLoggerModel);

        final List<DocumentModel> bulletinOfficielToRename = getBulletinOfficielQueryFromNor(
            session,
            ancienNorMinistere
        );

        if (bulletinOfficielToRename == null || bulletinOfficielToRename.isEmpty()) {
            migrationLoggerModel.setBulletinOfficielCount(0);
            migrationLoggerModel.setBulletinOfficielCurrent(0);
            SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationLogger(migrationLoggerModel);
            final MigrationDetailModel migrationDetailModel = SolonEpgServiceLocator
                .getChangementGouvernementService()
                .createMigrationDetailFor(
                    migrationLoggerModel,
                    MigrationDiscriminatorConstants.BO,
                    "Aucun bulletin officiel à migrer"
                );
            migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
            SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationDetail(migrationDetailModel);
        } else {
            LOGGER.info(
                session,
                EpgLogEnumImpl.UPDATE_BULLETIN_OFF_TEC,
                bulletinOfficielToRename.size() + " bulletin officiel à mettre à jour."
            );

            migrationLoggerModel.setBulletinOfficielCount(bulletinOfficielToRename.size());

            int index = 0;
            migrationLoggerModel.setBulletinOfficielCurrent(index);
            SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationLogger(migrationLoggerModel);

            for (final DocumentModel documentModel : bulletinOfficielToRename) {
                final BulletinOfficiel bulletinOfficiel = documentModel.getAdapter(BulletinOfficiel.class);

                final String nor = bulletinOfficiel.getNor();
                final String intitule = bulletinOfficiel.getIntitule();

                final MigrationDetailModel migrationDetailModel = SolonEpgServiceLocator
                    .getChangementGouvernementService()
                    .createMigrationDetailFor(
                        migrationLoggerModel,
                        MigrationDiscriminatorConstants.BO,
                        "Migration du bulletin officiel " +
                        nor +
                        " " +
                        intitule +
                        " (Nouveau nor : " +
                        nouveauNorMinistere +
                        ")"
                    );

                bulletinOfficiel.setNor(nouveauNorMinistere);
                bulletinOfficiel.save(session);

                migrationLoggerModel.setBulletinOfficielCurrent(++index);
                SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationLogger(migrationLoggerModel);

                migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
                SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationDetail(migrationDetailModel);
            }
        }

        migrationLoggerModel.setBulletinOfficiel(1);
        SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationLogger(migrationLoggerModel);
    }

    @Override
    public List<DocumentModel> getBulletinOfficielQueryFromNor(final CoreSession session, final String ministereNor) {
        return getBulletinOfficielQueryFromNor(session, ministereNor, Boolean.FALSE, Collections.emptyList());
    }

    @Override
    public DocumentModel initVecteurPublication(CoreSession session) {
        LOGGER.info(session, EpgLogEnumImpl.INIT_VECTEUR_PUB_TEC);
        DocumentModel modelDesired = session.createDocumentModel(
            SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE
        );
        modelDesired.setPathInfo(
            SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_PATH,
            UUID.randomUUID().toString()
        );
        VecteurPublication vecteurPublication = modelDesired.getAdapter(VecteurPublication.class);
        vecteurPublication.setIntitule("");
        return modelDesired;
    }

    @Override
    public void saveVecteurPublication(CoreSession session, VecteurPublication vecteur, boolean create) {
        if (!isValid(vecteur)) {
            throw new STValidationException("admin.vecteur.publication.not.valid");
        }

        List<DocumentModel> persistedVecteurs = this.getAllVecteurPublication(session);
        if (
            persistedVecteurs
                .stream()
                .filter(p -> !p.getId().equals(vecteur.getId()))
                .map(d -> d.getAdapter(VecteurPublication.class))
                .anyMatch(v -> Objects.equals(vecteur.getIntitule(), v.getIntitule()))
        ) {
            throw new STValidationException("admin.vecteur.publication.doublon");
        }

        if (create) {
            DocumentModel persistedVecteur = session.createDocument(vecteur.getDocument());
            LOGGER.info(session, EpgLogEnumImpl.CREATE_VECTEUR_PUB_TEC, persistedVecteur);
        } else {
            DocumentModel persistedVecteur = session.saveDocument(vecteur.getDocument());
            LOGGER.info(session, EpgLogEnumImpl.UPDATE_VECTEUR_PUB_TEC, persistedVecteur);
        }
        session.save();
    }

    private boolean isValid(VecteurPublication vecteur) {
        return (
            StringUtils.isNotBlank(vecteur.getIntitule()) ||
            ObjectUtils.anyNotNull(vecteur.getDateDebut(), vecteur.getDateFin())
        );
    }

    @Override
    public List<DocumentModel> getAllVecteurPublication(final CoreSession session) {
        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE,
            SELECT_ID,
            null
        );
    }

    @Override
    public List<DocumentModel> getAllVecteurPublication(final CoreSession session, List<SortInfo> sorts) {
        String orders = sorts
            .stream()
            .map(s -> String.format("v.vp:%s %s", s.getSortColumn(), s.getSortAscending() ? "ASC" : "DESC"))
            .collect(Collectors.joining(" "));

        String query = orders.isEmpty() ? SELECT_ID : SELECT_ID + " ORDER BY " + orders;
        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE,
            query,
            null
        );
    }

    @Override
    public Map<String, String> getAllVecteurPublicationIdIntitulePair(CoreSession session) {
        return getIdIntitulePairFromQuery(session, SELECT_ID_LABEL);
    }

    @Override
    public Map<String, String> getAllBulletinOfficielIdIntitulePair(CoreSession session) {
        return getIdIntitulePairFromQuery(session, SELECT_BO_ID_LABEL);
    }

    private Map<String, String> getIdIntitulePairFromQuery(CoreSession session, String query) {
        List<ImmutablePair<String, String>> pairs = QueryUtils.doUFNXQLQueryAndMapping(
            session,
            query,
            null,
            (Map<String, Serializable> rowData) ->
                new ImmutablePair<>((String) rowData.get("id"), (String) rowData.get("label"))
        );

        return pairs.stream().collect(Collectors.toMap(ImmutablePair::getLeft, ImmutablePair::getRight));
    }

    @Override
    public List<DocumentModel> getAllActifVecteurPublication(CoreSession documentManager) {
        // On veut 2 variables qui correspondent à maintenant car la date de début doit être antérieur à maintenant et
        // celle de fin doit être postérieure
        Calendar today = Calendar.getInstance();

        Object[] params = { today, today };

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            documentManager,
            SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE,
            GET_ALL_ACTIVE_VECTORS_QUERY,
            params
        );
    }

    @Override
    public Map<String, String> getAllActifVecteurPublicationIdIntitulePair(CoreSession session) {
        String query =
            SELECT_ID_LABEL +
            " where (v.vp:vpDebut IS NULL OR v.vp:vpDebut <= ?) AND (v.vp:vpFin IS NULL OR v.vp:vpFin >= ? )";

        // date début <= maintenant et date fin >= maintenant
        Calendar today = Calendar.getInstance();
        Object[] params = { today, today };

        List<ImmutablePair<String, String>> pairs = QueryUtils.doUFNXQLQueryAndMapping(
            session,
            query,
            params,
            (Map<String, Serializable> rowData) ->
                new ImmutablePair<>((String) rowData.get("id"), (String) rowData.get("label"))
        );

        return pairs.stream().collect(Collectors.toMap(ImmutablePair::getLeft, ImmutablePair::getRight));
    }

    @Override
    public boolean isAllVecteurPublicationActif(CoreSession documentManager, List<String> idsVecteur) {
        final StringBuilder queryBuilder = new StringBuilder();
        final List<Object> params = new ArrayList<>();

        // On cherche les vecteurs dont la période de validité inclut la date courante
        Calendar dateCourante = Calendar.getInstance();

        params.add(dateCourante);
        params.add(dateCourante);
        queryBuilder.append(GET_ALL_ACTIVE_VECTORS_QUERY);
        queryBuilder.append("AND v.ecm:uuid IN (");
        queryBuilder.append(StringHelper.join(idsVecteur, ",", "'"));
        queryBuilder.append(")");

        List<DocumentModel> lstVecteursPublication = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            documentManager,
            SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE,
            queryBuilder.toString(),
            params.toArray()
        );

        return lstVecteursPublication.size() == idsVecteur.size();
    }

    @Override
    public boolean isAllBulletinOfficielActif(CoreSession documentManager, List<String> names) {
        DocumentModelList docModelList = getAllActiveBulletinOfficielOrdered(documentManager);
        List<String> bulletins = new ArrayList<>(names);
        // On enlève les bulletins de la liste au fur et à mesure qu'on les trouve dans la liste des bulletins actifs
        for (DocumentModel docModel : docModelList) {
            String intitule = docModel.getAdapter(BulletinOfficiel.class).getIntitule();
            bulletins.remove(intitule);
        }
        return bulletins.isEmpty();
    }

    @Override
    public String getLibelleForJO(CoreSession documentManager) {
        return getLibelleFromPos(documentManager, 1);
    }

    private String getLibelleFromPos(CoreSession documentManager, Integer pos) {
        final StringBuffer queryBuilder = new StringBuffer();
        final List<Object> params = new ArrayList<>();

        params.add(pos);
        queryBuilder.append(GET_VECTORS_QUERY_FROM_POS);

        List<DocumentModel> lstDoc = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            documentManager,
            SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE,
            queryBuilder.toString(),
            params.toArray()
        );
        if (lstDoc.isEmpty()) {
            return "";
        } else {
            return lstDoc.get(0).getId();
        }
    }

    @Override
    public String getIdForLibelle(CoreSession session, EpgVecteurPublication vecteurPublication) {
        if (vecteurPublication == null) {
            return "";
        }

        return getIdForLibelle(session, vecteurPublication.getIntitule());
    }

    @Override
    public String getIdForLibelle(CoreSession documentManager, String libelle) {
        final List<Object> params = new ArrayList<>();

        params.add(libelle);

        List<DocumentModel> lstDoc = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            documentManager,
            SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE,
            GET_VECTORS_QUERY_FROM_LABEL,
            params.toArray()
        );
        if (lstDoc.isEmpty()) {
            return "";
        } else {
            return lstDoc.get(0).getId();
        }
    }

    @Override
    public List<String> convertVecteurIdListToLabels(CoreSession documentManager, List<String> idVecteurs) {
        List<String> lstVecteursConvertis = new ArrayList<>();

        if (idVecteurs != null && !idVecteurs.isEmpty()) {
            for (String idVecteur : idVecteurs) {
                try {
                    VecteurPublication vecteur = documentManager
                        .getDocument(new IdRef(idVecteur))
                        .getAdapter(VecteurPublication.class);
                    if (vecteur != null) {
                        lstVecteursConvertis.add(vecteur.getIntitule());
                    } else {
                        lstVecteursConvertis.add(idVecteur);
                    }
                } catch (Exception e) {
                    lstVecteursConvertis.add(idVecteur);
                }
            }
        }
        return lstVecteursConvertis;
    }

    @Override
    public String convertVecteurIdToLabel(CoreSession documentManager, String idVecteur) {
        if (idVecteur != null) {
            try {
                VecteurPublication vecteur = documentManager
                    .getDocument(new IdRef(idVecteur))
                    .getAdapter(VecteurPublication.class);
                if (vecteur != null) {
                    return vecteur.getIntitule();
                } else {
                    return idVecteur;
                }
            } catch (Exception e) {
                return idVecteur;
            }
        }
        return idVecteur;
    }
}
