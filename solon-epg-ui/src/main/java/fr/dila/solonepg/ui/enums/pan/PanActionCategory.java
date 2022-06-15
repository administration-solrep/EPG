package fr.dila.solonepg.ui.enums.pan;

import fr.dila.st.ui.enums.ActionCategory;

public enum PanActionCategory implements ActionCategory {
    EXPORT_ACTIONS,
    FIRST_TABLE_ACTIONS,
    FIRST_TABLE_LINE_ACTIONS,
    MINISTERE_TAB,
    SECOND_TABLE_ACTIONS,
    SECOND_TABLE_LINE_ACTIONS,
    SUBTAB,
    SUBTAB_TOOLBAR_LEFT,
    SUBTAB_TOOLBAR_RIGHT,
    TAB,
    TAB_TOOLBAR_RIGHT,
    TEXTE_MAITRE_ADD,
    TEXTEM_TAB,
    TEXTEM_ACTIONS_LOCK;

    @Override
    public String getName() {
        return name();
    }
}
