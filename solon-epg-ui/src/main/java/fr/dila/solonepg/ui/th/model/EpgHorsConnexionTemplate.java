package fr.dila.solonepg.ui.th.model;

import fr.dila.solonepg.ui.services.EpgSuiviLibreFooterService;
import fr.dila.st.ui.services.BreadcrumbsService;
import fr.dila.st.ui.services.FooterService;
import fr.dila.st.ui.th.annot.Fragment;
import fr.dila.st.ui.th.annot.FragmentContainer;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;

@FragmentContainer(
    name = "breadcrumbs",
    value = {
        @Fragment(
            service = BreadcrumbsService.class,
            templateFile = "fragments/breadcrumbsblocks/breadcrumbsLeft",
            template = "breadcrumbsLeft",
            order = 1
        )
    }
)
@FragmentContainer(
    name = "footer",
    value = {
        @Fragment(
            service = EpgSuiviLibreFooterService.class,
            templateFile = "fragments/suiviLibre/footerSuiviLibre",
            template = "footerSuiviLibre",
            order = 1
        ),
        @Fragment(
            service = FooterService.class,
            templateFile = "fragments/footerblocks/footerInfo",
            template = "footerInfo",
            order = 2
        )
    }
)
public class EpgHorsConnexionTemplate extends ThTemplate {

    public EpgHorsConnexionTemplate() {
        this(null, null);
    }

    public EpgHorsConnexionTemplate(String name, SpecificContext context) {
        super(name, context);
        this.setLayout("pageLayout");
    }
}
