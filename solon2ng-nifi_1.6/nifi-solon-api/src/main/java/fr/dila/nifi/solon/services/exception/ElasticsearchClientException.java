package fr.dila.nifi.solon.services.exception;

public class ElasticsearchClientException extends RuntimeException {

    private static final long serialVersionUID = 883330909701453400L;

    public ElasticsearchClientException(final String message) {
        super(message);
    }

    public ElasticsearchClientException(final Throwable cause) {
        super(cause);
    }

    public ElasticsearchClientException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
