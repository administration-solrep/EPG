package fr.dila.solonepg.ui.th.model;

import fr.dila.solonepg.ui.services.EpgDerniersElementsComponentService;
import fr.dila.solonepg.ui.services.SelectionToolComponentService;
import fr.dila.st.ui.services.AdminMenuService;
import fr.dila.st.ui.th.annot.ActionMenu;
import fr.dila.st.ui.th.annot.Fragment;
import fr.dila.st.ui.th.annot.FragmentContainer;
import fr.dila.st.ui.th.annot.IHM;
import fr.dila.st.ui.th.model.SpecificContext;

@IHM(
    menu = { @ActionMenu(id = "MAIN_ADMIN", category = "MAIN_MENU") },
    value = {
        @FragmentContainer(
            name = "left",
            value = {
                @Fragment(
                    service = AdminMenuService.class,
                    templateFile = "fragments/leftblocks/leftMenu",
                    template = "leftMenu",
                    order = 1
                ),
                @Fragment(
                    service = SelectionToolComponentService.class,
                    templateFile = "fragments/leftblocks/selection-tool",
                    template = "selectionTool",
                    order = 2
                ),
                @Fragment(
                    service = EpgDerniersElementsComponentService.class,
                    templateFile = "fragments/derniersElementsMenu/derniersElements",
                    template = "derniersElements",
                    order = 3
                )
            }
        )
    }
)
public class EpgAdminTemplate extends EpgLayoutThTemplate {

    public EpgAdminTemplate() {
        this(null, null);
    }

    public EpgAdminTemplate(String name, SpecificContext context) {
        super(name, context);
    }
}
