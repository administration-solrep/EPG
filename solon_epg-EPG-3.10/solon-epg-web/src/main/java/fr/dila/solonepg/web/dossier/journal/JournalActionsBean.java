package fr.dila.solonepg.web.dossier.journal;

import static org.jboss.seam.annotations.Install.FRAMEWORK;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.web.journal.STJournalActionsBean;

/**
 * ActionBean de gestion du dossier.
 * 
 * @author ARN
 */
@Name("journalActions")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = FRAMEWORK + 1)
public class JournalActionsBean extends STJournalActionsBean implements Serializable {

	private static final long	serialVersionUID	= 1L;

	private static final Log	log					= LogFactory.getLog(JournalActionsBean.class);

	@In(required = true, create = true)
	protected SSPrincipal		ssPrincipal;

	@Override
	protected void initCategoryList() {
		if (log.isDebugEnabled()) {
			log.debug("initialisation de la liste des catégories du journal");
		}

		setContentViewName(getDefaultContentViewName());

		categoryList.add("");
		categoryList.add(STEventConstant.CATEGORY_BORDEREAU);
		categoryList.add(STEventConstant.CATEGORY_PARAPHEUR);
		categoryList.add(STEventConstant.CATEGORY_FEUILLE_ROUTE);
		categoryList.add(STEventConstant.CATEGORY_REPRISE);

		if (ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.TRAITEMENT_PAPIER_READER)) {
			categoryList.add(SolonEpgEventConstant.CATEGORY_TRAITEMENT_PAPIER);
		}

		DocumentModel dossierDoc = navigationContext.getCurrentDocument();

		if (dossierDoc != null) {
			Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			// on récupère l'identifiant du ministère d'appartenance de l'acte
			String idMinistere = dossier.getMinistereAttache();

			if (ssPrincipal.getMinistereIdSet().contains(idMinistere)) {
				categoryList.add(SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR);
				categoryList
						.add(SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG);
			}
		}

		if (ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.FOND_DOSSIER_REPERTOIRE_SGG)) {
			categoryList.add(SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG);
			categoryList.add(SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG);
		}

		categoryList.add(SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER);
		categoryList.add(STEventConstant.CATEGORY_FDD);

		categoryList.add(SolonEpgEventConstant.CATEGORY_PROCEDURE_PARLEMENT);
		categoryList.add(STEventConstant.CATEGORY_ADMINISTRATION);

	}

	@Override
	public String getDefaultContentViewName() {
		return "JOURNAL_DOSSIER";
	};

}
