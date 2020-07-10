package fr.dila.solonepg.web.dossier;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManagerBean;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.DossierSignaleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.espace.CorbeilleActionsBean;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.service.CorbeilleService;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.web.lock.STLockActionsBean;

/**
 * Web Bean qui permet de gérer les actions relatives aux listes de dossiers.
 * 
 * @author jtremeaux
 */
@Name("dossierListingActions")
@Scope(ScopeType.CONVERSATION)
public class DossierListingActionsBean implements Serializable {
	/**
	 * Serial UID.
	 */
	private static final long							serialVersionUID	= 1L;

	/**
	 * Logger.
	 */
	private static final Log							log					= LogFactory
																					.getLog(DossierListingActionsBean.class);

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(create = true, required = false)
	protected transient DocumentsListsManagerBean		documentsListsManager;

	@In(create = true)
	protected transient ResourcesAccessor				resourcesAccessor;

	@In(create = true)
	protected transient NavigationContext				navigationContext;

	@In(create = true, required = false)
	protected transient CorbeilleActionsBean			corbeilleActions;

	@In(create = true, required = false)
	protected transient FacesMessages					facesMessages;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean	routingWebActions;

	@In(create = true, required = false)
	protected transient STLockActionsBean				stLockActions;

	/**
	 * Retourne la liste des étapes actives (CaseLink présent) pour un dossier, que l'utilisateur peut actionner.
	 * 
	 * @param dossierDoc
	 *            Dossier
	 * @return Liste de DossierLink
	 * @throws ClientException
	 */
	public List<DocumentModel> findDossierLink(DocumentModel dossierDoc) throws ClientException {
		// Recherche les DossierLink que l'utilisateur peut actionner
		final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();

		return corbeilleService.findDossierLink(documentManager, dossierDoc.getId());
	}

	/**
	 * Charge un dossier comme document courant à partir d'un espace de recherche, suivi, etc. Si l'utilisateur a la
	 * main sur le CaseLink (parce que le dossier lui est destiné, ou qu'il est administrateur), le CaseLink est aussi
	 * chargé pour que l'utilisateur puisse effectuer des actions de distribution.
	 * 
	 * @param dossierDoc
	 *            Dossier
	 * @return Vue
	 * @throws ClientException
	 */
	public String navigateToDossier(DocumentModel dossierDoc) throws ClientException {
		return navigateToDossier(dossierDoc, null);
	}

	public String navigateToDossier(DocumentModel dossierDoc, String view) throws ClientException {
		// Recherche les DossierLink que l'utilisateur peut actionner
		final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
		List<DocumentModel> dossierLinkList = corbeilleService.findDossierLink(documentManager, dossierDoc.getId());

		DocumentModel dossierLinkDoc = null;
		if (dossierLinkList.size() > 0) {
			if (dossierLinkList.size() > 1) {
				log.info("Plus d'un DossierLink trouvé, selection du premier");
			}
			dossierLinkDoc = dossierLinkList.get(0);
		}

		return navigateToDossierLink(dossierDoc, dossierLinkDoc, view);
	}

	/**
	 * Charge un dossier comme document courant à partir d'un espace de recherche, suivi, etc. Charge aussi le
	 * DossierLink pour prendre la main sur la distribution.
	 * 
	 * @param dossierDoc
	 *            Dossier
	 * @param dossierLinkDoc
	 *            DossierLink
	 * @return Vue
	 * @throws ClientException
	 */
	public String navigateToDossierLink(DocumentModel dossierDoc, DocumentModel dossierLinkDoc) throws ClientException {
		return navigateToDossierLink(dossierDoc, dossierLinkDoc, null);
	}

	public String navigateToDossierLink(DocumentModel dossierDoc, DocumentModel dossierLinkDoc, String view)
			throws ClientException {
		// Charge le dossier comme document courant
		navigationContext.navigateToDocument(dossierDoc);

		// Charge le DossierLink dans la corbeille
		DossierLink dossierLink = null;
		if (dossierLinkDoc != null) {
			dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);
		}
		corbeilleActions.setCurrentDossierLink(dossierLink);

		if (!StringUtils.isEmpty(view)) {
			routingWebActions.setFeuilleRouteView(view);
		}

