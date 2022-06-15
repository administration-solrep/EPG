package fr.dila.solonepg.ui.bean;

import java.util.ArrayList;
import java.util.List;

public class SubstitutionMassSummaryList {
    private List<SelectionSummaryDTO> substituableDossiers = new ArrayList<>();
    private List<SelectionSummaryDTO> ignoredDossiers = new ArrayList<>();

    public SubstitutionMassSummaryList() {}

    public List<SelectionSummaryDTO> getSubstituableDossiers() {
        return substituableDossiers;
    }

    public void setSubstituableDossierss(List<SelectionSummaryDTO> substituableDossiers) {
        this.substituableDossiers = substituableDossiers;
    }

    public List<SelectionSummaryDTO> getIgnoredDossiers() {
        return ignoredDossiers;
    }

    public void setIgnoredDossiers(List<SelectionSummaryDTO> ignoredDossiers) {
        this.ignoredDossiers = ignoredDossiers;
    }
}
