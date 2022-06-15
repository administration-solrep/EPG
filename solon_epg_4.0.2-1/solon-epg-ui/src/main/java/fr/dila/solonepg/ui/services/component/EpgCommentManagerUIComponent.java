package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.EpgCommentManagerUIService;
import fr.dila.solonepg.ui.services.impl.DossierCreationComponentServiceImpl;
import fr.dila.solonepg.ui.services.impl.EpgCommentManagerUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EpgCommentManagerUIComponent
    extends ServiceEncapsulateComponent<EpgCommentManagerUIService, EpgCommentManagerUIServiceImpl> {

    /**
     * Default constructor
     */
    public EpgCommentManagerUIComponent() {
        super(EpgCommentManagerUIService.class, new EpgCommentManagerUIServiceImpl());
    }
}
