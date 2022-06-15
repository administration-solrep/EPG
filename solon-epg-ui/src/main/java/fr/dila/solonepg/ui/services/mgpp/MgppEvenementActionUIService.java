package fr.dila.solonepg.ui.services.mgpp;

import fr.dila.st.ui.th.bean.TransmettreParMelForm;
import fr.dila.st.ui.th.model.SpecificContext;

public interface MgppEvenementActionUIService {
    String creerEvenement(SpecificContext context);

    void rectifierEvenement(SpecificContext context);

    void completerEvenement(SpecificContext context);

    /**
     * Retourne un TransmettreParMelForm avec le type d'événement comme objet
     *
     * @param context
     * @return
     */
    TransmettreParMelForm getTransmettreParMelForm(SpecificContext context);

    /**
     * Envoi du message suite à l'action Transmettre par mél
     *
     * @param context
     */
    void transmettreParMelEnvoyer(SpecificContext context);

    /**
     * Supprimer l'événement
     *
     * @param context
     */
    void supprimerEvenement(SpecificContext context);

    /**
     * Annuler l'événement
     *
     * @param context
     */
    void annulerEvenement(SpecificContext context);

    /**
     * Accepter la version
     *
     * @param context
     */
    void accepterVersion(SpecificContext context);

    /**
     * Rejeter la version
     *
     * @param context
     */
    void rejeterVersion(SpecificContext context);

    /**
     * Accuser réception de la version
     *
     * @param context
     */
    void accuserReceptionVersion(SpecificContext context);

    void mettreEnAttente(SpecificContext context);

    /**
     * Abandonner la version
     *
     * @param context
     */
    void abandonnerVersion(SpecificContext context);

    /**
     * Suivre la transition en cours de traitement sur l'événement
     *
     * @param context
     */
    void enCoursTraitementEvenement(SpecificContext context);

    /**
     * Suivre la transition traiter sur l'événement
     *
     * @param context
     */
    void traiterEvenement(SpecificContext context);

    /**
     * Retourne le type d'alerte successive à l'événement courant
     *
     * @param context
     * @return
     */
    String getTypeAlerteSuccessive(SpecificContext context);

    /**
     * Publier directement un événement à l'état brouillon (sans modifier de données)
     *
     * @param context
     */
    void publierEvenement(SpecificContext context);

    void saveModifierEvenement(SpecificContext context);
}
