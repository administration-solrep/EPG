package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.tableauxdynamiques;

import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getTableauDynamiqueUIService;

import fr.dila.solonepg.ui.th.bean.TableauDynamiqueForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRequired;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "TableauxDynamiques")
public class EpgSuiviTableauxDynamiques extends SolonWebObject {
    private static final String MODIFICATION_TITLE = "Modification du tableau dynamique %s";
    private static final String CREATE_NAVIGATION_TITLE = "Création d'un tableau dynamique";
    private static final String VISIBILITE_DESTINATAIRES = "visibiliteDestinataires";

    @GET
    @Path("creer")
    public ThTemplate getCreerTableauDynamique(@SwNotEmpty @QueryParam("type") String type) {
        template.setName("pages/suivi/tableaux/editTableauDynamique");
        template.setContext(context);
        context.setNavigationContextTitle(
            new Breadcrumb(CREATE_NAVIGATION_TITLE, "/suivi/tableaux/creer?type=" + type, Breadcrumb.TITLE_ORDER + 1)
        );
        template
            .getData()
            .put(
                VISIBILITE_DESTINATAIRES,
                getTableauDynamiqueUIService().isDirectionViewer(context.getSession().getPrincipal())
            );
        template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        template.getData().put(EpgTemplateConstants.TYPE, type);
        template.getData().put(EpgTemplateConstants.TABLEAU_FORM, new TableauDynamiqueForm());
        return template;
    }

    @Path("executer")
    public Object executerTd() {
        template.setName("pages/suivi/tableaux/tableauxDynamiquesResultats");
        template.setContext(context);
        template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        return newObject("TableauxDynamiquesAjax", context, template);
    }

    @GET
    @Path("modifier")
    public ThTemplate getEditTab(@SwRequired @SwId @QueryParam("idTab") String idTab) {
        template.setName("pages/suivi/tableaux/editTableauDynamique");
        template.setContext(context);
        context.putInContextData(STContextDataKey.ID, idTab);
        context.setCurrentDocument(idTab);
        TableauDynamiqueForm tableauForm = getTableauDynamiqueUIService().getTableauDynamiqueForm(context);
        context.setNavigationContextTitle(
            new Breadcrumb(
                String.format(MODIFICATION_TITLE, tableauForm.getLibelle()),
                "/suivi/tableaux/modifier?idTab=" + idTab,
                Breadcrumb.TITLE_ORDER + 1
            )
        );
        template
            .getData()
            .put(
                VISIBILITE_DESTINATAIRES,
                getTableauDynamiqueUIService().isDirectionViewer(context.getSession().getPrincipal())
            );
        template.getData().put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        template.getData().put(EpgTemplateConstants.TABLEAU_FORM, tableauForm);
        return template;
    }
}
