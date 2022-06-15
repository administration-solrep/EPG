package fr.dila.solonepg.ui.bean.action;

public class EpgSelectionToolActionDTO {
    private boolean isSelectionTypeDossier;

    private boolean isSelectionTypeModele;

    private boolean isSelectionEmpty;

    public boolean getIsSelectionTypeDossier() {
        return isSelectionTypeDossier;
    }

    public void setSelectionTypeDossier(boolean isSelectionTypeDossier) {
        this.isSelectionTypeDossier = isSelectionTypeDossier;
    }

    public boolean getIsSelectionTypeModele() {
        return isSelectionTypeModele;
    }

    public void setSelectionTypeModele(boolean isSelectionTypeModele) {
        this.isSelectionTypeModele = isSelectionTypeModele;
    }

    public boolean getisSelectionEmpty() {
        return isSelectionEmpty;
    }

    public void setSelectionNotEmpty(boolean isSelectionEmpty) {
        this.isSelectionEmpty = isSelectionEmpty;
    }
}
