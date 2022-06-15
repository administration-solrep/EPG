package fr.dila.solonepg.ui.enums.mgpp;

import com.google.common.collect.Lists;
import fr.dila.solonepg.ui.helper.mgpp.MgppEditWidgetFactory;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.core.requete.recherchechamp.Parametre;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.WidgetTypeEnum;
import fr.dila.st.ui.enums.parlement.CommunicationMetadonneeEnum;
import fr.sword.xsd.solon.epp.Institution;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;

public enum MgppSelectValueType {
    EMETTEUR(CommunicationMetadonneeEnum.EMETTEUR, MgppSelectValueType::getGouvernement),
    DESTINATAIRE(CommunicationMetadonneeEnum.DESTINATAIRE, MgppSelectValueType::getFilteredInstitution),
    COPIE(null, MgppSelectValueType::getFilteredInstitution),
    INSTITUTION(null, MgppSelectValueType::getAllInstitution),
    DESTINATAIRE_COPIE(CommunicationMetadonneeEnum.DESTINATAIRE_COPIE, MgppSelectValueType::getFilteredInstitution),
    TYPE_LOI(CommunicationMetadonneeEnum.TYPE_LOI, MgppSelectValueType::getTypeLoi),
    NATURE_LOI(CommunicationMetadonneeEnum.NATURE_LOI, MgppSelectValueType::getNatureLoi),
    NATURE(CommunicationMetadonneeEnum.NATURE, MgppSelectValueType::getNatureRapport),
    NATURE_RAPPORT(CommunicationMetadonneeEnum.NATURE_RAPPORT, MgppSelectValueType::getNatureRapport),
    ATTRIBUTION_COMMISSION(CommunicationMetadonneeEnum.ATTRIBUTION_COMMISSION, MgppSelectValueType::getCommisions),
    SORT_ADOPTION(CommunicationMetadonneeEnum.SORT_ADOPTION, MgppSelectValueType::getSort),
    RESULTAT_CMP(CommunicationMetadonneeEnum.RESULTAT_CMP, MgppSelectValueType::getResultatsCMP),
    MOTIF_IRRECEVABILITE(CommunicationMetadonneeEnum.MOTIF_IRRECEVABILITE, MgppSelectValueType::getMotifIrrecev),
    TYPE_ACTE(CommunicationMetadonneeEnum.TYPE_ACTE, MgppSelectValueType::getTypeActeEpp),
    TYPE_RAPPORT(null, MgppSelectValueType::getTypeRapport),
    TYPE_COM(null, MgppSelectValueType::getTypeCommunication),
    BASE_LEGALE(null, MgppSelectValueType::getBaseLegale),
    MINISTERE(null, MgppSelectValueType::getMinisteres),
    SENS_AVIS(CommunicationMetadonneeEnum.SENS_AVIS, MgppSelectValueType::getAvis),
    RAPPORT_PARLEMENT(CommunicationMetadonneeEnum.RAPPORT_PARLEMENT, MgppSelectValueType::getRapports),
    DECISION_PROC_ACC(CommunicationMetadonneeEnum.DECISION_PROC_ACC, MgppSelectValueType::getProcAcc),
    NIVEAU_LECTURE(CommunicationMetadonneeEnum.NIVEAU_LECTURE, MgppSelectValueType::getNiveauLecture),
    STATUT(null, MgppSelectValueType::getAllStatut),
    ETAT_EVENT(null, MgppSelectValueType::getAllStatutEvenement),
    NATURE_FDR(null, MgppSelectValueType::getNatureFdr),
    LEGISLATURE(null, MgppSelectValueType::getLegislature),
    SESSION(null, MgppSelectValueType::getSession),
    POSITION_ALERTE(CommunicationMetadonneeEnum.POSITION_ALERTE, MgppSelectValueType::getPositionAlerte),
    DEFAULT(null, MgppSelectValueType::getDefaultSelect);

    private final CommunicationMetadonneeEnum value;
    private final Supplier<List<SelectValueDTO>> selects;

    MgppSelectValueType(CommunicationMetadonneeEnum value, Supplier<List<SelectValueDTO>> selects) {
        this.value = value;
        this.selects = selects;
    }

