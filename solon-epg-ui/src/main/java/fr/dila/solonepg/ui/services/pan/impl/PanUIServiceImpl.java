package fr.dila.solonepg.ui.services.pan.impl;

import static fr.dila.solonepg.ui.enums.pan.PanContextDataKey.ACTIVITE_NORMATIVE_NOR;
import static fr.dila.solonepg.ui.enums.pan.PanContextDataKey.CURRENT_SECTION;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.cases.TranspositionDirective;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.core.dto.activitenormative.DecretDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.FicheSignaletiqueApplicationOrdonnanceDTO;
import fr.dila.solonepg.core.dto.activitenormative.FicheSignaletiqueLoiDTO;
import fr.dila.solonepg.core.dto.activitenormative.FicheSignaletiqueOrdonnanceDTO;
import fr.dila.solonepg.core.dto.activitenormative.MesureApplicativeDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.OrdonnanceDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreLoiDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreTraiteDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.TranspositionDirectiveDTOImpl;
import fr.dila.solonepg.core.exception.ActiviteNormativeException;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.pan.AbstractPanSortedList;
import fr.dila.solonepg.ui.bean.pan.ConsultTexteMaitreDTO;
import fr.dila.solonepg.ui.bean.pan.DecretsPanUnsortedList;
import fr.dila.solonepg.ui.bean.pan.MesuresApplicativesPanUnsortedList;
import fr.dila.solonepg.ui.bean.pan.OrdonnancesListPan;
import fr.dila.solonepg.ui.bean.pan.PanActionsDTO;
import fr.dila.solonepg.ui.bean.pan.PanOnglet;
import fr.dila.solonepg.ui.bean.pan.TextesMaitresListPan;
import fr.dila.solonepg.ui.bean.pan.TraitesListPan;
import fr.dila.solonepg.ui.bean.pan.TranspositionDirectivesListPan;
import fr.dila.solonepg.ui.contentview.AbstractPanPageProvider;
import fr.dila.solonepg.ui.contentview.PanDecretsPageProvider;
import fr.dila.solonepg.ui.contentview.PanMainTableauPageProvider;
import fr.dila.solonepg.ui.contentview.PanMesuresPageProvider;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.services.pan.PanUIService;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.bean.Onglet;
import fr.dila.st.ui.bean.OngletConteneur;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.platform.actions.Action;

/**
 * Implémentation du servce UI de l'espace d'activite normative. Migré depuis
 * ActiviteNormativeActionsBean.
 *
 * @author asatre
 */
