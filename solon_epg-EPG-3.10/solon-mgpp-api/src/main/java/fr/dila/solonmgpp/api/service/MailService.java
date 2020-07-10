package fr.dila.solonmgpp.api.service;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonmgpp.api.dto.EvenementDTO;

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
     * @throws ClientException
     */
    void sendMail(String text, String subject, String addresses, CoreSession session) throws ClientException;

    /**
     * Envoi de mail via WSEpp
     * 
     * @param text
     * @param subject
     * @param addresses
     * @param session
     * @param evenementDTO
     * @throws ClientException
     */
    void sendMailEpp(String text, String subject, String addresses, CoreSession session, EvenementDTO evenementDTO) throws ClientException;

}
