package fr.dila.solonepg.ui.jaxrs.webobject.ajax;

import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.DEFAULT_POSTE;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.DEFAUT_POSTE_OPTION;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.INSTALL_SOLONEDIT;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.LINK_DOWNLOAD_SOLONEDIT;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.MAJ_CONSEIL_ETAT;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.MESURES_NOMINATIVES;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.METADATA_VALUES;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.NB_DOSSIER_PAGE;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.NB_DOSSIER_PAGE_OPTION;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.PROFIL_VALUES;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.RETOUR_SGG;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.SUR_SIGNATURE;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.TYPE_ACTE_SELECTED;
import static fr.dila.solonepg.ui.th.constants.EpgTemplateConstants.VAL_AUTO_ETAPE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.api.service.vocabulary.TypeActeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.helper.EpgDossierListHelper;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.ProfilParametersForm;
import fr.dila.ss.ui.jaxrs.webobject.ajax.SSProfileAjax;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.service.organigramme.OrganigrammeService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.bean.SuggestionDTO;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.bean.PaginationForm;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "ProfileAjax")
public class EpgProfilAjax extends SSProfileAjax {

    @GET
    @Path("metadatas")
    public ThTemplate getContentModale() {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/components/profilUtilisateurModalContent", context);
        STUserService userService = STServiceLocator.getSTUserService();
        ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();

        STUser stUser = userService.getUser(context.getSession().getPrincipal().getName());

        List<String> profilValues = profilUtilisateurService.getAllowedDossierColumn(context.getSession());

        List<SelectValueDTO> metadataValues = profilUtilisateurService
            .getVocEntryAllAllowedEspaceTraitementColumn(context.getSession())
            .stream()
            .filter(vocEntry -> EpgDossierListHelper.filterColumnsUserCanOptToDisplay(vocEntry))
            .map(userColumn -> new SelectValueDTO(userColumn.getId(), userColumn.getLabel()))
            .collect(Collectors.toList());

        OrganigrammeService organigrammeService = STServiceLocator.getOrganigrammeService();

        List<SelectValueDTO> poste = stUser
            .getPostes()
            .stream()
            .map(posteId -> (PosteNode) organigrammeService.getOrganigrammeNodeById(posteId, OrganigrammeType.POSTE))
            .map(posteNode -> new SelectValueDTO(posteNode.getId(), posteNode.getLabel()))
            .collect(Collectors.toList());

        DocumentModel profilUser = profilUtilisateurService
            .getProfilUtilisateurForCurrentUser(context.getSession())
            .getDocument();
        ProfilParametersForm dto = MapDoc2Bean.docToBean(profilUser, ProfilParametersForm.class);

        TypeActeService typeActeService = SolonEpgServiceLocator.getTypeActeService();

        Map<String, Object> map = new HashMap<>();
        map.put(INSTALL_SOLONEDIT, dto.isInstallSolonedit());
        map.put(VAL_AUTO_ETAPE, dto.isValAutoEtape());
        map.put(MAJ_CONSEIL_ETAT, dto.isMajConseilEtat());
        map.put(SUR_SIGNATURE, dto.isSurSignature());
        map.put(RETOUR_SGG, dto.isRetourSGG());
        map.put(MESURES_NOMINATIVES, dto.isMesuresNominatives());
        map.put(METADATA_VALUES, metadataValues);
        map.put(PROFIL_VALUES, profilValues);
        map.put(NB_DOSSIER_PAGE, dto.getNbDossierPage());
        map.put(NB_DOSSIER_PAGE_OPTION, getNbDossierPage());
        map.put(DEFAUT_POSTE_OPTION, poste);
        map.put(
            TYPE_ACTE_SELECTED,
            dto
                .getTypeActe()
                .stream()
                .map(
                    id ->
                        typeActeService.getEntry(id).map(t -> new SelectValueDTO(t.getKey(), t.getValue())).orElse(null)
                )
                .collect(Collectors.toCollection(ArrayList::new))
        );
        map.put(DEFAULT_POSTE, dto.getDefaultPoste());
        map.put(LINK_DOWNLOAD_SOLONEDIT, "#");
        template.setData(map);
        return template;
    }

    @POST
    @Path("parametres")
    @Produces(MediaType.APPLICATION_JSON)
    public Response applyParameters(@SwBeanParam ProfilParametersForm params) {
        DocumentModel profil = SolonEpgServiceLocator
            .getProfilUtilisateurService()
            .getProfilUtilisateurForCurrentUser(context.getSession())
            .getDocument();

        MapDoc2Bean.beanToDoc(params, profil);
        context.getSession().saveDocument(profil);

        context
            .getMessageQueue()
            .addToastSuccess(ResourceHelper.getString("attente.signature.toast.success.modification"));

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    private List<SelectValueDTO> getNbDossierPage() {
        return PaginationForm
            .getAllSizes()
            .stream()
            .map(size -> new SelectValueDTO(size, size))
            .collect(Collectors.toList());
    }

    @GET
    @Path("suggestions")
    public String getSuggestions(@SwRequired @QueryParam("input") String input) throws JsonProcessingException {
        List<SuggestionDTO> suggestions = SolonEpgUIServiceLocator
            .getEpgSelectValueUIService()
            .getTypeActe()
            .stream()
            .filter(select -> StringUtils.startsWithIgnoreCase(select.getLabel(), input))
            .map(select -> new SuggestionDTO(select.getKey(), select.getLabel()))
            .collect(Collectors.toList());

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(suggestions);
    }
}
