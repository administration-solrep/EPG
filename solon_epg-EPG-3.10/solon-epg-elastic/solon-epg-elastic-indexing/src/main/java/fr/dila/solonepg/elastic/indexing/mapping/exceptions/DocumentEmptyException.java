package fr.dila.solonepg.elastic.indexing.mapping.exceptions;

public class DocumentEmptyException extends Exception {

	private static final long serialVersionUID = -8913090523860333862L;

	public DocumentEmptyException() {
		super();
	}

	public DocumentEmptyException(String message, Throwable cause) {
		super(message, cause);
	}

	public DocumentEmptyException(String message) {
		super(message);
	}

	public DocumentEmptyException(Throwable cause) {
		super(cause);
	}

}
