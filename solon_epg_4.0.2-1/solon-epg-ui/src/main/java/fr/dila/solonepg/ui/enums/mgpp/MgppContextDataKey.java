package fr.dila.solonepg.ui.enums.mgpp;

import fr.dila.solonepg.ui.bean.MgppDossierCommunicationConsultationFiche;
import fr.dila.solonepg.ui.bean.MgppExportable;
import fr.dila.solonepg.ui.th.bean.MessageListForm;
import fr.dila.solonepg.ui.th.bean.MgppCorbeilleContentListForm;
import fr.dila.solonepg.ui.th.bean.MgppFichePresentationOEPForm;
import fr.dila.solonepg.ui.th.bean.MgppIdentiteForm;
import fr.dila.solonepg.ui.th.bean.MgppMandatForm;
import fr.dila.solonepg.ui.th.bean.MgppMinistereForm;
import fr.dila.solonepg.ui.th.bean.MgppRepresentantForm;
import fr.dila.solonepg.ui.th.bean.MgppSuiviEcheancesListForm;
import fr.dila.solonepg.ui.th.bean.ModeleCourrierConsultationForm;
import fr.dila.solonepg.ui.th.model.bean.MgppParamForm;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName;
import fr.dila.st.ui.enums.ContextDataKey;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public enum MgppContextDataKey implements ContextDataKey {
    ACTEUR_ID(String.class),
    BASIC_SEARCH(Boolean.class),
    CAN_USER_LOCK_FICHE(Boolean.class, "canUserLockFiche"),
    CAN_USER_UNLOCK_FICHE(Boolean.class, "canUserUnlockFiche"),
    COMMUNICATION_ID(String.class),
    CORBEILLE_ID(MgppCorbeilleName.class),
    CRITERE_RECHERCHE(CritereRechercheDTO.class),
    CURRENT_EVENT(EvenementDTO.class),
    DOSSIERS_PARLEMENTAIRES_SELECTED(String.class, "dossiersParlementairesSelected"),
    DOSSIER_COMMUNICATION_CONSULTATION_FICHE(MgppDossierCommunicationConsultationFiche.class),
    EVENEMENT_DTO(EvenementDTO.class),
    EVENT_TYPE(String.class),
    EXPORT_LIST(MgppExportable.class),
    FICHE_ID(String.class),
    FICHE_LIST_FORM(MgppCorbeilleContentListForm.class),
    FICHE_METADONNEES_MAP(Map.class),
    FORCE_REFRESH(Boolean.class),
    FORM(MgppFichePresentationOEPForm.class),
    GERER_FICHE_PRESENTATION(Boolean.class),
    GOUVERNEMENT_ACTIF(Boolean.class),
    IDENTITE_FORM(MgppIdentiteForm.class),
    IDENTITE_NOM(String.class),
    INPUT(String.class),
    IS_EDIT_MODE(Boolean.class),
    IS_EN_ATTENTE(Boolean.class, "isEnAttente"),
    IS_FICHE_LOI_VISIBLE(Boolean.class, "isFicheLoiVisible"),
    IS_FICHE_SUPPRIMABLE(Boolean.class, "isFicheSupprimable"),
    IS_FICHE_VERROUILLEE(Boolean.class, "isFicheVerrouillee"),
    IS_FOND_DOSSIER_VISIBLE(Boolean.class, "isFondDossierVisible"),
    IS_PDF(Boolean.class),
    IS_RECHERCHE_RAPIDE(Boolean.class),
    JSON_SEARCH(String.class),
    MANDAT_FORM(MgppMandatForm.class),
    MAP_EVENT(Map.class),
    MAP_SEARCH(Map.class),
    MESSAGE_ID(String.class),
    MESSAGE_LIST_FORM(MessageListForm.class),
    METTRE_EN_ATTENTE(Boolean.class),
    MINISTERE_FORM(MgppMinistereForm.class),
    MODELE_CONSULTATION(Boolean.class),
    MODELE_COURRIER_CONSULTATION_FORM(ModeleCourrierConsultationForm.class),
    MODELE_NAME(String.class),
    MODELE_TEMPLATE_NAME(String.class),
    NUMERO_VERSION(String.class),
    ORGANISME_OEP(String.class),
    PARAM_FORM(MgppParamForm.class),
    PEUT_LIER_UN_OEP(Boolean.class, "peutLierUnOEP"),
    PEUT_METTRE_EN_ATTENTE(Boolean.class, "peutMettreEnAttente"),
    PROPRIETAIRE(String.class),
    PUBLIER(Boolean.class),
    REPRESENTANT_FORM(MgppRepresentantForm.class),
    SUIVI_ECHEANCES_LIST_FORM(MgppSuiviEcheancesListForm.class),
    TABLE_REF(String.class),
    TYPES_COMMUNICATION(List.class),
    TYPE_ORGANISME(String.class),
    TYPE_REPRESENTANT(String.class),
    VERSION_ID(String.class);

    private final Class<?> valueType;
    private final String specificKey;

    MgppContextDataKey(Class<?> valueType) {
        this(valueType, null);
    }

    MgppContextDataKey(Class<?> valueType, String specificKey) {
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
