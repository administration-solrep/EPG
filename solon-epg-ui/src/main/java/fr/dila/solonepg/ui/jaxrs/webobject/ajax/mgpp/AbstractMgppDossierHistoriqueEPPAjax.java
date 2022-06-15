package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import fr.dila.solonepg.ui.th.constants.MgppTemplateConstants;
import fr.dila.st.ui.bean.DossierHistoriqueEPP;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import org.apache.commons.collections4.CollectionUtils;

public abstract class AbstractMgppDossierHistoriqueEPPAjax extends SolonWebObject {

    protected AbstractMgppDossierHistoriqueEPPAjax() {
        super();
    }

    @GET
    public ThTemplate getHistorique(@PathParam("id") String id, @PathParam("tab") String tab) {
        if (template.getData() == null) {
            Map<String, Object> map = new HashMap<>();
            template.setData(map);
        }

        DossierHistoriqueEPP historique = getDossierHistoriqueEpp(id);

        if (CollectionUtils.isNotEmpty(context.getMessageQueue().getErrorQueue())) {
            String historiqueEppMessage = context
                .getMessageQueue()
                .getErrorQueue()
                .stream()
                .flatMap(a -> a.getAlertMessage().stream())
                .collect(Collectors.joining("\n"));
            template.getData().put(MgppTemplateConstants.HISTORIQUE_EPP_MESSAGE, historiqueEppMessage);
            context.getMessageQueue().getErrorQueue().clear();
        }

        template.getData().put(MgppTemplateConstants.IS_MGPP, true);
        template.getData().put(MgppTemplateConstants.LST_VERSIONS, historique.getLstVersions());
        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/mgpp/dossier/onglets/historiqueEPP", getMyContext());
    }

    protected abstract DossierHistoriqueEPP getDossierHistoriqueEpp(String id);
}
