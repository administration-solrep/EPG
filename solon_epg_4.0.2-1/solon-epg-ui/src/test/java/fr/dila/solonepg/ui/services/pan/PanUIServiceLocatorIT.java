package fr.dila.solonepg.ui.services.pan;

import static fr.dila.solonepg.ui.services.pan.PanUIServiceLocator.getActiviteNormativeParamStatsUIService;
import static fr.dila.solonepg.ui.services.pan.PanUIServiceLocator.getActiviteNormativeStatsUIService;
import static fr.dila.solonepg.ui.services.pan.PanUIServiceLocator.getActiviteNormativeTraitesUIService;
import static fr.dila.solonepg.ui.services.pan.PanUIServiceLocator.getExportActiviteNormativeStatsUIService;
import static fr.dila.solonepg.ui.services.pan.PanUIServiceLocator.getHistoriqueMajMinUIService;
import static fr.dila.solonepg.ui.services.pan.PanUIServiceLocator.getPanTreeUIService;
import static fr.dila.solonepg.ui.services.pan.PanUIServiceLocator.getPanUIService;
import static fr.dila.solonepg.ui.services.pan.PanUIServiceLocator.getTableauProgrammation38CUIService;
import static fr.dila.solonepg.ui.services.pan.PanUIServiceLocator.getTableauProgrammationUIService;
import static fr.dila.solonepg.ui.services.pan.PanUIServiceLocator.getTexteMaitreHabilitationUIService;
import static fr.dila.solonepg.ui.services.pan.PanUIServiceLocator.getTexteMaitreOrdonnanceUIService;
import static fr.dila.solonepg.ui.services.pan.PanUIServiceLocator.getTexteMaitreTraitesUIService;
import static fr.dila.solonepg.ui.services.pan.PanUIServiceLocator.getTexteMaitreUIService;
import static fr.dila.solonepg.ui.services.pan.PanUIServiceLocator.getTranspositionDirectiveUIService;
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
public class PanUIServiceLocatorIT {

    /**
     * Vérifie que les services UI sont bien instanciés/accessibles
     */
    @Test
    public void testServices() {
        assertThat(getPanTreeUIService()).isInstanceOf(PanTreeUIService.class);
        assertThat(getTableauProgrammationUIService()).isInstanceOf(TableauProgrammationUIService.class);
        assertThat(getPanUIService()).isInstanceOf(PanUIService.class);
        assertThat(getActiviteNormativeStatsUIService()).isInstanceOf(ActiviteNormativeStatsUIService.class);
        assertThat(getActiviteNormativeTraitesUIService()).isInstanceOf(ActiviteNormativeTraitesUIService.class);
        assertThat(getActiviteNormativeParamStatsUIService()).isInstanceOf(ActiviteNormativeParamStatsUIService.class);
        assertThat(getTexteMaitreUIService()).isInstanceOf(TexteMaitreUIService.class);
        assertThat(getHistoriqueMajMinUIService()).isInstanceOf(HistoriqueMajMinUIService.class);
        assertThat(getExportActiviteNormativeStatsUIService())
            .isInstanceOf(ExportActiviteNormativeStatsUIService.class);
        assertThat(getTableauProgrammation38CUIService()).isInstanceOf(TableauProgrammationCUIService.class);
        assertThat(getTexteMaitreHabilitationUIService()).isInstanceOf(TexteMaitreHabilitationUIService.class);
        assertThat(getTexteMaitreOrdonnanceUIService()).isInstanceOf(TexteMaitreOrdonnanceUIService.class);
        assertThat(getTexteMaitreTraitesUIService()).isInstanceOf(TexteMaitreTraitesUIService.class);
        assertThat(getTranspositionDirectiveUIService()).isInstanceOf(TranspositionDirectiveUIService.class);
    }
}
