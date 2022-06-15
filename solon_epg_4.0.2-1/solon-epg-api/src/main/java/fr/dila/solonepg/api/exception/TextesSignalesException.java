package fr.dila.solonepg.api.exception;

import fr.dila.solonepg.api.service.TexteSignaleService;
import java.util.Set;
import org.nuxeo.ecm.core.api.NuxeoException;

/**
 * Exception pour le service de gestion des textes signalés
 * @see TexteSignaleService
 * @author asatre
 *
 */
public class TextesSignalesException extends NuxeoException {
    private static final long serialVersionUID = 248518488260068763L;

    private Set<String> errors;

    public TextesSignalesException(String message) {
        super(message);
    }

    public TextesSignalesException(Set<String> error) {
        super();
        this.errors = error;
    }

    public void setErrors(Set<String> errors) {
        this.errors = errors;
    }

    public Set<String> getErrors() {
        return errors;
    }
}
