package fr.dila.solonepg.elastic.indexing.mapping.exceptions;

public class MappingException extends Exception {
    private static final long serialVersionUID = 1L;

    public MappingException() {
        super();
    }

    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappingException(String message) {
        super(message);
    }

    public MappingException(Throwable cause) {
        super(cause);
    }
}
