package fr.dila.solonepg.page.recherche.favoris.consultation;

public class FavorisConsultationDossier extends AbstractFavorisConsultation {

    private static String DOSSIER_CHECKBOX_TEMPLATE_ID = "recherche_favoris_consultations_dossier:nxl_dossier_listing_dto_1:nxw_listing_ajax_checkbox_dto@INDEX@";

    private static String RETIRER_DES_FAVORIS_DE_CONSULTATION_BTN_ID = "recherche_favoris_consultations_dossier:clipboardActionsTable__0:2:clipboardActionsButton";

    @Override
    protected String getCheckBoxId() {
        return DOSSIER_CHECKBOX_TEMPLATE_ID;
    }

    @Override
    protected String getRetirerDesFavorisDeConsultation() {
        return RETIRER_DES_FAVORIS_DE_CONSULTATION_BTN_ID;
    }

}
