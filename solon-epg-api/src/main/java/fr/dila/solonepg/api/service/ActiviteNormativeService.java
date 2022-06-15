package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.Habilitation;
import fr.dila.solonepg.api.cases.LigneProgrammation;
import fr.dila.solonepg.api.cases.LoiDeRatification;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.Ordonnance;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.cases.typescomplexe.LigneProgrammationHabilitation;
import fr.dila.solonepg.api.dto.DecretApplicationDTO;
import fr.dila.solonepg.api.dto.DecretDTO;
import fr.dila.solonepg.api.dto.HabilitationDTO;
import fr.dila.solonepg.api.dto.LoiDeRatificationDTO;
import fr.dila.solonepg.api.dto.MesureApplicativeDTO;
import fr.dila.solonepg.api.dto.OrdonnanceDTO;
import fr.dila.solonepg.api.dto.OrdonnanceHabilitationDTO;
import fr.dila.solonepg.api.dto.TexteMaitreLoiDTO;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface pour la gestion de l'activité nominatives
 *
 * @author asatre
 *
 */
public interface ActiviteNormativeService extends Serializable {
    /**
     * Ajout d'un dossier dans l'activite normative
     *
     * @param numeroNorDossier
     * @param type
     * @param session
     */
    void addDossierToTableauMaitre(String numeroNorDossier, String type, CoreSession session);

    /**
     * Sauvegarde du texte maître
     *
     * @param dossier
     * @param session
     * @return
     */
    DocumentModel saveTexteMaitre(TexteMaitre texteMaitre, CoreSession session);

    /**
     * Suppression du dossier de la liste en cours
     *
     * @param doc
     * @param type
     * @param session
     */
    void removeActiviteNormativeFrom(DocumentModel doc, String type, CoreSession session);

    /**
     * Extraction de la date de promulgation de la loi a partir du titre de la loi<br/>
     * Loi n°2010-43 du 23 janvier 2010 relative... retourne 23/01/2010
     *
     * @param doc
     * @return
     */
    Calendar extractDateFromTitre(String titre);

    /**
     * verifie si un dossier est un decret, sinon {@link ActiviteNormativeException}
     *
     * @param numeroNorDossier
     * @param session
     * @return le dossier
     */
    Dossier checkIsDecretFromNumeroNOR(String numeroNorDossier, CoreSession session, String... prefetch);

    /**
     * retourne l'url parametre vers legifrance a partir d'un numeroNor
     *
     * @param jorfLegifrance
     * @return
     */
    String createLienJORFLegifrance(String numeroNor);

    /**
     * Sauvegarde des decrets
     *
     * @param idCurrentMesure
     * @param activiteNormative
     * @param listDecret
     * @param idCurrentTexteMaitre
     * @param session
     * @return
     */
    void saveDecrets(
        String idCurrentMesure,
        List<DecretDTO> listDecret,
        String idCurrentTexteMaitre,
        CoreSession session
    );

    /**
     * Sauvegarde d'une mesure
     *
     * @param activiteNormative
     * @param listMesure
     * @param session
     */
    void saveMesure(ActiviteNormative activiteNormative, List<MesureApplicativeDTO> listMesure, CoreSession session);

    /**
     * Sauvegarde d'une ordonnance
     *
     * @param ordonnanceDTO
     * @param texteMaitre
     * @param session
     * @return
     */
    TexteMaitre saveOrdonnance(OrdonnanceDTO ordonnanceDTO, TexteMaitre texteMaitre, CoreSession session);

    /**
     * Fetch mesure
     *
     * @param listIds
     * @param session
     * @return
     */
    List<MesureApplicative> fetchMesure(List<String> listIds, CoreSession session);

    /**
     * fetch decret
     *
     * @param listIds
     * @param session
     * @return
     */
    List<Decret> fetchDecrets(List<String> listIds, CoreSession session);

    /**
     * Rattache un decret et sa mesure a sa loi
     *
     * @param dossierDecret
     * @param session
     */
    void attachDecretToLoi(Dossier dossierDecret, CoreSession session);

    /**
     * Rattache un decret et sa loi à une ordonnance
     *
     * @param dossierOdonnance
     * @param session
     */
    void attachDecretToOrdonnance(Dossier dossierOdonnance, CoreSession session);

    /**
     * construit les stats des mesures pour la fiche signaletique
     *
     * @param session
     * @param activiteNormative
     * @return
     * @throws
     */
    Map<String, Long> buildMesuresForFicheSignaletique(CoreSession session, ActiviteNormative activiteNormative);

    /**
     * construit les stats des ministeres pour la fiche signaletique
     *
     * @param session
     * @param activiteNormative
     * @return
     */
    Map<String, String> buildMinistereForFicheSignaletique(CoreSession session, ActiviteNormative activiteNormative);

