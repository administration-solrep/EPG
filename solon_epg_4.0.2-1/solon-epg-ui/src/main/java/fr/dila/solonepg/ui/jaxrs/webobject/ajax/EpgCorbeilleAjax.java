package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import static fr.dila.solonepg.ui.enums.EpgContextDataKey.CURRENT_TREE_SUIVI;
import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getEpgEspaceSuiviUIService;
import static fr.dila.solonepg.ui.services.impl.EpgSuiviMenuServiceImpl.DOSSIERS_CREES_OPEN_NODES_KEY;
import static fr.dila.solonepg.ui.services.impl.EpgSuiviMenuServiceImpl.DOSSIERS_RUNNING_OPEN_NODES_KEY;

import fr.dila.solonepg.ui.bean.EpgSuiviTreeElementDTO;
import fr.dila.solonepg.ui.services.EpgMailboxListComponentService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.ss.ui.jaxrs.webobject.ajax.SSCorbeilleAjax;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "CorbeilleAjax")
public class EpgCorbeilleAjax extends SSCorbeilleAjax {

    public EpgCorbeilleAjax() {
        super();
    }

    @GET
    @Path("suivi")
    public ThTemplate getSuiviArbre(
        @QueryParam("rootTreeId") String rootTreeId,
        @QueryParam("key") String selectedNode,
        @QueryParam("isOpening") Boolean isOpening
    ) {
        // Je déclare mon template et j'instancie mon context
        ThTemplate template = new AjaxLayoutThTemplate();
        template.setName("fragments/leftblocks/suivi-arbre");
        SpecificContext context = getMyContext();
        String userSessionHelperConstant = STDossier.DossierState.init.getKey().equals(rootTreeId)
            ? DOSSIERS_CREES_OPEN_NODES_KEY
            : DOSSIERS_RUNNING_OPEN_NODES_KEY;
        Map<String, Object> mapContext = new HashMap<>();
        if (StringUtils.isNotBlank(rootTreeId)) {
            context.putInContextData(CURRENT_TREE_SUIVI, rootTreeId);
        }

        String key = selectedNode;

        if (selectedNode.contains("__")) {
            String[] split = selectedNode.split("__");
            int length = split.length;
            key = split[length - 1];
        }
        if (StringUtils.isNotBlank(key)) {
            // Si un noeud de l'arbre est sélectionné
            Set<String> openNodes = Optional
                .ofNullable(UserSessionHelper.getUserSessionParameter(context, userSessionHelperConstant, Set.class))
                .orElseGet(() -> new HashSet());

            Set<String> openNodesCompleteKey = Optional
                .ofNullable(
                    UserSessionHelper.getUserSessionParameter(
                        context,
                        userSessionHelperConstant + "-complete-key",
                        Set.class
                    )
                )
                .orElseGet(() -> new HashSet());

            // On l'ajoute ou l'enlève de la liste des noeuds actifs
            if (BooleanUtils.isTrue(isOpening)) {
                openNodes.add(key);
                openNodesCompleteKey.add(selectedNode);
            } else {
                openNodes.remove(key);
                openNodesCompleteKey.remove(selectedNode);
                for (String completeKey : openNodesCompleteKey) {
                    if (completeKey.contains(key)) {
                        for (String k : completeKey.split("__")) {
                            openNodes.remove(k);
                        }
                    }
                }
            }

            // On sauvegarde la nouvelle liste de noeuds ouverts dans la
            // UserSession
            UserSessionHelper.putUserSessionParameter(context, userSessionHelperConstant, openNodes);
            UserSessionHelper.putUserSessionParameter(
                context,
                userSessionHelperConstant + "-complete-key",
                openNodesCompleteKey
            );
        }

        context.setContextData(mapContext);

        template.setContext(context);

        Set<String> constructTreeNode = UserSessionHelper.getUserSessionParameter(context, userSessionHelperConstant);

        STDossier.DossierState dossierState = STDossier.DossierState.init;

        if (!STDossier.DossierState.init.getKey().equals(rootTreeId)) {
            dossierState = STDossier.DossierState.running;
        }
        context.putInContextData(STContextDataKey.OPEN_NODE, constructTreeNode);
        context.putInContextData(STContextDataKey.DOSSIER_STATE, dossierState);
        context.putInContextData(STContextDataKey.TYPE, OrganigrammeType.GOUVERNEMENT);
        context.putInContextData(STContextDataKey.ID, rootTreeId);

        EpgSuiviTreeElementDTO root = getEpgEspaceSuiviUIService().buildTree(context);
        root.setIsOpen(CollectionUtils.isNotEmpty(constructTreeNode));

        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.TREE, root.getChilds());
        map.put(STTemplateConstants.LEVEL, 2);
        map.put(STTemplateConstants.CURRENT_ID, rootTreeId);
        map.put(STTemplateConstants.ROOT_ID, rootTreeId);
        map.put(STTemplateConstants.IS_OPEN, CollectionUtils.isNotEmpty(constructTreeNode));
        template.setData(map);

        return template;
    }

    @Override
    protected EpgMailboxListComponentService getMyService() {
        return EpgUIServiceLocator.getEpgMailboxListComponentService();
    }
}
