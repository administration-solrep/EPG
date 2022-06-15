package fr.dila.solonepg.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import fr.dila.solonepg.core.test.FakeUpdateFichePresentationListener;
import fr.dila.solonepg.core.test.SolonEPGFeature;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(SolonEPGFeature.class)
@Deploy("fr.dila.solonepg.core:OSGI-INF/test-listener-contrib.xml")
public class TestDossierService extends AbstractEPGTest {
    @Inject
    private DossierServiceImpl dossierService;

    private NORService norService = mock(NORService.class);

    @Inject
    private CoreFeature coreFeature;

    @Test
    public void testUpdateDossierWhenReattribution() throws Exception {
        DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
        Dossier dossier = docModel.getAdapter(Dossier.class);
        dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_LOI);
        String oldNor = "AAAB0000001C";
        String newNor = "DDDE0000001C";
        dossier.setNumeroNor(oldNor);
        String ministere = "ministere";
        String direction = "direction";
        boolean ministereOnly = false;
        boolean reattributionNor = true;
        String norMinistere = "DDD";
        String norDirection = "E";
        boolean checkUnicity = true;
        ActiviteNormative activiteNormative = mock(ActiviteNormative.class);

        when(norService.reattributionDirectionNOR(dossier, norMinistere, norDirection)).thenReturn(newNor);
        when(activiteNormative.getAdapter(TexteMaitre.class)).thenReturn(mock(TexteMaitre.class));
        when(activiteNormative.getDocument()).thenReturn(docModel);

        dossierService.updateDossierWhenReattribution(
            ministere,
            direction,
            ministereOnly,
            reattributionNor,
            norMinistere,
            norDirection,
            norService,
            dossier,
            session,
            checkUnicity,
            activiteNormative
        );

        assertNotNull(dossier);
        assertEquals(dossier.getNumeroNor(), newNor);

        coreFeature.waitForAsyncCompletion();

        assertNotNull(FakeUpdateFichePresentationListener.events);
        assertEquals(
            oldNor,
            FakeUpdateFichePresentationListener.events.get(0).getContext().getProperties().get("nor_pre_reattribution")
        );
        assertEquals(
            SolonEpgEventConstant.TYPE_ACTE_FL_EVENT_VALUE,
            FakeUpdateFichePresentationListener.events.get(0).getContext().getProperties().get("typeActe")
        );
    }
}
