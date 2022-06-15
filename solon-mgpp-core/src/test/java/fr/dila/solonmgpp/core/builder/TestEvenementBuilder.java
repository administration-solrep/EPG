package fr.dila.solonmgpp.core.builder;

import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.service.EvenementService;
import fr.dila.solonmgpp.core.SolonMgppFeature;
import fr.dila.solonmgpp.core.dto.EvenementDTOImpl;
import fr.dila.st.rest.helper.JaxBHelper;
import fr.sword.xsd.solon.epp.CreerVersionRequest;
import fr.sword.xsd.solon.epp.EppEvtContainer;
import fr.sword.xsd.solon.epp.EvenementType;
import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(SolonMgppFeature.class)
public class TestEvenementBuilder {
    @Inject
    private EvenementService evenementService;

    @Inject
    private CoreSession session;

    private EvenementDTO evenementDTO;

    String filename = "evenement/0001 Creer dossier EVT01 AGRT1100001Y.xml";

    @Test
    public void testbuildDTO() throws Exception {
        evenementDTO = evenementService.createEmptyEvenementDTO("EVT01", null);
        Assert.assertNotNull(evenementDTO);
        Assert.assertEquals(evenementDTO.getTypeEvenementName(), "EVT01");
    }

    @Test
    public void testbuildEppEvt() throws Exception {
        CreerVersionRequest creerVersionRequest = JaxBHelper.buildRequestFromFile(filename, CreerVersionRequest.class);
        EppEvtContainer eppEvtContainer = creerVersionRequest.getEvenement();
        Assert.assertNotNull(eppEvtContainer.getEvt01());
        Assert.assertEquals(eppEvtContainer.getType(), EvenementType.EVT_01);

        // creation d'un DTO
        EvenementDTO evenementDTO = new EvenementDTOImpl();

        // construction générique de l'evenementDTO
        EvenementBuilder.getInstance().buildEvenementDTOFromContainer(evenementDTO, eppEvtContainer, null);

        EppEvtContainer eppEvtContainerResult = evenementService.createEppEvtContainerFromEvenementDTO(
            evenementDTO,
            Boolean.TRUE,
            session
        );
        Assert.assertNotNull(eppEvtContainerResult.getEvt01());
        Assert.assertEquals(eppEvtContainerResult.getType(), eppEvtContainer.getType());
        Assert.assertEquals(
            eppEvtContainerResult.getEvt01().getCommentaire(),
            eppEvtContainer.getEvt01().getCommentaire()
        );
        Assert.assertEquals(eppEvtContainerResult.getEvt01().getIdDossier(), eppEvtContainer.getEvt01().getIdDossier());
        Assert.assertEquals(
            eppEvtContainerResult.getEvt01().getIdEvenement(),
            eppEvtContainer.getEvt01().getIdEvenement()
        );
        Assert.assertEquals(eppEvtContainerResult.getEvt01().getIntitule(), eppEvtContainer.getEvt01().getIntitule());
        Assert.assertEquals(
            eppEvtContainerResult.getEvt01().getDecretPresentation().getLibelle(),
            eppEvtContainer.getEvt01().getDecretPresentation().getLibelle()
        );
        Assert.assertEquals(
            eppEvtContainerResult.getEvt01().getDecretPresentation().getFichier().get(0).getSha512(),
            eppEvtContainer.getEvt01().getDecretPresentation().getFichier().get(0).getSha512()
        );
        Assert.assertEquals(
            eppEvtContainerResult.getEvt01().getDecretPresentation().getFichier().get(0).getNomFichier(),
            eppEvtContainer.getEvt01().getDecretPresentation().getFichier().get(0).getNomFichier()
        );
    }
}
