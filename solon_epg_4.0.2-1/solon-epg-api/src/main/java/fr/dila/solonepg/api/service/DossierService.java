package fr.dila.solonepg.api.service;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.recherche.EpgDossierListingDTO;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Service permettant de gérer le cycle de vie du dossier SOLON EPG.
 *
 * @author jtremeaux
 */
public interface DossierService extends Serializable {
    /**
     * Crée un dossier Solon EPG, les documents parapheur + fond de dossier associés, ainsi que l'objet Case associé au
     * dossier. Lève un évènement pour démarrer la feuille de route par défaut.
     *
     * @param session
     *            Session
     * @param dossier
     *            Dossier
     * @param posteId
     *            Identifiant technique du poste de la première étape de feuille de route
     * @param norDossierCopieFdr
     *            Numero Nor du dossier pour copier la feuille de route (facultatif)
     * @parma userMailBox MailBox
     * @return Dossier créé
     */
    Dossier createDossier(
        CoreSession session,
        DocumentModel dossierDoc,
        String posteId,
        Mailbox userMailBox,
        String norDossierCopieFdr
    );

    /**
     * Crée un dossier Solon EPG pour les webservices, les documents parapheur + fond de dossier associés, ainsi que
     * l'objet Case associé au dossier. Methode destinée au webservice car l'utilisateur webservices peut créer un
     * dossier dans n'importe quel ministère. Initialise les droits du dossier Lève un évènement pour démarrer la
     * feuille de route par défaut.
     *
     * @param session
     *            Session
     * @param dossier
     *            Dossier
     * @param posteId
     *            Identifiant technique du poste de la première étape de feuille de route
     * @parma userMailBox MailBox
     * @return Dossier créé
     */
    Dossier createDossierWs(CoreSession session, DocumentModel dossierDoc, String posteId, Mailbox userMailBox);

    /**
     * Crée un dossier Solon EPG, et les documents parapheur + fond de dossier associés. Lève un évènement pour démarrer
     * la feuille de route par défaut.
     *
     * @param session
     *            Session
     * @param dossier
     *            Dossier
     * @param posteId
     *            Identifiant technique du poste de la première étape de feuille de route
     * @param norDossierCopieFdr
     *            Numero Nor du dossier pour copier la feuille de route (facultatif)
     * @return Dossier créé
     */
    Dossier createDossier(CoreSession session, Dossier dossier, String posteId, String norDossierCopieFdr);

    /**
     * Initialise les ACL du dossier lors de sa création en Unrestricted.
     *
     * @param session
     *            Session
     * @param dossierDocList
     *            liste de Dossier
     */
    void initDossierCreationAclUnrestricted(CoreSession session, final List<Dossier> dossierDocList);

    /**
     * Initialise les ACL indexation du dossier lors du changement de cycle de vie dans une session Unrestricted.
     *
     * @param session
     *            Session
     * @param dossiersList
     *            Dossier
     */
    void initDossierIndexationAclUnrestricted(CoreSession session, List<Dossier> dossiersList);

    /**
     * Renvoie vrai si le dossier est reattribuable.
     *
     * @param dossier
     *            Dossier
     * @return vrai si le dossier est reattribuable.
     */
    Boolean isReattribuable(Dossier dossier);

    /**
     * Renvoie vrai si le dossier est transferable.
     *
     * @param dossier
     *            Dossier
     * @return vrai si le dossier est transferable.
     */
    Boolean isTransferable(Dossier dossier);

    /**
     * Transfert de dossiers clos à un autre ministere.
     *
     * @param docList
     *            liste des dossiers à transferer
     * @param ministereAttache
     *            id du ministere
     * @param directionAttachee
     *            id de la direction
     * @param documentManager
     *            {@link CoreSession}
     */
    void transfertDossiersUnrestricted(
        List<DocumentModel> docList,
        String ministereAttache,
        String directionAttachee,
        CoreSession session
    );

