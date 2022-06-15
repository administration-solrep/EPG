package fr.dila.solonepg.ui.helper;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.TypeActe;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.core.recherche.EpgDossierListingDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.EpgUserList;
import fr.dila.solonepg.ui.contentview.DerniersDossiersConsultesPageProvider;
import fr.dila.solonepg.ui.contentview.DerniersUsersConsultesPageProvider;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgPageProviders;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.ss.ui.contentview.ModeleFDRPageProvider;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.helper.PaginationHelper;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.contentview.AbstractDTOPageProvider;
import fr.dila.st.ui.services.actions.STActionsServiceLocator;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.utils.LockUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;

public final class SolonEpgProviderHelper {
    private static final STLogger LOG = STLogFactory.getLog(SolonEpgProviderHelper.class);

    public static void buildDTOFromDossierLink(
        final EpgDossierListingDTOImpl rrdto,
        final DossierLink dossierLink,
        CoreSession session,
        List<TypeActe> allTypeActes
    ) {
        Dossier dossier = dossierLink.getDossier(session, Dossier.class);
        rrdto.setDocIdForSelection(dossierLink.getId());

        if (StringUtils.isEmpty(rrdto.getNumeroNor())) {
            rrdto.setNumeroNor(dossier.getNumeroNor());
        }

        if (StringUtils.isEmpty(rrdto.getTitreActe())) {
            rrdto.setTitreActe(dossier.getTitreActe());
        }

        if (rrdto.getDatePublication() == null && dossier.getDatePublication() != null) {
            rrdto.setDatePublication(dossier.getDatePublication().getTime());
        }

        if (rrdto.getDateArrivee() == null && dossierLink.getDateCreation() != null) {
            rrdto.setDateArrivee(dossierLink.getDateCreation().getTime());
        }

        if (StringUtils.isEmpty(rrdto.getTypeActe())) {
            allTypeActes
                .stream()
                .filter(ta -> Objects.equals(ta.getId(), dossier.getTypeActe()))
                .findFirst()
                .ifPresent(acte -> rrdto.setTypeActe(acte.getLabel()));
        }

        if (rrdto.getDateCreation() == null) {
            rrdto.setDateCreation(dossierLink.getDateCreation().getTime());
        }

        rrdto.setUrgent(dossier.getIsUrgent());
        rrdto.setCaseLinkId(dossierLink.getId());
        rrdto.setDossierId(dossierLink.getDossierId());
        rrdto.setRead(dossierLink.isRead());
        rrdto.setLocked(LockUtils.isLocked(session, dossier.getDocument().getRef()));
        rrdto.setLockOwner(
            STActionsServiceLocator.getSTLockActionService().getLockOwnerName(dossier.getDocument(), session)
        );
        rrdto.setDossierCompletErreur(getDossierCompletErreur(dossier));
    }

    public static String getDossierCompletErreur(Dossier dossier) {
        String messageErreur = null;
        boolean isBordereauComplet = isBordereauComplet(dossier);
        boolean isParapheurComplet = Boolean.TRUE.equals(dossier.getIsParapheurComplet());
        if (!isBordereauComplet && !isParapheurComplet) {
            messageErreur =
                ResourceHelper.getString("label.dossier.complet.dossier.incomplets", dossier.getNumeroNor());
        } else if (!isBordereauComplet) {
            messageErreur =
                ResourceHelper.getString(
                    "label.dossier.complet.dossier.incomplet",
                    dossier.getNumeroNor(),
                    "bordereau"
                );
        } else if (!isParapheurComplet) {
            messageErreur =
                ResourceHelper.getString(
                    "label.dossier.complet.dossier.incomplet",
                    dossier.getNumeroNor(),
                    "parapheur"
                );
        }
        return messageErreur;
    }

    private static boolean isBordereauComplet(Dossier dossier) {
        return (
            isBordereauChampsComplets(dossier) ||
            isPublicationComplete(dossier) ||
            CollectionUtils.isEmpty(dossier.getIndexationRubrique())
        );
    }

    private static boolean isPublicationComplete(Dossier dossier) {
        return (
            VocabularyConstants.DELAI_PUBLICATION_A_DATE_PRECISEE.equals(dossier.getDelaiPublication()) &&
            dossier.getDatePreciseePublication() == null
        );
    }

    private static boolean isBordereauChampsComplets(Dossier dossier) {
        return (
            StringUtils.isNoneEmpty(
                dossier.getNomRespDossier(),
                dossier.getQualiteRespDossier(),
                dossier.getTelephoneRespDossier()
            ) &&
            CollectionUtils.isNotEmpty(dossier.getVecteurPublication())
        );
    }

