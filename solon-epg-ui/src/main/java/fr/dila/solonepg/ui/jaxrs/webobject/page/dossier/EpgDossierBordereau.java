package fr.dila.solonepg.ui.jaxrs.webobject.page.dossier;

import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getProfilUtilisateurService;
import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getEpgDossierCreationUIService;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.IS_DOSSIER_CREATION;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.POSTES_LIST;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.POSTE_SELECTED;
import static fr.dila.ss.ui.enums.SSActionCategory.DOSSIER_TAB_ACTIONS;
import static fr.dila.st.core.util.ObjectHelper.requireNonNullElseGet;
import static fr.dila.st.ui.helper.UserSessionHelper.getUserSessionParameter;

import fr.dila.solonepg.api.administration.ProfilUtilisateur;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.dossier.bordereau.BordereauSaveForm;
import fr.dila.solonepg.ui.bean.dossier.bordereau.EpgBordereauDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.BooleanUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliDossierBordereau")
public class EpgDossierBordereau extends SolonWebObject {

    public EpgDossierBordereau() {
        super();
    }

    @GET
    public ThTemplate getBordereau() {
        Map<String, Object> map = requireNonNullElseGet(template.getData(), HashMap::new);

        EpgBordereauDTO bordereauDto = EpgUIServiceLocator.getBordereauUIService().getBordereau(context);
        map.put("bordereauDto", bordereauDto);

        if (BooleanUtils.isTrue(bordereauDto.getIsEdit())) {
            map.put("typeActeOptions", EpgUIServiceLocator.getBordereauUIService().getTypeActeOptions(context));
            List<SelectValueDTO> categorieActeOptions = EpgUIServiceLocator
                .getEpgSelectValueUIService()
                .getCategorieActe();
            if (!EpgUIServiceLocator.getBordereauUIService().isVisibleCategorieActeConventionCollective(context)) {
                categorieActeOptions =
                    categorieActeOptions
                        .stream()
                        .filter(option -> !VocabularyConstants.NATURE_CONVENTION_COLLECTIVE.equals(option.getId()))
                        .collect(Collectors.toList());
            }
            map.put("categorieActeOptions", categorieActeOptions);
            map.put(
                "statutArchivageOptions",
                VocabularyConstants
                    .LIST_LIBELLE_STATUT_ARCHIVAGE_PAR_ID.entrySet()
                    .stream()
                    .map(entry -> new SelectValueDTO(entry.getKey(), entry.getValue()))
                    .sorted(Comparator.comparing(SelectValueDTO::getLabel))
                    .collect(Collectors.toList())
            );
            map.put("prioriteOptions", EpgUIServiceLocator.getEpgSelectValueUIService().getPrioriteCE());
            map.put(
                "vecteurPublicationOptions",
                EpgUIServiceLocator
                    .getBordereauUIService()
                    .getVecteurPublicationList(context, bordereauDto.getParution().getVecteurPublication())
            );
            map.put(
                "modeParutionOptions",
                EpgUIServiceLocator.getBordereauUIService().getModesParution(context.getSession())
            );
            map.put(
                "delaiPublicationOptions",
                EpgUIServiceLocator.getEpgSelectValueUIService().getDelaiPublicationFiltered()
            );
            map.put(
                "rubriquesOptions",
                SolonEpgServiceLocator
                    .getIndexationEpgService()
                    .findAllIndexationRubrique(context.getSession(), "")
                    .stream()
                    .map(i -> new SelectValueDTO(i, i))
                    .collect(Collectors.toList())
            );
            map.put(
                "motsClesOptions",
                SolonEpgServiceLocator
                    .getIndexationEpgService()
                    .findAllIndexationMotCleForDossier(context.getSession(), context.getCurrentDocument(), "")
                    .stream()
                    .map(i -> new SelectValueDTO(i, i))
                    .collect(Collectors.toList())
            );
            map.put("natureTexteOptions", EpgUIServiceLocator.getEpgSelectValueUIService().getNatureTexteBaseLegale());
            map.put("integraleOuExtraitOptions", EpgUIServiceLocator.getEpgSelectValueUIService().getTypePublication());
        }
        map.put("tabActions", context.getActions(DOSSIER_TAB_ACTIONS));
        map.put(STTemplateConstants.ID_DOSSIER, context.getCurrentDocument().getId());
        map.put(
            EpgTemplateConstants.PERIODICITE_RAPPORT_OPTIONS,
            EpgUIServiceLocator.getEpgSelectValueUIService().getPeriodiciteRapport()
        );
        Boolean isRectificatif = (Boolean) getUserSessionParameter(context, EpgUserSessionKey.RECTIFICATIF);
        map.put(IS_DOSSIER_CREATION, isRectificatif);
        if (BooleanUtils.isTrue(isRectificatif)) {
            map.put(POSTES_LIST, getEpgDossierCreationUIService().getUserPostes(context));
            ProfilUtilisateur profil = getProfilUtilisateurService()
                .getProfilUtilisateurForCurrentUser(context.getSession());
            map.put(POSTE_SELECTED, profil.getPosteDefaut());
        }
        // on supprime la valeur en session
        UserSessionHelper.clearUserSessionParameter(context, EpgUserSessionKey.RECTIFICATIF);

        template.setData(map);
        return template;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveBordereau(@SwBeanParam BordereauSaveForm form) {
        context.putInContextData(EpgContextDataKey.BORDEREAU_SAVE_FORM, form);
        EpgUIServiceLocator.getBordereauUIService().saveBordereau(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/dossier/onglets/bordereau", context);
    }
}
