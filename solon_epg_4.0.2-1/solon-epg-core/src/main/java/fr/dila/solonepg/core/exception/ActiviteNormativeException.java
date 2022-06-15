package fr.dila.solonepg.core.exception;

import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.st.core.util.ResourceHelper;
import java.util.Set;
import org.nuxeo.ecm.core.api.NuxeoException;

/**
 * Exception pour le service de gestion de l'activite Normative
 * @see ActiviteNormativeService
 * @author asatre
 *
 */
public class ActiviteNormativeException extends NuxeoException {
    private static final long serialVersionUID = 248518488260068763L;

    private Set<String> errors;

    public ActiviteNormativeException(String message) {
        super(ResourceHelper.getString(message));
    }

    public ActiviteNormativeException(Set<String> error) {
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
