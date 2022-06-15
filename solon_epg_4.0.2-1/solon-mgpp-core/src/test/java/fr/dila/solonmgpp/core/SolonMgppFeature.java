package fr.dila.solonmgpp.core;

import fr.dila.solonepg.core.test.SolonEPGFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.RunnerFeature;

@Features({ SolonEPGFeature.class })
@Deploy("fr.dila.solonmgpp.core")
@Deploy("org.nuxeo.template.manager:OSGI-INF/core-types-contrib.xml")
public class SolonMgppFeature implements RunnerFeature {}
