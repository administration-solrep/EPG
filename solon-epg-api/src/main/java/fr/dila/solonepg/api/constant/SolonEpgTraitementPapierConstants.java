package fr.dila.solonepg.api.constant;

public final class SolonEpgTraitementPapierConstants {
    // Reference : Signataire

    /**
     * Reference : signataire PR
     */
    public static final String REFERENCE_SIGNATAIRE_PR = "1";
    /**
     * Reference : signataire PM
     */
    public static final String REFERENCE_SIGNATAIRE_PM = "2";
    /**
     * Reference : signataire SGG
     */
    public static final String REFERENCE_SIGNATAIRE_SGG = "3";
    /**
     * Reference : pas de signataire
     */
    public static final String REFERENCE_SIGNATAIRE_AUCUN = "4";

    //Communication Niveau de priorite

    /**
     * Communication Niveau de priorite urgent
     */
    public static final String COMMUNICATION_PRIORITE_URGENT = "1";

    /**
     * Communication Niveau de priorite TTU
     */
    public static final String COMMUNICATION_PRIORITE_TTU = "2";

    /**
     * Communication Niveau de priorite normal
     */
    public static final String COMMUNICATION_PRIORITE_NORMAL = "3";

    /**
     * table de reference : entrée cabinet PM
     */
    public static final String REFERENCE_TYPE_CABINET_PM = "CABINET_PM";

    /**
     * table de reference : entrée cabinet PM
     */
    public static final String REFERENCE_TYPE_CHARGES_MISSION = "CHARGES_MISSION";

    /**
     * table de reference : entrée signataires
     */
    public static final String REFERENCE_TYPE_SIGNATAIRE = "SIGNATAIRES";

    /**
     * table de reference : entrée destinataire SGG
     */
    public static final String REFERENCE_TYPE_SIGNATURE_SGG = "SIGNATURE_SGG";

    /**
     * table de reference : entrée  destinataire CPM
     */
    public static final String REFERENCE_TYPE_SIGNATURE_CPM = "SIGNATURE_CPM";

    /**
     * directeur pour communication
     */
    public static final String REFERENCE_TYPE_COMMUNICATION_DIRECTEUR = "COMMUNICATION_DIRECTEUR";

    // Communication : chaamp Objet du schema
    public static final String SCHEMA_COMMUNICATION_DESTINATAIRE_OBJET = "objetCommunication";
    // Communication : Message auto Objet
    public static final String COMMUNICATION_OBJET_AVIS = "Avis";
    public static final String COMMUNICATION_OBJET_AVIS_AVANT_PUBLIC = "Avis avant publication";
    public static final String COMMUNICATION_OBJET_AVIS_AVANT_PUBLIC_SIGN = "Avis avant signature et publication";
    public static final String COMMUNICATION_OBJET_AVIS_AVANT_SIGN = "Avis avant signature";

    /** constant utility class */
    private SolonEpgTraitementPapierConstants() {}
}
