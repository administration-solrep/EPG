package fr.dila.solonepg.ui.jaxrs.webobject;

import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.nuxeo.ecm.webengine.model.WebObject;

@Produces("text/html;charset=UTF-8")
@WebObject(type = "MgppAjax")
public class MgppAjax extends SolonWebObject {

    public MgppAjax() {
        super();
    }

    @Path("dossier")
    public Object dossier() {
        return newObject("DossierCommunicationAjax", context);
    }

    @Path("dossierSimple")
    public Object dossiersimple() {
        return newObject("DossierSimpleAjax", context);
    }

    @Path("communication")
    public Object communication() {
        return newObject("CommunicationAjax", context);
    }

    @Path("recherche")
    public Object recherche() {
        return newObject("RechercheMgppAjax", context);
    }

    @Path("recherche/experte")
    public Object doRechercheExperte() {
        return newObject("RechercheExperteMgppAjax", context);
    }

    @Path("fiche")
    public Object fiche() {
        return newObject("DossierCommunicationFicheAjax", context);
    }

    @Path("modele/courrier")
    public Object modeleCourrier() {
        return newObject("ModeleCourrierAjax", context);
    }

    @Path("calendrier")
    public Object getCalendrier() {
        return newObject("CalendrierAjax", context);
    }

    @Path("referenceEPP")
    public Object tableReferenceEpp() {
        return newObject("TableReferenceEPPAjax", context);
    }
}
