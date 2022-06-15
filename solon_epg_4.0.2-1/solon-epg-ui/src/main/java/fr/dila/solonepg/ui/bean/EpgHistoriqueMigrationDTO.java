package fr.dila.solonepg.ui.bean;

import fr.dila.ss.ui.bean.SSHistoriqueMigrationDTO;

public class EpgHistoriqueMigrationDTO extends SSHistoriqueMigrationDTO {

    public EpgHistoriqueMigrationDTO() {
        super();
    }

    private String ministereRattachement;
    private String reattributionDossiers;
    private String bulletinOfficiel;
    private String tablesReference;

    public String getMinistereRattachement() {
        return ministereRattachement;
    }

    public void setMinistereRattachement(String ministereRattachement) {
        this.ministereRattachement = ministereRattachement;
    }

    public String getReattributionDossiers() {
        return reattributionDossiers;
    }

    public void setReattributionDossiers(String reattributionDossiers) {
        this.reattributionDossiers = reattributionDossiers;
    }

    public String getBulletinOfficiel() {
        return bulletinOfficiel;
    }

    public void setBulletinOfficiel(String bulletinOfficiel) {
        this.bulletinOfficiel = bulletinOfficiel;
    }

    public String getTablesReference() {
        return tablesReference;
    }

    public void setTablesReference(String tablesReference) {
        this.tablesReference = tablesReference;
    }
}
