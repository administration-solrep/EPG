package fr.dila.solonepg.api.exception;

import org.nuxeo.ecm.core.api.NuxeoException;

/**
 * Exception pour le service d'indexation EPG
 * @author asatre
 *
 */
public class IndexationException extends NuxeoException {
    private static final long serialVersionUID = 248518488260068763L;

    public IndexationException(String message) {
        super(message);
    }
}
