package fr.dila.solonepg.ui.services.impl;

import fr.dila.st.core.util.ResourceHelper;

public class StatistiquesItem {
    private Integer id;
    private String bundleKey;
    private String label;
    private String category;
    private String idRapportBirt;
    private String pathReportResult;
    private boolean isStatBirt;
    private boolean isStatGraphBirt;

    public StatistiquesItem(
        Integer id,
        String bundleKey,
        String idRapportBirt,
        String category,
        String pathReportResult
    ) {
        this(id, bundleKey, idRapportBirt, category, pathReportResult, true, false);
    }

    public StatistiquesItem(
        Integer id,
        String bundleKey,
        String idRapportBirt,
        String category,
        String pathReportResult,
        boolean isStatBirt,
        boolean isStatGraphBirt
    ) {
        super();
        this.setId(id);
        this.bundleKey = bundleKey;
        this.label = ResourceHelper.getString(bundleKey);
        this.idRapportBirt = idRapportBirt;
        this.category = category;
        this.pathReportResult = pathReportResult;
        this.isStatBirt = isStatBirt;
        this.isStatGraphBirt = isStatGraphBirt;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString() {
        return "StatisticItem : [label = " + label + ", category = " + category;
    }

    public void setIdRapportBirt(String rapportBirt) {
        this.idRapportBirt = rapportBirt;
    }

    public String getIdRapportBirt() {
        return idRapportBirt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getBundleKey() {
        return bundleKey;
    }

    public void setBundleKey(String bundleKey) {
        this.bundleKey = bundleKey;
    }

    public String getPathReportResult() {
        return pathReportResult;
    }

    public void setPathReportResult(String pathReportResult) {
        this.pathReportResult = pathReportResult;
    }

    public boolean isStatBirt() {
        return isStatBirt;
    }

    public void setStatBirt(boolean isStatBirt) {
        this.isStatBirt = isStatBirt;
    }

    public boolean isStatGraphBirt() {
        return isStatGraphBirt;
    }

    public void setStatGraphBirt(boolean statGraphBirt) {
        isStatGraphBirt = statGraphBirt;
    }
}
