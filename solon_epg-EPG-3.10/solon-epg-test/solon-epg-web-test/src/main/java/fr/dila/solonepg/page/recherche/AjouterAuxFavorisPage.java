package fr.dila.solonepg.page.recherche;

public class AjouterAuxFavorisPage extends AbstractAjouterAux {

    public static final String INTITULE_ID = "fav_rech_form:nxl_favoris_recherche_layout:nxw_fr_intitule_select";

    public static final String FIND_BOUTTON_ID = "fav_rech_form:nxl_favoris_recherche_layout:nxw_fr_poste_select_findButton";

    public static final String AJOUTER_BOUTTON_PATH = "//input[@id='fav_rech_form:savefr']";

    @Override
    protected String getIntituleId() {
        return INTITULE_ID;
    }

    @Override
    protected String getFindButtonId() {
        return FIND_BOUTTON_ID;
    }

    @Override
    protected String getAjouterBouttonPath() {
        return AJOUTER_BOUTTON_PATH;
    }

}