    /**
     * Construit une liste de dossiers à partir du provider paramétré
     *
     * @param form si form n'est pas fourni en entrée, on essaie de l'extraire des
     *             context data. En dernier recours, on l'initialise.
     */
    public static EpgDossierList buildDossierList(
        SpecificContext context,
        String providerName,
        List<Object> params,
        String title,
        DossierListForm form
    ) {
        return buildDossierList(context, providerName, params, title, form, null);
    }

    public static EpgDossierList buildDossierList(
        SpecificContext context,
        String providerName,
        List<Object> params,
        String title,
        DossierListForm form,
        List<String> columns
    ) {
        if (form == null) {
            form =
                ObjectHelper.requireNonNullElseGet(
                    (DossierListForm) context.getContextData().get(EpgContextDataKey.DOSSIER_RECHERCHE_FORM.getName()),
                    DossierListForm::newForm
                );
        }

        form.setDossier(true);
        AbstractDTOPageProvider provider = form.getPageProvider(context.getSession(), providerName, null, params);

        FiltreUtils.putProviderInfosInContextData(context, providerName, params, title, provider);

        List<Map<String, Serializable>> docList = provider.getCurrentPage();
        LOG.debug(STLogEnumImpl.GET_DOSSIER_TEC, provider.getResultsCount() + " dossiers trouvés pour ces critères");

        List<String> colonnes = columns;
        if (CollectionUtils.isEmpty(colonnes)) {
            colonnes =
                SolonEpgServiceLocator
                    .getProfilUtilisateurService()
                    .getProfilUtilisateurForCurrentUser(context.getSession())
                    .getIdsDisplayedColumnsEspaceTraitement();
        }

        EpgDossierList dossierList = EpgDossierListHelper.buildDossierList(
            context.getSession(),
            docList,
            title,
            form,
            colonnes,
            (int) provider.getResultsCount(),
            providerName
        );
        form.setPage(PaginationHelper.getPageFromCurrentIndex(provider.getCurrentPageIndex()));
        return dossierList;
    }

    /**
     * Construit une liste de dossiers consulté
     */
    public static EpgDossierList buildDossierConsultList(
        CoreSession session,
        DerniersDossiersConsultesPageProvider provider
    ) {
        List<Map<String, Serializable>> docList = provider.getCurrentPage();
        LOG.debug(STLogEnumImpl.GET_DOSSIER_TEC, provider.getResultsCount() + " dossiers trouvés pour ces critères");

        return EpgDossierListHelper.buildDossierList(
            session,
            docList,
            null,
            null,
            new ArrayList<>(),
            (int) provider.getResultsCount(),
            provider.getName()
        );
    }

    /**
     * Construit une liste de dossiers à partir du provider paramétré
     */
    public static EpgUserList buildUserList(DerniersUsersConsultesPageProvider provider) {
        List<Map<String, Serializable>> docList = provider.getCurrentPage();
        LOG.debug(STLogEnumImpl.GET_DOSSIER_TEC, provider.getResultsCount() + " user trouvés pour ces critères");

        return EpgUserListHelper.buildUserList(docList, (int) provider.getResultsCount());
    }

    /**
     * Construit une liste de model de FDR à partir du provider paramétré
     */
    public static ModeleFDRList builModelFdrList(ModeleFDRPageProvider provider) {
        List<Map<String, Serializable>> docList = provider.getCurrentPage();
        LOG.debug(
            STLogEnumImpl.GET_DOSSIER_TEC,
            provider.getResultsCount() + " modeles feuille de route trouvés pour ces critères"
        );

        return EpgModelFDRListHelper.buildModeleFDRList(docList, (int) provider.getResultsCount());
    }

    public static List<String> paginateList(List<String> userIds, long offset, long pageSize, long resultsCount) {
        int min = Math.toIntExact(offset);
        int max = Math.toIntExact(offset + pageSize);
        return userIds.subList(
            min <= (int) resultsCount ? min : 0,
            max >= (int) resultsCount ? (int) resultsCount : max
        );
    }

    private SolonEpgProviderHelper() {}

    public static List<String> getAdditionalColumns(String providerName) {
        EpgPageProviders pageProvider = EpgPageProviders.fromContentViewName(providerName);
        List<String> additionalColumns = new ArrayList<>();
        if (pageProvider != null) {
            additionalColumns.addAll(pageProvider.getAdditionalColumns());
        }
        return additionalColumns;
    }
}
