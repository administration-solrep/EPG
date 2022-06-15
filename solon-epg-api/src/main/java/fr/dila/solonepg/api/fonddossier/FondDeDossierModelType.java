package fr.dila.solonepg.api.fonddossier;

import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;

/**
 * Types de document contenus dans l'arborescence du modèle de fond de dossier.
 *
 * @author antoine Rolin
 */
public enum FondDeDossierModelType {
    FOND_DOSSIER_FOLDER_UNDELETABLE_ROOT(SolonEpgFondDeDossierConstants.FOND_DOSSIER_FOLDER_UNDELETABLE_ROOT),
    FOND_DOSSIER_FOLDER_UNDELETABLE(SolonEpgFondDeDossierConstants.FOND_DOSSIER_FOLDER_UNDELETABLE),
    FOND_DOSSIER_FOLDER_DELETABLE(SolonEpgFondDeDossierConstants.FOND_DOSSIER_FOLDER_DELETABLE);

    private String value;

    FondDeDossierModelType(String type) {
        this.setValue(type);
    }

    private void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FondDeDossierModelType getEnum(String enumValue) {
        FondDeDossierModelType type = null;
        if (FOND_DOSSIER_FOLDER_UNDELETABLE.value.equals(enumValue)) {
            type = FOND_DOSSIER_FOLDER_UNDELETABLE;
        } else if (FOND_DOSSIER_FOLDER_DELETABLE.value.equals(enumValue)) {
            type = FOND_DOSSIER_FOLDER_DELETABLE;
        } else if (FOND_DOSSIER_FOLDER_UNDELETABLE_ROOT.value.equals(enumValue)) {
            type = FOND_DOSSIER_FOLDER_UNDELETABLE_ROOT;
        }
        return type;
    }
}
