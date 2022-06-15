package fr.dila.solonepg.ui.jaxrs.webobject.ajax.mgpp;

import static fr.dila.solonepg.ui.services.mgpp.impl.MgppTableReferenceUIServiceImpl.TABLE_REFERENCE_EPP_KEY;
import static fr.dila.solonepg.ui.services.mgpp.impl.MgppTableReferenceUIServiceImpl.TABLE_REFERENCE_EPP_OPEN_NODES_KEY;

import fr.dila.solonepg.ui.bean.MgppTableReferenceEPP;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "TableReferenceEPPAjax")
public class MgppTableReferenceAjax extends SolonWebObject {

    @GET
    @Path("currentGouv")
    public ThTemplate getCurrentGouvernement(@QueryParam("selectedID") String id) {
        MgppTableReferenceEPP dto = MgppUIServiceLocator
            .getMgppTableReferenceUIService()
            .getCurrentGouvernement(context);

        template.getData().put(STTemplateConstants.TREE, dto.getListe());
        template.getData().put(STTemplateConstants.LEVEL, 1);
        template.getData().put(STTemplateConstants.CURRENT_ID, "tableReferenceEPP");
        template.getData().put(STTemplateConstants.IS_OPEN, true);
        template.getData().put(STTemplateConstants.SELECT_ID, id);

        template.getData().put(EpgTemplateConstants.CATEGORY, "BUTTON");

        return template;
    }

    @GET
    public ThTemplate getTableReference(@QueryParam("selectedNode") String selectedNode) {
        if (StringUtils.isNotBlank(selectedNode)) {
            // Si un noeud de la table de référence est appuyé
            @SuppressWarnings("unchecked")
            Set<String> openNodes = ObjectHelper.requireNonNullElseGet(
                UserSessionHelper.getUserSessionParameter(context, TABLE_REFERENCE_EPP_OPEN_NODES_KEY, Set.class),
                HashSet::new
            );
            // On l'ajoute ou l'enlève de la liste des noeuds actifs
            if (openNodes.contains(selectedNode)) {
                openNodes.remove(selectedNode);
            } else {
                openNodes.add(selectedNode);
            }
            // On sauvegarde la nouvelle liste de noeuds ouverts dans la
            // UserSession
            UserSessionHelper.putUserSessionParameter(context, TABLE_REFERENCE_EPP_OPEN_NODES_KEY, openNodes);
        } else {
            UserSessionHelper.putUserSessionParameter(context, TABLE_REFERENCE_EPP_KEY, null);
        }

        MgppTableReferenceEPP dto = MgppUIServiceLocator.getMgppTableReferenceUIService().getTableReferenceEPP(context);

        template.getData().put(STTemplateConstants.TREE, dto.getListe());
        template.getData().put(STTemplateConstants.LEVEL, 1);
        template.getData().put(STTemplateConstants.CURRENT_ID, "tableReferenceEPP");
        template.getData().put(STTemplateConstants.IS_OPEN, true);
        template.getData().put(EpgTemplateConstants.CATEGORY, "DROPDOWN");

        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/components/table-reference-arbre", getMyContext());
    }
}
