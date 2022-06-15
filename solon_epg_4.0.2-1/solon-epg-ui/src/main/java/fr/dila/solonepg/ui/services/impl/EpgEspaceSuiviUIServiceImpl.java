package fr.dila.solonepg.ui.services.impl;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgSuiviTreeElementDTO;
import fr.dila.solonepg.ui.services.EpgEspaceSuiviUIService;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.bean.TreeElementDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;

public class EpgEspaceSuiviUIServiceImpl implements EpgEspaceSuiviUIService {

    @Override
    public EpgSuiviTreeElementDTO addChildrenNode(SpecificContext context) {
        EpgSuiviTreeElementDTO child = null;
        OrganigrammeNode organigrammeNode;

        String id = context.getFromContextData(STContextDataKey.ID);
        String completeKey = context.getFromContextData(STContextDataKey.COMPLETE_KEY);
        STDossier.DossierState state = context.getFromContextData(STContextDataKey.DOSSIER_STATE);

        String nodeId = id;
        if (id.contains("dossiers")) {
            nodeId = completeKey;
        }
        organigrammeNode = SolonEpgServiceLocator.getEpgOrganigrammeService().getOrganigrammeNodeById(nodeId);

        EpgSuiviTreeElementDTO node = new EpgSuiviTreeElementDTO(
            organigrammeNode.getType(),
            organigrammeNode,
            null,
            state
        );
        node.setKey(id);
        node.setCompleteKey(completeKey);
        switch (node.getType()) {
            case GOUVERNEMENT:
                child = addMin(context, node);
                break;
            case MINISTERE:
                child = addDir(context, node);
                break;
            case DIRECTION:
            case UNITE_STRUCTURELLE:
                child = addPst(context, node);
                break;
            default:
                break;
        }
        return child;
    }

    @Override
    public EpgSuiviTreeElementDTO loadRootNode(SpecificContext context) {
        OrganigrammeType type = context.getFromContextData(STContextDataKey.TYPE);
        STDossier.DossierState state = context.getFromContextData(STContextDataKey.DOSSIER_STATE);

        final OrganigrammeNode currentGouv = STServiceLocator.getSTGouvernementService().getCurrentGouvernement();
        EpgSuiviTreeElementDTO nodeParent = new EpgSuiviTreeElementDTO(type, currentGouv, null, state);

        if (state == STDossier.DossierState.init) {
            nodeParent.setLabel("epg.sidebar.suivi.init");
        } else if (state == STDossier.DossierState.running) {
            nodeParent.setLabel("epg.sidebar.suivi.running");
        }
        nodeParent.setKey(state.getKey());
        nodeParent.setDossierState(state);
        nodeParent.setCompleteKey(currentGouv.getId());
        context.putInContextData(STContextDataKey.ID, currentGouv.getId());
        context.putInContextData(STContextDataKey.COMPLETE_KEY, currentGouv.getId());
        return nodeParent;
    }

    @Override
    public EpgSuiviTreeElementDTO buildTree(SpecificContext context) {
        EpgSuiviTreeElementDTO root = loadRootNodeWithChild(context);
        Set<String> openNode = context.getFromContextData(STContextDataKey.OPEN_NODE);
        getChildren(context, root, openNode);
        return root;
    }

    private void getChildren(SpecificContext context, EpgSuiviTreeElementDTO treeElementDTO, Set<String> openNode) {
        for (TreeElementDTO child : treeElementDTO.getChilds()) {
            if (openNode.contains(child.getKey())) {
                context.putInContextData(STContextDataKey.ID, child.getKey());
                child.setIsOpen(true);
                child.setChilds(addChildrenNode(context).getChilds());
                getChildren(context, (EpgSuiviTreeElementDTO) child, openNode);
            }
        }
    }

