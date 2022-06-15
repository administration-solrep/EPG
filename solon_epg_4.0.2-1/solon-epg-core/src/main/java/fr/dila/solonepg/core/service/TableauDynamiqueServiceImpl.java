package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants;
import fr.dila.solonepg.api.enums.FavorisRechercheType;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.recherche.TableauDynamique;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.solonepg.api.service.TableauDynamiqueService;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.StringHelper;
import fr.sword.naiad.nuxeo.ufnxql.core.query.FlexibleQueryMaker;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;

/**
 *
 * @author asatre
 *
 */
public class TableauDynamiqueServiceImpl implements TableauDynamiqueService {
    private static final long serialVersionUID = -7648584505613656603L;

    /**
     * Préfixe ajouté à l'identifiant technique du noeud de type ministère.
     */
    //	private static final String		PREFIX_MIN			= STConstant.PREFIX_MIN;

    /**
     * Préfixe ajouté à l'identifiant technique du noeud de type unité structurelle.
     */
    public static final String PREFIX_US = STConstant.PREFIX_US;

    /**
     * Préfixe ajouté à l'identifiant technique du noeud.
     */
    public static final String PREFIX_POSTE = STConstant.PREFIX_POSTE;

    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(TableauDynamiqueServiceImpl.class);

    @Override
    public List<DocumentModel> findAll(final CoreSession session) {
        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT DISTINCT td.ecm:uuid as id FROM TableauDynamique as td");
        queryBuilder.append(" WHERE td.dc:creator = ? ");
        queryBuilder.append(" OR td.tabdyn:usersNames = ? ");

        final String userName = session.getPrincipal().getName();
        final Object params[] = new Object[] { userName, userName };

        return QueryUtils.doUnrestrictedUFNXQLQueryAndFetchForDocuments(session, queryBuilder.toString(), params);
    }

    @Override
    public List<DocumentModel> findMine(final CoreSession session) {
        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT DISTINCT td.ecm:uuid as id FROM TableauDynamique as td");
        queryBuilder.append(" WHERE td.dc:creator = ? ");
        queryBuilder.append(" AND td.tabdyn:usersNames = ? ");

        final String userName = session.getPrincipal().getName();
        final Object params[] = new Object[] { userName, userName };

        return QueryUtils.doUnrestrictedUFNXQLQueryAndFetchForDocuments(session, queryBuilder.toString(), params);
    }

    @Override
    public Map<Integer, String> createTableauDynamique(
        final CoreSession session,
        final FavorisRechercheType type,
        final String query,
        final List<String> idDestinataires,
        final String intitule
    ) {
        List<String> splitedDestinataireIds = new ArrayList<>();
        List<String> destinataires = ObjectHelper.requireNonNullElseGet(idDestinataires, ArrayList::new);
        for (String destinataireId : destinataires) {
            if (!destinataireId.matches("^[0-9]{8}$")) {
                String[] splitDestinataireId = destinataireId.split("-");
                splitedDestinataireIds.add(splitDestinataireId[splitDestinataireId.length - 1]);
            } else {
                splitedDestinataireIds.add(destinataireId);
            }
        }
        final Map<Integer, Set<String>> users = getUsersName(session, splitedDestinataireIds, null);
        if (!users.get(0).isEmpty()) {
            final DocumentModel tabModel = session.createDocumentModel(
                SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_PATH,
                UUID.randomUUID().toString(),
                SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_DOCUMENT_TYPE
            );
            DublincoreSchemaUtils.setTitle(tabModel, intitule);

            final TableauDynamique tableauDynamique = tabModel.getAdapter(TableauDynamique.class);
            tableauDynamique.setType(type);
            tableauDynamique.setQuery(query);
            tableauDynamique.setUsers(new ArrayList<String>(users.get(0)));
            tableauDynamique.setDestinatairesId(destinataires);

            session.createDocument(tableauDynamique.getDocument());
            session.save();
        }

        final Map<Integer, String> result = new HashMap<Integer, String>();
        result.put(0, StringHelper.join(users.get(0).toArray(new String[users.get(0).size()]), ", ", ""));
        result.put(1, StringHelper.join(users.get(1).toArray(new String[users.get(1).size()]), ", ", ""));
        return result;
    }

