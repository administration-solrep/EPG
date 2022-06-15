package fr.dila.solonmgpp.api.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe des constantes pour messages.properties
 *
 * @author admin
 *
 */
public final class SolonMgppI18nConstant {
    public static final String TYPE_EVENEMENT_VIDE = "create.evenement.type.evenement.vide";
    public static final String PIECE_JOINTE_FICHIER_MANQUANT = "create.evenement.piece.jointe.fichier.manquant";
    public static final String EVENEMENT_CREER = "create.evenement.ok";
    public static final String EVENEMENT_NON_CREER = "create.evenement.ko";
    public static final String EVENEMENT_RECTIFIER = "rectifier.evenement.ok";
    public static final String EVENEMENT_NON_RECTIFIER = "rectifier.evenement.ko";
    public static final String EVENEMENT_COMPLETER = "completer.evenement.ok";
    public static final String EVENEMENT_NON_COMPLETER = "completer.evenement.ko";
    public static final String EVENEMENT_TRAITER = "traiter.evenement.ok";
    public static final String EVENEMENT_EN_COURS_DE_TRAITEMENT = "traiter.evenement.en.cours";
    public static final String EVENEMENT_NON_TRAITER = "traiter.evenement.ko";
    public static final String EVENEMENT_ACCEPTER = "accepter.evenement.ok";
    public static final String EVENEMENT_NON_ACCEPTER = "accepter.evenement.ko";
    public static final String EVENEMENT_REJETER = "rejeter.evenement.ok";
    public static final String EVENEMENT_NON_REJETER = "rejeter.evenement.ko";
    public static final String EVENEMENT_AR = "ar.evenement.ok";
    public static final String EVENEMENT_NON_AR = "ar.evenement.ko";
    public static final String EVENEMENT_ABANDON = "abandonner.evenement.ok";
    public static final String EVENEMENT_NON_ABANDON = "abandonner.evenement.ko";
    public static final String EVENEMENT_ANNULER = "annuler.evenement.ok";
    public static final String EVENEMENT_NON_ANNULER = "annuler.evenement.ko";
    public static final String EVENEMENT_PUBLIER = "publier.evenement.ok";
    public static final String EVENEMENT_NON_PUBLIER = "publier.evenement.ko";
    public static final String EVENEMENT_SUPPRIMER = "supprimer.evenement.ok";
    public static final String EVENEMENT_NON_SUPPRIMER = "supprimer.evenement.ko";
    public static final String EVENEMENT_MAIL = "mail.evenement.ok";
    public static final String EVENEMENT_NON_MAIL = "mail.evenement.ko";
    public static final String EVENEMENT_MODIFIER = "modifier.evenement.ok";
    public static final String EVENEMENT_NON_MODIFIER = "modifier.evenement.ko";

    public static final String CREER_EVENEMENT_SUCCESSIF = "label.create.evenement.successif";
    public static final String METAS_MANQUANTES = "publier.evenement.metas.manquantes";
    public static final String METAS_MANQUANTES_LIST = "publier.evenement.metas.manquantes.list";
    public static final String SAME_EMETTEUR_DESTIANTAIRE_ERROR = "evenement.same.emetteur.destinataire";
    public static final String COPIE_CONTAINS_EMETTEUR = "evenement.copie.contains.emetteur";
    public static final String COPIE_CONTAINS_DESTINATAIRE = "evenement.copie.contains.destinataire";

    public static final String NOR_OBLIGATOIRE = "evenement.nor.obligatoire";
    public static final String NOR_STRUCTURE_INCORRECTE = "evenement.nor.structure.incorrecte";
    public static final String IDENTIFIANT_DOSSIER_OBLIGATOIRE = "evenement.identifiant.dossier.obligatoire";

