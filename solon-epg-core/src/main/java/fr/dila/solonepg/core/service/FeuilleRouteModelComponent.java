package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentName;
import org.nuxeo.runtime.model.DefaultComponent;

public class FeuilleRouteModelComponent extends DefaultComponent {
    public static final ComponentName NAME = new ComponentName(
        "fr.dila.solonepg.core.service.FeuilleRouteModelComponent"
    );

    private FeuilleRouteModelServiceImpl feuilleRouteModelService;

    public FeuilleRouteModelComponent() {
        super();
    }

    @Override
    public void activate(ComponentContext context) {
        super.activate(context);
        feuilleRouteModelService = new FeuilleRouteModelServiceImpl();
    }

    @Override
    public void deactivate(ComponentContext context) {
        super.deactivate(context);
        feuilleRouteModelService = null;
    }

    @Override
    public <T> T getAdapter(Class<T> clazz) {
        if (clazz.isAssignableFrom(FeuilleRouteModelService.class)) {
            return clazz.cast(feuilleRouteModelService);
        }
        return null;
    }
}
