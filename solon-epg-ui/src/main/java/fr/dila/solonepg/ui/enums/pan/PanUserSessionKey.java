package fr.dila.solonepg.ui.enums.pan;

import fr.dila.st.ui.enums.UserSessionKey;

public enum PanUserSessionKey implements UserSessionKey {
    FILTERS,
    JSON_SEARCH_HISTORIQUE_MAJ,
    JSON_SEARCH_RECHERCHE_EXPERTE,
    JSON_SEARCH_STATISTIQUES_PUBLICATION,
    JSON_SEARCH_TABLEAU_LOIS,
    JSON_SEARCH_TABLEAU_MAITRE,
    JSON_SEARCH_TABLEAU_MAITRE_MINISTERES,
    ONGLET,
    PAGINATE_FORM,
    SECOND_TABLE,
    SECTION,
    TAB_SORT_PAGINATE_FORM,
    THIRD_TABLE;

    @Override
    public String getName() {
        return name();
    }
}
