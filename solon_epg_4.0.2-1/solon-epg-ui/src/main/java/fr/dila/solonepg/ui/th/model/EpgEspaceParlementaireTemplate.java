package fr.dila.solonepg.ui.th.model;

import fr.dila.st.ui.th.annot.ActionMenu;
import fr.dila.st.ui.th.annot.FragmentContainer;
import fr.dila.st.ui.th.annot.IHM;
import fr.dila.st.ui.th.model.SpecificContext;

@IHM(
    menu = { @ActionMenu(id = "main_parlementaire", category = "MAIN_MENU") },
    value = { @FragmentContainer(name = "left", value = {}) }
)
public class EpgEspaceParlementaireTemplate extends EpgLayoutThTemplate {

    public EpgEspaceParlementaireTemplate() {
        this(null, null);
    }

    public EpgEspaceParlementaireTemplate(String name, SpecificContext context) {
        super(name, context);
    }
}
