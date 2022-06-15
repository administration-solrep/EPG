package fr.dila.solonepg.ui.services;

import fr.dila.ss.ui.th.bean.DossierMailForm;
import fr.dila.st.ui.th.model.SpecificContext;

public interface EpgArchiveUIService {
    /**
     * Export zip d'un dossier avec son fond de dossier
     *
     */
    void exportFdd(SpecificContext context);

    /**
     * Export zip d'un dossier avec son fond de dossier
     *
     */
    void exportParapheur(SpecificContext context);

    DossierMailForm getDossierMailForm(SpecificContext context);

    /**
     * Export zip d'un dossier
     *
     */
    void exportDossier(SpecificContext context);
}
