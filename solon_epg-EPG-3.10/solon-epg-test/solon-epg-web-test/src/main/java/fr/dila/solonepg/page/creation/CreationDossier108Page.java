package fr.dila.solonepg.page.creation;

public class CreationDossier108Page extends AbstractCreationDossierPage {

    public static final String BOUTON_TERMINER_ID = "creation_dossier_108:buttonTerminer";

    @Override
    protected String getButtonSuivantId() {
        return null;
    }

    @Override
    protected String getButtonTerminerId() {
        return BOUTON_TERMINER_ID;
    }

}
