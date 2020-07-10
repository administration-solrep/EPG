package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface dédiée au document NotificationDoc
 *
 */
public interface NotificationDoc {

    String getTypeNotification();
    
    void setTypeNotification(String typeNotification);
    
    Calendar getDateArrive();

    void setDateArrive(Calendar dateArrive);

    String getEvenementId();

    void setEvenementId(String evenementId);

    List<String> getMessageCorbeilleList();

    void setMessageCorbeilleList(List<String> messageCorbeilleList);

    DocumentModel getDocument();

}
