package fr.dila.solonepg.core.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitEventListener;

import fr.dila.solonepg.api.activitenormative.ANReport;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.constant.SolonEpgANEventConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ActiviteNormativeStatsService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.SessionUtil;

public class RefreshANStatsListener implements PostCommitEventListener {

    /**
     * Logger formalisé en surcouche du logger apache/log4j 
     */
    private static final STLogger LOGGER = STLogFactory.getLog(RefreshANStatsListener.class);
    
    @Override
    public void handleEvent(EventBundle events) throws ClientException {
        if(!events.containsEventName(SolonEpgANEventConstants.REFRESH_STATS_EVENT)){
            return;
        }
        for (final Event event : events) {
            if (SolonEpgANEventConstants.REFRESH_STATS_EVENT.equals(event.getName())) {
                handleEvent(event);
            }
        }
    }

    private void handleEvent(Event event) {
        final EventContext eventCtx = event.getContext();
        // récupération des propriétés de l'événement
        final Map<String, Serializable> eventProperties = eventCtx.getProperties();
        final ANReport report = (ANReport) eventProperties.get(SolonEpgANEventConstants.CURRENT_REPORT_PROPERTY);
        final String userWorkspacePath = (String) eventProperties.get(SolonEpgANEventConstants.USER_WS_PATH_PROPERTY);
        final String user = (String) eventProperties.get(SolonEpgANEventConstants.USER_PROPERTY);        
        @SuppressWarnings("unchecked")
        final Map<String, String> inputValues = (HashMap<String, String>) eventProperties.get(SolonEpgANEventConstants.INPUT_VALUES_PROPERTY);
        CoreSession session = null;
        final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
        try {
            session = SessionUtil.getCoreSession();            
            if (session != null) {
                try {
                	LOGGER.info(session, SSLogEnumImpl.CREATE_BIRT_TEC, "Rafraichissement du rapport : " + report.getName());
                    anStatsService.refreshStats(session, userWorkspacePath, report, user, inputValues);
                    try {
                        sendMailRefreshOK(session, user, getLibelle(session, report, inputValues), anStatsService.getHorodatageRequest(session, report, user));
                    } catch (ClientException exc) {
                        LOGGER.error(session, STLogEnumImpl.FAIL_SEND_MAIL_TEC, exc);
                    }
                } catch (ClientException exc) {
                    LOGGER.error(session, EpgLogEnumImpl.FAIL_UPDATE_STATS_AN_TEC, exc);
                    sendMailRefreshKO(session, user, getLibelle(session, report, inputValues), anStatsService.getHorodatageRequest(session, report, user), exc.getMessage());
                } finally {
                    anStatsService.flagEndRefreshFor(session, report, user, userWorkspacePath);
                }
            } else {
                LOGGER.warn(null, STLogEnumImpl.FAIL_GET_SESSION_TEC);
            }
        } catch (ClientException exc) {
            LOGGER.error(null, STLogEnumImpl.FAIL_GET_SESSION_TEC, exc);
        } finally {            
            if (session != null) {
                SessionUtil.close(session);
            }
        }
    }
    
    private String getLibelle(CoreSession session, ANReport report, Map<String, String> inputValues) {
        StringBuilder libelleStat = new StringBuilder("\"" + report.getType().getLibelle() + "\"");
        String ministereLabel = inputValues.get("MINISTEREPILOTELABEL_PARAM");
        String idDossier = inputValues.get("DOSSIERID_PARAM");
        if (ministereLabel != null) {
            libelleStat.append(" pour le ministère \"")
                .append(ministereLabel).append("\"");
        }

        if (idDossier != null) {
            String norDossier;
            try {
                DocumentModel doc = session.getDocument(new IdRef(idDossier));
                if (STConstant.DOSSIER_DOCUMENT_TYPE.equals(doc.getType())) {
                	norDossier = doc.getAdapter(Dossier.class).getNumeroNor();
                } else if (ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE.equalsIgnoreCase(doc.getType())) {
                	norDossier = doc.getAdapter(TexteMaitre.class).getNumeroNor();
                } else {
                	LOGGER.warn(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, doc);
                	norDossier = "";
                }            	
                libelleStat.append(" pour le dossier \"")
                .append(norDossier).append("\"");
            } catch (ClientException exc) {
                LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, exc);
            }
        }
        return libelleStat.toString();
    }

    private void sendMailRefreshOK(CoreSession session, String user, String reportName, String dateRequest) {
        String userMail = STServiceLocator.getSTUserService().getUserInfo(user, "m");
        String objet = "[SOLON-EPG] Votre demande de rafraichissement de la statistique " + reportName;
        String corpsTemplate = "Bonjour, le rafraichissement de la statistique " + reportName + " demandé le " + dateRequest + " est terminé.";
        sendMail(session, userMail, objet, corpsTemplate);
    }
    
    private void sendMailRefreshKO(CoreSession session, String user, String reportName, String dateRequest, String messageStack) {
        String userMail = STServiceLocator.getSTUserService().getUserInfo(user, "m");
        String objet = "[SOLON-EPG] Votre demande de rafraichissement de la statistique " + reportName;
        String corpsTemplate = "Bonjour, le rafraichissement de la statistique " + reportName + " demandé le " + dateRequest + " a échoué. " +
        		"Le message remonté est le suivant : \n" + messageStack;
        
        sendMail(session, userMail, objet, corpsTemplate);
    }
    
    private void sendMail(CoreSession session, String adresse, String objet, String corps) {
        final STMailService mailService = STServiceLocator.getSTMailService();
        try {
            if (adresse != null) {
                mailService.sendTemplateMail(adresse, objet, corps, null);
            } else {
                LOGGER.warn(session, STLogEnumImpl.FAIL_GET_MAIL_TEC);
            }
        } catch (final ClientException exc) {
            LOGGER.error(session, STLogEnumImpl.FAIL_SEND_MAIL_TEC, exc);
        }
    }

}
