package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Service de document routing de solon epg
 *
 */
public interface EpgDocumentRoutingService extends fr.dila.ss.api.service.DocumentRoutingService {
    String isNextPostsCompatibleWithNextStep(String ministereRequisId, List<DocumentModel> steps, String typeAvisCe);

    String isNextPostCompatibleWithNextStep(String ministereRequisId, SSRouteStep nextstepDocument, String typeAvisCe);

    String isNextPostCompatibleWithNextStep(CoreSession session, SSRouteStep nextstepDocument);

    String isNextPostsCompatibleWithNextStep(CoreSession session, List<DocumentModel> steps);

    String isNextStepCompatibleWithTypeActe(SSRouteStep nextstepDocument, CoreSession documentManager, String idDoc);

    /**
     * Ajoute les étapes d'épreuvage à la suite de l'étape passé en paramètre
     *
     * @param documentManager
     * @param postePublicationId
     * @param posteDanId
     */
    void createStepsPourEpreuve(
        CoreSession session,
        String posteBdcId,
        String postePublicationId,
        String posteDanId,
        DocumentModel routeDoc,
        String sourceDocName,
        String parentPath
    );

    /** Valide le squelette de feuille de route. */
    SolonEpgFeuilleRoute validateSquelette(SSFeuilleRoute squelette, CoreSession session);

    /**
     * Dévalide un squelette de feuille de route (passe son lifeCycle de "validated" à "draft").
     *
     */
    SolonEpgFeuilleRoute invalidateSquelette(final FeuilleRoute squelette, CoreSession session);

    /**
     * Indique si le document model est un squelette de feuille de route (en opposition à un modèle notamment)
     * @param docModel
     * @return
     */
    boolean isSqueletteFeuilleRoute(final DocumentModel docModel);

    /**
     * Crée un modele à partir d'un squelette
     * @param model
     * @param docIds
     * @param session
     * @return
     */
    SSFeuilleRoute createNewModelInstanceFromSquelette(
        final SolonEpgFeuilleRoute squelette,
        final DocumentModel location,
        final String ministereId,
        final String bdcId,
        final String cdmId,
        final String scdmId,
        final String cpmId,
        CoreSession session
    );

    /**
     * Copie la feuille de route d'un dossier vers un nouveau
     *
     * @param session
     *            Session
     * @param dossierDoc
     *            Document modèle du dossier cible
     * @param routeDocToCopy
     *            Feuille de route a copier
     * @return Nouveau modèle de feuille de route
     */
    SSFeuilleRoute duplicateFeuilleRoute(CoreSession session, DocumentModel dossierDoc, DocumentModel routeDocToCopy);

    /**
     * Crée un nouveau RouteStep en copiant les valeurs de celui passé en parametre.
     * Les valeurs copiées sont celles visibles dans l'écran de création d'une étape de feuille de route.
     * (type, distributionMailboxId, AutomaticValidation, ObligatoireSGG, ObligatoireMinistere, DeadLine)
     * @param session
     * @param docToCopy
     * @param parentDocument
     * @return
     */
    DocumentModel duplicateRouteStep(CoreSession session, DocumentModel docToCopy);

    /**
     * Vérifie si l'utilisateur peut valider l'étape en cours
     * @param ssPrincipal
     * @param dossierLink
     * @return
     */
    boolean isRoutingTaskTypeValiderAllowed(SSPrincipal ssPrincipal, DossierLink dossierLink);

    SolonEpgFeuilleRoute duplicateSquelette(final CoreSession session, final DocumentModel squeletteToCopyDoc);
}
