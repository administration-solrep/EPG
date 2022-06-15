package fr.dila.solonepg.ui.jaxrs.webobject.ajax.suiviLibre;

import fr.dila.solonepg.ui.bean.MgppFichePresentationOEPList;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.MgppFichePresentationOEPForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.sword.naiad.nuxeo.commons.core.util.SessionUtil;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.ws.rs.GET;
import org.nuxeo.ecm.core.api.CloseableCoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.runtime.api.Framework;

@WebObject(type = "SuiviLibreActiviteParlementaireAjax")
public class EpgSuiviLibreActiviteParlementaireAjax extends SolonWebObject {

    public EpgSuiviLibreActiviteParlementaireAjax() {
        super();
    }

    @GET
    public ThTemplate getResults(@SwBeanParam MgppFichePresentationOEPForm form) {
        init(template, form, context);
        return template;
    }

    public static void init(ThTemplate template, MgppFichePresentationOEPForm form, SpecificContext context) {
        Map<String, Object> map = new HashMap<>();
        map.put(EpgTemplateConstants.DOWNLOAD_ACTION, context.getAction(EpgActionEnum.ACTIVITE_PARLEMENTAIRE_DOWNLOAD));

        form = ObjectHelper.requireNonNullElseGet(form, MgppFichePresentationOEPForm::new);
        form.noPagination();

        context.putInContextData(MgppContextDataKey.FORM, form);

        try {
            LoginContext loginContext = Framework.login();
            try (CloseableCoreSession session = SessionUtil.openSession()) {
                context.setSession(session);
                MgppFichePresentationOEPList fiches = MgppUIServiceLocator
                    .getMgppDossierUIService()
                    .getFichePresentationOEP(context);
                map.put(STTemplateConstants.RESULT_LIST, fiches);
                map.put(STTemplateConstants.LST_COLONNES, fiches.getListeColonnes());
                map.put(STTemplateConstants.DATA_URL, EpgURLConstants.URL_SUIVI_LIBRE_ACTIVITE_PARLEMENTAIRE);
                map.put(STTemplateConstants.DATA_AJAX_URL, EpgURLConstants.URL_AJAX_SUIVI_LIBRE_ACTIVITE_PARLEMENTAIRE);
                map.put(
                    EpgTemplateConstants.DOWNLOAD_ACTION,
                    context.getAction(EpgActionEnum.ACTIVITE_PARLEMENTAIRE_DOWNLOAD)
                );
            }
            loginContext.logout();
        } catch (LoginException e) {
            throw new NuxeoException(e);
        }

        template.setData(map);
        template.setContext(context);
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/suiviLibre/activiteParlementaireTable", context);
    }
}
