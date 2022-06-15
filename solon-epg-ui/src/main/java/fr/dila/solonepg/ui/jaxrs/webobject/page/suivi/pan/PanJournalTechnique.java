package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan;

import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.ss.ui.bean.JournalDossierResultList;
import fr.dila.ss.ui.jaxrs.webobject.page.journal.SsJournalTechnique;
import fr.dila.ss.ui.services.SSJournalUIService;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanJournalTechnique")
public class PanJournalTechnique extends SsJournalTechnique {

    @Override
    protected SSJournalUIService<JournalDossierResultList> getJournalUIService() {
        return PanUIServiceLocator.getJournalUIService();
    }

    @Override
    protected String getTemplateName() {
        return "pages/suivi/panJournalTechnique";
    }

    @Override
    protected String getAjax() {
        return "PanJournalTechniqueAjax";
    }
}