    /**
     * Met à jour le Ministère et la Direction de rattachement du dossier, ainsi que les droits du dossier.
     *
     * @param ministere
     * @param direction
     * @param ministereOnly
     * @param dossier
     * @param session
     */
    void updateDossierWhenTransfert(
        String ministere,
        String direction,
        boolean ministereOnly,
        Dossier dossier,
        CoreSession session
    );

    /**
     * Reattribution de dossiers à un autre ministere.
     *
     * @param docList
     *            liste des dossiers à transferer
     * @param ministere
     *            id du ministere
     * @param direction
     *            id de la direction
     * @param documentManager
     *            {@link CoreSession}
     */
    void reattribuerDossiersUnrestricted(
        List<DocumentModel> docList,
        String ministere,
        String directio,
        CoreSession session
    );

    /**
     * Met à jour le Ministère et la Direction de rattachement du dossier, son nor, son titre ainsi que les droits du
     * dossier. Met à jour uniquement le ministère si
     * <p>
     * ministereOnly
     * </p>
     * est vrai
     *
     * @param ministere
     * @param direction
     * @param ministereOnly
     * @param norMinistere
     * @param norDirection
     * @param norService
     * @param dossier
     */
    void updateDossierWhenReattribution(
        final String ministere,
        final String direction,
        final boolean ministereOnly,
        boolean reattributionNor,
        final String norMinistere,
        final String norDirection,
        final NORService norService,
        final Dossier dossier,
        final CoreSession session
    );

    /**
     * @param updatePubConjointes
     *            Si true, les publications conjointes sont mises à jour directement (cas de la réattribution). Si false
     *            (cas de la migration), elles seront mises à jour dans un second temps, lorsque tous les dossiers
     *            auront été migrés.
     * @return true si le dossier a des publications conjointes.
     */
    boolean updateDossierWhenReattribution(
        String ministere,
        String direction,
        boolean ministereOnly,
        boolean reattributionNor,
        String norMinistere,
        String norDirection,
        NORService norService,
        Dossier dossier,
        CoreSession session,
        boolean checkNorUnicity,
        ActiviteNormative activiteNormative
    );

    /**
     * @param doUpdate
     *            true si on doit réellement réaliser l'update, false si la méthode ne sert qu'à détecteur.
     * @return
     */
    boolean checkDossierPublicationsConjointesWhenReattribution(Dossier dossier, final CoreSession session);

    void updateDossierWhenReattributionJetonCE(CoreSession session, List<DocumentModel> dossierToReattribuer);

    /**
     * Marque un dossier comme candidat à la suppression.
     *
     * @param session
     * @param dossierDoc
     * @throws Exception
     */
    void candidateDossierToSuppression(CoreSession session, final DocumentModel dossierDoc) throws Exception;

    /**
     * Abandonner un dossier.
     *
     * @param session
     *            Session
     * @param dossierDoc
     *            le dossier a abndonner
     */
    void abandonDossier(CoreSession session, DocumentModel dossierDoc);

    /**
     * Retire le dossier de la liste de suppression.
     *
     * @param session
     *            Session
     * @param dossierDoc
     *            le document a retirer
     */
    void retirerDossierFromSuppressionListUnrestricted(CoreSession session, DocumentModel dossierDoc);

    /**
     * Valider la transmission du dossier vers la liste de supression.
     *
     * @param session
     *            Session
     * @param dossierDoc
     *            le document a transmettre
     */
    void validerTransmissionToSuppressionListUnrestricted(CoreSession session, DocumentModel dossierDoc);

    /**
     * Retirer le dossier de la liste d'abandon des dossier.
     *
     * @param session
     *            Session
     * @param dossierDoc
     *            le dossier a retirer
     */
    void retirerAbandonDossier(CoreSession session, DocumentModel dossierDoc);

    void restartAbandonDossier(CoreSession session, final DocumentModel dossierDoc);

    /**
     * Retire le dossier de la base d'archivage intermediaire et le maintenir dans la base d eprodcution pour une
     * determinee saisi
     *
     * @param session
     *            Session
     * @param dossierDoc
     *            le dossier a retirer
     * @param dureeManitienDossierEnProduction
     *            duree du Manitien du Dossier dans la base Production
     */
    void retirerDossierFromListCandidatsArchivageIntermediaireUnrestricted(
        CoreSession session,
        DocumentModel dossierDoc,
        int dureeManitienDossierEnProduction
    );

