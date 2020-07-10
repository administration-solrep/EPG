package fr.dila.solonmgpp.api.constant;

/**
 * Constante des fonctions unitaires pour le MGPP
 * 
 * @author admin
 * 
 */
public final class SolonMgppBaseFunctionConstant {

    private SolonMgppBaseFunctionConstant() {
        // private default constructor
    }

    /**
     * Accepter une rectification ou une annulation d'une communication
     */
    public static final String ACCEPTER_EVENEMENT = "AccepterEvenement";

    /**
     * Rejeter une rectification ou une annulation d'une communication
     */
    public static final String REJETER_EVENEMENT = "RejeterEvenement";

    /**
     * Supprimer une version
     */
    public static final String SUPPRIMER_VERSION = "SupprimerVersion";

    /**
     * Lever/Poser Alerte
     */
    public static final String CREER_ALERTE = "CreerAlerte";

    /**
     * Visualisation des corbeilles et modification du contenu
     */
    public static final String CORBEILLE_MGPP_UPDATER = "CorbeilleMGPPUpdater";

    /**
     * Visualisation des corbeilles
     */
    public static final String AR_EVENEMENT = "AccuserReception";
    
    /**
     * Visualisation informations spécifiques mgpp
     */
    public static final String DROIT_VUE_MGPP = "DroitVueMgpp";

}
