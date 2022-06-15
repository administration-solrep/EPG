package fr.dila.solonepg.api.fonddossier;

import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;

/**
 * Type contenu dans le champs ou des noeud du fond de dossier
 *
 * @author antoine Rolin
 *
 */
public enum FondDeDossierType {
    FOND_DOSSIER_FOLDER_UNDELETABLE_ROOT(SolonEpgFondDeDossierConstants.FOND_DOSSIER_FOLDER_UNDELETABLE_ROOT),
    FOND_DOSSIER_FOLDER_UNDELETABLE(SolonEpgFondDeDossierConstants.FOND_DOSSIER_FOLDER_UNDELETABLE),
    FOND_DOSSIER_FOLDER_DELETABLE(SolonEpgFondDeDossierConstants.FOND_DOSSIER_FOLDER_DELETABLE),
    FOND_DOSSIER_FILE(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE);

    private String value;

    FondDeDossierType(String type) {
        this.setValue(type);
    }

    private void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FondDeDossierType getEnum(String enumValue) {
        FondDeDossierType type = null;
        if (FOND_DOSSIER_FOLDER_UNDELETABLE_ROOT.value.equals(enumValue)) {
            type = FOND_DOSSIER_FOLDER_UNDELETABLE_ROOT;
        } else if (FOND_DOSSIER_FOLDER_UNDELETABLE.value.equals(enumValue)) {
            type = FOND_DOSSIER_FOLDER_UNDELETABLE;
        } else if (FOND_DOSSIER_FOLDER_DELETABLE.value.equals(enumValue)) {
            type = FOND_DOSSIER_FOLDER_DELETABLE;
        } else if (FOND_DOSSIER_FILE.value.equals(enumValue)) {
            type = FOND_DOSSIER_FILE;
        }
        return type;
    }
}