    public static final String LABEL_REPRESENTANT_NOM_AN = "label.mgpp.nomination.nom.representantAN";
    public static final String LABEL_REPRESENTANT_FONCTION_AN = "label.mgpp.nomination.fonction.representantAN";
    public static final String LABEL_REPRESENTANT_NOM_FONCTION_AN = "label.mgpp.nomination.nom.fonction.representantAN";
    public static final String LABEL_REPRESENTANT_NOM_SE = "label.mgpp.nomination.nom.representantSE";
    public static final String LABEL_REPRESENTANT_FONCTION_SE = "label.mgpp.nomination.fonction.representantSE";
    public static final String LABEL_REPRESENTANT_NOM_FONCTION_SE = "label.mgpp.nomination.nom.fonction.representantSE";

    public static final String LABEL_NOR = "label.mgpp.evenement.numeroNor";
    public static final String LABEL_IDENTIFIANT_DOSSIER = "label.mdpp.evenement.identifiantDossier";
    public static final String LABEL_EMETTEUR = "label.mgpp.evenement.emetteur";
    public static final String LABEL_DESTINATAIRE = "label.mgpp.evenement.destinataire";
    public static final String LABEL_COPIE = "label.mgpp.evenement.copie";
    public static final String LABEL_NIVEAU_LECTURE = "label.mgpp.evenement.niveauDeLecture";
    public static final String LABEL_OBJET = "label.mgpp.evenement.objet";
    public static final String LABEL_NATURE_LOI = "label.mgpp.evenement.natureLoi";
    public static final String LABEL_TYPE_LOI = "label.mgpp.evenement.typeLoi";
    public static final String LABEL_AUTEUR = "label.mgpp.evenement.auteur";
    public static final String LABEL_COAUTEUR = "label.mgpp.evenement.coauteur";
    public static final String LABEL_INTITULE = "label.mgpp.evenement.intitule";
    public static final String LABEL_HORODATAGE = "label.mgpp.evenement.horodatage";
    public static final String LABEL_PIECE_JOINTE = "label.mgpp.evenement.pieceJointe";
    public static final String LABEL_DATE = "label.mgpp.evenement.date";
    public static final String LABEL_RAPPORT_PARLEMENT = "label.mgpp.evenement.rapportParlement";
    public static final String LABEL_DTE_LETTRE_PM = "label.mgpp.evenement.dateLettrePM";
    public static final String LABEL_DATE_ACTE = "label.mgpp.evenement.dateActe";
    public static final String LABEL_ECHEANCE = "label.mgpp.evenement.echeance";
    public static final String LABEL_BASE_LEGAL = "label.mgpp.evenement.baseLegale";
    public static final String LABEL_ORGANISME = "label.mgpp.evenement.organisme";
    public static final String LABEL_PERSONNE = "label.mgpp.evenement.personne";
    public static final String LABEL_FONCTION = "label.mgpp.evenement.fonction";
    public static final String LABEL_TYPE_ACTE = "label.mgpp.evenement.typeActe";
    public static final String LABEL_DATE_CONGRES = "label.mgpp.evenement.dateCongres";
    public static final String LABEL_DATE_PRESENTATION = "label.mgpp.evenement.datePresentation";
    public static final String LABEL_DATE_DECLARATION = "label.mgpp.evenement.dateDeclaration";
    public static final String LABEL_NUMERO_DEPOT_TEXTE = "label.mgpp.evenement.numeroDepotTexte";
    public static final String LABEL_DATE_DEPOT_TEXTE = "label.mgpp.evenement.dateDepotTexte";
    public static final String LABEL_LIBELLE_ANNEXES = "label.mgpp.evenement.libelleAnnexes";
    public static final String LABEL_DATE_ENGAGEMENT_PROCEDURE = "label.mgpp.evenement.dateEngagementProcedure";
    public static final String LABEL_SORT_ADOPTION = "label.mgpp.evenement.sortAdoption";
    public static final String LABEL_NUMERO_TEXTE_ADOPTE = "label.mgpp.evenement.numeroTexteAdopte";
    public static final String LABEL_DATE_ADOPTION = "label.mgpp.evenement.dateAdoption";
    public static final String LABEL_DATE_PUBLICATION = "label.mgpp.evenement.datePublication";
    public static final String LABEL_DATE_PROMULGATION = "label.mgpp.evenement.datePromulgation";
    public static final String LABEL_TITRE = "label.mgpp.evenement.titre";
    public static final String LABEL_NUMERO_LOI = "label.mgpp.evenement.numeroLoi";

