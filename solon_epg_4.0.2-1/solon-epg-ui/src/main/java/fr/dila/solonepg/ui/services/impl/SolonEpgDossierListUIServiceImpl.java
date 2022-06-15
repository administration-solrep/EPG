package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.ui.enums.EpgPageProviders.PROVIDER_CORBEILLE_DOSSIERS_EN_CREATION;
import static fr.dila.solonepg.ui.enums.EpgPageProviders.PROVIDER_CORBEILLE_DOSSIERS_SUIVIS;
import static fr.dila.solonepg.ui.enums.EpgPageProviders.PROVIDER_CORBEILLE_POSTE;
import static fr.dila.solonepg.ui.enums.EpgPageProviders.PROVIDER_CORBEILLE_TYPE_ACTE;
import static fr.dila.solonepg.ui.enums.EpgPageProviders.PROVIDER_CORBEILLE_TYPE_ETAPE;
import static fr.dila.solonepg.ui.enums.EpgPageProviders.PROVIDER_DOSSIERS_ABANDON;
import static fr.dila.solonepg.ui.enums.EpgPageProviders.PROVIDER_DOSSIERS_A_TRAITER;
import static fr.dila.solonepg.ui.enums.EpgPageProviders.PROVIDER_DOSSIERS_CANDIDATS_ARCHIVAGE_DEFINITIF;
import static fr.dila.solonepg.ui.enums.EpgPageProviders.PROVIDER_DOSSIERS_CANDIDATS_ARCHIVAGE_INTERMEDIAIRE;
import static fr.dila.solonepg.ui.enums.EpgPageProviders.PROVIDER_DOSSIERS_CANDIDATS_ELIMINATION;
import static fr.dila.solonepg.ui.enums.EpgPageProviders.PROVIDER_DOSSIERS_CREES;
import static fr.dila.solonepg.ui.enums.EpgPageProviders.PROVIDER_DOSSIERS_FAVORIS;
import static fr.dila.st.core.util.StringHelper.quote;
import static java.util.Arrays.asList;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.DossierSignaleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.contentview.DerniersDossiersConsultesPageProvider;
import fr.dila.solonepg.ui.contentview.DossierPageProvider;
import fr.dila.solonepg.ui.enums.EpgColumnEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgFiltreEnum;
import fr.dila.solonepg.ui.enums.EpgPageProviders;
import fr.dila.solonepg.ui.helper.EpgDossierListHelper;
import fr.dila.solonepg.ui.helper.SolonEpgProviderHelper;
import fr.dila.solonepg.ui.services.SolonEpgDossierListUIService;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.ss.api.service.SSMinisteresService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.enums.SSUserSessionKey;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.ss.ui.utils.ExportEventUtils;
import fr.dila.st.api.constant.STRechercheExportEventConstants;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.core.helper.PaginationHelper;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.PermissionHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.StringHelper;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.bean.PaginationForm;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.query.api.PageProvider;

public class SolonEpgDossierListUIServiceImpl implements SolonEpgDossierListUIService {
    public static final String DOSSIERS_A_TRAITER_LABEL = "menu.suivi.dossiers.atraiter.title";
    protected static final String MISSING_PARAM_ERROR =
        "Un ou plusieurs paramètres manquants pour l'affichage de la liste des dossiers";

    @Override
    public EpgDossierList getDossiersFromPosteCorbeille(SpecificContext context) throws MissingArgumentException {
        String posteId = context.getFromContextData(EpgContextDataKey.POSTE_ID);

        // si un seul de nos argument est vide on ne retourne rien
        if (StringUtils.isBlank(posteId)) {
            throw new MissingArgumentException(MISSING_PARAM_ERROR);
        }

        Mailbox mailbox = SSServiceLocator.getMailboxPosteService().getMailboxPoste(context.getSession(), posteId);

        String titre;
        String mailboxId = "";
        if (mailbox == null) {
            titre = "label.poste.inconnu";
        } else {
            titre = mailbox.getTitle();
            mailboxId = "'" + mailbox.getDocument().getId() + "'";
        }

        DossierListForm form = context.getFromContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM);

