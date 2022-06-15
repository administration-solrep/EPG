package fr.dila.solonmgpp.api.service;

import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Interface de gestion des types d'événements.
 *
 * @author asatre
 */
public interface EvenementTypeService {
    /**
     * Retourne la description d'un type d'événement.
     *
     * @param evenementType
     *            Type d'événement
     * @return Description du type d'événement
     */
    EvenementTypeDescriptor getEvenementType(String evenementType);

    /**
     * Détermine si un type d'événement permet de créer des dossiers.
     *
     * @param evenementType
     *            Type d'événement
     * @return Vrai si le type d'événement permet de créer des dossiers
     */
    boolean isTypeCreateur(String evenementType);

    /**
     * Détermine si un type d'événement peut succéder à un autre événement.
     *
     * @param evenementType
     *            Type d'événement
     * @return Vrai si le type d'événement peut succéder à un autre événement
     */
    boolean isTypeSuccessif(String evenementType);

    /**
     * Détermine si les versions de ce type d'événement nécessite un accusé de réception.
     *
     * @param evenementType
     *            Type d'événement
     * @return Vrai si les versions de ce type d'événement nécessite un accusé de réception
     */
    boolean isDemandeAr(String evenementType);

    /**
     * Détermine si l'émetteur est autorisé pour ce type d'événement.
     *
     * @param evenementType
     *            Type d'événement
     * @param emetteur
     *            Emetteur
     * @return Émetteur autorisé
     */
    boolean isEmetteurAutorise(String evenementType, String emetteur);

    /**
     * Détermine si le destinataire est autorisé pour ce type d'événement.
     *
     * @param evenementType
     *            Type d'événement
     * @param destinataire
     *            Destinataire
     * @return Destinataire autorisé
     */
    boolean isDestinataireAutorise(String evenementType, String destinataire);

    /**
     * Détermine si le destinataire en copie est obligatoire pour ce type d'événement.
     *
     * @param evenementType
     *            Type d'événement
     * @return Destinataire obligatoire
     */
    boolean isDestinataireCopieObligatoire(String evenementType);

    /**
     * Détermine si le destinataire en copie est autorisé pour ce type d'événement.
     *
     * @param evenementType
     *            Type d'événement
     * @param destinataireCopie
     *            Destinataire
     * @return Destinataire autorisé
     */
    boolean isDestinataireCopieAutorise(String evenementType, String destinataireCopie);

    /**
     * Détermine si un type de pièce jointe est obligatoire.
     *
     * @param evenementType
     *            Type d'événement
     * @param pieceJointeType
     *            Type de pièce jointe
     * @return Vrai si le type de pièce jointe est obligatoire
     */
    boolean isPieceJointeObligatoire(String evenementType, String pieceJointeType);

    /**
     * Détermine si un type d'événement peut être créé à l'état brouillon (pour initialisation).
     *
     * @param evenementType
     *            Type d'événement
     */
    boolean isCreerBrouillon(String evenementType);

    /**
     * Détermine si un type d'événement peut faire l'objet d'une complétion.
     *
     * @param evenementType
     *            Type d'événement
     */
    boolean isCompleter(String evenementType);

    /**
     * Détermine si un type d'événement peut faire l'objet d'une rectification.
     *
     * @param evenementType
     *            Type d'événement
     */
    boolean isRectifier(String evenementType);

    /**
     * Détermine si un type d'événement peut faire l'objet d'une annulation.
     *
     * @param evenementType
     *            Type d'événement
     */
    boolean isAnnuler(String evenementType);

    /**
     * Retourne la liste des événements créateur par onglet
     *
     * @param currentMainAction
     *
     * @return liste des événements créateur
     */
    List<EvenementTypeDescriptor> findEvenementTypeCreateur(String currentMenu);

    /**
     * retourne la liste des evenements successifs possibles a un typeEvenement pour le gouvernement qui sont dans la
     * même procédure et ne contenant pas la liste des evenementSuivants
     *
     * @param evenementType
     * @param evenementsSuivants
     * @return
     */
    List<EvenementTypeDescriptor> findEvenementTypeSuccessif(String evenementType, List<String> evenementsSuivants);

    /**
     * Retourne la liste des événements
     *
     * @return liste des événements
     */
    List<EvenementTypeDescriptor> findEvenementType();

    /**
     * Retourne la liste des événements d'une procédure
     *
     * @param procedure
     */
    List<EvenementTypeDescriptor> findEvenementTypeByProcedure(String procedure);

    List<FichePresentationOEP> getAllFichesPresentationOEP(CoreSession session);

    EvenementTypeDescriptor findEvenementTypeById(String evtId);

    List<String> findEvenementTypeNameByProcedure(String menu);
}
