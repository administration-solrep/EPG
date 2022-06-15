package fr.dila.solonepg.elastic.indexing.mapping.exceptions;

public class DocumentTooLargeException extends Exception {
    private static final long serialVersionUID = -8913090523860333862L;

    public DocumentTooLargeException() {
        super();
    }

    public DocumentTooLargeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentTooLargeException(String message) {
        super(message);
    }

    public DocumentTooLargeException(Throwable cause) {
        super(cause);
    }
}
