package fr.dila.solonepg.ui.contentview;

import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.ss.ui.bean.JournalDossierResultList;
import fr.dila.ss.ui.services.SSJournalUIService;

/**
 * page provider du journal affiche dans le PAN
 *
 *
 */
public class PanJournalAdminPageProvider extends EpgJournalAdminPageProvider {
    private static final long serialVersionUID = 1L;

    @Override
    protected SSJournalUIService<JournalDossierResultList> getJournalUIService() {
        return PanUIServiceLocator.getJournalUIService();
    }
}
