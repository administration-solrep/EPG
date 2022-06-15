package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.ordonnances;

import fr.dila.solonepg.core.dto.activitenormative.FicheSignaletiqueApplicationOrdonnanceDTO;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.PanAbstractSousOngletContextuel;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanOrdonnancesFichesignaletique")
public class PanOrdonnancesFichesignaletique extends PanAbstractSousOngletContextuel {

    public PanOrdonnancesFichesignaletique() {
        super();
    }

    @Override
    public void loadSubtabContent() {
        FicheSignaletiqueApplicationOrdonnanceDTO ficheSignaletique = PanUIServiceLocator
            .getPanUIService()
            .getCurrentFicheSignaletiqueApplicationOrdonnance(context);
        template.getData().put("fiche", ficheSignaletique);
    }
}