    private static List<SelectValueDTO> getFilteredInstitution() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getSelectableInstitutions();
    }

    private static List<SelectValueDTO> getAllInstitution() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllInstitutions();
    }

    private static List<SelectValueDTO> getDefaultSelect() {
        return Collections.emptyList();
    }

    private static List<SelectValueDTO> getGouvernement() {
        return Lists.newArrayList(
            new SelectValueDTO(
                Institution.GOUVERNEMENT.value(),
                ResourceHelper.getString("label.mgpp.institution.GOUVERNEMENT")
            )
        );
    }

    private static List<SelectValueDTO> getTypeLoi() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllTypesLoi();
    }

    private static List<SelectValueDTO> getBaseLegale() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllBasesLegales();
    }

    private static List<SelectValueDTO> getMinisteres() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getSelectableMinisteres();
    }

    private static List<SelectValueDTO> getTypeActeEpp() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllTypesActeEpp();
    }

    private static List<SelectValueDTO> getAllStatut() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllStatutDossier();
    }

    private static List<SelectValueDTO> getAllStatutEvenement() {
        return SolonMgppServiceLocator
            .getMetaDonneesService()
            .findAllEtatEvenement()
            .stream()
            .map(etat -> new SelectValueDTO(etat, "label.mgpp.etat." + etat))
            .collect(Collectors.toList());
    }

    private static List<SelectValueDTO> getTypeRapport() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllTypesRapport();
    }

    private static List<SelectValueDTO> getTypeCommunication() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllTypesCommunication();
    }

    private static List<SelectValueDTO> getCommisions() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllAttributionsCommission();
    }

    private static List<SelectValueDTO> getProcAcc() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllDecisionsProcAcc();
    }

    private static List<SelectValueDTO> getMotifIrrecev() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllMotifsIrrecevabilite();
    }

    private static List<SelectValueDTO> getNatureLoi() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllNaturesLoi();
    }

    private static List<SelectValueDTO> getNatureRapport() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllNaturesRapport();
    }

    private static List<SelectValueDTO> getNiveauLecture() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllNiveauxLecture();
    }

    private static List<SelectValueDTO> getRapports() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllRapportsParlement();
    }

    private static List<SelectValueDTO> getResultatsCMP() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllResultatsCMP();
    }

    private static List<SelectValueDTO> getAvis() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllSensAvis();
    }

    private static List<SelectValueDTO> getSort() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllSortsAdoption();
    }

    private static List<SelectValueDTO> getNatureFdr() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllNaturesFdr();
    }

    private static List<SelectValueDTO> getLegislature() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllLegislatures();
    }

    private static List<SelectValueDTO> getSession() {
        return MgppUIServiceLocator.getMgppSelectValueUIService().getAllSessions();
    }

    private static List<SelectValueDTO> getPositionAlerte() {
        return Lists.newArrayList(
            new SelectValueDTO("true", ResourceHelper.getString("label.mgpp.evenement.positionAlerte.debut")),
            new SelectValueDTO("false", ResourceHelper.getString("label.mgpp.evenement.positionAlerte.fin"))
        );
    }

    public List<SelectValueDTO> getSelectValues() {
        return selects.get();
    }

    public CommunicationMetadonneeEnum getValue() {
        return value;
    }

    public List<Parametre> buildParametreSelect(Serializable entryId, String typeChamp) {
        return Lists.newArrayList(
            new Parametre(MgppEditWidgetFactory.LST_VALUES_PARAM_NAME, getSelectValues()),
            buildParametreCurValeur(entryId, typeChamp)
        );
    }

    @SuppressWarnings("unchecked")
    private Parametre buildParametreCurValeur(Serializable entryId, String typeChamp) {
        if (WidgetTypeEnum.TEXT.toString().equals(typeChamp) && CollectionUtils.isNotEmpty(getSelectValues())) {
            if (entryId instanceof String || entryId instanceof Boolean) {
                return new Parametre(MgppEditWidgetFactory.VALEUR, getValueForEntryId(entryId.toString()));
            }
            if (entryId instanceof List) {
                List<String> entryIds = (List<String>) entryId;
                return new Parametre(
                    MgppEditWidgetFactory.VALEUR,
                    entryIds.stream().map(this::getValueForEntryId).collect(Collectors.joining(", "))
                );
            }
        }
        return new Parametre(NIVEAU_LECTURE.equals(this) ? "selectValeur" : MgppEditWidgetFactory.VALEUR, entryId);
    }

    private String getValueForEntryId(String entryId) {
        return getSelectValues()
            .stream()
            .filter(select -> select.getId().equals(entryId))
            .findFirst()
            .map(SelectValueDTO::getLabel)
            .orElse(entryId);
    }

    public static MgppSelectValueType fromValue(CommunicationMetadonneeEnum searchedValue) {
        return Stream
            .of(values())
            .filter(curEnum -> curEnum != DEFAULT && curEnum.getValue() == searchedValue)
            .findFirst()
            .orElse(DEFAULT);
    }
}
