package fr.dila.solonepg.ui.services.mgpp.impl;

import static fr.dila.solonmgpp.core.service.TableReferenceServiceImpl.MANDATS_SANS_MINISTERES;

import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.ui.bean.MgppReferenceNode;
import fr.dila.solonepg.ui.bean.MgppTableReferenceEPP;
import fr.dila.solonepg.ui.bean.TableReferenceEPPElementDTO;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppTableReferenceUIService;
import fr.dila.solonepg.ui.th.bean.MgppIdentiteForm;
import fr.dila.solonepg.ui.th.bean.MgppMandatForm;
import fr.dila.solonepg.ui.th.bean.MgppMinistereForm;
import fr.dila.solonmgpp.api.node.GouvernementNode;
import fr.dila.solonmgpp.api.node.IdentiteNode;
import fr.dila.solonmgpp.api.node.MandatNode;
import fr.dila.solonmgpp.api.node.MinistereNode;
import fr.dila.solonmgpp.api.node.TableReferenceEppNode;
import fr.dila.solonmgpp.core.node.GouvernementNodeImpl;
import fr.dila.solonmgpp.core.node.IdentiteNodeImpl;
import fr.dila.solonmgpp.core.node.MandatNodeImpl;
import fr.dila.solonmgpp.core.node.MinistereNodeImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.bean.GouvernementForm;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.xsd.solon.epp.ObjetType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;