    /**
     * Verse le dossier dans la base d'archivage intermediaire.
     *
     * @param session
     *            Session
     * @param dossierDoc
     *            le dossier a verser
     */
    void verserDossierDansBaseArchivageIntermediaireUnrestricted(CoreSession session, DocumentModel dossierDoc);

    /**
     * Retire le caractère mesure nominative du dossier.
     *
     * @param session
     *            Session
     * @param dossierDoc
     *            Dossier
     */
    void annulerMesureNominativeUnrestricted(CoreSession session, DocumentModel dossierDoc);

    /**
     * Ajoute le caractère mesure nominative du dossier.
     *
     * @param session
     *            Session
     * @param dossierDoc
     *            Dossier
     */
    void ajouterMesureNominativeUnrestricted(CoreSession session, DocumentModel dossierDoc);

    void abandonDossierPourReprise(CoreSession session, DocumentModel dossierDoc);

    /**
     * Met à jour le dossier suite à un changement de type d'acte(UC-SOL-DOS-26)
     *
     * @param dossier
     *            dossier courant à modifier
     * @param newTypeActe
     *            nouveeu type d'acte
     * @param session
     *            session
     * @return vrai si le type d'acte a changé
     */
    Boolean updateDossierWhenTypeActeUpdated(DocumentModel dossier, CoreSession session);

    /**
     * Publie le dossier.
     *
     * @param dossier
     *            dossier courant à modifier
     * @param newTypeActe
     *            nouveeu type d'acte
     * @param session
     *            session
     * @return vrai si le type d'acte a changé
     */
    void publierDossier(Dossier dossier, CoreSession session, boolean validateCurrentSteps);

    /**
     * Retourne la liste des dossiers ayant pour ministère <b>nodeId</b> ou pour direction <b>nodeId</b> si
     * <b>isDirection</b> est vrai.
     *
     * @param session
     * @param nodeId
     * @param isDirection
     * @return Retourne la liste des dossiers ayant pour ministère <b>nodeId</b> ou pour direction <b>nodeId</b> si
     * 			<b>isDirection</b> est vrai.
     */
    List<DocumentModel> getDossierRattacheToMinistereOrDirection(
        CoreSession session,
        String nodeId,
        boolean isDirection
    );

    /**
     * Recherche de dossier par numeroNOR.
     *
     * @param session
     * @param numeroNOR
     * @return
     */
    Dossier findDossierFromNumeroNOR(CoreSession session, String numeroNOR);

    /**
     * recherche de dossier par idDossier (ie rattachement avec le MGPP)
     *
     * @param session
     * @param idDossier
     * @return
     */
    Dossier findDossierFromIdDossier(CoreSession session, String idDossier);

    /**
     * recherche des dossiers par id des Dossier (ie rattachement avec le MGPP)
     *
     * @param session
     * @param ids
     * @return
     */
    List<DocumentModel> findDossiersFromIdsDossier(CoreSession session, List<String> ids);

    /**
     * recherche des dossier ids par direction resp.
     */
    List<String> findDossierIdsFromDirectionResp(CoreSession session, OrganigrammeNode node);

    List<String> findDossierIdsFromDirectionAttache(CoreSession session, OrganigrammeNode node);

    /**
     * recherche des dossier ids par ministere resp.
     */
    List<String> findDossierIdsFromMinistereResp(CoreSession session, OrganigrammeNode node);

    List<String> findDossierIdsFromMinistereAttache(CoreSession session, OrganigrammeNode node);

    /**
     *
     * @param session
     * @param idDossier
     * @return
     */
    DocumentModel findFicheLoiDocumentFromMGPP(CoreSession session, String idDossier);

    /**
     * Clos un dossier : supprime les étapes à venir, valide les étapes en cours, passe le statut à clos
     *
     * @param session
     * @param dossier
     */
    void cloreDossierUnrestricted(CoreSession session, Dossier dossier);

