package fr.dila.solonmgpp.core.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonmgpp.api.constant.SolonMgppNotificationConstants;
import fr.dila.solonmgpp.api.domain.NotificationDoc;
import fr.dila.st.core.util.PropertyUtil;

/**
 * Implémentation interface NotificationDoc pour les documents de Notification
 *
 */
public class NotificationDocImpl implements NotificationDoc , Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5148705326090823977L;
    
    private final DocumentModel document;

    public NotificationDocImpl(DocumentModel doc) {
        this.document = doc;
    }
    
    @Override
    public DocumentModel getDocument() {
        return document;
    }

    @Override
    public Calendar getDateArrive() {
        return PropertyUtil.getCalendarProperty(document, SolonMgppNotificationConstants.NOTIFICATION_SCHEMA, SolonMgppNotificationConstants.DATE_ARRIVE);
    }

    @Override
    public void setDateArrive(Calendar dateArrive) {
        PropertyUtil.setProperty(document, SolonMgppNotificationConstants.NOTIFICATION_SCHEMA, SolonMgppNotificationConstants.DATE_ARRIVE, dateArrive);
    }

    @Override
    public String getEvenementId() {
        return PropertyUtil.getStringProperty(document, SolonMgppNotificationConstants.NOTIFICATION_SCHEMA, SolonMgppNotificationConstants.EVENEMENT_ID);
    }

    @Override
    public void setEvenementId(String evenementId) {
        PropertyUtil.setProperty(document, SolonMgppNotificationConstants.NOTIFICATION_SCHEMA, SolonMgppNotificationConstants.EVENEMENT_ID, evenementId);
    }

    @Override
    public List<String> getMessageCorbeilleList() {
        return PropertyUtil.getStringListProperty(document, SolonMgppNotificationConstants.NOTIFICATION_SCHEMA, SolonMgppNotificationConstants.MESSAGE_CORBEILLE_LIST);
    }

    @Override
    public void setMessageCorbeilleList(List<String> messageCorbeilleList) {
        PropertyUtil.setProperty(document, SolonMgppNotificationConstants.NOTIFICATION_SCHEMA, SolonMgppNotificationConstants.MESSAGE_CORBEILLE_LIST, messageCorbeilleList);
    }

	@Override
	public String getTypeNotification() {
		return PropertyUtil.getStringProperty(document, SolonMgppNotificationConstants.NOTIFICATION_SCHEMA, SolonMgppNotificationConstants.TYPE_NOTIFICATION);
	}

	@Override
	public void setTypeNotification(String typeNotification) {
		PropertyUtil.setProperty(document, SolonMgppNotificationConstants.NOTIFICATION_SCHEMA, SolonMgppNotificationConstants.TYPE_NOTIFICATION, typeNotification);
	}

}