public class MgppTableReferenceUIServiceImpl implements MgppTableReferenceUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(MgppTableReferenceUIServiceImpl.class);

    private static final String NODE_KEY = "node";
    public static final String TABLE_REFERENCE_EPP_KEY = "tableReferenceEPP";
    private static final String CURRENT_GOUV_EPP_KEY = "currentGouvEPP";
    public static final String TABLE_REFERENCE_EPP_OPEN_NODES_KEY = "tableReferenceEPP_openNodes";

    @Override
    public List<SelectValueDTO> getCurrentMinisteres(SpecificContext context) {
        CoreSession session = context.getSession();
        List<SelectValueDTO> lstMins = new ArrayList<>();
        try {
            GouvernementNode gouv = SolonMgppServiceLocator.getTableReferenceService().getCurrentGouvernement(session);
            List<TableReferenceEppNode> lstMinNodes = SolonMgppServiceLocator
                .getTableReferenceService()
                .getChildrenList(gouv, session);
            if (CollectionUtils.isNotEmpty(lstMinNodes)) {
                lstMins =
                    lstMinNodes
                        .stream()
                        .map(node -> new SelectValueDTO(((MinistereNode) node).getAppellation()))
                        .collect(Collectors.toList());
            }
        } catch (EPGException e) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_GET_WS_EPP_TEC, e);
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("tableref.epp.error"));
        }

        return lstMins;
    }

    @Override
    public MgppTableReferenceEPP getCurrentGouvernement(SpecificContext context) {
        MgppTableReferenceEPP tableReference = UserSessionHelper.getUserSessionParameter(
            context,
            CURRENT_GOUV_EPP_KEY,
            MgppTableReferenceEPP.class
        );

        CoreSession session = context.getSession();

        if (tableReference == null) {
            GouvernementNode gouv = SolonMgppServiceLocator.getTableReferenceService().getCurrentGouvernement(session);
            tableReference = new MgppTableReferenceEPP();

            if (gouv != null) {
                TableReferenceEPPElementDTO elementGouv = convertToTableReferenceElement(
                    context,
                    gouv,
                    "javascript:void(0);",
                    null
                );
                elementGouv.setIsOpen(true);
                elementGouv.setIsLastLevel(false);
                elementGouv.setActions(null);

                List<TableReferenceEppNode> lstMins = SolonMgppServiceLocator
                    .getTableReferenceService()
                    .getChildrenList(gouv, session);
                List<TableReferenceEPPElementDTO> elementsMin = lstMins
                    .stream()
                    .map(
                        min ->
                            convertToTableReferenceElement(
                                context,
                                min,
                                "javascript:void(0);",
                                MgppActionCategory.TABLE_REFERENCE_MINSELECTEUR_ACTIONS
                            )
                    )
                    .sorted(Comparator.comparingLong(child -> getOrdreProtocolaire(context, child)))
                    .collect(Collectors.toList());
                elementGouv.setChilds(elementsMin);

                tableReference.getListe().add(elementGouv);
                UserSessionHelper.putUserSessionParameter(context, CURRENT_GOUV_EPP_KEY, tableReference);
            } else {
                tableReference.getListe().forEach(elem -> openOrCloseElement(context, elem));
            }
        }

        return tableReference;
    }

    @Override
    public MgppTableReferenceEPP getTableReferenceEPP(SpecificContext context) {
        MgppTableReferenceEPP tableReference = UserSessionHelper.getUserSessionParameter(
            context,
            TABLE_REFERENCE_EPP_KEY,
            MgppTableReferenceEPP.class
        );

        CoreSession session = context.getSession();

        if (tableReference == null) {
            tableReference = new MgppTableReferenceEPP();
            try {
                List<TableReferenceEPPElementDTO> elements = SolonMgppServiceLocator
                    .getTableReferenceService()
                    .getGouvernementList(session)
                    .stream()
                    .map(
                        gouv ->
                            convertToTableReferenceElement(
                                context,
                                gouv,
                                String.format("onOpenTableReferenceNode('%s')", gouv.getIdentifiant()),
                                MgppActionCategory.TABLE_REFERENCE_EPP_ACTIONS
                            )
                    )
                    .collect(Collectors.toList());

                tableReference.setListe(elements);
            } catch (NuxeoException e) {
                LOGGER.error(session, STLogEnumImpl.FAIL_GET_GOUVERNEMENT_TEC, e);
                context.getMessageQueue().addErrorToQueue(e.getMessage());
            }
        } else {
            tableReference.getListe().forEach(elementDTO -> openOrCloseElement(context, elementDTO));
        }

        UserSessionHelper.putUserSessionParameter(context, TABLE_REFERENCE_EPP_KEY, tableReference);

        return tableReference;
    }

    private static TableReferenceEPPElementDTO convertToTableReferenceElement(
        SpecificContext context,
        TableReferenceEppNode node,
        String clickLink,
        MgppActionCategory category
    ) {
        TableReferenceEPPElementDTO elementDTO = new TableReferenceEPPElementDTO();
        elementDTO.setTableReferenceEppNode(node);
        elementDTO.setKey(node.getIdentifiant());
        elementDTO.setType(node.getTypeValue());
        elementDTO.setLabel(node.getLabel());
        elementDTO.setLink(clickLink);
        elementDTO.setIsActive(node.getDateFin() == null || (new Date()).before(node.getDateFin()));
        elementDTO.setIsLastLevel(BooleanUtils.isNotTrue(node.isHasChildren()));
        openOrCloseElement(context, elementDTO);
        context.putInContextData(NODE_KEY, elementDTO);
        if (category != null) {
            elementDTO.setActions(context.getActions(category));
        }

        elementDTO.setOrganigrammeNode(
            new MgppReferenceNode(elementDTO.getKey(), elementDTO.getType(), elementDTO.getMinistereId())
        );
        return elementDTO;
    }

    private static void openOrCloseElement(SpecificContext context, TableReferenceEPPElementDTO elementDTO) {
        @SuppressWarnings("unchecked")
        Set<String> openNodes = ObjectHelper.requireNonNullElse(
            UserSessionHelper.getUserSessionParameter(context, TABLE_REFERENCE_EPP_OPEN_NODES_KEY, Set.class),
            Collections.emptySet()
        );
        elementDTO.setIsOpen(openNodes.contains(elementDTO.getKey()));
        if (BooleanUtils.isTrue(elementDTO.getIsOpen()) && CollectionUtils.isEmpty(elementDTO.getChilds())) {
            elementDTO.setChilds(getchildrenListForElement(context, elementDTO));
        } else if (BooleanUtils.isTrue(elementDTO.getIsOpen())) {
            elementDTO.getChilds().forEach(child -> openOrCloseElement(context, (TableReferenceEPPElementDTO) child));
        }
    }

    private static List<TableReferenceEPPElementDTO> getchildrenListForElement(
        SpecificContext context,
        TableReferenceEPPElementDTO elementDTO
    ) {
        return SolonMgppServiceLocator
            .getTableReferenceService()
            .getChildrenList(elementDTO.getTableReferenceEppNode(), context.getSession())
            .stream()
            .map(
                child ->
                    convertToTableReferenceElement(
                        context,
                        child,
                        String.format("onOpenTableReferenceNode('%s')", child.getIdentifiant()),
                        MgppActionCategory.TABLE_REFERENCE_EPP_ACTIONS
                    )
            )
            .sorted(Comparator.comparingLong(child -> getOrdreProtocolaire(context, child)))
            .collect(Collectors.toList());
    }

    private static Long getOrdreProtocolaire(SpecificContext context, TableReferenceEPPElementDTO elementDTO) {
        TableReferenceEppNode node = elementDTO.getTableReferenceEppNode();
        if (node instanceof MandatNode) {
            return ((MandatNode) node).getOrdreProtocolaire();
        } else if (ObjetType.MINISTERE.value().equals(elementDTO.getType())) {
            if (BooleanUtils.isTrue(node.isHasChildren()) && CollectionUtils.isEmpty(elementDTO.getChilds())) {
                elementDTO.setChilds(getchildrenListForElement(context, elementDTO));
            }
            return elementDTO
                .getChilds()
                .stream()
                .findFirst()
                .map(TableReferenceEPPElementDTO.class::cast)
                .map(child -> getOrdreProtocolaire(context, child))
                .orElse(Long.MAX_VALUE);
        }
        return 0L;
    }

    @Override
    public GouvernementForm getGouvernementFormFromId(SpecificContext context) {
        String idGvt = context.getFromContextData(STContextDataKey.ID);
        GouvernementNode gouvernementNode = SolonMgppServiceLocator
            .getTableReferenceService()
            .getGouvernement(idGvt, context.getSession());
        GouvernementForm gouvernementForm = new GouvernementForm();
        gouvernementForm.setId(gouvernementNode.getIdentifiant());
        gouvernementForm.setAppellation(gouvernementNode.getAppellation());
        gouvernementForm.setDateDebut(SolonDateConverter.DATE_SLASH.format(gouvernementNode.getDateDebut()));
        gouvernementForm.setDateFin(SolonDateConverter.DATE_SLASH.format(gouvernementNode.getDateFin()));
        return gouvernementForm;
    }

    @Override
    public void creerGouvernement(SpecificContext context) {
        GouvernementNode gouvernementNode = getGouvernementNodeFromForm(context);
        try {
            SolonMgppServiceLocator
                .getTableReferenceService()
                .createGouvernement(gouvernementNode, context.getSession());
            context
                .getMessageQueue()
                .addToastSuccess(ResourceHelper.getString("info.organigrammeManager.gouvernementCreated"));
            // Supprimer la table de référence sauvegardée en mémoire
            UserSessionHelper.clearUserSessionParameter(context, TABLE_REFERENCE_EPP_KEY);
        } catch (NuxeoException e) {
            LOGGER.error(context.getSession(), STLogEnumImpl.FAIL_CREATE_GOUVERNEMENT_TEC, e);
            context.getMessageQueue().addErrorToQueue(e.getMessage());
        }
    }

    @Override
    public void modifierGouvernement(SpecificContext context) {
        GouvernementNode gouvernementNode = getGouvernementNodeFromForm(context);
        try {
            SolonMgppServiceLocator
                .getTableReferenceService()
                .updateGouvernement(gouvernementNode, context.getSession());
            context
                .getMessageQueue()
                .addToastSuccess(ResourceHelper.getString("info.organigrammeManager.gouvernementModified"));
            // Supprimer la table de référence sauvegardée en mémoire
            UserSessionHelper.clearUserSessionParameter(context, TABLE_REFERENCE_EPP_KEY);
        } catch (NuxeoException e) {
            LOGGER.error(context.getSession(), STLogEnumImpl.FAIL_UPDATE_GOUVERNEMENT_TEC, e);
            context.getMessageQueue().addErrorToQueue(e.getMessage());
        }
    }

    private static GouvernementNode getGouvernementNodeFromForm(SpecificContext context) {
        GouvernementForm gouvernementForm = context.getFromContextData(STContextDataKey.GVT_FORM);
        GouvernementNode gouvernementNode = new GouvernementNodeImpl();
        gouvernementNode.setIdentifiant(gouvernementForm.getId());
        gouvernementNode.setAppellation(gouvernementForm.getAppellation());
        gouvernementNode.setDateDebut(SolonDateConverter.DATE_SLASH.parseToDateOrNull(gouvernementForm.getDateDebut()));
        gouvernementNode.setDateFin(SolonDateConverter.DATE_SLASH.parseToDateOrNull(gouvernementForm.getDateFin()));
        return gouvernementNode;
    }

    @Override
    public MgppMinistereForm getMinistereFormFromId(SpecificContext context) {
        String idMinistere = context.getFromContextData(STContextDataKey.ID);
        MinistereNode ministereNode = SolonMgppServiceLocator
            .getTableReferenceService()
            .getMinistere(idMinistere, context.getSession());
        MgppMinistereForm ministereForm = new MgppMinistereForm();
        ministereForm.setIdentifiant(ministereNode.getIdentifiant());
        ministereForm.setIdGouvernement(ministereNode.getGouvernement());
        ministereForm.setNom(ministereNode.getNom());
        ministereForm.setLibelle(ministereNode.getLibelle());
        ministereForm.setAppellation(ministereNode.getAppellation());
        ministereForm.setEdition(ministereNode.getEdition());
        ministereForm.setDateDebut(SolonDateConverter.DATE_SLASH.format(ministereNode.getDateDebut()));
        ministereForm.setDateFin(SolonDateConverter.DATE_SLASH.format(ministereNode.getDateFin()));
        return ministereForm;
    }

    @Override
    public void creerMinistere(SpecificContext context) {
        MinistereNode ministereNode = getMinistereNodeFromForm(context);
        try {
            SolonMgppServiceLocator.getTableReferenceService().createMinistere(ministereNode, context.getSession());
            context
                .getMessageQueue()
                .addToastSuccess(ResourceHelper.getString("info.organigrammeManager.entiteCreated"));
            // Supprimer la table de référence sauvegardée en mémoire
            UserSessionHelper.clearUserSessionParameter(context, TABLE_REFERENCE_EPP_KEY);
        } catch (NuxeoException e) {
            LOGGER.error(context.getSession(), STLogEnumImpl.FAIL_CREATE_MINISTERE_TEC, e);
            context.getMessageQueue().addErrorToQueue(e.getMessage());
        }
    }

    @Override
    public void modifierMinistere(SpecificContext context) {
        MinistereNode ministereNode = getMinistereNodeFromForm(context);
        try {
            SolonMgppServiceLocator.getTableReferenceService().updateMinistere(ministereNode, context.getSession());
            context
                .getMessageQueue()
                .addToastSuccess(ResourceHelper.getString("info.organigrammeManager.entiteModified"));
            // Supprimer la table de référence sauvegardée en mémoire
            UserSessionHelper.clearUserSessionParameter(context, TABLE_REFERENCE_EPP_KEY);
        } catch (NuxeoException e) {
            LOGGER.error(context.getSession(), STLogEnumImpl.FAIL_UPDATE_MINISTERE_TEC, e);
            context.getMessageQueue().addErrorToQueue(e.getMessage());
        }
    }

    private static MinistereNode getMinistereNodeFromForm(SpecificContext context) {
        MgppMinistereForm ministereForm = context.getFromContextData(MgppContextDataKey.MINISTERE_FORM);
        MinistereNode ministereNode = new MinistereNodeImpl();
        ministereNode.setIdentifiant(ministereForm.getIdentifiant());
        ministereNode.setGouvernement(ministereForm.getIdGouvernement());
        ministereNode.setNom(ministereForm.getNom());
        ministereNode.setLibelle(ministereForm.getLibelle());
        ministereNode.setAppellation(ministereForm.getAppellation());
        ministereNode.setEdition(ministereForm.getEdition());
        ministereNode.setDateDebut(SolonDateConverter.DATE_SLASH.parseToDateOrNull(ministereForm.getDateDebut()));
        ministereNode.setDateFin(SolonDateConverter.DATE_SLASH.parseToDateOrNull(ministereForm.getDateFin()));
        return ministereNode;
    }

    @Override
    public MgppMandatForm getMandat(SpecificContext context) {
        String id = context.getFromContextData(STContextDataKey.ID);
        MgppMandatForm form = new MgppMandatForm();
        MandatNode mandat = SolonMgppServiceLocator.getTableReferenceService().getMandat(id, context.getSession());

        form.setAppellation(mandat.getAppellation());
        form.setDateDebutMandat(DateUtil.dateToGregorianCalendar(mandat.getDateDebut()));
        form.setDateFinMandat(DateUtil.dateToGregorianCalendar(mandat.getDateFin()));
        form.setIdMandat(mandat.getIdentifiant());
        form.setNor(mandat.getNor());
        form.setOrdreProtocolaire(mandat.getOrdreProtocolaire());
        form.setTitre(mandat.getTitre());
        form.setTypeMandat(mandat.getTypeMandat());

        context.putInContextData(
            MgppContextDataKey.IDENTITE_NOM,
            mandat.getIdentite().getPrenom() + " " + mandat.getIdentite().getNom()
        );

        return form;
    }

    @Override
    public MgppIdentiteForm getIdentite(SpecificContext context) {
        String id = context.getFromContextData(MgppContextDataKey.IDENTITE_NOM);
        MgppIdentiteForm form = new MgppIdentiteForm();
        IdentiteNode identite = SolonMgppServiceLocator
            .getTableReferenceService()
            .getIdentiteNode(context.getSession(), id);

        form.setCivilite(identite.getCivilite());
        form.setDateDebutIdentite(DateUtil.dateToGregorianCalendar(identite.getDateDebut()));
        form.setDateFinIdentite(DateUtil.dateToGregorianCalendar(identite.getDateFin()));
        form.setDateNaissance(DateUtil.dateToGregorianCalendar(identite.getDateNaissance()));
        form.setDepartementNaissance(identite.getDeptNaissance());
        form.setIdIdentite(identite.getIdentifiant());
        form.setLieuNaissance(identite.getLieuNaissance());
        form.setNom(identite.getNom());
        form.setPaysNaissance(identite.getPaysNaissance());
        form.setPrenom(identite.getPrenom());

        context.putInContextData(MgppContextDataKey.ACTEUR_ID, identite.getActeur().getIdentifiant());

        return form;
    }

    @Override
    public void saveMandat(SpecificContext context) {
        MgppMandatForm mandatForm = context.getFromContextData(MgppContextDataKey.MANDAT_FORM);

        MandatNode mandat = SolonMgppServiceLocator
            .getTableReferenceService()
            .getMandat(mandatForm.getIdMandat(), context.getSession());
        if (mandat != null) {
            mapMandatFormToNode(mandat, mandatForm);
            SolonMgppServiceLocator.getTableReferenceService().updateMandat(mandat, context.getSession());
            context.getMessageQueue().addToastSuccess(ResourceHelper.getString("action.save.mandat.success"));
            UserSessionHelper.clearUserSessionParameter(context, TABLE_REFERENCE_EPP_KEY);
        } else {
            context.getMessageQueue().addErrorToQueue("action.save.mandat.error");
        }
    }

    private void mapMandatFormToNode(MandatNode mandat, MgppMandatForm form) {
        mandat.setAppellation(form.getAppellation());
        mandat.setDateDebut(form.getDateDebutMandat().getTime());
        if (form.getDateFinMandat() != null) {
            mandat.setDateFin(form.getDateFinMandat().getTime());
        }
        mandat.setNor(form.getNor());
        mandat.setOrdreProtocolaire(form.getOrdreProtocolaire());
        mandat.setTypeMandat(form.getTypeMandat());
        mandat.setTitre(form.getTitre());
    }

    private void mapIdentiteFormToNode(IdentiteNode identite, MgppIdentiteForm form) {
        identite.setCivilite(form.getCivilite());
        identite.setDateDebut(form.getDateDebutIdentite().getTime());
        if (form.getDateFinIdentite() != null) {
            identite.setDateFin(form.getDateFinIdentite().getTime());
        }
        if (form.getDateNaissance() != null) {
            identite.setDateNaissance(form.getDateNaissance().getTime());
        }
        identite.setDeptNaissance(form.getDepartementNaissance());
        identite.setLieuNaissance(form.getLieuNaissance());
        identite.setNom(form.getNom());
        identite.setPaysNaissance(form.getPaysNaissance());
        identite.setPrenom(form.getPrenom());
    }

    @Override
    public void saveIdentite(SpecificContext context) {
        MgppIdentiteForm identiteForm = context.getFromContextData(MgppContextDataKey.IDENTITE_FORM);
        final CoreSession session = context.getSession();
        IdentiteNode identite = SolonMgppServiceLocator
            .getTableReferenceService()
            .getIdentiteNodeFromId(session, identiteForm.getIdIdentite());

        String id = context.getFromContextData(STContextDataKey.ID);
        MandatNode mandat = SolonMgppServiceLocator.getTableReferenceService().getMandat(id, session);

        if (identite != null) {
            mapIdentiteFormToNode(identite, identiteForm);
            mandat.setIdentite(identite);

            SolonMgppServiceLocator.getTableReferenceService().updateIdentite(mandat, session);
            context.getMessageQueue().addToastSuccess(ResourceHelper.getString("action.save.identite.success"));
            UserSessionHelper.clearUserSessionParameter(context, TABLE_REFERENCE_EPP_KEY);
        } else if (StringUtils.isBlank(identiteForm.getIdIdentite())) {
            identite = new IdentiteNodeImpl();

            mapIdentiteFormToNode(identite, identiteForm);

            // On transfère l'acteur
            identite.setActeur(mandat.getIdentite().getActeur());
            mandat.setIdentite(identite);

            SolonMgppServiceLocator.getTableReferenceService().createIdentite(mandat, session);
            context.getMessageQueue().addToastSuccess(ResourceHelper.getString("action.create.identite.success"));
            UserSessionHelper.clearUserSessionParameter(context, TABLE_REFERENCE_EPP_KEY);
        } else {
            context.getMessageQueue().addErrorToQueue("action.save.identite.error");
        }
    }

    @Override
    public String newActeur(SpecificContext context) {
        String id = context.getFromContextData(STContextDataKey.ID);
        CoreSession session = context.getSession();
        MandatNode mandat = SolonMgppServiceLocator.getTableReferenceService().getMandat(id, session);
        SolonMgppServiceLocator.getTableReferenceService().createActeur(mandat, session);
        context.getMessageQueue().addToastSuccess(ResourceHelper.getString("action.create.acteur.success"));
        mandat = SolonMgppServiceLocator.getTableReferenceService().getMandat(id, session);

        return mandat.getIdentite().getActeur().getIdentifiant();
    }

    @Override
    public void creerMandatComplet(SpecificContext context) {
        MgppMandatForm mandatForm = context.getFromContextData(MgppContextDataKey.MANDAT_FORM);
        MgppIdentiteForm identiteForm = context.getFromContextData(MgppContextDataKey.IDENTITE_FORM);
        String ministere = context.getFromContextData(STContextDataKey.MINISTERE_ID);
        MandatNode mandat = new MandatNodeImpl();
        mandat.setMinistere(!MANDATS_SANS_MINISTERES.equals(ministere) ? ministere : null);

        mapMandatFormToNode(mandat, mandatForm);
        mapIdentiteFormToNode(mandat.getIdentite(), identiteForm);
        SolonMgppServiceLocator.getTableReferenceService().createMandat(mandat, context.getSession());
        context.getMessageQueue().addToastSuccess(ResourceHelper.getString("action.create.mandat.success"));
        UserSessionHelper.clearUserSessionParameter(context, TABLE_REFERENCE_EPP_KEY);
    }
}
