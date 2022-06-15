package fr.dila.solonepg.api.constant;

/**
 * Constantes pour le Drag And Drop de SolonEpg
 *
 * @author acleuet
 *
 */
public class SolonEpgDragAndDropConstants {
    // Messages d'erreur et de confirmation
    /**
     * Element et dossier non compatibles
     */
    public static final String DND_TYPES_NON_COMPATIBLES = "Cet élément n'est pas compatible avec ce dossier";

    /**
     * Erreur lors de la récupération de l'élément qu'on souhaite déplacer
     */
    public static final String ERREUR_RECUPERATION_ELEMENT = "Erreur lors de la récupération de l'élément";

    /**
     * Erreur lors de la récupération du dossier de destination
     */
    public static final String ERREUR_RECUPERATION_DOSSIER = "Erreur lors de la récupération du dossier de destination";

    /**
     * Erreur lors du déplacement - répertoire plein
     */
    public static final String ERREUR_TRANSFERT_REPERTOIRE_PLEIN =
        "Erreur lors du transfert du fichier. Le répertoire de destination est déjà plein";

    /**
     * Erreur lors du déplacement - type de fichier incorrect
     */
    public static final String ERREUR_TYPE_FICHIER = "Ce type de fichier n'est pas compatible avec ce répertoire";

    /**
     * Pas les droits sur ce répertoire
     */
    public static final String ERREUR_DROITS_REPERTOIRE = "Vous n'êtes pas autorisé à modifier ce répertoire";

    /**
     * Déjà un fichier avec le même nom dans le nouveau répertoire
     */
    public static final String ERREUR_NOM_FICHIER =
        "Le document ne peut pas être déplacé car il porte le même nom qu'un document existant ou en cours de suppression dans le répertoire de destination. Veuillez renommer le document pour le déplacer.";

    /**
     * Erreur générique
     */
    public static final String ERREUR_DND = "Une erreur s'est produite lors du déplacement du document";

    /**
     * Erreur même répertoire
     */
    public static final String ERREUR_REPERTOIRE_IDENTIQUE =
        "Le répertoire de provenance et de destination sont identiques. Aucune action effectuée";

    // Compatibilité entre le document et le container
    /**
     * Compatibilité error
     */
    public static final String DND_COMPATIBILITE_ERROR = "DndCompatibiliteError";

    /**
     * Compatibilite sans conversion
     */
    public static final String DND_COMPATIBILITE_SANS_CONVERSION = "DndCompatibiliteSansConversion";

    /**
     * Compatibilité avec conversion du FDD vers le Parapheur
     */
    public static final String DND_COMPATIBILITE_CONVERSION_FDD_VERS_PARAPHEUR =
        "DndCompatibiliteConversionFDDVersParapheur";

    /**
     * Compatibilité avec conversion du parapheur vers le FDD
     */
    public static final String DND_COMPATIBILITE_CONVERSION_PARAPHEUR_VERS_FDD =
        "DndCompatibiliteConversionParapheurVersFDD";
}
