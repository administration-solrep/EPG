package fr.dila.solonepg.ui.bean;

import java.util.ArrayList;
import java.util.List;

public class TransfertSummaryList {
    private List<SelectionSummaryDTO> transferableFolders = new ArrayList<>();
    private List<SelectionSummaryDTO> ignoredFolders = new ArrayList<>();

    public TransfertSummaryList() {}

    public List<SelectionSummaryDTO> getTransferableFolders() {
        return transferableFolders;
    }

    public void setTransferableFolders(List<SelectionSummaryDTO> transferableFolders) {
        this.transferableFolders = transferableFolders;
    }

    public List<SelectionSummaryDTO> getIgnoredFolders() {
        return ignoredFolders;
    }

    public void setIgnoredFolders(List<SelectionSummaryDTO> ignoredFolders) {
        this.ignoredFolders = ignoredFolders;
    }
}
