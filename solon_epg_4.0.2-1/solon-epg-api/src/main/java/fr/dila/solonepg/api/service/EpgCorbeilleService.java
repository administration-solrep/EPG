package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.st.api.dossier.STDossier.DossierState;
import fr.dila.st.api.service.STCorbeilleService;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Service qui permet de gérer les corbeilles utilisateur / postes, c'est à dire la recherche de dossiers via leur
 * distribution (DossierLink).
 *
 * @author jtremeaux
 */
public interface EpgCorbeilleService extends STCorbeilleService {
    /**
     *
     * @param documentManager
     * @param dossierState
     * @param mailboxIds
     * @return la requete suivant un etat {@link DossierState}
     */
    String getInfocentreQuery(CoreSession documentManager, DossierState dossierState, Set<String> mailboxIds);

    /**
     * Retourne le CaseLink correspondant à l'étape "Pour initialisation" de la feuille de route du dossier en
     * paramètre.
     *
     * @param session
     *            Session
     * @param dossierDoc
     *            Dossier
     * @return CaseLink de l'étape "pour initialisation"
     */
    DocumentModel getCaseLinkPourInitialisation(CoreSession session, DocumentModel dossierDoc);

    /**
     *
     * Trouver les dossiers link à partir d'une liste d'identificants de dossier
     *
     * @param session
     * @param dossierIds
     * @return
     */
    List<DossierLink> findDossierLinks(CoreSession session, List<String> dossierIds);

    /**
     * Vérifie s'il existe une étape "pour avis ce" en cours (unrestricted)
     *
     */
    boolean existsPourAvisCEStep(CoreSession session, String dossierId);

    /**
     * Récupère le documentModel du dossier link de l'étape "pour avis ce" si elle existe, null sinon (unrestricted)
     *
     */
    DocumentModel getPourAvisCeDossierLinkDoc(CoreSession session, String dossierId);

    /**
     * Récupère la date de début d'étape présente dans le dossierLink
     * @param session
     * @param dossierId
     * @param stepType
     * @return
     */
    Calendar getStartDateForRunningStep(CoreSession session, String dossierId, String stepType);
}
