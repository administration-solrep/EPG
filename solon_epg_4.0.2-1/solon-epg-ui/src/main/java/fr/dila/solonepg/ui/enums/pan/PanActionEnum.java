package fr.dila.solonepg.ui.enums.pan;

import fr.dila.st.ui.enums.ActionEnum;

public enum PanActionEnum implements ActionEnum {
    ADD_ELT_PREMIER_TABLEAU_NOR,
    ADD_SECOND_ELT_NOR,
    HIDE_COLUMNS_MESURES,
    HISTORIQUE_DELETE,
    MAJ_MIN_DELETE_ALL,
    MAJ_MIN_DELETE_SELECTION,
    PAN_BIRT_PUBLISH,
    PAN_BIRT_REFRESH,
    PARAMETRAGE_STATISTIQUES,
    RELOAD_DECRETS,
    TAB_TABLEAU_MAITRE_MINISTERES,
    TEXTE_MAITRE_ADD,
    TEXTE_MAITRE_DELETE,
    TEXTE_MAITRE_DELETE_RECHERCHE_AN;

    @Override
    public String getName() {
        return name();
    }
}
