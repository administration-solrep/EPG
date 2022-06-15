package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.th.bean.ParametreApplicationForm;
import fr.dila.st.ui.th.model.SpecificContext;

public interface ParametreApplicationUIService {
    ParametreApplicationForm getParametreApplication(SpecificContext context);

    void save(SpecificContext context);
}
