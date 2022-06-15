package fr.dila.solonmgpp.core.service;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepp.rest.api.WSEpp;
import fr.dila.solonmgpp.api.constant.SolonMgppBaseFunctionConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppCorbeilleConstant;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.dto.CorbeilleDTO;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.enumeration.CorbeilleTypeObjet;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.CorbeilleService;
import fr.dila.solonmgpp.api.tree.CorbeilleNode;
import fr.dila.solonmgpp.core.descriptor.CorbeilleDescriptor;
import fr.dila.solonmgpp.core.descriptor.CorbeilleNodeDescriptor;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.solonmgpp.core.dto.CorbeilleDTOImpl;
import fr.dila.solonmgpp.core.util.WSErrorHelper;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STDossierLinkConstant;
import fr.dila.st.api.constant.STQueryConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.dossier.STDossier.DossierState;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.rest.client.HttpTransactionException;
import fr.dila.st.rest.client.WSProxyFactoryException;
import fr.sword.naiad.nuxeo.ufnxql.core.query.FlexibleQueryMaker;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epp.Action;
import fr.sword.xsd.solon.epp.ChercherCorbeilleRequest;
import fr.sword.xsd.solon.epp.ChercherCorbeilleResponse;
import fr.sword.xsd.solon.epp.Corbeille;
import fr.sword.xsd.solon.epp.HasCommunicationNonTraiteesResponse.CorbeilleInfos;
import fr.sword.xsd.solon.epp.Section;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * Implementation de {@link CorbeilleService}
 *
 * @author asatre
 *
 */
public class CorbeilleServiceImpl extends DefaultComponent implements CorbeilleService {
    private static final String SELECT_DECMUUID_FROM = "SELECT d.ecm:uuid as id FROM ";

    private static final String SELECT_DOSSIER_FROM = "SELECT d.ecm:uuid as id FROM %s as d ";

    private static final String SELECT_FICHE_FROM_WHERE_DATE =
        "SELECT f.ecm:uuid as id FROM %s as f WHERE f.%s:dateFin IS NULL OR f.%s:dateFin > ?";

    private static final String SELECT_DR_FROM =
        String.format(SELECT_DOSSIER_FROM, CorbeilleTypeObjet.FICHE_DR.getDocTypeName()) +
        "WHERE d.fpdr:rapportParlement != 'RAPPORT_RELATIF_ARTICLE_67_LOI_N_2004-1343'";

    private static final String SELECT_DR67_FROM =
        String.format(SELECT_DOSSIER_FROM, CorbeilleTypeObjet.FICHE_DR_67.getDocTypeName()) +
        "WHERE d.fpdr:rapportParlement = 'RAPPORT_RELATIF_ARTICLE_67_LOI_N_2004-1343'";

    private static final STLogger LOGGER = STLogFactory.getLog(CorbeilleServiceImpl.class);

    private static final String CORBEILLE_EXTENSION_POINT = "corbeille";

    private static final String QUERY_HAS_COMM_ALL_SQL =
        STQueryConstant.SELECT +
        SolonMgppCorbeilleConstant.COL_CORBEILLE_MGPP_INFO_CORBEILLE +
        ", " +
        SolonMgppCorbeilleConstant.COL_HASMESS_MGPP_INFO_CORBEILLE +
        STQueryConstant.FROM +
        SolonMgppCorbeilleConstant.MGPP_INFO_CORBEILLE_TABLE_NAME +
        STQueryConstant.WHERE +
        SolonMgppCorbeilleConstant.COL_HASMESS_MGPP_INFO_CORBEILLE +
        STQueryConstant.EQUAL +
        " 1 ";

    private Map<String, List<CorbeilleDTO>> mapCorbeille = null;

    private final Set<String> setCorbeilleLoaded = new HashSet<>();
    private Map<String, List<CorbeilleDTO>> corbeilleMap;

    /**
     * Mapping action, idCorbeille
     */
    private Map<String, String> fetchMap;

