package fr.dila.solonepg.api.parapheur;

import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import java.util.stream.Stream;

/**
 * Types de document contenus dans l'arborescence du modèle de parapheur.
 *
 * @author antoine Rolin
 */
public enum ParapheurModelType {
    PARAPHEUR_MODEL_FOLDER_UNDELETABLE(SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_UNDELETABLE),
    PARAPHEUR_MODEL_FOLDER(SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_TYPE);

    private final String value;

    ParapheurModelType(String type) {
        value = type;
    }

    public String getValue() {
        return value;
    }

    public static ParapheurModelType getEnum(String value) {
        return Stream.of(values()).filter(type -> type.getValue().equals(value)).findFirst().orElse(null);
    }
}
