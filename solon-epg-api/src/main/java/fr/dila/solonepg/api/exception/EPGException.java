package fr.dila.solonepg.api.exception;

import fr.dila.ss.api.exception.SSException;
import org.nuxeo.ecm.core.api.NuxeoException;

public class EPGException extends SSException {
    /**
     *
     */
    private static final long serialVersionUID = 5459543694478099112L;

    public EPGException() {
        super("Exception EPG");
    }

    public EPGException(String message, NuxeoException cause) {
        super(message, cause);
    }

    public EPGException(String message, Throwable exc) {
        super(message, exc);
    }

    public EPGException(String message) {
        super(message);
    }

    public EPGException(Throwable cause) {
        super(cause);
    }

    public EPGException(NuxeoException cause) {
        super(cause);
    }
}
