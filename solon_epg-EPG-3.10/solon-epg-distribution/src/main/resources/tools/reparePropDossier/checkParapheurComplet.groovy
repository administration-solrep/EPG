import java.lang.String;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.apache.commons.lang.StringUtils;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

String dossierUuid = Context.get("dossierUUID");
if(StringUtils.isBlank(dossierUuid)) {
	return "Argument dossierUUID non trouvé. Vous devez spécifier : -ctx \"dossierUUID='uuidDossier'\" ";
} else {
	println("Running runStep : dossierUUID = " + dossierUuid);

	dossierUuid = dossierUuid.replace("'", "");
	
	DocumentModel model = Session.getDocument(new IdRef(dossierUuid));

	if(model == null){
		return "Argument dossierUuid non valide, dossier non trouvé";
	} else{				
        ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
		println("Vérification données parapheur");
		Dossier dossier = model.getAdapter(Dossier.class);
		println("état complétude parapheur avant : "+ dossier.getIsParapheurComplet());
		parapheurService.checkParapheurComplet(model, Session);
		println("état complétude parapheur après : "+ dossier.getIsParapheurComplet());
		
		return "Fin du script - état parapheur : "+dossier.getIsParapheurComplet();
	}
}
