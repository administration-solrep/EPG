package fr.dila.solonepg.ui.services;

import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getEpgAttenteSignatureUIService;
import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getEpgDossierCreationUIService;
import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getEpgMailboxListComponentService;
import static fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator.getEpgTraitementPapierUIService;
import static org.assertj.core.api.Assertions.assertThat;

import fr.dila.solonepg.core.test.SolonEPGFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(SolonEPGFeature.class)
@Deploy("fr.dila.st.ui")
@Deploy("fr.dila.ss.ui")
@Deploy("fr.dila.solonepg.ui")
public class EpgUIServiceLocatorIT {

    /**
     * Vérifie que les services UI sont bien instanciés/accessibles
     */
    @Test
    public void testServices() {
        assertThat(getEpgAttenteSignatureUIService()).isInstanceOf(EpgAttenteSignatureUIService.class);
        assertThat(getEpgDossierCreationUIService()).isInstanceOf(EpgDossierCreationUIService.class);
        assertThat(getEpgMailboxListComponentService()).isInstanceOf(EpgMailboxListComponentService.class);
        assertThat(getEpgTraitementPapierUIService()).isInstanceOf(EpgTraitementPapierUIService.class);
    }
}
