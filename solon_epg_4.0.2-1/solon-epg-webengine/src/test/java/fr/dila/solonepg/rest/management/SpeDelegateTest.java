package fr.dila.solonepg.rest.management;

import static fr.dila.st.api.constant.STWebserviceConstant.ENVOYER_RETOUR_PE;
import static fr.sword.xsd.solon.spe.PEstatut.KO;
import static fr.sword.xsd.solon.spe.PEstatut.OK;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;

import com.google.common.base.Charsets;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFolder;
import fr.dila.solonepg.api.service.EPGFeuilleRouteService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import fr.dila.solonepg.rest.api.SpeDelegate;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.sword.xsd.solon.spe.EnvoyerPremiereDemandePERequest;
import fr.sword.xsd.solon.spe.EnvoyerPremiereDemandePEResponse;
import fr.sword.xsd.solon.spe.EnvoyerRetourPERequest;
import fr.sword.xsd.solon.spe.EnvoyerRetourPEResponse;
import fr.sword.xsd.solon.spe.PEActeRetourBo;
import fr.sword.xsd.solon.spe.PEActeRetourJo;
import fr.sword.xsd.solon.spe.PEDemandeType;
import fr.sword.xsd.solon.spe.PEFichier;
import fr.sword.xsd.solon.spe.PERetourEpreuvage;
import fr.sword.xsd.solon.spe.PERetourGestion;
import fr.sword.xsd.solon.spe.PERetourPublicationBo;
import fr.sword.xsd.solon.spe.PERetourPublicationJo;
import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.nuxeo.ecm.core.api.CloseableCoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.mockito.RuntimeService;

public class SpeDelegateTest extends AbstractEPGTest {
    @Mock
    private SSPrincipal principal;

    @Inject
    private NORService norService;

    @Mock
    @RuntimeService
    private ParapheurService parapheurService;

    @Mock
    @RuntimeService
    private EPGFeuilleRouteService epgFeuilleRouteService;

    private SpeDelegate delegate;
    private CloseableCoreSession session;
    private PERetourEpreuvage peRetourEpreuvage;
    private PERetourPublicationJo peRetourPublicationJo;
    private PERetourPublicationBo peRetourPublicationBo;
    private String nor;
    Dossier dossier;

    @Before
    public void setUp() throws Exception {
        session = coreFeature.openCoreSession(principal);
        delegate = new SpeDelegateImpl(session);

        when(principal.isAdministrator()).thenReturn(true);

        dossier = createDossier();
        nor = dossier.getNumeroNor();
        PEFichier fichier = new PEFichier();
        fichier.setNom("Fichier");
        fichier.setOrdre(0);
        fichier.setContent("Content".getBytes(Charsets.UTF_8));
        peRetourEpreuvage = new PERetourEpreuvage();
        peRetourEpreuvage.setNor(nor);
        peRetourEpreuvage.setEpreuve(fichier);
        peRetourEpreuvage.setCompare(fichier);
        PEActeRetourJo peActeRetourJo = new PEActeRetourJo();
        peActeRetourJo.setNor(nor);
        peActeRetourJo.setNumeroTexte("1");
        peActeRetourJo.setTitreOfficiel("Titre officiel");
        PERetourGestion peRetourGestion = new PERetourGestion();
        peRetourGestion.setDateParution(DatatypeFactory.newInstance().newXMLGregorianCalendar());
        peRetourPublicationJo = new PERetourPublicationJo();
        peRetourPublicationJo.getActe().add(peActeRetourJo);
        peRetourPublicationJo.setGestion(peRetourGestion);
        PEActeRetourBo peActeRetourBo = new PEActeRetourBo();
        peActeRetourBo.setNor(nor);
        peActeRetourBo.setNumeroTexte("2");
        peRetourPublicationBo = new PERetourPublicationBo();
        peRetourPublicationBo.getActe().add(peActeRetourBo);
        peRetourPublicationBo.setGestion(peRetourGestion);
    }

    @After
    public void tearDown() {
        session.close();
    }

    @Test
    public void testEnvoyerPremiereDemandePE() throws Exception {
        EnvoyerPremiereDemandePERequest request = new EnvoyerPremiereDemandePERequest();
        EnvoyerPremiereDemandePEResponse response;

        response = delegate.envoyerPremiereDemandePE(request);
        Assert.assertEquals(OK, response.getStatus());
    }

    @Test
    public void testEnvoyerRetourPE() throws Exception {
        EnvoyerRetourPERequest request = new EnvoyerRetourPERequest();
        EnvoyerRetourPEResponse response;

        request.setType(PEDemandeType.EPREUVAGE);
        addRepEpreuveFdd(dossier);
        request.setRetourEpreuvage(peRetourEpreuvage);
        response = delegate.envoyerRetourPE(request);
        Assert.assertEquals(KO, response.getStatus());
        Assert.assertEquals("Vous n'avez pas les droits pour accéder à ce service !", response.getMessageErreur());

        when(principal.getGroups()).thenReturn(singletonList(ENVOYER_RETOUR_PE));
        response = delegate.envoyerRetourPE(request);
        Assert.assertEquals(OK, response.getStatus());

        request.setType(PEDemandeType.PUBLICATION_JO);
        request.setRetourPublicationJo(peRetourPublicationJo);
        response = delegate.envoyerRetourPE(request);
        Assert.assertEquals(OK, response.getStatus());
        RetourDila retourDila = norService.getDossierFromNOR(session, nor).getAdapter(RetourDila.class);
        Assert.assertEquals("1", retourDila.getNumeroTexteParutionJorf());
        Assert.assertEquals("Titre officiel", retourDila.getTitreOfficiel());

        request.setType(PEDemandeType.PUBLICATION_BO);
        request.setRetourPublicationBo(peRetourPublicationBo);
        response = delegate.envoyerRetourPE(request);
        Assert.assertEquals(OK, response.getStatus());
        retourDila = norService.getDossierFromNOR(session, nor).getAdapter(RetourDila.class);
    }

    private void addRepEpreuveFdd(Dossier dossier) {
        // Créer Rep Epreuve FDD
        DocumentModel fddFolderAccessAllUser = SolonEpgServiceLocator
            .getFondDeDossierService()
            .getFondDossierFolder(
                dossier.getDocument(),
                session,
                SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER,
                false
            );
        DocumentModel newFddFolder = SolonEpgServiceLocator
            .getFondDeDossierService()
            .createNewFolder(
                session,
                SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE,
                fddFolderAccessAllUser,
                SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES
            );
        // on le paramètre
        FondDeDossierFolder fddFolderEpreuves = newFddFolder.getAdapter(FondDeDossierFolder.class);
        fddFolderEpreuves.setIsDeletable(false);
        fddFolderEpreuves.save(session);
    }
}