    @Override
    public Map<String, Set<String>> fillMailbox(SpecificContext context) {
        CoreSession session = context.getSession();
        String entiteNodeId = context.getFromContextData(STContextDataKey.ID);
        OrganigrammeNode entiteNode = SolonEpgServiceLocator
            .getEpgOrganigrammeService()
            .getOrganigrammeNodeById(entiteNodeId);

        Map<String, Set<String>> mailboxMap = new HashMap<>();
        List<Mailbox> mailboxList = new ArrayList<>();

        if (OrganigrammeType.POSTE == entiteNode.getType()) {
            mailboxList.add(SSServiceLocator.getMailboxPosteService().getMailboxPoste(session, entiteNode.getId()));
        } else if (
            OrganigrammeType.DIRECTION == entiteNode.getType() ||
            OrganigrammeType.UNITE_STRUCTURELLE == entiteNode.getType()
        ) {
            mailboxList =
                STServiceLocator
                    .getSTPostesService()
                    .getPosteIdInSubNode(entiteNode)
                    .stream()
                    .map(p -> SSServiceLocator.getMailboxPosteService().getMailboxPoste(session, p))
                    .collect(Collectors.toList());
        }

        mailboxMap.put(
            "mailboxDocIds",
            mailboxList.stream().filter(Objects::nonNull).map(m -> m.getDocument().getId()).collect(Collectors.toSet())
        );
        mailboxMap.put(
            "mailboxIds",
            mailboxList.stream().filter(Objects::nonNull).map(Mailbox::getId).collect(Collectors.toSet())
        );

        return mailboxMap;
    }

    private EpgSuiviTreeElementDTO loadRootNodeWithChild(SpecificContext context) {
        EpgSuiviTreeElementDTO root = loadRootNode(context);
        root.setChilds(addChildrenNode(context).getChilds());
        return root;
    }

