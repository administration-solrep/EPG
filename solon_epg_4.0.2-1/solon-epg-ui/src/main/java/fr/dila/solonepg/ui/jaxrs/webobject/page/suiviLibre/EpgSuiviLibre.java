package fr.dila.solonepg.ui.jaxrs.webobject.page.suiviLibre;

import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import javax.ws.rs.Path;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "SuiviLibre")
public class EpgSuiviLibre extends SolonWebObject {
    private static final String CONSULTER = "/consulter";

    @Path("activiteParlementaire")
    public Object doActiveParlementaire() {
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("suiviLibre.activiteParlementaire.titre"),
                EpgURLConstants.URL_SUIVI_LIBRE_ACTIVITE_PARLEMENTAIRE,
                Breadcrumb.SUBTITLE_ORDER
            )
        );
        return newObject("ActiviteParlementaire", context);
    }

    @Path("depotAmendements")
    public Object doDepotAmendements() {
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("suiviLibre.depotAmendements.titre"),
                EpgURLConstants.URL_SUIVI_LIBRE_DEPOT_AMENDEMENTS,
                Breadcrumb.SUBTITLE_ORDER
            )
        );
        return newObject("DepotAmendements", context);
    }

    @Path("applicationLois")
    public Object doApplicationLois() {
        context.putInContextData(EpgContextDataKey.SHOW_SUIVI_LIBRE_FOOTER, true);
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("suiviLibre.applicationLois.titre"),
                EpgURLConstants.URL_SUIVI_LIBRE_APPLICATION_LOIS + CONSULTER,
                Breadcrumb.SUBTITLE_ORDER
            )
        );
        return newObject("ApplicationLois", context);
    }

    @Path("applicationOrdonnances")
    public Object doApplicationOrdonnances() {
        context.putInContextData(EpgContextDataKey.SHOW_SUIVI_LIBRE_FOOTER, true);
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("suiviLibre.applicationOrdonnances.titre"),
                EpgURLConstants.URL_SUIVI_LIBRE_APPLICATION_ORDONNANCES + CONSULTER,
                Breadcrumb.SUBTITLE_ORDER
            )
        );
        return newObject("ApplicationOrdonnances", context);
    }

    @Path("suiviOrdonnances")
    public Object doSuiviOrdonnances() {
        context.putInContextData(EpgContextDataKey.SHOW_SUIVI_LIBRE_FOOTER, true);
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("suiviLibre.suiviOrdonnances.titre"),
                EpgURLConstants.URL_SUIVI_LIBRE_SUIVI_ORDONNANCES + CONSULTER,
                Breadcrumb.SUBTITLE_ORDER
            )
        );
        return newObject("SuiviOrdonnances", context);
    }
}
