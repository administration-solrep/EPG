package fr.dila.solonepg.ui.jaxrs.webobject.page.suiviLibre;

import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "ApplicationLois")
public class EpgSuiviLibreApplicationLois extends AbstractEpgSuiviLibreApplication {

    public EpgSuiviLibreApplicationLois() {
        super();
    }

    @Override
    String getApplicationName() {
        return "applicationLois";
    }

    @Override
    String getTypeActe() {
        return "lois";
    }
}
