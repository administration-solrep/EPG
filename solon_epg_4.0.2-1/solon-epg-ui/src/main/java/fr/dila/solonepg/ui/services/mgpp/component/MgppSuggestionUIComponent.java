package fr.dila.solonepg.ui.services.mgpp.component;

import fr.dila.solonepg.ui.services.mgpp.MgppSuggestionUIService;
import fr.dila.solonepg.ui.services.mgpp.impl.MgppSuggestionUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class MgppSuggestionUIComponent
    extends ServiceEncapsulateComponent<MgppSuggestionUIService, MgppSuggestionUIServiceImpl> {

    public MgppSuggestionUIComponent() {
        super(MgppSuggestionUIService.class, new MgppSuggestionUIServiceImpl());
    }
}
