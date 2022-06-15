package fr.dila.solonepg.ui.jaxrs.webobject.page.dossier;

import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.services.EpgFondDeDossierUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.th.model.EpgLayoutThTemplate;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Map;
import javax.ws.rs.GET;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "DossierSaisineRectificative")
public class EpgSaisineRectificative extends SolonWebObject {

    public EpgSaisineRectificative() {
        super();
    }

    @GET
    public ThTemplate getSaisine() throws IllegalAccessException, InstantiationException {
        ThTemplate template = getMyTemplate(context);
        template.setName("pages/dossier/saisine-rectificative");
        template.setContext(context);

        String id = context.getCurrentDocument().getId();
        String url = String.format(EpgURLConstants.URL_DOSSIER_SAISINE_RECTIFICATIVE, id);

        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("saisine.rectificative.titre"),
                url,
                Breadcrumb.SUBTITLE_ORDER + 1,
                context.getWebcontext().getRequest()
            )
        );

        EpgFondDeDossierUIService epgFondDeDossierUIService = EpgUIServiceLocator.getEpgFondDeDossierUIService();

        Map<String, Object> map = template.getData();
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        map.put(STTemplateConstants.ID_DOSSIER, id);
        map.put(EpgTemplateConstants.SAISINES, epgFondDeDossierUIService.getDocumentsSaisineRectificative(context));
        map.put(
            EpgTemplateConstants.PIECES_COMPLEMENTAIRES,
            epgFondDeDossierUIService.getDocumentsPiecesComplementairesSaisine(context)
        );
        map.put(
            EpgTemplateConstants.SAISINE_ACTIONS,
            context.getActions(EpgActionCategory.DOSSIER_SAISINE_RECTIFICATIVE)
        );
        map.put(STTemplateConstants.DATA_URL, url);
        map.put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_SAISINE_RECTIFICATIVE);

        template.setData(map);

        return template;
    }

    protected ThTemplate getMyTemplate(SpecificContext context) throws IllegalAccessException, InstantiationException {
        @SuppressWarnings("unchecked")
        Class<ThTemplate> oldTemplate = (Class<ThTemplate>) context
            .getWebcontext()
            .getUserSession()
            .get(SpecificContext.LAST_TEMPLATE);
        if (oldTemplate == ThTemplate.class) {
            return new EpgLayoutThTemplate();
        } else {
            return oldTemplate.newInstance();
        }
    }
}
