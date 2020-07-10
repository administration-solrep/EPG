package fr.dila.solonepg.page.recherche.dernierresultat;

public class DernierDossierResultat extends AbstractDernierResultat {

    public static String DOSSIER_CHECKBOX_TEMPLATE_ID = "recherche_resultats_consultes_dossier:nxl_dossier_listing_dto@INDEX@:nxw_listing_ajax_checkbox_dto@INDEX@";

    public static String AJOUTER_AUX_FAVORIS_DE_CONSULTATIONS_BTN_ID = "recherche_resultats_consultes_dossier:clipboardActionsTable__0:1:clipboardActionsButton";

    @Override
    protected String getAjouterAuxFavrisDeConsultation() {
        return AJOUTER_AUX_FAVORIS_DE_CONSULTATIONS_BTN_ID;
    }

    @Override
    protected String getCheckBoxId() {
        return DOSSIER_CHECKBOX_TEMPLATE_ID;
    }

}
