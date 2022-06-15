package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.EpgUserList;
import fr.dila.st.ui.th.model.SpecificContext;

public interface EpgUserListUIService {
    EpgUserList getFavorisConsultationUsers(SpecificContext context);

    EpgUserList getDernierUserConsulte(SpecificContext context);
}
