package fr.dila.solonmgpp.core.service;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepp.rest.api.WSEvenement;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.MailService;
import fr.dila.solonmgpp.core.util.WSErrorHelper;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.rest.client.HttpTransactionException;
import fr.dila.st.rest.client.WSProxyFactoryException;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epp.EnvoyerMelRequest;
import fr.sword.xsd.solon.epp.EnvoyerMelResponse;
import fr.sword.xsd.solon.epp.EvtId;

/**
 * Implementation de {@link MailService}
 * 
 * @author asatre
 * 
 */
public class MailServiceImpl implements MailService {

    /**
     * Logger formalisé en surcouche du logger apache/log4j 
     */
    private static final STLogger LOGGER = STLogFactory.getLog(MailServiceImpl.class);
    
    @Override
    public void sendMail(String text, String subject, String addresses, CoreSession session) throws ClientException {

        STMailService mailService = STServiceLocator.getSTMailService();

        String[] addressesSplit = addresses.split(";");
        Address[] recipients = new Address[addressesSplit.length];
        try {
            for (int i = 0; i < addressesSplit.length; i++) {
                String address = addressesSplit[i];
                recipients[i] = new InternetAddress(address);
            }
            mailService.sendMail(text, subject, "mailSession", recipients);
        } catch (Exception e) {            
            LOGGER.error(session, STLogEnumImpl.FAIL_SEND_MAIL_TEC, e) ;
            throw new ClientException(e.getMessage());
        }

    }

    @Override
    public void sendMailEpp(String text, String subject, String addresses, CoreSession session, EvenementDTO evenementDTO) throws ClientException {

        WSEvenement wsEvenement = null;

        try {
            wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
        } catch (WSProxyFactoryException e) {
            LOGGER.error(session,MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
            throw new ClientException(e);
        }

        EnvoyerMelRequest envoyerMelRequest = new EnvoyerMelRequest();
        envoyerMelRequest.setContenuMel(text);
        envoyerMelRequest.setDestinataireMel(addresses);
        envoyerMelRequest.setObjetMel(subject);
        envoyerMelRequest.setInclusPieceJointe(Boolean.TRUE);

        EvtId evtId = new EvtId();
        evtId.setId(evenementDTO.getIdEvenement());

        envoyerMelRequest.setIdEvenement(evtId);

        EnvoyerMelResponse envoyerMelResponse = null;

        try {
            envoyerMelResponse = wsEvenement.envoyerMel(envoyerMelRequest);
        } catch (HttpTransactionException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
            throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
        } catch (Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_SEND_MAIL_TEC, e) ;
            throw new ClientException(e);
        }

        if (envoyerMelResponse == null) {
            throw new ClientException("Erreur de communication avec SOLON EPP, l'envoi de mèl a échoué.");
        }
        else if(envoyerMelResponse.getStatut()==null || !TraitementStatut.OK.equals(envoyerMelResponse.getStatut())) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, envoyerMelResponse.getMessageErreur()) ;
            throw new ClientException("Erreur de communication avec SOLON EPP, l'envoi de mèl a échoué."
                    + WSErrorHelper.buildCleanMessage(envoyerMelResponse.getMessageErreur()));
        }

    }

}
