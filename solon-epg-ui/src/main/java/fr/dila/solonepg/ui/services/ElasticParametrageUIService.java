package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.parametres.ParametreESList;
import fr.dila.st.ui.th.model.SpecificContext;

public interface ElasticParametrageUIService {
    ParametreESList handleListResult(SpecificContext context);
}
