package fr.dila.solonepg.web.activitenormative;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.webapp.action.WebActionsBean;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreLoiDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.admin.journal.JournalAdminPANActionsBean;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;

@Name("paramStatsActiviteNormativeStatsActions")
@Scope(ScopeType.CONVERSATION)
public class ActiviteNormativeParamStatsActionsBean implements Serializable {
	private static final String EVENT_LEGISLATURE_CHANGED = "legislaturePublicationChanged";

	private static final long serialVersionUID = 2776349205588506395L;

	private static final STLogger LOGGER = STLogFactory.getLog(ActiviteNormativeParamStatsActionsBean.class);

	@In(create = true, required = false)
	protected transient WebActionsBean webActions;

	@In(create = true, required = true)
	protected transient CoreSession documentManager;

	@In(create = true, required = false)
	protected transient TexteMaitreActionsBean texteMaitreActions;

	@In(create = true, required = false)
	protected transient ActiviteNormativeActionsBean activiteNormativeActions;

	@In(create = true, required = false)
	protected transient ActiviteNormativeStatsActionsBean activiteNormativeStatsActions;

	@In(create = true, required = false)
	protected transient ExportActiviteNormativeStatsActionsBean exportActiviteNormativeStatsActions;
	
	@In(create = true, required = false)
	protected transient JournalAdminPANActionsBean journalPAN;

	@In(create = true, required = false)
	protected transient ResourcesAccessor resourcesAccessor;

	@In(create = true, required = false)
	protected transient FacesMessages facesMessages;

	@In(create = true, required = false)
	protected transient NavigationContext navigationContext;

	@In(required = true, create = true)
	protected SSPrincipal ssPrincipal;

	@In(create = true, required = false)
	protected transient ContentViewActions contentViewActions;

	@RequestParameter
	protected String legislaturesListId;

	@RequestParameter
	protected String freeInputText;
	
	private String legislatureInput;

	private String selectedLegislatureValue;

	private String selectedOldLegislatureValue;

	private DocumentModel parameterStatsDoc;

	private HashMap<String, String> temporaryLegis;
	private HashMap<String, String> reverseTemporaryLegis;
	
	public List<EspaceActiviteNormativeTreeNode> getParamStatsTreeNodes() {

		List<EspaceActiviteNormativeTreeNode> nodes = new ArrayList<EspaceActiviteNormativeTreeNode>();

		EspaceActiviteNormativeTreeNode mainNode = new EspaceActiviteNormativeTreeNode("Paramétrages des statistiques",
				"param-stats-key", null);
		String detailTitle = "detailTitle";
		String detailKey = "detailKey";
		EspaceActiviteNormativeTreeNode childNode = new EspaceActiviteNormativeTreeNode(detailTitle, detailKey, null);
		mainNode.getChildren().add(childNode);

		nodes.add(mainNode);
		return nodes;
	}

