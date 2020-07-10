package fr.dila.solonepg.core.event;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitEventListener;

import com.google.common.collect.Lists;

import fr.dila.solonepg.api.birt.ExportPANStat;
import fr.dila.solonepg.api.constant.SolonEpgANEventConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ActiviteNormativeStatsService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.SessionUtil;

public class ExportPANStatsListener implements PostCommitEventListener {

    /**
     * Logger formalisé en surcouche du logger apache/log4j 
     */
    private static final STLogger LOGGER = STLogFactory.getLog(ExportPANStatsListener.class);
    
    @Override
    public void handleEvent(EventBundle events) throws ClientException {
        if(!events.containsEventName(SolonEpgANEventConstants.EXPORT_STATS_EVENT)){
            return;
        }
        for (final Event event : events) {
            if (SolonEpgANEventConstants.EXPORT_STATS_EVENT.equals(event.getName())) {
                handleEvent(event);
            }
        }
    }

    private String getDateRequest(CoreSession session, String user,List<String> legislatures)throws ClientException{
      
      final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
      
      DocumentModel doc = anStatsService.getExportPanStatDoc(session, user,legislatures);
      if(doc != null){
        Calendar calendar  =  doc.getAdapter(ExportPANStat.class).getDateRequest();
        if(calendar != null){
          return formatWithHour(calendar.getTime());  
        }        
      }
      return null  ;      
    }
    
    
    public static String formatWithHour(final Date date) {
      if (date != null) {
          SimpleDateFormat format = DateUtil.simpleDateFormat("dd/MM/yyyy à HH:mm");
          return format.format(date);
      }
      return null;
  }
    
    
    
    private void handleEvent(Event event) {
        final EventContext eventCtx = event.getContext();
        // récupération des propriétés de l'événement
        final Map<String, Serializable> eventProperties = eventCtx.getProperties();        
        final String userWorkspacePath = (String) eventProperties.get(SolonEpgANEventConstants.USER_WS_PATH_PROPERTY);
        final String user = (String) eventProperties.get(SolonEpgANEventConstants.USER_PROPERTY);
        @SuppressWarnings("unchecked")
        final Map<String, String> inputValues = (HashMap<String, String>) eventProperties.get(SolonEpgANEventConstants.INPUT_VALUES_PROPERTY);
        String exportedLegis = (String) eventProperties.get(SolonEpgANEventConstants.EXPORTED_LEGIS);
        List<String> lstLegislatures = Lists.newArrayList(exportedLegis.split(",")); 
        
        CoreSession session = null;
        final ActiviteNormativeStatsService anStatsService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
        try {
            session = SessionUtil.getCoreSession();            
            if (session != null) {
                try {
                    anStatsService.exportPANStats(session, userWorkspacePath,user, inputValues,lstLegislatures);
                    try {
                        sendMailExportOK(session, user, this.getDateRequest(session, user,lstLegislatures));
                    } catch (ClientException exc) {
                        LOGGER.error(session, STLogEnumImpl.FAIL_SEND_MAIL_TEC, exc);
                    }
                } catch (ClientException exc) {
                    LOGGER.error(session, EpgLogEnumImpl.FAIL_UPDATE_STATS_AN_TEC, exc);
                    sendMailExportKO(session, user, this.getDateRequest(session, user,lstLegislatures), exc.getMessage());
                } finally {
                    anStatsService.flagEndEXportingFor(session, user, userWorkspacePath,lstLegislatures);
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
   

    private void sendMailExportOK(CoreSession session, String user, String dateRequest) {
        String userMail = STServiceLocator.getSTUserService().getUserInfo(user, "m");
        String objet = "[SOLON-EPG] Votre demande d'export de la statistique ";
        String corpsTemplate = "Bonjour, l'export de la statistique, demandé le " + dateRequest + ", est terminé.";
        sendMail(session, userMail, objet, corpsTemplate);
    }
    
    private void sendMailExportKO(CoreSession session, String user, String dateRequest, String messageStack) {
        String userMail = STServiceLocator.getSTUserService().getUserInfo(user, "m");
        String objet = "[SOLON-EPG] Votre demande d'export de la statistique ";
        String corpsTemplate = "Bonjour, l'export de la statistique, demandé le " + dateRequest + ", a échoué. " +
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