		return view;
	}

	/**
	 * Retourne le libellé permettant de sélectionner un DossierLink dans le cas de DossierLink multiples.
	 * 
	 * @param dossierLink
	 *            DossierLink
	 * @return Libellé
	 * @throws ClientException
	 */
	public String getDossierLinkLabel(DocumentModel dossierLinkDoc) throws ClientException {
		DossierLink dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);

		final String routingTaskLabelKey = dossierLink.getRoutingTaskLabel();
		final String routingTaskMailboxLabel = dossierLink.getRoutingTaskMailboxLabel();
		String routingTaskLabel = resourcesAccessor.getMessages().get(routingTaskLabelKey);

		String labelFormat = resourcesAccessor.getMessages().get("epg.dossier.liste.etape.label");
		return MessageFormat.format(labelFormat, routingTaskLabel, routingTaskMailboxLabel);
	}

	public String verserDossier() {
		DocumentModel dossierDoc = navigationContext.getCurrentDocument();

		// TODO Vérifie si le document est supprimé

		// on copie un proxy du dossier dans les dossier signales de l'espace de suivi
		try {
			DossierSignaleService dossierSignaleService = SolonEpgServiceLocator.getDossierSignaleService();
			if (dossierSignaleService.verserDossier(documentManager, dossierDoc) == 0) {
				// le dossierLink n'as pas pu être ajouté à la liste des dossiers signalés de l'utilisateur
				facesMessages.add(
						StatusMessage.Severity.WARN,
						resourcesAccessor.getMessages().get(
								"feedback.solonepg.dossier.error.cannotAddDossierLinkToDossierSignale"));
			} else if (dossierSignaleService.verserDossier(documentManager, dossierDoc) == -1) {
				// le dossierLink n'as pas pu être ajouté à la liste des dossiers signalés de l'utilisateur
				facesMessages.add(
						StatusMessage.Severity.WARN,
						resourcesAccessor.getMessages().get(
								"feedback.solonepg.dossier.error.cannotAddDossierLinkToDossierSignale"));
			} else {
				// le dossier link a été ajouté à la liste des dossiers signalés de l'utilisteur avec succès
				facesMessages.add(
						StatusMessage.Severity.INFO,
						resourcesAccessor.getMessages().get(
								"feedback.solonepg.dossier.error.DossierLinkAddedToDossierSignale"));
			}
		} catch (ClientException e) {
			facesMessages.add(
					StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get(
							"feedback.solonepg.dossier.error.cannotAddDossierLinkToDossierSignale"));
		}
		return null;
	}

	/**
	 * Retourn le dossier a partir de son UUID
	 * 
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	public DocumentModel findDossierById(String idDossier) throws ClientException {
		if (StringUtils.isEmpty(idDossier)) {
			return null;
		} else {
			return documentManager.getDocument(new IdRef(idDossier));
		}
	}

	/**
	 * Retourne les informations à afficher pour les dossiers dans l'outil de sélection
	 */
	public String getInformationsAffichageDossier(DocumentModel dossierDoc, Boolean showDelailPublication) {
		String informationAffichage = "";
		Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		// récupération du NOR et du titre de l'acte
		String NORDossier = dossier.getNumeroNor();
		String titreDossier = dossier.getTitreActe();
		if (titreDossier != null && !titreDossier.isEmpty()) {
			informationAffichage = NORDossier + " - " + titreDossier;
		} else {
			informationAffichage = NORDossier;
		}
		if (showDelailPublication) {
			// récupération du délai de publication - FEV507
			String delaiPublication = dossier.getDelaiPublication();
			Calendar datePrecisePublication = dossier.getDatePreciseePublication();
			// Si on a une date précise de publication, on renseigne cette dernière
			if ("1".equals(delaiPublication) && datePrecisePublication != null) {
				informationAffichage = informationAffichage + " - "
						+ DateUtil.formatDDMMYYYY(datePrecisePublication).replace("-", "/");
			} else if (delaiPublication != null && !delaiPublication.isEmpty()) {
				// Sinon on récupère le libellé correspondant au délai de publication
				final VocabularyService vocabularyService = STServiceLocator.getVocabularyService();
				String libelleDelaiPublication = vocabularyService.getEntryLabel(VocabularyConstants.DELAI_PUBLICATION,
						delaiPublication);
				if (!libelleDelaiPublication.isEmpty() && libelleDelaiPublication != null) {
					informationAffichage = informationAffichage + " - " + libelleDelaiPublication;
				}
			}
		}
		return informationAffichage;
	}

}
