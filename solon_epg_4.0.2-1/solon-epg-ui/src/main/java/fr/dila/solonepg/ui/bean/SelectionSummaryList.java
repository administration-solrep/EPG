package fr.dila.solonepg.ui.bean;

import java.util.ArrayList;
import java.util.List;

public class SelectionSummaryList {
    private List<SelectionSummaryDTO> ignoredDossiers = new ArrayList<>();
    private List<SelectionSummaryDTO> deletableDossiers = new ArrayList<>();

    public SelectionSummaryList() {}

    public List<SelectionSummaryDTO> getIgnoredDossiers() {
        return ignoredDossiers;
    }

    public void setIgnoredDossiers(List<SelectionSummaryDTO> ignoredDossiers) {
        this.ignoredDossiers = ignoredDossiers;
    }

    public List<SelectionSummaryDTO> getDeletableDossiers() {
        return deletableDossiers;
    }

    public void setDeletableDossiers(List<SelectionSummaryDTO> deletableDossiers) {
        this.deletableDossiers = deletableDossiers;
    }
}
