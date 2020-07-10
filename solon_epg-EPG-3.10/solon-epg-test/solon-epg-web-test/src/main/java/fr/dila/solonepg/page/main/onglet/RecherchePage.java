package fr.dila.solonepg.page.main.onglet;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import fr.dila.solonepg.page.commun.EPGWebPage;
import fr.dila.solonepg.page.recherche.DernierResultatConsulteUtilisateurPage;
import fr.dila.solonepg.page.recherche.RechercheExpertePage;
import fr.dila.solonepg.page.recherche.RechercheModeleFeuilleDeRoutePage;
import fr.dila.solonepg.page.recherche.RechercheUtilisateurPage;
import fr.dila.solonepg.page.recherche.dernierresultat.DernierDossierResultat;
import fr.dila.solonepg.page.recherche.dernierresultat.DernierFeuilleDeRouteResultat;
import fr.dila.solonepg.page.recherche.favoris.consultation.FavorisConsultationDossier;
import fr.dila.solonepg.page.recherche.favoris.consultation.FavorisConsultationFeulleDeRoute;
import fr.dila.solonepg.page.recherche.favoris.consultation.FavorisConsultationUtilisateur;
import fr.sword.naiad.commons.webtest.WebPage;

public class RecherchePage extends EPGWebPage {

    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Recherche experte")
    private WebElement rechercheExperte;

    @FindBy(how = How.ID, using = RECHERCHE_UTILISATEUR_LINK_ID)
    private WebElement rechercheUtilisateur;

    @FindBy(how = How.ID, using = RECHERCHE_MODELE_FEUILLE_DE_ROUTE_ID)
    private WebElement rechercheModeleFeuilleDeRoute;

    @FindBy(how = How.ID, using = DERNIER_MODELE_FEUILLE_DE_ROUTE_ID)
    private WebElement dernierModeleFeuilleDeRoute;

    @FindBy(how = How.ID, using = DERNIER_RESULTAT_CONSULTE_UTILISATEUR_LINK_ID)
    private WebElement dernierResultatConsulteUtilisateur;

    @FindBy(how = How.ID, using = FAVORIS_DE_CONSULTATION_FDR_ID)
    private WebElement favorisDeConsultationFDR;

    @FindBy(how = How.ID, using = FAVORIS_DE_CONSULATION_DOSSIER_ID)
    private WebElement favorisDeConsultationDossier;

    @FindBy(how = How.ID, using = FAVORIS_DE_CONSULTATION_UTILISATEURS_ID)
    private WebElement favorisDeConsultationUtilisateurs;

    private static final String RECHERCHE_UTILISATEUR_LINK_ID = "form_espace_recherche:espaceRechercheTree:node:0:node:2::feuilleNodeLabel_O";

    public static final String FAVORIS_DE_RECHERCHE_PATH = "//span[@id='form_espace_recherche:espaceRechercheTree:node:3::feuilleNodeLabel_BC']";

    public static final String DERNIER_DOSSIER_ID = "form_espace_recherche:espaceRechercheTree:node:1:node:0::feuilleNodeLabel_O";

    public static final String RECHERCHE_MODELE_FEUILLE_DE_ROUTE_ID = "form_espace_recherche:espaceRechercheTree:node:0:node:1::feuilleNodeLabel_O";

    public static final String DERNIER_MODELE_FEUILLE_DE_ROUTE_ID = "form_espace_recherche:espaceRechercheTree:node:1:node:1::feuilleNodeLabel_O";

    private static final String DERNIER_RESULTAT_CONSULTE_UTILISATEUR_LINK_ID = "form_espace_recherche:espaceRechercheTree:node:1:node:2::feuilleNodeLabel_O";

    private static final String FAVORIS_DE_CONSULTATION_FDR_ID = "form_espace_recherche:espaceRechercheTree:node:2:node:1::feuilleNodeLabel_O";

    private static final String FAVORIS_DE_CONSULATION_DOSSIER_ID = "form_espace_recherche:espaceRechercheTree:node:2:node:0::feuilleNodeLabel_O";

    private static final String FAVORIS_DE_CONSULTATION_UTILISATEURS_ID = "form_espace_recherche:espaceRechercheTree:node:2:node:2::feuilleNodeLabel_O";

    /**
     * Va vers l'administration
     * 
     * @return
     */
    public RechercheExpertePage goToRechercheExperte() {
        return clickToPage(rechercheExperte, RechercheExpertePage.class);
    }

    public RechercheUtilisateurPage goToRechercheUtilisateur() {
        return clickToPage(rechercheUtilisateur, RechercheUtilisateurPage.class);
    }

    /**
     * Click sur le lien Favoris de recherche qui se trouve a gauche
     */
    public void goToFavorisDeRecherche() {
        clickToPage(getElementByXpath(FAVORIS_DE_RECHERCHE_PATH), WebPage.class);
    }

    /**
     * Click sur le lien dossier
     */
    public DernierDossierResultat goToDernierDossier() {
        this.getElementById(DERNIER_DOSSIER_ID).click();
        this.waitElementVisibilityById("espaceRechercheDossierContentListPanel");
        return this.getPage(DernierDossierResultat.class);
    }

    /**
     * @return RechercheModeleFeuilleDeRoutePage
     */
    public RechercheModeleFeuilleDeRoutePage goToRechercheModeleFeuilleDeRoute() {
        return clickToPage(rechercheModeleFeuilleDeRoute, RechercheModeleFeuilleDeRoutePage.class);
    }

    /**
     * @return DernierFeuilleDeRouteResultat
     */
    public DernierFeuilleDeRouteResultat goToDernierModeleFeuilleDeRoute() {
        DernierFeuilleDeRouteResultat click = clickToPage(dernierModeleFeuilleDeRoute, DernierFeuilleDeRouteResultat.class);
        this.waitElementVisibilityById("espaceRechercheDossierContentListPanel");
        return click;
    }

    public DernierResultatConsulteUtilisateurPage goToDernierResultatConsulteUtilisateur() {
        DernierResultatConsulteUtilisateurPage click = clickToPage(dernierResultatConsulteUtilisateur, DernierResultatConsulteUtilisateurPage.class);
        this.waitElementVisibilityById("espaceRechercheDossierContentListPanel");
        return click;
    }

    /**
     * @return FavorisConsultationFeulleDeRoute
     */
    public FavorisConsultationFeulleDeRoute goToFavorsiDeConsultationFDR() {
        FavorisConsultationFeulleDeRoute click = clickToPage(favorisDeConsultationFDR, FavorisConsultationFeulleDeRoute.class);
        this.waitElementVisibilityById("espaceRechercheDossierContentListPanel");
        return click;
    }

    /**
     * @return FavorisConsultationDossier
     */
    public FavorisConsultationDossier goToFavorsiDeConsultationDossier() {
        FavorisConsultationDossier click = clickToPage(favorisDeConsultationDossier, FavorisConsultationDossier.class);
        this.waitElementVisibilityById("espaceRechercheDossierContentListPanel");
        return click;
    }

    /**
     * @return FavorisConsultationUtilisateur
     */
    public FavorisConsultationUtilisateur goToFavoriDeConsultationUtilisateurs() {
        FavorisConsultationUtilisateur click = clickToPage(favorisDeConsultationUtilisateurs, FavorisConsultationUtilisateur.class);
        this.waitElementVisibilityById("espaceRechercheDossierContentListPanel");
        return click;
    }
}