    private EpgSuiviTreeElementDTO addMin(SpecificContext context, final EpgSuiviTreeElementDTO nodeParent) {
        List<OrganigrammeNode> listNode = new ArrayList<>();
        List<TreeElementDTO> children = new ArrayList<>();
        CoreSession session = context.getSession();

        SSPrincipal principal = (SSPrincipal) session.getPrincipal();
        if (principal.isMemberOf(SolonEpgBaseFunctionConstant.INFOCENTRE_GENERAL_FULL_READER)) {
            // Admin SGG
            final OrganigrammeNode currentGouv = STServiceLocator.getSTGouvernementService().getCurrentGouvernement();
            listNode.addAll(STServiceLocator.getOrganigrammeService().getChildrenList(session, currentGouv, true));
        } else {
            Set<String> idsMinistere;
            if (principal.isMemberOf(SolonEpgBaseFunctionConstant.INFOCENTRE_GENERAL_DIR_READER)) {
                idsMinistere = principal.getDirectionIdSet();
                final STUsAndDirectionService usService = STServiceLocator.getSTUsAndDirectionService();
                listNode =
                    idsMinistere
                        .stream()
                        .map(usService::getUniteStructurelleNode)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            } else {
                idsMinistere = principal.getMinistereIdSet();
                final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
                listNode =
                    idsMinistere
                        .stream()
                        .map(ministeresService::getEntiteNode)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        }

        listNode
            .stream()
            .filter(
                e ->
                    OrganigrammeType.POSTE != (e.getType()) &&
                    e.isActive() &&
                    (e.getDateFin() == null || e.getDateFin().compareTo(Calendar.getInstance().getTime()) > 0)
            )
            .forEach(
                e -> {
                    final StringBuilder label = new StringBuilder();
                    if (e instanceof EntiteNode) {
                        String nodeLabel = e.getLabel();
                        if (StringUtils.isNotBlank(nodeLabel) && nodeLabel.startsWith("NOR ")) {
                            // Skip des ministeres ne servant qu'à avoir un lien sur d'ancien NOR dont tous
                            // les dossiers sont publiés
                            return;
                        }
                        label.append(((EntiteNode) e).getEdition());
                    } else if (e instanceof UniteStructurelleNode) {
                        final List<OrganigrammeNode> listParent = SolonEpgServiceLocator
                            .getEpgOrganigrammeService()
                            .getParentList(e);
                        if (CollectionUtils.isNotEmpty(listParent)) {
                            label
                                .append(
                                    ((UniteStructurelleNode) e).getNorDirectionForMinistereId(listParent.get(0).getId())
                                )
                                .append(" - ")
                                .append(e.getLabel());
                        }
                    }

                    EpgSuiviTreeElementDTO treeDTO = createEspaceSuiviTreeElementDTO(
                        context,
                        e,
                        nodeParent,
                        label.toString()
                    );
                    children.add(treeDTO);
                }
            );

        nodeParent.setChilds(children);
        nodeParent.setIsOpen(true);
        return nodeParent;
    }

    private EpgSuiviTreeElementDTO addPst(SpecificContext context, final EpgSuiviTreeElementDTO nodeParent) {
        List<TreeElementDTO> children = new ArrayList<>();

        final OrganigrammeNode organigrammeNode = nodeParent.getNode();
        if (organigrammeNode != null) {
            SolonEpgServiceLocator
                .getEpgOrganigrammeService()
                .getChildrenList(context.getSession(), organigrammeNode, true)
                .stream()
                .filter(e -> OrganigrammeType.USER != e.getType())
                .forEach(
                    e -> {
                        EpgSuiviTreeElementDTO treeDTO = createEspaceSuiviTreeElementDTO(
                            context,
                            e,
                            nodeParent,
                            e.getLabel()
                        );
                        children.add(treeDTO);
                    }
                );
        }
        nodeParent.setChilds(children);
        nodeParent.setIsOpen(true);
        return nodeParent;
    }

    private EpgSuiviTreeElementDTO addDir(SpecificContext context, EpgSuiviTreeElementDTO nodeParent) {
        List<TreeElementDTO> children = new ArrayList<>();
        final OrganigrammeNode organigrammeNode = nodeParent.getNode();

        if (organigrammeNode != null) {
            SolonEpgServiceLocator
                .getEpgOrganigrammeService()
                .getChildrenList(context.getSession(), organigrammeNode, true)
                .forEach(
                    e -> {
                        StringBuilder name = new StringBuilder();
                        if (e instanceof UniteStructurelleNode) {
                            name
                                .append(
                                    ((UniteStructurelleNode) e).getNorDirectionForMinistereId(organigrammeNode.getId())
                                )
                                .append(" - ");
                        }
                        name.append(e.getLabel());
                        EpgSuiviTreeElementDTO treeDTO = createEspaceSuiviTreeElementDTO(
                            context,
                            e,
                            nodeParent,
                            name.toString()
                        );
                        children.add(treeDTO);
                    }
                );
        }

        nodeParent.setChilds(children);
        nodeParent.setIsOpen(true);
        return nodeParent;
    }

    private String getDossierCountQuery(SpecificContext context) {
        CoreSession session = context.getSession();
        STDossier.DossierState state = context.getFromContextData(STContextDataKey.DOSSIER_STATE);

        Map<String, Set<String>> mailboxMap = fillMailbox(context);

        return SolonEpgServiceLocator
            .getCorbeilleService()
            .getInfocentreQuery(session, state, mailboxMap.get("mailboxIds"));
    }

    private EpgSuiviTreeElementDTO createEspaceSuiviTreeElementDTO(
        SpecificContext context,
        OrganigrammeNode entiteNode,
        EpgSuiviTreeElementDTO nodeParent,
        String label
    ) {
        CoreSession session = context.getSession();
        Long count = 0L;
        String nodeName = label;
        String link = null;
        boolean isLastLevel = false;
        context.putInContextData(STContextDataKey.ID, entiteNode.getId());
        context.putInContextData(
            STContextDataKey.COMPLETE_KEY,
            nodeParent.getCompleteKey() + "__" + entiteNode.getId()
        );

        if (OrganigrammeType.MINISTERE != (entiteNode.getType())) {
            final String query = getDossierCountQuery(context);
            count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(query));
            nodeName += " - " + count;
            if (count != 0L) {
                link =
                    "/suivi/liste?cle=" +
                    entiteNode.getId() +
                    "&state=" +
                    nodeParent.getDossierState() +
                    "&label=" +
                    label;
            }
        }

        if (OrganigrammeType.POSTE == entiteNode.getType()) {
            isLastLevel = true;
        }

        EpgSuiviTreeElementDTO epgSuiviTreeElementDTO = new EpgSuiviTreeElementDTO();
        epgSuiviTreeElementDTO.setType(entiteNode.getType());
        epgSuiviTreeElementDTO.setNode(entiteNode);
        epgSuiviTreeElementDTO.setCount(count);
        epgSuiviTreeElementDTO.setDossierState(nodeParent.getDossierState());
        epgSuiviTreeElementDTO.setLabel(nodeName);
        epgSuiviTreeElementDTO.setIsOpen(false);
        epgSuiviTreeElementDTO.setIsLastLevel(isLastLevel);
        epgSuiviTreeElementDTO.setKey(entiteNode.getId());
        epgSuiviTreeElementDTO.setCompleteKey(nodeParent.getCompleteKey() + "__" + entiteNode.getId());
        epgSuiviTreeElementDTO.setLink(link);

        return epgSuiviTreeElementDTO;
    }
}
