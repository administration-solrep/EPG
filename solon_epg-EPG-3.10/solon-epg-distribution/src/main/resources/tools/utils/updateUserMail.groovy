import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.platform.usermanager.UserManager;

import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.service.STServiceLocator;

/**
 * 
 * script groovy de modification des mails utilisateurs
 * 
 */

class FixUtils {    
    public static final String MODE_INFO = "INFO";
    public static final String MODE_FIX = "FIX";
}

print "Début du script groovy de modification des mails utilisateurs";
print "-------------------------------------------------------------------------------";

	String newMel = Context.get("newMel");
	String oldMel = Context.get("oldMel");
	String mode = null;
	emailPattern = /[A-Za-z0-9._-]+@[A-Za-z0-9._-]{2,}\.[A-Za-z]{2,4}/
	
	if (StringUtils.isBlank(oldMel)) {
		print "Argument oldMel non trouvé. exit";
		return "Fin du script groovy";
	}
	if(StringUtils.isBlank(newMel)) {
		print "Argument newMel non trouvé. Mode info";
		oldMel = oldMel.replace("'", "");
		print "Recherche des mails : " + oldMel; 
		mode = FixUtils.MODE_INFO;
	} else {
		newMel = newMel.replace("'", "");
		oldMel = oldMel.replace("'", "");
		if (!StringUtils.isBlank(newMel) && !(newMel ==~ emailPattern) ) {
	        print "Nouveau mel non reconnu - Vous devez spécifier : -ctx \"newMel='nouvelle adresse'\" où 'nouvelle adresse' doit correspondre à une adresse mel valide"
	        return "Echec du script groovy";
	    }
		if (!StringUtils.isBlank(oldMel) && !(oldMel ==~ emailPattern) ) {
	        print "Ancien mel non reconnu - Vous devez spécifier : -ctx \"oldMel='ancienne adresse'\" où 'ancienne adresse' doit correspondre à une adresse mel valide"
	        return "Echec du script groovy";
	    }
		print "Toutes les adresses méls " + oldMel + " seront remplacées par : " + newMel;
		mode = FixUtils.MODE_FIX;
	}

	final UserManager userManager = STServiceLocator.getUserManager();
	final STMailService mailService = STServiceLocator.getSTMailService();
	
	// On récupère tous les utilisateurs par ordre alphabétique
	Map<String, Serializable> filter = new HashMap<String, Serializable>();
	final DocumentModelList userModelList = userManager.searchUsers(filter, null);
	final List<STUser> allUsersList = new ArrayList<STUser>();
	
	int nbCorrection = 0;
	int nbErreur = 0;
	for (DocumentModel userDocModel : userModelList) {
		STUser user = userDocModel.getAdapter(STUser.class);
		if (user.isActive() && oldMel.equals(user.getEmail())) {
			nbErreur++;
			if (FixUtils.MODE_INFO.equals(mode)) {
				print "L'utilisateur " + user.getUsername() + " possède une adresse mél non valide : " + user.getEmail();
			}
			else if (FixUtils.MODE_FIX.equals(mode)) {
				user.setEmail(newMel);
				userManager.updateUser(user.getDocument());
				nbCorrection++;
				print "Adresse mél changée pour l'utilisateur " + user.getUsername();
			} else {
				print "Une erreur est survenue";
				return "Echec de l'exécution du script groovy";
			}
		}
	}
	
    print "Nombre de méls erronés trouvés : " + nbErreur;
    print "Nombre de méls modifiés : " + nbCorrection + "/" + nbErreur + " méls en erreur";
    
print "-------------------------------------------------------------------------------";
print "Fin du script groovy de  modification des mails utilisateurs";
return "Fin du script groovy";
