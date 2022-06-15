package fr.dila.solonepg.ui.jaxrs.webobject.page.suiviLibre;

import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "ApplicationOrdonnances")
public class EpgSuiviLibreApplicationOrdonnances extends AbstractEpgSuiviLibreApplication {

    public EpgSuiviLibreApplicationOrdonnances() {
        super();
    }

    @Override
    String getApplicationName() {
        return "applicationOrdonnances";
    }

    @Override
    String getTypeActe() {
        return "ordonnances";
    }
}
