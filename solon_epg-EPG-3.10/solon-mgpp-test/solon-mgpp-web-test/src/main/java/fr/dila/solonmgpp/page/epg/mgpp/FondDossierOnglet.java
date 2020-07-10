package fr.dila.solonmgpp.page.epg.mgpp;

import fr.dila.solonmgpp.page.commun.CommonWebPage;

public class FondDossierOnglet extends CommonWebPage {

    @Override
    protected String getAddFileInput() {
        return "//*[contains(@id, 'editFileFormFP') and contains(@id, 'FilesFP:file') and not(contains(@id, 'Items'))]";
    }

    @Override
    protected String getAddFile() {
        return "//*[contains(@id, 'editFileFormFP') and contains(@id, 'ButtonImage')]/img";
    }

}
