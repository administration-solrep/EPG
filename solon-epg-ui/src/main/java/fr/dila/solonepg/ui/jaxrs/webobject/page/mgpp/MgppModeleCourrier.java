package fr.dila.solonepg.ui.jaxrs.webobject.page.mgpp;

import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getModeleCourrierUIService;
import static fr.dila.st.api.constant.MediaType.APPLICATION_OPENXML_WORD;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.ui.bean.ModeleCourrierDTO;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionEnum;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.th.bean.ModeleCourrierConsultationForm;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.ss.ui.th.model.SSAdminTemplate;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "MgppModeleCourrier")
public class MgppModeleCourrier extends SolonWebObject {

    public MgppModeleCourrier() {
        super();
    }

    @GET
    public ThTemplate getModeleCourrierListe() {
        verifyAction(
            MgppActionEnum.ADMIN_MENU_ESPACE_PARL_GESTION_MODELE_COURRIERS,
            EpgURLConstants.URL_MGPP_COURRIERS
        );
        ThTemplate template = getMyTemplate();
        template.setName("pages/admin/modele/courrier/liste");

        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("menu.admin.modele.courrier"),
                EpgURLConstants.URL_MGPP_COURRIERS,
                Breadcrumb.TITLE_ORDER
            )
        );
        template.setContext(context);

        Map<String, Object> map = new HashMap<>();
        map.put("createAction", context.getAction(MgppActionEnum.ADMIN_MODELE_COURRIER_CREATE));

        ModeleCourrierDTO epgModeleCourrierDTO = getModeleCourrierUIService().getModeleCourrierDTO(context);

        map.put(STTemplateConstants.LST_COLONNES, epgModeleCourrierDTO.getLstColonnes());
        map.put("modeleCourriers", epgModeleCourrierDTO.getModeleCourriers());
        template.setData(map);
        return template;
    }

    @GET
    @Path("{id}")
    public ThTemplate getModeleCourrier(@PathParam("id") String modeleCourrierId) {
        template.setName("pages/admin/modele/courrier/view");
        context.setNavigationContextTitle(
            new Breadcrumb(
                modeleCourrierId,
                EpgURLConstants.URL_MGPP_COURRIERS + modeleCourrierId,
                Breadcrumb.SUBTITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );
        template.setContext(context);

        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.EDIT_ACTIONS, context.getActions(MgppActionCategory.ADMIN_VIEW_COURRIER));
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        context.putInContextData(MgppContextDataKey.MODELE_NAME, modeleCourrierId);
        context.putInContextData(MgppContextDataKey.MODELE_CONSULTATION, true);
        ModeleCourrierConsultationForm modeleCourrierForm = getModeleCourrierUIService()
            .convertToModeleCourrierConsultationForm(context);

        map.put("modeleCourrierForm", modeleCourrierForm);
        template.setData(map);
        return template;
    }

    @GET
    @Path("ajout")
    public ThTemplate ajoutModeleCourrier() {
        ThTemplate template = getMyTemplate();
        template.setName("pages/admin/modele/courrier/createCourrier");
        Map<String, Object> map = new HashMap<>();

        context.setNavigationContextTitle(
            new Breadcrumb(
                "Ajout d'un modèle de courrier",
                EpgURLConstants.URL_MGPP_COURRIERS + "ajout",
                Breadcrumb.SUBTITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );
        template.setContext(context);

        List<SelectValueDTO> evenementTypes = SolonMgppServiceLocator
            .getEvenementTypeService()
            .findEvenementType()
            .stream()
            .map(type -> new SelectValueDTO(type.getName(), type.getLabel()))
            .sorted(Comparator.comparing(SelectValueDTO::getLabel))
            .collect(Collectors.toList());

        //Rajout du traitement papier manuellement car il ne s'agit pas d'un type de communications
        evenementTypes.add(new SelectValueDTO("TP", "Traitement papier"));

        map.put("evenementTypes", evenementTypes);
        map.put("acceptedType", ImmutableList.of(APPLICATION_OPENXML_WORD.extension()));

        template.setData(map);
        return template;
    }

    @GET
    @Path("modifier/{id}")
    public ThTemplate modifierModeleCourrier(@PathParam("id") String modeleCourrierId) {
        ThTemplate template = getMyTemplate();
        template.setName("pages/admin/modele/courrier/createCourrier");

        context.setNavigationContextTitle(
            new Breadcrumb(
                "Modification",
                EpgURLConstants.URL_MGPP_COURRIERS + "modifier/" + modeleCourrierId,
                Breadcrumb.SUBTITLE_ORDER + 1,
                context.getWebcontext().getRequest()
            )
        );
        template.setContext(context);

        Map<String, Object> map = new HashMap<>();
        context.putInContextData(MgppContextDataKey.MODELE_NAME, modeleCourrierId);
        ModeleCourrierConsultationForm modeleCourrierForm = getModeleCourrierUIService()
            .convertToModeleCourrierConsultationForm(context);

        List<SelectValueDTO> evenementTypes = SolonMgppServiceLocator
            .getEvenementTypeService()
            .findEvenementType()
            .stream()
            .map(type -> new SelectValueDTO(type.getName(), type.getLabel()))
            .sorted(Comparator.comparing(SelectValueDTO::getLabel))
            .collect(Collectors.toList());

        //Rajout du traitement papier manuellement car il ne s'agit pas d'un type de communications
        evenementTypes.add(new SelectValueDTO("TP", "Traitement papier"));

        map.put("evenementTypes", evenementTypes);
        map.put("acceptedType", ImmutableList.of(APPLICATION_OPENXML_WORD.extension()));
        map.put("modeleCourrierForm", modeleCourrierForm);

        template.setData(map);
        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new SSAdminTemplate();
    }
}
