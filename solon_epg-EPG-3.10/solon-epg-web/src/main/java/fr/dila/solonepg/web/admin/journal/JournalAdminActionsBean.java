package fr.dila.solonepg.web.admin.journal;

import static org.jboss.seam.annotations.Install.FRAMEWORK;

import java.io.Serializable;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
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
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.web.journal.STJournalAdminActionsBean;

/**
 * ActionBean de gestion du journal de l'espace d'administration.
 * 
 * @author BBY, ARN
 */
@Name("journalAdminActions")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = FRAMEWORK + 1)
public class JournalAdminActionsBean extends STJournalAdminActionsBean implements Serializable {

	private static final long	serialVersionUID	= 1L;

	private static final Log	log					= LogFactory.getLog(JournalAdminActionsBean.class);

	private int					excelLineLimit		= 65000;

	@In(required = true, create = true)
	protected SSPrincipal		ssPrincipal;

	protected String			currentCommentaire;

	protected List<String>		currentCategories;

	@Override
	protected void initCategoryList() {
		setContentViewName(getDefaultContentViewName());

		categoryList.add("");
		categoryList.add(STEventConstant.CATEGORY_BORDEREAU);
		categoryList.add(STEventConstant.CATEGORY_PARAPHEUR);
		categoryList.add(STEventConstant.CATEGORY_FEUILLE_ROUTE);
		categoryList.add(STEventConstant.CATEGORY_REPRISE);

		if (ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.TRAITEMENT_PAPIER_READER)) {
			categoryList.add(SolonEpgEventConstant.CATEGORY_TRAITEMENT_PAPIER);
		}

		if (ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.FOND_DOSSIER_REPERTOIRE_SGG)) {
			categoryList.add(SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG);
			categoryList.add(SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG);
		}

		if (ssPrincipal.isMemberOf(STBaseFunctionConstant.FEUILLE_DE_ROUTE_MODEL_VALIDATOR)) {
			// admin fonctionnel, il voit tout
			categoryList.add(SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR);
			categoryList.add(SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG);
			categoryList.add(SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG);
			categoryList.add(SolonEpgEventConstant.CATEGORY_TRAITEMENT_PAPIER);
		}

		categoryList.add(SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER);
		categoryList.add(STEventConstant.CATEGORY_FDD);

		categoryList.add(SolonEpgEventConstant.CATEGORY_PROCEDURE_PARLEMENT);
		categoryList.add(STEventConstant.CATEGORY_ADMINISTRATION);

	}

	/**
	 * On affiche le NOR du dossier.
	 */
	@Override
	public String getDossierRef(String dossierRef) {
		if (dossierRef == null || dossierRef.isEmpty()) {
			return dossierRef;
		}
		DocumentModel docModel = null;
		try {
			docModel = documentManager.getDocument(new IdRef(dossierRef));
		} catch (ClientException e) {
			log.debug("erreur lors de la récupération du dossier");
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
			log.debug("erreur lors de la recherche dans les dossiers supprimés. Impossible de trouver le nor du dossier");
		} catch (NoResultException e) {
			log.debug("NOR supprimé non retrouvé");
			return "Dossier supprimé";
		}
		return dossierRef;
	}

	public void setExcelLineLimit(int excelLineLimit) {
		this.excelLineLimit = excelLineLimit;
	}

	public int getExcelLineLimit() {
		return excelLineLimit;
	}

	public void reset() {
		super.reset();
		currentCommentaire = null;
		currentCategories = null;
	}

	public String getCurrentCommentaire() {
		return currentCommentaire;
	}

	public void setCurrentCommentaire(String currentCommentaire) {
		this.currentCommentaire = currentCommentaire;
	}

	public List<String> getCurrentCategories() {
		return currentCategories;
	}

	public void setCurrentCategories(List<String> currentCategories) {
		if (currentCategories.size() == 1 && currentCategories.get(0).isEmpty())
			this.currentCategories = null;
		else
			this.currentCategories = currentCategories;
	}

}
