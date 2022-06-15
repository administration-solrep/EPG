package fr.dila.solonepg.ui.jaxrs.webobject;

import static fr.dila.ss.ui.jaxrs.webobject.ajax.AbstractSSDossierAjax.DOSSIER_AJAX_WEBOBJECT;

import fr.dila.ss.ui.jaxrs.webobject.SSAjax;
import fr.dila.st.core.service.STServiceLocator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.nuxeo.ecm.webengine.model.WebObject;

@Produces("text/html;charset=UTF-8")
@WebObject(type = "AppliAjax")
public class EpgAjax extends SSAjax {

    public EpgAjax() {
        super();
    }

    @Path("mgpp")
    public Object mgpp() {
        return newObject("MgppAjax", context);
    }

    @Path("dossier")
    public Object doDossier() {
        return newObject(DOSSIER_AJAX_WEBOBJECT, context);
    }

    @Path("archivage")
    public Object doArchivage() {
        return newObject("ArchivageAjax");
    }

    @Path("suivi")
    public Object doSuivi() {
        return newObject("AppliSuiviAjax");
    }

    @Path("token")
    @GET
    public String token() {
        return STServiceLocator.getTokenService().acquireToken(context.getSession().getPrincipal(), "");
    }

    @Path("travail")
    public Object doListeDossiersTravail() {
        return newObject("AppliTravailAjax", context);
    }

    @Path("travail/attenteSignature")
    public Object doAttenteSignature() {
        return newObject("AttenteSignatureAjax", context);
    }

    @Path("dossiersimilaire")
    public Object doDossierSimilaire() {
        return newObject("DossiersimilaireAjax");
    }

    @Path("recherche/experte")
    public Object doRechercheExperte() {
        return newObject("AjaxRequeteExperte", context);
    }

    @Path("recherche/favoris")
    public Object dofavorisAjax() {
        return newObject("RechercheFavorisAjax", context);
    }

    @Path("recherche/favoris/executer")
    public Object dofavorisResultatsAjax() {
        return newObject("RechercheFavorisResultatsAjax", context);
    }

    @Path("admin/paramMoteur")
    public Object doParametresMoteur() {
        return newObject("EpgParametresMoteurAjax");
    }

    @Path("admin/param/indexation")
    public Object doParametresIndexation() {
        return newObject("EpgParametresIndexationAjax");
    }

    @Path("admin/dossiers")
    public Object doAdminDossier() {
        return newObject("AdminDossierAjax");
    }

    @Path("admin/archivage")
    public Object doAdminArchivage() {
        return newObject("AdminArchivageAjax");
    }

    @Path("selection")
    public Object doSelectionAjax() {
        return newObject("OutilSelectionAjax", context);
    }

    @Path("suiviLibre")
    public Object doSuiviLibreAjax() {
        return newObject("SuiviLibreAjax", context);
    }

    @Path("saisine/rectificative")
    public Object doSaisineRectificative() {
        return newObject("EpgSaisineRectificativeAjax");
    }
}
