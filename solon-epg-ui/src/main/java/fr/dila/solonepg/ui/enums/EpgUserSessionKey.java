package fr.dila.solonepg.ui.enums;

import fr.dila.st.ui.enums.UserSessionKey;

public enum EpgUserSessionKey implements UserSessionKey {
    BIRT_REPORT_PARAMS,
    CRITERE_RECHERCHE_KEY,
    DOSSIER_ARCHIVAGE_STAT_PP,
    DOSSIER_LIST_FORM,
    DOSSIER_SIMILAIRE_PP,
    EPG_FAVORIS_RECHERCHE_MODELE_FDR_FORM,
    FAVORIS_RECHERCHE_TYPE,
    INJECTIONS,
    INJECTIONS_MODIFICATIONS,
    IS_ETAPE_FILTRE_CE,
    LIST_SIGNATAIRE_LIBRE,
    MAP_KEY_INJECTIONS_NEW,
    MAP_KEY_INJECTIONS_OLD,
    RECTIFICATIF,
    STATS_FORM_PARAMS,
    TD_CRITERES_RECH_LIBRE,
    USER_SEARCH_FORM;

    @Override
    public String getName() {
        return name();
    }
}
