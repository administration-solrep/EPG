package fr.dila.solonepg.ui.jaxrs.webobject.ajax.suivi.pan;

import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.ss.ui.jaxrs.webobject.ajax.journal.SsJournalTechniqueAjax;
import fr.dila.ss.ui.services.SSJournalAdminUIService;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanJournalTechniqueAjax")
public class PanJournalTechniqueAjax extends SsJournalTechniqueAjax {

    @Override
    protected String getBaseUrl() {
        return "/suivi/pan/journal/technique";
    }

    @Override
    protected String getAjaxUrl() {
        return "/suivi/pan/journalTechnique/resultats";
    }

    @Override
    protected String getAjaxExportUrl() {
        return "/suivi/pan/journalTechnique/exporter";
    }

    @Override
    protected String getBreadcrumbTitle() {
        return "pan.journal.technique.title.breadcrumb";
    }

    @Override
    protected SSJournalAdminUIService getJournalAdminUIService() {
        return PanUIServiceLocator.getJournalAdminUIService();
    }
}
