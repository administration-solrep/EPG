package fr.dila.solonepg.api.exception;

import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonepg.api.service.ActiviteNormativeService;

/**
 * Exception pour le service de gestion de l'activite Normative
 * @see ActiviteNormativeService
 * @author asatre
 *
 */
public class ActiviteNormativeException extends ClientException {

	private static final long serialVersionUID = 248518488260068763L;
	
	private Set<String> errors; 
	
	public ActiviteNormativeException(String message) {
		super(message);
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
