package fr.dila.solonmgpp.web.recherche;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.dossier.DossierListingActionsBean;
import fr.dila.solonmgpp.api.constant.SolonMgppActionConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppI18nConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.builder.QueryBuilder;
import fr.dila.solonmgpp.core.dto.CritereRechercheDTOImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.corbeille.CorbeilleTreeBean;
import fr.dila.solonmgpp.web.espace.NavigationWebActionsBean;
import fr.dila.solonmgpp.web.evenement.EvenementCreationActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;

/**
 * bean de gestion des recherches simples
 * 
 * @author asatre
 * 
 */
@Name("rechercheActions")
@Scope(ScopeType.CONVERSATION)
public class RechercheActionsBean implements Serializable {

	private static final String							TYPE_AFFICHAGE_MESSAGE	= "Message";

	private static final String							TYPE_AFFICHAGE_OEP		= "OEP";

	private static final String							TYPE_AFFICHAGE_DR		= "DR";

	private static final String							TYPE_AFFICHAGE_AVI		= "AVI";

	private static final String							TYPE_AFFICHAGE_DOC		= "DOC";

	private static final String							TYPE_AFFICHAGE_DPG		= "DPG";

	private static final String							TYPE_AFFICHAGE_SD		= "SD";

	private static final String							TYPE_AFFICHAGE_JSS		= "JSS";
	
	private static final String							TYPE_AFFICHAGE_DECRET	= "Decret";

	private static final String							TYPE_AFFICHAGE_AUD		= "AUD";

	private static final String							TYPE_AFFICHAGE_DOSSIER	= "Dossier";

	private static final long							serialVersionUID		= 1434831296190728807L;

	/**
	 * Logger surcouche socle de log4j
	 */
	private static final STLogger						LOGGER					= STLogFactory
																						.getLog(RechercheActionsBean.class);

	@In(create = true, required = false)
	protected transient NavigationWebActionsBean		navigationWebActions;

	@In(create = true, required = false)
	protected transient FacesMessages					facesMessages;

	@In(create = true, required = false)
	protected transient ResourcesAccessor				resourcesAccessor;

	@In(create = true, required = false)
	protected transient CoreSession						documentManager;

	@In(create = true, required = false)
	protected transient DossierListingActionsBean		dossierListingActions;

	@In(required = false, create = true)
	protected CorbeilleTreeBean							corbeilleTree;

	@In(create = true, required = false)
	protected transient NavigationContextBean			navigationContext;

	@In(create = true, required = false)
	protected transient EvenementCreationActionsBean	evenementCreationActions;

	private CritereRechercheDTO							critereRecherche;

	private CritereRechercheDTO							critereRechercheAvancee;

	private List<MessageDTO>							listMessage;

	private List<MessageDTO>							listMessageRecherche;

	private List<DocumentModel>							listDossier;

	private List<DocumentModel>							listOEP;
	
	private List<DocumentModel>							listDecret;

	/**
	 * la liste des rapports chargés
	 */
	private List<DocumentModel>							listDR;

	private List<DocumentModel>							listAVI;

	private List<DocumentModel>							listDOC;

	private List<DocumentModel>							listDPG;

	private List<DocumentModel>							listSD;

	private List<DocumentModel>							listJSS;

	private List<DocumentModel>							listAUD;

	private boolean										selectEvenement			= false;

