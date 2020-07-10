package fr.dila.solonepg.divers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import fr.dila.solonepg.page.creation.AbstractCreationDossierPage;
import fr.dila.solonepg.webtest.utils.RegexUtils;
import junit.framework.Assert;

public class Regexp {

	@Test
	public void recupereNomDossier() {
		String norDossier = "EFIM1500011D";
		String str = "\tDossier EFIM1500011D enregistré\t";
		Matcher m =Pattern.compile("[\\s|\\t]*Dossier (.*) enregistr.*").matcher(str);
		
		Assert.assertTrue(m.matches());
		Assert.assertEquals(norDossier, RegexUtils.getGroupMatch(AbstractCreationDossierPage.regExpDossierCree, str));
	}
}
