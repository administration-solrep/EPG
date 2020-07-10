package fr.dila.solonepg.page.recherche.favoris.consultation;

public class FavorisConsultationFeulleDeRoute extends AbstractFavorisConsultation {

    public static final String FDR_CHECKBOX_TEMPLATE_ID = "recherche_favoris_consultation_modele_feuille_route:nxl_recherche_feuille_route_model_listing@INDEX@:nxw_listing_ajax_selection_box@INDEX@";

    @Override
    protected String getCheckBoxId() {
        return FDR_CHECKBOX_TEMPLATE_ID;
    }

    @Override
    protected String getRetirerDesFavorisDeConsultation() {
        return null;
    }

}
