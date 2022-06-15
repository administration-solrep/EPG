package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.ss.api.service.DossierDistributionService;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Service de distribution des dossiers de SOLON EPG.
 *
 * @author jtremeaux
 */
public interface EpgDossierDistributionService extends DossierDistributionService {
    /**
     * Donne un avis favorable avec correction pour l'étape de feuille de route lié au dossier du dossier link.
     *
     * @param session
     *            Session
     * @param dossierDoc
     *            Dossier
     * @param dossierLinkDoc
     *            dossierLink à valider
     * @param userId
     *            identifiantUtilisateur
     */
    void validerEtapeCorrection(CoreSession session, DocumentModel dossierDoc, DocumentModel dossierLinkDoc);

    /**
     * Exécute l'étape avec "non concerné" pour l'étape de feuille de route lié au dossier du dossier link. Ajout
     * automatiquement une étape "retour pour modification" après l'étape validée.
     *
     * @param session
     *            Session
     * @param dossierDoc
     *            Dossier
     * @param dossierLinkDoc
     *            dossierLink à valider
     */
    void validerEtapeNonConcerneAjoutEtape(CoreSession session, DocumentModel dossierDoc, DocumentModel dossierLinkDoc);

    /**
     * Exécute l'étape avec "retour pour modification" pour l'étape de feuille de route lié au dossier du dossier link.
     *
     * @param session
     *            Session
     * @param dossierDoc
     *            Dossier
     * @param dossierLinkDoc
     *            dossierLink à valider
     */
    void validerEtapeRetourModification(CoreSession session, DocumentModel dossierDoc, DocumentModel dossierLinkDoc);

    /**
     * Exécute l'étape avec "retour pour modification" pour l'étape de feuille de route lié au dossier du dossier link.
     * Ajout automatiquement une étape "retour pour modification" et une copie de l'étape validée après l'étape validée.
     *
     * @param session
     *            Session
     * @param dossierDoc
     *            Dossier
     * @param dossierLinkDoc
     *            dossierLink à valider
     * @param posteID
     *            poste à indiquer dans la nouvelle étape
     */
    void validerEtapeRetourModificationAjoutEtape(
        CoreSession session,
        DocumentModel dossierDoc,
        DocumentModel dossierLinkDoc,
        String posteID
    );

    /**
     * Refus d'une étape : gère le cas des validationStatus différents dans le cas de l'avis CE cf RG-INS-FDR-20
     */
    void validerEtapeRefus(
        CoreSession session,
        DocumentModel dossierDoc,
        DocumentModel dossierLinkDoc,
        String validationStatus
    );

    /**
     * Validation d'un étape : gère le cas des validationStatus différents dans le cas de l'avis CE cf RG-INS-FDR-20
     */
    void validerEtape(
        CoreSession session,
        DocumentModel dossierDoc,
        DocumentModel dossierLinkDoc,
        String validationStatus
    );

    /**
     * Renseigne les données du DossierLink après sa création.
     *
     * @param session
     *            Session
     * @param dossierLinkDoc
     *            Document DossierLink
     */
    void setDossierLinksFields(CoreSession session, DocumentModel dossierLinkDoc);

    /**
     * Recherche et démarre la feuille de route associée au dossier.
     *
     * @param session
     *            Session
     * @param dossier
     *            Dossier
     * @param posteId
     *            Identifiant technique du poste de la première étape
     * @return Instance de feuille de route
     */
    FeuilleRoute startDefaultRoute(CoreSession session, Dossier dossier, String posteId);

    /**
     * Permet de lancer le dossier (approuve le CaseLink de la première étape).
     *
     * @param session
     *            Session
     * @param dossierDoc
     *            Dossier
     * @param dossierLinkDoc
     *            ActionableCaseLink de la première étape
     */
    void lancerDossier(CoreSession session, DocumentModel dossierDoc, DocumentModel dossierLinkDoc);

