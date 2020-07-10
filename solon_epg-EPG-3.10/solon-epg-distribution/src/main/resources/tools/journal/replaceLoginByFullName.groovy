import javax.persistence.EntityManager;
import javax.persistence.Query;
import fr.dila.solonepg.core.recherche.UserRequeteur;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunVoid;
import fr.dila.st.api.user.STUser;
import fr.dila.solonepg.api.service.SolonEpgUserManager;
import fr.dila.st.core.service.STServiceLocator;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;

import fr.dila.solonepg.core.service.ChangementGouvernementServiceImpl;

class SolonRunVoid implements  RunVoid{

            public void runWith(EntityManager em) throws ClientException {
			SolonEpgUserManager userManger = (SolonEpgUserManager) STServiceLocator.getUserManager();
			String query = "(&(&(objectClass=personne)(uid=*)))";
			List<String> args= new ArrayList<String>();
			UserRequeteur userRequeteur = new UserRequeteur();
			DocumentModelList usersList = userRequeteur.searchUsers(query,args,null);
		            for (DocumentModel user : usersList) {
                STUser sTUser = user.getAdapter(STUser.class);

             
                String firstName = sTUser.getFirstName();
                String lastName = sTUser.getLastName();
                String userName = sTUser.getUsername();

                println("start updating journal for user :"+userName);
                		        
                if (userManger.getAdministratorIds() == null || !userManger.getAdministratorIds().contains(userName)) {

                    if (firstName == null) {
                        firstName = "";
                    }
                    if (lastName == null) {
                        lastName = "";
                    }
                Query nativeQuery = em.createNativeQuery("UPDATE nxp_logs SET log_principal_name = :log_principal_name WHERE log_principal_name = :userName");
                nativeQuery.setParameter("log_principal_name", firstName +" "+lastName);
                nativeQuery.setParameter("userName", userName);
                nativeQuery.executeUpdate();
                em.flush();
                }
                 println("end updating journal for user :"+userName);
             
             
            }
            }
}    

	SolonRunVoid runVoid  = new SolonRunVoid() ;  

        try {
            ChangementGouvernementServiceImpl.getOrCreatePersistenceProvider().run(true, runVoid);
        } catch (ClientException e) {
        	return e.getMessage() ;
            e.printStackTrace();
        }


return "Fin du script groovy";