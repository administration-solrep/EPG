package fr.dila.solonmgpp;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.SimpleFeature;
import org.nuxeo.ecm.core.test.CoreFeature;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
public class FicheLoiActionBeanTest {

	@Inject
	CoreSession	session;

	@Test
	public void ChargeMissionTest() {
		Assert.assertNotNull(session);
	}
}
