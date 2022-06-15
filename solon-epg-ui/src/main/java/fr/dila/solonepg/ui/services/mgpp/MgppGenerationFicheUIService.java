package fr.dila.solonepg.ui.services.mgpp;

import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;

public interface MgppGenerationFicheUIService {
    File genererFicheXls(SpecificContext context);

    File genererFichePdf(SpecificContext context);
}
