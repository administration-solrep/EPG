package fr.dila.solonepg.elastic.indexing.mapping.exceptions;

public class DocumentNotParsedException extends Exception {
    private static final long serialVersionUID = -8913090523860333862L;

    public DocumentNotParsedException() {
        super();
    }

    public DocumentNotParsedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentNotParsedException(String message) {
        super(message);
    }

    public DocumentNotParsedException(Throwable cause) {
        super(cause);
    }
}
