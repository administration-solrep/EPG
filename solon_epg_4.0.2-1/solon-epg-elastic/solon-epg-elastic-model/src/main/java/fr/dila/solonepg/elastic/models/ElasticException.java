package fr.dila.solonepg.elastic.models;

public class ElasticException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 8662485078244079224L;

    public ElasticException(String message, Throwable cause) {
        super(message, cause);
    }

    public ElasticException(String message) {
        super(message);
    }
}