    /**
     * construit les stats du taux d'application pour la fiche signaletique
     *
     * @param session
     * @param activiteNormative
     * @return
     */
    Integer buildTauxApplicationForFicheSignaletique(CoreSession session, ActiviteNormative activiteNormative);

    /**
     * construit les stats des delai pour la fiche sgnaletique
     *
     * @param session
     * @param adapter
     * @param texteMaitre
     * @return
     */
    Map<String, String> buildDelaiPublicationForFicheSignaletique(
        CoreSession session,
        ActiviteNormative adapter,
        TexteMaitre texteMaitre
    );

    /**
     * Sauvegarde des lignes du tableau de programmation dans l'activite normative
     *
     * @param lignesProgrammations
     * @param session
     * @param activiteNormative
     */
    void saveCurrentProgrammationLoi(
        List<LigneProgrammation> lignesProgrammations,
        ActiviteNormative activiteNormative,
        CoreSession session
    );

    /**
     * Suppression des lignes du tableau de programmation dans l'activite normative
     *
     * @param session
     * @param activiteNormative
     */
    void removeCurrentProgrammationLoi(ActiviteNormative activiteNormative, CoreSession session);

    /**
     * publication du tableau de suivi
     *
     * @param lignesProgrammations
     * @param adapter
     * @param documentManager
     */
    void publierTableauSuivi(
        List<LigneProgrammation> lignesProgrammations,
        ActiviteNormative session,
        CoreSession documentManager
    );

    /**
     * recherche de {@link ActiviteNormative} par numeroNor contenu dans {@link TexteMaitre}
     *
     * @param numeroNor
     * @param session
     * @return
     */
    ActiviteNormative findActiviteNormativeByNor(String numeroNor, CoreSession session);

    String findActiviteNormativeIdByNumero(CoreSession session, String numero);

    Map<String, ActiviteNormative> findActiviteNormativeByNors(
        final List<String> numeroNors,
        final CoreSession session
    );

    List<String> findActiviteNormativeIdsByNors(List<String> numeroNors, CoreSession session);

    /**
     * fetch des projets de loi de ratification
     *
     * @param loiRatificationIds
     * @param session
     * @return
     */
    List<LoiDeRatification> fetchLoiDeRatification(List<String> loiRatificationIds, CoreSession session);

    /**
     * Creation ou update des {@link LoiDeRatification}
     *
     * @param listLoiDeRatification
     * @param session
     * @param activiteNormative
     * @return
     */
    ActiviteNormative saveProjetsLoiDeRatification(
        List<LoiDeRatificationDTO> listLoiDeRatification,
        CoreSession session,
        ActiviteNormative activiteNormative
    );

    /**
     * Sauvegarde des decrets d'une loi de ratification
     *
     * @param idCurrentLoiDeRatification
     * @param listDecret
     * @param session
     */
    void saveDecretsOrdonnance(
        String idCurrentLoiDeRatification,
        List<DecretApplicationDTO> listDecret,
        CoreSession session
    );

    /**
     * get all dossier application loi
     *
     * @param session
     * @param legislatureEncours
     * @return
     */
    List<DocumentModel> getAllActiviteNormative(CoreSession session, String currentSection, boolean legislatureEncours);

    /**
     * get all dossier application loi
     *
     * @param session
     * @return
     */
    List<DocumentModel> getAllLoiHabilitationDossiers(CoreSession session, boolean curLegis);

    List<String> getAllAplicationMinisteresList(CoreSession session, String section, boolean curLegis);

    List<String> getAllHabilitationMinisteresList(CoreSession session, boolean curLegis);

    /**
     * met a jour les informations de publication correspondant au {@link RetourDila}
     *
     * @param retourDila
     * @param session
     */
    void setPublicationInfo(RetourDila retourDila, CoreSession session);

    /**
     * verifie que le numero interne est unique pour une loi ou une ordonnance (RG-LOI-MAI-12)
     *
     * @param texteMaitre
     * @param numeroInterne
     * @param session
     */
    void checkNumeroInterne(TexteMaitre texteMaitre, String numeroInterne, CoreSession session);

    /**
     * recherche de toutes le habilitation d'une loi
     *
     * @param idsHabilitation
     * @param session
     * @return
     */
    List<Habilitation> fetchListHabilitation(List<String> idsHabilitation, CoreSession session);

    /**
     * fetch ordonnance
     *
     * @param ordonnanceIds
     * @param session
     * @return
     */
    List<Ordonnance> fetchListOrdonnance(List<String> ordonnanceIds, CoreSession session);

    /**
     * Verifie si le dossier est une ordonnance 38C, si non {@link ActiviteNormativeException}, si oui retourne
     * l'ordonnance correspondante si elle existe
     *
     * @param numeroNorDossier
     * @param session
     * @return
     */
    Ordonnance checkIsOrdonnance38CFromNumeroNOR(String numeroNorDossier, CoreSession session);

