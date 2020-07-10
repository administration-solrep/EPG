import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import fr.dila.ecm.platform.routing.api.DocumentRoute;
import fr.dila.ecm.platform.routing.api.DocumentRouteStep;
import org.nuxeo.ecm.core.api.DocumentModelList;
import java.lang.String;
import org.apache.commons.lang.StringUtils;
import fr.dila.st.api.feuilleroute.STRouteStep;

String stepUuid = Context.get("stepUUID");

if(StringUtils.isBlank(stepUuid)) {
	return "Argument stepUUID non trouvé. Vous devez spécifier : -ctx \"stepUUID='uuidEtape'\" ";
} else {
	println("Running runStep : stepUUID = " + stepUuid);

	stepUuid = stepUuid.replace("'", "");
	
	DocumentModel model = Session.getDocument(new IdRef(stepUuid));

	if(model == null){
		println("Argument stepUUID non valide, étape non trouvée ");
	} else{

		println("retrieve DossierLink from Step");

		String queryDL = "SELECT * FROM DossierLink WHERE acslk:stepDocumentId = '" + stepUuid + "' AND acslk:deleted = 0 ";
		DocumentModelList list = Session.query(queryDL);

		if(list != null && !list.isEmpty()) {
			println("Le dossier est distribué dans des corbeilles ! : ");
			for (DocumentModel documentModel : list) {
				println("   DossierLinkId = " + documentModel.getId());
			}
			return "Fin du script, etape non redemarree, dossier distribué dans des corbeilles, voir log pour plus d'information";
		} else {

			println("retrieve DossierLink from Dossier");

			STRouteStep stStep = model.getAdapter(STRouteStep.class);

			String queryDossier = "SELECT * FROM Dossier WHERE dos:lastDocumentRoute = '" + stStep.getDocumentRouteId() + "' ";
			DocumentModelList listDossier = Session.query(queryDossier);
			if(listDossier == null || listDossier.isEmpty()) {
				return "Fin du script, dossier introuvable";
			} else {
				for (DocumentModel documentModel : listDossier) {
					String queryDossierLink = "SELECT * FROM DossierLink WHERE acslk:caseDocumentId = '" + documentModel.getId() + "' AND acslk:stepDocumentId='" + stepUuid + "' AND acslk:deleted = 0 ";
					DocumentModelList listDL = Session.query(queryDossierLink);

					if(listDL != null && !listDL.isEmpty()){
						println("Le dossier " + documentModel.getId() + " est distribué dans des corbeilles ! : ");
						for (DocumentModel docuModel : listDL) {
							println("   DossierLinkId = " + docuModel.getId());
						}
						
						return "Fin du script, etape non redemarree, etape distribuée dans des corbeilles, voir log pour plus d'information";
					}else {
						println("ok running step");
						DocumentRouteStep step = model.getAdapter(DocumentRouteStep.class);
						step.run(Session);

						println("script ok");
						return "Fin du script, etape redemarree";
					}
				}
			}
		}
	}
}
