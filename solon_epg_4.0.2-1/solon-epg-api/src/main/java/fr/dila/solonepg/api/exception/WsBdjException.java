package fr.dila.solonepg.api.exception;

public class WsBdjException extends Exception {
    private static final long serialVersionUID = -8595478499659917394L;

    public WsBdjException(String msg) {
        super(msg);
    }

    public WsBdjException(Throwable cause) {
        super(cause);
    }
}
