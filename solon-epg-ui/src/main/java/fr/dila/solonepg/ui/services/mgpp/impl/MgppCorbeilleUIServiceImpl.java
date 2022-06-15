package fr.dila.solonepg.ui.services.mgpp.impl;

import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.CORBEILLE_ID;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.CRITERE_RECHERCHE;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.DOSSIERS_PARLEMENTAIRES_SELECTED;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.FICHE_LIST_FORM;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.IS_RECHERCHE_RAPIDE;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.MAP_SEARCH;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.MESSAGE_LIST_FORM;

import fr.dila.solonepg.ui.bean.CorbeilleUIDTO;
import fr.dila.solonepg.ui.bean.MessageList;
import fr.dila.solonepg.ui.bean.MgppCorbeilleContentList;
import fr.dila.solonepg.ui.bean.MgppMessageDTO;
import fr.dila.solonepg.ui.bean.RapidSearchDTO;
import fr.dila.solonepg.ui.contentview.DocumentModelFiltrablePaginatedPageDocumentProvider;
import fr.dila.solonepg.ui.contentview.mgpp.MgppDossierPageProvider;
import fr.dila.solonepg.ui.contentview.mgpp.MgppMessageListPageProvider;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppCorbeilleFiltreEnum;
import fr.dila.solonepg.ui.enums.mgpp.MgppFiltreEnum;
import fr.dila.solonepg.ui.enums.mgpp.MgppSuggestionType;
import fr.dila.solonepg.ui.helper.mgpp.MgppEditWidgetFactory;
import fr.dila.solonepg.ui.helper.mgpp.MgppFicheListProviderHelper;
import fr.dila.solonepg.ui.services.mgpp.MgppCorbeilleUIService;
import fr.dila.solonepg.ui.th.bean.MessageListForm;
import fr.dila.solonepg.ui.th.bean.MgppCorbeilleContentListForm;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.solonmgpp.api.constant.SolonMgppActionConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppCorbeilleConstant;
import fr.dila.solonmgpp.api.dto.CorbeilleDTO;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.CorbeilleService;
import fr.dila.solonmgpp.api.tree.CorbeilleNode;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.core.tree.CorbeilleNodeImpl;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.core.enumeration.TypeChampEnum;
import fr.dila.ss.ui.bean.RequeteExperteDTO;
import fr.dila.ss.ui.bean.RequeteLigneDTO;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.requete.recherchechamp.Parametre;
import fr.dila.st.core.requete.recherchechamp.descriptor.ChampDescriptor;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.STExcelUtil;
import fr.dila.st.ui.bean.WidgetDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryAndFetchPageProvider;

public class MgppCorbeilleUIServiceImpl implements MgppCorbeilleUIService {
    private static final String ERREUR_RECUPERATION_CORBEILLE =
        "Une erreur est survenue lors de la récupération des messages";
    private static final String ERREUR_RECUPERATION_RESULTATS =
        "Une erreur est survenue lors de la récupération des résultats";
    private static final STLogger LOGGER = STLogFactory.getLog(MgppCorbeilleUIServiceImpl.class);

    private static final String DATE_EVENEMENT_DEBUT = "dateEvenementDebut";
    private static final String DATE_EVENEMENT_FIN = "dateEvenementFin";

