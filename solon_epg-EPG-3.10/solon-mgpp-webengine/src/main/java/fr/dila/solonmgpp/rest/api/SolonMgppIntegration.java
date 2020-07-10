package fr.dila.solonmgpp.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.platform.usermanager.NuxeoPrincipalImpl;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.exceptions.WebResourceNotFoundException;
import org.nuxeo.ecm.webengine.model.exceptions.WebSecurityException;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;

import fr.dila.solonepp.rest.api.WSNotification;
import fr.dila.solonmgpp.rest.management.WSNotificationImpl;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epg.ResultatTraitement;

/**
 * Racine du module des Web Services pour le MGPP. Classe requise par le fichier module.xml : le bundle fr.dila.solonmgpp.webengine doit avoir au moins un composant.
 * 
 * @author arolin
 */
@Path("solonmgpp")
@WebObject(type = "SolonMgppIntegration")
public class SolonMgppIntegration extends ModuleRoot {

    NuxeoPrincipalImpl principal;

    @GET
    @Produces("text/html;charset=UTF-8")
    public Object doGet() {
        return getView("base");
    }

    @Path(WSNotification.SERVICE_NAME)
    public WSNotificationImpl getWSNotification() {
        return (WSNotificationImpl) newObject(WSNotification.SERVICE_NAME);
    }

    /**
     * Handle Errors
     * 
     * @see org.nuxeo.ecm.webengine.model.impl.ModuleRoot#handleError(javax.ws.rs.WebApplicationException)
     */
    @Override
    public Object handleError(WebApplicationException e) {
        ResultatTraitement result = new ResultatTraitement();
        result.setStatut(TraitementStatut.KO);
        if (e instanceof WebSecurityException) {
            result.setMessageErreur("Vous n'avez pas les droits nécessaire pour accéder à cette resource");
            return Response.status(401).entity(result).type("text/xml").build();
        } else if (e instanceof WebResourceNotFoundException) {
            result.setMessageErreur("Resource inexistante");
            return Response.status(404).entity(result).type("text/xml").build();
        } else {
            result.setMessageErreur(e.getMessage());
            return Response.status(404).entity(result).type("text/xml").build();
        }
    }
}
