package fr.dila.solonepg.ui.services.actions.suggestion.numeronor;

import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class NumeroNorSuggestionProviderComponent
    extends ServiceEncapsulateComponent<NumeroNorSuggestionProviderService, NumeroNorSuggestionProviderServiceImpl> {

    /**
     * Default constructor
     */
    public NumeroNorSuggestionProviderComponent() {
        super(NumeroNorSuggestionProviderService.class, new NumeroNorSuggestionProviderServiceImpl());
    }
}
