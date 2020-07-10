package fr.dila.solonmgpp.web.evenement;

import java.util.Calendar;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.NotificationService;
import fr.dila.solonmgpp.api.service.TableReferenceService;
import fr.dila.solonmgpp.api.tree.CorbeilleNode;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.content.NotificationDTO;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.corbeille.CorbeilleTreeBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;

/**
 * Bean Seam d'alerte utilisateur lorsque la corbeille et/ou la communication courante sont modifiées.
 * 
 * @author bgamard
 */
@Name("notificationActions")
@Scope(ScopeType.PAGE)
public class NotificationActionsBean {

    @In(create = true, required = false)
    protected transient CoreSession documentManager;

    @In(create = true, required = false)
    protected transient CorbeilleTreeBean corbeilleTree;

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    private static final int MILLE = 1000;
    private static final int DELAI_CHECK_NOTIF_DEFAULT = 30000;

    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(NotificationActionsBean.class);    
    
    /**
     * Date de dernière mise à jour de la corbeille/communication courante.
     */
    private Calendar lastUpdate;
    private long delai = -1;

    public NotificationActionsBean() {
    	//Default constructor
    }
    
    @Create
    public void onCreate() throws ClientException {
        final NotificationService notificationService = SolonMgppServiceLocator.getNotificationService();
        lastUpdate = notificationService.getLastNotificationDate(documentManager) ;
        if(lastUpdate == null) {
            lastUpdate = Calendar.getInstance();
        }
    }

    /**
     * Retourne les données permettant d'afficher une notification en cas de changement de la corbeille/communication courante.
     * 
     * @return
     * @throws ClientException
     */
    public NotificationDTO getNotificationDTO() throws ClientException {

        NotificationDTO notificationDTO = new NotificationDTO();
        final NotificationService notificationService = SolonMgppServiceLocator.getNotificationService();
        // Modifications sur la corbeille
        CorbeilleNode corbeilleNode = corbeilleTree.getCurrentItem();
        if (corbeilleNode != null && corbeilleNode.isTypeCorbeille()) {
            Long count = notificationService.getCountNotificationsCorbeilleSince(documentManager, corbeilleNode.getId(), lastUpdate);
            if (count > 0) {
                notificationDTO.setCorbeilleModified(true);
            }
        }

        // Modifications sur l'évènement
        EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
        
        if (evenementDTO != null) {
            Long count = notificationService.getCountNotificationsEvenementSince(documentManager, evenementDTO.getIdEvenement(), lastUpdate);
            if (count > 0) {
                notificationDTO.setEvenementModified(true);
            }
        }
        return notificationDTO;
    }
    
    public long getNotificationDelai() throws ClientException {
      if(delai < 0) {
        ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator.getParametreMgppService().findParametrageMgpp(documentManager);
        delai = parametrageMgpp.getDelai() == null ? DELAI_CHECK_NOTIF_DEFAULT : parametrageMgpp.getDelai() * MILLE;
      }
      return delai;
      
    }
	
	public void reloadCacheTdrEppIfNecessary() {
		final NotificationService notificationService = SolonMgppServiceLocator.getNotificationService();
		Calendar notificationCalendar = Calendar.getInstance();
		try {
			notificationCalendar = notificationService.getLastNotificationUpdateCache(documentManager);
		} catch (ClientException exc) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_DATE_NOTIFICATION_TEC, exc); 
		}
		final TableReferenceService tdrService = SolonMgppServiceLocator.getTableReferenceService();
		final Calendar lastUpdateCache = tdrService.getLastUpdateCache();
		if ( lastUpdateCache == null || lastUpdateCache.before(notificationCalendar)) {
			try {
				tdrService.updateCaches(documentManager);
			} catch (ClientException exc) {
				LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_UPDATE_TDR_TEC, exc);
			}
		}
	}
}
