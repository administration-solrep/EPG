package fr.dila.solonepg.ui.services.mgpp;

import fr.dila.solonepg.ui.bean.MgppRechercheDynamique;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.st.ui.th.model.SpecificContext;

public interface MgppRechercheUIService {
    MgppRechercheDynamique getCriteresRechercheDynamique(SpecificContext context);

    CritereRechercheDTO buildCriteresForRechercheAvancee(SpecificContext context);
}
