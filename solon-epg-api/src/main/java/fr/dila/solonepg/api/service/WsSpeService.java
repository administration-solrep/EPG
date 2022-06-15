package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.st.api.event.batch.BatchLoggerModel;
import org.nuxeo.ecm.core.api.CoreSession;

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
     */
    void envoiPremiereDemandePublicationPourDila(
        Dossier dossier,
        String webservice,
        CoreSession session,
        String typePublication,
        SSRouteStep runningStep
    );

    /**
     * Envoie un mail pour la demande de publication suivante.
     *
     * @param dossier
     * @param session
     * @param typePublication
     * @param runningStep
     */
    void envoiDemandePublicationSuivante(
        Dossier dossier,
        CoreSession session,
        String typePublication,
        SSRouteStep runningStep
    );

    /**
     * Rééssaie d'envoyer la demande de publication en echec à la dila
     *
     * @param session
     */
    long retryPremiereDemandePublication(CoreSession session, final BatchLoggerModel batchLogger, long nbError);
}
