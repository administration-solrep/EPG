package fr.dila.solonepg.ui.th.model;

import fr.dila.ss.ui.th.model.SSLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;

public class EpgLayoutThTemplate extends SSLayoutThTemplate {
    public static final String HAS_NOR = "hasNor";
    public static final String SUIVI_AN = "suiviAN";

    public EpgLayoutThTemplate() {
        this(null, null);
    }

    public EpgLayoutThTemplate(String name, SpecificContext context) {
        super(name, context);
    }
}
