import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import fr.dila.cm.cases.CaseConstants;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import fr.dila.ecm.platform.routing.api.DocumentRoute;
import fr.dila.ecm.platform.routing.api.DocumentRouteElement;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.api.feuilleroute.STFeuilleRoute;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Script groovy pour réparer un dossier qui a vu sa validation être interrompue en milieu d'execution (caselink à l'état "done", mais étape "running")
 * 
 * Paramètre :
 * - nor : nor du dossier (obligatoire)
 * 
 */
class FixDossierUtils {    

	/**
	 * Retourne le document d'un dossier par son Nor
	 * 
	 * @return DocumentModel Dossier correspondant au nor en parametre, null si non trouvé
	 */
	public static DocumentModel getDossierDocByNor (session, nor) {
		String queryDossier = "SELECT * FROM Dossier WHERE dos:numeroNor='" + nor + "'";
		DocumentModelList listDossier = session.query(queryDossier);
		if(listDossier == null || listDossier.isEmpty()) {
			print "Pas de dossier correspondant au nor passé";
			return null;
		}
		if (listDossier.size() >1) {
			print  "Plusieurs dossiers correspondant au nor passé. ";
			return null;
		}
		return listDossier.get(0);
	}

	/**
	 * Retourne le DocumentModelList des dossier link par l'id du dossier
	 * 
	 * @return DocumentModelList DossierLink correspondants à l'id en parametre, null si non trouvé
	 */
	public static DocumentModelList getDossiersLinkDoneListByIdDossier (session, idDossier) {
		String queryDossierLink = "SELECT * FROM DossierLink WHERE cslk:caseDocumentId='" + idDossier + "' and ecm:currentLifeCycleState = 'done'";
		DocumentModelList listDossierLink = session.query(queryDossierLink);
		if(listDossierLink == null || listDossierLink.isEmpty()) {
			print "Pas de dossier link correspondants à l'id passé";
			return null;
		}
		return listDossierLink;
	}

	/**
	 * Récupère la liste des étapes en cours d'une feuille de route
	 * @param routeId id de la route dont on souhaite récupérer les étapes en cours
	 * @return DocumentModelList liste des étapes en cours, null si non trouvée, ou aucune
	 */
	public static DocumentModelList getRunningStepForRouteId (session, routeId) {
		String queryRoute = "SELECT * FROM RouteStep WHERE rtsk:documentRouteId='" + routeId + "' and ecm:currentLifeCycleState = 'running'";
		DocumentModelList listRunningStep = session.query(queryRoute);
		if(listRunningStep == null || listRunningStep.isEmpty()) {
			print "Pas d'étape en cours pour à l'id de route passé : " + routeId;
			return null;
		}
		return listRunningStep;
	}

	/**
	 * Supprime un caseLink en utilisant son id
	 * @param caseLinkId id du caseLink a supprimer
	 * 
	 */
	public static void removeCaseLinkById (session, caseLinkId) {
		print "Suppression caseLink id : " + caseLinkId;
		session.removeDocument(new IdRef(caseLinkId));
	}

	/**
	 * Passe une étape de feuille de route de l'état running à l'état done
	 * @param stepId id de l'étape à modifier
	 *
	 */
	public static void setRunningStepToDone (session, stepId) {
		print "Passage d'étape de running à done pour étape id : " + stepId;
		session.followTransition(new IdRef(stepId), "toDone")
	}

	/*
	 * Redémarre une feuille de route
	 * @param id id de la feuille de route à redémarrer
	 *
	 */
	public static void restartFeuilleRoute (session, routeId) {
		DocumentRoute startRoute = null;
		DocumentModel startRouteDoc = session.getDocument(new IdRef(routeId));
        if (startRouteDoc != null) {
            try {
                startRoute = startRouteDoc.getAdapter(DocumentRoute.class);
			} catch (Exception e) {
	            print "L'id de la route à démarrer ne correspond pas à une feuille de route" + e;
				return;
        	}
        } else {
            print "L'id de la route à démarrer ne correspond pas à une feuille de route";
			return;
        }
		if (startRoute != null) {
            if (startRoute.getDocument().getCurrentLifeCycleState().equals(DocumentRouteElement.ElementLifeCycleState.done.toString())) {
                startRoute.backToReady(session);
            }
            startRoute.run(session);
            print "Route démarrée";
			return;
        } 
	}

	

    public static boolean fixDossierByNor (session, nor) {        
        
        if (StringUtils.isEmpty(nor)) {
            print "Le paramètre nor ne doit pas être vide";
			return false;
        }
		print "Récupération dossier " + nor;
        DocumentModel dossierDoc = getDossierDocByNor (session, nor);        
        if (dossierDoc == null) {
			print "Document dossier est null";
			return false;
        }

		Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		String routeId = dossier.getLastDocumentRoute();

		DocumentModelList listDossierLink = getDossiersLinkDoneListByIdDossier (session, dossierDoc.getId());
		DocumentModelList listRunningSteps = getRunningStepForRouteId (session, routeId);		
		boolean restart = false;
		
		// On supprime les dossiers link à l'état done pour les étapes toujours en running
		// et on passe lesdites étapes à l'état done
		for(DocumentModel dossierLinkDoc : listDossierLink) {
			DossierLink dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);
			String stepId = dossierLink.getRoutingTaskId();
			DocumentModel stepDoc = session.getDocument(new IdRef(stepId));
			if (listRunningSteps.contains(stepDoc)) {
				print "Suppression des DossierLinks à done pour les étapes en running";
				removeCaseLinkById (session, dossierLinkDoc.getId());
				setRunningStepToDone (session, stepId);
				restart = true;
			}
		}
		
		if (restart) {
			restartFeuilleRoute (session, routeId);
		}
		return true;
    }
}

print "Début script groovy de déblocage de feuille de route";
print "-------------------------------------------------------------------------------";
String dossierNor = Context.get("nor");

if(StringUtils.isBlank(dossierNor)) {
	return "Argument nor non trouvé. Vous devez spécifier : -ctx \"nor='norDossier'\" ";
}
dossierNor = dossierNor.replace("'", "");
if (!FixDossierUtils.fixDossierByNor (Session, dossierNor)) {
	print "Fin du script - Une erreur a provoqué son arrêt avant sa fin";
	return "Fin du script - Une erreur a provoqué son arrêt avant sa fin";
} 
print "-------------------------------------------------------------------------------";
print "Fin du script groovy de déblocage de feuille de route";
return "Fin du script groovy";
