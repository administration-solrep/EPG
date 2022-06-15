package fr.dila.solonepg.ui.enums.mgpp;

import fr.dila.st.ui.enums.UserSessionKey;

public enum MgppUserSessionKey implements UserSessionKey {
    DOSSIER_PARLEMENTAIRE,
    EVENEMENT_DTO,
    MESSAGE_DTO;

    @Override
    public String getName() {
        return name();
    }
}
