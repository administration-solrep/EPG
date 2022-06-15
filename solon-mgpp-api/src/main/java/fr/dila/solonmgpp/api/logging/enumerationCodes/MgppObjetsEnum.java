package fr.dila.solonmgpp.api.logging.enumerationCodes;

import fr.dila.st.api.logging.enumerationCodes.STCodes;

/**
 * Énumération de l'objet des actions <br>
 * Décompte sur 3 chiffres, le premier (5) indique qu'il s'agit d'un objet du MGPP <br>
 * <br>
 * 501 : Fiche loi <br>
 * 502 : Représentant OEP <br>
 * 503 : Représentant AVI <br>
 * 504 : Fichier SOLON MGPP <br>
 * 505 : Activité parlementaire <br>
 * 506 : Semaine parlementaire <br>
 * 507 : représentant dépôt de rapport <br>
 * 508 : dto <br>
 * l 510 : MESSAGE <br>
 * 512 : EVENT TYPE<br>
 * 513 : NOR<br>
 * 514 : NAVETTE<br>
 * 515 :CALENDAR<br>
 * 516 :FOLDER BUILDER<br>
 * 517 : CONTAINER BUILDER 518 : EVENT BUILDER 520 : INSTITUTION<br>
 * 521 : Requêteur MGPP<br>
 * 522 Notification<br>
 * 523 PIECE JOINTE BUILDER<br>
 * 524 Add Oep<br>
 * 525 Widget Generator<br>
 * 526 Déclaration de politique générale<br>
 * 527 Déclaration sur un sujet déterminé<br>
 * 528 Article 28-3C<br>
 * 529 Autres documents transmis aux assemblées <br>
 * 530 : Représentant AUD (audition) <br>
 * 531 : date d'arrivée de la notification <br>
 * 532 : Table de référence EPP <br>
 * 533 : Courrier <br>
 * 534 : Table MGPP_INFO_CORBEILLE <br>
 * 535 : Fiche présentation <br>
 * 599 : WS EPP <br>
 *
 *
 * @author admin
 */
public enum MgppObjetsEnum implements STCodes {
    /**
     * 501 : Fiche loi
     */
    FICHE_LOI(501, "Fiche loi"),
    /**
     * 502 : Représentant OEP
     */
    REPRESENTANT_OEP(502, "Représentant OEP"),
    /**
     * 503 : Représentant AVI (nominé)
     */
    REPRESENTANT_AVI(503, "Représentant AVI (nominé)"),
    /**
     * 504 : Fichier SOLON MGPP
     */
    FILE_MGPP(504, "Fichier SOLON MGPP"),
    /**
     * 505 : Activité parlementaire
     */
    ACT_PARLEMENTAIRE(505, "Activité parlementaire"),
    /**
     * 506 : Semaine parlementaire
     */
    SEM_PARLEMENTAIRE(506, "Semaine parlementaire"),
    /**
     * 507 : Représentant Dépôt de rapport
     */
    REPRESENTANT_DR(507, "Dépôt de rapport"),
    /**
     * 508 : DTO
     */
    DTO(508, "DTO"),

    /**
     * 510 : MESSAGE
     */
    MESSAGE(510, "Message"),

    /**
     * 513 NOR
     */
    NOR(513, "Nor"),

    /**
     * 514 NAVETTE
     */
    NAVETTE(514, "Navette"),

    /**
     * 515 CALENDAR
     */
    CALENDAR(515, "calendrier"),

    /**
     * 516 FOLDER BUILDER
     */
    FOLDER_BUILDER(516, "DossierBuilder"),

    /**
     * 517 CONTAINER BUILDER
     */
    CONTAINER_BUILDER(517, "FolderBuilder"),

    /**
     * 518 EVENT BUILDER
     */
    EVENT_BUILDER(518, "EventBuilder"),

    /**
     * 520 INSTITUTION
     */
    INSTITUTION(520, "Institution"),

    /**
     * 521 Requêteur MGPP
     */
    MGPP_REQUESTOR(521, "Requêteur de MGPP"),

    /**
     * 522 Notification
     */
    NOTIFICATION(522, "Notification"),

    /**
     * 523 EVENT BUILDER
     */
    PIECE_JOINTE_BUILDER(523, "PieceJointeBuilder"),

    /**
     * 524 ADD OEP
     */
    ADD_OEP(524, "Add Oep"),

    /**
     * 525 Widget Generator
     */
    WIDGET_GENERATOR(525, "Widget Generator"),

    /**
     * 526 : Déclaration de politique générale
     */
    DECLARATION_POLITIQUE_GENERALE(526, "Déclaration de politique générale"),

    /**
     * 527 : Déclaration sur un sujet déterminé
     */
    DECLARATION_SUJET_DETERMINE(527, "Déclaration sur un sujet déterminé"),

    /**
     * 528 : Article 28-3C
     */
    DECLARATION_ARTICLE_23_8C(528, "Article 28-3C"),

    /**
     * 529 : Autres documents transmis aux assemblées
     */
    DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES(529, "Autres documents transmis aux assemblées"),

    /**
     * 530 : Représentant AUD (audition)
     */
    REPRESENTANT_AUD(530, "Représentant AUD"),

    /**
     * 531 : Date d'arrivée de la notification
     */
    DATE_NOTIFICATION(531, "Date d'arrivée notification"),

    /**
     * 532 : Table de référence EPP
     */
    TDR_EPP(532, "Table de référence EPP"),

    /**
     * 533 : Courrier
     */
    COURRIER(533, "Courrier"),

    /**
     * 534 : Table MGPP_INFO_CORBEILLE
     */
    TABLE_MGPP_INFO_CORBEILLE(534, "Table MGPP_INFO_CORBEILLE"),

    /**
     * 535 : Fiche Présentation
     */
    FICHE_PRESENTATION(535, "Fiche de présentation"),

    /**
     * 599 : WS EPP
     */
    WS_EPP(599, "WebServices EPP");

    /* **** (Ne pas oublier de tenir à jour la documentation en lien avec le code) **** */

    private int codeNumber;
    private String codeText;

    MgppObjetsEnum(int codeNumber, String codeText) {
        this.codeNumber = codeNumber;
        this.codeText = codeText;
    }

    @Override
    public int getCodeNumber() {
        return this.codeNumber;
    }

    @Override
    public String getCodeText() {
        return this.codeText;
    }

    @Override
    public String getCodeNumberStr() {
        return String.valueOf(this.codeNumber);
    }
}