    @Override
    public Map<Integer, String> updateTableauDynamique(
        final CoreSession session,
        final DocumentModel tabModel,
        final String query,
        final List<String> idDestinataires,
        final String intitule
    ) {
        final Map<Integer, String> result = new HashMap<Integer, String>();
        if (tabModel != null && tabModel.hasSchema(SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_SCHEMA)) {
            DublincoreSchemaUtils.setTitle(tabModel, intitule);

            final TableauDynamique tableauDynamique = tabModel.getAdapter(TableauDynamique.class);
            tableauDynamique.setQuery(query);

            List<String> splitedDestinataireIds = new ArrayList<String>();
            List<String> destinataires = ObjectHelper.requireNonNullElseGet(idDestinataires, ArrayList::new);
            for (String destinataireId : destinataires) {
                if (!destinataireId.matches("^[0-9]{8}$")) {
                    String[] splitDestinataireId = destinataireId.split("-");
                    splitedDestinataireIds.add(splitDestinataireId[splitDestinataireId.length - 1]);
                } else {
                    splitedDestinataireIds.add(destinataireId);
                }
            }

            final Map<Integer, Set<String>> users = getUsersName(session, splitedDestinataireIds, null);
            tableauDynamique.setUsers(new ArrayList<String>(users.get(0)));
            tableauDynamique.setDestinatairesId(destinataires);

            session.saveDocument(tableauDynamique.getDocument());
            session.save();

            result.put(0, StringHelper.join(users.get(0).toArray(new String[users.get(0).size()]), ", ", ""));
            result.put(1, StringHelper.join(users.get(1).toArray(new String[users.get(1).size()]), ", ", ""));
            return result;
        }

        result.put(0, "");
        result.put(1, "");
        return result;
    }

    private Map<Integer, Set<String>> getUsersName(
        final CoreSession session,
        final List<String> idDestinataires,
        final String tabModelId
    ) {
        final Map<Integer, Set<String>> map = new HashMap<Integer, Set<String>>();
        final Set<String> usersName = new HashSet<String>();
        usersName.add(session.getPrincipal().getName());

        final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
        final STUsAndDirectionService usService = STServiceLocator.getSTUsAndDirectionService();
        final STPostesService postesService = STServiceLocator.getSTPostesService();
        final EpgOrganigrammeService orgaService = SolonEpgServiceLocator.getEpgOrganigrammeService();

        // on ajoute les user du poste ou de l'unité
        if (idDestinataires != null) {
            for (final String idDestinataire : idDestinataires) {
                // à partir d'ici c'est à revoir : l'idéal est de récupérer le type du noeuf demandé
                Object e = orgaService.getOrganigrammeNodeById(idDestinataire, OrganigrammeType.MINISTERE);
                if (e != null) {
                    // ministere
                    usersName.addAll(ministeresService.findUserFromMinistere(idDestinataire));
                } else {
                    e = orgaService.getOrganigrammeNodeById(idDestinataire, OrganigrammeType.UNITE_STRUCTURELLE);
                    if (e != null) {
                        // Unite structurelle
                        usersName.addAll(usService.findUserFromUniteStructurelle(idDestinataire));
                    } else {
                        e = orgaService.getOrganigrammeNodeById(idDestinataire, OrganigrammeType.POSTE);
                        if (e != null) {
                            // Poste
                            usersName.addAll(postesService.getUserNamesFromPoste(idDestinataire));
                        } else {
                            throw new NuxeoException("Impossible de retrouve le type de noeud pour " + idDestinataire);
                        }
                    }
                }
            }
        }

        final ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();

        final ParametrageApplication parametrageApplication = parametreApplicationService.getParametreApplicationDocument(
            session
        );

        final Set<String> resultOk = new HashSet<String>();
        final Set<String> resultKo = new HashSet<String>();
        if (parametrageApplication != null) {
            for (final String userName : usersName) {
                // limitation du nombre de Tableaux Dynamiques par user
                if (
                    getCountForUser(session, userName, tabModelId).intValue() <
                    parametrageApplication.getNbTableauxDynamiques().intValue()
                ) {
                    resultOk.add(userName);
                } else {
                    resultKo.add(userName);
                }
            }
        }
        map.put(0, resultOk);
        map.put(1, resultKo);
        return map;
    }

    private Long getCountForUser(final CoreSession session, final String userName, final String tabModelId) {
        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT DISTINCT td.ecm:uuid as id FROM TableauDynamique as td");
        queryBuilder.append(" WHERE ( td.dc:creator = ? ");
        queryBuilder.append(" OR td.tabdyn:usersNames = ? ) ");

        if (tabModelId == null) {
            final Object params[] = new Object[] { userName, userName };
            return QueryUtils.doCountQuery(
                session,
                FlexibleQueryMaker.KeyCode.UFXNQL.getKey() + queryBuilder.toString(),
                params
            );
        } else {
            queryBuilder.append(" AND td.ecm:uuid != ? ");
            final Object params[] = new Object[] { userName, userName, tabModelId };
            return QueryUtils.doCountQuery(
                session,
                FlexibleQueryMaker.KeyCode.UFXNQL.getKey() + queryBuilder.toString(),
                params
            );
        }
    }

    @Override
    public Boolean userIsUnderQuota(final CoreSession session, final String userName) {
        final ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
        final ParametrageApplication parametrageApplication = parametreApplicationService.getParametreApplicationDocument(
            session
        );

        if (
            getCountForUser(session, userName, null).intValue() <
            parametrageApplication.getNbTableauxDynamiques().intValue()
        ) {
            return true;
        } else return false;
    }

    @Override
    public void deleteTableauDynamique(final CoreSession session, final DocumentModel tabModel) {
        LOGGER.info(session, EpgLogEnumImpl.DEL_TAB_DYN_TEC, tabModel);
        session.removeDocument(tabModel.getRef());
        session.save();
    }
}
