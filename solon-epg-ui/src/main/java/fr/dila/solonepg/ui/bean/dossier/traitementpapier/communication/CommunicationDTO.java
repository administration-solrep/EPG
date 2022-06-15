package fr.dila.solonepg.ui.bean.dossier.traitementpapier.communication;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.mapper.MapDoc2BeanComplexeTypeProcess;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.FormParam;
import org.apache.commons.collections4.CollectionUtils;

@SwBean
public class CommunicationDTO {
    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_CABINET_PM_COMMUNICATION_XPATH,
        process = MapDoc2BeanComplexeTypeProcess.class
    )
    private List<DestinataireCommunicationDTO> cabinetPm;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_CHARGE_MISSION_COMMUNICATION_XPATH,
        process = MapDoc2BeanComplexeTypeProcess.class
    )
    private List<DestinataireCommunicationDTO> chargeMission;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_AUTRES_DESTINATAIRES_COMMUNICATION_XPATH,
        process = MapDoc2BeanComplexeTypeProcess.class
    )
    private List<DestinataireCommunicationDTO> autresDestinataires;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_CABINET_SG_COMMUNICATION_XPATH,
        process = MapDoc2BeanComplexeTypeProcess.class
    )
    private List<DestinataireCommunicationDTO> cabinetSg;

    @FormParam("signataire")
    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_NOMS_SIGNATAIRES_COMMUNICATION_XPATH
    )
    private String nomsSignatairesCommunication;

    private SelectValueDTO signataire;

    @FormParam("personne")
    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_PERSONNES_NOMMEE_XPATH
    )
    private String personnesNommees;

    @FormParam("priorite")
    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_PRIORITE_XPATH
    )
    private String priorite;

    private List<String> selectedCabinetPm;

    private List<String> selectedChargeMission;

    private List<String> selectedAutresDestinataires;

    public List<DestinataireCommunicationDTO> getCabinetPm() {
        return cabinetPm;
    }

    public void setCabinetPm(List<DestinataireCommunicationDTO> cabinetPm) {
        this.cabinetPm = cabinetPm;
    }

    public List<DestinataireCommunicationDTO> getChargeMission() {
        return chargeMission;
    }

    public void setChargeMission(List<DestinataireCommunicationDTO> chargeMission) {
        this.chargeMission = chargeMission;
    }

    public List<DestinataireCommunicationDTO> getAutresDestinataires() {
        return autresDestinataires;
    }

    public void setAutresDestinataires(List<DestinataireCommunicationDTO> autresDestinataires) {
        this.autresDestinataires = autresDestinataires;
    }

    public List<DestinataireCommunicationDTO> getCabinetSg() {
        return cabinetSg;
    }

    public void setCabinetSg(List<DestinataireCommunicationDTO> cabinetSg) {
        this.cabinetSg = cabinetSg;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public String getPersonnesNommees() {
        return personnesNommees;
    }

    public void setPersonnesNommees(String personnesNommees) {
        this.personnesNommees = personnesNommees;
    }

    public String getNomsSignatairesCommunication() {
        return nomsSignatairesCommunication;
    }

    public void setNomsSignatairesCommunication(String nomsSignatairesCommunication) {
        this.nomsSignatairesCommunication = nomsSignatairesCommunication;
    }

    public SelectValueDTO getSignataire() {
        return signataire;
    }

    public void setSignataire(SelectValueDTO signataire) {
        this.signataire = signataire;
    }

    public List<String> getSelectedCabinetPm() {
        return selectedCabinetPm;
    }

    public void setSelectedCabinetPm(List<String> selectedCabinetPm) {
        this.selectedCabinetPm = selectedCabinetPm;
    }

    public List<String> getSelectedChargeMission() {
        return selectedChargeMission;
    }

    public void setSelectedChargeMission(List<String> selectedChargeMission) {
        this.selectedChargeMission = selectedChargeMission;
    }

    public List<String> getSelectedAutresDestinataires() {
        return selectedAutresDestinataires;
    }

    public void setSelectedAutresDestinataires(List<String> selectedAutresDestinataires) {
        this.selectedAutresDestinataires = selectedAutresDestinataires;
    }

    public List<DestinataireCommunicationDTO> getAllDestinataires() {
        return Stream
            .of(autresDestinataires, cabinetPm, cabinetSg, chargeMission)
            .filter(CollectionUtils::isNotEmpty)
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }
}
