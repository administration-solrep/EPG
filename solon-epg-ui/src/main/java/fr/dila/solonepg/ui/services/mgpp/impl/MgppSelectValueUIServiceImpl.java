package fr.dila.solonepg.ui.services.mgpp.impl;

import static fr.dila.solonmgpp.api.constant.VocabularyConstants.VOCABULARY_FONCTION_DIRECTORY;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.services.mgpp.MgppSelectValueUIService;
import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.organigramme.InstitutionNode;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.ss.ui.services.impl.SSSelectValueUIServiceImpl;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.SelectValueDTO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class MgppSelectValueUIServiceImpl extends SSSelectValueUIServiceImpl implements MgppSelectValueUIService {

    @Override
    public List<SelectValueDTO> getAllAttributionsCommission() {
        return getSelectValuesFromVocabulary("attribution_commission");
    }

    @Override
    public List<SelectValueDTO> getAllDecisionsProcAcc() {
        return getSelectValuesFromVocabulary("decision_proc_acc");
    }

    @Override
    public List<SelectValueDTO> getAllInstitutions() {
        return getSelectableInstitutions(SolonMgppServiceLocator.getInstitutionService().findAllInstitution());
    }

    @Override
    public List<SelectValueDTO> getAllMotifsIrrecevabilite() {
        return getSelectValuesFromVocabulary("motif_irrecevabilite");
    }

    @Override
    public List<SelectValueDTO> getAllNaturesLoi() {
        return getSelectValuesFromVocabulary("nature_loi");
    }

    @Override
    public List<SelectValueDTO> getAllNaturesRapport() {
        return getSelectValuesFromVocabulary("nature_rapport");
    }

    @Override
    public List<SelectValueDTO> getAllNiveauxLecture() {
        return getSelectValuesFromVocabulary("niveau_lecture");
    }

    @Override
    public List<SelectValueDTO> getAllRapportsParlement() {
        return getSelectValuesFromVocabulary("rapport_parlement");
    }

    @Override
    public List<SelectValueDTO> getAllResultatsCMP() {
        return getSelectValuesFromVocabulary("resultat_cmp");
    }

    @Override
    public List<SelectValueDTO> getAllSensAvis() {
        return getSelectValuesFromVocabulary("sens_avis");
    }

    @Override
    public List<SelectValueDTO> getAllSortsAdoption() {
        return getSelectValuesFromVocabulary("sort_adoption");
    }

    @Override
    public List<SelectValueDTO> getAllTypesActeEpp() {
        return getSelectValuesFromVocabulary("type_acte_epp");
    }

    @Override
    public List<SelectValueDTO> getAllTypesRapport() {
        return getSelectValuesFromVocabulary("rapport_parlement");
    }

    @Override
    public List<SelectValueDTO> getAllStatutDossier() {
        return getSelectValuesFromVocabulary("statut_dossier");
    }

    @Override
    public List<SelectValueDTO> getAllTypesLoi() {
        return getSelectValuesFromVocabulary("type_loi");
    }

    private String getDynamicLabel(String type, String value) {
        return ResourceHelper.getString("label.mgpp." + type + "." + value);
    }

    @Override
    public List<SelectValueDTO> getSelectableInstitutions() {
        return getSelectableInstitutions(SolonMgppServiceLocator.getInstitutionService().findFilteredInstitution());
    }

    private List<SelectValueDTO> getSelectableInstitutions(List<InstitutionNode> institutions) {
        return getSelectValues(
            institutions,
            InstitutionNode::getId,
            instit -> getDynamicLabel("institution", instit.getId())
        );
    }

    @Override
    public List<SelectValueDTO> getSelectValuesFromVocabulary(String vocabulary) {
        return getSelectValuesFromVocabulary(vocabulary, STVocabularyConstants.VOCABULARY);
    }

    @Override
    public List<SelectValueDTO> getSelectableMinisteres() {
        return getSelectValues(
            STServiceLocator
                .getSTMinisteresService()
                .getAllMinisteres()
                .stream()
                .sorted(Comparator.comparingLong(EntiteNode::getOrdre))
                .collect(Collectors.toList()),
            EntiteNode::getId,
            EntiteNode::getLabel
        );
    }

    @Override
    public List<SelectValueDTO> getChargesMission(CoreSession session) {
        TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
        DocumentModel tableReferenceDoc = tableReferenceService.getTableReference(session);
        TableReference tableReference = tableReferenceDoc.getAdapter(TableReference.class);

        return Optional
            .ofNullable(tableReference.getChargesMission())
            .map(MgppSelectValueUIServiceImpl::getUsersInfoFromChargesMission)
            .map(usersInfo -> getSelectValues(usersInfo, Function.identity(), Function.identity()))
            .orElseGet(ArrayList::new);
    }

    private static List<String> getUsersInfoFromChargesMission(List<String> chargesMission) {
        STUserService stUserService = STServiceLocator.getSTUserService();
        return chargesMission
            .stream()
            .map(idUser -> stUserService.getUserInfo(idUser, STUserService.CIVILITE_ABREGEE_PRENOM_NOM))
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
    }

    @Override
    public List<SelectValueDTO> getAllTypesCommunication() {
        return getSelectValues(
                SolonMgppServiceLocator.getEvenementTypeService().findEvenementType(),
                EvenementTypeDescriptor::getName,
                EvenementTypeDescriptor::getLabel
            )
            .stream()
            .sorted(Comparator.comparing(SelectValueDTO::getLabel))
            .collect(Collectors.toList());
    }

    @Override
    public List<SelectValueDTO> getAllBasesLegales() {
        return getSelectValuesFromVocabulary("base_legale_nature_texte");
    }

    @Override
    public List<SelectValueDTO> getAllTypesMandats() {
        return getSelectValuesFromVocabulary("type_mandat_epp");
    }

    @Override
    public List<SelectValueDTO> getAllNaturesFdr() {
        return getSelectValuesFromVocabulary("nature_fdr");
    }

    @Override
    public List<SelectValueDTO> getAllLegislatures() {
        return getSelectValuesFromVocabulary("legislature", VocabularyConstants.LEGISLATURE_VOCABULARY_SCHEMA);
    }

    @Override
    public List<SelectValueDTO> getAllSessions() {
        return getSelectValuesFromVocabulary("session");
    }

    @Override
    public List<SelectValueDTO> getAllFonctions() {
        return getSelectValuesFromVocabulary(VOCABULARY_FONCTION_DIRECTORY);
    }
}
