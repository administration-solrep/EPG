package fr.dila.solonepg.ui.bean.pan;

import fr.dila.ss.ui.bean.SSConsultDossierDTO;

public class ConsultTexteMaitreDTO extends SSConsultDossierDTO {
    private String nor = "";

    private String numero = "";

    private String titre = "";

    private boolean texteMaitreIsLocked;

    private boolean canEditTexteMaitre;

    private boolean tableauProgrammationIsLocked;

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public boolean getTexteMaitreIsLocked() {
        return texteMaitreIsLocked;
    }

    public void setTexteMaitreIsLocked(boolean texteMaitreIsLocked) {
        this.texteMaitreIsLocked = texteMaitreIsLocked;
    }

    public boolean getCanEditTexteMaitre() {
        return canEditTexteMaitre;
    }

    public void setCanEditTexteMaitre(boolean canEditTexteMaitre) {
        this.canEditTexteMaitre = canEditTexteMaitre;
    }

    public boolean getTableauProgrammationIsLocked() {
        return tableauProgrammationIsLocked;
    }

    public void setTableauProgrammationIsLocked(boolean tableauProgrammationIsLocked) {
        this.tableauProgrammationIsLocked = tableauProgrammationIsLocked;
    }
}
