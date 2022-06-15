package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.lois;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.core.dto.activitenormative.FicheSignaletiqueLoiDTO;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.PanAbstractSousOngletContextuel;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanLoisFichesignaletique")
public class PanLoisFichesignaletique extends PanAbstractSousOngletContextuel {

    public PanLoisFichesignaletique() {
        super();
    }

    @Override
    public void loadSubtabContent() {
        ActiviteNormative activiteNormative = context.getCurrentDocument().getAdapter(ActiviteNormative.class);
        FicheSignaletiqueLoiDTO ficheSignaletique = new FicheSignaletiqueLoiDTO(
            activiteNormative,
            context.getSession()
        );
        template.getData().put("fiche", ficheSignaletique);
    }
}