    /**
     * attache une ordonnnance avec sa loi et son habilitation
     *
     * @param dossier
     * @param session
     */
    void attachOrdonnanceToLoiHabilitation(Dossier dossier, CoreSession session);

    /**
     * Sauvegarde des Habilitations
     *
     * @param listHabilitation
     * @param idDossier
     * @param session
     * @return
     */
    TexteMaitre saveHabilitation(List<HabilitationDTO> listHabilitation, String idDossier, CoreSession session);

    /**
     * sauvegarde des ordonnances d'une habilitation
     *
     * @param listOrdonnanceHabilitation
     * @param idHabilitation
     * @param idTexteMaitre
     * @param session
     * @return
     */
    List<String> saveOrdonnanceHabilitation(
        List<OrdonnanceHabilitationDTO> listOrdonnanceHabilitation,
        String idHabilitation,
        String idTexteMaitre,
        CoreSession session
    );

    /**
     * sauvegarde du tableau de programmation des ordonnances 38C
     *
     * @param lignesProgrammations
     * @param activiteNormative
     * @param session
     */
    void saveCurrentProgrammationHabilitation(
        List<LigneProgrammationHabilitation> lignesProgrammations,
        ActiviteNormative activiteNormative,
        CoreSession session
    );

    /**
     * suppression du tableau de programmation des ordonnances 38C
     *
     * @param activiteNormative
     * @param session
     */
    void removeCurrentProgrammationHabilitation(ActiviteNormative activiteNormative, CoreSession session);

    /**
     * Publication du tableau de suivi des ordonnances 38C
     *
     * @param lignesProgrammations
     * @param activiteNormative
     * @param session
     */
    void publierTableauSuiviHab(
        List<LigneProgrammationHabilitation> lignesProgrammations,
        ActiviteNormative activiteNormative,
        CoreSession session
    );

    /**
     * Verifi si le nor correspond bine a une loi
     *
     * @param numeroNor
     */
    Dossier checkIsLoi(String numeroNor, CoreSession session);

    /**
     * true si le currentUser a locké le {@link TexteMaitre}
     *
     * @param texteMaitre
     * @param session
     * @return
     */
    Boolean isTexteMaitreLockByCurrentUser(TexteMaitre texteMaitre, CoreSession session);

    /**
     * Lock du {@link TexteMaitre}
     *
     * @param texteMaitre
     * @param session
     */
    TexteMaitre lockTexteMaitre(TexteMaitre texteMaitre, CoreSession session);

    /**
     * Unlock du {@link TexteMaitre}
     *
     * @param texteMaitre
     * @param session
     */
    TexteMaitre unlockTexteMaitre(TexteMaitre texteMaitre, CoreSession session);

    /**
     * Ajout d'un traite dans le tableau maitre des traités et accords
     *
     * @param titre
     * @param dateSignature
     * @param publication
     * @param session
     */
    void addTraiteToTableauMaitre(String titre, Date dateSignature, Boolean publication, CoreSession session);

    /**
     * Recherche d'une activite normative par son numeroNor, creation de celle-ci si elle n'existe pas
     *
     * @param session
     * @param numeroNor
     * @return
     */
    ActiviteNormative findOrCreateActiviteNormativeByNor(CoreSession session, String numeroNor);

    TexteMaitre addLoiToTableauMaitreReprise(TexteMaitreLoiDTO texteMaitreLoiDTO, CoreSession session);

    MesureApplicative saveMesureReprise(
        String idActiviteNormative,
        MesureApplicativeDTO mesureApplicativeDTO,
        CoreSession session
    );

    void addDecretToMesureReprise(String texteMaitreLoiId, String mesureId, DecretDTO decretDTO, CoreSession session);

    /**
     * get le path du répertoire qui va contenir le stat publie de l'activite normative
     *
     * @return
     */
    String getPathDirANStatistiquesPublie();

    /**
     * genrer AN html repartion ministeriel pour la page de suivi AN
     *
     * @param session
     * @param activiteNormative
     * @param legislatureEnCours
     */
    void generateANRepartitionMinistereHtml(
        CoreSession session,
        ActiviteNormative activiteNormative,
        boolean legislatureEnCours
    );

    /**
     *
     * @param session
     * @param curLegis
     *            : si il s'agit d'une publication pour la législature courante ou non
     */
    void updateLoiListePubliee(CoreSession session, boolean curLegis);

    /**
     * @param curLegis
     *            : si il s'agit d'une publication pour la législature courante ou non
     * @param session
     */
    void updateHabilitationListePubliee(CoreSession session, boolean curLegis);

    List<DocumentModel> findTexteMaitreByMinisterePilote(String id, CoreSession session);

