package fr.dila.solonepg.api.constant;

/**
 * Constantes liées aux bulletins oficiels et à ses éléments (schéma,type,métadonnées).
 *
 * @author asatre
 *
 */
public final class SolonEpgVecteurPublicationConstants {
    public static final String VECTEUR_PUBLICATION_SCHEMA = "vecteur_publication";
    public static final String VECTEUR_PUBLICATION_SCHEMA_PREFIX = "vp";
    public static final String VECTEUR_PUBLICATION_PATH = "/case-management/vecteur-publication-root";

    public static final String VECTEUR_PUBLICATION_DOCUMENT_TYPE = "VecteurPublication";

    public static final String VECTEUR_PUBLICATION_DEBUT = "vpDebut";
    public static final String VECTEUR_PUBLICATION_DEBUT_XPATH =
        VECTEUR_PUBLICATION_SCHEMA + ":" + VECTEUR_PUBLICATION_DEBUT;
    public static final String VECTEUR_PUBLICATION_INTITULE = "vpIntitule";
    public static final String VECTEUR_PUBLICATION_INTITULE_XPATH =
        VECTEUR_PUBLICATION_SCHEMA + ":" + VECTEUR_PUBLICATION_INTITULE;
    public static final String VECTEUR_PUBLICATION_FIN = "vpFin";
    public static final String VECTEUR_PUBLICATION_FIN_XPATH =
        VECTEUR_PUBLICATION_SCHEMA + ":" + VECTEUR_PUBLICATION_FIN;
    public static final String VECTEUR_PUBLICATION_ID = "vpId";
    public static final String VECTEUR_PUBLICATION_POS = "vpPos";

    // Constant utility class
    private SolonEpgVecteurPublicationConstants() {}
}
