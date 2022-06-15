package fr.dila.solonmgpp.api.service;

import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Interface de gestion des mails
 *
 * @author asatre
 *
 */
public interface MailService {
    /**
     * Envoi de mail simple
     *
     * @param text
     * @param subject
     * @param addresses
     * @param session
     */
    void sendMail(String text, String subject, String addresses, CoreSession session);

    /**
     * Envoi de mail via WSEpp
     *
     * @param text
     * @param subject
     * @param addresses
     * @param idEvenement
     * @param session
     */
    void sendMailEpp(String text, String subject, String addresses, String idEvenement, CoreSession session);
}
