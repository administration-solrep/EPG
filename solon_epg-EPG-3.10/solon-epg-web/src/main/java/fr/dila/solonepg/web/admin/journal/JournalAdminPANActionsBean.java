package fr.dila.solonepg.web.admin.journal;

import java.io.Serializable;

import javax.persistence.NoResultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.core.util.UnrestrictedGetDocumentRunner;

/**
 * ActionBean de gestion du journal de l'espace d'administration.
 * 
 * @author BBY, ARN
 */
@Name("journalAdminPANActions")
@Scope(ScopeType.CONVERSATION)
public class JournalAdminPANActionsBean extends JournalAdminActionsBean implements Serializable {

	private static final long	serialVersionUID	= 1L;

	private static final Log	log					= LogFactory.getLog(JournalAdminPANActionsBean.class);

	@In(required = true, create = true)
	protected SSPrincipal		ssPrincipal;

	@Override
	public String getDossierRef(String dossierRef) {
		if (dossierRef == null || dossierRef.isEmpty()) {
			return dossierRef;
		}
		DocumentModel docModel = null;
		try {
			docModel = documentManager.getDocument(new IdRef(dossierRef));
		} catch (ClientException e) {
			log.debug("erreur lors de la récupération du dossier; essai de récupération en mode unrestricted");
			try {
				// Récupération en mode unrestricted du dossier auquel l'utilisateur actuel n'a pas accès
				UnrestrictedGetDocumentRunner uGet = new UnrestrictedGetDocumentRunner(documentManager);
				docModel = uGet.getByRef(new IdRef(dossierRef));
			} catch (ClientException e1) {
				log.debug("erreur lors de la récupération du dossier");
			}
		}
		if (docModel != null) {
			Dossier dossier = docModel.getAdapter(Dossier.class);
			if (dossier != null && dossier.getNumeroNor() != null) {
				return dossier.getNumeroNor();
			}
		}
		log.debug("erreur lors de la récupération du dossier");
		log.debug("recherche dans les dossiers supprimés");
		DossierService dossierService = SolonEpgServiceLocator.getDossierService();
		try {
			String nor = dossierService.getNorDossierSupprime(dossierRef);
			if (nor != null && !nor.isEmpty()) {
				return nor;
			}
		} catch (ClientException e) {
			log.debug(
					"erreur lors de la recherche dans les dossiers supprimés. Impossible de trouver le nor du dossier");
		} catch (NoResultException e) {
			log.debug("NOR supprimé non retrouvé");
			return "Dossier supprimé";
		}
		return dossierRef;
	}
	
	@Override
	public String getDefaultContentViewName() {
		return "PAN_JOURNAL";
	};

	@Override
	protected void initCategoryList() {
		setContentViewName(getDefaultContentViewName());

		categoryList.add("");
		categoryList.add(SolonEpgEventConstant.CATEGORY_LOG_PAN_APPLICATION_LOIS);
		categoryList.add(SolonEpgEventConstant.CATEGORY_LOG_PAN_APPLICATION_ORDO);
		categoryList.add(SolonEpgEventConstant.CATEGORY_LOG_PAN_SUIVI_HABILITATIONS);
		categoryList.add(SolonEpgEventConstant.CATEGORY_LOG_PAN_TRAITES_ACCORD);
		categoryList.add(SolonEpgEventConstant.CATEGORY_LOG_PAN_TRANSPO_DIRECTIVES);
		categoryList.add(SolonEpgEventConstant.CATEGORY_LOG_PAN_GENERAL);
		categoryList.add(SolonEpgEventConstant.CATEGORY_LOG_PAN_RATIFICATION_ORDO);
	}

	@Override
	public boolean isAccessAuthorized() {
		return ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_READER)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_READER)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_READER)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_READER)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_READER)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_READER)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_LOIS_UPDATER)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APPLICATION_ORDONNANCES_UPDATER)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_RATIFICATION_UPDATER)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_SUIVIS_UPDATER)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRAITE_UPDATER)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_TRANSPOSITION_UPDATER)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_EXPORT_DATA_UPDATER)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_PARAM_LEGIS_UPDATER)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_PUBLI_LEGIS_EXEC)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_LOI_MINISTERE)
				|| ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_ORDONNANCE_MINISTERE);
	}
}
