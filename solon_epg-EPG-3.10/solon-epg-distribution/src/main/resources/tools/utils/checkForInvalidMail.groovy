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
 * jbt
 * script groovy de récupération des utilisateurs dont l'identifiant ne remplit pas les critères
 * 
 */

class FixUtils {    
    public static final String MODE_INFO = "INFO";
    public static final String MODE_FIX = "FIX";
}

print "Début du script groovy de récupération des utilisateurs dont le mail ne remplit pas les critères";
print "-------------------------------------------------------------------------------";

	String mel = Context.get("mel");
	String mode = null;
	emailPattern = /[A-Za-z0-9._-]+@[A-Za-z0-9._-]{2,}\.[A-Za-z]{2,4}/
	if(StringUtils.isBlank(mel)) {
		print "Argument mel non trouvé. mode par défaut : 'INFO' (Pour effectuer les modifications, vous devez spécifier : -ctx \"mel='nouvelle adresse'\" ";
	    mode = FixUtils.MODE_INFO;    
	} else {
		mel = mel.replace("'", "");
		if (!StringUtils.isBlank(mel) && !(mel ==~ emailPattern) ) {
	        print "Mel non reconnu - Vous devez spécifier : -ctx \"mel='nouvelle adresse'\" où 'nouvelle adresse' doit correspondre à une adresse mel valide"
	        return "Echec du script groovy";
	    }
		print "Toutes les adresses mèls non valides seront remplacées par : " + mel;
		mode = FixUtils.MODE_FIX;
	}

	final UserManager userManager = STServiceLocator.getUserManager();
	final STMailService mailService = STServiceLocator.getSTMailService();
	
	// On récupère tous les utilisateurs par ordre alphabétique
	Map<String, Serializable> filter = new HashMap<String, Serializable>();
	final DocumentModelList userModelList = userManager.searchUsers(filter, null);
	final List<STUser> allUsersList = new ArrayList<STUser>();
	
	for (DocumentModel userDocModel : userModelList) {
		STUser user = userDocModel.getAdapter(STUser.class);
		if (user.isActive()) {
			allUsersList.add(user);
		}
	}
	boolean wrongMail = false;
	int nbErreur = 0
	int nbCorrection = 0;
	for (STUser user : allUsersList) {
    	wrongMail = false;
    	if (user.getEmail() == null) {
    		wrongMail = true;
    	} else {
	    	String emailsStr = ((String) user.getEmail()).trim();
	        String[] emails = emailsStr.split(",|;| ");
	        for (String email : emails) {
	            if ( !StringUtils.isBlank(email) && !(email ==~ emailPattern) ) {
	            	wrongMail = true;
	            }
	        }
    	}
    	if (wrongMail) {
    		nbErreur++;
    		if (FixUtils.MODE_INFO.equals(mode)) {
    			print "L'utilisateur " + user.getUsername() + " possède une adresse mèl non valide : " + user.getEmail();
    		}
    		else if (FixUtils.MODE_FIX.equals(mode)) {
    			user.setEmail(mel);
    			userManager.updateUser(user.getDocument());
    			nbCorrection++;
    			print "Adresse mèl changée pour l'utilisateur " + user.getUsername();
    		} else {
    			print "Une erreur est survenue";
    			return "Echec de l'exécution du script groovy";
    		}
    	}
    }
    print "Nombre de mèls erronés trouvés : " + nbErreur + "/" + allUsersList.size() + " utilisateurs";
    print "Nombre de mèls modifiés : " + nbCorrection + "/" + nbErreur + " mèls en erreur";
    
print "-------------------------------------------------------------------------------";
print "Fin du script groovy de récupération des utilisateurs dont l'identifiant ne remplit pas les critères";
return "Fin du script groovy";