	public void validerParamStats() {
		if (documentManager != null && parameterStatsDoc != null) {
			ParametrageAN parametrageAN = parameterStatsDoc.getAdapter(ParametrageAN.class);
			try {
				parameterStatsDoc = parametrageAN.save(documentManager);
				parametrageAN = parameterStatsDoc.getAdapter(ParametrageAN.class);

				// On réinitialise les paramètres dans les autres écrans
				if (activiteNormativeStatsActions != null) {
					activiteNormativeStatsActions.setParameterStatsDoc(null);
				}
				if (exportActiviteNormativeStatsActions != null) {
					exportActiviteNormativeStatsActions.setParameterStatsDoc(null);
				}

				// on met à jour les textes maitres car une legislature a changé
				if (!temporaryLegis.isEmpty()) {
					
					// Mise à jour du champ publication de tous les textes maitres concernés
					// MAJ du champ legislature en cours
					String oldLegisEnCours = parametrageAN.getLegislatureEnCours();
					if (temporaryLegis.containsKey(oldLegisEnCours)) {
						parametrageAN.setLegislatureEnCours(temporaryLegis.get(oldLegisEnCours));
						documentManager.saveDocument(parametrageAN.getDocument());
						documentManager.save();
					}
					
					SolonEpgServiceLocator.getSolonEpgParametreService().updateLegislaturesFromTextesMaitres(documentManager, temporaryLegis);
					
					//On cherche à mettre à jour manuellement le texte maitre courant
					if (navigationContext.getCurrentDocument() != null) {
						TexteMaitreLoiDTOImpl texteMaitreDisplayed = texteMaitreActions.getCurrentTexteMaitre();
						if (temporaryLegis.containsKey(texteMaitreDisplayed.getLegislaturePublication())) {
							texteMaitreDisplayed.setLegislaturePublication(temporaryLegis.get(texteMaitreDisplayed.getLegislaturePublication()));
						}

						texteMaitreActions.setCurrentTexteMaitre(texteMaitreDisplayed);
					}
					temporaryLegis = null;
					reverseTemporaryLegis = null;

					Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
					contentViewActions.refreshOnSeamEvent(EVENT_LEGISLATURE_CHANGED);
					
					LOGGER.info(STLogEnumImpl.END_EVENT_TEC, "Rafraichissement de la contentview terminé.");
				
				}
				
				STServiceLocator.getJournalService().journaliserActionPAN(documentManager, null,
						SolonEpgEventConstant.PARAM_PAN_UPDATE_EVENT, SolonEpgEventConstant.PARAM_PAN_UPDATE_COMMENT,
						SolonEpgEventConstant.CATEGORY_LOG_PAN_GENERAL);
				
			} catch (ClientException exc) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_SAVE_DOCUMENT_FONC, parametrageAN.getDocument(), exc);
			}
		}
	}

	/**
	 * Permet de ne pas afficher la pop up de paramétrage si un document et
	 * ouvert et verrouillé par l'utilisateur courant
	 * 
	 * @return true si un document est ouvert et verrouillé
	 */
	public Boolean isThereAnOpenedAndLockedDocument() {
		DocumentModel currentDoc = navigationContext.getCurrentDocument();
		if (currentDoc != null && currentDoc.hasSchema(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA)) {
			TexteMaitre docEnCours = currentDoc.getAdapter(TexteMaitre.class);
			if (docEnCours.getDocLockUser()!=null) {
				if (docEnCours.getDocLockUser().equals(ssPrincipal.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	public Boolean isEspaceActiviteNormativeParametrageLegislatureReader() {
		if (ssPrincipal != null) {
			return ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_PARAM_LEGIS_UPDATER);
		}
		return Boolean.FALSE;
	}

	public void resetParameters() {
		try {
			if (parameterStatsDoc != null) {
				parameterStatsDoc.refresh();
			}
		} catch (ClientException exc) {
			LOGGER.warn(documentManager, STLogEnumImpl.FAIL_GET_DOCUMENT_FONC, parameterStatsDoc, exc);
		}
		setSelectedLegislatureValue(null);
	}

	public void displayParameters() throws ClientException {
		SolonEpgParametreService paramEPGservice = SolonEpgServiceLocator.getSolonEpgParametreService();
		ParametrageAN myDoc;
		try {
			myDoc = paramEPGservice.getDocAnParametre(documentManager);
			if (myDoc != null) {
				parameterStatsDoc = myDoc.getDocument();
			}
			temporaryLegis = new HashMap<String, String>();
			reverseTemporaryLegis = new HashMap<String, String>();

		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOCUMENT_FONC, e);
		}
	}
	
	/*********************
	 * 
	 * Méthodes du wigdet template de la liste de legislatures
	 * 
	 */

	public String addLegislature(final DocumentModel dossierDoc) throws ClientException {
		if (!legislatureInput.isEmpty()) {
			legislatureInput = StringUtil.sanitizeJS(legislatureInput);

			List<String> legislatures = getLegislatures();
			if (!legislatures.contains(legislatureInput)) {
				legislatures.add(legislatureInput);
				ParametrageAN parametrageAN = parameterStatsDoc.getAdapter(ParametrageAN.class);
				parametrageAN.setLegislatures(legislatures);
			}

			legislatureInput = "";
		}
		return null;
	}

	public String modifLegislature() throws ClientException {
		List<String> legislatures = getLegislatures();
		if (!legislatures.contains(selectedLegislatureValue) && StringUtil.isNotBlank(selectedLegislatureValue)) {
			selectedLegislatureValue = StringUtil.sanitizeJS(selectedLegislatureValue);
			int oldIndex = legislatures.indexOf(selectedOldLegislatureValue);
			legislatures.add(oldIndex, selectedLegislatureValue);
			legislatures.remove(selectedOldLegislatureValue);
	
			ParametrageAN parametrageAN = parameterStatsDoc.getAdapter(ParametrageAN.class);
			parametrageAN.setLegislatures(legislatures);
	
			if (temporaryLegis.containsValue(selectedOldLegislatureValue)) {
				temporaryLegis.put(reverseTemporaryLegis.get(selectedOldLegislatureValue), selectedLegislatureValue);
				reverseTemporaryLegis.put(selectedLegislatureValue, reverseTemporaryLegis.get(selectedOldLegislatureValue));
				reverseTemporaryLegis.remove(selectedOldLegislatureValue);
			} else {
				temporaryLegis.put(selectedOldLegislatureValue, selectedLegislatureValue);
				reverseTemporaryLegis.put(selectedLegislatureValue, selectedOldLegislatureValue);
			}
		} 
		setSelectedLegislatureValue(null);
		return null;
	}

	public void updateLegislatureListener(final ValueChangeEvent event) throws Exception {
		if (event != null && event.getNewValue() != null) {
			final String newValue = event.getNewValue().toString();
			setSelectedOldLegislatureValue(selectedLegislatureValue);
			setSelectedLegislatureValue(newValue);
		}
	}

	public void resetProperties() throws ClientException {
		setSelectedLegislatureValue(null);
	}

	public List<String> getLegislatures() {
		if (parameterStatsDoc != null) {
			ParametrageAN parametrageAN = parameterStatsDoc.getAdapter(ParametrageAN.class);
			return parametrageAN.getLegislatures();
		}
		return new ArrayList<String>();
	}

	public String getLegislatureInput() {
		return legislatureInput;
	}

	public void setLegislatureInput(String legislatureInput) {
		this.legislatureInput = legislatureInput;
	}

	public void setSelectedLegislatureValue(String selectedLegislatureValue) {
		this.selectedLegislatureValue = selectedLegislatureValue;
	}

	public String getSelectedLegislatureValue() {
		return selectedLegislatureValue;
	}

	public DocumentModel getParameterStatsDoc() {
		return parameterStatsDoc;
	}

	public void setParameterStatsDoc(DocumentModel parameterStatsDoc) {
		this.parameterStatsDoc = parameterStatsDoc;
	}

	private void setSelectedOldLegislatureValue(String selectedOldLegislatureValue) {
		this.selectedOldLegislatureValue = selectedOldLegislatureValue;
	}
}
