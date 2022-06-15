package fr.dila.solonmgpp.core.util;

import org.apache.commons.lang3.StringUtils;

public class WSErrorHelper {
    private static final int MAX_MESSAGE_LENGTH = 50;

    /**
     * recupération de l'erreur du WS sans la stack
     *
     * @param messageErreur
     * @return
     */
    public static String buildCleanMessage(final String messageErreur) {
        // stack de ma forme
        // org.nuxeo.ecm.core.api.NuxeoException: %WSMESSAGE%
        // at %methode%(%class%:%ligne%)
        if (messageErreur == null) {
            return "";
        }

        String[] splitAt = messageErreur.split("\n\tat ");
        String[] splitPoint = splitAt[0].split(":");

        if (splitPoint.length > 1) {
            StringBuilder result = new StringBuilder(" ");
            for (int i = 1; i < splitPoint.length; i++) {
                result.append(splitPoint[i]);
            }
            return result.toString();
        }
        return " " + StringUtils.abbreviate(messageErreur, MAX_MESSAGE_LENGTH);
    }

    private WSErrorHelper() {
        // Private default constructor
    }
}