    /**
     * Met à jour les publications conjointes d'un dossier lors de son changement de nor de oldNor vers newNor,
     * potentiellement dans le cadre d'un batch (migration de gouvernement)
     *
     * @param dossier
     *            Dossier
     * @param session
     *            CoreSession Nuxeo
     * @param oldNor
     *            ancien nor du dossier
     * @param newNor
     *            nouveau nor du dossier
     * @param norReattributionsPubConj
     *            Lot de migrations de Nor (migration de gouvernement), null si reattribution simple.
     */
    void updatePublicationsConjointes(
        Dossier dossier,
        CoreSession session,
        String oldNor,
        String newNor,
        Map<String, String> norReattributionsPubConj
    );

    /**
     * Utilisé lors de la suppression d'un dossier inscrit le nor et l'id dans la table id_nor_dossiers_supprimes
     *
     * @param nor
     * @param id
     * @return
     */
    void saveNorIdDossierSupprime(String nor, String id);

    /**
     * Va chercher le nor du dossier dans la table id_nor_dossiers_supprimes grace à son id
     */
    String getNorDossierSupprime(String id);

    /**
     * Va chercher les ids des dossiers dans la table id_nor_dossiers_supprimes grace à leur nor
     */
    List<String> getIdsDossiersSupprimes(String nor);

    /**
     * Récupère la liste des dossiers rattachés à un lot d'extraction de
     * dossiers (ADAMANT), dans la limite d'un nombre maximal fourni en
     * paramètre (s'il est nul on retourne la liste complète).
     *
     * @param idExtractionLot
     *            id technique du lot
     * @param limit
     *            nombre maximal de dossiers à retourner.
     * @return une liste de DocumentModel
     */
    List<DocumentModel> findDossiersFromIdExtractionLot(CoreSession session, Integer idExtractionLot, Integer limit);

    /**
     * Retourne true si le dossier est issue de la reprise de solon v1
     *
     * @param dossier
     */
    boolean isDossierReprise(Dossier dossier);

    /**
     * Archive les dossiers links associés au dossier.
     * @param session
     * @param dossierDoc
     */
    void archiveDossiersLinks(final CoreSession session, final DocumentModel dossierDoc);

    /**
     * Retourne true si le dossier possède l'étape "Pour avis CE" dans sa feuille de route
     * @param session
     * @param dossier
     */
    boolean hasEtapeAvisCE(CoreSession session, Dossier dossier);

    /**
     * Retourne true si le dossier possède l'étape "Pour avis CE" à l'état "Avis rendu" dans sa feuille de route
     * @param session
     * @param dossier
     */
    boolean hasEtapeAvisCEValide(CoreSession session, Dossier dossier);

    /**
     * recherche de dossier par idDossier (ie rattachement avec le MGPP)
     *
     * @param session
     * @param idDossier
     * @return
     */
    DocumentModel findDossierDocFromIdDossier(CoreSession session, String idDossier);

    void removeEditingFlagOnFiles(CoreSession session, DocumentModel dossierDoc);

    /**
     * Passe à l'état "done" l'étape en cours d'une feuille de route et supprime les routes à venir
     * Journalise les différentes actions dans le journal du dossier
     * @param session
     * @param dossier
     */
    void deleteFeuilleRouteStepToCome(CoreSession session, Dossier dossier);

    long countAllDossiersSuppressionConsultation(CoreSession session);

    List<EpgDossierListingDTO> getAllDossiersSuppressionConsultation(CoreSession session);

    long countAllDossiersSuppressionSuivi(CoreSession session);

    List<EpgDossierListingDTO> getAllDossiersSuppressionSuivi(CoreSession session);

    long countAllDossiersAbandon(CoreSession session);

    List<EpgDossierListingDTO> getAllDossiersAbandon(CoreSession session);

    void deletedDossiers(CoreSession session, List<String> idDossiers);

    void removeDossierReferences(CoreSession session, String id);
}
