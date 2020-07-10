package fr.dila.solonmgpp.web.context;

import static org.jboss.seam.ScopeType.CONVERSATION;
import static org.jboss.seam.annotations.Install.FRAMEWORK;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgActionConstant;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.espace.CorbeilleActionsBean;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.espace.NavigationWebActionsBean;
import fr.dila.solonmgpp.web.evenement.EvenementDetailsActionsBean;
import fr.dila.solonmgpp.web.tree.HistoriqueEppTreeBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.CorbeilleService;
import fr.dila.st.core.factory.STLogFactory;

/**
 * Surcharge du NavigationContextBean de SOLON EPG.
 */
@Name("navigationContext")
@Scope(CONVERSATION)
@Install(precedence = FRAMEWORK + 3)
public class NavigationContextBean extends fr.dila.solonepg.web.context.NavigationContextBean {

	private static final long						serialVersionUID	= -1013586985152059057L;
	/**
	 * Logger surcouche socle de log4j
	 */
	private static final STLogger					LOGGER				= STLogFactory
																				.getLog(NavigationContextBean.class);

	@In(create = true, required = false)
	protected transient FacesMessages				facesMessages;

	@In(create = true, required = false)
	protected transient EvenementDetailsActionsBean	evenementDetailsActions;

	@In(create = true)
	protected transient NavigationWebActionsBean	navigationWebActions;

	@In(create = true, required = false)
	protected transient HistoriqueEppTreeBean		historiqueEppTree;

	@In(create = true, required = false)
	protected transient CorbeilleActionsBean		corbeilleActions;

	private MessageDTO								currentMessage;
	private EvenementDTO							currentEvenement;
	private String									currentIdDossier;

	public MessageDTO getCurrentMessage() {
		return currentMessage;
	}

	public void setCurrentMessage(MessageDTO currentMessage) {
		setCurrentMessage(currentMessage, null);
	}

	public void setCurrentMessage(MessageDTO currentMessage, String numeroVersion) {
		this.currentMessage = currentMessage;

		if (currentMessage != null) {
			// load dossier
			Dossier dossier;
			try {
				// try by idDossier
				dossier = SolonEpgServiceLocator.getDossierService().findDossierFromIdDossier(documentManager,
						currentMessage.getIdDossier());
				if (dossier == null) {
					// try by nor
					dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager,
							currentMessage.getIdDossier());
				}

				// Assign Doc
				setCurrentDocument(dossier == null ? null : dossier.getDocument());

				if (dossier != null) {

					// Recherche les DossierLink que l'utilisateur peut actionner
					final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
					List<DocumentModel> dossierLinkList = corbeilleService.findDossierLink(documentManager, dossier
							.getDocument().getId());

					DocumentModel dossierLinkDoc = null;
					if (dossierLinkList.size() > 0) {
						if (dossierLinkList.size() > 1) {
							LOGGER.info(documentManager, STLogEnumImpl.ANO_DL_MULTI_TEC);
						}
						dossierLinkDoc = dossierLinkList.get(0);
					}
					// Charge le DossierLink dans la corbeille
					DossierLink dossierLink = null;
					if (dossierLinkDoc != null) {
						dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);
					}
					corbeilleActions.setCurrentDossierLink(dossierLink);
				}
			} catch (ClientException e) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOSSIER_TEC,
						"id : " + currentMessage.getIdDossier());
				facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
				TransactionHelper.setTransactionRollbackOnly();
			}

			// load Evenement from message
			EvenementDTO evenementDTO;
			try {
				evenementDTO = SolonMgppServiceLocator.getEvenementService().findEvenement(
						currentMessage.getIdEvenement(), null, documentManager, true);
				setCurrentEvenement(evenementDTO);
			} catch (ClientException e) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_EVENT_TEC, e);
				facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
				TransactionHelper.setTransactionRollbackOnly();
			}

			// set current idDossier
			currentIdDossier = currentMessage.getIdDossier();

			// set back message (reset in setCurrentDocument(DocumentModel documentModel)
			this.currentMessage = currentMessage;

		}
	}

	@Override
	public void setCurrentDocument(DocumentModel documentModel) throws ClientException {

		super.setCurrentDocument(documentModel);

		currentIdDossier = null;

		if (documentModel != null) {
			Action mainAction = navigationWebActions.getCurrentMainMenuAction();

			if (mainAction != null && mainAction.getId().equals(SolonEpgActionConstant.ESPACE_PARLEMENTAIRE)) {
				if (documentModel.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
					Dossier dossier = documentModel.getAdapter(Dossier.class);
					if (StringUtils.isNotBlank(dossier.getIdDossier())) {
						setCurrentIdDossier(dossier.getIdDossier());
					} else {
						setCurrentIdDossier(dossier.getNumeroNor());
					}
				}
			}
		}
	}

	public EvenementDTO getCurrentEvenement() {
		return currentEvenement;
	}

	public void setCurrentEvenement(EvenementDTO currentEvenement) {
		evenementDetailsActions.reset();
		historiqueEppTree.reset();
		this.currentEvenement = currentEvenement;
	}

	@Override
	public void resetCurrentDocument() throws ClientException {
		setCurrentDocument(null);
		resetMGPPDocs();
	}

	public void resetMGPPDocs() {
		setCurrentMessage(null);
		setCurrentEvenement(null);
		setCurrentIdDossier(null);
		evenementDetailsActions.reset();
		historiqueEppTree.reset();
	}

	public void setCurrentIdDossier(String currentIdDossier) {
		this.currentIdDossier = currentIdDossier;
	}

	public String getCurrentIdDossier() {
		return currentIdDossier;
	}
}
