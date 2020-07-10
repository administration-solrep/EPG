package fr.dila.solonepg.web.activitenormative;

import java.util.HashMap;
import java.util.Map;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;

public enum EspaceActiviteNormativeTabEnum {
    // NOM("id", CONTENT_VIEW_MENU_0, CONTENT_VIEW_MENU_1, CONTENT_VIEW_MENU_2, CONTENT_VIEW_MENU_3);
    TAB_AN_TABLEAU_MAITRE("TAB_AN_TABLEAU_MAITRE",
                          ActiviteNormativeConstants.ACTIVITE_NORMATIVE_TABLEAU_MAITRE_APPLICATION,
                          ActiviteNormativeConstants.ACTIVITE_NORMATIVE_TABLEAU_MAITRE_ORDONNANCES,
                          ActiviteNormativeConstants.ACTIVITE_NORMATIVE_TABLEAU_MAITRE_TRAITES,
                          ActiviteNormativeConstants.ACTIVITE_NORMATIVE_TABLEAU_MAITRE_TRANSPOSITION,
                          ActiviteNormativeConstants.ACTIVITE_NORMATIVE_TABLEAU_MAITRE_ORDONNANCES_38C,
                          ActiviteNormativeConstants.ACTIVITE_NORMATIVE_TABLEAU_MAITRE_APP_ORDONNANCES),
    TAB_AN_TABLEAU_BORD("TAB_AN_TABLEAU_BORD", "", "", "", "", "", ""),
    TAB_AN_STATISTIQUES("TAB_AN_STATISTIQUES", "", "", "", "", "", ""),
    TAB_AN_INDICATEUR_LOLF("TAB_AN_INDICATEUR_LOLF", "", "", "", "", "", ""),
    TAB_AN_BILAN_SEMESTRIEL("TAB_AN_BILAN_SEMESTRIEL", "", "", "", "", "", ""),
    TAB_AN_TAUX_APPLICATION("TAB_AN_TAUX_APPLICATION", "", "", "", "", "", ""),
    TAB_AN_DELAIS_MOYEN("TAB_AN_DELAIS_MOYEN", "", "", "", "", "", "");

    private String id;
    private Map<String, String> contentView;

    EspaceActiviteNormativeTabEnum(String id, String contentViewLoi, String contentViewOrdonnances, String contentViewTraites,
            String contentViewTransposition, String contentViewOrdonnances38C, String contentViewAppOrdonnances) {
        this.id = id;
        contentView = new HashMap<String, String>();
        contentView.put(ActiviteNormativeConstants.MENU_0, contentViewLoi);
        contentView.put(ActiviteNormativeConstants.MENU_1, contentViewOrdonnances);
        contentView.put(ActiviteNormativeConstants.MENU_2, contentViewTraites);
        contentView.put(ActiviteNormativeConstants.MENU_3, contentViewTransposition);
        contentView.put(ActiviteNormativeConstants.MENU_4, contentViewOrdonnances38C);
        contentView.put(ActiviteNormativeConstants.MENU_5, contentViewAppOrdonnances);
    }

    protected void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static String getContentViewName(String id, ActiviteNormativeEnum menu) {
        EspaceActiviteNormativeTabEnum tab = getById(id);
        if (tab != null && menu != null) {
            return tab.contentView.get(menu.getMenuIndex());
        }
        return null;
    }

    public static EspaceActiviteNormativeTabEnum getById(String id) {
        for (EspaceActiviteNormativeTabEnum elem : EspaceActiviteNormativeTabEnum.values()) {
            if (elem.getId().equals(id)) {
                return elem;
            }
        }
        return null;
    }

}
