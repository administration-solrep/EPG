package fr.dila.solonepg.page.recherche;

/**
 * s
 * 
 * @author admin
 */
public class DernierResultatConsulteUtilisateurPage extends CommonRechercheUtilisateurPage {

	public static final String	RECHERCHE_RESULTATS_CONSULTES_USER_1		= "//form[@id='recherche_resultats_consultes_user']/div/table/tbody/tr[1]/td[2]";
	public static final String	RECHERCHE_RESULTATS_CONSULTES_USER_2		= "//form[@id='recherche_resultats_consultes_user']/div/table/tbody/tr[2]/td[2]";

	public static final String	UTILISATEUR_CHECKBOX_TEMPLATE_ID			= "recherche_resultats_consultes_user:nxl_recherche_user_listing_@INDEX@:nxw_user_listing_ajax_selection_box_@INDEX@:nxw_user_listing_ajax_selection_box_@INDEX@";
	public static final String	AJOUTER_AUX_FAVORIS_DE_CONSULTATION_BTN_ID	= "recherche_resultats_consultes_user:clipboardActionsTable__0:1:clipboardActionsButton";
	public static final String	RETIRER_DES_FAVORIS_DE_CONSULTATIONS_BTN_ID	= "recherche_favoris_consultation_user:clipboardActionsTable__0:2:clipboardActionsButton";

	/**
	 * @param index
	 *            start by 1
	 */
	public void selectUser(int index) {
		String checkboxId = index == 0 ? UTILISATEUR_CHECKBOX_TEMPLATE_ID.replace("@INDEX@", "")
				: UTILISATEUR_CHECKBOX_TEMPLATE_ID.replace("@INDEX@", Integer.toString(index - 1));
		getElementById(checkboxId).click();
		sleep(1);
	}

	/**
	 * Clic sur le boutton ajouter aux favoris de consutation
	 */
	public void ajouterAuxFavorisDeConsultation() {
		getElementById(AJOUTER_AUX_FAVORIS_DE_CONSULTATION_BTN_ID).click();
		sleep(1);
	}

	/**
	 * Clic sur Retirer des favoris de consultations
	 */
	public void retirerDesFavorisDeConsultation() {
		getElementById(RETIRER_DES_FAVORIS_DE_CONSULTATIONS_BTN_ID).click();
		sleep(1);
	}
}
