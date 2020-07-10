import java.lang.String;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.apache.commons.lang.StringUtils;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.VocabularyConstants;

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
		//Passage au statut lancé
		println("changement du statut vers \"lancé\" ");
		Dossier dossier = model.getAdapter(Dossier.class);
		String state = dossier.getDocument().getCurrentLifeCycleState();
		String statut = dossier.getStatut();
		String resultat="";
		if(state.equals("running") && statut.equals(VocabularyConstants.STATUT_INITIE)){
			println("dossier relancé : " + dossier.getNumeroNor() );
			dossier.setStatut(VocabularyConstants.STATUT_LANCE);
			Session.saveDocument(dossier.getDocument());
			resultat = "success  ";
		}else{
			println("Le dossier n'est pas au statut Initié ou déjà en cours de traitement. Script inutile ici.");
			resultat = "fail";
		}
		return "Fin du script : "+resultat;
	}
}
