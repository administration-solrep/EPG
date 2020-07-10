package fr.dila.solonepg.page.recherche.user;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;

import fr.dila.solonepg.page.administration.GestionUtilisateurs;

public class ModifierUser extends UserDetailTab {

	public static final String	MONSIEUR_ID			= "editUser:nxl_user:nxw_title_select:1";
	public static final String	TELEPHONE_ID		= "editUser:nxl_user:nxw_telephoneNumber";
	public static final String	MAIL_ID				= "editUser:nxl_user:nxw_email:nxw_email_email";
	public static final String	PROFILE_ID			= "editUser:nxl_user:nxw_groups_selectProfile";
	public static final String	PRENOM_ID			= "editUser:nxl_user:nxw_firstname";
	public static final String	POST_UTILISATEUR_ID	= "editUser:nxl_userPostes:nxw_postes_findButton";

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Retour à la liste")
	public WebElement			retourAlaListe;

	public void selectMonsieur() {
		this.getElementById(MONSIEUR_ID).click();
	}

	public void setTelePhone(String tel) {
		WebElement el = this.getElementById(TELEPHONE_ID);
		this.fillField("Téléphone", el, tel);
	}

	public void setMail(String mail) {
		WebElement el = this.getElementById(MAIL_ID);
		this.fillField("Mél", el, mail);
	}

	public void setPrenom(String prenom) {
		WebElement el = this.getElementById(PRENOM_ID);
		this.fillField("Prénom", el, prenom);
	}

	public void setProfile(String profile) {
		WebElement el = this.getElementById(PROFILE_ID);
		Select select = new Select(el);
		select.selectByVisibleText(profile);

		this.getElementById("editUser:nxl_user:nxw_groups_addProfile").click();
	}

	public void setPostUtilisateur(List<String> ar) {
		this.selectMultipleInOrganigramme(ar, POST_UTILISATEUR_ID);
	}

	public GestionUtilisateurs retourAlaListe() {
		return clickToPage(retourAlaListe, GestionUtilisateurs.class);
	}
}
