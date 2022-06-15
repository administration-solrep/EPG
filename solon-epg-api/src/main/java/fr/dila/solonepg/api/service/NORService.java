package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.cases.Dossier;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface NORService extends Serializable {
    /**
     * Création d'un nouveau NOR. NOR = Code ministère (3 lettres) + Code direction (1 lettre) + Année (2 chiffres) +
     * Compteur (5 chiffres) + Code acte (1 lettre) Incrémente la séquence du numéro de NOR.
     *
     * @param norActe
     *            Lettre de NOR de l'acte (ex. "L")
     * @param norMinistere
     *            Lettres de NOR du ministère (ex. "ECO")
     * @param norDirection
     *            Lettre de NOR de la direction (ex. "A")
     * @return Nouveau NOR (ex. "ECOX9800017L")
     */
    String createNOR(String norActe, String norMinistere, String norDirection);

    /**
     * Création du NOR d'un dossier de type Rectificatif à partir du dossier rectifié.
     *
     * @param norDossier
     *            NOR du dossier à rectifier (ex. "ECOX9800017L")
     * @return Nouveau NOR (ex. "ECOX9800017Z")
     */
    String createRectificatifNOR(Dossier dossierRectifie);

    /**
     * Renvoie la lettre associé au nombre de rectificatif créé pour un dossier.
     *
     * @param nbRectificatif
     * @return String la lettre associé au nombre de rectificatif créé pour un dossier.
     */
    String getLettreFromNbRectificatif(Long nbRectificatif);

    /**
     * Récupération d'un dossier à partir de son NOR
     *
     * @param session
     * @param nor
     * @return dossier
     */
    DocumentModel getDossierFromNOR(CoreSession session, String nor, String... schemas);

    /**
     * Récupération de l'id technique d'un dossier à partir de son NOR
     */
    String getDossierIdFromNOR(CoreSession session, String nor);

    /**
     * Récupération d'un rectificatif à partir du NOR du dossier Initial et du numéro du rectificatif (1er, 2ème ou
     * 3ème)
     *
     * @param session
     * @param nor
     * @return dossier
     */
    Dossier getRectificatifFromNORAndNumRect(CoreSession session, String nor, Long nbRectificatif);

    /**
     * Récupération d'un dossier à partir de son NOR
     *
     * @param session
     * @param nor
     * @return dossier
     */
    Dossier findDossierFromNOR(CoreSession session, String nor, String... schemas);

    /**
     * Récupération d'un dossier à partir de son NOR en respectant les droit d'accès
     *
     * @param session
     * @param nor
     * @return dossier
     */
    Dossier findDossierFromNORWithACL(CoreSession session, String nor);

    /**
     * Réattribution du NOR : renvoi le nor modifié cf UC-SOL-SEL-18
     *
     * @param dossierReattribue
     *            dossier à rectifier (ex. "ECOX9800017L")
     * @param norMinistere
     *            norMinistere CCO
     * @return Nouveau NOR (ex. "CCOX9800017L")
     */
    String reattributionMinistereNOR(Dossier dossierReattribue, String norMinistere);

    /**
     * Réattribution du NOR : renvoi le nor modifié cf UC-SOL-SEL-18
     *
     * @param dossierReattribue
     *            dossier à rectifier (ex. "ECOX9800017L")
     * @param norMinistere
     *            norMinistere CCO
     * @param norDirection
     *            norDirection D
     * @return Nouveau NOR (ex. "CCOD9800017L")
     */
    String reattributionDirectionNOR(Dossier dossierReattribue, String norMinistere, String norDirection);

    /**
     *
     * @param coreSession
     * @param nor
     * @return
     */
    List<String> findDossierIdsFromNOR(CoreSession coreSession, String nor);

    /**
     * @param session
     *            The CoreSession
     * @return set of available NOR
     */
    Set<String> retrieveAvailableNorList(final CoreSession session);

    boolean checkNorUnicity(final CoreSession session, String nor);

    /**
     * Récupération du dossier via son numéro ISA
     *
     * @param session
     * @param nor
     * @return
     */
    DocumentModel getDossierFromISA(final CoreSession session, final String nor, String... schemas);

    /**
     * Vérification de la structure d'un NOR.
     *
     * @param nor
     * @return
     */
    boolean isStructNorValide(String nor);

    /**
     * Vérfication de la structure d'un numéro ISA.
     *
     * @param numeroISA
     * @return
     */
    boolean isStructNumeroISAValid(String numeroISA);

    /**
     * Récupération d'un dossier à partir de son NOR en respectant les droit d'accès
     *
     * @param session
     * @param nor
     * @return dossier
     */
    DocumentModel getDossierFromNORWithACL(CoreSession session, String nor);

    Optional<String> getNorFromDossierId(CoreSession session, String dossierId);
}
