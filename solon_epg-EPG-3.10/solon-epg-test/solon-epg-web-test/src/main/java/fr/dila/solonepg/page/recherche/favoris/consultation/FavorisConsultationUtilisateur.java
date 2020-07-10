package fr.dila.solonepg.page.recherche.favoris.consultation;

public class FavorisConsultationUtilisateur extends AbstractFavorisConsultation {

	public static final String	UTILISATEUR_CHECKBOX_TEMPLATE_ID			= "recherche_favoris_consultation_user:nxl_recherche_user_listing@INDEX@:nxw_user_listing_ajax_selection_box@INDEX@:nxw_user_listing_ajax_selection_box@INDEX@";
	public static final String	RETIRER_DES_FAVORIS_DE_CONSULTATIONS_BTN_ID	= "recherche_favoris_consultation_user:clipboardActionsTable__0:2:clipboardActionsButton";

	@Override
	protected String getCheckBoxId() {
		return UTILISATEUR_CHECKBOX_TEMPLATE_ID;
	}

	@Override
	protected String getRetirerDesFavorisDeConsultation() {
		return RETIRER_DES_FAVORIS_DE_CONSULTATIONS_BTN_ID;
	}

}
