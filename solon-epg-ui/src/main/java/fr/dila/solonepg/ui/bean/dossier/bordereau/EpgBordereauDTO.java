package fr.dila.solonepg.ui.bean.dossier.bordereau;

import java.util.HashMap;
import java.util.Map;

public class EpgBordereauDTO {

    public EpgBordereauDTO() {
        super();
    }

    private InformationsActeDTO informationsActe;
    private ResponsablesActeDTO responsablesActe;
    private ConseilEtatDTO conseilEtat;
    private ParutionDTO parution;
    private TranspositionApplicationDTO transpositionApplication;
    private BaseLegaleDTO baseLegale;
    private DonneesIndexationDTO donneesIndexation;
    private PeriodiciteDTO periodicite;
    private Boolean isEdit = Boolean.FALSE;
    private Map<String, Boolean> acteVisibility = new HashMap<>();
    private Boolean isBaseLegaleEmpty = Boolean.FALSE;
    private Boolean isCopy = Boolean.FALSE;

    public InformationsActeDTO getInformationsActe() {
        return informationsActe;
    }

    public void setInformationsActe(InformationsActeDTO informationsActe) {
        this.informationsActe = informationsActe;
    }

    public ResponsablesActeDTO getResponsablesActe() {
        return responsablesActe;
    }

    public void setResponsablesActe(ResponsablesActeDTO responsablesActe) {
        this.responsablesActe = responsablesActe;
    }

    public ConseilEtatDTO getConseilEtat() {
        return conseilEtat;
    }

    public void setConseilEtat(ConseilEtatDTO conseilEtat) {
        this.conseilEtat = conseilEtat;
    }

    public ParutionDTO getParution() {
        return parution;
    }

    public void setParution(ParutionDTO parution) {
        this.parution = parution;
    }

    public TranspositionApplicationDTO getTranspositionApplication() {
        return transpositionApplication;
    }

    public void setTranspositionApplication(TranspositionApplicationDTO transpositionApplication) {
        this.transpositionApplication = transpositionApplication;
    }

    public BaseLegaleDTO getBaseLegale() {
        return baseLegale;
    }

    public void setBaseLegale(BaseLegaleDTO baseLegale) {
        this.baseLegale = baseLegale;
    }

    public DonneesIndexationDTO getDonneesIndexation() {
        return donneesIndexation;
    }

    public void setDonneesIndexation(DonneesIndexationDTO donneesIndexation) {
        this.donneesIndexation = donneesIndexation;
    }

    public PeriodiciteDTO getPeriodicite() {
        return periodicite;
    }

    public void setPeriodicite(PeriodiciteDTO periodicite) {
        this.periodicite = periodicite;
    }

    public Boolean getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(Boolean isEdit) {
        this.isEdit = isEdit;
    }

    public Map<String, Boolean> getActeVisibility() {
        return acteVisibility;
    }

    public void setActeVisibility(Map<String, Boolean> acteVisibility) {
        this.acteVisibility = acteVisibility;
    }

    public Boolean getIsBaseLegaleEmpty() {
        return isBaseLegaleEmpty;
    }

    public void setIsBaseLegaleEmpty(Boolean isBaseLegaleEmpty) {
        this.isBaseLegaleEmpty = isBaseLegaleEmpty;
    }

    public Boolean getIsCopy() {
        return isCopy;
    }

    public void setIsCopy(Boolean isCopy) {
        this.isCopy = isCopy;
    }
}
