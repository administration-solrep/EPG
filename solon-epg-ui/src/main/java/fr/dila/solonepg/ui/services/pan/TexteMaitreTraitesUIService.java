package fr.dila.solonepg.ui.services.pan;

import fr.dila.solonepg.api.dto.TexteMaitreTraiteDTO;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreTraiteDTOImpl;
import fr.dila.st.ui.th.model.SpecificContext;

public interface TexteMaitreTraitesUIService {
    String saveTexteMaître(SpecificContext context);

    TexteMaitreTraiteDTO getCurrentTexteMaitre(SpecificContext context);

    TexteMaitreTraiteDTOImpl.RatificationTraite getCurrentRatification(SpecificContext context);

    TexteMaitreTraiteDTOImpl.DecretTraite getCurrentDecret(SpecificContext context);
}
