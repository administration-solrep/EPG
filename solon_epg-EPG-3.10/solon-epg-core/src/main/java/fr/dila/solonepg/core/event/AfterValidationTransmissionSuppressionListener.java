package fr.dila.solonepg.core.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitEventListener;
import org.nuxeo.ecm.platform.usermanager.UserManager;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.core.util.ExcelUtil;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.api.service.ProfileService;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.service.STServiceLocator;

/**
 * listener After Validation Transmission Suppression Listener
 */
public class AfterValidationTransmissionSuppressionListener implements PostCommitEventListener {

    
    private static final String FILE_NAME_TO_SEND = "Transmission_pour_suppression.xls";
    
    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog(AfterValidationTransmissionSuppressionListener.class);

    @Override
    public void handleEvent(final EventBundle events) throws ClientException {
    	if(!events.containsEventName(SolonEpgEventConstant.AFTER_VALIDER_TRANSMISSION_TO_SUPPRESSION)){
    		return;
    	}
        for (final Event event : events) {
            if (event.getName().equals(SolonEpgEventConstant.AFTER_VALIDER_TRANSMISSION_TO_SUPPRESSION)) {
                handleEvent(event);
            }
        }
    }
    
    public void handleEvent(Event event) throws ClientException {
        final ProfileService profileService = STServiceLocator.getProfileService();
        final STParametreService paramService = STServiceLocator.getSTParametreService();
        final STMailService mailService = STServiceLocator.getSTMailService();

        // Traite uniquement les évènements de création / modification de documents
        EventContext ctx = event.getContext();

        @SuppressWarnings("unchecked")
        List<String> dossierIdsList = (List<String>) ctx.getProperty(SolonEpgEventConstant.DOCUMENT_ID_LIST);
        // Traite uniquement les documents de type délégation
        CoreSession session = ctx.getCoreSession();
       
        String objet = paramService.getParametreValue(session, STParametreConstant.OBJET_MAIL_DOSSIER_AFTER_VALIDATION_TRANSMISSION);
        String text = paramService.getParametreValue(session, STParametreConstant.TEXT_MAIL_DOSSIER_AFTER_VALIDATION_TRANSMISSION);
 
        List<STUser> users = profileService.getUsersFromBaseFunction(SolonEpgBaseFunctionConstant.ADMIN_MINISTERIEL_EMAIL_RECEIVER);
       
        // Une map d'id des ministeres vers le fichier et les utilisateurs qui le recevront
        Map<String, FileUsersSet> ministereUserMap = initializeFiles(session, dossierIdsList);
            
        try {
            putUsersToListMail(users, ministereUserMap);            
            // On parcourt les entrées Fichier/Liste utilisateur
            // pour lancer un e-mail avec les fichiers en piece jointe
            for (FileUsersSet entryFichierUser : ministereUserMap.values()) {
                // Conversion du fichier excel en datasource
                HSSFWorkbook workbook = entryFichierUser.getWorkbook();
                ExcelUtil.formatStyleDossier(workbook);
                DataSource fichierExcelResultat = ExcelUtil.convertExcelToDataSource(entryFichierUser.getWorkbook());
                mailService.sendMailWithAttachement(entryFichierUser.getUsersMails(), objet, text, FILE_NAME_TO_SEND, fichierExcelResultat);
            }            
        } catch (Exception e) {
            LOGGER.error("Echec d'envoi du mail de validation transmission  ", e);
        }
    }
    
    /**
     * On parcourt tous les dossiers que l'on veut transmettre à suppression 
     * et on les ajoute aux fichiers excels correspondant aux ministères
     * @param session
     * @param dossierIdsList 
     * @return Une map d'id des ministeres vers le fichier et les utilisateurs qui le recevront
     * @throws ClientException
     */
    private Map<String, FileUsersSet> initializeFiles(CoreSession session, List<String> dossierIdsList) throws ClientException {
        Map<String, FileUsersSet> ministereUserMap = new HashMap<String, FileUsersSet>();
        FileUsersSet fileUsersSet;
        for (String dossierId : dossierIdsList) {
            DocumentModel dossierDoc = session.getDocument(new IdRef(dossierId));
            String ministereAttache = dossierDoc.getAdapter(Dossier.class).getMinistereAttache();            
            if (ministereUserMap.containsKey(ministereAttache)) {
                // Si on a déjà croisé ce ministère, récupère l'ensemble workbook/users déjà existant
                fileUsersSet = ministereUserMap.get(ministereAttache);
            } else {
                // Sinon, on créé le workbook 
                fileUsersSet = new FileUsersSet(ExcelUtil.createDossierExcelFile(), new ArrayList<String>());
                ministereUserMap.put(ministereAttache, fileUsersSet);                
            }
            // On ajoute le dossier au workbook correspondant
            ExcelUtil.addDossierAtEndToFile(dossierDoc, fileUsersSet.getWorkbook());
        }
        
        return ministereUserMap;
    }
    
    /**
     * On parcourt les utilisateurs pour récupérer ceux qui ont un e-mail 
     * et on les ajoute au listes d'envoi des ministères auxquels ils sont rattachés
     * @param users
     * @param ministereUserMap
     * @throws ClientException
     */
    private void putUsersToListMail(List<STUser> users, Map<String, FileUsersSet> ministereUserMap) throws ClientException {
        final UserManager userManager = STServiceLocator.getUserManager();        
        for (STUser user : users) {
            SSPrincipal principal = (SSPrincipal) userManager.getPrincipal(user.getUsername());
            String email = user.getEmail();
            if (email != null) {
                for (String ministereId : principal.getMinistereIdSet()) {
                    FileUsersSet fileUsersSet = ministereUserMap.get(ministereId);
                    // Tous les ministères auxquels appartient le user n'ont pas forcément de dossier à supprimer
                    if (fileUsersSet != null) {
                        fileUsersSet.addUserMail(email);
                    }
                }
            }
        }  
    }
    
    private class FileUsersSet {
        private final HSSFWorkbook workbook;
        private final List<String> usersMails;
        
        public FileUsersSet(HSSFWorkbook workbook, List<String> usersMails) {
            this.workbook = workbook;
            this.usersMails = usersMails;
        }
        
        public HSSFWorkbook getWorkbook() {
            return this.workbook;
        }
        
        public List<String> getUsersMails() {
            return this.usersMails;
        }
        
        public void addUserMail(String userMail) {
            this.usersMails.add(userMail);
        }
    }
}
