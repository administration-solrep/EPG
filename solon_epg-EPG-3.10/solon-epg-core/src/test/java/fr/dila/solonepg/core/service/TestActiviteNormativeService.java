package fr.dila.solonepg.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.nuxeo.ecm.core.api.ClientException;

import edu.emory.mathcs.backport.java.util.Arrays;
import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.solonepg.core.dto.activitenormative.injectionbdj.BilanSemestrielDTO;
import fr.dila.solonepg.core.mock.MockActiviteNormativeService;
import fr.dila.solonepg.core.mock.MockTexteMaitre;
import fr.dila.st.core.util.DateUtil;
import junit.framework.Assert;

public class TestActiviteNormativeService extends SolonEpgRepositoryTestCase {
	private TexteMaitre texteMaitre;
	private TexteMaitre texteMaitreOlderLegislature;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		openSession();

		SolonEpgParametreService paramEPGservice = SolonEpgServiceLocator.getSolonEpgParametreService();
		ParametrageAN parametrageAN = paramEPGservice.getDocAnParametre(session);
		parametrageAN.setLegislatureEnCours("13");
		session.saveDocument(parametrageAN.getDocument());
		session.save();

		texteMaitre = new MockTexteMaitre();
		texteMaitre.setNumero("2017-1510");
		texteMaitre.setNumeroNor("INTX1716370L");
		texteMaitre.setMesuresIds(Arrays.asList(new String[] { "1", "2", "3", "4", "5", "6", "7", "8" }));
		texteMaitre.setLegislaturePublication("13");
		
		texteMaitreOlderLegislature=new MockTexteMaitre();
		texteMaitreOlderLegislature.setNumero("2017-0001");
		texteMaitreOlderLegislature.setNumeroNor("INTX1716371L");
		texteMaitreOlderLegislature.setMesuresIds(Arrays.asList(new String[] { "1", "2" }));
		texteMaitreOlderLegislature.setLegislaturePublication("12");
	}

	@Override
	public void tearDown() throws Exception {
		closeSession();
		super.tearDown();
	}

	private String getFileContent(String filePath) throws IOException {
		return IOUtils.toString(getClass().getClassLoader().getResourceAsStream(filePath));
	}

	@Test
	public void testBuildNullEcheancier() {
		MockActiviteNormativeService actNormService = new MockActiviteNormativeService();

		ArrayList<TexteMaitre> textesMaitres = new ArrayList<TexteMaitre>();
		
		String echeancierXml = null;
		try {
			echeancierXml = actNormService.buildEcheancierXmlStr(session, textesMaitres);
			assertNull(echeancierXml);
		} catch (ClientException e) {
			Assert.fail();
		}
		
		textesMaitres.add(texteMaitreOlderLegislature);
		try {
			echeancierXml = actNormService.buildEcheancierXmlStr(session, textesMaitres);
			assertNull(echeancierXml);
		} catch (ClientException e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testBuildEcheancierJsonStr() {
		MockActiviteNormativeService actNormService = new MockActiviteNormativeService();

		ArrayList<TexteMaitre> textesMaitres = new ArrayList<TexteMaitre>();
		textesMaitres.add(texteMaitre);

		String echeancierXml = null;
		try {
			echeancierXml = actNormService.buildEcheancierXmlStr(session, textesMaitres);
			assertNotNull(echeancierXml);
		} catch (ClientException e) {
			Assert.fail();
		}

		String expectedXml = null;
		String expectedJson = null;
		try {
			expectedXml = getFileContent("injectionbdj/echeancier.xml");
			expectedJson = getFileContent("injectionbdj/echeancier.json");
		} catch (IOException e) {
			Assert.fail();
		}

		Assert.assertNotNull(echeancierXml);
		Assert.assertEquals(expectedXml, echeancierXml);

		String echeancierJson = null;
		try {
			echeancierJson = actNormService.buildEcheancierJsonStr(echeancierXml);
		} catch (IOException e) {
			Assert.fail();
		}

		Assert.assertNotNull(echeancierJson);
		Assert.assertEquals(expectedJson, echeancierJson);
	}
	
	
	@Test
	public void testBuildBilanSemestrielJsonStr() {
		MockActiviteNormativeService actNormService = new MockActiviteNormativeService();
		BilanSemestrielDTO bilanSemestriel = new BilanSemestrielDTO();
		
		try {
			SolonEpgParametreService paramEPGservice = SolonEpgServiceLocator.getSolonEpgParametreService();
			ParametrageAN parametrageAN = paramEPGservice.getDocAnParametre(session);
			parametrageAN.setLegislatureEnCours("15");
			session.saveDocument(parametrageAN.getDocument());
			session.save();
			
			bilanSemestriel.setType(BilanSemestrielDTO.BilanSemestrielType.LOI);
			bilanSemestriel.setDateBilan(DateUtil.parse("10/08/2018").getTime());
			bilanSemestriel.setDateDebutIntervalleTextesPublies(DateUtil.parse("01/07/2017").getTime());
			bilanSemestriel.setDateFinIntervalleTextesPublies(DateUtil.parse("31/12/2018").getTime());
			bilanSemestriel.setDateDebutIntervalleMesures(DateUtil.parse("01/07/2017").getTime());
			bilanSemestriel.setDateFinIntervalleMesures(DateUtil.parse("31/12/2018").getTime());
			
			bilanSemestriel.addTexte("1", "Loi pour la confiance dans la vie politique", "10", "5", "5");
			bilanSemestriel.addTexte("2", "Loi de financement de la sécurité sociale", "20", "5", "15");
			
		} catch (ClientException e1) {
			Assert.fail();
		}

		String bilanXml = null;
		
		bilanXml = actNormService.buildBilanXmlStr(bilanSemestriel, "15");

		String expectedXml = null;
		try {
			expectedXml = getFileContent("injectionbdj/bilansemestriel.xml");
			
		} catch (IOException e) {
			Assert.fail();
		}
		
		Assert.assertNotNull(bilanXml);
		Assert.assertEquals(expectedXml, bilanXml);
	}
	
}