package fr.dila.solonepg.ui.th.model;

import fr.dila.solonepg.ui.services.DossierCreationBtnComponentService;
import fr.dila.solonepg.ui.services.DossierCreationComponentService;
import fr.dila.solonepg.ui.services.EpgDerniersElementsComponentService;
import fr.dila.solonepg.ui.services.EpgMailboxListComponentService;
import fr.dila.solonepg.ui.services.SelectionToolComponentService;
import fr.dila.solonepg.ui.services.SuiviActiviteComponentService;
import fr.dila.st.ui.th.annot.ActionMenu;
import fr.dila.st.ui.th.annot.Fragment;
import fr.dila.st.ui.th.annot.FragmentContainer;
import fr.dila.st.ui.th.annot.IHM;
import fr.dila.st.ui.th.model.SpecificContext;

@IHM(
    menu = { @ActionMenu(id = "MAIN_TRAVAIL", category = "MAIN_MENU") },
    value = {
        @FragmentContainer(
            name = "left",
            value = {
                @Fragment(
                    service = DossierCreationBtnComponentService.class,
                    templateFile = "fragments/leftblocks/creation-dossier-button",
                    template = "creation-dossier-button",
                    order = 1
                ),
                @Fragment(
                    service = EpgMailboxListComponentService.class,
                    templateFile = "fragments/leftblocks/mailboxList",
                    template = "mailboxList",
                    order = 2
                ),
                @Fragment(
                    service = SelectionToolComponentService.class,
                    templateFile = "fragments/leftblocks/selection-tool",
                    template = "selectionTool",
                    order = 3
                ),
                @Fragment(
                    service = DossierCreationComponentService.class,
                    templateFile = "fragments/leftblocks/dossierCreationList",
                    template = "dossierCreationList",
                    order = 4
                ),
                @Fragment(
                    service = SuiviActiviteComponentService.class,
                    templateFile = "fragments/leftblocks/suiviActivite",
                    template = "suiviActivite",
                    order = 5
                ),
                @Fragment(
                    service = EpgDerniersElementsComponentService.class,
                    templateFile = "fragments/derniersElementsMenu/derniersElements",
                    template = "derniersElements",
                    order = 6
                )
            }
        )
    }
)
public class EpgTravailTemplate extends EpgLayoutThTemplate {

    public EpgTravailTemplate() {
        this(null, null);
    }

    public EpgTravailTemplate(String name, SpecificContext context) {
        super(name, context);
    }
}
