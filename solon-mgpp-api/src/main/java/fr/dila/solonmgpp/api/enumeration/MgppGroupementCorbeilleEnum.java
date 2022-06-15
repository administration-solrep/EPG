package fr.dila.solonmgpp.api.enumeration;

import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CONSULTATION_FICHE_341_EXISTANT;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CONSULTATION_FICHE_DR_67_EXISTANT;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CONSULTATION_FICHE_DR_EXISTANT;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES_HISTORIQUE;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_CENSURE;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_DECLARATION_DE_POLITIQUE_GENERALE;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_DECLARATION_SUR_UN_SUJET_DETERMINE_50_1C;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_DECRET;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_DEMANDE_AUDITION;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_283C;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_283C_HISTORIQUE;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_INTERVENTION_EXTERIEURE;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_NOMINATION;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_OEP;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_PUBLICATION_ATTRIB;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_PUBLICATION_ATTRIB_PARLEMENTAIRE;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_PUBLICATION_BON_TIRER;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_PUBLICATION_FOURN_EPRV;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_PUBLICATION_PUBLI_JO;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_RAPPORT;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_RAPPORT_HISTORIQUE;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMENT_RESOLUTION_341;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMET_PROC_LEG_ATTENTE;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMET_PROC_LEG_EMIS;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMET_PROC_LEG_HISTORIQUE;
import static fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName.CORBEILLE_GOUVERNEMET_PROC_LEG_RECEPTION;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enumération des regroupements de corbeilles
 * dans l'interface mgpp en lien avec l'id utilisé dans le solonmgpp-action-contrib
 *
 */
public enum MgppGroupementCorbeilleEnum {
    PROCEDURE_LEG(
        "mgpp_procedureLegislative",
        CORBEILLE_GOUVERNEMET_PROC_LEG_RECEPTION,
        CORBEILLE_GOUVERNEMET_PROC_LEG_EMIS,
        CORBEILLE_GOUVERNEMET_PROC_LEG_ATTENTE,
        CORBEILLE_GOUVERNEMENT_CENSURE,
        CORBEILLE_GOUVERNEMET_PROC_LEG_HISTORIQUE
    ),
    PUBLICATION(
        "mgpp_publication",
        CORBEILLE_GOUVERNEMENT_PUBLICATION_ATTRIB_PARLEMENTAIRE,
        CORBEILLE_GOUVERNEMENT_PUBLICATION_ATTRIB,
        CORBEILLE_GOUVERNEMENT_PUBLICATION_FOURN_EPRV,
        CORBEILLE_GOUVERNEMENT_PUBLICATION_BON_TIRER,
        CORBEILLE_GOUVERNEMENT_PUBLICATION_PUBLI_JO
    ),
    RESOLUTIONS("mgpp_resolutionArticle341", CORBEILLE_GOUVERNEMENT_RESOLUTION_341),
    DECLARATION(
        "mgpp_declaration_category_id",
        CORBEILLE_GOUVERNEMENT_INTERVENTION_EXTERIEURE,
        CORBEILLE_GOUVERNEMENT_DECLARATION_DE_POLITIQUE_GENERALE,
        CORBEILLE_GOUVERNEMENT_DECLARATION_SUR_UN_SUJET_DETERMINE_50_1C
    ),
    NOMINATION(
        "mgpp_nomination_category_id",
        CORBEILLE_GOUVERNEMENT_OEP,
        CORBEILLE_GOUVERNEMENT_NOMINATION,
        CORBEILLE_GOUVERNEMENT_DEMANDE_AUDITION
    ),
    ORGANISATION(
        "mgpp_organisation_category_id",
        CORBEILLE_GOUVERNEMENT_DECRET,
        CORBEILLE_GOUVERNEMENT_DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_283C
    ),
    RAPPORT(
        "mgpp_raport_category_id",
        CORBEILLE_GOUVERNEMENT_RAPPORT,
        CORBEILLE_GOUVERNEMENT_AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES
    ),
    DECLARATION_IE("mgpp_interventionExterieure", CORBEILLE_GOUVERNEMENT_INTERVENTION_EXTERIEURE),
    DECLARATION_POL_GEN(
        "mgpp_declarationDePolitiqueGenerale",
        CORBEILLE_GOUVERNEMENT_DECLARATION_DE_POLITIQUE_GENERALE
    ),
    DECLARATION_SUJET50_1C(
        "mgpp_declarationSurUnSujetDetermine",
        CORBEILLE_GOUVERNEMENT_DECLARATION_SUR_UN_SUJET_DETERMINE_50_1C
    ),
    NOMINATION_OEP("mgpp_designationOEP", CORBEILLE_GOUVERNEMENT_OEP),
    NOMINATION_NOMINATION("mgpp_avisNomination", CORBEILLE_GOUVERNEMENT_NOMINATION),
    NOMINATION_AUDITION("mgpp_demandeAudition", CORBEILLE_GOUVERNEMENT_DEMANDE_AUDITION),
    ORGANISATION_DECRET_PR("mgpp_decret", CORBEILLE_GOUVERNEMENT_DECRET),
    ORGANISATION_ARTICLE28_3C(
        "mgpp_demandeMiseEnOeuvreArticle283C",
        CORBEILLE_GOUVERNEMENT_DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_283C,
        CORBEILLE_GOUVERNEMENT_DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_283C_HISTORIQUE,
        CONSULTATION_FICHE_341_EXISTANT
    ),
    RAPPORT_DEPOT(
        "mgpp_depotDeRapport",
        CORBEILLE_GOUVERNEMENT_RAPPORT,
        CORBEILLE_GOUVERNEMENT_RAPPORT_HISTORIQUE,
        CONSULTATION_FICHE_DR_EXISTANT,
        CONSULTATION_FICHE_DR_67_EXISTANT
    ),
    RAPPORT_AUTRES_DOCS(
        "mgpp_autresDocumentsTransmisAuxAssemblees",
        CORBEILLE_GOUVERNEMENT_AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES,
        CORBEILLE_GOUVERNEMENT_AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES_HISTORIQUE
    );

