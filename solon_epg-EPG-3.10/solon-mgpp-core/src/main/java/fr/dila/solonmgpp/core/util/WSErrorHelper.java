package fr.dila.solonmgpp.core.util;

public class WSErrorHelper {

	/**
	 * recupération de l'erreur du WS sans la stack
	 * 
	 * @param messageErreur
	 * @return
	 */
	public static String buildCleanMessage(final String messageErreur) {
		// stack de ma forme
		// org.nuxeo.ecm.core.api.ClientException: %WSMESSAGE%
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
		if (messageErreur.length() > 40) {
			return " " + messageErreur.substring(0, 50);
		}

		return " " + messageErreur;
	}

	private WSErrorHelper() {
		// Private default constructor
	}
}
