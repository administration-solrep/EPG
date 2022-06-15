package fr.dila.solonepg.ui.th.model.mgpp;

import fr.dila.solonepg.ui.th.model.EpgLayoutThTemplate;
import fr.dila.st.ui.th.annot.ActionMenu;
import fr.dila.st.ui.th.annot.FragmentContainer;
import fr.dila.st.ui.th.annot.IHM;
import fr.dila.st.ui.th.model.SpecificContext;

@IHM(
    menu = { @ActionMenu(id = "main_espaceparlementaire", category = "MAIN_MENU") },
    value = { @FragmentContainer(name = "left", value = {}) }
)
public class MgppEspaceParlementaireTemplate extends EpgLayoutThTemplate {

    public MgppEspaceParlementaireTemplate() {
        this(null, null);
    }

    public MgppEspaceParlementaireTemplate(String name, SpecificContext context) {
        super(name, context);
    }
}
