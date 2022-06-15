package fr.dila.nifi.solon.processors.exception;

import org.apache.nifi.processor.exception.ProcessException;

public class InvalidFlowFileException extends ProcessException {

    private static final long serialVersionUID = 1L;

    public InvalidFlowFileException(String message) {
        super(message);
    }

    public InvalidFlowFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
