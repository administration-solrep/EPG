import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;

import fr.dila.solonepg.api.alert.SolonEpgAlert;

/**
 * Script groovy pour supprimer toutes les alertes qui n'ont pas de destinataires
 * 
 * Paramètre :
 * 
 */
class AlertCleaner {    

	/**
	 * Retourne les alertes qui n'ont pas de destinataires enregistrés 
	 * 
	 * @return DocumentModelList liste des alertes sans destinataires
	 */
	public static int removeAlertsWithNoRecipients (session) {
		String queryAlerts = "SELECT * FROM Alert";
		DocumentModelList alertList = session.query(queryAlerts);
		if(alertList == null || alertList.isEmpty()) {
			print "Pas d'alertes";
			return 0;
		}
		int removedAlert = 0;
		for(DocumentModel alertDoc : alertList) {
			SolonEpgAlert alert = alertDoc.getAdapter(SolonEpgAlert.class);
			if (alert.getRecipientIds().isEmpty()) {
				print "Suppression de l'alerte nommée '" + alert.getTitle() + "' du créateur " + alert.getNameCreator();
				session.removeDocument(new IdRef(alertDoc.getId()));
				removedAlert++;
			}
		}
		return removedAlert;		
	}
}

print "Début script groovy de nettoyage d'alertes";
print "-------------------------------------------------------------------------------";
int removedAlert = AlertCleaner.removeAlertsWithNoRecipients(Session);
print "-------------------------------------------------------------------------------";
print "Fin du script groovy de nettoyage d'alertes";
print "Alertes supprimées : " + removedAlert ;
return "Fin du script groovy - " + removedAlert + " suppression(s)";
