package fr.dila.solonepg.ui.th.model.mgpp;

import fr.dila.solonepg.ui.services.mgpp.MgppDossierParlementaireMenuService;
import fr.dila.st.ui.th.annot.ActionMenu;
import fr.dila.st.ui.th.annot.Fragment;
import fr.dila.st.ui.th.annot.FragmentContainer;
import fr.dila.st.ui.th.annot.IHM;
import fr.dila.st.ui.th.model.SpecificContext;

@IHM(
    menu = {
        @ActionMenu(id = "main_espaceparlementaire", category = "MAIN_MENU"),
        @ActionMenu(id = "MGPP_DOSSIERS", category = "MGPP_SUBMENU")
    },
    value = {
        @FragmentContainer(
            name = "left",
            value = {
                @Fragment(
                    service = MgppDossierParlementaireMenuService.class,
                    templateFile = "fragments/leftblocks/mgpp-dossierParlementaireMenu",
                    template = "corbeille",
                    order = 1
                )
            }
        )
    }
)
public class MgppDossierParlementaireTemplate extends MgppEspaceParlementaireTemplate {

    public MgppDossierParlementaireTemplate() {
        this(null, null);
    }

    public MgppDossierParlementaireTemplate(String name, SpecificContext context) {
        super(name, context);
    }
}
