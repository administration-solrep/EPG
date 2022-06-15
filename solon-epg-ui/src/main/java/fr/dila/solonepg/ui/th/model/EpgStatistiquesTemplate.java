package fr.dila.solonepg.ui.th.model;

import fr.dila.st.ui.th.annot.ActionMenu;
import fr.dila.st.ui.th.annot.IHM;
import fr.dila.st.ui.th.model.SpecificContext;

@IHM(menu = { @ActionMenu(id = "MAIN_STATS", category = "MAIN_MENU") })
public class EpgStatistiquesTemplate extends EpgLayoutThTemplate {

    public EpgStatistiquesTemplate() {
        this(null, null);
    }

    public EpgStatistiquesTemplate(String name, SpecificContext context) {
        super(name, context);
    }
}
