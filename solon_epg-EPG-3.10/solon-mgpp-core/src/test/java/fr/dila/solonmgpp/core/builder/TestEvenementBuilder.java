package fr.dila.solonmgpp.core.builder;

import junit.framework.Assert;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.service.EvenementService;
import fr.dila.solonmgpp.core.SolonMgppRepositoryTestCase;
import fr.dila.solonmgpp.core.dto.EvenementDTOImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.rest.helper.JaxBHelper;
import fr.sword.xsd.solon.epp.CreerVersionRequest;
import fr.sword.xsd.solon.epp.EppEvtContainer;
import fr.sword.xsd.solon.epp.EvenementType;

public class TestEvenementBuilder extends SolonMgppRepositoryTestCase {
    
    private EvenementService evenementService;
    
    private EvenementDTO evenementDTO;
    
    String filename = "evenement/0001 Creer dossier EVT01 AGRT1100001Y.xml";

    @Override
    public void deployRepositoryContrib() throws Exception {
        super.deployRepositoryContrib();
        evenementService = SolonMgppServiceLocator.getEvenementService();
        Assert.assertNotNull(evenementService);
    }
    
    public void testbuildDTO() throws Exception {
        evenementDTO = evenementService.createEmptyEvenementDTO("EVT01", null);
        Assert.assertNotNull(evenementDTO);
        Assert.assertEquals(evenementDTO.getTypeEvenementName(), "EVT01");
    }
    
    public void testbuildEppEvt() throws Exception {
        CreerVersionRequest creerVersionRequest = JaxBHelper.buildRequestFromFile(filename, CreerVersionRequest.class);
        EppEvtContainer eppEvtContainer = creerVersionRequest.getEvenement(); 
        Assert.assertNotNull(eppEvtContainer.getEvt01());
        Assert.assertEquals(eppEvtContainer.getType(), EvenementType.EVT_01);
        
        // creation d'un DTO
        EvenementDTO evenementDTO = new EvenementDTOImpl();
       
        // construction générique de l'evenementDTO
        EvenementBuilder.getInstance().buildEvenementDTOFromContainer(evenementDTO, eppEvtContainer, null);
        
        EppEvtContainer eppEvtContainerResult = evenementService.createEppEvtContainerFromEvenementDTO(evenementDTO, Boolean.TRUE, null);
        Assert.assertNotNull(eppEvtContainerResult.getEvt01());
        Assert.assertEquals(eppEvtContainerResult.getType(), eppEvtContainer.getType());
        Assert.assertEquals(eppEvtContainerResult.getEvt01().getCommentaire(), eppEvtContainer.getEvt01().getCommentaire());
        Assert.assertEquals(eppEvtContainerResult.getEvt01().getIdDossier(), eppEvtContainer.getEvt01().getIdDossier());
        Assert.assertEquals(eppEvtContainerResult.getEvt01().getIdEvenement(), eppEvtContainer.getEvt01().getIdEvenement());
        Assert.assertEquals(eppEvtContainerResult.getEvt01().getIntitule(), eppEvtContainer.getEvt01().getIntitule());
        Assert.assertEquals(eppEvtContainerResult.getEvt01().getDecretPresentation().getLibelle(), eppEvtContainer.getEvt01().getDecretPresentation().getLibelle());
        Assert.assertEquals(eppEvtContainerResult.getEvt01().getDecretPresentation().getFichier().get(0).getSha512(), eppEvtContainer.getEvt01().getDecretPresentation().getFichier().get(0).getSha512());
        Assert.assertEquals(eppEvtContainerResult.getEvt01().getDecretPresentation().getFichier().get(0).getNomFichier(), eppEvtContainer.getEvt01().getDecretPresentation().getFichier().get(0).getNomFichier());
        
    }
    
}
