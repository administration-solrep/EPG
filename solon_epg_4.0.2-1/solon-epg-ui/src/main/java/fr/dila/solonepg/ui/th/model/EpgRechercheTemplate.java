package fr.dila.solonepg.ui.th.model;

import fr.dila.solonepg.ui.services.EpgDerniersElementsComponentService;
import fr.dila.solonepg.ui.services.EpgRechercheMenuService;
import fr.dila.solonepg.ui.services.EpgRechercheTypeMenuService;
import fr.dila.solonepg.ui.services.SelectionToolComponentService;
import fr.dila.st.ui.th.annot.ActionMenu;
import fr.dila.st.ui.th.annot.Fragment;
import fr.dila.st.ui.th.annot.FragmentContainer;
import fr.dila.st.ui.th.annot.IHM;
import fr.dila.st.ui.th.model.SpecificContext;

@IHM(
    menu = { @ActionMenu(id = "MAIN_RECHERCHE", category = "MAIN_MENU") },
    value = {
        @FragmentContainer(
            name = "left",
            value = {
                @Fragment(
                    service = EpgRechercheTypeMenuService.class,
                    templateFile = "fragments/leftblocks/leftMenu",
                    template = "leftMenu",
                    order = 1
                ),
                @Fragment(
                    service = EpgRechercheMenuService.class,
                    templateFile = "fragments/leftblocks/recherche",
                    template = "recherche",
                    order = 2
                ),
                @Fragment(
                    service = SelectionToolComponentService.class,
                    templateFile = "fragments/leftblocks/selection-tool",
                    template = "selectionTool",
                    order = 3
                ),
                @Fragment(
                    service = EpgDerniersElementsComponentService.class,
                    templateFile = "fragments/derniersElementsMenu/derniersElements",
                    template = "derniersElements",
                    order = 4
                )
            }
        )
    }
)
public class EpgRechercheTemplate extends EpgLayoutThTemplate {

    public EpgRechercheTemplate() {
        this(null, null);
    }

    public EpgRechercheTemplate(String name, SpecificContext context) {
        super(name, context);
    }
}
