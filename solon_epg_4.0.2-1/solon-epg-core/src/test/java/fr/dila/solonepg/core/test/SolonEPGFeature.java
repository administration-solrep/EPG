package fr.dila.solonepg.core.test;

import fr.dila.solonepg.api.service.FondDeDossierModelService;
import fr.dila.solonepg.core.service.FeuilleRouteModelComponent;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.test.STAuditFeature;
import fr.dila.st.core.test.STCommonFeature;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.ComponentName;
import org.nuxeo.runtime.test.runner.BlacklistComponent;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.RunnerFeature;

@Features({ PlatformFeature.class, AutomationFeature.class, STAuditFeature.class, STCommonFeature.class })
@Deploy("fr.dila.ss.core")
@Deploy("fr.dila.solonepg.core")
@Deploy("fr.dila.solonepg.core:OSGI-INF/test-datasources-contrib.xml")
@Deploy("fr.dila.solonepg.core:OSGI-INF/test-st-computedgroups-contrib.xml")
@Deploy("fr.dila.solonepg.core:OSGI-INF/test-mockministeres-framework.xml")
@Deploy("fr.dila.solonepg.core:OSGI-INF/test-mockpostes-framework.xml")
@Deploy("fr.dila.solonepg.core:OSGI-INF/test-mockusanddirection-framework.xml")
@BlacklistComponent({ "fr.dila.st.core.datasources.contrib" })
public class SolonEPGFeature implements RunnerFeature {

    @Override
    public void initialize(FeaturesRunner runner) {
        System.setProperty("solon.web.service.eurlex.hostnames", "eur-lex.europa.eu");
        System.setProperty("solon.web.service.eurlex.proxyHost", "localhost");
        System.setProperty("solon.web.service.eurlex.proxyPort", "3128");
        System.setProperty("solon.web.service.eurlex.protocol", "HTTP");
    }

    @Override
    public void beforeSetup(FeaturesRunner runner) {
        // reactivate some components to clear their caches
        reactivateComponent(FeuilleRouteModelComponent.NAME);
        FondDeDossierModelService fondDeDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
        fondDeDossierModelService.cleanCache();
    }

    private void reactivateComponent(ComponentName componentName) {
        ComponentInstance fddmCompInst = Framework.getRuntime().getComponentInstance(componentName);
        if (fddmCompInst != null) {
            fddmCompInst.deactivate();
            fddmCompInst.activate();
        }
    }
}
