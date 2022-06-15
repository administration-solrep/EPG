package fr.dila.solonepg.elastic.indexing.mapping.exceptions;

public class DocumentMissingException extends Exception {
    private static final long serialVersionUID = -8913090523860333862L;

    public DocumentMissingException() {
        super();
    }

    public DocumentMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentMissingException(String message) {
        super(message);
    }

    public DocumentMissingException(Throwable cause) {
        super(cause);
    }
}
