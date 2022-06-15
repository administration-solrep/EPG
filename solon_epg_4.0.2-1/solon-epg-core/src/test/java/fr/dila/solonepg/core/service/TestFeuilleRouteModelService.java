package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Test;

public class TestFeuilleRouteModelService extends AbstractEPGTest {
    @Inject
    private FeuilleRouteModelService feuilleRouteModelService;

    @Test
    public void testContrib() {
        Assert.assertNotNull(feuilleRouteModelService);
    }
}
