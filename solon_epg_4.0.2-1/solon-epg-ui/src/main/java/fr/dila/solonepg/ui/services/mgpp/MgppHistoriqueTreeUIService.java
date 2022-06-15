package fr.dila.solonepg.ui.services.mgpp;

import fr.dila.st.ui.bean.DossierHistoriqueEPP;
import fr.dila.st.ui.th.model.SpecificContext;

public interface MgppHistoriqueTreeUIService {
    DossierHistoriqueEPP getHistoriqueEPP(SpecificContext context);

    DossierHistoriqueEPP getHistoriqueEPPFiche(SpecificContext context);
}
