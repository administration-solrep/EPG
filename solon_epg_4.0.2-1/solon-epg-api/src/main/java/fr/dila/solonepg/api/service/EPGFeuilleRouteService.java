package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.ss.api.service.SSFeuilleRouteService;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import java.util.Collection;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Service permettant d'effectuer des actions spécifiques sur les instances de feuille de route dans SOLON EPG.
 *
 * @author jtremeaux
 */
public interface EPGFeuilleRouteService extends SSFeuilleRouteService {
    /**
     * Modifie l'étape "Pour initialisation" à l'instanciation d'une feuille de route, afin de renseigner dynamiquement
     * le destinataire de cette étape.
     *
     * @param session
     *            Session
     * @param route
     *            Instance de feuille de route
     * @param posteId
     *            Identifiant technique du poste
     */
    void initEtapePourInitialisation(CoreSession session, FeuilleRoute route, String posteId);

    /**
     * Recherche de l'étape "pour initialisation" d'une feuille de route. L'étape doit exister et être définie de façon
     * unique.
     *
     * @param session
     *            Session
     * @param route
     *            Feuille de route
     * @return Étape "pour initialisation"
     */
    DocumentModel getEtapePourInitialisation(CoreSession session, FeuilleRoute route);

    /**
     *
     * @param session
     * @param dueDossierLinks
     */
    void validerAuromatiquementDossier(CoreSession session, List<DocumentModel> dueDossierLinks);

    /**
     *
     * @param session
     * @param dossierDoc
     * @return
     */
    DocumentModel getEtapeInitialisationDossier(final CoreSession session, final DocumentModel dossierDoc);

    /**
     * Vérifie si la prochaine étape est compatible avec le type d'acte texte non publié
     *
     * @param session
     * @param feuilleRouteDocId
     *            la feuille de route
     * @param stepDoc
     *            l'étape à partir de laquelle chercher
     * @return vrai si l'étape suivante est compatible, faux sinon
     */
    boolean isNextStepCompatibleWithActeTxtNonPub(CoreSession session, String feuilleRouteDocId, DocumentModel stepDoc);

    /**
     * @return le nom du dernier chargé de mission ayant validé une étape "Pour avis"
     */
    String getLastChargeMission(final CoreSession session, final DocumentModel dossierDoc);

    /**
     * @return la dernière étape de la feuille de route
     */
    SolonEpgRouteStep getLastStep(CoreSession session, DocumentModel dossierDoc);

    /**
     * Récupère la liste des libellés des étapes en cours sous la forme 'Type étape - Poste'
     *
     * @param session
     * @param feuilleRouteId
     * @return
     */
    List<String> getEtapesEnCours(CoreSession session, String feuilleRouteId);

    /**
     * Renvoie la liste (potentiellement vide) des étapes de feuille de route
     * de squelette pour lesquelles le poste est affecté et qui sont actives
     * ou à venir
     *
     * @param session session
     * @posteId identifiant du poste
     * @return une collection vide ou remplie d'objet FeuilleRouteStep
     *         (DocumentModel).
     */
    Collection<DocumentModel> getRouteStepsInSqueletteFdrForPosteId(CoreSession session, String posteId);
}
