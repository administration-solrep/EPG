package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getEspaceRechercheService;
import static fr.dila.solonepg.ui.enums.EpgContextDataKey.FAVORIS_RECHERCHE_FORM;
import static fr.dila.solonepg.ui.enums.EpgContextDataKey.FAVORIS_RECHERCHE_MODELE_FDR_FORM;

import fr.dila.cm.core.dao.FeuilleRouteDao;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.enums.FavorisRechercheType;
import fr.dila.solonepg.api.recherche.FavorisRecherche;
import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgFavorisRechercheModeleFdrForm;
import fr.dila.solonepg.ui.services.FavorisRechercheModeleFeuilleRouteUIService;
import fr.dila.solonepg.ui.th.bean.FavorisRechercheForm;
import fr.dila.ss.api.criteria.FeuilleRouteCriteria;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.model.SpecificContext;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class FavorisRechercheModeleFeuilleRouteUIServiceImpl implements FavorisRechercheModeleFeuilleRouteUIService {

    @Override
    public String getSearchQueryString(SpecificContext context) {
        return getFeuilleRouteDao(context).getQueryString();
    }

    public List<Object> getSearchQueryParameter(SpecificContext context) {
        return getFeuilleRouteDao(context).getParamList();
    }

    private FeuilleRouteDao getFeuilleRouteDao(SpecificContext context) {
        final EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
        FeuilleRouteCriteria criteria = espaceRechercheService.getFeuilleRouteCriteria(context.getCurrentDocument());
        return new FeuilleRouteDao(context.getSession(), criteria);
    }

    @Override
    public String getSearchQueryStringForDisplay(SpecificContext context, DocumentModel favoriRechercheDoc) {
        final VocabularyService vocabularyService = STServiceLocator.getVocabularyService();
        final STUserService stUserService = STServiceLocator.getSTUserService();
        final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
        FavorisRecherche favorisRecherche = favoriRechercheDoc.getAdapter(FavorisRecherche.class);
        List<String> criteriaList = new ArrayList<>();

        if (StringUtils.isNotBlank(favorisRecherche.getFeuilleRouteTitle())) {
            String message = ResourceHelper.getString(
                "ss.recherche.feuilleRoute.feuilleRoute.feuilleRouteIntitule.criteria"
            );
            criteriaList.add(MessageFormat.format(message, favorisRecherche.getFeuilleRouteTitle()));
        }

        if (StringUtils.isNotBlank(favorisRecherche.getFeuilleRouteTypeActe())) {
            String message = ResourceHelper.getString(
                "epg.recherche.feuilleRoute.feuilleRoute.feuilleRouteTypeActe.criteria"
            );
            String label = vocabularyService.getEntryLabel(
                VocabularyConstants.TYPE_ACTE_VOCABULARY,
                favorisRecherche.getFeuilleRouteTypeActe()
            );
            criteriaList.add(MessageFormat.format(message, label));
        }

        if (StringUtils.isNotBlank(favorisRecherche.getFeuilleRouteNumero())) {
            String message = ResourceHelper.getString(
                "epg.recherche.feuilleRoute.feuilleRoute.feuilleRouteNumero.criteria"
            );
            criteriaList.add(MessageFormat.format(message, favorisRecherche.getFeuilleRouteNumero()));
        }

        if (StringUtils.isNotBlank(favorisRecherche.getFeuilleRouteCreationUtilisateur())) {
            String message = ResourceHelper.getString(
                "ss.recherche.feuilleRoute.feuilleRoute.feuilleRouteCreationUtilisateur.criteria"
            );
            String label = stUserService.getUserFullName(favorisRecherche.getFeuilleRouteCreationUtilisateur());
            criteriaList.add(MessageFormat.format(message, label));
        }

        if (StringUtils.isNotBlank(favorisRecherche.getFeuilleRouteMinistere())) {
            String message = ResourceHelper.getString(
                "ss.recherche.feuilleRoute.feuilleRoute.feuilleRouteMinistere.criteria"
            );
            OrganigrammeNode entiteNode = STServiceLocator
                .getSTMinisteresService()
                .getEntiteNode(favorisRecherche.getFeuilleRouteMinistere());
            criteriaList.add(MessageFormat.format(message, entiteNode.getLabel()));
        }

        if (StringUtils.isNotBlank(favorisRecherche.getFeuilleRouteDirection())) {
            String message = ResourceHelper.getString(
                "epg.recherche.feuilleRoute.feuilleRoute.feuilleRouteDirection.criteria"
            );
            OrganigrammeNode entiteNode = STServiceLocator
                .getSTUsAndDirectionService()
                .getUniteStructurelleNode(favorisRecherche.getFeuilleRouteDirection());
            criteriaList.add(MessageFormat.format(message, entiteNode.getLabel()));
        }

        if (favorisRecherche.getFeuilleRouteCreationDateMin() != null) {
            String message = ResourceHelper.getString(
                "ss.recherche.feuilleRoute.feuilleRoute.feuilleRouteCreationDateMin.criteria"
            );
            String date = SolonDateConverter.DATE_SLASH.format(favorisRecherche.getFeuilleRouteCreationDateMin());
            criteriaList.add(MessageFormat.format(message, date));
        }

        if (favorisRecherche.getFeuilleRouteCreationDateMax() != null) {
            String message = ResourceHelper.getString(
                "ss.recherche.feuilleRoute.feuilleRoute.feuilleRouteCreationDateMax.criteria"
            );
            String date = SolonDateConverter.DATE_SLASH.format(favorisRecherche.getFeuilleRouteCreationDateMax());
            criteriaList.add(MessageFormat.format(message, date));
        }

        if (StringUtils.isNotBlank(favorisRecherche.getFeuilleRouteValidee())) {
            String message = ResourceHelper.getString(
                "ss.recherche.feuilleRoute.feuilleRoute.feuilleRouteValidee.criteria"
            );
            String label = vocabularyService.getEntryLabel(
                VocabularyConstants.BOOLEAN_VOCABULARY,
                favorisRecherche.getFeuilleRouteValidee()
            );
            criteriaList.add(MessageFormat.format(message, label));
        }

        if (StringUtils.isNotBlank(favorisRecherche.getRouteStepRoutingTaskType())) {
            String message = ResourceHelper.getString(
                "ss.recherche.feuilleRoute.routeStep.routeStepRoutingTaskType.criteria"
            );
            String labelKey = vocabularyService.getEntryLabel(
                STVocabularyConstants.ROUTING_TASK_TYPE_VOCABULARY,
                favorisRecherche.getRouteStepRoutingTaskType()
            );
            criteriaList.add(MessageFormat.format(message, ResourceHelper.getString(labelKey)));
        }

        if (StringUtils.isNotBlank(favorisRecherche.getRouteStepDistributionMailboxId())) {
            String message = ResourceHelper.getString(
                "ss.recherche.feuilleRoute.routeStep.routeStepDistributionMailboxId.criteria"
            );
            String posteId = mailboxPosteService.getPosteIdFromMailboxId(
                favorisRecherche.getRouteStepDistributionMailboxId()
            );
            OrganigrammeNode posteNode = STServiceLocator.getSTPostesService().getPoste(posteId);
            criteriaList.add(MessageFormat.format(message, posteNode.getLabel()));
        }

        if (favorisRecherche.getRouteStepEcheanceIndicative() != null) {
            String message = ResourceHelper.getString(
                "ss.recherche.feuilleRoute.routeStep.routeStepEcheanceIndicative.criteria"
            );
            criteriaList.add(MessageFormat.format(message, favorisRecherche.getRouteStepEcheanceIndicative()));
        }

        if (StringUtils.isNotBlank(favorisRecherche.getRouteStepAutomaticValidation())) {
            String message = ResourceHelper.getString(
                "ss.recherche.feuilleRoute.routeStep.routeStepAutomaticValidation.criteria"
            );
            String label = vocabularyService.getEntryLabel(
                VocabularyConstants.BOOLEAN_VOCABULARY,
                favorisRecherche.getRouteStepAutomaticValidation()
            );
            criteriaList.add(MessageFormat.format(message, label));
        }

        if (StringUtils.isNotBlank(favorisRecherche.getRouteStepObligatoireSgg())) {
            String message = ResourceHelper.getString(
                "ss.recherche.feuilleRoute.routeStep.routeStepObligatoireSgg.criteria"
            );
            String label = vocabularyService.getEntryLabel(
                VocabularyConstants.BOOLEAN_VOCABULARY,
                favorisRecherche.getRouteStepObligatoireSgg()
            );
            criteriaList.add(MessageFormat.format(message, label));
        }

        if (StringUtils.isNotBlank(favorisRecherche.getRouteStepObligatoireMinistere())) {
            String message = ResourceHelper.getString(
                "ss.recherche.feuilleRoute.routeStep.routeStepObligatoireMinistere.criteria"
            );
            String label = vocabularyService.getEntryLabel(
                VocabularyConstants.BOOLEAN_VOCABULARY,
                favorisRecherche.getRouteStepObligatoireMinistere()
            );
            criteriaList.add(MessageFormat.format(message, label));
        }

        return StringUtils.join(
            criteriaList,
            " " + ResourceHelper.getString("ss.recherche.feuilleRoute.criteria.separator") + " "
        );
    }

    @Override
    public DocumentModel mapFormToFavorisDoc(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel favoriRechercheDoc = getEspaceRechercheService()
            .createBareFavorisRecherche(session, FavorisRechercheType.MODELE_FEUILLE_ROUTE);
        EpgFavorisRechercheModeleFdrForm form = context.getFromContextData(FAVORIS_RECHERCHE_MODELE_FDR_FORM);
        MapDoc2Bean.beanToDoc(form, favoriRechercheDoc);
        final FavorisRecherche favorisRecherche = favoriRechercheDoc.getAdapter(FavorisRecherche.class);

        FavorisRechercheForm favorisRechercheForm = context.getFromContextData(FAVORIS_RECHERCHE_FORM);
        Objects.requireNonNull(favorisRechercheForm);
        String intitule = favorisRechercheForm.getIntitule();
        favorisRecherche.setTitle(intitule);

        return favoriRechercheDoc;
    }
}
