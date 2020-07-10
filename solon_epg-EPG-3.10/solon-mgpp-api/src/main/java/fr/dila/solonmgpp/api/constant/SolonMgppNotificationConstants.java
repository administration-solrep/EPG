package fr.dila.solonmgpp.api.constant;

/**
 * Constantes liées au Document NotificationDoc
 * 
 *
 */
public final class SolonMgppNotificationConstants {

	/**
	 * Schéma du document notification : notification_doc
	 */
	public static final String NOTIFICATION_SCHEMA = "notification_doc";
	
	/**
	 * Type du document notification : NotificationDoc
	 */
	public static final String NOTIFICATION_DOC_TYPE = "NotificationDoc";
	
	/**
	 * Préfixe du document notification : nt
	 */
	public static final String NOTIFICATION_PREFIX = "nt";
	
	/**
	 * Propriété date arrivée : dateArrive
	 */
	public static final String DATE_ARRIVE = "dateArrive";
	
	/**
	 * Propriété évènement id : evenementId
	 */
	public static final String EVENEMENT_ID = "evenementId";
	
	/**
	 * Propriété message corbeille list : messageCorbeilleList
	 */
	public static final String MESSAGE_CORBEILLE_LIST = "messageCorbeilleList";
	
	/**
	 * Propriété type notification : typeNotification
	 */
	public static final String TYPE_NOTIFICATION = "typeNotification";

	/**
	 * Valeur de type de notification : CACHE_TDR_UPDATE
	 */
	public static final String NOTIFICATION_TDR = "CACHE_TDR_UPDATE";

	/**
	 * Chemin racine des notificationDoc
	 */
	public static final String NOTIFICATION_ROOT_PATH = "/case-management/notification";
	
	private SolonMgppNotificationConstants() {
		// private default constructor
	}
}
