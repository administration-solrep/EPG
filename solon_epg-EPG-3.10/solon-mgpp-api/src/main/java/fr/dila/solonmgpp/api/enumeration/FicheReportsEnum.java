package fr.dila.solonmgpp.api.enumeration;

import fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant;

/**
 * Enumération des rapports et fiches présentation
 *
 */
public enum FicheReportsEnum {
  
	FICHE_AVIS_NOMINATION("0", SolonMgppFicheConstant.FICHE_AVIS_NOMINATION_NAME, SolonMgppFicheConstant.FICHE_AVIS_NOMINATION),
	FICHE_DECRET_PR("1", SolonMgppFicheConstant.FICHE_DECRET_PR_NAME, SolonMgppFicheConstant.FICHE_DECRET_PR),
	FICHE_IE("2", SolonMgppFicheConstant.FICHE_IE_NAME,	SolonMgppFicheConstant.FICHE_IE),
	FICHE_LOI("3", SolonMgppFicheConstant.FICHE_LOI_NAME, SolonMgppFicheConstant.FICHE_LOI),
	FICHE_OEP("4", SolonMgppFicheConstant.FICHE_OEP_NAME, SolonMgppFicheConstant.FICHE_OEP),
	FICHE_RAPPORT("5", SolonMgppFicheConstant.FICHE_RAPPORT_NAME, SolonMgppFicheConstant.FICHE_RAPPORT),
	FICHE_RESOLUTION("6", SolonMgppFicheConstant.FICHE_RESOLUTION_NAME, SolonMgppFicheConstant.FICHE_RESOLUTION),
	FICHE_DECLARATION_GENERALE("7", SolonMgppFicheConstant.FICHE_DPG_NAME, SolonMgppFicheConstant.FICHE_DPG),
	FICHE_DOC("8", SolonMgppFicheConstant.FICHE_DOC_NAME, SolonMgppFicheConstant.FICHE_DOC),
	FICHE_SD("9", SolonMgppFicheConstant.FICHE_SD_NAME, SolonMgppFicheConstant.FICHE_SD),
	FICHE_JSS("10", SolonMgppFicheConstant.FICHE_JSS_NAME, SolonMgppFicheConstant.FICHE_JSS),
	FICHE_AUD("11", SolonMgppFicheConstant.FICHE_AUD_NAME, SolonMgppFicheConstant.FICHE_AUD);
    
    
    private String identifiant;
    private String name;
    private String file;
    

    FicheReportsEnum(String identifiant, String name, String file) {
        this.identifiant = identifiant;
        this.setName(name);
        this.setFile(file);
    }

    public static String getFileById(String identifiant) {
        for (FicheReportsEnum elem : FicheReportsEnum.values()) {
            if (elem.getId().equals(identifiant)) {
                return elem.getFile();
            }
        }
        return null;
    }

    public static String getNameById(String identifiant) {
        for (FicheReportsEnum elem : FicheReportsEnum.values()) {
            if (elem.getId().equals(identifiant)) {
                return elem.getName();
            }
        }
        return null;
    }

    public String getId() {
        return identifiant;
    }

    public void setId(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