        return SolonEpgProviderHelper.buildDossierList(
            context,
            PROVIDER_CORBEILLE_POSTE.getCvName(),
            Lists.newArrayList(mailboxId),
            titre,
            form
        );
    }

    @Override
    public EpgDossierList getDossiersFromTypeActeCorbeille(SpecificContext context) throws MissingArgumentException {
        String acte = context.getFromContextData(EpgContextDataKey.TYPE_ACTE);

        // si un seul de nos argument est vide on ne retourne rien
        if (StringUtils.isBlank(acte)) {
            throw new MissingArgumentException(MISSING_PARAM_ERROR);
        }

        String mailboxParam = SolonEpgServiceLocator.getMailboxService().getMailboxListQuery(context.getSession());

        String acteLabel = STServiceLocator
            .getVocabularyService()
            .getEntryLabel(VocabularyConstants.TYPE_ACTE_VOCABULARY, acte);

        if (Strings.isNullOrEmpty(acteLabel)) {
            acteLabel = "label.acte.inconnu";
        }

        DossierListForm form = context.getFromContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM);

        return SolonEpgProviderHelper.buildDossierList(
            context,
            PROVIDER_CORBEILLE_TYPE_ACTE.getCvName(),
            Lists.newArrayList(mailboxParam, quote(acte)),
            acteLabel,
            form
        );
    }

    @Override
    public EpgDossierList getDossiersFromTypeEtapeCorbeille(SpecificContext context) throws MissingArgumentException {
        String routingTask = context.getFromContextData(SSTemplateConstants.TYPE_ETAPE);

        // si un seul de nos argument est vide on ne retourne rien
        if (StringUtils.isBlank(routingTask)) {
            throw new MissingArgumentException(MISSING_PARAM_ERROR);
        }

        String mailboxParam = SolonEpgServiceLocator.getMailboxService().getMailboxListQuery(context.getSession());

        String routingTaskLabel = STServiceLocator
            .getVocabularyService()
            .getEntryLabel(STVocabularyConstants.ROUTING_TASK_TYPE_VOCABULARY, routingTask);

        if (StringUtils.isBlank(routingTaskLabel)) {
            routingTaskLabel = "label.etape.inconnue";
        }

        DossierListForm form = context.getFromContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM);

        return SolonEpgProviderHelper.buildDossierList(
            context,
            PROVIDER_CORBEILLE_TYPE_ETAPE.getCvName(),
            Lists.newArrayList(mailboxParam, quote(routingTask)),
            routingTaskLabel,
            form
        );
    }

    @Override
    public EpgDossierList getDossiersFromEnCoursDeCreationCorbeille(SpecificContext context) {
        String mailboxParam = SolonEpgServiceLocator.getMailboxService().getMailboxListQuery(context.getSession());

        return SolonEpgProviderHelper.buildDossierList(
            context,
            PROVIDER_CORBEILLE_DOSSIERS_EN_CREATION.getCvName(),
            Lists.newArrayList(mailboxParam),
            ResourceHelper.getString("suivi.table.dossiers.encreation.title"),
            DossierListForm.newFormSortedByCreatedDateDesc()
        );
    }

    @Override
    public EpgDossierList getDossiersFromCrees(SpecificContext context) {
        DossierListForm form = context.getFromContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM);

        List<Mailbox> mailboxPosteList = SSServiceLocator
            .getMailboxPosteService()
            .getMailboxPosteList(context.getSession());

        String mailBoxIds = mailboxPosteList
            .stream()
            .filter(Objects::nonNull)
            .map(Mailbox::getId)
            .collect(Collectors.joining("','", "'", "'"));

        return SolonEpgProviderHelper.buildDossierList(
            context,
            PROVIDER_DOSSIERS_CREES.getCvName(),
            Lists.newArrayList(mailBoxIds),
            ResourceHelper.getString("suivi.table.dossiers.crees.title"),
            form
        );
    }

    @Override
    public EpgDossierList getDossiersFromSuivi(SpecificContext context) {
        DossierListForm form = context.getFromContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM);

        DossierSignaleService dossierSignaleService = SolonEpgServiceLocator.getDossierSignaleService();
        String ids = dossierSignaleService.getIdsDossierSignale(context.getSession());

        return SolonEpgProviderHelper.buildDossierList(
            context,
            PROVIDER_CORBEILLE_DOSSIERS_SUIVIS.getCvName(),
            Lists.newArrayList(ids),
            ResourceHelper.getString("suivi.table.dossiers.suivis.title"),
            form
        );
    }

    @Override
    public EpgDossierList getDernierDossierConsultee(SpecificContext context) {
        PaginationForm form = context.getFromContextData(STContextDataKey.PAGINATION_FORM);
        String username = context.getSession().getPrincipal().getName();
        DerniersDossiersConsultesPageProvider provider = form.getPageProvider(
            context.getSession(),
            "dossier_page_consult",
            asList(username)
        );

        Map<String, Serializable> props = provider.getProperties();
        props.put("filtrableId", "espace_suivi_dossier_content");
        props.put(DossierPageProvider.KEEP_ORDER, true);
        provider.setProperties(props);

        EpgDossierList dossierList = SolonEpgProviderHelper.buildDossierConsultList(context.getSession(), provider);
        form.setPage(PaginationHelper.getPageFromCurrentIndex(provider.getCurrentPageIndex()));

        return dossierList;
    }

    @Override
    public EpgDossierList getDossiersFromByMailBoxAndState(SpecificContext context) {
        STDossier.DossierState state = context.getFromContextData(STContextDataKey.DOSSIER_STATE);
        Map<String, Set<String>> mailboxMap = context.getFromContextData(STContextDataKey.MAILBOX_MAP);

        String providerName = STDossier.DossierState.init == state
            ? PROVIDER_DOSSIERS_CREES.getCvName()
            : PROVIDER_DOSSIERS_A_TRAITER.getCvName();

        String mailbox = StringHelper.join(mailboxMap.get("mailboxIds"), ",", "'");

        return SolonEpgProviderHelper.buildDossierList(
            context,
            providerName,
            Collections.singletonList(mailbox),
            ResourceHelper.getString("mailbox.table.title"),
            null
        );
    }

    @Override
    public EpgDossierList getDossiersAbandon(SpecificContext context) {
        DossierListForm form = context.getFromContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM);
        form.setDossier(true);
        final SSMinisteresService ssMinisteresService = SSServiceLocator.getSSMinisteresService();
        List<String> preferenceColumns = SolonEpgServiceLocator
            .getProfilUtilisateurService()
            .getUserEspaceTraitementColumn(context.getSession());
        preferenceColumns.add(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName());

        return SolonEpgProviderHelper.buildDossierList(
            context,
            PROVIDER_DOSSIERS_ABANDON.getCvName(),
            Lists.newArrayList(ssMinisteresService.getMinisteresQuery(context.getSession())),
            ResourceHelper.getString("admin.abandon.dossier.title"),
            form,
            preferenceColumns
        );
    }

    @Override
    public EpgDossierList getDossiersCandidatElimination(SpecificContext context) {
        String candidat;
        NuxeoPrincipal ssPrincipal = context.getSession().getPrincipal();

        List<String> preferenceColumns = SolonEpgServiceLocator
            .getProfilUtilisateurService()
            .getUserEspaceTraitementColumn(context.getSession());

        preferenceColumns.add(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName());

        if (PermissionHelper.isAdminFonctionnel(ssPrincipal)) {
            candidat = VocabularyConstants.CANDIDAT_ELIMINATION_ADMIN_FONCTIONNEL;
        } else if (PermissionHelper.isAdminMinisteriel(ssPrincipal)) {
            candidat = VocabularyConstants.CANDIDAT_ELIMINATION_ADMIN_MINISTERIEL;
        } else {
            EpgDossierList list = new EpgDossierList();
            list.buildColonnes(null, preferenceColumns);
            return list;
        }

        DossierListForm form = context.getFromContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM);
        form = ObjectHelper.requireNonNullElseGet(form, DossierListForm::newForm);
        form.setDossier(true);
        return EpgDossierListHelper.getListFromDossierPageProvider(
            context,
            form,
            PROVIDER_DOSSIERS_CANDIDATS_ELIMINATION.getCvName(),
            ResourceHelper.getString("admin.delete.dossier.title"),
            Collections.singletonList(candidat),
            preferenceColumns
        );
    }

    @Override
    public EpgDossierList getDossiersValidesElimination(SpecificContext context) {
        DossierListForm form = context.getFromContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM_SUIVI);
        form = ObjectHelper.requireNonNullElseGet(form, DossierListForm::newForm);
        List<String> preferenceColumns = SolonEpgServiceLocator
            .getProfilUtilisateurService()
            .getUserEspaceTraitementColumn(context.getSession());

        preferenceColumns.add(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName());

        return SolonEpgProviderHelper.buildDossierList(
            context,
            "dossiersValidesElimination",
            null,
            ResourceHelper.getString("admin.delete.dossier.valides.suppression.title"),
            form,
            preferenceColumns
        );
    }

    @Override
    public EpgDossierList getDossiersFavoris(SpecificContext context) {
        List<String> idDossiers = context.getFromContextData(EpgContextDataKey.ID_DOSSIERS);
        String ids = StringHelper.join(idDossiers, "','", "");

        return SolonEpgProviderHelper.buildDossierList(
            context,
            PROVIDER_DOSSIERS_FAVORIS.getCvName(),
            Lists.newArrayList(ids),
            ResourceHelper.getString("rechercher.favoris.dossiers.table.title"),
            null,
            Lists.newArrayList(EpgColumnEnum.TEXTE_ENTREPRISE.name(), EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName())
        );
    }

    @Override
    public EpgDossierList getDossiersCandidatToArchivageDefinitive(SpecificContext context) {
        DossierListForm form = (DossierListForm) context
            .getContextData()
            .get(EpgContextDataKey.DOSSIER_RECHERCHE_FORM.getName());
        form.setDossier(true);
        List<String> preferenceColumns = SolonEpgServiceLocator
            .getProfilUtilisateurService()
            .getUserEspaceTraitementColumn(context.getSession());
        preferenceColumns.add(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName());
        return EpgDossierListHelper.getListFromDossierPageProvider(
            context,
            form,
            PROVIDER_DOSSIERS_CANDIDATS_ARCHIVAGE_DEFINITIF.getCvName(),
            ResourceHelper.getString("admin.archivage.definitif.table.title"),
            null,
            preferenceColumns
        );
    }

    @Override
    public EpgDossierList getDossiersCandidatToArchivageIntermediaire(SpecificContext context) {
        DossierListForm form = (DossierListForm) context
            .getContextData()
            .get(EpgContextDataKey.DOSSIER_RECHERCHE_FORM.getName());
        form.setDossier(true);
        List<String> preferenceColumns = SolonEpgServiceLocator
            .getProfilUtilisateurService()
            .getUserEspaceTraitementColumn(context.getSession());
        preferenceColumns.add(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName());

        return EpgDossierListHelper.getListFromDossierPageProvider(
            context,
            form,
            PROVIDER_DOSSIERS_CANDIDATS_ARCHIVAGE_INTERMEDIAIRE.getCvName(),
            ResourceHelper.getString("admin.archivage.intermediaire.table.title"),
            new ArrayList<>(),
            preferenceColumns
        );
    }

    @Override
    public void removeDossiersFromDossiersSuivis(SpecificContext context) {
        CoreSession session = context.getSession();
        List<String> ids = context.getFromContextData(EpgContextDataKey.ID_DOSSIERS);
        List<DocumentModel> docs = session.getDocuments(ids.stream().map(IdRef::new).toArray(DocumentRef[]::new));

        if (CollectionUtils.isNotEmpty(docs)) {
            SolonEpgServiceLocator.getDossierSignaleService().retirer(session, docs);
            context.getMessageQueue().addToastSuccess(ResourceHelper.getString("epg.dossiers.suivis.remove.success"));
        }
    }

    @Override
    public void emptyDossiersSuivis(SpecificContext context) {
        CoreSession session = context.getSession();
        DossierSignaleService dossierSignaleService = SolonEpgServiceLocator.getDossierSignaleService();
        dossierSignaleService.viderCorbeilleDossiersSignales(session);
        context.getMessageQueue().addToastSuccess(ResourceHelper.getString("epg.dossiers.suivis.vider.success"));
    }

    @Override
    public EpgDossierList getDossiersFromHistoriqueValidation(SpecificContext context) {
        DossierListForm form = context.getFromContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM);

        List<Mailbox> mailboxPosteList = SSServiceLocator
            .getMailboxPosteService()
            .getMailboxPosteList(context.getSession());

        String mailBoxIds = mailboxPosteList
            .stream()
            .filter(Objects::nonNull)
            .map(Mailbox::getId)
            .collect(Collectors.joining("','", "'", "'"));

        List<String> preferenceColumns = SolonEpgServiceLocator
            .getProfilUtilisateurService()
            .getUserEspaceTraitementColumn(context.getSession());
        preferenceColumns.add(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName());

        return EpgDossierListHelper.getListFromDossierPageProvider(
            context,
            form,
            EpgPageProviders.PROVIDER_HISTORIQUE_VALIDATION.getCvName(),
            ResourceHelper.getString("suivi.table.historique.validation.title"),
            Lists.newArrayList(mailBoxIds),
            preferenceColumns
        );
    }

    @Override
    public EpgDossierList getDossiersFromATraiter(SpecificContext context) {
        DossierListForm form = context.getFromContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM);

        List<Mailbox> mailboxPosteList = SSServiceLocator
            .getMailboxPosteService()
            .getMailboxPosteList(context.getSession());

        String mailBoxIds = mailboxPosteList
            .stream()
            .filter(Objects::nonNull)
            .map(Mailbox::getId)
            .collect(Collectors.joining("','", "'", "'"));

        return SolonEpgProviderHelper.buildDossierList(
            context,
            PROVIDER_DOSSIERS_A_TRAITER.getCvName(),
            Collections.singletonList(mailBoxIds),
            ResourceHelper.getString(DOSSIERS_A_TRAITER_LABEL),
            form
        );
    }

    @Override
    public EpgDossierList applyFilters(
        String jsonSearch,
        DossierListForm dossierlistForm,
        Map<String, Serializable> mapSearch,
        SpecificContext context
    ) {
        List<String> providerParams = new ArrayList<>();
        Map<String, Serializable> filters = new LinkedHashMap<>();

        mapSearch.putAll(FiltreUtils.extractMapSearch(jsonSearch));

        // Reconstitution du provider et des informations associées
        Pair<PageProvider<?>, String> providerInfos = FiltreUtils.extractProviderInfos(
            mapSearch,
            providerParams,
            filters,
            context.getSession(),
            EpgFiltreEnum.class
        );
        PageProvider<?> provider = providerInfos.getLeft();

        FiltreUtils.putProviderInfosInContextData(
            context,
            provider.getName(),
            providerParams,
            providerInfos.getRight(),
            provider
        );

        // Numéro de page
        String pageStr = ObjectHelper.requireNonNullElse((String) mapSearch.get("page"), "1");
        Long page = Long.parseLong(pageStr) - 1;

        // Taille de la page, check surcharge
        long pageSize = provider.getCurrentPageSize();
        String sizeStr = (String) mapSearch.get("size");
        if (sizeStr != null) {
            pageSize = Long.parseLong(sizeStr);
        }
        provider.setPageSize(pageSize);

        return FiltreUtils.applyFiltersOnProvider(context.getSession(), provider, filters, dossierlistForm, page);
    }

    @Override
    public void exportDossiers(SpecificContext context) {
        ExportEventUtils.fireExportEvent(
            context,
            context.getFromContextData(EpgContextDataKey.EXPORT_TYPE),
            ImmutableMap.of()
        );
    }

    @Override
    public void exportDossiersRechercheRapide(SpecificContext context) {
        String nor = UserSessionHelper.getUserSessionParameter(context, SSUserSessionKey.NOR);

        if (StringUtils.isNotBlank(nor)) {
            Map<String, Serializable> eventProperties = ImmutableMap.of(SolonEpgEventConstant.NOR, nor);

            ExportEventUtils.fireExportEvent(
                context,
                SolonEpgEventConstant.DOSSIERS_RECHERCHE_RAPIDE_EXPORT_EVENT,
                eventProperties
            );
        }
    }

    @Override
    public void exportDossiersRechercheExperte(SpecificContext context) {
        Map<String, Serializable> eventProperties = ImmutableMap.of(
            SolonEpgEventConstant.REQUEST_DTO,
            context.getFromContextData(SSContextDataKey.REQUETE_EXPERTE_DTO)
        );

        ExportEventUtils.fireExportEvent(context, STRechercheExportEventConstants.EVENT_NAME, eventProperties);
    }

    @Override
    public void exportDossiersTabDyn(SpecificContext context) {
        Map<String, Serializable> eventProperties = ImmutableMap.of(
            SolonEpgEventConstant.ID_TAB_DYN,
            context.getFromContextData(STContextDataKey.ID)
        );

        ExportEventUtils.fireExportEvent(context, SolonEpgEventConstant.DOSSIERS_TAB_DYN_EXPORT_EVENT, eventProperties);
    }
}
