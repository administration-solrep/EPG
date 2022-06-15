package fr.dila.nifi.solon.services.exception;

public class ElasticsearchRetryableClientException extends ElasticsearchClientException {

    private static final long serialVersionUID = 6063377983058086741L;

    public ElasticsearchRetryableClientException(final String message) {
        super(message);
    }

    public ElasticsearchRetryableClientException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
