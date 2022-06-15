package fr.dila.solonepg.ui.jaxrs.webobject.ajax.admin.dossier;

import static fr.dila.solonepg.ui.th.constants.EpgURLConstants.URL_INDEXATION_ADD_SIGNATAIRE;

import fr.dila.solonepg.ui.bean.dossier.tablesreference.AdminModeParutionDTO;
import fr.dila.solonepg.ui.bean.dossier.tablesreference.AdminTableReferenceDTO;
import fr.dila.solonepg.ui.bean.dossier.tablesreference.EpgIndexationSignataireDTO;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossiersTableReferenceAjax")
public class EpgDossierTableReferenceAjax extends SolonWebObject {
    private static final String MODE_PARUTION_TABLE_FRAGMENT = "fragments/dossier/table-reference/mode-parution";

    @POST
    @Path("sauvegarder")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveTableReference(@SwBeanParam AdminTableReferenceDTO dto) {
        context.putInContextData(EpgContextDataKey.ADMIN_TABLE_REFERENCE, dto);
        EpgUIServiceLocator.getEpgDossierTablesReferenceUIService().saveReferences(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Path("signataire/ajouter")
    public ThTemplate ajouterSignataire(@SwRequired @FormParam("signataire") String signataire) {
        verifyAction(EpgActionEnum.REMOVE_SIGNATAIRE, EpgURLConstants.URL_INDEXATION_DELETE_SIGNATAIRE);
        List<EpgIndexationSignataireDTO> signataireLibre = getSignataireLibre();
        if (
            StringUtils.isNotBlank(signataire) && signataireLibre.stream().noneMatch(s -> signataire.equals(s.getId()))
        ) {
            // ajouter le signataire
            signataireLibre.add(
                new EpgIndexationSignataireDTO(
                    signataire,
                    signataire,
                    context.getAction(EpgActionEnum.REMOVE_SIGNATAIRE)
                )
            );
        }

        return buildInputListTemplateForSignataire(signataireLibre);
    }

    @POST
    @Path("signataire/supprimer")
    public ThTemplate supprimerSignataire(@SwRequired @FormParam("id") String id) {
        List<EpgIndexationSignataireDTO> signataireLibre = getSignataireLibre();
        List<EpgIndexationSignataireDTO> lNouveauSignataireLibre = new ArrayList<>();
        // supprimer le signataire de la liste
        for (EpgIndexationSignataireDTO signataire : signataireLibre) {
            if (!id.equals(signataire.getId())) {
                lNouveauSignataireLibre.add(signataire);
            }
        }

        return buildInputListTemplateForSignataire(lNouveauSignataireLibre);
    }

    /**
     * Récupérer la liste des signataires libres (en session ou à partir du dto)
     *
     * @return la liste des signataires libres
     */
    private List<EpgIndexationSignataireDTO> getSignataireLibre() {
        List<EpgIndexationSignataireDTO> lSignataireLibre;
        if (UserSessionHelper.getUserSessionParameter(context, EpgUserSessionKey.LIST_SIGNATAIRE_LIBRE) != null) {
            // on recupère la liste en session
            lSignataireLibre =
                UserSessionHelper.getUserSessionParameter(context, EpgUserSessionKey.LIST_SIGNATAIRE_LIBRE);
        } else {
            // sinon on récupère la liste à partir du dto
            AdminTableReferenceDTO dto = EpgUIServiceLocator
                .getEpgDossierTablesReferenceUIService()
                .getReferences(context);
            lSignataireLibre =
                dto.getSignatairesLibres() == null
                    ? new ArrayList<>()
                    : dto
                        .getSignatairesLibres()
                        .stream()
                        .map(
                            nomSignataire ->
                                new EpgIndexationSignataireDTO(
                                    nomSignataire,
                                    nomSignataire,
                                    context.getAction(EpgActionEnum.REMOVE_SIGNATAIRE)
                                )
                        )
                        .collect(Collectors.toList());
        }
        return lSignataireLibre;
    }

    private ThTemplate buildInputListTemplateForSignataire(List<EpgIndexationSignataireDTO> signataireLibre) {
        verifyAction(EpgActionEnum.ADD_SIGNATAIRE, URL_INDEXATION_ADD_SIGNATAIRE);
        ThTemplate template = new AjaxLayoutThTemplate("fragments/components/input-list", context);
        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.ID, "signatairesLibres");
        map.put(EpgTemplateConstants.LABEL, "admin.table.reference.signataire");
        map.put(EpgTemplateConstants.LEGEND, "admin.table.reference.signataire.ajouter");
        map.put(EpgTemplateConstants.ADD_ACTION, context.getAction(EpgActionEnum.ADD_SIGNATAIRE));
        map.put(STTemplateConstants.RESULT_LIST, signataireLibre);
        template.setData(map);

        UserSessionHelper.putUserSessionParameter(context, EpgUserSessionKey.LIST_SIGNATAIRE_LIBRE, signataireLibre);

        return template;
    }

    @POST
    @Path("mode/parution/sauvegarder")
    public ThTemplate saveOrUpdateModeParution(@SwBeanParam AdminModeParutionDTO adminModeParutionDTO) {
        context.putInContextData(EpgContextDataKey.ADMIN_MODE_PARUTION, adminModeParutionDTO);
        ThTemplate template = new AjaxLayoutThTemplate(MODE_PARUTION_TABLE_FRAGMENT, context);

        if (StringUtils.isBlank(adminModeParutionDTO.getId())) {
            // save
            EpgUIServiceLocator.getEpgDossierTablesReferenceUIService().createModeParution(context);
        } else {
            // update
            EpgUIServiceLocator.getEpgDossierTablesReferenceUIService().saveModeParution(context);
        }
        putModeParutionDataInTemplate(template);

        return template;
    }

    @POST
    @Path("mode/parution/supprimer")
    public ThTemplate deleteModeParution(@SwRequired @SwNotEmpty @FormParam("id") String idMode) {
        context.putInContextData(STContextDataKey.ID, idMode);
        ThTemplate template = new AjaxLayoutThTemplate(MODE_PARUTION_TABLE_FRAGMENT, context);
        // suppression
        EpgUIServiceLocator.getEpgDossierTablesReferenceUIService().deleteModeParution(context);
        putModeParutionDataInTemplate(template);

        return template;
    }

    private void putModeParutionDataInTemplate(ThTemplate template) {
        // récupérer la liste des modes de parution
        List<AdminModeParutionDTO> lModeParution = EpgUIServiceLocator
            .getEpgDossierTablesReferenceUIService()
            .getListModeParution(context);
        // ajout au template data
        Map<String, Object> map = template.getData();
        map.put(EpgTemplateConstants.LST_MODE_PARUTION, lModeParution);
        map.put(EpgTemplateConstants.NB_MODE_PARUTION, lModeParution.size());
        map.put(STTemplateConstants.TABLE_ACTIONS, context.getActions(EpgActionCategory.MODE_PARUTION_TABLE_ACTIONS));
        map.put(
            EpgTemplateConstants.ADD_ACTION_MODE_PARUTION,
            context.getActions(EpgActionCategory.MODE_PARUTION_ADD_ACTIONS)
        );
    }
}