    @Override
    public void activate(ComponentContext context) {
        corbeilleMap = new HashMap<>();
        fetchMap = new HashMap<>();
    }

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        if (extensionPoint.equals(CORBEILLE_EXTENSION_POINT)) {
            CorbeilleDescriptor descriptor = (CorbeilleDescriptor) contribution;
            List<CorbeilleDTO> corbeillesDTO = buildCorbeilleDTOFromCorbeilleDescriptor(
                descriptor.getCorbeilleNodeList()
            );
            corbeilleMap.put(descriptor.getName(), corbeillesDTO);
            fetchMap.put(descriptor.getAction(), descriptor.getName());
        } else {
            throw new IllegalArgumentException("Unknown extension point: " + extensionPoint);
        }
    }

    /**
     * COnstruit une liste de {@link CorbeilleDTO} à partir de la liste des {@link CorbeilleNodeDescriptor}
     *
     * @param descriptors
     * @return
     */
    private List<CorbeilleDTO> buildCorbeilleDTOFromCorbeilleDescriptor(
        final List<CorbeilleNodeDescriptor> descriptors
    ) {
        List<CorbeilleDTO> list = new ArrayList<>();

        for (CorbeilleNodeDescriptor corbeilleNodeDescriptor : descriptors) {
            CorbeilleDTO corbeilleDTO = new CorbeilleDTOImpl();
            corbeilleDTO.setIdCorbeille(corbeilleNodeDescriptor.getName());
            corbeilleDTO.setNom(corbeilleNodeDescriptor.getLabel());
            corbeilleDTO.setRoutingTaskType(corbeilleNodeDescriptor.getRoutingTaskType());
            corbeilleDTO.setTypeActe(corbeilleNodeDescriptor.getTypeActe());
            corbeilleDTO.setCorbeille(new ArrayList<>());
            corbeilleDTO.setType(SolonMgppCorbeilleConstant.MGPP_NODE);
            corbeilleDTO.setIfMember(corbeilleNodeDescriptor.getIfMember());
            corbeilleDTO.setTypeObjet(CorbeilleTypeObjet.findByName(corbeilleNodeDescriptor.getType()));
            corbeilleDTO.setAdoption(corbeilleNodeDescriptor.isAdoption());
            corbeilleDTO.setParent(corbeilleNodeDescriptor.getParent());
            list.add(corbeilleDTO);
        }

        return list;
    }

    private void findAllCorbeille(CoreSession session) {
        WSEpp wsEpp = null;
        try {
            wsEpp = SolonMgppWsLocator.getWSEpp(session);
        } catch (WSProxyFactoryException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
            throw new NuxeoException(e);
        }

        ChercherCorbeilleResponse chercherCorbeilleResponse = null;
        try {
            chercherCorbeilleResponse = wsEpp.chercherCorbeille(new ChercherCorbeilleRequest());
        } catch (HttpTransactionException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
            throw new NuxeoException(SolonMgppWsLocator.getConnexionFailureMessage(session));
        } catch (Exception e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
            throw new NuxeoException(e);
        }

        if (chercherCorbeilleResponse == null) {
            throw new NuxeoException(
                "Erreur de communication avec SOLON EPP, la récupération des corbeilles a échoué."
            );
        } else if (
            chercherCorbeilleResponse.getStatut() == null ||
            !TraitementStatut.OK.equals(chercherCorbeilleResponse.getStatut())
        ) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, chercherCorbeilleResponse.getMessageErreur());
            throw new NuxeoException(
                "Erreur de communication avec SOLON EPP, la récupération des corbeilles a échoué." +
                WSErrorHelper.buildCleanMessage(chercherCorbeilleResponse.getMessageErreur())
            );
        }

        mapCorbeille = new HashMap<>();

        for (Section section : chercherCorbeilleResponse.getSection()) {
            CorbeilleDTO corbeilleDTOParent = buildCorbeilleDTOFromSection(section);

            // recuperation des sections (corbeille avec emis et recu)
            for (Corbeille corbeille : section.getCorbeille()) {
                CorbeilleDTO corbeilleDTO = buildCorbeilleDTOFromCorbeille(corbeille);
                corbeilleDTOParent.getCorbeille().add(corbeilleDTO);
            }

            List<CorbeilleDTO> list = new ArrayList<>();
            list.add(corbeilleDTOParent);
            mapCorbeille.put(corbeilleDTOParent.getIdCorbeille(), list);
        }

        for (Corbeille corbeille : chercherCorbeilleResponse.getCorbeille()) {
            // recupération corbeille simple
            CorbeilleDTO corbeilleDTO = buildCorbeilleDTOFromCorbeille(corbeille);

            List<CorbeilleDTO> list = new ArrayList<>();
            list.add(corbeilleDTO);
            mapCorbeille.put(corbeilleDTO.getIdCorbeille(), list);
        }
    }

    private CorbeilleDTO buildCorbeilleDTOFromSection(final Section section) {
        CorbeilleDTO corbeilleDTO = new CorbeilleDTOImpl();
        corbeilleDTO.setDescription(section.getDescription());
        corbeilleDTO.setIdCorbeille(section.getIdSection());
        corbeilleDTO.setNom(section.getNom());
        corbeilleDTO.setCorbeille(new ArrayList<>());
        corbeilleDTO.setType(SolonMgppCorbeilleConstant.ROOT_NODE);
        corbeilleDTO.setTypeObjet(CorbeilleTypeObjet.MESSAGE);
        return corbeilleDTO;
    }

    /**
     * Construit une {@link CorbeilleDTO} à partir d'une {@link Corbeille}
     *
     * @param corbeille
     * @return
     */
    private CorbeilleDTO buildCorbeilleDTOFromCorbeille(final Corbeille corbeille) {
        CorbeilleDTO corbeilleDTO = new CorbeilleDTOImpl();
        corbeilleDTO.setDescription(corbeille.getDescription());
        corbeilleDTO.setIdCorbeille(corbeille.getIdCorbeille());
        corbeilleDTO.setNom(corbeille.getNom());
        corbeilleDTO.setType(SolonMgppCorbeilleConstant.CORBEILLE_NODE);
        corbeilleDTO.setTypeObjet(CorbeilleTypeObjet.MESSAGE);
        return corbeilleDTO;
    }

    @Override
    public List<CorbeilleDTO> findCorbeille(final String idCorbeille, SSPrincipal ssPrincipal, CoreSession session) {
        synchronized (this) {
            if (mapCorbeille == null) {
                findAllCorbeille(session);
            }

            String corbeille = fetchCorbeille(idCorbeille, session);

            // recuperation de la corbeille EPP
            List<CorbeilleDTO> corbeillesDTO = mapCorbeille.get(corbeille);

            if (corbeillesDTO == null) {
                corbeillesDTO = new ArrayList<>();
            }

            // ajout des corbeilles spécifiques au MGPP
            addCorbeilleMGPP(corbeillesDTO, corbeille);

            corbeillesDTO = mapCorbeille.get(corbeille);

            if (corbeillesDTO == null) {
                corbeillesDTO = new ArrayList<>();
            }

            List<CorbeilleDTO> corbeilles = new ArrayList<>();

            Map<String, Boolean> cacheMemberOf = new HashMap<>();

            for (CorbeilleDTO corbeilleDTO : corbeillesDTO) {
                String group = corbeilleDTO.getIfMember();

                if (StringUtils.isBlank(group)) {
                    group = SolonMgppBaseFunctionConstant.CORBEILLE_MGPP_UPDATER;
                }

                if (cacheMemberOf.get(group) == null) {
                    if (ssPrincipal.isMemberOf(group)) {
                        cacheMemberOf.put(group, Boolean.TRUE);
                    } else {
                        cacheMemberOf.put(group, Boolean.FALSE);
                    }
                }

                // verif des droits de visibilité de la corbeille
                if (BooleanUtils.isTrue(cacheMemberOf.get(group))) {
                    corbeilles.add(corbeilleDTO);
                }
            }

            return corbeilles;
        }
    }

    @Override
    public CorbeilleDTO findSimpleCorbeille(String idCorbeille, CoreSession session) {
        if (mapCorbeille == null) {
            findAllCorbeille(session);
        }

        return mapCorbeille
            .values()
            .stream()
            .map(corbeilles -> getCorbeilleFromList(idCorbeille, corbeilles))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
    }

    @Override
    public String findDossierParlementaireForCorbeille(String idCorbeille, CoreSession session) {
        if (mapCorbeille == null) {
            findAllCorbeille(session);
        }

        String key = mapCorbeille
            .entrySet()
            .stream()
            .filter(entry -> getCorbeilleFromList(idCorbeille, entry.getValue()) != null)
            .map(Entry::getKey)
            .findFirst()
            .orElse(null);

        return fetchMap
            .entrySet()
            .stream()
            .filter(entry -> key != null && key.equals(entry.getValue()))
            .map(Entry::getKey)
            .findFirst()
            .orElse(null);
    }

    private CorbeilleDTO getCorbeilleFromList(String idCorbeille, List<CorbeilleDTO> corbeilles) {
        Optional<CorbeilleDTO> result = corbeilles
            .stream()
            .filter(corbeille -> StringUtils.equals(idCorbeille, corbeille.getIdCorbeille()))
            .findFirst();

        if (!result.isPresent()) {
            result =
                corbeilles
                    .stream()
                    .map(CorbeilleDTO::getCorbeille)
                    .map(subList -> getCorbeilleFromList(idCorbeille, subList))
                    .filter(Objects::nonNull)
                    .findFirst();
        }

        return result.orElse(null);
    }

    @Override
    public String fetchCorbeille(String idCorbeille, CoreSession session) {
        String corbeille = fetchMap.get(idCorbeille);
        if (corbeille == null) {
            String message = "Type de corbeille inconnue : " + idCorbeille;
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_CORBEILLE_TEC, message);
            throw new NuxeoException(message);
        }
        return fetchMap.get(idCorbeille);
    }

    private void addCorbeilleMGPP(final List<CorbeilleDTO> corbeillesDTO, final String corbeille) {
        if (!setCorbeilleLoaded.contains(corbeille)) {
            setCorbeilleLoaded.add(corbeille);
            List<CorbeilleDTO> mgppCorbeilleList = corbeilleMap.get(corbeille);
            for (CorbeilleDTO mgppCorbeille : mgppCorbeilleList) {
                if (mgppCorbeille.getParent() == null) {
                    corbeillesDTO.add(mgppCorbeille);
                } else {
                    for (CorbeilleDTO corbeilleParent : corbeillesDTO) {
                        if (corbeilleParent.getIdCorbeille().equals(mgppCorbeille.getParent())) {
                            corbeilleParent.getCorbeille().add(mgppCorbeille);
                            break;
                        } else {
                            corbeillesDTO.add(mgppCorbeille);
                        }
                    }
                }
            }

            mapCorbeille.put(corbeille, corbeillesDTO);
        }
    }

    @Override
    public Set<String> findActionPossible(CoreSession session, SSPrincipal ssPrincipal, EvenementDTO evenementDTO) {
        if (evenementDTO == null || evenementDTO.getActionSuivante() == null) {
            return new HashSet<>();
        }

        Set<String> actions = new HashSet<>(evenementDTO.getActionSuivante());

        if (!ssPrincipal.isMemberOf(SolonMgppBaseFunctionConstant.AR_EVENEMENT)) {
            actions.remove(Action.ACCUSER_RECEPTION.value());
        }

        if (!ssPrincipal.isMemberOf(SolonMgppBaseFunctionConstant.ACCEPTER_EVENEMENT)) {
            actions.remove(Action.ACCEPTER.value());
        }

        if (!ssPrincipal.isMemberOf(SolonMgppBaseFunctionConstant.REJETER_EVENEMENT)) {
            actions.remove(Action.REJETER.value());
        }

        if (!ssPrincipal.isMemberOf(SolonMgppBaseFunctionConstant.SUPPRIMER_VERSION)) {
            actions.remove(Action.SUPPRIMER.value());
        }

        if (!ssPrincipal.isMemberOf(SolonMgppBaseFunctionConstant.CREER_ALERTE)) {
            actions.remove(Action.LEVER_ALERTE.value());
            actions.remove(Action.CREER_ALERTE.value());
        }

        return actions;
    }

    @Override
    public Long countDossier(CoreSession session, CorbeilleNode corbeilleNode) {
        Long count = 0L;
        switch (corbeilleNode.getTypeObjet()) {
            case DOSSIER:
                List<Object> params = new ArrayList<>();
                StringBuilder queryDossier = getDossierQuery(corbeilleNode, params);
                String query = QueryUtils.ufnxqlToFnxqlQuery(queryDossier.toString());
                count = QueryUtils.doCountQuery(session, query, params.toArray());
                break;
            case MESSAGE:
                Long result = 0L;
                result += corbeilleNode.getMessageDTO().size();
                for (CorbeilleNode childNode : corbeilleNode.getCorbeilleNodeList()) {
                    result += childNode.getMessageDTO().size();
                }
                count = result;
                break;
            case FICHE_LOI:
                count =
                    QueryUtils.doCountQuery(
                        session,
                        QueryUtils.ufnxqlToFnxqlQuery(getFicheLoiQuery()),
                        getFicheLoiQueryParameter(session).toArray()
                    );
                break;
            case FICHE_DR:
                count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(SELECT_DR_FROM));
                break;
            case FICHE_DR_67:
                count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(SELECT_DR67_FROM));
                break;
            case AVI:
                count = doCountCorbeilleNomination(session, corbeilleNode, FichePresentationAVI.PREFIX);
                break;
            case OEP:
                count = doCountCorbeilleNomination(session, corbeilleNode, FichePresentationOEPImpl.PREFIX);
                break;
            case FICHE_AUD:
                count = doCountCorbeilleNomination(session, corbeilleNode, FichePresentationAUD.PREFIX);
                break;
            case FICHE_DECRET:
            case FICHE_IE:
            case FICHE_341:
            case FICHE_DPG:
            case FICHE_SD:
            case FICHE_JSS:
            case FICHE_DOC:
                String queryFiche = String.format(SELECT_DOSSIER_FROM, corbeilleNode.getTypeObjet().getDocTypeName());

                count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(queryFiche));
                break;
        }

        return count;
    }

    private Long doCountCorbeilleNomination(CoreSession session, CorbeilleNode corbeilleNode, String prefix) {
        String queryFiche = String.format(
            SELECT_FICHE_FROM_WHERE_DATE,
            corbeilleNode.getTypeObjet().getDocTypeName(),
            prefix,
            prefix
        );

        return QueryHelper.doUfnxqlCountQuery(session, queryFiche, Calendar.getInstance());
    }

    @Override
    public Long countDossierUnrestricted(CoreSession session, CorbeilleNode corbeilleNode) {
        Long count = 0L;
        if (corbeilleNode.getTypeObjet() == CorbeilleTypeObjet.DOSSIER) {
            List<Object> params = new ArrayList<>();
            StringBuilder queryDossier = getDossierQuery(corbeilleNode, params);
            String query = QueryUtils.ufnxqlToFnxqlQuery(queryDossier.toString());
            count = QueryUtils.doCountQuery(session, query, params.toArray());
        }

        return count;
    }

    private String getFicheLoiQuery() {
        StringBuilder queryFicheLoi = new StringBuilder("SELECT d.ecm:uuid as id , d.floi:dateCreation FROM ");
        queryFicheLoi.append(FicheLoi.DOC_TYPE);
        queryFicheLoi.append(" as d ");
        queryFicheLoi.append(" WHERE d.");
        queryFicheLoi.append(FicheLoi.PREFIX);
        queryFicheLoi.append(":");
        queryFicheLoi.append(FicheLoi.DATE_CREATION);
        queryFicheLoi.append(" > ?");

        return queryFicheLoi.toString();
    }

    private List<Object> getFicheLoiQueryParameter(CoreSession session) {
        final ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator
            .getParametreMgppService()
            .findParametrageMgpp(session);
        Long iFiltre = parametrageMgpp.getFiltreDateCreationLoi();
        Calendar date = DateUtil.removeMonthsToNow(iFiltre.intValue());
        List<Object> param = new ArrayList<>();
        param.add(date);

        return param;
    }

    @Override
    public StringBuilder getDossierQuery(CorbeilleNode corbeilleNode, List<Object> params) {
        StringBuilder queryDossier = new StringBuilder(SELECT_DECMUUID_FROM);
        queryDossier.append(STDossierLinkConstant.DOSSIER_LINK_DOCUMENT_TYPE);
        queryDossier.append(" as dl, ");
        queryDossier.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
        queryDossier.append(" as d WHERE dl.");
        queryDossier.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
        queryDossier.append(":");
        queryDossier.append(STSchemaConstant.CASE_LINK_CASE_DOCUMENT_ID_PROPERTY);
        queryDossier.append(" = d.ecm:uuid AND dl.");
        queryDossier.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
        queryDossier.append(":");
        queryDossier.append(STDossierLinkConstant.DOSSIER_LINK_ROUTING_TASK_TYPE_PROPERTY);
        queryDossier.append(" = ? AND d.");
        queryDossier.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
        queryDossier.append(":");
        queryDossier.append(DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_PROPERTY);
        queryDossier.append(" IN ('");
        queryDossier.append(corbeilleNode.getTypeActe());
        queryDossier.append("') AND dl.");
        queryDossier.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
        queryDossier.append(":");
        queryDossier.append(DossierSolonEpgConstants.DOSSIER_LINK_CASE_LIFE_CYCLE_STATE_PROPERTY);
        queryDossier.append(" = ? AND dl.");
        queryDossier.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
        queryDossier.append(":");
        queryDossier.append(DossierSolonEpgConstants.ARCHIVE_DOSSIER_PROPERTY);
        queryDossier.append(" = ? AND dl.");
        queryDossier.append(STSchemaConstant.ACTIONABLE_CASE_LINK_SCHEMA_PREFIX);
        queryDossier.append(":");
        queryDossier.append(DossierSolonEpgConstants.DELETED);
        queryDossier.append(" = ? ");

        params.add(corbeilleNode.getRoutingTaskType());
        params.add(DossierState.running.toString());
        params.add(0);
        params.add(0);

        if (BooleanUtils.isTrue(corbeilleNode.isAdoption())) {
            queryDossier.append(" AND d.");
            queryDossier.append(DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX);
            queryDossier.append(":");
            queryDossier.append(DossierSolonEpgConstants.ADOPTION);
            queryDossier.append(" = ? ");
            params.add(1);
        }

        return queryDossier;
    }

    @Override
    public boolean hasCorbeilleMessages(CoreSession session, String idCorbeille) {
        StringBuilder query = new StringBuilder(QUERY_HAS_COMM_ALL_SQL)
            .append(STQueryConstant.AND)
            .append(SolonMgppCorbeilleConstant.COL_CORBEILLE_MGPP_INFO_CORBEILLE)
            .append(STQueryConstant.EQUAL_PARAM);
        List<CorbeilleInfos> list = getInfosCorbeilleFromQuery(session, query.toString(), new Object[] { idCorbeille });
        return !list.isEmpty();
    }

    @Override
    public boolean haveCorbeillesMessages(CoreSession session, Set<String> idsCorbeilles) {
        StringBuilder query = new StringBuilder(QUERY_HAS_COMM_ALL_SQL)
            .append(STQueryConstant.AND)
            .append(
                QueryHelper.getQuestionMarkQueryWithColumn(
                    SolonMgppCorbeilleConstant.COL_CORBEILLE_MGPP_INFO_CORBEILLE,
                    idsCorbeilles.size()
                )
            );
        List<CorbeilleInfos> infos = getInfosCorbeilleFromQuery(session, query.toString(), idsCorbeilles.toArray());
        return !infos.isEmpty();
    }

    /**
     * Retourne une liste de CorbeilleInfos avec une requête en paramètre. /!\ la requête en paramètre doit avoir les
     * colonnes "CORBEILLE" et "HASMESS" dans le select
     *
     * @param session
     * @param query
     * @return
     */
    private List<CorbeilleInfos> getInfosCorbeilleFromQuery(CoreSession session, String query, Object[] params) {
        List<CorbeilleInfos> corbeillesInfosList = new ArrayList<>();
        String[] colonnes = { FlexibleQueryMaker.COL_ID, STSchemaConstant.DUBLINCORE_TITLE_XPATH };
        IterableQueryResult res = null;
        try {
            res = QueryUtils.doSqlQuery(session, colonnes, query, params);
            if (res != null) {
                final Iterator<Map<String, Serializable>> iterator = res.iterator();
                while (iterator.hasNext()) {
                    corbeillesInfosList.add(getInfosFromRow(iterator.next()));
                }
            }
        } catch (NuxeoException exc) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_INFO_CORBEILLE_TEC, exc);
        } finally {
            if (res != null) {
                res.close();
            }
        }

        return corbeillesInfosList;
    }

    /**
     * Renvoi un CorbeilleInfos avec idCorbeille et hasNonTraitees renseignés avec la row en paramètre /!\ la row doit
     * contenir les colonnes "FlexibleQueryMaker.COL_ID" pour corbeille et "STSchemaConstant.DUBLINCORE_TITLE_XPATH"
     * pour hasmess
     *
     * @param row
     * @return
     */
    private CorbeilleInfos getInfosFromRow(Map<String, Serializable> row) {
        CorbeilleInfos infos = new CorbeilleInfos();
        String corbeilleId = (String) row.get(FlexibleQueryMaker.COL_ID);
        infos.setIdCorbeille(corbeilleId);
        infos.setHasNonTraitees(BooleanUtils.toBoolean((String) row.get(STSchemaConstant.DUBLINCORE_TITLE_XPATH)));
        return infos;
    }
}
