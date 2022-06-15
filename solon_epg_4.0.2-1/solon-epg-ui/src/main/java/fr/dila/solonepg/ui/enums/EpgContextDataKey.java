package fr.dila.solonepg.ui.enums;

import fr.dila.solonepg.api.enums.FavorisRechercheType;
import fr.dila.solonepg.ui.bean.EditionEtapeSqueletteDTO;
import fr.dila.solonepg.ui.bean.EpgDossierActionDTO;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.EpgFavorisRechercheModeleFdrForm;
import fr.dila.solonepg.ui.bean.EpgIndexationMotCleForm;
import fr.dila.solonepg.ui.bean.EpgListTraitementForm;
import fr.dila.solonepg.ui.bean.EpgSqueletteActionDTO;
import fr.dila.solonepg.ui.bean.action.EpgGestionListeActionDTO;
import fr.dila.solonepg.ui.bean.action.EpgRoutingActionDTO;
import fr.dila.solonepg.ui.bean.action.EpgSelectionToolActionDTO;
import fr.dila.solonepg.ui.bean.dossier.bordereau.BordereauSaveForm;
import fr.dila.solonepg.ui.bean.dossier.bordereau.TranspositionApplicationDetailDTO;
import fr.dila.solonepg.ui.bean.dossier.similaire.DossierSimilaireListForm;
import fr.dila.solonepg.ui.bean.dossier.tablesreference.AdminModeParutionDTO;
import fr.dila.solonepg.ui.bean.dossier.tablesreference.AdminTableReferenceDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.ampliation.AmpliationDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.communication.CommunicationDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.epreuve.EpreuveDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.publication.PublicationDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.reference.ComplementDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.retour.RetourDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.signature.SignatureDTO;
import fr.dila.solonepg.ui.bean.dossier.vecteurpublication.AdminPublicationMinisterielleDTO;
import fr.dila.solonepg.ui.bean.dossier.vecteurpublication.AdminVecteurPublicationDTO;
import fr.dila.solonepg.ui.bean.recherchelibre.CritereRechercheForm;
import fr.dila.solonepg.ui.bean.travail.DossierCreationForm;
import fr.dila.solonepg.ui.th.bean.AdminPublicationMinisterielleForm;
import fr.dila.solonepg.ui.th.bean.AdminVecteurPublicationForm;
import fr.dila.solonepg.ui.th.bean.AttenteSignatureForm;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.th.bean.EpgCreateMassModeleForm;
import fr.dila.solonepg.ui.th.bean.EpgFavorisRechercheListForm;
import fr.dila.solonepg.ui.th.bean.EpgSqueletteListForm;
import fr.dila.solonepg.ui.th.bean.EpgUserListForm;
import fr.dila.solonepg.ui.th.bean.FavorisRechercheForm;
import fr.dila.solonepg.ui.th.bean.ParametreAdamantForm;
import fr.dila.solonepg.ui.th.bean.ParametreApplicationForm;
import fr.dila.solonepg.ui.th.bean.ParametreParapheurFormCreation;
import fr.dila.solonepg.ui.th.bean.StatutArchivageDossierForm;
import fr.dila.solonepg.ui.th.bean.TableauDynamiqueForm;
import fr.dila.solonepg.ui.th.model.bean.EpgModeleFdrForm;
import fr.dila.solonepg.ui.th.model.bean.EpgStatForm;
import fr.dila.solonepg.ui.th.model.bean.SqueletteFdrForm;
import fr.dila.st.ui.enums.ContextDataKey;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public enum EpgContextDataKey implements ContextDataKey {
    ADD_STEP(Boolean.class),
    ADMIN_MODE_PARUTION(AdminModeParutionDTO.class),
    ADMIN_PUBLICATION_MINISTERIELLE(AdminPublicationMinisterielleDTO.class),
    ADMIN_PUBLICATION_MINISTERIELLE_FORM(AdminPublicationMinisterielleForm.class),
    ADMIN_TABLE_REFERENCE(AdminTableReferenceDTO.class),
    ADMIN_VECTEUR_PUBLICATION(AdminVecteurPublicationDTO.class),
    ADMIN_VECTEUR_PUBLICATION_FORM(AdminVecteurPublicationForm.class),
    ANCIEN_POSTE(String.class),
    ATTENTE_SIGNATURE_FORM(AttenteSignatureForm.class),
    BIRT_REPORT_SUFFIX(String.class),
    BORDEREAU_SAVE_FORM(BordereauSaveForm.class),
    CREATE_MASS_MODELE_FORM(EpgCreateMassModeleForm.class),
    CRITERE_RECHERCHE_FORM(CritereRechercheForm.class),
    CURRENT_INDEX(Long.class),
    CURRENT_TREE_SUIVI(String.class),
    DATA(String.class),
    DATE(String.class),
    DELETE_ALL_VERSIONS(Boolean.class),
    DOSSIER_ACTIONS(EpgDossierActionDTO.class, "dossierActions"),
    DOSSIER_AUTEUR(String.class),
    DOSSIER_COAUTEUR(List.class),
    DOSSIER_CREATION_FORM(DossierCreationForm.class),
    DOSSIER_NOM(String.class),
    DOSSIER_NOR(String.class, "dossierNor"),
    DOSSIER_RECHERCHE_FORM(DossierListForm.class),
    DOSSIER_RECHERCHE_FORM_SUIVI(DossierListForm.class),
    DOSSIER_SIMILAIRE_FORM(DossierSimilaireListForm.class),
    DUREE_MAINTIEN_EN_PRODUCTION(Integer.class),
    EDIT_ETAPE_SQUELETTE_DTO(EditionEtapeSqueletteDTO.class),
    EPG_DOSSIER_LIST(EpgDossierList.class),
    EPG_MODELE_FORM(EpgModeleFdrForm.class),
    EPG_STAT_FORM(EpgStatForm.class),
    EXPORT_TYPE(String.class),
    FAVORIS_RECHERCHE_FORM(FavorisRechercheForm.class),
    FAVORIS_RECHERCHE_LIST_FORM(EpgFavorisRechercheListForm.class),
    FAVORIS_RECHERCHE_MODELE_FDR_FORM(EpgFavorisRechercheModeleFdrForm.class),
    FDD_ADD_POSITION(Optional.class),
    FDD_FORMATS(List.class),
    FDD_ID(String.class),
    FDD_NAME(String.class),
    FDD_TYPE(String.class),
    FDR_NAME(String.class),
    GESTION_LIST_ACTIONS(EpgGestionListeActionDTO.class, "gestionListeActions"),
    ID_DOSSIERS(List.class),
    ID_FDRS(List.class),
    ID_FILE(String.class),
    ID_FOLDER(String.class),
    ID_FOLDERS(List.class),
    ID_LISTE(String.class),
    ID_POSTE_RETOUR_DAN(String.class),
    ID_TABLEAU_DYNAMIQUE(String.class, "idTableau"),
    INDEXATION_MOT_CLE(String.class),
    INDEXATION_MOT_CLE_FORM(EpgIndexationMotCleForm.class),
    INDEXATION_MOT_CLE_LIST(String.class),
    INDEXATION_RUBRIQUE(String.class),
    INDEXATION_SIGNATAIRE(String.class),
    IS_DOSSIER_SUIVI(Boolean.class, "isDossierSuivi"),
    IS_FDD_DELETABLE(Boolean.class, "isFddDeletable"),
    IS_FILE_CUT(Boolean.class, "isFileCut"),
    IS_FROM_ADMINISTRATION(Boolean.class),
    IS_FROM_CORBEILLE(Boolean.class, "isFromCorbeille"),
    JSON_QUERY(String.class),
    LABEL_TABLEAU_DYNAMIQUE(String.class, "labelTableau"),
    LIST_COLONNES(List.class),
    LIST_FAVORIS(List.class),
    LIST_FILTERABLE_COLUMNS(List.class),
    MESSAGE_ERROR(String.class),
    NEXT_STATUT(String.class),
    NOR(String.class, "nor"),
    NOR_TO_COPIE(String.class),
    NOUVEAU_POSTE(String.class),
    ORIGINE_EDIT_DOSSIER(String.class),
    PARAMETRES(List.class),
    PARAMETRE_ADAMANT_FORM(ParametreAdamantForm.class, "parametreAdamantForm"),
    PARAMETRE_APPLICATION_FORM(ParametreApplicationForm.class, "parametreApplicationForm"),
    PARAMETRE_PARAPHEUR_FORM(ParametreParapheurFormCreation.class, "parametreParapheurForm"),
    PARAMETRE_PARAPHEUR_FORM_LIST(List.class, "parapheursForm"),
    PERIODICITE_VALUE(String.class),
    POSTE_DEFAUT(String.class),
    POSTE_ID(String.class, "posteId"),
    PROVIDER_NAME(String.class, "providerName"),
    PROVIDER_PARAM_LIST(List.class, "providerParams"),
    PROVIDER_SORT(List.class, "providerSort"),
    PROVIDER_TITLE(String.class, "providerTitle"),
    RECHERCHE_EXPERTE_KEY(String.class, "rechercheExperteKey"),
    RECOMPUTE_LIST(Boolean.class),
    ROUTING_ACTIONS(EpgRoutingActionDTO.class, "routingActions"),
    SELECTION_ACTIONS(EpgSelectionToolActionDTO.class, "selectionActions"),
    SELECTION_TYPE(EpgSelectionTypeEnum.class),
    SHOW_SUIVI_LIBRE_FOOTER(Boolean.class),
    SQUELETTE_ACTIONS(EpgSqueletteActionDTO.class, "squeletteActions"),
    SQUELETTE_FDR_FORM(SqueletteFdrForm.class, "squeletteForm"),
    SQUELETTE_LIST_FORM(EpgSqueletteListForm.class),
    SQUELETTE_VALIDE(Boolean.class, "isSqueletteValide"),
    STATS_ID(String.class),
    STATUT_ARCHIVAGE_DOSSIER_FORM(StatutArchivageDossierForm.class),
    TABLEAU_DYNAMIQUE_FORM(TableauDynamiqueForm.class),
    TD_TYPE_RECHERCHE(String.class),
    TRAITEMENT_PAPIER_AMPLIATION(AmpliationDTO.class),
    TRAITEMENT_PAPIER_COMMUNICATION(CommunicationDTO.class),
    TRAITEMENT_PAPIER_EPREUVE(EpreuveDTO.class),
    TRAITEMENT_PAPIER_LIST_FORM(EpgListTraitementForm.class),
    TRAITEMENT_PAPIER_PUBLICATION(PublicationDTO.class),
    TRAITEMENT_PAPIER_REFERENCE(ComplementDTO.class),
    TRAITEMENT_PAPIER_RETOUR(RetourDTO.class),
    TRAITEMENT_PAPIER_SIGNATURE(SignatureDTO.class),
    TRANSPOSITION_APPLICATION_DETAIL(TranspositionApplicationDetailDTO.class),
    TYPE_ACTE(String.class),
    TYPE_ATTENTE_SIGNATURE(String.class),
    TYPE_FOLDER_PARAPHEUR(String.class),
    TYPE_RECHERCHE(FavorisRechercheType.class),
    UPDATED_IDS(List.class),
    USERNAME(String.class),
    USER_LIST_FORM(EpgUserListForm.class);

    private final Class<?> valueType;
    private final String specificKey;

    EpgContextDataKey(Class<?> valueType) {
        this(valueType, null);
    }

    EpgContextDataKey(Class<?> valueType, String specificKey) {
        this.valueType = valueType;
        this.specificKey = specificKey;
    }

    @Override
    public String getName() {
        return StringUtils.defaultIfBlank(specificKey, name());
    }

    @Override
    public Class<?> getValueType() {
        return valueType;
    }
}
