package fr.dila.solonepg.ui.services.mgpp;

import fr.dila.solonepg.ui.th.model.bean.MgppParamForm;
import fr.dila.st.ui.th.model.SpecificContext;

public interface MgppAdminUIService {
    MgppParamForm getMgppParameters(SpecificContext context);

    void saveMgppParameters(SpecificContext context);
}
