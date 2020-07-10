package fr.dila.solonepg.api.service;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.st.api.event.batch.BatchLoggerModel;
import fr.dila.st.api.feuilleroute.STRouteStep;

/**
 * Service de publication ( envoi des informations d'un dossier à publier à la dila )
 * 
 * @author arolin
 * 
 */
public interface WsSpeService {

    /**
     * Tente d'envoyer une demande de publication au webservice pour le dossier.
     * En cas d'échec, un mail est envoyé à l'administrateur technique et aux utilisateurs du poste
     * 
     * @param dossier
     * @param webservice
     * @param session
     * @param typePublication
     * @param runningStep
     * @throws ClientException
     */
    void envoiPremiereDemandePublicationPourDila(Dossier dossier, String webservice, CoreSession session, String typePublication, STRouteStep runningStep) throws ClientException;

    /**
     * Envoie un mail pour la demande de publication suivante.
     * 
     * @param dossier
     * @param session
     * @param typePublication
     * @param runningStep
     * @throws ClientException
     */
    void envoiDemandePublicationSuivante(Dossier dossier, CoreSession session, String typePublication, STRouteStep runningStep) throws ClientException;

    /**
     * Rééssaie d'envoyer la demande de publication en echec à la dila
     * 
     * @param session
     * @throws ClientException
     */
    long retryPremiereDemandePublication(CoreSession session, final BatchLoggerModel batchLogger, long nbError) throws ClientException;
}
