package fr.dila.solonmgpp.core.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.nuxeo.ecm.core.api.NuxeoException;

/**
 * Validateur des fichiers de pièces jointes.
 *
 * @author asatre
 */
public class PieceJointeFichierValidator {
    /**
     * Motif des noms des fichiers de pièce jointe autorisés.
     */
    public static final Pattern PIECE_JOINTE_FICHIER_FILENAME_PATTERN = Pattern.compile(
        "^(?!^(PRN|AUX|CLOCK\\$|NUL|CON|COM\\d|LPT\\d|\\..*)(\\..+)?$)[^\\x00-\\x1f\\\\?*:\\\";|/]+$"
    );

    private static PieceJointeFichierValidator instance;

    private PieceJointeFichierValidator() {
        //default private constructor
    }

    public static PieceJointeFichierValidator getInstance() {
        if (instance == null) {
            instance = new PieceJointeFichierValidator();
        }
        return instance;
    }

    /**
     * Vérifie si le format du nom de fichier d'une pièce jointe est valide.
     *
     * @param filename Nom de fichier
     */
    public void validatePieceJointeFichierFileName(String filename) {
        Matcher matcher = PIECE_JOINTE_FICHIER_FILENAME_PATTERN.matcher(filename);
        if (!matcher.matches()) {
            throw new NuxeoException(
                "Le nom de fichier + '" +
                filename +
                "' doit comporter uniquement les caractères suivants (accentués ou non) : [A-Z a-z 0-9], tiret (-), souligné (_) et espace"
            );
        }
    }
}
