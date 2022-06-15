package fr.dila.solonepg.ui.th.model;

import fr.dila.solonepg.ui.services.EpgDerniersElementsComponentService;
import fr.dila.solonepg.ui.services.EpgSuiviMenuService;
import fr.dila.solonepg.ui.services.SelectionToolComponentService;
import fr.dila.st.ui.th.annot.ActionMenu;
import fr.dila.st.ui.th.annot.Fragment;
import fr.dila.st.ui.th.annot.FragmentContainer;
import fr.dila.st.ui.th.annot.IHM;
import fr.dila.st.ui.th.model.SpecificContext;

@IHM(
    menu = { @ActionMenu(id = "MAIN_SUIVI", category = "MAIN_MENU") },
    value = {
        @FragmentContainer(
            name = "left",
            value = {
                @Fragment(
                    service = EpgSuiviMenuService.class,
                    templateFile = "fragments/leftblocks/leftMenuSuivi",
                    template = "leftMenuSuivi",
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
public class EpgSuiviTemplate extends EpgLayoutThTemplate {

    public EpgSuiviTemplate() {
        this(null, null);
    }

    public EpgSuiviTemplate(String name, SpecificContext context) {
        super(name, context);
    }
}
