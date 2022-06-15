package fr.dila.nifi.solon.services.domain;

public enum HttpResponseStatusFamily {
    INFORMATION,
    SUCCESS,
    REDIRECTION,
    CLIENT_ERROR,
    SERVER_ERROR,
    UNDEFINED;

    private static final int STATUS_CODE_DIVIDER = 100;

    public static HttpResponseStatusFamily valueOf(final int code) {
        final HttpResponseStatusFamily statusFamily;
        switch (code / STATUS_CODE_DIVIDER) {
            case 1:
                statusFamily = INFORMATION;
                break;
            case 2:
                statusFamily = SUCCESS;
                break;
            case 3:
                statusFamily = REDIRECTION;
                break;
            case 4:
                statusFamily = CLIENT_ERROR;
                break;
            case 5:
                statusFamily = SERVER_ERROR;
                break;
            default:
                statusFamily = UNDEFINED;
                break;
        }
        return statusFamily;
    }
}
