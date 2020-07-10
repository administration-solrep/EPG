import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.ACL;
import org.nuxeo.ecm.core.api.security.ACP;

import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;

import com.google.common.collect.Lists;

/**
 * script groovy de modification des permissions sur les dossiers links : ce script s'assure que les dossiers links d'un ministère possèdent bien le droit pour le ministère
 * !!!! ATTENTION !!!!! Ce script n'est pas à exécuter en l'état, il faut toujours s'assurer que les ministères définis dans la méthode runTraitement(CoreSession session) correspondent bien aux ministères à modifier
 */
class FixDossierPermsUtils {
	
	public static List<String> getListDocsToModify(String idPoste, CoreSession session) {

		List<String> ids = new ArrayList<String>();
		try {
			ids.addAll(QueryUtils.doQueryForIds(session,
					"SELECT * FROM DossierLink WHERE cmdist:all_action_participant_mailboxes='poste-" + idPoste + "' and ecm:currentLifeCycleState='todo'"));
		} catch (Exception e) {

			println("Une erreur est survenue lors de la récupération des DossierLinks pour la corbeille : "+idPoste);
			e.printStackTrace();
		}
		return ids;
	}

	public static boolean checkIfDocumentHasPerm(String permUserLabel, DocumentModel doc) {
		boolean hasPerm = false;
		try {

			ACP listOfPerms = doc.getACP();
			ACL listOfDroits = listOfPerms.getACL("security");
			if (listOfDroits != null) {
				for (int i = 0; i < listOfDroits.size(); i++) {
					// On vérifie que les droits sont correctes pour l'utilisateur (libellé des utilisateur +
					// lecture/écriture + autorisé
					if (listOfDroits.get(i).getUsername().equals(permUserLabel) && listOfDroits.get(i).isGranted()
							&& listOfDroits.get(i).getPermission().equals("ReadWrite")) {
						println("Le document : "+doc.getId()+" disposait déjà du droit pour le ministère");
						hasPerm = true;
					}
				}
			}

		} catch (ClientException e) {

			println("Une erreur est survenue lors de la vérification de la présence de la permission dans le document : "+doc.getId());
			e.printStackTrace();
		}
		return hasPerm;
	}

	public static boolean addPermissionToDoc(String permLabel, DocumentModel doc, CoreSession session) {
		boolean addPerm = false;
		try {

			ACP listOfPerms = doc.getACP();
			ACL listOfDroits = listOfPerms.getACL("security");
			ACE nouvellePerm = new ACE(permLabel, "ReadWrite", true);
			listOfDroits.add(nouvellePerm);
			session.setACP(doc.getRef(), listOfPerms, false);
			session.save();
			addPerm = true;

		} catch (ClientException e) {
			println("Une erreur est survenue lors de l'ajout de la permission au document : "+doc.getId());
			e.printStackTrace();
		}

		return addPerm;
	}
	
	public static void traiterPosteCorbeille(String idPoste, String idMinistere, CoreSession session) {
		println("Début du traitement du poste "+idPoste);
		try {
			List<String> lstIds = getListDocsToModify(idPoste, session);

			if (lstIds != null && !lstIds.isEmpty()) {
				for (String id : lstIds) {
					DocumentModel doc = session.getDocument(new IdRef(id));
					String permLabel = "dossier_link_updater_min-" + idMinistere;
					boolean hasPerm = checkIfDocumentHasPerm(permLabel, doc);
					if (!hasPerm) {
						if (!addPermissionToDoc(permLabel, doc, session)) {
							println("La permission" +permLabel+ " n'a pas été ajoutée au document "+id);
						} else {

							println("On a bien rajouté la permission "+permLabel+" au document : "+id);
						}
					}

				}
			}

		} catch (ClientException e) {
			println("Une erreur est survenue lors du traitement du ministère "+idMinistere);
			e.printStackTrace();
		}
		println("Fin du traitement du poste "+idPoste);
	}

	public static void runTraitement(CoreSession session) {
		try {
			List<String> ministereIds=Lists.newArrayList("60001415");
			
			for(String ministereId : ministereIds){
				STMinisteresService ministereService = STServiceLocator.getSTMinisteresService();
				EntiteNode minECF = ministereService.getEntiteNode(ministereId);
	
				STPostesService postesService = STServiceLocator.getSTPostesService();
				List<String> lstPostes = postesService.getPosteIdInSubNode(minECF);
							
				if (lstPostes != null && !lstPostes.isEmpty()) {
					println("On s'apprête à traiter "+lstPostes.size()+" postes");
					for (String postId : lstPostes) {
						traiterPosteCorbeille(postId, ministereId, session);
					}
				} else {
					println("Pas de poste trouvé pour le ministère "+ministereId);
				}
			}

		} catch (Exception e) {
			println("Un problème est survenu lors de la récupération des postes du ministère ECF");
			e.printStackTrace();
		}
	}
	
	
}

println("Début du script de mise à jour des permissions sur les dossiers links");

FixDossierPermsUtils.runTraitement(Session);	
println("Fin du script de mise à jour des permissions sur les dossiers links");

return "Le script a été exécuté, consultez le fichier server.log pour le suivi de son déroulement";