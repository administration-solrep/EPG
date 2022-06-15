package fr.dila.nifi.solon.processors.exception;

public class BinaryExtractorRetryableException extends RuntimeException {

    private static final long serialVersionUID = 6063377983058086741L;

    public BinaryExtractorRetryableException(final String message) {
        super(message);
    }

    public BinaryExtractorRetryableException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
