package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.ui.enums.EpgActionCategory.ALERTE_ACTIONS;
import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getEpgEspaceSuiviUIService;
import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getTableauDynamiqueUIService;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.ui.bean.AlerteDTO;
import fr.dila.solonepg.ui.bean.EpgSuiviTreeElementDTO;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgSuiviMenuService;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.services.actions.SSActionsServiceLocator;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.ui.bean.TreeElementDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.services.impl.LeftMenuServiceImpl;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.automation.core.util.PageProviderHelper;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.query.api.PageProviderDefinition;
import org.nuxeo.ecm.platform.query.api.PageProviderService;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryDocumentPageProvider;

public class EpgSuiviMenuServiceImpl extends LeftMenuServiceImpl implements EpgSuiviMenuService {
    public static final String TREES = "trees";
    public static final String LISTS = "lists";
    public static final String ALERTES = "alertes";
    public static final String LIST_TD = "lstTableaux";
    /** Identifiant du set de noeuds ouverts de l'arbre de dossier créés */
    public static final String DOSSIERS_CREES_OPEN_NODES_KEY = "dossiersCreesTree_openNodes";

    /** Identifiant du set de noeuds ouverts de l'arbre de dossier en cours */
    public static final String DOSSIERS_RUNNING_OPEN_NODES_KEY = "dossiersRunningTree_openNodes";

    public static final String SUIVI_DOSSIER_KEY = "suiviDossierKey";
    public static final String SUIVI_DOSSIER_NODE_KEY = EpgTemplateConstants.SUIVI_DOSSIER_NODE_KEY;
    public static final String SUIVI_DOSSIER_STATE = "suiviDossierState";
    public static final String SUIVI_DOSSIER_LABEL = "suiviDossierLabel";
    public static final String SUIVI_DOSSIER_LIST_FORM_KEY = "suiviDossierListFormKey";
    private static final String PAN_MAIN_TABLEAU_PAGE_PROVIDER = "panMainTableauPageProvider";

    public EpgSuiviMenuServiceImpl() {
        super("SUIVI_MENU");
    }

    @Override
    public Map<String, Object> getData(SpecificContext context) {
        SSPrincipal principal = (SSPrincipal) context.getSession().getPrincipal();
        Map<String, Object> map = new HashMap<>();

        if (principal.isMemberOf(SolonEpgBaseFunctionConstant.INFOCENTRE_GENERAL_READER)) {
            Set<String> dossiersCreeOpenNodeSet = null;
            Set<String> dossiersRunningOpenNodeSet = null;
            if (
                context.getFromContextData(EpgContextDataKey.PROVIDER_NAME) != null &&
                !PAN_MAIN_TABLEAU_PAGE_PROVIDER.equals(context.getFromContextData(EpgContextDataKey.PROVIDER_NAME))
            ) {
                dossiersCreeOpenNodeSet =
                    UserSessionHelper.getUserSessionParameter(context, DOSSIERS_CREES_OPEN_NODES_KEY);
                dossiersRunningOpenNodeSet =
                    UserSessionHelper.getUserSessionParameter(context, DOSSIERS_RUNNING_OPEN_NODES_KEY);
            }

            EpgSuiviTreeElementDTO dossiersInit = buildCorbeilleRoot(
                context,
                dossiersCreeOpenNodeSet,
                STDossier.DossierState.init
            );
            EpgSuiviTreeElementDTO dossiersRunning = buildCorbeilleRoot(
                context,
                dossiersRunningOpenNodeSet,
                STDossier.DossierState.running
            );

            List<TreeElementDTO> trees = Arrays.asList(dossiersInit, dossiersRunning);
            map.put(TREES, trees);
        }
        map.put(EpgTemplateConstants.GESTION_LISTE_ACTION, context.getAction(EpgActionEnum.SUIVI_GESTION_LISTES));
        map.put(getMenuKey(), getMenu(context));
        map.put(ALERTES, getAlertes(context));
        map.put(LIST_TD, getTableauDynamiqueUIService().getListTableauxDynamiqueDTO(context));
        return map;
    }

    private EpgSuiviTreeElementDTO buildCorbeilleRoot(
        SpecificContext context,
        Set<String> openedNode,
        STDossier.DossierState dossierState
    ) {
        EpgSuiviTreeElementDTO rootNode;
        context.putInContextData(STContextDataKey.OPEN_NODE, openedNode);
        context.putInContextData(STContextDataKey.DOSSIER_STATE, dossierState);
        context.putInContextData(STContextDataKey.TYPE, OrganigrammeType.GOUVERNEMENT);
        String rootId = dossierState.getKey();

        if (CollectionUtils.isNotEmpty(openedNode)) {
            context.putInContextData(STContextDataKey.ID, rootId);
            rootNode = getEpgEspaceSuiviUIService().buildTree(context);
            rootNode.setIsOpen(true);
        } else {
            rootNode = getEpgEspaceSuiviUIService().loadRootNode(context);
            rootNode.setIsOpen(false);
        }
        return rootNode;
    }

    private List<AlerteDTO> getAlertes(SpecificContext context) {
        PageProviderService providerService = ServiceUtil.getRequiredService(PageProviderService.class);
        PageProviderDefinition def = providerService.getPageProviderDefinition("espace_suivi_mes_alertes_content");

        CoreQueryDocumentPageProvider provider = (CoreQueryDocumentPageProvider) PageProviderHelper.getPageProvider(
            context.getSession(),
            def,
            Collections.emptyMap()
        );
        return provider
            .getCurrentPage()
            .stream()
            .map(doc -> toAlerteDto(context, EpgURLConstants.URL_ALERTES, doc))
            .collect(Collectors.toList());
    }

    private AlerteDTO toAlerteDto(SpecificContext context, String path, DocumentModel doc) {
        Boolean isAlertActivated = SSActionsServiceLocator.getAlertActionService().isActivated(doc);
        context.putInContextData(SSContextDataKey.IS_ALERT_ACTIVATED, isAlertActivated);
        return new AlerteDTO(
            doc.getId(),
            doc.getTitle(),
            String.format(path, doc.getId()),
            isAlertActivated,
            context.getActions(ALERTE_ACTIONS)
        );
    }
}
