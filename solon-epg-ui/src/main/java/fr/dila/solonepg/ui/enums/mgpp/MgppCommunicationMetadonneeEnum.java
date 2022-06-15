package fr.dila.solonepg.ui.enums.mgpp;

import fr.dila.solonepg.ui.helper.mgpp.MgppWidgetFactory;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.bean.WidgetDTO;
import fr.dila.st.ui.enums.WidgetTypeEnum;
import fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public enum MgppCommunicationMetadonneeEnum {
    /*
     * Attention : l'ordre des éléments de l'énumération est identique à l'ordre dans lequel
     * ils seront affichés (pour ceux qui sont viewable) !
     * @see /solon-mgpp-web/src/main/resources/OSGI-INF/layout/metadonnees-layout-contrib.xml
     */
    ID_EVENEMENT("idEvenement"),
    ID_EVENEMENT_PRECEDENT("idEvenementPrecedent", WidgetDisplayMode.IF_NOT_EMPTY),
    EVENEMENT_PARENT(CommunicationMetadonneeEnum.EVENEMENT_PARENT),
    DOSSIER(CommunicationMetadonneeEnum.DOSSIER),
    ID_DOSSIER(CommunicationMetadonneeEnum.ID_DOSSIER),
    EMETTEUR(CommunicationMetadonneeEnum.EMETTEUR),
    DESTINATAIRE(CommunicationMetadonneeEnum.DESTINATAIRE),
    DEST_COPIE(
        CommunicationMetadonneeEnum.DESTINATAIRE_COPIE,
        "copie",
        MgppWidgetTypeEnum.MULTIPLE_SELECT,
        WidgetDisplayMode.IF_NOT_EMPTY,
        null
    ),
    HORODATAGE(
        CommunicationMetadonneeEnum.HORODATAGE,
        date -> MgppWidgetFactory.createDateTextWidget(date, SolonDateConverter.DATETIME_SLASH_MINUTE_COLON)
    ),
    SENAT(CommunicationMetadonneeEnum.SENAT),
    NIVEAU_LECTURE(CommunicationMetadonneeEnum.NIVEAU_LECTURE, WidgetDisplayMode.WIDGET_SPECIFIC),
    DATE_AR(
        CommunicationMetadonneeEnum.DATE_AR,
        "dateAr",
        MgppWidgetTypeEnum.DATE,
        WidgetDisplayMode.IF_NOT_EMPTY,
        date -> MgppWidgetFactory.createDateTextWidget(date, SolonDateConverter.DATETIME_SLASH_MINUTE_COLON)
    ),
    OBJET(CommunicationMetadonneeEnum.OBJET),
    DATE_DISTRIBUTION_ELECTRONIQUE(
        CommunicationMetadonneeEnum.DATE_DISTRIBUTION_ELECTRONIQUE,
        "dateDistribution",
        MgppWidgetTypeEnum.DATE,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createDateTextWidget
    ),
    NATURE_RAPPORT(CommunicationMetadonneeEnum.NATURE_RAPPORT, MgppWidgetFactory::createNatureRapportTextWidget),
    DATE_LETTRE_PM(CommunicationMetadonneeEnum.DATE_LETTRE_PM),
    DATE_DEMANDE(CommunicationMetadonneeEnum.DATE_DEMANDE),
    DATE_CONGRES(CommunicationMetadonneeEnum.DATE_CONGRES),
    NOR(CommunicationMetadonneeEnum.NOR),
    NOR_LOI(CommunicationMetadonneeEnum.NOR_LOI),
    URL_DOSSIER_AN(CommunicationMetadonneeEnum.URL_DOSSIER_AN),
    URL_DOSSIER_SENAT(CommunicationMetadonneeEnum.URL_DOSSIER_SENAT),
    NATURE_LOI(CommunicationMetadonneeEnum.NATURE_LOI),
    TYPE_LOI(CommunicationMetadonneeEnum.TYPE_LOI, MgppWidgetFactory::createTypeLoiTextWidget),
    AUTEUR(CommunicationMetadonneeEnum.AUTEUR, MgppWidgetFactory::createIdentiteTextWidget),
    COAUTEUR(
        CommunicationMetadonneeEnum.COAUTEUR,
        "coAuteur",
        MgppWidgetTypeEnum.MULTIPLE_INPUT_TEXT,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createMultipleIdentiteTextWidget
    ),
    COSIGNATAIRE(
        CommunicationMetadonneeEnum.COSIGNATAIRE,
        "coSignataireCollectif",
        MgppWidgetTypeEnum.INPUT_TEXT,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createTextWidget
    ),
    RAPPORTEUR_LIST(
        CommunicationMetadonneeEnum.RAPPORTEUR_LIST,
        "rapporteur",
        MgppWidgetTypeEnum.MULTIPLE_INPUT_TEXT,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createMultipleIdentiteTextWidget
    ),
    TITRE(CommunicationMetadonneeEnum.TITRE),
    INTITULE(CommunicationMetadonneeEnum.INTITULE),
    INTITULE_DECRET(
        CommunicationMetadonneeEnum.INTITULE,
        "intituleDecret",
        MgppWidgetTypeEnum.INPUT_TEXT,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createTextWidget
    ),
    ECHEANCE(CommunicationMetadonneeEnum.ECHEANCE),
    RAPPORT_PARLEMENT(CommunicationMetadonneeEnum.RAPPORT_PARLEMENT),
    DATE(CommunicationMetadonneeEnum.DATE),
    MOTIF_IRRECEVABILITE(CommunicationMetadonneeEnum.MOTIF_IRRECEVABILITE),
    DATE_CMP(CommunicationMetadonneeEnum.DATE_CMP),
    DATE_DEPOT_RAPPORT(CommunicationMetadonneeEnum.DATE_DEPOT_RAPPORT),
    NUMERO_DEPOT_RAPPORT(CommunicationMetadonneeEnum.NUMERO_DEPOT_RAPPORT),
    DATE_DEPOT_TEXTE(CommunicationMetadonneeEnum.DATE_DEPOT_TEXTE),
    NUMERO_DEPOT_TEXTE(CommunicationMetadonneeEnum.NUMERO_DEPOT_TEXTE),
    DATE_DEPOT(
        CommunicationMetadonneeEnum.DATE_DEPOT_TEXTE,
        "dateDepot",
        MgppWidgetTypeEnum.DATE,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createDateTextWidget
    ),
    NUMERO_DEPOT(
        CommunicationMetadonneeEnum.NUMERO_DEPOT_TEXTE,
        "numeroDepot",
        MgppWidgetTypeEnum.INPUT_TEXT,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createTextWidget
    ),
    DATE_SAISINE(CommunicationMetadonneeEnum.DATE_SAISINE),
    DATE_RETRAIT(CommunicationMetadonneeEnum.DATE_RETRAIT),
    NATURE(CommunicationMetadonneeEnum.NATURE, WidgetDisplayMode.NEVER), // contient [VERSION_COURANTE], à afficher en msg d'info
    RESULTAT_CMP(
        CommunicationMetadonneeEnum.RESULTAT_CMP,
        "resultatCmp",
        MgppWidgetTypeEnum.SELECT,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createResultatCMPTextWidget
    ),
    COMMISSION_SAISIE(CommunicationMetadonneeEnum.COMMISSION_SAISIE, MgppWidgetFactory::createOrganismeTextWidget),
    ATTRIBUTION_COMMISSION(CommunicationMetadonneeEnum.ATTRIBUTION_COMMISSION),
    DATE_REFUS(CommunicationMetadonneeEnum.DATE_REFUS),
    LIBELLE_ANNEXE(
        CommunicationMetadonneeEnum.LIBELLE_ANNEXE,
        "libelleAnnexes",
        MgppWidgetTypeEnum.MULTIPLE_INPUT_TEXT,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createMultiplePropertyTextWidget
    ),
    DATE_ENGAGEMENT_PROCEDURE(
        CommunicationMetadonneeEnum.DATE_ENGAGEMENT_PROCEDURE,
        "dateEngagementProcedure",
        MgppWidgetTypeEnum.DATE,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createDateTextWidget
    ),
    DATE_REFUS_PROCEDURE_ENGAGEMENT_AN(CommunicationMetadonneeEnum.DATE_REFUS_PROCEDURE_ENGAGEMENT_AN),
    DATE_REFUS_PROCEDURE_ENGAGEMENT_SENAT(CommunicationMetadonneeEnum.DATE_REFUS_PROCEDURE_ENGAGEMENT_SENAT),
    DATE_REFUS_ENGAGEMENT_PROCEDURE(CommunicationMetadonneeEnum.DATE_REFUS_ENGAGEMENT_PROCEDURE),
    NUMERO_TEXTE_ADOPTE(CommunicationMetadonneeEnum.NUMERO_TEXTE_ADOPTE),
    DATE_ADOPTION(CommunicationMetadonneeEnum.DATE_ADOPTION),
    SORT_ADOPTION(CommunicationMetadonneeEnum.SORT_ADOPTION, MgppWidgetFactory::createSortAdoptionTextWidget),
    COMMISSION_SAISIE_AU_FOND(
        CommunicationMetadonneeEnum.COMMISSION_SAISIE_AU_FOND,
        MgppWidgetFactory::createOrganismeTextWidget
    ),
    COMMISSION_SAISIE_POUR_AVIS(
        CommunicationMetadonneeEnum.COMMISSION_SAISIE_POUR_AVIS,
        MgppWidgetFactory::createMultipleOrganismeTextWidget
    ),
    REDEPOT(CommunicationMetadonneeEnum.REDEPOT),
    POSITION_ALERTE(CommunicationMetadonneeEnum.POSITION_ALERTE, MgppWidgetFactory::createPositionAlerteTextWidget),
    ANNEE_RAPPORT(CommunicationMetadonneeEnum.ANNEE_RAPPORT),
    URL_BASE_LEGALE(CommunicationMetadonneeEnum.URL_BASE_LEGALE),
    IDENTIFIANT_METIER(CommunicationMetadonneeEnum.IDENTIFIANT_METIER),
    PARLEMENTAIRE_TITULAIRE_LIST(
        CommunicationMetadonneeEnum.PARLEMENTAIRE_TITULAIRE_LIST,
        "titulaires",
        MgppWidgetTypeEnum.MULTIPLE_INPUT_TEXT,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createMultipleIdentiteTextWidget
    ),
    PARLEMENTAIRE_SUPPLEANT_LIST(
        CommunicationMetadonneeEnum.PARLEMENTAIRE_SUPPLEANT_LIST,
        "suppleant",
        MgppWidgetTypeEnum.MULTIPLE_INPUT_TEXT,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createMultipleIdentiteTextWidget
    ),
    DATE_PROMULGATION(CommunicationMetadonneeEnum.DATE_PROMULGATION),
    DATE_PUBLICATION(CommunicationMetadonneeEnum.DATE_PUBLICATION),
    NUMERO_LOI(CommunicationMetadonneeEnum.NUMERO_LOI),
    TYPE_ACTE(CommunicationMetadonneeEnum.TYPE_ACTE, MgppWidgetFactory::createTypeActeEppTextWidget),
    DATE_ACTE(CommunicationMetadonneeEnum.DATE_ACTE),
    DATE_CONVOCATION(CommunicationMetadonneeEnum.DATE_CONVOCATION),
    DATE_DESIGNATION(CommunicationMetadonneeEnum.DATE_DESIGNATION),
    NUMERO_JO(CommunicationMetadonneeEnum.NUMERO_JO),
    PAGE_JO(CommunicationMetadonneeEnum.PAGE_JO),
    ANNEE_JO(CommunicationMetadonneeEnum.ANNEE_JO),
    DATE_JO(CommunicationMetadonneeEnum.DATE_JO),
    NUMERO_RUBRIQUE(CommunicationMetadonneeEnum.NUMERO_RUBRIQUE),
    URL_PUBLICATION(CommunicationMetadonneeEnum.URL_PUBLICATION),
    DATE_VOTE(CommunicationMetadonneeEnum.DATE_VOTE),
    SENS_AVIS(CommunicationMetadonneeEnum.SENS_AVIS, MgppWidgetFactory::createTypeSensAvisTextWidget),
    SENS_VOTE(
        CommunicationMetadonneeEnum.SENS_AVIS,
        "sensVote",
        MgppWidgetTypeEnum.SELECT,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createTypeSensAvisTextWidget
    ),
    SUFFRAGE_EXPRIME(
        CommunicationMetadonneeEnum.SUFFRAGE_EXPRIME,
        "nombreSuffrage",
        MgppWidgetTypeEnum.INPUT_TEXT,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createTextWidget
    ),
    BULLETIN_BLANC(CommunicationMetadonneeEnum.BULLETIN_BLANC),
    VOTE_POUR(CommunicationMetadonneeEnum.VOTE_POUR),
    VOTE_CONTRE(CommunicationMetadonneeEnum.VOTE_CONTRE),
    ABSTENTION(CommunicationMetadonneeEnum.ABSTENTION),
    COMMISSIONS(
        CommunicationMetadonneeEnum.COMMISSIONS,
        "commission",
        MgppWidgetTypeEnum.MULTIPLE_INPUT_TEXT,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createMultipleOrganismeTextWidget
    ),
    DATE_CADUCITE(CommunicationMetadonneeEnum.DATE_CADUCITE),
    DOSSIER_CIBLE(CommunicationMetadonneeEnum.DOSSIER_CIBLE),
    RECTIFICATIF(CommunicationMetadonneeEnum.RECTIFICATIF),
    DATE_PRESENTATION(CommunicationMetadonneeEnum.DATE_PRESENTATION),
    DATE_DECLARATION(CommunicationMetadonneeEnum.DATE_DECLARATION),
    ORGANISME(CommunicationMetadonneeEnum.ORGANISME, MgppWidgetFactory::createOrganismeTextWidget),
    DEMANDE_VOTE(CommunicationMetadonneeEnum.DEMANDE_VOTE),
    DATE_AUDITION(CommunicationMetadonneeEnum.DATE_AUDITION),
    GROUPE_PARLEMENTAIRE(
        CommunicationMetadonneeEnum.GROUPE_PARLEMENTAIRE,
        MgppWidgetFactory::createMultipleOrganismeTextWidget
    ),
    PERSONNE(CommunicationMetadonneeEnum.PERSONNE),
    FONCTION(CommunicationMetadonneeEnum.FONCTION),
    BASE_LEGALE(CommunicationMetadonneeEnum.BASE_LEGALE),
    DESCRIPTION(
        CommunicationMetadonneeEnum.DESCRIPTION,
        "commentaire",
        MgppWidgetTypeEnum.INPUT_TEXT,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createTextWidget
    ),
    DOSSIER_LEGISLATIF(
        CommunicationMetadonneeEnum.DOSSIER_LEGISLATIF,
        "identifiantsDossiersLegislatifsConcernes",
        MgppWidgetTypeEnum.MULTIPLE_INPUT_TEXT,
        WidgetDisplayMode.ALWAYS,
        MgppWidgetFactory::createMultiplePropertyTextWidget
    ),
    DATE_REFUS_ASSEMBLEE_1(CommunicationMetadonneeEnum.DATE_REFUS_ASSEMBLEE_1),
    DATE_CONFERENCE_ASSEMBLEE_2(CommunicationMetadonneeEnum.DATE_CONFERENCE_ASSEMBLEE_2),
    DECISION_PROC_ACC(
        CommunicationMetadonneeEnum.DECISION_PROC_ACC,
        MgppWidgetFactory::createDecisionProcAccTextWidget
    ),
    TITLE(CommunicationMetadonneeEnum.TITLE, WidgetDisplayMode.NEVER),
    DOSSIER_PRECEDENT(CommunicationMetadonneeEnum.DOSSIER_PRECEDENT, WidgetDisplayMode.NEVER),
    NIVEAU_LECTURE_NUMERO(CommunicationMetadonneeEnum.NIVEAU_LECTURE_NUMERO, WidgetDisplayMode.NEVER),
    RUBRIQUE(CommunicationMetadonneeEnum.RUBRIQUE, WidgetDisplayMode.NEVER),
    DATE_LIST(CommunicationMetadonneeEnum.DATE_LIST, WidgetDisplayMode.NEVER),
    COMMENTAIRE("commentaire"),
    DOC_RAPPORTS("RAPPORTS", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_AVIS("AVIS", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_TEXTE("TEXTE", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_TEXTES("TEXTES", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_ANNEXES("ANNEXES", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_EXPOSE_DES_MOTIFS("EXPOSE_DES_MOTIFS", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_ETUDE_IMPACT("ETUDE_IMPACT", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_DECRET_PRESENTATION("DECRET_PRESENTATION", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_AMPLIATION_DECRET("AMPLIATION_DECRET", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_LETTRE_PM("LETTRE_PM", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_DECRET_PRESIDENT_REPUBLIQUE("DECRET_PRESIDENT_REPUBLIQUE", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_ANNEXE("ANNEXE", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_LISTE_ANNEXES("LISTE_ANNEXES", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_TRAITE("TRAITE", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_ACCORD_INTERNATIONAL("ACCORD_INTERNATIONAL", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_RAPPORT("RAPPORT", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_LETTRE("LETTRE", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_LETTRE_SAISINE_CC("LETTRE_SAISINE_CC", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_TEXTE_ADOPTE("TEXTE_ADOPTE", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_TEXTE_TRANSMIS("TEXTE_TRANSMIS", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_TRAVAUX_PREPARATOIRES("TRAVAUX_PREPARATOIRES", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_COHERENT("COHERENT", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_COPIES3_LETTRES_TRANSMISSION("COPIES3_LETTRES_TRANSMISSION", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_PETITE_LOI("PETITE_LOI", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_TEXTE_MOTION("TEXTE_MOTION", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_LETTRE_PM_VERS_AN("LETTRE_PM_VERS_AN", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_LETTRE_PM_VERS_SENAT("LETTRE_PM_VERS_SENAT", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_LETTRE_CONJOINTE_PRESIDENTS("LETTRE_CONJOINTE_PRESIDENTS", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_CURRICULUM_VITAE("CURRICULUM_VITAE", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_AMPLIATION_DECRET_PRESIDENT_REPUBLIQUE("AMPLIATION_DECRET_PRESIDENT_REPUBLIQUE", MgppWidgetTypeEnum.FILE_MULTI),
    DOC_AUTRES("AUTRES", MgppWidgetTypeEnum.FILE_MULTI);

    /*  S'il y a un parent, ne pas donner de name (et inversement). */
    private final CommunicationMetadonneeEnum parent;
    private final String name;

    /** Lorsqu'il n'ya pas de parent/il y a un name, cette valeur doit être renseignée (s'il y a un parent, on retourne son widgetType) */
    private final MgppWidgetTypeEnum widgetType;

    /**
     * Indique dans quelles conditions ce widget est affiché dans MGPP.
     */
    private final WidgetDisplayMode displayMode;

    /**
     * Méthode de la factory à appliquer sur la value de départ pour générer le widget.
     * Si null, on utiliser la méthode portée par le widgetType.
     */
    private final Function<Object, WidgetDTO> factoryFunction;

    MgppCommunicationMetadonneeEnum(String name) {
        this(null, name, MgppWidgetTypeEnum.TEXT, WidgetDisplayMode.ALWAYS, null);
    }

    MgppCommunicationMetadonneeEnum(String name, WidgetDisplayMode displayMode) {
        this(null, name, MgppWidgetTypeEnum.TEXT, displayMode, null);
    }

    MgppCommunicationMetadonneeEnum(String name, MgppWidgetTypeEnum widgetType) {
        this(null, name, widgetType, WidgetDisplayMode.ALWAYS, null);
    }

    MgppCommunicationMetadonneeEnum(String name, MgppWidgetTypeEnum widgetType, WidgetDisplayMode displayMode) {
        this(null, name, widgetType, displayMode, null);
    }

    MgppCommunicationMetadonneeEnum(CommunicationMetadonneeEnum parent) {
        this(parent, null, null, WidgetDisplayMode.ALWAYS, null);
    }

    MgppCommunicationMetadonneeEnum(CommunicationMetadonneeEnum parent, WidgetDisplayMode displayMode) {
        this(parent, null, null, displayMode, null);
    }

    MgppCommunicationMetadonneeEnum(CommunicationMetadonneeEnum parent, Function<Object, WidgetDTO> factoryFunction) {
        this(parent, null, null, WidgetDisplayMode.ALWAYS, factoryFunction);
    }

    MgppCommunicationMetadonneeEnum(
        CommunicationMetadonneeEnum parent,
        String name,
        MgppWidgetTypeEnum widgetType,
        WidgetDisplayMode displayMode,
        Function<Object, WidgetDTO> factoryFunction
    ) {
        this.parent = parent;
        this.name = name;
        this.widgetType = widgetType;
        this.displayMode = displayMode;
        this.factoryFunction = factoryFunction;
    }

    /**
     * Retourne le {@link CommunicationMetadonneeEnum} à partir de son nom
     */
    public static MgppCommunicationMetadonneeEnum fromString(String name) {
        Predicate<MgppCommunicationMetadonneeEnum> finderPredicate = cme ->
            (cme.getParent() != null && cme.getParent() == CommunicationMetadonneeEnum.fromString(name)) ||
            (cme.getName() != null && cme.getName().equals(name));
        return Stream.of(values()).filter(finderPredicate).findFirst().orElse(null);
    }

    public Function<Object, WidgetDTO> getFactoryFunction() {
        return factoryFunction;
    }

    public CommunicationMetadonneeEnum getParent() {
        return parent;
    }

    public WidgetTypeEnum getEditWidgetType() {
        return parent.getEditWidgetType();
    }

    public WidgetDisplayMode getDisplayMode() {
        return displayMode;
    }

    public static boolean getFilterViewableMetadonnee(Entry<String, Serializable> entry) {
        MgppCommunicationMetadonneeEnum widget = fromString(entry.getKey());
        return (
            widget != null &&
            (
                WidgetDisplayMode.ALWAYS == widget.getDisplayMode() ||
                isDisplayIfNotEmptyWidgetViewable(entry, widget) ||
                isSpecificWidgetViewable(entry, widget)
            )
        );
    }

    public static boolean getFilterEditableMetadonnee(String name) {
        MgppCommunicationMetadonneeEnum widget = fromString(name);
        return (widget != null && (WidgetDisplayMode.NEVER != widget.getDisplayMode()));
    }

    private static boolean isDisplayIfNotEmptyWidgetViewable(
        Entry<String, Serializable> entry,
        MgppCommunicationMetadonneeEnum widget
    ) {
        return WidgetDisplayMode.IF_NOT_EMPTY == widget.getDisplayMode() && entry.getValue() != null;
    }

    private static boolean isSpecificWidgetViewable(
        Entry<String, Serializable> entry,
        MgppCommunicationMetadonneeEnum widget
    ) {
        return widget == NIVEAU_LECTURE && MgppWidgetFactory.isNiveauLectureDisplayable(entry.getValue());
    }

    public static Comparator<String> getViewableMetadonneeComparator() {
        return (key1, key2) -> {
            MgppCommunicationMetadonneeEnum cme1 = MgppCommunicationMetadonneeEnum.fromString(key1);
            MgppCommunicationMetadonneeEnum cme2 = MgppCommunicationMetadonneeEnum.fromString(key2);
            return Integer.compare(Arrays.binarySearch(values(), cme1), Arrays.binarySearch(values(), cme2));
        };
    }

    public String getName() {
        return name;
    }

    /**
     * S'il y a un parent, on récupère le MgppWidgetType correspondant au widgetType du parent. Sinon, il aura été renseigné dans l'énum courante.
     */
    public MgppWidgetTypeEnum getWidgetType() {
        if (name != null) {
            return widgetType;
        }
        return MgppWidgetTypeEnum.fromWidgetTypeEnum(parent.getEditWidgetType());
    }
}
