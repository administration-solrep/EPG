import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.DocumentModelList;
import java.lang.String;
import org.apache.commons.lang.StringUtils;
import java.util.Collections;
import org.nuxeo.common.utils.*;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.common.utils.IdUtils;
import fr.dila.solonepg.api.cases.Dossier;

import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.util.UnrestrictedQueryRunner;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.DocumentRef;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.service.FondDeDossierModelService;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.core.util.StringUtil;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import org.nuxeo.runtime.transaction.TransactionHelper;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModel;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModelNode;

	//Script groovy pour la FEV505 - Ajout du répertoire saisine rectificative sur les dossiers concernés par ce répertoire pour les dossiers à l'état initiés et lancés
	println("lancement du script de création du répertoire 'saisine rectificative' pour les dossiers à l'état initié ou lancé");
	final List<String> params = new ArrayList<String>();
	params.add(TypeActeConstants.TYPE_ACTE_LOI);
	params.add(TypeActeConstants.TYPE_ACTE_ORDONNANCE);
	params.add(TypeActeConstants.TYPE_ACTE_DECRET_CE);
	params.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND);
	params.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37);
	params.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM);
	params.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND);
	params.add(TypeActeConstants.TYPE_ACTE_ARRETE_CE);
	params.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_CE);
	params.add(TypeActeConstants.TYPE_ACTE_DEMANDE_AVIS_CE);
	params.add(TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE);
	params.add(TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION);
	params.add(TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE);
	final StringBuilder query = new StringBuilder("SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d WHERE (d.dos:archive = 0 AND d.dos:statut IN (1,2) AND d.dos:typeActe IN (");
	query.append(StringUtil.getQuestionMark(params.size()));
	query.append(") )");
	List<DocumentModel> listDocs = new ArrayList<DocumentModel>();
	println("Recherche des dossiers qui sont concernés par le répertoire Saisine Rectificative");
	listDocs = QueryUtils.doUnrestrictedUFNXQLQueryAndFetchForDocuments(Session,STConstant.DOSSIER_DOCUMENT_TYPE, query.toString(), params.toArray());
	println("Parcours des dossiers et ajout du répertoire");
	FondDeDossierModelService	fondDossierModelService = SolonEpgServiceLocator.getFondDeDossierModelService();
	for(DocumentModel doc : listDocs) {
		println(doc.getTitle());
		//récupération du répertoire documentation jointe du fond de dossier
		DocumentModel docATrouver = Session.getDocument(new PathRef(doc.getPath().append("Fond de dossier/Répertoire accessible à tous les utilisateurs/Documentation jointe").toString() ));
		
		if(!Session.exists(new PathRef(doc.getPath().append("Fond de dossier/Répertoire accessible à tous les utilisateurs/Documentation jointe/Saisine rectificatuve").toString() ))) {
			//On y ajoute un dossier Saisine Rectificative
			fondDossierModelService.createFondDossierModelFolderElement(docATrouver, Session,
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_SAISINE_RECTIFICATIVE);	
			//On sauvegarde les modifications
	    	Session.saveDocument(doc);
	    	Session.save();
			TransactionHelper.commitOrRollbackTransaction();
			TransactionHelper.startTransaction();
		}
	}
	
	println("Fin du script");
	
	return "Fin du script groovy";