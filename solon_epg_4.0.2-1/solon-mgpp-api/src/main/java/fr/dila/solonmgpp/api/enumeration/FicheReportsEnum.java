package fr.dila.solonmgpp.api.enumeration;

import fr.dila.solonmgpp.api.constant.SolonMgppFicheConstant;

/**
 * Enumération des rapports et fiches présentation
 *
 */
public enum FicheReportsEnum {
    FICHE_AVIS_NOMINATION(
        SolonMgppFicheConstant.FICHE_AVIS_NOMINATION_NAME,
        SolonMgppFicheConstant.FICHE_AVIS_NOMINATION
    ),
    FICHE_DECRET_PR(SolonMgppFicheConstant.FICHE_DECRET_PR_NAME, SolonMgppFicheConstant.FICHE_DECRET_PR),
    FICHE_IE(SolonMgppFicheConstant.FICHE_IE_NAME, SolonMgppFicheConstant.FICHE_IE),
    FICHE_LOI(SolonMgppFicheConstant.FICHE_LOI_NAME, SolonMgppFicheConstant.FICHE_LOI),
    FICHE_OEP(SolonMgppFicheConstant.FICHE_OEP_NAME, SolonMgppFicheConstant.FICHE_OEP),
    FICHE_RAPPORT(SolonMgppFicheConstant.FICHE_RAPPORT_NAME, SolonMgppFicheConstant.FICHE_RAPPORT),
    FICHE_RESOLUTION(SolonMgppFicheConstant.FICHE_RESOLUTION_NAME, SolonMgppFicheConstant.FICHE_RESOLUTION),
    FICHE_DECLARATION_GENERALE(SolonMgppFicheConstant.FICHE_DPG_NAME, SolonMgppFicheConstant.FICHE_DPG),
    FICHE_DOC(SolonMgppFicheConstant.FICHE_DOC_NAME, SolonMgppFicheConstant.FICHE_DOC),
    FICHE_SD(SolonMgppFicheConstant.FICHE_SD_NAME, SolonMgppFicheConstant.FICHE_SD),
    FICHE_JSS(SolonMgppFicheConstant.FICHE_JSS_NAME, SolonMgppFicheConstant.FICHE_JSS),
    FICHE_AUD(SolonMgppFicheConstant.FICHE_AUD_NAME, SolonMgppFicheConstant.FICHE_AUD);

    private final String name;
    private final String file;

    FicheReportsEnum(String name, String file) {
        this.name = name;
        this.file = file;
    }

    public String getId() {
        return name().toLowerCase();
    }

    public String getFile() {
        return file;
    }

    public String getName() {
        return name;
    }
}