public class PanUIServiceImpl implements PanUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(PanUIServiceImpl.class);

    @Override
    public <T extends AbstractMapDTO> AbstractPanSortedList<T> getTableauMaitre(SpecificContext context) {
        ActiviteNormativeEnum section = context.getFromContextData(PanContextDataKey.CURRENT_SECTION_ENUM);
        LoisSuiviesForm loisSuiviesForm = context.getFromContextData(PanContextDataKey.PAN_FORM);
        if (!loisSuiviesForm.getIsTableChangeEvent() && section != null && section.getDefaultSortColumn() != null) {
            switch (section.getDefaultSortColumn()) {
                case PUBLICATION:
                    loisSuiviesForm.setDatePublication(SortOrder.DESC);
                    break;
                case PROMULGATION:
                    loisSuiviesForm.setPromulgation(SortOrder.DESC);
                    break;
                case ADOPTION:
                    loisSuiviesForm.setDateEcheance(SortOrder.DESC);
                    break;
            }
        }

        Boolean forceNonPaginated = ObjectHelper.requireNonNullElse(
            context.getFromContextData(PanContextDataKey.FORCE_NON_PAGINATED_TABLEAU_MAITRE),
            false
        );

        String pageProviderName = "panMainTableauPageProvider";
        PanMainTableauPageProvider genProvider = loisSuiviesForm.getPageProvider(
            context.getSession(),
            pageProviderName,
            "d.",
            null
        );

        String currentSection = context.getFromContextData(PanContextDataKey.CURRENT_SECTION);
        ActiviteNormativeEnum anEnum = ActiviteNormativeEnum.getById(currentSection);
        genProvider.setAttributSchema(anEnum.getAttributSchema());
        genProvider.setForceNonPaginatedResults(forceNonPaginated);

        FiltreUtils.putProviderInfosInContextData(context, pageProviderName, null, "", genProvider);

        return callGenericProvider(context, genProvider, anEnum);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AbstractMapDTO> AbstractPanSortedList<T> callGenericProvider(
        SpecificContext context,
        PanMainTableauPageProvider genProvider,
        ActiviteNormativeEnum anEnum
    ) {
        String legislature = context.computeFromContextDataIfAbsent(PanContextDataKey.LEGISLATURE_INPUT, () -> "");
        genProvider.setLegislature(legislature);
        if (StringUtils.equals(anEnum.getId(), ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId())) {
            return (AbstractPanSortedList<T>) callGenericProviderSectionApplicationDesLois(context, genProvider);
        } else if (
            StringUtils.equals(anEnum.getId(), ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES.getId()) ||
            StringUtils.equals(anEnum.getId(), ActiviteNormativeEnum.ORDONNANCES_38C.getId())
        ) {
            return (AbstractPanSortedList<T>) callGenericProviderSectionApplicationOrdonnacesAnd38C(
                context,
                genProvider
            );
        } else if (StringUtils.equals(anEnum.getId(), ActiviteNormativeEnum.TRANSPOSITION.getId())) {
            return (AbstractPanSortedList<T>) callGenericProviderSectionTranspositionDesDirectives(
                context,
                genProvider
            );
        } else if (StringUtils.equals(anEnum.getId(), ActiviteNormativeEnum.ORDONNANCES.getId())) {
            return (AbstractPanSortedList<T>) callGenericProviderSectionOrdonances(context, genProvider);
        } else if (StringUtils.equals(anEnum.getId(), ActiviteNormativeEnum.TRAITES_ET_ACCORDS.getId())) {
            return (AbstractPanSortedList<T>) callGenericProviderSectionTraitesEtAccord(context, genProvider);
        } else {
            return null;
        }
    }

    private TraitesListPan callGenericProviderSectionTraitesEtAccord(
        SpecificContext context,
        PanMainTableauPageProvider genProvider
    ) {
        addFiltersToProvider(context, genProvider);

        genProvider.setDtoClass(TexteMaitreTraiteDTOImpl.class);
        List<TexteMaitreTraiteDTOImpl> list = genProvider
            .getCurrentPage()
            .stream()
            .map(TexteMaitreTraiteDTOImpl.class::cast)
            .collect(Collectors.toList());
        TraitesListPan texteMaitreTraiteList = new TraitesListPan(context, list);
        texteMaitreTraiteList.setNbTotal((int) genProvider.getResultsCount());
        return texteMaitreTraiteList;
    }

    private OrdonnancesListPan callGenericProviderSectionOrdonances(
        SpecificContext context,
        PanMainTableauPageProvider genProvider
    ) {
        addFiltersToProvider(context, genProvider);

        boolean exclueRatifie = context.computeFromContextDataIfAbsent(PanContextDataKey.MASQUER_RATIFIE, () -> false);
        genProvider.setDtoClass(OrdonnanceDTOImpl.class);
        genProvider.setExcludeRatifie(exclueRatifie);
        List<OrdonnanceDTOImpl> list = genProvider
            .getCurrentPage()
            .stream()
            .map(OrdonnanceDTOImpl.class::cast)
            .collect(Collectors.toList());
        OrdonnancesListPan ordonnancesList = new OrdonnancesListPan(context, list);
        ordonnancesList.setNbTotal((int) genProvider.getResultsCount());
        return ordonnancesList;
    }

    private TranspositionDirectivesListPan callGenericProviderSectionTranspositionDesDirectives(
        SpecificContext context,
        PanMainTableauPageProvider genProvider
    ) {
        addFiltersToProvider(context, genProvider);

        genProvider.overrideAdapterClass(TranspositionDirective.class);
        genProvider.setDtoClass(TranspositionDirectiveDTOImpl.class);
        List<TranspositionDirectiveDTOImpl> list = genProvider
            .getCurrentPage()
            .stream()
            .map(TranspositionDirectiveDTOImpl.class::cast)
            .collect(Collectors.toList());
        TranspositionDirectivesListPan transpositionDirectiveList = new TranspositionDirectivesListPan(context, list);
        transpositionDirectiveList.setNbTotal((int) genProvider.getResultsCount());
        return transpositionDirectiveList;
    }

    private TextesMaitresListPan callGenericProviderSectionApplicationOrdonnacesAnd38C(
        SpecificContext context,
        PanMainTableauPageProvider genProvider
    ) {
        addFiltersToProvider(context, genProvider);

        genProvider.setDtoClass(TexteMaitreLoiDTOImpl.class);
        List<TexteMaitreLoiDTOImpl> list = genProvider
            .getCurrentPage()
            .stream()
            .map(TexteMaitreLoiDTOImpl.class::cast)
            .collect(Collectors.toList());
        TextesMaitresListPan textesMaitresList = new TextesMaitresListPan(context, list);
        textesMaitresList.setNbTotal((int) genProvider.getResultsCount());
        return textesMaitresList;
    }

    private TextesMaitresListPan callGenericProviderSectionApplicationDesLois(
        SpecificContext context,
        PanMainTableauPageProvider genProvider
    ) {
        addFiltersToProvider(context, genProvider);

        genProvider.overrideAdapterClass(ActiviteNormative.class);
        genProvider.setDtoClass(TexteMaitreLoiDTOImpl.class);
        List<TexteMaitreLoiDTOImpl> list = genProvider
            .getCurrentPage()
            .stream()
            .map(TexteMaitreLoiDTOImpl.class::cast)
            .collect(Collectors.toList());
        TextesMaitresListPan textesMaitresList = new TextesMaitresListPan(context, list);
        textesMaitresList.setNbTotal((int) genProvider.getResultsCount());
        LOGGER.debug(
            STLogEnumImpl.GET_DOSSIER_TEC,
            genProvider.getResultsCount() + " textes trouvés pour ces critères"
        );
        return textesMaitresList;
    }

    public void addFiltersToProvider(SpecificContext context, PanMainTableauPageProvider genProvider) {
        Map<String, Serializable> filters = context.getFromContextData(PanContextDataKey.PAN_FILTERS);
        if (MapUtils.isNotEmpty(filters)) {
            filters.forEach(genProvider::addFiltrableProperty);
        }
    }

    @Override
    public TextesMaitresListPan getLoisList(SpecificContext context) {
        LoisSuiviesForm loisSuiviesForm = context.getFromContextData(PanContextDataKey.PAN_FORM);

        PanMainTableauPageProvider genProvider = loisSuiviesForm.getPageProvider(
            context.getSession(),
            "panMainTableauPageProvider"
        );
        genProvider.setAttributSchema("applicationLoi");
        genProvider.setDtoClass(TexteMaitreLoiDTOImpl.class);
        List<TexteMaitreLoiDTOImpl> loisList = genProvider
            .getCurrentPage()
            .stream()
            .map(TexteMaitreLoiDTOImpl.class::cast)
            .collect(Collectors.toList());
        TextesMaitresListPan loisSuiviesList = new TextesMaitresListPan();
        loisSuiviesList.setListe(loisList);
        loisSuiviesList.setNbTotal((int) genProvider.getResultsCount());
        loisSuiviesList.buildColonnes(context);

        return loisSuiviesList;
    }

    @Override
    public void addToActiviteNormative(SpecificContext context) {
        CoreSession session = context.getSession();
        String activiteNormativeNor = context.getFromContextData(ACTIVITE_NORMATIVE_NOR);
        ActiviteNormativeEnum currentMenu = ActiviteNormativeEnum.getById(context.getFromContextData(CURRENT_SECTION));

        if (StringUtils.isNotEmpty(activiteNormativeNor) && currentMenu != null) {
            try {
                SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .addDossierToTableauMaitre(
                        activiteNormativeNor.trim(),
                        currentMenu.getTypeActiviteNormative(),
                        session
                    );
                context.getMessageQueue().addSuccessToQueue(ResourceHelper.getString("pan.add.feedback"));
                if (
                    ActiviteNormativeEnum.APPLICATION_DES_LOIS.equals(currentMenu) ||
                    ActiviteNormativeEnum.APPLICATION_DES_LOIS_MINISTERE.equals(currentMenu)
                ) {
                    SolonEpgServiceLocator.getActiviteNormativeService().updateLoiListePubliee(session, true);
                } else if (ActiviteNormativeEnum.ORDONNANCES_38C.equals(currentMenu)) {
                    SolonEpgServiceLocator.getActiviteNormativeService().updateHabilitationListePubliee(session, true);
                } else if (
                    ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES.equals(currentMenu) ||
                    ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES_MINISTERE.equals(currentMenu)
                ) {
                    SolonEpgServiceLocator.getActiviteNormativeService().updateOrdonnancesListePubliee(session, true);
                }
            } catch (ActiviteNormativeException e) {
                LOGGER.warn(EpgLogEnumImpl.FAIL_ADD_NOR_PAN, e);
                context.getMessageQueue().addErrorToQueue(ResourceHelper.getString(e.getMessage()));
            }
        }
    }

    @Override
    public void removeFromActiviteNormative(SpecificContext context) {
        CoreSession session = context.getSession();

        DocumentModel activiteNormativeDoc = context.getCurrentDocument();
        ActiviteNormativeEnum currentMenu = ActiviteNormativeEnum.getById(context.getFromContextData(CURRENT_SECTION));

        if (currentMenu != null) {
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .removeActiviteNormativeFrom(activiteNormativeDoc, currentMenu.getTypeActiviteNormative(), session);
            context.getMessageQueue().addSuccessToQueue(ResourceHelper.getString("pan.remove.feedback"));
            if (ActiviteNormativeEnum.APPLICATION_DES_LOIS.equals(currentMenu)) {
                SolonEpgServiceLocator.getActiviteNormativeService().updateLoiListePubliee(session, true);
            } else if (ActiviteNormativeEnum.ORDONNANCES_38C.equals(currentMenu)) {
                SolonEpgServiceLocator.getActiviteNormativeService().updateHabilitationListePubliee(session, true);
            } else if (ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES.equals(currentMenu)) {
                SolonEpgServiceLocator.getActiviteNormativeService().updateOrdonnancesListePubliee(session, true);
            }
        }
    }

    @Override
    public boolean isEditMode(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel texteMaitreDoc = context.getCurrentDocument();

        // Vérifie que le texteMaitre en cours est verrouillé
        if (texteMaitreDoc != null) {
            TexteMaitre texteMaitre = texteMaitreDoc.getAdapter(TexteMaitre.class);
            return SolonEpgServiceLocator
                .getActiviteNormativeService()
                .isTexteMaitreLockByCurrentUser(texteMaitre, session);
        }
        return false;
    }

    @Override
    public boolean isEditModeForBordereau(SpecificContext context) {
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();

        if (
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_LOI_MINISTERE) &&
            !ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_UPDATER)
        ) {
            return false;
        } else {
            return isEditMode(context);
        }
    }

    @Override
    public boolean isEditModeForBordereauAppOrdonnances(SpecificContext context) {
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();

        if (
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_ORDONNANCE_MINISTERE) &&
            !ssPrincipal.isMemberOf(
                SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_UPDATER
            )
        ) {
            return false;
        } else {
            return isEditMode(context);
        }
    }

    @Override
    public boolean isInApplicationDesLois(SpecificContext context) {
        ActiviteNormativeEnum currentItem = ActiviteNormativeEnum.getById(
            context.getFromContextData(PanContextDataKey.CURRENT_SECTION)
        );
        return (
            ActiviteNormativeEnum.APPLICATION_DES_LOIS == currentItem ||
            ActiviteNormativeEnum.APPLICATION_DES_LOIS_MINISTERE == currentItem
        );
    }

    @Override
    public boolean isInApplicationDesOrdonnances(SpecificContext context) {
        ActiviteNormativeEnum currentItem = ActiviteNormativeEnum.getById(
            context.getFromContextData(PanContextDataKey.CURRENT_SECTION)
        );
        return (
            ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES == currentItem ||
            ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES_MINISTERE == currentItem
        );
    }

    @Override
    public boolean isInApplicationDesLoisOrOrdonnances(SpecificContext context) {
        return isInApplicationDesLois(context) || isInApplicationDesOrdonnances(context);
    }

    @Override
    public boolean isInOrdonnances(SpecificContext context) {
        ActiviteNormativeEnum currentItem = ActiviteNormativeEnum.getById(
            context.getFromContextData(PanContextDataKey.CURRENT_SECTION)
        );
        return ActiviteNormativeEnum.ORDONNANCES == currentItem;
    }

    @Override
    public boolean isInOrdonnances38C(SpecificContext context) {
        ActiviteNormativeEnum currentItem = ActiviteNormativeEnum.getById(
            context.getFromContextData(PanContextDataKey.CURRENT_SECTION)
        );
        return ActiviteNormativeEnum.ORDONNANCES_38C == currentItem;
    }

    @Override
    public boolean isInTransposition(SpecificContext context) {
        ActiviteNormativeEnum currentItem = ActiviteNormativeEnum.getById(
            context.getFromContextData(PanContextDataKey.CURRENT_SECTION)
        );
        return ActiviteNormativeEnum.TRANSPOSITION == currentItem;
    }

    @Override
    public boolean isInTraiteAccord(SpecificContext context) {
        ActiviteNormativeEnum currentItem = ActiviteNormativeEnum.getById(
            context.getFromContextData(PanContextDataKey.CURRENT_SECTION)
        );
        return ActiviteNormativeEnum.TRAITES_ET_ACCORDS == currentItem;
    }

    @Override
    public boolean isInTexteMaitre(SpecificContext context) {
        String subTabId = context.getFromContextData(PanContextDataKey.CURRENT_SUB_TAB);
        return subTabId.startsWith(ActiviteNormativeConstants.TAB_AN_TEXTE_MAITRE);
    }

    @Override
    public boolean isInTableauLois(SpecificContext context) {
        String subTabId = context.getFromContextData(PanContextDataKey.CURRENT_SUB_TAB);
        return subTabId.startsWith(ActiviteNormativeConstants.TAB_AN_TABLEAU_LOIS);
    }

    @Override
    public boolean isInTableauOrdonnances(SpecificContext context) {
        String subTabId = context.getFromContextData(PanContextDataKey.CURRENT_SUB_TAB);
        return subTabId.startsWith(ActiviteNormativeConstants.TAB_AN_TABLEAU_ORDONNANCES);
    }

    @Override
    public boolean isInTableauProgrammation(SpecificContext context) {
        String subTabId = context.getFromContextData(PanContextDataKey.CURRENT_SUB_TAB);
        return subTabId.startsWith(ActiviteNormativeConstants.TAB_AN_TABLEAU_PROGRAMMATION);
    }

    @Override
    public boolean isInTableauSuivi(SpecificContext context) {
        String subTabId = context.getFromContextData(PanContextDataKey.CURRENT_SUB_TAB);
        return subTabId.startsWith(ActiviteNormativeConstants.TAB_AN_TABLEAU_SUIVI);
    }

    @Override
    public boolean isOnlyUtilisateurMinistereLoiOrOrdonnance(SpecificContext context) {
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();
        return (
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_LOI_MINISTERE) &&
            !ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_UPDATER) ||
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_ORDONNANCE_MINISTERE) &&
            !ssPrincipal.isMemberOf(
                SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_UPDATER
            )
        );
    }

    @Override
    public FicheSignaletiqueLoiDTO getCurrentFicheSignaletiqueLoi(SpecificContext context) {
        CoreSession session = context.getSession();

        DocumentModel activiteNormativeDoc = context.getCurrentDocument();
        return new FicheSignaletiqueLoiDTO(activiteNormativeDoc.getAdapter(ActiviteNormative.class), session);
    }

    @Override
    public FicheSignaletiqueApplicationOrdonnanceDTO getCurrentFicheSignaletiqueApplicationOrdonnance(
        SpecificContext context
    ) {
        CoreSession session = context.getSession();

        DocumentModel activiteNormativeDoc = context.getCurrentDocument();
        return new FicheSignaletiqueApplicationOrdonnanceDTO(
            activiteNormativeDoc.getAdapter(ActiviteNormative.class),
            session
        );
    }

    @Override
    public FicheSignaletiqueOrdonnanceDTO getCurrentFicheSignaletiqueOrdonnance(SpecificContext context) {
        CoreSession session = context.getSession();

        DocumentModel activiteNormativeDoc = context.getCurrentDocument();
        return new FicheSignaletiqueOrdonnanceDTO(activiteNormativeDoc.getAdapter(ActiviteNormative.class), session);
    }

    @Override
    public boolean isActiviteNormativeUpdater(SpecificContext context) {
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();
        if (ssPrincipal != null) {
            return (
                isApplicationLoisUpdater(context) ||
                isRatificationUpdater(context) ||
                isSuiviUpdater(context) ||
                isTraiteUpdater(context) ||
                isTranspositionUpdater(context) ||
                isApplicationOrdonnancesUpdater(context)
            );
        }

        return false;
    }

    private boolean isApplicationOrdonnancesUpdater(SpecificContext context) {
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();
        return (
            isInApplicationDesOrdonnances(context) &&
            ssPrincipal.isMemberOf(
                SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_UPDATER
            )
        );
    }

    private boolean isTranspositionUpdater(SpecificContext context) {
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();
        return (
            isInTransposition(context) &&
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_UPDATER)
        );
    }

    private boolean isTraiteUpdater(SpecificContext context) {
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();
        return (
            isInTraiteAccord(context) &&
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_UPDATER)
        );
    }

    private boolean isSuiviUpdater(SpecificContext context) {
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();
        return (
            isInOrdonnances38C(context) &&
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_UPDATER)
        );
    }

    private boolean isRatificationUpdater(SpecificContext context) {
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();
        return (
            isInOrdonnances(context) &&
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_UPDATER)
        );
    }

    private boolean isApplicationLoisUpdater(SpecificContext context) {
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();
        return (
            isInApplicationDesLois(context) &&
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_UPDATER)
        );
    }

    /**
     * Verrouillage du document
     */
    @Override
    public void lockCurrentDocument(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel texteMaitreDoc = context.getCurrentDocument();
        if (texteMaitreDoc != null) {
            TexteMaitre texteMaitre = texteMaitreDoc.getAdapter(TexteMaitre.class);
            SolonEpgServiceLocator.getActiviteNormativeService().lockTexteMaitre(texteMaitre, session);
        }
    }

    @Override
    public void unlockCurrentDocument(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel texteMaitreDoc = context.getCurrentDocument();
        if (texteMaitreDoc != null) {
            TexteMaitre texteMaitre = texteMaitreDoc.getAdapter(TexteMaitre.class);
            SolonEpgServiceLocator.getActiviteNormativeService().unlockTexteMaitre(texteMaitre, session);
        }
    }

    @Override
    public boolean isTexteMaitreLocked(SpecificContext context) {
        DocumentModel texteMaitreDoc = context.getCurrentDocument();

        if (texteMaitreDoc != null) {
            TexteMaitre texteMaitre = texteMaitreDoc.getAdapter(TexteMaitre.class);
            return texteMaitre.getDocLockUser() != null;
        }
        return false;
    }

    @Override
    public String getCurrentLockTime(SpecificContext context) {
        DocumentModel texteMaitreDoc = context.getCurrentDocument();
        if (texteMaitreDoc != null) {
            TexteMaitre texteMaitre = texteMaitreDoc.getAdapter(TexteMaitre.class);
            Calendar cal = texteMaitre.getDocLockDate();
            if (cal != null) {
                return SolonDateConverter.getClientConverter().formatNow();
            }
        }

        return null;
    }

    @Override
    public String getCurrentLockOwnerInfo(SpecificContext context) {
        DocumentModel texteMaitreDoc = context.getCurrentDocument();

        if (texteMaitreDoc != null) {
            TexteMaitre texteMaitre = texteMaitreDoc.getAdapter(TexteMaitre.class);
            STUserService userService = STServiceLocator.getSTUserService();
            return userService.getUserFullName(texteMaitre.getDocLockUser());
        }

        return null;
    }

    private Pair<String, String> genererCsvReport(ANReportEnum exportType) {
        String reportFile = null;
        String name = "";

        if (exportType != null) {
            reportFile = SSServiceLocator.getBirtGenerationService().getReport(exportType.getRptdesignId()).getFile();
            name = exportType.getFilename();
        }

        return Pair.of(reportFile, name);
    }

    private Pair<String, String> genererCsvApplicationLois(SpecificContext context) {
        Map<String, Serializable> inputValues = context.getFromContextData(PanContextDataKey.INPUT_VALUES);
        Boolean masquerApplique = context.getFromContextData(PanContextDataKey.MASQUER_APPLIQUE);

        ANReportEnum exportType = null;
        if (isInTexteMaitre(context)) {
            exportType = ANReportEnum.CSV_APPLICATION_LOI_TEXTES_MAITRES;
        }
        if (isInTableauLois(context)) {
            exportType = ANReportEnum.CSV_APPLICATION_LOI_TABLEAU_LOIS;
        } else if (isInTableauSuivi(context)) {
            exportType =
                masquerApplique
                    ? ANReportEnum.CSV_APPLICATION_LOI_TABLEAU_SUIVI_MASQUER
                    : ANReportEnum.CSV_APPLICATION_LOI_TABLEAU_SUIVI_AFFICHER;
        } else if (isInTableauProgrammation(context)) {
            String figer = BooleanUtils.toStringTrueFalse(
                !PanUIServiceLocator.getTableauProgrammationUIService().isCurrentProgrammationLoiLocked(context)
            );
            inputValues.put("FIGER_PARAM", figer);
            exportType = ANReportEnum.CSV_APPLICATION_LOI_TABLEAU_PROGRAMMATION;
        }

        return genererCsvReport(exportType);
    }

    private Pair<String, String> genererCsvApplicationOrdonnances(
        SpecificContext context,
        Boolean masquerApplique,
        Map<String, Serializable> inputValues
    ) {
        ANReportEnum exportType = null;
        if (isInTexteMaitre(context)) {
            exportType = ANReportEnum.CSV_APPLICATION_ORDO_TEXTES_MAITRES;
        }
        if (isInTableauOrdonnances(context)) {
            exportType = ANReportEnum.CSV_APPLICATION_ORDO_TABLEAU_ORDOS;
        } else if (isInTableauSuivi(context)) {
            exportType =
                masquerApplique
                    ? ANReportEnum.CSV_APPLICATION_ORDO_TABLEAU_SUIVI_MASQUER
                    : ANReportEnum.CSV_APPLICATION_ORDO_TABLEAU_SUIVI_AFFICHER;
        } else if (isInTableauProgrammation(context)) {
            String figer = BooleanUtils.toStringTrueFalse(
                !PanUIServiceLocator.getTableauProgrammationUIService().isCurrentProgrammationLoiLocked(context)
            );
            inputValues.put("FIGER_PARAM", figer);
            exportType = ANReportEnum.CSV_APPLICATION_ORDO_TABLEAU_PROGRAMMATION;
        }

        return genererCsvReport(exportType);
    }

    @Override
    public Pair<File, String> genererXls(SpecificContext context) {
        CoreSession session = context.getSession();

        String dossierId = context.getFromContextData(PanContextDataKey.ACTIVITE_NORMATIVE_PROGRAMMATION_ID);
        if (dossierId == null) {
            dossierId = context.getFromContextData(PanContextDataKey.TEXTE_MAITRE_ID);
        }

        Boolean masquerApplique = context.getFromContextData(PanContextDataKey.MASQUER_APPLIQUE);
        String ministerePiloteId = context.getFromContextData(PanContextDataKey.MINISTERE_PILOTE_ID);

        Map<String, Serializable> inputValues = new HashMap<>();
        inputValues.put("MINISTEREPILOTE_PARAM", ministerePiloteId);
        inputValues.put("DOSSIERID_PARAM", dossierId);
        context.putInContextData(PanContextDataKey.INPUT_VALUES, inputValues);

        String reportFile = null;
        String name = "";
        if (this.isInApplicationDesLois(context)) {
            Pair<String, String> reportData = genererCsvApplicationLois(context);
            reportFile = reportData.getLeft();
            name = reportData.getRight();
        } else if (isInApplicationDesOrdonnances(context)) {
            Pair<String, String> reportData = genererCsvApplicationOrdonnances(context, masquerApplique, inputValues);
            reportFile = reportData.getLeft();
            name = reportData.getRight();
        } else if (this.isInOrdonnances38C(context)) {
            if (this.isInTableauSuivi(context)) {
                ANReportEnum exportType = ANReportEnum.CSV_SUIVI_HABILIT_TABLEAU_SUIVI;
                reportFile = SSServiceLocator.getBirtGenerationService().getReport(exportType.getRptdesignId()).getId();
                name = exportType.getFilename();
            } else if (this.isInTableauProgrammation(context)) {
                ANReportEnum exportType = ANReportEnum.CSV_SUIVI_HABILIT_TABLEAU_PROGRAMMATION;
                reportFile = SSServiceLocator.getBirtGenerationService().getReport(exportType.getRptdesignId()).getId();
                name = exportType.getFilename();
            }
        } else if (this.isInTransposition(context) && this.isInTexteMaitre(context)) {
            ANReportEnum exportType = ANReportEnum.CSV_TRANSPO_DIRECTIVES_TEXTES_MAITRES;
            reportFile = SSServiceLocator.getBirtGenerationService().getReport(exportType.getRptdesignId()).getId();
            name = exportType.getFilename();
        }

        if (reportFile != null) {
            String ministeresParam = SolonEpgServiceLocator
                .getStatsGenerationResultatService()
                .getMinisteresListBirtReportParam(session);
            String directionsParam = SolonEpgServiceLocator
                .getStatsGenerationResultatService()
                .getDirectionsListBirtReportParam();
            inputValues.put("MINISTERES_PARAM", ministeresParam);
            inputValues.put("DIRECTIONS_PARAM", directionsParam);
            File file = SSServiceLocator
                .getBirtGenerationService()
                .generate(name, reportFile, BirtOutputFormat.XLS, inputValues, null, false);
            return Pair.of(file, name + BirtOutputFormat.XLS.getExtensionWithSeparator());
        } else {
            return null;
        }
    }

    @Override
    public SpecificContext putPanActionDTOInContext(SpecificContext context) {
        PanActionsDTO panActionDTO = new PanActionsDTO();
        panActionDTO.setIsOnlyUtilisateurMinistereLoiOrOrdonnance(isOnlyUtilisateurMinistereLoiOrOrdonnance(context));
        context.putInContextData("panActionDTO", panActionDTO);
        return context;
    }

    @Override
    public MesuresApplicativesPanUnsortedList getMesures(SpecificContext context) {
        LoisSuiviesForm listForm = ObjectHelper.requireNonNullElseGet(
            UserSessionHelper.getUserSessionParameter(context, PanUserSessionKey.SECOND_TABLE),
            LoisSuiviesForm::new
        );

        DocumentModel loiDoc = context.getCurrentDocument();
        TexteMaitre texteMaitre = loiDoc.getAdapter(TexteMaitre.class);
        PanMesuresPageProvider provider = buildPanProvider(
            "panMesuresPageProvider",
            null,
            listForm,
            context.getSession()
        );
        provider.setIds(texteMaitre.getMesuresIds());
        provider.setTexteMaitre(texteMaitre);

        List<MesureApplicativeDTOImpl> mesuresDTO = provider
            .getCurrentPage()
            .stream()
            .map(MesureApplicativeDTOImpl.class::cast)
            .collect(Collectors.toList());

        MesuresApplicativesPanUnsortedList mesuresList = new MesuresApplicativesPanUnsortedList();
        mesuresList.setListe(mesuresDTO);
        mesuresList.setNbTotal((int) provider.getResultsCount());
        mesuresList.buildColonnes();
        return mesuresList;
    }

    @Override
    public DecretsPanUnsortedList getDecrets(SpecificContext context) {
        LoisSuiviesForm listForm = ObjectHelper.requireNonNullElseGet(
            UserSessionHelper.getUserSessionParameter(context, PanUserSessionKey.THIRD_TABLE),
            LoisSuiviesForm::new
        );

        String mesureId = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ID);
        MesureApplicative mesure = context
            .getSession()
            .getDocument(new IdRef(mesureId))
            .getAdapter(MesureApplicative.class);

        DocumentModel document = context.getSession().getDocument(new IdRef(mesure.getIdDossier()));
        TexteMaitre texteMaitre = document.getAdapter(TexteMaitre.class);

        PanDecretsPageProvider provider = buildPanProvider(
            "panDecretsPageProvider",
            null,
            listForm,
            context.getSession()
        );
        provider.setIds(mesure.getDecretIds());
        provider.setTexteMaitre(texteMaitre);
        provider.setMesure(mesure);

        List<DecretDTOImpl> decretsDTO = provider
            .getCurrentPage()
            .stream()
            .map(DecretDTOImpl.class::cast)
            .collect(Collectors.toList());
        DecretsPanUnsortedList decretsList = new DecretsPanUnsortedList();
        decretsList.setListe(decretsDTO);
        decretsList.setNbTotal((int) provider.getResultsCount());
        decretsList.buildColonnes();
        return decretsList;
    }

    protected <T extends AbstractPanPageProvider> T buildPanProvider(
        String pageProviderName,
        List<Object> lstParams,
        LoisSuiviesForm loisSuiviesForm,
        CoreSession session
    ) {
        lstParams = lstParams != null ? lstParams : new ArrayList<>();
        return loisSuiviesForm.getPageProvider(session, pageProviderName, lstParams);
    }

    @Override
    public OngletConteneur actionsToTabs(List<Action> actions, String current) {
        OngletConteneur conteneur = new OngletConteneur();
        List<Onglet> onglets = new ArrayList<>();
        if (StringUtils.isEmpty(current) && CollectionUtils.isNotEmpty(actions)) {
            // Si on ne spécifie pas d'onglet courant, alors c'est le premier onglet qui est courant par défaut
            current = (String) actions.get(0).getProperties().get("name");
        }
        for (Action action : actions) {
            PanOnglet onglet = new PanOnglet();
            onglet.setLabel(action.getLabel());
            onglet.setId((String) action.getProperties().get("name"));
            if (onglet.getId() != null && onglet.getId().equals(current)) {
                conteneur.setCurrentTabAllowed(true);
                onglet.setFragmentFile((String) action.getProperties().get("fragmentFile"));
                onglet.setFragmentName((String) action.getProperties().get("fragmentName"));
                onglet.setIsActif(true);
                onglet.setIdBirtReport((String) action.getProperties().get("idBirtReport"));
                onglet.setAltIdBirtReport((String) action.getProperties().get("altIdBirtReport"));
                onglet.setSwitchLabel((String) action.getProperties().get("switchLabel"));
            } else {
                onglet.setScript((String) action.getProperties().get("script"));
            }
            onglets.add(onglet);
        }
        conteneur.setOnglets(onglets);
        return conteneur;
    }

    @Override
    public PanOnglet getActiveTab(OngletConteneur content) {
        return (PanOnglet) content.getOnglets().stream().filter(Onglet::getIsActif).findFirst().orElse(null);
    }

    @Override
    public List<SelectValueDTO> getLegislaturesPublicationValues(SpecificContext context) {
        SolonEpgParametreService paramEPGservice = SolonEpgServiceLocator.getSolonEpgParametreService();
        ParametrageAN myDoc = paramEPGservice.getDocAnParametre(context.getSession());
        if (myDoc != null && myDoc.getLegislatures() != null) {
            return myDoc.getLegislatures().stream().map(SelectValueDTO::new).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public ConsultTexteMaitreDTO getConsultTexteMaitreDTO(SpecificContext context) {
        TexteMaitre texteMaitre = context.getCurrentDocument().getAdapter(TexteMaitre.class);
        ConsultTexteMaitreDTO texteMaitreDTO = new ConsultTexteMaitreDTO();

        texteMaitreDTO.setTexteMaitreIsLocked(PanUIServiceLocator.getPanUIService().isTexteMaitreLocked(context));
        texteMaitreDTO.setCanEditTexteMaitre(PanUIServiceLocator.getPanUIService().isEditMode(context));
        if (texteMaitre.getDocLockDate() != null) {
            texteMaitreDTO.setLockTime(SolonDateConverter.getClientConverter().format(texteMaitre.getDocLockDate()));
        }
        texteMaitreDTO.setLockOwner(texteMaitre.getDocLockUser());

        if (StringUtils.equals(context.getFromContextData(PanContextDataKey.CURRENT_SECTION), "habilitations")) {
            texteMaitreDTO.setTableauProgrammationIsLocked(
                PanUIServiceLocator
                    .getTableauProgrammation38CUIService()
                    .isCurrentProgrammationHabilitationLocked(context)
            );
        } else {
            texteMaitreDTO.setTableauProgrammationIsLocked(
                PanUIServiceLocator.getTableauProgrammationUIService().isCurrentProgrammationLoiLocked(context)
            );
        }

        if (isInTransposition(context)) {
            texteMaitreDTO.setNor(texteMaitre.getNumero());
        } else {
            texteMaitreDTO.setNor(texteMaitre.getNumeroNor());
        }
        texteMaitreDTO.setTitre(
            StringUtils.isNotEmpty(texteMaitre.getTitreOfficiel())
                ? texteMaitre.getTitreOfficiel()
                : texteMaitre.getTitreActe()
        );
        return texteMaitreDTO;
    }

    @Override
    public void setTemplateUrls(String currentSection, String currentPanTab, Map<String, Object> templateMap) {
        String url = "/suivi/pan/" + currentSection + "/" + currentPanTab;
        templateMap.put(PanTemplateConstants.AJAX_URL_CONTEXTUAL_TAB, "/ajax" + url);
        templateMap.put(PanTemplateConstants.URL_CONTEXTUAL_TAB, url);
        templateMap.put(STTemplateConstants.DATA_URL, url);
        templateMap.put(STTemplateConstants.DATA_AJAX_URL, "/ajax" + url);
    }
}
