package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.ui.bean.MessageList;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.MessageListForm;
import fr.dila.solonepg.ui.th.model.bean.MgppRechercheRapideForm;
import fr.dila.solonmgpp.api.constant.SolonMgppActionConstant;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.core.dto.CritereRechercheDTOImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "RechercheRapideMgppAjax")
public class MgppRechercheRapideAjax extends SolonWebObject {
    private static final List<String> RECHERCHE_RAPIDE_NOR_TYPES = ImmutableList.of(
        SolonMgppActionConstant.DEPOT_DE_RAPPORT,
        SolonMgppActionConstant.DECRET,
        SolonMgppActionConstant.RESOLUTION_ARTICLE_341
    );

    @POST
    public ThTemplate doHome(@SwBeanParam MgppRechercheRapideForm form) {
        return doRechercheRapideCommunication(form, null);
    }

    @POST
    @Path("rechercher")
    public ThTemplate doRechercheRapideCommunication(
        @SwBeanParam MgppRechercheRapideForm form,
        @FormParam("search") String jsonSearch
    ) {
        if (jsonSearch != null) {
            template = getMyTemplate();
            template.setName("fragments/table/tableCommunications");
            form = new MgppRechercheRapideForm(jsonSearch);
        }

        template.setContext(context);

        context.setNavigationContextTitle(
            new Breadcrumb(
                "Recherche rapide",
                "/mgpp/rapide/rechercher",
                Breadcrumb.SUBTITLE_ORDER + 1,
                context.getWebcontext().getRequest()
            )
        );

        String dossierParlementaireId = form.getIdDossierParlementaire();
        MessageListForm msgform = new MessageListForm(jsonSearch);
        CritereRechercheDTO critereRecherche = new CritereRechercheDTOImpl();
        critereRecherche.setTypeEvenement(
            SolonMgppServiceLocator.getEvenementTypeService().findEvenementTypeNameByProcedure(dossierParlementaireId)
        );
        critereRecherche.setMenu(dossierParlementaireId);
        if (RECHERCHE_RAPIDE_NOR_TYPES.contains(dossierParlementaireId)) {
            critereRecherche.setIdDossier(form.getNumeroNor());
        } else {
            critereRecherche.setIdDossier(form.getIdDossier());
            critereRecherche.setNumeroNor(form.getNumeroNor());
        }
        critereRecherche.setObjet(form.getObjet());
        critereRecherche.setTitreActe(form.getTitreActe());
        critereRecherche.setNomOrganisme(form.getNomOrganisme());

        context.putInContextData(MgppContextDataKey.MESSAGE_LIST_FORM, msgform);
        context.putInContextData(MgppContextDataKey.DOSSIERS_PARLEMENTAIRES_SELECTED, dossierParlementaireId);
        context.putInContextData(MgppContextDataKey.IS_RECHERCHE_RAPIDE, true);
        context.putInContextData(MgppContextDataKey.CRITERE_RECHERCHE, critereRecherche);
        MessageList result = MgppUIServiceLocator.getMgppCorbeilleUIService().getMessageListForCorbeille(context);

        if (template.getData() == null) {
            Map<String, Object> map = new HashMap<>();
            template.setData(map);
        }

        template.getData().put("dossiersParlementairesSelected", dossierParlementaireId);
        template.getData().put(STTemplateConstants.RESULT_LIST, result);
        template.getData().put(STTemplateConstants.LST_COLONNES, result.getListeColonnes(msgform));
        template.getData().put(STTemplateConstants.RESULT_FORM, msgform);
        template.getData().put(STTemplateConstants.DATA_URL, "/mgpp/recherche/rapide/rechercher");
        template.getData().put(STTemplateConstants.DATA_AJAX_URL, "/ajax/mgpp/recherche/rapide/rechercher");
        template.getData().put("rechercheRapideForm", form);

        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        ThTemplate myTemplate = new AjaxLayoutThTemplate();
        myTemplate.setData(new HashMap<>());
        return myTemplate;
    }
}
