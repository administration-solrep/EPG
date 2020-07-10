package fr.dila.solonepg.page.recherche;

public class AjouterAuxTableauxDynamiquesPage extends AbstractAjouterAux {

    public static final String INTITULE_ID = "view_tab_dyn:nxl_tableau_dynamique_intitule:nxw_tab_dynamique_intitule";

    public static final String FIND_BOUTTON_ID = "view_tab_dyn:nxl_tableau_dynamique_destinataires:nxw_tab_dynamique_destinataires_findButton";

    public static final String AJOUTER_BOUTTON_PATH = "//input[@id='view_tab_dyn:saveTD']";

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
