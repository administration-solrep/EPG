package fr.dila.solonepg.core.event;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataSource;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitEventListener;

import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.core.util.ExcelUtil;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;

public class SendSearchUsersResultsListener implements PostCommitEventListener {

    /**
     * Logger formalisé en surcouche du logger apache/log4j 
     */
    private static final STLogger LOGGER = STLogFactory.getLog(SendSearchUsersResultsListener.class);
    
    @Override
    public void handleEvent(EventBundle events) throws ClientException {
        if(!events.containsEventName(SolonEpgEventConstant.SEND_SEARCH_USERS_RESULTS_EVENT)){
            return;
        }
        for (final Event event : events) {
            if (SolonEpgEventConstant.SEND_SEARCH_USERS_RESULTS_EVENT.equals(event.getName())) {
                handleEvent(event);
            }
        }
    }
    
	private void handleEvent(Event event) {
        final EventContext eventCtx = event.getContext();
        // récupération des propriétés de l'événement
        final Map<String, Serializable> eventProperties = eventCtx.getProperties();        
        @SuppressWarnings("unchecked")
		final List<DocumentModel> usersDocs = (List<DocumentModel>) eventProperties.get(SolonEpgEventConstant.SEND_SEARCH_USERS_RESULTS_USERS_PROPERTY);
        final Boolean isAdmin = (Boolean) eventProperties.get(SolonEpgEventConstant.SEND_SEARCH_USERS_RESULTS_IS_ADMIN_PROPERTY);
        final Boolean isAdminMinisteriel = (Boolean) eventProperties.get(SolonEpgEventConstant.SEND_SEARCH_USERS_RESULTS_IS_ADMIN_MINISTERIEL_PROPERTY);
        final String recipient = (String) eventProperties.get(SolonEpgEventConstant.SEND_SEARCH_USERS_RESULTS_RECIPIENT_PROPERTY);
        final HashSet<String> ministeres = new HashSet<String>();
        {
	        @SuppressWarnings("unchecked")
	        Set<String> eventProperty = (Set<String>) eventProperties.get(SolonEpgEventConstant.SEND_SEARCH_USERS_RESULTS_ADMIN_MINISTERIEL_MINISTERES_PROPERTY);
	        ministeres.addAll(eventProperty);
        }
        final Date dateDemande = new Date(event.getTime());
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        DataSource excelFile = ExcelUtil.exportResultUserSearch(usersDocs, isAdmin, isAdminMinisteriel, ministeres);
		final STMailService mailService = STServiceLocator.getSTMailService();
		final String content = "Bonjour, veuillez trouver en pièce jointe l'export de la liste des résultats demandée le " + sdf.format(dateDemande) + ".";
        final String object = "[SOLON-EPG] Votre demande d'export d'une liste de résultats";
        final String nomFichier = "export_resultat_recherche_utilisateurs.xls";
        
        try {
        	mailService.sendMailWithAttachement(Collections.singletonList(recipient), object, content, nomFichier, excelFile);
        } catch (Exception exc) {
        	LOGGER.error(STLogEnumImpl.FAIL_SEND_MAIL_TEC, exc);
        }
        
    }
}
