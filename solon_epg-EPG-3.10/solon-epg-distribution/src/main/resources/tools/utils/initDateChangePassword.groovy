import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.platform.usermanager.UserManager;

import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.api.administration.ProfilUtilisateur;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.service.STServiceLocator;

/**
 * script groovy d'initialisation des dates de changement de mot de passe
 * 
 */

    
print "Début du script groovy d'initialisation des dates de changement de mot de passe";
print "-------------------------------------------------------------------------------";

    final UserManager userManager = STServiceLocator.getUserManager();
    final STMailService mailService = STServiceLocator.getSTMailService();
    final ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
    
    // On récupère tous les ids par ordre alphabétique
    Map<String, Serializable> filter = new HashMap<String, Serializable>();
    final DocumentModelList userModelList = userManager.searchUsers(filter, null);
    final List<STUser> allUsersList = new ArrayList<STUser>();
    
    for (DocumentModel userDocModel : userModelList) {
    	STUser user = userDocModel.getAdapter(STUser.class);
    	if (user.isActive()) {
    		allUsersList.add(user);
    	}
    }
    
    final List<ProfilUtilisateur> allUsersWithProfilList = new ArrayList<ProfilUtilisateur>();
    for (STUser user : allUsersList) {
    	try {
    		DocumentModel profilModel = profilUtilisateurService.getOrInitUserProfilFromId(Session,user.getUsername());
    		if (profilModel != null) {
    			ProfilUtilisateur profilUtilisateur =  profilModel.getAdapter(ProfilUtilisateur.class);
	    		if (profilUtilisateur != null) {
	        		allUsersWithProfilList.add(profilUtilisateur);
	        	}
    		}
    	} catch (ClientException e) {
    		print "ATTENTION : Impossible de récupérer le profil de " + user.getUsername();
    		print e.getMessage();
    	}
    }
    
    print allUsersWithProfilList.size() + " profils récupérés."
    
    static final int NB_GROUPE = 4;
    final int TAILLE_GROUPE = allUsersWithProfilList.size()/NB_GROUPE;
	Calendar calendar = Calendar.getInstance();
	calendar.set(2014,Calendar.MARCH,3,0,0,0);
    for ( int i=0 ; i<allUsersWithProfilList.size() ; i++ ) {
    	ProfilUtilisateur profilUtilisateur = allUsersWithProfilList.get(i);
		if (i == TAILLE_GROUPE-1) {
    		calendar.add(Calendar.DATE, 7);
    	} else if (i == 2*TAILLE_GROUPE-1) {
    		calendar.add(Calendar.DATE, 7);
    	} else if (i == 3*TAILLE_GROUPE-1) {
    		calendar.add(Calendar.DATE, 7);
    	}
    	profilUtilisateur.setDernierChangementMotDePasse(calendar);
    	Session.saveDocument(profilUtilisateur.getDocument());
    	Session.save();
    }
    
    
print "-------------------------------------------------------------------------------";
print "Fin du script groovy d'initialisation des dates de changement de mot de passe";
return "Fin du script groovy";
