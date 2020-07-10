package fr.dila.solonepg.page.main.onglet;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.dila.solonepg.page.administration.FondDeDossierPage;
import fr.dila.solonepg.page.administration.GestionDeLindexationPage;
import fr.dila.solonepg.page.administration.GestionDesMigrationsPage;
import fr.dila.solonepg.page.administration.GestionModelesPage;
import fr.dila.solonepg.page.administration.GestionOrganigrammePage;
import fr.dila.solonepg.page.administration.GestionRestrictionAccesPage;
import fr.dila.solonepg.page.administration.GestionUtilisateurs;
import fr.dila.solonepg.page.administration.ParametrageDeLapplicationPage;
import fr.dila.solonepg.page.administration.ParapheurPage;
import fr.dila.solonepg.page.administration.TablesDeReferencePage;
import fr.dila.solonepg.page.administration.VecteursPublicationPage;
import fr.dila.solonepg.page.commun.EPGWebPage;

public class AdministrationEpgPage extends EPGWebPage {

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Gestion des modèles")
	private WebElement	gestionModeles;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Fond de dossier")
	private WebElement	fondDeDossier;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Parapheur")
	private WebElement	parapheur;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Tables de référence")
	private WebElement	tablesDeReference;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Gestion de l'indexation")
	private WebElement	gestionDeLindexation;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Paramétrage des vecteurs de publication")
	// Anciennement "Liste des bulletins officiels"
	private WebElement	vecteursPublication;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Paramétrage de l'application")
	private WebElement	parametrageDeLapplication;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Gestion des utilisateurs")
	private WebElement	gestionUtilisateurs;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Gestion des migrations")
	private WebElement	gestionDesMigrations;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Gestion de l'accès")
	private WebElement	gestionAcces;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Gestion de l'organigramme")
	private WebElement	gestionOrganigramme;

	/**
	 * Va vers l'administration
	 * 
	 * @return
	 */
	public GestionModelesPage goToGestionModeles() {
		return clickToPage(gestionModeles, GestionModelesPage.class);
	}

	/**
	 * Va vers l'administration
	 * 
	 * @return
	 */
	public FondDeDossierPage goToFondDeDossier() {
		return clickToPage(fondDeDossier, FondDeDossierPage.class);
	}

	/**
	 * Va vers Parapheur
	 * 
	 * @return parapheur page
	 */
	public ParapheurPage goToParapheur() {
		return clickToPage(parapheur, ParapheurPage.class);
	}

	/**
	 * Va vers Tables de Reference
	 * 
	 * @return tablesDeReference
	 */
	public TablesDeReferencePage goToTablesDeReference() {
		return clickToPage(tablesDeReference, TablesDeReferencePage.class);
	}

	/**
	 * Va vers Gestion de L'indexation
	 * 
	 * @return GestionDeLindexationPage
	 */
	public GestionDeLindexationPage goToGestionDeLindexation() {
		return clickToPage(gestionDeLindexation, GestionDeLindexationPage.class);
	}

	/**
	 * Va vers Liste de Bulletins Officiels Page
	 * 
	 * @return BulletinsOfficielsPage
	 */
	public VecteursPublicationPage goToVecteursPublication() {
		return clickToPage(vecteursPublication, VecteursPublicationPage.class);
	}

	/**
	 * @return ParametrageDeLapplicationPage
	 */
	public ParametrageDeLapplicationPage goToParametrageDeLapplicationPage() {
		return clickToPage(parametrageDeLapplication, ParametrageDeLapplicationPage.class);
	}

	/**
	 * @return GestionUtilisateurs
	 */
	public GestionUtilisateurs goToGestionUtilisateurs() {
		return clickToPage(gestionUtilisateurs, GestionUtilisateurs.class);
	}

	/**
	 * Va Gestion des migrations
	 * 
	 * @return GestionDesMigrationsPage
	 */
	public GestionDesMigrationsPage goToGestionDesMigrations() {
		return clickToPage(gestionDesMigrations, GestionDesMigrationsPage.class);
	}

	/**
	 * @return GestionAcces
	 */
	public GestionRestrictionAccesPage goToGestionAcces() {
		return clickToPage(gestionAcces, GestionRestrictionAccesPage.class);
	}

	public GestionOrganigrammePage goToGestionOrganigrammePage() {
		GestionOrganigrammePage click = clickToPage(gestionOrganigramme, GestionOrganigrammePage.class);
		this.waitElementVisibilityByXpath("//form[@id='viewOrganigrammeForm']");
		return click;
	}
}