    @Override
    public List<CorbeilleUIDTO> getCorbeilles(SpecificContext context) {
        String dossiersParlementairesSelected = context.getFromContextData(DOSSIERS_PARLEMENTAIRES_SELECTED);
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();

        if (StringUtils.isNotEmpty(dossiersParlementairesSelected)) {
            try {
                List<CorbeilleDTO> corbeilles = SolonMgppServiceLocator
                    .getCorbeilleService()
                    .findCorbeille(dossiersParlementairesSelected, ssPrincipal, session);
                return corbeilles
                    .stream()
                    .map(apiDto -> convertToCorbeilleNode(context, apiDto))
                    .map(node -> convertToCorbeilleDTO(node, "SECTION"))
                    .collect(Collectors.toList());
            } catch (NuxeoException e) {
                String message = e.getMessage();
                LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_INFO_CORBEILLE_TEC, message);
                context.getMessageQueue().addErrorToQueue(message);
            }
        }
        return Collections.emptyList();
    }

    private CorbeilleNode convertToCorbeilleNode(SpecificContext context, CorbeilleDTO corbeilleDTO) {
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();
        CorbeilleNode corbeilleNode = new CorbeilleNodeImpl();
        corbeilleNode.setDescription(corbeilleDTO.getDescription());
        corbeilleNode.setName(corbeilleDTO.getNom());
        corbeilleNode.setId(corbeilleDTO.getIdCorbeille());
        Boolean close =
            SolonMgppCorbeilleConstant.CORBEILLE_NODE.equals(corbeilleDTO.getType()) ||
            SolonMgppCorbeilleConstant.MGPP_NODE.equals(corbeilleDTO.getType());
        corbeilleNode.setOpened(!close);
        corbeilleNode.setType(corbeilleDTO.getType());
        corbeilleNode.setRoutingTaskType(corbeilleDTO.getRoutingTaskType());
        corbeilleNode.setTypeActe(corbeilleDTO.getTypeActe());
        corbeilleNode.setTypeObjet(corbeilleDTO.getTypeObjet());
        corbeilleNode.setAdoption(corbeilleDTO.isAdoption());
        // load messages
        try {
            List<fr.dila.solonmgpp.api.dto.MessageDTO> listMessageDTO = SolonMgppServiceLocator
                .getMessageService()
                .findMessageByCorbeille(
                    ssPrincipal,
                    corbeilleDTO,
                    session,
                    context.getFromContextData(DOSSIERS_PARLEMENTAIRES_SELECTED)
                );
            corbeilleNode.setMessageDTO(listMessageDTO);
        } catch (NuxeoException e) {
            String message = e.getMessage();
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_MESSAGE_TEC, message);
            context.getMessageQueue().addErrorToQueue(message);
        }
        // add children
        corbeilleNode.setCorbeilleNodeList(
            corbeilleDTO
                .getCorbeille()
                .stream()
                .map(subDTO -> convertToCorbeilleNode(context, subDTO))
                .collect(Collectors.toList())
        );

        CorbeilleService corbeilleService = SolonMgppServiceLocator.getCorbeilleService();
        if (SolonMgppActionConstant.PUBLICATION.equals(corbeilleDTO.getIdCorbeille())) {
            // traitement unrestricted spécifique à publication pour des raisons
            // de performances
            corbeilleNode.setCount(corbeilleService.countDossierUnrestricted(session, corbeilleNode));
        } else {
            corbeilleNode.setCount(corbeilleService.countDossier(session, corbeilleNode));
        }
        return corbeilleNode;
    }

    private CorbeilleUIDTO convertToCorbeilleDTO(CorbeilleNode corbeilleNode, String type) {
        CorbeilleUIDTO corbeille = new CorbeilleUIDTO(
            type,
            corbeilleNode.getId(),
            corbeilleNode.getName(),
            corbeilleNode.getDescription(),
            Long.toString(corbeilleNode.getCount()),
            corbeilleNode
                .getCorbeilleNodeList()
                .stream()
                .map(subNode -> convertToCorbeilleDTO(subNode, "CORBEILLE"))
                .collect(Collectors.toList()),
            false
        );

        MgppCorbeilleName corbeilleType = MgppCorbeilleName.fromValue(corbeilleNode.getId());
        if (corbeilleType != null) {
            corbeille.setDynamic(corbeilleType.isDynamic());
        }

        return corbeille;
    }

    @Override
    public MessageList getMessageListForCorbeille(SpecificContext context) {
        CoreSession session = context.getSession();

        MgppCorbeilleName corbeilleId = context.getFromContextData(CORBEILLE_ID);
        String dossierParlementaire = context.getFromContextData(DOSSIERS_PARLEMENTAIRES_SELECTED);
        boolean isRechercheRapide = BooleanUtils.toBooleanDefaultIfNull(
            context.getFromContextData(IS_RECHERCHE_RAPIDE),
            false
        );
        Map<String, Serializable> mapSearch = context.getFromContextData(MAP_SEARCH);
        CritereRechercheDTO critereRecherche = context.getFromContextData(CRITERE_RECHERCHE);
        CorbeilleNode corbeilleNode = Optional
            .ofNullable(corbeilleId)
            .map(
                corbId ->
                    SolonMgppServiceLocator.getCorbeilleService().findSimpleCorbeille(corbId.getIdCorbeille(), session)
            )
            .map(dto -> convertToCorbeilleNode(context, dto))
            .orElse(null);
        MessageList messageList = new MessageList();

        MessageListForm messageListForm = context.getFromContextData(MESSAGE_LIST_FORM);
        ObjectHelper.requireNonNullElseGet(messageListForm, MessageListForm::newForm);

        MgppMessageListPageProvider provider = messageListForm.getPageProvider(session, "mgppMessageListPageProvider");
        Map<String, Serializable> props = provider.getProperties();
        props.put(CoreQueryAndFetchPageProvider.CORE_SESSION_PROPERTY, (Serializable) session);
        props.put(MgppMessageListPageProvider.CORBEILLE_NODE_PROPERTY, corbeilleNode);
        props.put(MgppMessageListPageProvider.DOSSIER_PARLEMENTAIRE_PROPERTY, dossierParlementaire);
        props.put(MgppMessageListPageProvider.IS_RECHERCHE_RAPIDE_PROPERTY, isRechercheRapide);
        props.put(MgppMessageListPageProvider.CRITERE_RECHERCHE_PROPERTY, (Serializable) critereRecherche);
        props.put(MgppMessageListPageProvider.MAP_SEARCH_PROPERTY, (Serializable) mapSearch);
        provider.setProperties(props);
        if (BooleanUtils.isTrue(context.getFromContextData(SSContextDataKey.IS_EXPORT))) {
            provider.setCurrentPageOffset(0);
            provider.setPageSize(STExcelUtil.XLS_ROW_LIMIT - MgppDossierUIServiceImpl.EXPORT_EXCEL_HEADER_ROW_SPAN);
        }

        List<Map<String, Serializable>> docList = provider.getCurrentPage();

        LOGGER.debug(
            MgppLogEnumImpl.INIT_MGPP_REQUESTOR_TEC,
            provider.getResultsCount() + " messages trouvés dans cette corbeille"
        );

        messageList.setNbTotal((int) provider.getResultsCount());
        messageList.setListe(
            docList
                .stream()
                .filter(MgppMessageDTO.class::isInstance)
                .map(MgppMessageDTO.class::cast)
                .collect(Collectors.toList())
        );
        messageList.setTitre(
            isRechercheRapide
                ? ResourceHelper.getString("label.quickSearchTitle")
                : Optional.ofNullable(corbeilleNode).map(CorbeilleNode::getName).orElse("Liste des communications")
        );

        // Vérifie si le cache corbeille a besoin d'être invalidé (seulement quand aucun filtre n'est appliqué = tous les messages récupérés)
        if (Optional.ofNullable(corbeilleId).isPresent() && critereRecherche == null) {
            SolonMgppServiceLocator
                .getMessageService()
                .invalidateCacheCorbeille(
                    (SSPrincipal) session.getPrincipal(),
                    SolonMgppServiceLocator
                        .getCorbeilleService()
                        .findSimpleCorbeille(corbeilleId.getIdCorbeille(), session),
                    messageList.getNbTotal()
                );
        }

        return messageList;
    }

    private WidgetDTO buildWidgetDTO(MgppFiltreEnum filtre, SpecificContext context) {
        WidgetDTO widgetFiltre = filtre.initWidget(context);
        Map<String, Serializable> oldValues = context.getFromContextData(MgppContextDataKey.MAP_SEARCH);
        if (MapUtils.isNotEmpty(oldValues) && oldValues.containsKey(filtre.getWidgetName())) {
            widgetFiltre
                .getParametres()
                .add(new Parametre(MgppEditWidgetFactory.VALEUR, oldValues.get(filtre.getWidgetName())));
        }
        if ("dateEvenement".equals(widgetFiltre.getName()) && oldValues.containsKey(DATE_EVENEMENT_DEBUT)) {
            widgetFiltre
                .getParametres()
                .add(new Parametre(MgppEditWidgetFactory.VALUE_BEGINNING, oldValues.get(DATE_EVENEMENT_DEBUT)));
        }
        if ("dateEvenement".equals(widgetFiltre.getName()) && oldValues.containsKey(DATE_EVENEMENT_FIN)) {
            widgetFiltre
                .getParametres()
                .add(new Parametre(MgppEditWidgetFactory.VALUE_ENDING, oldValues.get(DATE_EVENEMENT_FIN)));
        }
        if (filtre == MgppFiltreEnum.EMETTEUR) {
            widgetFiltre.getParametres().addAll(MgppSuggestionType.EMETTEUR.buildParametres());
        }

        return widgetFiltre;
    }

    @Override
    public RapidSearchDTO buildFilters(SpecificContext context) {
        RapidSearchDTO staticFilters = new RapidSearchDTO();
        MgppCorbeilleFiltreEnum corbeilleFiltre = MgppCorbeilleFiltreEnum.fromCorbeille(
            context.getFromContextData(MgppContextDataKey.CORBEILLE_ID)
        );

        staticFilters
            .getLstWidgets()
            .addAll(
                corbeilleFiltre
                    .getLstFiltres()
                    .stream()
                    .map(filtre -> buildWidgetDTO(filtre, context))
                    .collect(Collectors.toList())
            );

        return staticFilters;
    }

    @Override
    public MgppCorbeilleContentList getFicheListForCorbeille(SpecificContext context) {
        CoreSession session = context.getSession();

        MgppCorbeilleName corbeille = context.getFromContextData(CORBEILLE_ID);

        Map<String, Serializable> mapSearch = context.getFromContextData(MAP_SEARCH);

        String dossiersParlementairesSelected = context.getFromContextData(DOSSIERS_PARLEMENTAIRES_SELECTED);

        List<CorbeilleDTO> lstCorbeilleDto = SolonMgppServiceLocator
            .getCorbeilleService()
            .findCorbeille(dossiersParlementairesSelected, (SSPrincipal) session.getPrincipal(), session);
        CorbeilleNode corbeilleNode = lstCorbeilleDto
            .stream()
            .filter(dto -> dto.getIdCorbeille().equals(corbeille.getIdCorbeille()))
            .map(dto -> convertToCorbeilleNode(context, dto))
            .findFirst()
            .orElse(null);

        MgppCorbeilleContentList ficheList = new MgppCorbeilleContentList();

        try {
            MgppCorbeilleContentListForm messageListForm = (MgppCorbeilleContentListForm) Optional
                .ofNullable(context.getFromContextData(FICHE_LIST_FORM))
                .orElse(new MgppCorbeilleContentListForm());

            String providerName;
            // Cas particulier pour la distinction entre DR67 et DR
            if (corbeille == MgppCorbeilleName.CONSULTATION_FICHE_DR_67_EXISTANT) {
                providerName = "ficheDR67PageProvider";
            } else {
                providerName = MgppCorbeilleFiltreEnum.fromCorbeille(corbeille).getProviderName();
            }

            PageProvider<?> provider = messageListForm.getPageProvider(session, providerName);
            Map<String, Serializable> props = provider.getProperties();
            props.put(MgppDossierPageProvider.CORBEILLE_NODE, corbeilleNode);
            provider.setProperties(props);
            if (BooleanUtils.isTrue(context.getFromContextData(SSContextDataKey.IS_EXPORT))) {
                provider.setCurrentPageOffset(0);
                provider.setPageSize(STExcelUtil.XLS_ROW_LIMIT - MgppDossierUIServiceImpl.EXPORT_EXCEL_HEADER_ROW_SPAN);
            }

            MgppCorbeilleFiltreEnum corbeilleFiltres = MgppCorbeilleFiltreEnum.fromCorbeille(corbeille);

            Map<String, Serializable> filters = new LinkedHashMap<>(
                mapSearch
                    .entrySet()
                    .stream()
                    .filter(entry -> !"idCorbeille".equals(entry.getKey()))
                    .filter(
                        entry ->
                            convertWidgetNameToNuxeoField(entry.getKey(), corbeilleFiltres.getXpathPrefix()) != null
                    )
                    .collect(
                        Collectors.toMap(
                            entry -> convertWidgetNameToNuxeoField(entry.getKey(), corbeilleFiltres.getXpathPrefix()),
                            entry -> FiltreUtils.convertValue(entry.getValue())
                        )
                    )
            );

            MgppFicheListProviderHelper.setProviderData(messageListForm, corbeille, provider, filters);

            if (provider instanceof DocumentModelFiltrablePaginatedPageDocumentProvider) {
                List<DocumentModel> docList = (List<DocumentModel>) provider.getCurrentPage();

                ficheList.setListe(
                    docList.stream().map(MgppFicheListProviderHelper::convertDocToMap).collect(Collectors.toList())
                );
            }

            ficheList.setNbTotal((int) provider.getResultsCount());

            LOGGER.debug(
                MgppLogEnumImpl.INIT_MGPP_REQUESTOR_TEC,
                provider.getResultsCount() + " fiches trouvés dans cette corbeille"
            );
            ficheList.setTitre(
                Optional.ofNullable(corbeilleNode).map(CorbeilleNode::getName).orElse("Liste de fiches inconnue")
            );
        } catch (Exception e) {
            LOGGER.error(context.getSession(), MgppLogEnumImpl.FAIL_GET_MESSAGE_TEC, ERREUR_RECUPERATION_CORBEILLE, e);
            throw new NuxeoException(ERREUR_RECUPERATION_CORBEILLE, e);
        }

        return ficheList;
    }

    private String convertWidgetNameToNuxeoField(String widgetName, String xpath) {
        String appender = "";
        String correctWidgetName = StringUtils.removeEnd(widgetName, "Debut");

        if (widgetName.endsWith("Fin")) {
            correctWidgetName = StringUtils.removeEnd(widgetName, "Fin");
            appender = DocumentModelFiltrablePaginatedPageDocumentProvider.MAX;
        }

        MgppFiltreEnum filtre = MgppFiltreEnum.fromWidgetName(correctWidgetName);

        return filtre == null ? null : xpath + filtre.getFieldName() + appender;
    }

    @Override
    public MgppCorbeilleContentList getFicheListForRechercheExperte(SpecificContext context) {
        MgppCorbeilleContentListForm ficheListForm = context.getFromContextData(FICHE_LIST_FORM);
        RequeteExperteDTO requeteExperteDTO = context.getFromContextData(SSContextDataKey.REQUETE_EXPERTE_DTO);
        List<RequeteLigneDTO> requetes = requeteExperteDTO.getRequetes();
        MgppCorbeilleContentList ficheList = new MgppCorbeilleContentList();

        try {
            MgppCorbeilleFiltreEnum corbeilleFiltre = MgppCorbeilleFiltreEnum.fromSearchCategory(
                requetes
                    .stream()
                    .findFirst()
                    .map(RequeteLigneDTO::getChamp)
                    .map(ChampDescriptor::getCategorie)
                    .orElse(null)
            );

            DocumentModelFiltrablePaginatedPageDocumentProvider provider = ficheListForm.getPageProvider(
                context.getSession(),
                "fichePageProvider",
                null
            );
            String query = corbeilleFiltre.getSearchQuery() + getWhereClause(requetes);
            provider.getDefinition().setPattern(query);

            MgppFicheListProviderHelper.setProviderData(ficheListForm, corbeilleFiltre, provider, new HashMap<>());
            List<DocumentModel> docList = provider.getCurrentPage();
            ficheList.setListe(
                docList.stream().map(MgppFicheListProviderHelper::convertDocToMap).collect(Collectors.toList())
            );
            ficheList.getListeColonnes(ficheListForm, corbeilleFiltre);
            ficheList.setNbTotal((int) provider.getResultsCount());
        } catch (Exception e) {
            throw new NuxeoException(ERREUR_RECUPERATION_RESULTATS, e);
        }
        return ficheList;
    }

    /**
     * Retourne la clause where à partir d'une liste de RequeteLigneDTO
     *
     * @param requeteCriteria
     * @return
     */
    private String getWhereClause(List<RequeteLigneDTO> requeteCriteria) {
        return CollectionUtils.isEmpty(requeteCriteria)
            ? ""
            : "WHERE " +
            requeteCriteria.stream().map(this::getWhereConditionFromRequeteLigne).collect(Collectors.joining(" "));
    }

    /**
     * Retourne une requête NXQL à partie d'une RequeteLigneDTO
     *
     * @param ligne
     * @return
     */
    private String getWhereConditionFromRequeteLigne(RequeteLigneDTO ligne) {
        StringBuilder condition = new StringBuilder();
        if (StringUtils.isNotBlank(ligne.getAndOr())) {
            condition.append("ET".equals(ligne.getAndOr()) ? "AND " : "OR ");
        }
        condition.append(ligne.getChamp().getField() + " ");
        condition.append(ligne.getOperator().getOperator() + " ");
        if (!TypeChampEnum.SIMPLE_SELECT_BOOLEAN.name().equals(ligne.getChamp().getTypeChamp())) {
            condition.append(ligne.getOperator().getDisplayFunction().apply(ligne.getValue()));
        } else {
            condition.append(ligne.getValue().get(0));
        }
        return condition.toString();
    }
}
