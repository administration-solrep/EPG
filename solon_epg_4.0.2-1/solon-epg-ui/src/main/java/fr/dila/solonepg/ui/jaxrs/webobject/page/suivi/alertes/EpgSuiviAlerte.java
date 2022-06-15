package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.alertes;

import static fr.dila.solonepg.ui.enums.EpgUserSessionKey.CRITERE_RECHERCHE_KEY;

import fr.dila.solonepg.ui.bean.recherchelibre.CritereRechercheForm;
import fr.dila.solonepg.ui.jaxrs.webobject.ajax.EpgRechercheAjax;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.ui.bean.AlerteForm;
import fr.dila.ss.ui.jaxrs.webobject.page.suivi.SSSuiviAlerte;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwRequired;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "SuiviAlerte")
public class EpgSuiviAlerte extends SSSuiviAlerte {
    private static final String ID_CRITERE_FORM = "critereForm";
    private static final String ID_ALERT_FRAGMENT = "alertDatasFragment";

    @GET
    @Path("creerLibre")
    public ThTemplate getAlerteLibre(@SwBeanParam CritereRechercheForm rechercheForm) {
        AlerteForm form = new AlerteForm();

        CritereRechercheForm sessionForm = ObjectHelper.requireNonNullElseGet(
            UserSessionHelper.getUserSessionParameter(context, CRITERE_RECHERCHE_KEY),
            CritereRechercheForm::new
        );

        if (rechercheForm.getBaseArchivage() != null) {
            sessionForm.setBaseArchivage(rechercheForm.getBaseArchivage());
        }

        if (rechercheForm.getBaseProduction() != null) {
            sessionForm.setBaseProduction(rechercheForm.getBaseProduction());
        }

        if (rechercheForm.getDerniereVersion() != null) {
            sessionForm.setDerniereVersion(rechercheForm.getDerniereVersion());
        }

        if (rechercheForm.getRecherche() != null) {
            sessionForm.setRecherche(rechercheForm.getRecherche());
        }

        if (rechercheForm.getExpressionExacte() != null) {
            sessionForm.setExpressionExacte(rechercheForm.getExpressionExacte());
        }

        context.removeNavigationContextTitle();

        context.setNavigationContextTitle(
            new Breadcrumb(NAVIGATION_TITLE, "/suivi/alertes/creer", Breadcrumb.TITLE_ORDER + 1)
        );

        return buildTemplate(form.getId(), sessionForm, form);
    }

    @GET
    @Path("creerExperte")
    public ThTemplate getAlerteExperte(@SwRequired @QueryParam("sessionDtoKey") String sessionDtoKey) {
        AlerteForm alerteForm = new AlerteForm();
        template = super.buildTemplate(alerteForm.getId(), alerteForm);
        template.getData().put(ID_ALERT_FRAGMENT, "fragments/components/alerte/alerte-experte");
        template.getData().put(EpgTemplateConstants.RECHERCHE_SESSION_KEY, sessionDtoKey);
        return template;
    }

    public ThTemplate buildTemplate(String formId, CritereRechercheForm critereForm, AlerteForm alerteForm) {
        template = super.buildTemplate(formId, alerteForm);
        template.getData().put(ID_CRITERE_FORM, critereForm);
        template.getData().put(ID_ALERT_FRAGMENT, "fragments/components/alerte/alerte-criteres");
        return template;
    }
}
