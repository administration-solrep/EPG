package fr.dila.solonmgpp.api.enumeration;

import fr.dila.solonmgpp.api.constant.SolonMgppCourrierConstant;

public enum CourrierReportsEnum {
  
    COURRIER_10_PA("0", SolonMgppCourrierConstant.COURRIER_10_PA_NAME, SolonMgppCourrierConstant.COURRIER_10_PA, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_10_PA_RECTIFICATIVE("1", SolonMgppCourrierConstant.COURRIER_10_PA_RECTIFICATIVE_NAME,SolonMgppCourrierConstant.COURRIER_10_PA_RECTIFICATIVE, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_13_1_LECTURE("2", SolonMgppCourrierConstant.COURRIER_13_1_LECTURE_NAME,SolonMgppCourrierConstant.COURRIER_13_1_LECTURE, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_13_1_LECTURE_MODIFIE("3", SolonMgppCourrierConstant.COURRIER_13_1_LECTURE_MODIFIE_NAME,SolonMgppCourrierConstant.COURRIER_13_1_LECTURE_MODIFIE, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_13_1_LECTURE_REJET("4", SolonMgppCourrierConstant.COURRIER_13_1_LECTURE_REJET_NAME, SolonMgppCourrierConstant.COURRIER_13_1_LECTURE_REJET, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_13_1_LECTURE_PA("5",SolonMgppCourrierConstant.COURRIER_13_1_LECTURE_PA_NAME,SolonMgppCourrierConstant.COURRIER_13_1_LECTURE_PA, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_13_1_LECTURE_PA_MODIFIE("6",SolonMgppCourrierConstant.COURRIER_13_1_LECTURE_PA_MODIFIE_NAME,SolonMgppCourrierConstant.COURRIER_13_1_LECTURE_PA_MODIFIE, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_13_1_LECTURE_PA_REJET("7",SolonMgppCourrierConstant.COURRIER_13_1_LECTURE_PA_REJET_NAME,SolonMgppCourrierConstant.COURRIER_13_1_LECTURE_PA_REJET, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_13_2_LECTURE("8",SolonMgppCourrierConstant.COURRIER_13_2_LECTURE_NAME,SolonMgppCourrierConstant.COURRIER_13_2_LECTURE, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_13_2_LECTURE_MODIFIE("9",SolonMgppCourrierConstant.COURRIER_13_2_LECTURE_MODIFIE_NAME,SolonMgppCourrierConstant.COURRIER_13_2_LECTURE_MODIFIE, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_13_N_LECTURE("10",SolonMgppCourrierConstant.COURRIER_13_N_LECTURE_NAME,SolonMgppCourrierConstant.COURRIER_13_N_LECTURE, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_19_CMP("11",SolonMgppCourrierConstant.COURRIER_19_CMP_NAME,SolonMgppCourrierConstant.COURRIER_19_CMP, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_19_CMP_COMMUN("12",SolonMgppCourrierConstant.COURRIER_19_CMP_COMMUN_NAME,SolonMgppCourrierConstant.COURRIER_19_CMP_COMMUN, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_23_CMP("13",SolonMgppCourrierConstant.COURRIER_23_CMP_NAME,SolonMgppCourrierConstant.COURRIER_23_CMP, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_23_BIS_ECHEC_CMP("14",SolonMgppCourrierConstant.COURRIER_23_BIS_ECHEC_CMP_NAME,SolonMgppCourrierConstant.COURRIER_23_BIS_ECHEC_CMP, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_23_BIS_ECHEC_CMP_COMMUN("15",SolonMgppCourrierConstant.COURRIER_23_BIS_ECHEC_CMP_COMMUN_NAME,SolonMgppCourrierConstant.COURRIER_23_BIS_ECHEC_CMP_COMMUN, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_23_BIS_REJET_AN("16",SolonMgppCourrierConstant.COURRIER_23_BIS_REJET_AN_NAME,SolonMgppCourrierConstant.COURRIER_23_BIS_REJET_AN, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_23_BIS_REJET_SENAT("17",SolonMgppCourrierConstant.COURRIER_23_BIS_REJET_SENAT_NAME,SolonMgppCourrierConstant.COURRIER_23_BIS_REJET_SENAT, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_23_BIS_REJET_SENAT_AMDTS("18",SolonMgppCourrierConstant.COURRIER_23_BIS_REJET_SENAT_AMDTS_NAME,SolonMgppCourrierConstant.COURRIER_23_BIS_REJET_SENAT_AMDTS, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_23_DERNIER_LECTURE("19",SolonMgppCourrierConstant.COURRIER_23_DERNIER_LECTURE_NAME,SolonMgppCourrierConstant.COURRIER_23_DERNIER_LECTURE, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    COURRIER_23_DERNIER_LECTURE_REJET("20",SolonMgppCourrierConstant.COURRIER_23_DERNIER_LECTURE_REJET_NAME,SolonMgppCourrierConstant.COURRIER_23_DERNIER_LECTURE_REJET, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_LOI),
    
    COURRIER_CHOIX_MULTIPLES_DECES("1", SolonMgppCourrierConstant.COURRIER_CHOIX_MULTIPLES_DECES_NAME, SolonMgppCourrierConstant.COURRIER_CHOIX_MULTIPLES_DECES, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_OEP),
    COURRIER_CHOIX_MULTIPLES_DEMISSION("2", SolonMgppCourrierConstant.COURRIER_CHOIX_MULTIPLES_DEMISSION_NAME, SolonMgppCourrierConstant.COURRIER_CHOIX_MULTIPLES_DEMISSION, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_OEP),
    COURRIER_CHOIX_MULTIPLES_EXPIRATION("3", SolonMgppCourrierConstant.COURRIER_CHOIX_MULTIPLES_EXPIRATION_NAME, SolonMgppCourrierConstant.COURRIER_CHOIX_MULTIPLES_EXPIRATION, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_OEP),
    COURRIER_LETTRE_CREATION("4", SolonMgppCourrierConstant.COURRIER_LETTRE_CREATION_NAME, SolonMgppCourrierConstant.COURRIER_LETTRE_CREATION, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_OEP),
    COURRIER_LETTRE_RE_CREATION("5", SolonMgppCourrierConstant.COURRIER_LETTRE_RE_CREATION_NAME, SolonMgppCourrierConstant.COURRIER_LETTRE_RE_CREATION, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_OEP),
    COURRIER_LETTRE_SUITE_GOV("6", SolonMgppCourrierConstant.COURRIER_LETTRE_SUITE_GOV_NAME, SolonMgppCourrierConstant.COURRIER_LETTRE_SUITE_GOV, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_OEP),
    COURRIER_LETTRE_SUITE_NOMINATION("7", SolonMgppCourrierConstant.COURRIER_LETTRE_SUITE_NOMINATION_NAME, SolonMgppCourrierConstant.COURRIER_LETTRE_SUITE_NOMINATION, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_OEP),
    
    COURRIER_LETTRES_INJONCTIONS("1", SolonMgppCourrierConstant.COURRIER_LETTRES_INJONCTIONS_NAME, SolonMgppCourrierConstant.COURRIER_LETTRES_INJONCTIONS, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_RESOLUTION_341),
    COURRIER_LETTRES_RESPONSABILITE("2", SolonMgppCourrierConstant.COURRIER_LETTRES_RESPONSABILITE_NAME, SolonMgppCourrierConstant.COURRIER_LETTRES_RESPONSABILITE, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_RESOLUTION_341),

    COURRIER_D_OUV("1", SolonMgppCourrierConstant.COURRIER_D_OUV_NAME, SolonMgppCourrierConstant.COURRIER_D_OUV, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DECRETS_PRESIDENT),
    COURRIER_D_CLO("2", SolonMgppCourrierConstant.COURRIER_D_CLO_NAME, SolonMgppCourrierConstant.COURRIER_D_CLO, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DECRETS_PRESIDENT),
    COURRIER_D_OUV_AMPLI("3", SolonMgppCourrierConstant.COURRIER_D_OUV_AMPLI_NAME, SolonMgppCourrierConstant.COURRIER_D_OUV_AMPLI, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DECRETS_PRESIDENT),
    COURRIER_D_CLO_AMPLI("4", SolonMgppCourrierConstant.COURRIER_D_CLO_AMPLI_NAME, SolonMgppCourrierConstant.COURRIER_D_CLO_AMPLI, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DECRETS_PRESIDENT),
    COURRIER_L_OUV_AN_S("5", SolonMgppCourrierConstant.COURRIER_L_OUV_AN_S_NAME, SolonMgppCourrierConstant.COURRIER_L_OUV_AN_S, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DECRETS_PRESIDENT),
    COURRIER_L_CLO_AN_SE("6", SolonMgppCourrierConstant.COURRIER_L_CLO_AN_SE_NAME, SolonMgppCourrierConstant.COURRIER_L_CLO_AN_SE, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DECRETS_PRESIDENT),
    COURRIER_LETTRES_DECRETS("7", SolonMgppCourrierConstant.COURRIER_LETTRES_DECRETS_NAME, SolonMgppCourrierConstant.COURRIER_LETTRES_DECRETS, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DECRETS_PRESIDENT),
    COURRIER_DECRET_CONGRES_ARTICLE18C("8", SolonMgppCourrierConstant.COURRIER_DECRET_CONGRES_ARTICLE18C_NAME, SolonMgppCourrierConstant.COURRIER_DECRET_CONGRES_ARTICLE18C, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DECRETS_PRESIDENT),
    COURRIER_LETTRES_DECRET_CONGRES_ARTICLE18C("9", SolonMgppCourrierConstant.COURRIER_LETTRES_DECRET_CONGRES_ARTICLE18C_NAME, SolonMgppCourrierConstant.COURRIER_LETTRES_DECRET_CONGRES_ARTICLE18C, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DECRETS_PRESIDENT),
    COURRIER_LETTRES_DECRET_CONGRES("10", SolonMgppCourrierConstant.COURRIER_LETTRES_DECRET_CONGRES_NAME, SolonMgppCourrierConstant.COURRIER_LETTRES_DECRET_CONGRES, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DECRETS_PRESIDENT),
    COURRIER_LETTRES_AMPLIATION_CONGRES("11", SolonMgppCourrierConstant.COURRIER_LETTRES_AMPLIATION_CONGRES_NAME, SolonMgppCourrierConstant.COURRIER_LETTRES_AMPLIATION_CONGRES, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DECRETS_PRESIDENT),
    
    COURRIER_LETTRE_DR__ART_67_FINAL("1", SolonMgppCourrierConstant.COURRIER_LETTRE_DR_ART_67_FINAL_NAME, SolonMgppCourrierConstant.COURRIER_LETTRE_DR_ART_67_FINAL, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DEPOT_RAPPORT),
    COURRIER_LETTRE_DR_NORMAL_FINAL("2", SolonMgppCourrierConstant.COURRIER_LETTRE_DR_NORMAL_FINAL_NAME, SolonMgppCourrierConstant.COURRIER_LETTRE_DR_NORMAL_FINAL, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DEPOT_RAPPORT),
    COURRIER_LETTRE_DR_POUR_INFORMATION("3", SolonMgppCourrierConstant.COURRIER_LETTRE_DR_POUR_INFORMATION_NAME, SolonMgppCourrierConstant.COURRIER_LETTRE_DR_POUR_INFORMATION, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DEPOT_RAPPORT),
    COURRIER_LETTRE_DR_APPLICTION("4", SolonMgppCourrierConstant.COURRIER_LETTRE_DR_APPLICATION_NAME, SolonMgppCourrierConstant.COURRIER_LETTRE_DR_APPLICATION, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DEPOT_RAPPORT),
    
    COURRIER_LETTRES_IE("1", SolonMgppCourrierConstant.COURRIER_LETTRES_IE_NAME, SolonMgppCourrierConstant.COURRIER_LETTRES_IE, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_INTERVENTION_EXT),
    COURRIER_LETTRES_IE_EXTRA("2", SolonMgppCourrierConstant.COURRIER_LETTRES_IE_EXTRA_NAME, SolonMgppCourrierConstant.COURRIER_LETTRES_IE_EXTRA, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_INTERVENTION_EXT),
    
    COURRIER_AUDITION("2", SolonMgppCourrierConstant.COURRIER_AUDITION_AUTRES_NAME, SolonMgppCourrierConstant.COURRIER_AUDITION_AUTRES, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_AUDIT),
    COURRIER_AUDITION_SANTE_PUB("1", SolonMgppCourrierConstant.COURRIER_AUDITION_SANTE_PUB_NAME, SolonMgppCourrierConstant.COURRIER_AUDITION_SANTE_PUB, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_AUDIT),
    
    COURRIER_AUTRES_TRANS("1", SolonMgppCourrierConstant.COURRIER_DOCS_TRANSMIS_AUX_ASSEMBLEES_NAME, SolonMgppCourrierConstant.COURRIER_DOCS_TRANSMIS_AUX_ASSEMBLEES, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DOC),
    COURRIER_TRANS_INVEST_AVENIR("2", SolonMgppCourrierConstant.COURRIER_TRANS_INVEST_AVENIR_NAME, SolonMgppCourrierConstant.COURRIER_TRANS_INVEST_AVENIR, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DOC),
    COURRIER_TRANS_COM("3", SolonMgppCourrierConstant.COURRIER_TRANS_COM_NAME, SolonMgppCourrierConstant.COURRIER_TRANS_COM, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DOC),
    COURRIER_TRANS_CONTREX_PUB("4", SolonMgppCourrierConstant.COURRIER_TRANS_CONTREX_INVEST_PUB_NAME, SolonMgppCourrierConstant.COURRIER_TRANS_CONTREX_INVEST_PUB, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DOC),
    
    COURRIER_APPLICATION_ARTICLE_28("1", SolonMgppCourrierConstant.COURRIER_APPLICATION_ARTICLE_28_NAME, SolonMgppCourrierConstant.COURRIER_APPLICATION_ARTICLE_28, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_JSS),
    
    COURRIER_DECLARATION_POLITIQUE_GENERALE("1", SolonMgppCourrierConstant.COURRIER_DECLARATION_POLITIQUE_GENERALE_NAME, SolonMgppCourrierConstant.COURRIER_DECLARATION_POLITIQUE_GENERALE, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DPG),
    COURRIER_DECLARATION_POLITIQUE_GENERALE_APPROBATION("2", SolonMgppCourrierConstant.COURRIER_DECLARATION_POLITIQUE_GENERALE_APPROBATION_NAME, SolonMgppCourrierConstant.COURRIER_DECLARATION_POLITIQUE_GENERALE_APPROBATION, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_DPG),
    
    COURRIER_DECLARATION_ARTICLE_50_1("1", SolonMgppCourrierConstant.COURRIER_DECLARATION_ARTICLE_50_1_NAME, SolonMgppCourrierConstant.COURRIER_DECLARATION_ARTICLE_50_1, SolonMgppCourrierConstant.TABLE_VOC_COURRIER_SD);
    
    
    //COURRIER_AVENANT_CONVENTIONS("30", SolonMgppCourrierConstant.COURRIER_AVENANT_CONVENTIONS_NAME, SolonMgppCourrierConstant.COURRIER_AVENANT_CONVENTIONS),
    
    
    
    private String id;
    private String name;
    private String file;
    private String table;
    

    CourrierReportsEnum(String id, String name, String file, String table) {
        this.id = id;
        this.setName(name);
        this.setFile(file);
        this.setTable(table);
    }

    public static String getFileByIdAndTable(String id, String table) {
        for (CourrierReportsEnum elem : CourrierReportsEnum.values()) {
            if (elem.getTable().equals(table) && elem.getId().equals(id)) {
                return elem.getFile();
            }
        }
        return null;
    }

    public static String getNameByIdAndTable(String id, String table) {
        for (CourrierReportsEnum elem : CourrierReportsEnum.values()) {
            if (elem.getTable().equals(table) && elem.getId().equals(id)) {
                return elem.getName();
            }
        }
        return null;
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
    
    
}
