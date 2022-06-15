package fr.dila.solonepg.ui.services.mgpp;

import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getEvenementActionService;
import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getMgppAdminUIService;
import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getMgppCorbeilleUIService;
import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getMgppDossierUIService;
import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getMgppEvenementDetailsUIservice;
import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getMgppFicheUIService;
import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getMgppFondDossierUIService;
import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getMgppGenerationFicheUIService;
import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getMgppHistoriqueTreeUIService;
import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getMgppMetadonneeUIService;
import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getMgppSelectValueUIService;
import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getMgppSuggestionUIService;
import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getMgppTableReferenceUIService;
import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getModeleCourrierUIService;
import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getNotificationUIService;
import static fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator.getRechercheUIService;
import static org.assertj.core.api.Assertions.assertThat;

import fr.dila.solonepg.core.test.SolonEPGFeature;
import fr.dila.st.ui.services.NotificationUIService;
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
public class MgppUIServiceLocatorIT {

    /**
     * Vérifie que les services UI sont bien instanciés/accessibles
     */
    @Test
    public void testServices() {
        assertThat(getMgppCorbeilleUIService()).isInstanceOf(MgppCorbeilleUIService.class);
        assertThat(getMgppEvenementDetailsUIservice()).isInstanceOf(MgppEvenementDetailsUIService.class);
        assertThat(getMgppAdminUIService()).isInstanceOf(MgppAdminUIService.class);
        assertThat(getMgppHistoriqueTreeUIService()).isInstanceOf(MgppHistoriqueTreeUIService.class);
        assertThat(getMgppSelectValueUIService()).isInstanceOf(MgppSelectValueUIService.class);
        assertThat(getMgppSuggestionUIService()).isInstanceOf(MgppSuggestionUIService.class);
        assertThat(getMgppMetadonneeUIService()).isInstanceOf(MgppMetadonneeUIService.class);
        assertThat(getModeleCourrierUIService()).isInstanceOf(ModeleCourrierUIService.class);
        assertThat(getMgppDossierUIService()).isInstanceOf(MgppDossierUIService.class);
        assertThat(getMgppFicheUIService()).isInstanceOf(MgppFicheUIService.class);
        assertThat(getEvenementActionService()).isInstanceOf(MgppEvenementActionUIService.class);
        assertThat(getNotificationUIService()).isInstanceOf(NotificationUIService.class);
        assertThat(getRechercheUIService()).isInstanceOf(MgppRechercheUIService.class);
        assertThat(getMgppTableReferenceUIService()).isInstanceOf(MgppTableReferenceUIService.class);
        assertThat(getMgppGenerationFicheUIService()).isInstanceOf(MgppGenerationFicheUIService.class);
        assertThat(getMgppFondDossierUIService()).isInstanceOf(MgppFondDossierUIService.class);
    }
}
