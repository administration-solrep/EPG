package fr.dila.solonepg.ui.jaxrs.webobject.ajax.fdr;

import fr.dila.solonepg.ui.jaxrs.webobject.page.fdr.EpgSubstitutionFDRMass;
import fr.dila.solonepg.ui.services.EpgModeleFdrListUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.ss.api.criteria.SubstitutionCriteria;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.th.bean.ModeleFDRListForm;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "SubstitutionMassAjax")
public class EpgSubstitutionFDRMassAjax extends SolonWebObject {

    @GET
    @Path("liste")
    public ThTemplate getListModeleSubstitution(@SwBeanParam ModeleFDRListForm modeleFDRform) {
        ThTemplate template = new AjaxLayoutThTemplate("fragments/table/tableModelesFDR", context);
        context.putInContextData(SSContextDataKey.LIST_MODELE_FDR, modeleFDRform);

        // Créer les critéria pour filtrer les modèles par ministère du user courant
        SSPrincipal principal = (SSPrincipal) context.getSession().getPrincipal();
        SubstitutionCriteria criteria = new SubstitutionCriteria(new ArrayList<String>(principal.getMinistereIdSet()));
        context.putInContextData(SSContextDataKey.SUBSTITUTION_CRITERIA, criteria);

        // Récupération de la liste des modèles disponnible pour la substitution
        EpgModeleFdrListUIService modeleFDRListUIService = EpgUIServiceLocator.getEpgModeleFdrListUIService();
        ModeleFDRList lstResults = modeleFDRListUIService.getModelesFDRSubstitution(context);

        // Je renvoie mon formulaire en sortie
        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.RESULT_LIST, lstResults);
        map.put(STTemplateConstants.RESULT_FORM, modeleFDRform);
        map.put(STTemplateConstants.LST_COLONNES, lstResults.getListeColonnes());
        map.put(STTemplateConstants.DATA_AJAX_URL, "/ajax/selection/substitution/liste");
        map.put(EpgSubstitutionFDRMass.KEY_IS_MASS_SUBSTITUTION, true);
        template.setData(map);
        return template;
    }
}