    /**
     * Recherche le poste à attribuer à la première étape de feuille de route. Si l'utilisateur a un seul poste : on
     * choisit celui-ci. Sinon, recherche le poste correspondant aux 4 premières lettres du NOR. Sinon, retourne null.
     * L'utilisateur devra choisir manuellement le poste.
     *
     * @param posteIdSet
     *            Postes de l'utilisateur
     * @param norMinistere
     *            Lettres de NOR ministère (ex. "ECO")
     * @param norDirection
     *            Lettres de NOR direction (ex. "A")
     * @return Poste trouvé ou null
     */
    String getPosteForNor(Set<String> posteIdSet, String norMinistere, String norDirection);

    /**
     * Initialise l'ACL du DossierLink à sa création.
     */
    void initDossierLinkAcl(CoreSession session, DocumentModel dossierLinkDoc, DocumentModel dossierDoc);

    /**
     * Action "NOR Attribué" (passe du dossier à l'état publié.
     */
    void norAttribue(CoreSession session, DocumentModel dossierDoc);

    /**
     * Mise a jour des champs de denormalisation des {@link DossierLink} lié au {@link Dossier}
     */
    void updateDossierLinksFields(CoreSession session, Dossier dossier);

    void validerAutomatiquementEtapePourPublication(CoreSession session);

    /**
     * Vérifie la complétude du bordereau et la cohérence des données avec les étapes suivantes
     */
    void checkDossierBeforeValidateStep(CoreSession session, Dossier dossier, List<DocumentModel> nextStepsDoc);

    void checkDossierBeforeValidateStepWithAddingStep(
        CoreSession session,
        Dossier dossier,
        List<DocumentModel> nextStepsDoc
    );

    /**
     * Modifie les acls suite aux modifications de ministère de rattachement des dossiers
     */
    void updateDossierLinksACLs(final CoreSession session, String idMinistere);

    /**
     * FEV507 : Identifie et renvoie la ou les étapes à contrôler parmi les étapes précédant l'étape courante.
     *
     * On remonte les étapes à partir de l'étape passée en paramètre jusqu'à la
     * première occurrence de :
     * <li>- Une étape pour avis affectée à un poste identifié comme « Chargé de
     * mission SGG » et/ou « Conseiller du Premier ministre »</li>
     * <li>- une étape // avec une étape Pour avis dedans, elle-même affectée à un
     * poste identifié comme « Chargé de mission SGG » et/ou « Conseiller du Premier
     * ministre »</li>
     *
     * @param session
     * @param stepDoc DocumentModel associée à l'étape en cours
     * @return
     */
    Collection<DocumentModel> findChargesDeMissionSteps(final CoreSession session, DocumentModel stepDoc);

    void avisRectificatif(CoreSession session, DocumentModel dossierDocList);

    /**
     * Recherche et démarre la feuille de route associée au dossier.
     *
     * @param session
     *            Session
     * @param dossier
     *            Dossier
     * @param posteId
     *            Identifiant technique du poste de la première étape
     * @param norDossierCopieFdr
     *            Numero NOR du dossier pour copier la feuille de route
     * @return Instance de feuille de route
     */
    FeuilleRoute startRouteCopieFromDossier(
        CoreSession session,
        DocumentModel dossier,
        String posteId,
        String norDossierCopieFdr
    );

    /**
     * Termine la feuille de route : Supprime toute les étapes à venir et valide l'étape en cours
     */
    void terminateFeuilleRoute(final CoreSession session, final DocumentModel dossierDoc);

    /**
     * Si la feuille de route est annulée, supprime tous les DossierLink et et
     * annule toutes les étapes non-annulées de la FDR
     *
     * @param session
     *            CoreSession
     * @param doc
     *            le dossier ou la FDR
     * @return
     */
    boolean processCanceledRoute(final CoreSession session, final DocumentModel doc);

    /**
     * Redémarre la feuille de route d'un dossier dont la feuille de route a été terminée précédemment.
     *
     * @param session Session
     * @param dossierDoc Dossier
     * @param nextStatut le statut dans lequel mettre le dossier après redémarrage.
     */
    void restartDossier(CoreSession session, DocumentModel dossierDoc, String nextStatut);

    /**
     * Soft delete dossier links
     */
    void removeDossierLinks(CoreSession session, Collection<String> dossierIds);
}