    /**
     * Trouve l'activité normative par numéro, si elle existe, renvoie null dans le cas contraire
     *
     * @param session
     *            la session utilisateur
     * @param numero
     *            le numéro de l'activité normative à rechercher
     * @return le document model de l'activité normative
     */
    DocumentModel findActiviteNormativeDocByNumero(CoreSession session, String numero);

    /**
     * create Ordonnance pour la Reprise
     *
     * @param ordonnanceDTO
     * @param session
     * @return
     */
    Ordonnance createOrdonnanceReprise(OrdonnanceDTO ordonnanceDTO, CoreSession session);

    Ordonnance saveOrdonnanceHabilitationReprise(
        OrdonnanceHabilitationDTO ordonnanceHabilitationDTO,
        String article,
        String idDossier,
        CoreSession session,
        boolean ratifie
    );

    ActiviteNormative saveProjetsLoiDeRatificationReprise(
        LoiDeRatificationDTO loiDeRatificationDTO,
        CoreSession session,
        ActiviteNormative activiteNormative
    );

    /**
     * Update decret from feuille de route
     *
     * @param dossierDoc
     * @param session
     */
    void updateDecretFromFeuilleDeRoute(DocumentModel dossierDoc, CoreSession session, boolean fluxRetour);

    /**
     * Update loi from feuille de route
     *
     * @param dossierDoc
     * @param session
     */
    void updateLoiFromFeuilleDeRoute(DocumentModel dossierDoc, CoreSession session, boolean fluxRetour);

    /**
     * Publier le tableau de suivi pour la page d'acceuil
     *
     * @param currentDocument
     * @param documentManager
     * @param masquerApplique
     * @param fromBatch
     * @param legislatureEnCours
     */
    void publierTableauSuiviHTML(
        DocumentModel currentDocument,
        CoreSession documentManager,
        boolean masquerApplique,
        boolean fromBatch,
        boolean legislatureEnCours
    );

    /**
     * Indique si la fonctionnalité de publication des bilans semestriels vers la BDJ
     * est activée.
     *
     * @return
     */
    boolean isPublicationBilanSemestrielsBdjActivated(CoreSession session);

    /**
     * Permet de récupérer le path pour les statistiques publiées pour la législature précédente
     *
     * @param documentManager
     * @return
     */
    String getPathDirANStatistiquesLegisPrecPublie(CoreSession documentManager);

    /**
     * Vérifie la validation des décrets pour une mesure d'application donnée. (application des lois)
     *
     * @param session
     * @param mesure
     * @return faux si la mesure contient au moins 1 décret non validé, vrai sinon
     */
    boolean checkDecretsValidationForMesure(CoreSession session, MesureApplicative mesure);

    /**
     * Vérifie la validation des ordonnances pour une mesure d'habilitation. (suivi des habilitations)
     *
     * @param session
     * @param habilitation
     * @return faux si la mesure contient au moins 1 ordonnance non validée, vrai sinon
     */
    boolean checkOrdonnancesValidationForHabilitation(CoreSession session, Habilitation habilitation);

    /**
     * Vérifie la validation des décrets pour une ordonnance (ratification des ordonnances)
     *
     * @param session
     * @param ordonnance
     * @return
     */
    boolean checkDecretsValidationForOrdonnance(CoreSession session, Ordonnance ordonnance);

    void lockCurrentProgrammationHabilitation(ActiviteNormative activiteNormative, CoreSession session);

    void unlockCurrentProgrammationHabilitation(ActiviteNormative activiteNormative, CoreSession session);

    /**
     * get all dossier application ordonnances
     *
     * @param session
     * @param legislatureEncours
     * @return
     */
    List<DocumentModel> getAllAplicationOrdonnanceDossiers(final CoreSession session, boolean legislatureEnCours);

    /**
     *
     * @param session
     * @param curLegis
     *            : si il s'agit d'une publication pour la législature courante ou non
     */
    void updateOrdonnancesListePubliee(final CoreSession session, boolean curLegis);

    void publierBilanSemestrielLoiBDJ(
        CoreSession session,
        String legislatureEnCours,
        Date DatePromulBorneInf,
        Date DatePromulBorneSup,
        Date DatePubliBorneInf,
        Date DatePubliBorneSup
    );

    void publierBilanSemestrielOrdonnanceBDJ(
        CoreSession session,
        String legislatureEnCours,
        Date DatePromulBorneInf,
        Date DatePromulBorneSup,
        Date DatePubliBorneInf,
        Date DatePubliBorneSup
    );

    String getLegislatureParam(CoreSession session, boolean legislatureEnCours);

    void validerDecrets(String idCurrentMesure, String idCurrentTexteMaitre, CoreSession session);

    String getLegislatureEnCoursOuPrec(CoreSession session, boolean legislatureEnCours);
}
