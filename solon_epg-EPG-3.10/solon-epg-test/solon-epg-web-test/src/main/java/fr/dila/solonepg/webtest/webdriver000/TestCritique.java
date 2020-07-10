package fr.dila.solonepg.webtest.webdriver000;

import org.junit.Assert;

import fr.dila.solonepg.page.main.onglet.TraitementPage;
import fr.dila.solonepg.webtest.common.AbstractEpgWebTest;
import fr.dila.st.annotations.TestDocumentation;
import fr.sword.naiad.commons.webtest.annotation.WebTest;
import junit.framework.AssertionFailedError;

/**
 * Tests qui ne modifient pas du tout l'état des données en base, mais qui font simplement des tentatives de connexion/accès à des onglets
 * car il y a eu beaucoup d'aléa sur cette partie.
 * 
 * @author eboussaton
 *
 */
public class TestCritique extends AbstractEpgWebTest {

	private void loginEtControleOnglets(String login) {
		TraitementPage traitementPage;
		boolean admin = "adminsgg".equals(login);
		traitementPage = loginEpg(login);
        traitementPage.getOngletMenu().goToEspaceCreation();

        traitementPage.getOngletMenu().goToEspaceTraitement();
    	traitementPage.getOngletMenu().goToAdministration();
    	if (admin) {
   		 traitementPage.getOngletMenu().goToPilotageAN();
		 traitementPage.getOngletMenu().goToRecherche();
    	}
    	traitementPage.getOngletMenu().goToSuivi();

        logoutEpg();
	}

	@WebTest(description = "Première connexion avec chaque utilisateur")
	@TestDocumentation(categories={"Utilisateurs"})
	public void premiersAcces() {
		String[] users = {"adminsgg", "bdc", "finance_bdc", "documentalistesgg", "documentalistemin", "documentalistedir", "agriculture_bdc", "contributeurmin", "signataire"};
		for (String user : users) {
			try {
				loginEpg(user);
				logoutEpg();
			} catch (RuntimeException e) {
				getFlog().warn("#SAUVETAGE#: La connexion avec "+user+" n'a pas fonctionné: on réessaye");
				loginEpg(user);
				logoutEpg();
			}
		}
	}
	
	@WebTest(description = "Tests d'accès et contrôle de présence d'onglets")
	@TestDocumentation(categories={"Navigation"})
    public void repetitionAcces() {
		loginEtControleOnglets("bdc");
		loginEtControleOnglets("bdc");
		loginEtControleOnglets("adminsgg");
		loginEtControleOnglets("finance_bdc");
		loginEtControleOnglets("finance_bdc");
		loginEtControleOnglets("adminsgg");
    }

	@WebTest(description = "cas d'erreur")
	@TestDocumentation(categories={"Utilisateurs"})
	public void doubleLogin() {
		String login = "finance_bdc";
		loginEpg(login);
        boolean error = false;
        try {
        	loginEpg(login);
        } catch (AssertionFailedError e) {
        	// On a bien une erreur dans un double login => OK
        	error = true;
        }
        Assert.assertTrue(error);
	}
}