    public static final Map<String, String> REQUIRED_FIELD_MAP = new HashMap<>();

    static {
        REQUIRED_FIELD_MAP.put("natureLoi", LABEL_NATURE_LOI);
        REQUIRED_FIELD_MAP.put("objet", LABEL_OBJET);
        REQUIRED_FIELD_MAP.put("niveauLecture.niveau", LABEL_NIVEAU_LECTURE);
        REQUIRED_FIELD_MAP.put("niveauLecture.code", LABEL_NIVEAU_LECTURE);
        REQUIRED_FIELD_MAP.put("typeLoi", LABEL_TYPE_LOI);
        REQUIRED_FIELD_MAP.put("coAuteur", LABEL_COAUTEUR);
        REQUIRED_FIELD_MAP.put("intitule", LABEL_INTITULE);
        REQUIRED_FIELD_MAP.put("versionCourante.horodatage", LABEL_HORODATAGE);
        REQUIRED_FIELD_MAP.put("auteur", LABEL_AUTEUR);
        REQUIRED_FIELD_MAP.put("date", LABEL_DATE);
        REQUIRED_FIELD_MAP.put("rapportParlement", LABEL_RAPPORT_PARLEMENT);
        REQUIRED_FIELD_MAP.put("dateLettrePm", LABEL_DTE_LETTRE_PM);
        REQUIRED_FIELD_MAP.put("dateActe", LABEL_DATE_ACTE);
        REQUIRED_FIELD_MAP.put("echeance", LABEL_ECHEANCE);
        REQUIRED_FIELD_MAP.put("baseLegale", LABEL_BASE_LEGAL);
        REQUIRED_FIELD_MAP.put("organisme", LABEL_ORGANISME);
        REQUIRED_FIELD_MAP.put("personne", LABEL_PERSONNE);
        REQUIRED_FIELD_MAP.put("fonction", LABEL_FONCTION);
        REQUIRED_FIELD_MAP.put("typeActe", LABEL_TYPE_ACTE);
        REQUIRED_FIELD_MAP.put("dateCongres", LABEL_DATE_CONGRES);
        REQUIRED_FIELD_MAP.put("datePresentation", LABEL_DATE_PRESENTATION);
        REQUIRED_FIELD_MAP.put("dateDeclaration", LABEL_DATE_DECLARATION);
        REQUIRED_FIELD_MAP.put("libelleAnnexes", LABEL_LIBELLE_ANNEXES);
        REQUIRED_FIELD_MAP.put("dateEngagementProcedure", LABEL_DATE_ENGAGEMENT_PROCEDURE);
        REQUIRED_FIELD_MAP.put("numeroDepot", LABEL_NUMERO_DEPOT_TEXTE);
        REQUIRED_FIELD_MAP.put("dateDepot", LABEL_DATE_DEPOT_TEXTE);
        REQUIRED_FIELD_MAP.put("sortAdoption", LABEL_SORT_ADOPTION);
        REQUIRED_FIELD_MAP.put("numeroTextAdopte", LABEL_NUMERO_TEXTE_ADOPTE);
        REQUIRED_FIELD_MAP.put("dateAdoption", LABEL_DATE_ADOPTION);
        REQUIRED_FIELD_MAP.put("datePublication", LABEL_DATE_PUBLICATION);
        REQUIRED_FIELD_MAP.put("datePromulgation", LABEL_DATE_PROMULGATION);
        REQUIRED_FIELD_MAP.put("titre", LABEL_TITRE);
        REQUIRED_FIELD_MAP.put("numeroLoi", LABEL_NUMERO_LOI);
    }

    public static final String AJOUT_FICHIER_OEP = "fichier.ajout.oep";
    public static final String AJOUT_FICHIER_DEFAULT = "fichier.ajout.default";

    public static final String EMPTY_WHERE_SEARCH = "label.recherche.avancee.empty.where";

    private SolonMgppI18nConstant() {
        // default private constructor
    }
}
