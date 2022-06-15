package fr.dila.solonepg.ui.bean.dossier.bordereau;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.st.ui.annot.NxProp;

public class PeriodiciteDTO {

    public PeriodiciteDTO() {}

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX + ":" + DossierSolonEpgConstants.PERIODICITE_RAPPORT,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String periodiciteRapport;

    private String periodiciteRapportLabel;

    public String getPeriodiciteRapport() {
        return periodiciteRapport;
    }

    public void setPeriodiciteRapport(String periodiciteRapport) {
        this.periodiciteRapport = periodiciteRapport;
    }

    public String getPeriodiciteRapportLabel() {
        return periodiciteRapportLabel;
    }

    public void setPeriodiciteRapportLabel(String periodiciteRapportLabel) {
        this.periodiciteRapportLabel = periodiciteRapportLabel;
    }
}
