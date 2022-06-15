package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import fr.dila.solonepg.ui.bean.MgppCorbeilleContentList;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.mgpp.MgppRecherche;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.MgppCorbeilleContentListForm;
import fr.dila.ss.ui.bean.RequeteExperteDTO;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.jaxrs.webobject.ajax.SSRequeteExperteAjax;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "RechercheExperteMgppAjax")
public class MgppRechercheExperteAjax extends SSRequeteExperteAjax {

    public MgppRechercheExperteAjax() {
        super();
    }

    @Override
    public String getSuffixForSessionKeys(SpecificContext context) {
        return "_MGPP";
    }

    @POST
    @Path("resultats")
    public ThTemplate getResultsFromRequete(@FormParam("search") String jsonSearch) {
        // Je déclare mon template et j'instancie mon context
        ThTemplate template = new AjaxLayoutThTemplate();
        template.setName("fragments/mgpp/recherche/result-list");
        template.setContext(context);

        // Je renvoie mon formulaire en sortie
        Map<String, Object> map = new HashMap<>();

        MgppCorbeilleContentListForm form = new MgppCorbeilleContentListForm(jsonSearch);

        context.putInContextData(MgppContextDataKey.FICHE_LIST_FORM, form);
        context.putInContextData(
            SSContextDataKey.REQUETE_EXPERTE_DTO,
            UserSessionHelper.getUserSessionParameter(context, getDtoSessionKey(context), RequeteExperteDTO.class)
        );

        MgppCorbeilleContentList lstResults = MgppUIServiceLocator
            .getMgppCorbeilleUIService()
            .getFicheListForRechercheExperte(context);

        String titre = StringUtils.defaultIfBlank(
            lstResults.getTitre(),
            ResourceHelper.getString("requete.experte.result.titre")
        );

        map.put(STTemplateConstants.RESULT_LIST, lstResults);

        map.put(STTemplateConstants.RESULT_FORM, form);
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.TITRE, titre);
        map.put(
            STTemplateConstants.SOUS_TITRE,
            ResourceHelper.getString("requete.experte.result.nbResults", lstResults.getNbTotal())
        );
        map.put(STTemplateConstants.DISPLAY_TABLE, true);
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/mgpp/recherche/experte/resultats");

        template.setData(map);
        UserSessionHelper.putUserSessionParameter(context, getResultsSessionKey(context), map);

        return template;
    }

    @Override
    protected String getContribName() {
        return MgppRecherche.MGPP_RECHERCHE_EXPERTE_CHAMP_CONTRIB_NAME;
    }
}
