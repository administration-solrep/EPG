package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.ordonnances;

import com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.lois.PanLoisTextemaitre;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanOrdonnancesTextemaitre")
public class PanOrdonnancesTextemaitre extends PanLoisTextemaitre {
    private static final ImmutableMap<String, String> FONDEMENT_CONSTITUTIONNEL_VALUES = ImmutableMap.of(
        "true",
        "Article 38 C",
        "false",
        "Article 74-1"
    );

    @Override
    public void loadSubtabContent() {
        super.loadSubtabContent();
        template.getData().put("fondementConstitutionnelValues", FONDEMENT_CONSTITUTIONNEL_VALUES);
    }

    public PanOrdonnancesTextemaitre() {
        super();
    }
}
