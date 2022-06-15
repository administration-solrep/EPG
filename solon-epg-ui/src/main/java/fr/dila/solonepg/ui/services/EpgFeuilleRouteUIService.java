package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.squelette.EtapeSqueletteDTO;
import fr.dila.ss.ui.bean.fdr.EtapeDTO;
import fr.dila.ss.ui.services.SSFeuilleRouteUIService;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface EpgFeuilleRouteUIService extends SSFeuilleRouteUIService {
    EtapeSqueletteDTO saveEtapeSquelette(SpecificContext context);

    List<EtapeDTO> getEtapesPourEpreuve(SpecificContext context);
}
