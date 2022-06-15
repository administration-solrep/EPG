package fr.dila.solonmgpp.rest.api;

import fr.sword.xsd.solon.epp.NotifierEvenementRequest;
import fr.sword.xsd.solon.epp.NotifierEvenementResponse;
import fr.sword.xsd.solon.epp.NotifierTableDeReferenceRequest;
import fr.sword.xsd.solon.epp.NotifierTableDeReferenceResponse;

/**
 * Interface du webservice reçevant les notifications de solon epp.
 *
 * @author arolin
 */
public interface NotificationDelegate {
    /**
     *
     *
     * @param request
     * @return
     * @throws Exception
     */
    NotifierEvenementResponse notifierEvenement(NotifierEvenementRequest request) throws Exception;

    /**
     * Notifie une modification dans les tables de référence
     * @param request
     * @return
     */
    NotifierTableDeReferenceResponse notifierTableDeReference(NotifierTableDeReferenceRequest request);
}