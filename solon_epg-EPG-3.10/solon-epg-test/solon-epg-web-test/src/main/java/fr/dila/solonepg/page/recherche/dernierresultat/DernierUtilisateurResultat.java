package fr.dila.solonepg.page.recherche.dernierresultat;

public class DernierUtilisateurResultat extends AbstractDernierResultat {

    public static final String UTILISATEUR_CHECKBOX_TEMPLATE_ID = "recherche_resultats_consultes_user:nxl_recherche_user_listing@INDEX@:nxw_user_listing_ajax_selection_box@INDEX@";
    public static final String AJOUTER_AUX_FAVORIS_DE_CONSULTATION_BTN_ID = "recherche_resultats_consultes_user:clipboardActionsTable__0:1:clipboardActionsButton";

    @Override
    protected String getAjouterAuxFavrisDeConsultation() {
        return AJOUTER_AUX_FAVORIS_DE_CONSULTATION_BTN_ID;
    }

    @Override
    protected String getCheckBoxId() {
        return UTILISATEUR_CHECKBOX_TEMPLATE_ID;
    }

}
