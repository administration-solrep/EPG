package fr.dila.solonepg.ui.bean;

import java.util.ArrayList;
import java.util.List;

public class ReattributionNorSummaryList {
    private List<SelectionSummaryDTO> nonReattribuables = new ArrayList<>();
    private List<SelectionSummaryDTO> rectificatifs = new ArrayList<>();
    private List<SelectionSummaryDTO> reattribuables = new ArrayList<>();
    private Boolean hasNoError = Boolean.FALSE;

    public List<SelectionSummaryDTO> getNonReattribuables() {
        return nonReattribuables;
    }

    public void setNonReattribuables(List<SelectionSummaryDTO> nonReattribuables) {
        this.nonReattribuables = nonReattribuables;
    }

    public List<SelectionSummaryDTO> getRectificatifs() {
        return rectificatifs;
    }

    public void setRectificatifs(List<SelectionSummaryDTO> rectificatifs) {
        this.rectificatifs = rectificatifs;
    }

    public List<SelectionSummaryDTO> getReattribuables() {
        return reattribuables;
    }

    public void setReattribuables(List<SelectionSummaryDTO> reattribuables) {
        this.reattribuables = reattribuables;
    }

    public Boolean getHasNoError() {
        return hasNoError;
    }

    public void setHasNoError(Boolean hasNoError) {
        this.hasNoError = hasNoError;
    }
}
