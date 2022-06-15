package fr.dila.solonepg.ui.jaxrs.webobject.page.suiviLibre;

import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "SuiviOrdonnances")
public class EpgSuiviLibreSuiviOrdonnances extends AbstractEpgSuiviLibreApplication {

    public EpgSuiviLibreSuiviOrdonnances() {
        super();
    }

    @Override
    String getApplicationName() {
        return "suiviOrdonnances";
    }

    @Override
    String getTypeActe() {
        return "habilitations";
    }

    @Override
    boolean treatsLegis(Boolean legis) {
        return true;
    }

    @Override
    boolean canConsultMin() {
        return true;
    }
}