	public String doSearchMessage() {
		try {
			listMessage = SolonMgppServiceLocator.getRechercheService().findMessage(critereRecherche, documentManager);
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_MESSAGE_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return null;
	}

	public String doSearchDossier() {
		try {
			listDossier = SolonMgppServiceLocator.getRechercheService().findDossier(documentManager, critereRecherche);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return null;
	}

	public String doSearchDecret() {
		try {
			setListDecret(SolonMgppServiceLocator.getRechercheService().findDecret(documentManager, critereRecherche));
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return null;
	}

	public String doSearchOEP() {
		try {
			listOEP = SolonMgppServiceLocator.getRechercheService().findOEP(documentManager, critereRecherche);
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_REPRESENTANT_OEP_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return null;
	}

	/**
	 * Charge la liste des rapports en fonction des meta spécifié (normalement que le nor)
	 */
	public String doSearchDR() {
		try {
			listDR = SolonMgppServiceLocator.getRechercheService().findDR(documentManager, critereRecherche);
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_REPRESENTANT_DR_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return null;

	}

	public String doSearchAVI() {
		try {
			listAVI = SolonMgppServiceLocator.getRechercheService().findAVI(documentManager, critereRecherche);
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_REPRESENTANT_AVI_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return null;
	}

	public String doSearchDOC() {
		try {
			listDOC = SolonMgppServiceLocator.getRechercheService().findDOC(documentManager, critereRecherche);
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_REPRESENTANT_AVI_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return null;
	}

	public String doSearchDPG() {
		try {
			listDPG = SolonMgppServiceLocator.getRechercheService().findDPG(documentManager, critereRecherche);
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_REPRESENTANT_AVI_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return null;
	}

	public String doSearchSD() {
		try {
			listSD = SolonMgppServiceLocator.getRechercheService().findSD(documentManager, critereRecherche);
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_REPRESENTANT_AVI_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return null;
	}

	public String doSearchJSS() {
		try {
			listJSS = SolonMgppServiceLocator.getRechercheService().findJSS(documentManager, critereRecherche);
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_REPRESENTANT_AVI_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return null;
	}

	public String doSearchAUD() {
		try {
			listAUD = SolonMgppServiceLocator.getRechercheService().findAUD(documentManager, critereRecherche);
		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_REPRESENTANT_AVI_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return null;
	}

	public void setCritereRecherche(CritereRechercheDTO critereRecherche) {
		this.critereRecherche = critereRecherche;
	}

	public CritereRechercheDTO getCritereRecherche() {
		if (critereRecherche == null) {
			critereRecherche = new CritereRechercheDTOImpl(navigationWebActions.getCurrentSecondMenuAction());
		}

		return critereRecherche;
	}

	@Observer(NavigationWebActionsBean.MENU_CHANGE_EVENT)
	public void resetCritereRecherche() {
		critereRechercheAvancee = null;
		critereRecherche = null;
		listMessage = null;
		listDossier = null;
		listAVI = null;
		listOEP = null;
		listDOC = null;
		listDPG = null;
		listSD = null;
		listJSS = null;
	}

	/**
	 * true si le menu courant correspond avec un des menus passés en paramètres
	 * 
	 * @param menus
	 * @return
	 */
	public Boolean isVisible(String menus, String property) {

		if (StringUtils.isBlank(menus)) {
			return Boolean.TRUE;
		}

		Action action = navigationWebActions.getCurrentSecondMenuAction();
		if (action == null) {
			return Boolean.FALSE;
		}

		for (String menu : menus.split(",")) {
			if (menu.equals(action.getId())) {
				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}

	public void setListMessage(List<MessageDTO> listMessage) {
		this.listMessage = listMessage;
	}

	public List<MessageDTO> getListMessage() {
		return listMessage;
	}

	public void setListDossier(List<DocumentModel> listDossier) {
		this.listDossier = listDossier;
	}

	public List<DocumentModel> getListDossier() {
		return listDossier;
	}

	public String getAffichageType() {
		if (SolonMgppActionConstant.PUBLICATION.equals(navigationWebActions.getCurrentSecondMenuAction().getId())) {
			return TYPE_AFFICHAGE_DOSSIER;
		} else if (SolonMgppActionConstant.DEPOT_DE_RAPPORT.equals(navigationWebActions.getCurrentSecondMenuAction()
				.getId())) {
			return TYPE_AFFICHAGE_DR;
		} else if (SolonMgppActionConstant.DESIGNATION_OEP.equals(navigationWebActions.getCurrentSecondMenuAction()
				.getId())) {
			return TYPE_AFFICHAGE_OEP;
		} else if (SolonMgppActionConstant.AVIS_NOMINATION.equals(navigationWebActions.getCurrentSecondMenuAction()
				.getId())) {
			return TYPE_AFFICHAGE_AVI;
		} else if (SolonMgppActionConstant.DEMANDE_AUDITION.equals(navigationWebActions.getCurrentSecondMenuAction()
				.getId())) {
			return TYPE_AFFICHAGE_AUD;
		} else if (SolonMgppActionConstant.AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES.equals(navigationWebActions
				.getCurrentSecondMenuAction().getId())) {
			return TYPE_AFFICHAGE_DOC;
		} else if (SolonMgppActionConstant.DECLARATION_DE_POLITIQUE_GENERALE.equals(navigationWebActions
				.getCurrentSecondMenuAction().getId())) {
			return TYPE_AFFICHAGE_DPG;
		} else if (SolonMgppActionConstant.DECLARATION_SUR_UN_SUJET_DETERMINE.equals(navigationWebActions
				.getCurrentSecondMenuAction().getId())) {
			return TYPE_AFFICHAGE_SD;
		} else if (SolonMgppActionConstant.DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_283C.equals(navigationWebActions
				.getCurrentSecondMenuAction().getId())) {
			return TYPE_AFFICHAGE_JSS;
		} else if (SolonMgppActionConstant.DECRET.equals(navigationWebActions.getCurrentSecondMenuAction().getId())) {
			return TYPE_AFFICHAGE_DECRET;
		} else {
			return TYPE_AFFICHAGE_MESSAGE;
		}

	}

	public String navigateToDossier(DocumentModel dossierDoc) throws ClientException {
		corbeilleTree.resetCurrentItem();

		navigationContext.resetCurrentDocument();

		String view = null;
		try {
			view = dossierListingActions.navigateToDossier(dossierDoc, SolonMgppViewConstant.VIEW_CORBEILLE_DOSSIER);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_NAVIGATE_TO_DOSSIER_TEC);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return view;
	}

	public void setListOEP(List<DocumentModel> listOEP) {
		this.listOEP = listOEP;
	}

	public List<DocumentModel> getListOEP() {
		return listOEP;
	}

	public void setListAVI(List<DocumentModel> listAVI) {
		this.listAVI = listAVI;
	}

	public List<DocumentModel> getListAVI() {
		return listAVI;
	}

	public void setListDR(List<DocumentModel> listDR) {
		this.listDR = listDR;
	}

	public List<DocumentModel> getListDR() {
		return listDR;
	}

	public String navigateToRechercheAvancee() throws ClientException {

		resetCritereRecherche();

		return goBackToRechercheAvancee();
	}

	public String goBackToRechercheAvancee() throws ClientException {

		corbeilleTree.resetCurrentItem();

		navigationContext.resetCurrentDocument();

		if (critereRechercheAvancee != null && selectEvenement == false) {
			critereRechercheAvancee.setTypeEvenement(new ArrayList<String>());
		}

		return SolonMgppViewConstant.VIEW_RECHERCHE_AVANCEE;
	}

	public String doRechercheAvancee() {

		boolean isTypeEvenementEmpty = critereRechercheAvancee.getTypeEvenement().isEmpty();
		List<String> evenementListParProcedure = new ArrayList<String>();
		if (isTypeEvenementEmpty) {

			List<SelectItem> lsTypeEvenenemts = evenementCreationActions.getEvenementList(critereRechercheAvancee
					.getProcedure());

			for (SelectItem currentItem : lsTypeEvenenemts) {
				evenementListParProcedure.add((String) currentItem.getValue());
			}
			critereRechercheAvancee.setTypeEvenement(evenementListParProcedure);
			selectEvenement = false;
		} else {
			selectEvenement = true;
		}

		// check query not empty
		if (QueryBuilder.getInstance().isWhereClauseEmptyMessage(critereRechercheAvancee)) {
			facesMessages.add(StatusMessage.Severity.WARN,
					resourcesAccessor.getMessages().get(SolonMgppI18nConstant.EMPTY_WHERE_SEARCH));
			return null;
		} else {
			// refresh provider
			Events.instance().raiseEvent(ProviderBean.RESET_CONTENT_VIEW_EVENT);
			return SolonMgppViewConstant.VIEW_RECHERCHE_AVANCEE_RESULTAT;
		}
	}

	public void setCritereRechercheAvancee(CritereRechercheDTO critereRechercheAvancee) {
		this.critereRechercheAvancee = critereRechercheAvancee;
	}

	public CritereRechercheDTO getCritereRechercheAvancee() {
		if (critereRechercheAvancee == null) {
			critereRechercheAvancee = new CritereRechercheDTOImpl();
			if (navigationWebActions.getCurrentSecondMenuAction() != null) {
				critereRechercheAvancee.setProcedure(navigationWebActions.getCurrentSecondMenuAction().getId());
			}
		}
		return critereRechercheAvancee;
	}

	public void setListMessageRecherche(List<MessageDTO> listMessageRecherche) {
		this.listMessageRecherche = listMessageRecherche;
	}

	public List<MessageDTO> getListMessageRecherche() {
		return listMessageRecherche;
	}

	public void setListDOC(List<DocumentModel> listDOC) {
		this.listDOC = listDOC;
	}

	public List<DocumentModel> getListDOC() {
		return listDOC;
	}

	public void setListDPG(List<DocumentModel> listDPG) {
		this.listDPG = listDPG;
	}

	public List<DocumentModel> getListDPG() {
		return listDPG;
	}

	public void setListSD(List<DocumentModel> listSD) {
		this.listSD = listSD;
	}

	public List<DocumentModel> getListSD() {
		return listSD;
	}

	public void setListJSS(List<DocumentModel> listJSS) {
		this.listJSS = listJSS;
	}

	public List<DocumentModel> getListJSS() {
		return listJSS;
	}

	public void setListAUD(List<DocumentModel> listAUD) {
		this.listAUD = listAUD;
	}

	public List<DocumentModel> getListAUD() {
		return listAUD;
	}

	public List<DocumentModel> getListDecret() {
		return listDecret;
	}

	public void setListDecret(List<DocumentModel> listDecret) {
		this.listDecret = listDecret;
	}

}