    private String actionMgppId;
    private List<MgppCorbeilleName> corbeillesIds;

    MgppGroupementCorbeilleEnum(String actionMgppId, MgppCorbeilleName... corbeillesIds) {
        this.actionMgppId = actionMgppId;
        this.corbeillesIds = Arrays.asList(corbeillesIds);
    }

    /**
     * Récupère l'actionMgppId à partir d'un id corbeille en paramètre
     * @param corbeilleId
     * @return actionMgppId ou null si aucun ne correspond
     */
    public static String getActionMgppIdFromCorbeilleId(String corbeilleId) {
        for (MgppGroupementCorbeilleEnum elem : MgppGroupementCorbeilleEnum.values()) {
            if (elem.isCorbeilleInGroupement(corbeilleId)) {
                return elem.getActionMgppId();
            }
        }
        return null;
    }

    /**
     * Récupère les corbeilles liées à un actionMgppId
     * @param actionMgppId
     * @return la liste des corbeillesIds, ou une liste vide si l'actionMgppId n'existe pas
     */
    public static List<String> getCorbeilleIdsFromActionMgppId(String actionMgppId) {
        return Stream
            .of(values())
            .filter(group -> group.getActionMgppId().equals(actionMgppId))
            .flatMap(groupName -> groupName.getCorbeillesIds().stream())
            .map(MgppCorbeilleName::getIdCorbeille)
            .collect(Collectors.toList());
    }

    public String getActionMgppId() {
        return this.actionMgppId;
    }

    public List<MgppCorbeilleName> getCorbeillesIds() {
        return this.corbeillesIds;
    }

    public boolean isCorbeilleInGroupement(String corbeilleId) {
        return this.corbeillesIds.stream().anyMatch(id -> id.getIdCorbeille().equals(corbeilleId));
    }

    public static MgppGroupementCorbeilleEnum fromCorbeilleId(String corbeilleId) {
        return Stream.of(values()).filter(group -> group.isCorbeilleInGroupement(corbeilleId)).findFirst().orElse(null);
    }
}
