package fr.dila.solonmgpp.web.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.platform.ui.web.api.WebActions;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.nuxeo.runtime.transaction.TransactionHelper;
import org.richfaces.component.UITree;
import org.richfaces.event.NodeExpandedEvent;

import fr.dila.solonmgpp.api.constant.SolonMgppActionConstant;
import fr.dila.solonmgpp.api.descriptor.PropertyDescriptor;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.FicheDossierDTO;
import fr.dila.solonmgpp.api.dto.HistoriqueDossierDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.dto.CritereRechercheDTOImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.content.MetadonneesContent;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.espace.NavigationWebActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;

@Name("historiqueEppTree")
@Scope(ScopeType.SESSION)
public class HistoriqueEppTreeBean extends MetadonneesContent implements Serializable {

	private static final String						DOSSIER_PROPERTY				= "dossier";

	private static final long						serialVersionUID				= 8164374428143449458L;

	/**
	 * Logger surcouche socle de log4j
	 */
	private static final STLogger					LOGGER							= STLogFactory
																							.getLog(HistoriqueEppTreeBean.class);

	private static final String						METADONNEES_ERREUR_RECUPERATION	= "metadonnees.erreur.recuperation";

	private static final List<String>				OEP_EVENEMENT_TYPES_LIST = Arrays.asList("EVT49-0", "EVT49", "EVT51",
			"GENERIQUE10", "ALERTE10", "EVT53-10");

	@In(create = true, required = false)
	protected transient NavigationContextBean		navigationContext;

	@In(create = true, required = false)
	protected transient FacesMessages				facesMessages;

	@In(create = true, required = false)
	protected transient ResourcesAccessor			resourcesAccessor;

	@In(create = true, required = false)
	protected transient InstitutionTreeBean			institutionTree;

	@In(create = true, required = true)
	protected transient CoreSession					documentManager;

	@In(create = true, required = true)
	protected transient ActionManager				actionManager;

	@In(create = true, required = false)
	protected transient NavigationWebActionsBean	navigationWebActions;

	@In(create = true, required = true)
	protected transient WebActions					webActions;

	private List<HistoriqueDataNode>				historiqueTree;

	private String									idDossier;

	private HistoriqueDataNode						currentItem;

	private FicheDossierDTO							ficheDossierDTO;

	private Set<String>								listHistoriqueType;

	private String									messageErreur;

	/**
	 * @return the historiqueTree
	 * @throws ClientException
	 */
	public List<HistoriqueDataNode> getHistoriqueTree() {

		String currentidDossier = navigationContext.getCurrentIdDossier();

		if (historiqueTree == null || idDossier == null || !idDossier.equals(currentidDossier)) {
			idDossier = currentidDossier;
			loadTree();
		}

		return historiqueTree;
	}

	/**
	 * Charge l'historique du dossier
	 * 
	 * @throws ClientException
	 */
	private void loadTree() {

		historiqueTree = new ArrayList<HistoriqueDataNode>();

		HistoriqueDossierDTO historiqueDTO = null;
		String idDossierTmp = idDossier;
		try {
			EvenementDTO currentEvenement = navigationContext.getCurrentEvenement();
			if (currentEvenement == null || OEP_EVENEMENT_TYPES_LIST.contains(currentEvenement.getTypeEvenementName())) {
				// FEV 394 : cas particulier des OEP où plusieurs communications peuvent être rattachées manuellement.
				FichePresentationOEP fpOEP = SolonMgppServiceLocator.getDossierService().findFicheOEP(documentManager,
						idDossier);
				if (fpOEP != null && fpOEP.getIdsANATLies() != null) {
					idDossier = fpOEP.getIdsANATLies();
					if (currentEvenement != null
							&& !Arrays.asList(idDossier.split(";")).contains(currentEvenement.getIdDossier())) {
						idDossier += ";" + currentEvenement.getIdDossier();
					}
				}
			}
			historiqueDTO = SolonMgppServiceLocator.getDossierService().findDossier(idDossier, documentManager);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOSSIER_TEC,
					"Erreur de recuperation de l'historique du dossier EPP " + idDossier, e);
		}

		if (historiqueDTO == null) {
			messageErreur = "Aucun historique EPP trouvé pour : " + idDossierTmp;
			return;
		} else {
			messageErreur = null;
		}

		if (historiqueDTO.getRootEvents() != null) {
			for (EvenementDTO evenementDTO : historiqueDTO.getRootEvents().values()) {
				HistoriqueDataNode dataNode = new HistoriqueDataNode(evenementDTO.getTypeEvenementLabel(),
						isEvenementCourant(evenementDTO), evenementDTO);
				historiqueTree.add(dataNode);
				addChildren(dataNode, historiqueDTO.getMapSuivant());

			}
		}

		ficheDossierDTO = historiqueDTO.getFicheDossierDTO();

		listHistoriqueType = historiqueDTO.getListHistoriqueType();

	}

	private void addChildren(HistoriqueDataNode evtNodeParent, Map<String, List<EvenementDTO>> mapSuivant) {
		int index = 0;

		List<EvenementDTO> listSuivant = mapSuivant.get(evtNodeParent.getEvenementDTO().getIdEvenement());
		if (listSuivant != null) {
			for (EvenementDTO evenementDTO : listSuivant) {

				HistoriqueDataNode dataNode = new HistoriqueDataNode(evenementDTO.getTypeEvenementLabel(),
						isEvenementCourant(evenementDTO), evenementDTO);
				evtNodeParent.addChild(index, dataNode);
				index++;
				addChildren(dataNode, mapSuivant);
			}

		}
	}

	public Boolean adviseNodeOpened(UITree treeComponent) {
		Object value = treeComponent.getRowData();
		if (value instanceof HistoriqueDataNode) {
			HistoriqueDataNode historiqueDataNode = (HistoriqueDataNode) value;
			return historiqueDataNode.isOpened();
		}
		return null;
	}

	public void changeExpandListener(NodeExpandedEvent event) {
		UIComponent component = event.getComponent();
		if (component instanceof UITree) {
			UITree treeComponent = (UITree) component;
			Object value = treeComponent.getRowData();
			if (value instanceof HistoriqueDataNode) {
				HistoriqueDataNode historiqueDataNode = (HistoriqueDataNode) value;
				historiqueDataNode.setOpened(!historiqueDataNode.isOpened());
			}
		}
	}

	private Boolean isEvenementCourant(EvenementDTO evenement) {
		EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
		return evenementDTO != null && evenementDTO.getIdEvenement().equals(evenement.getIdEvenement());
	}

	/**
	 * Navigation vers la recherche suite au clic sur un noeud
	 * 
	 * @param historiqueDataNode
	 * @return
	 */
	public String navigateTo(HistoriqueDataNode historiqueDataNode) {
		if (historiqueDataNode != null) {
			try {

				String idEvenement = historiqueDataNode.getEvenementDTO().getIdEvenement();

				// load message
				CritereRechercheDTO critereRechercheDTO = new CritereRechercheDTOImpl();
				critereRechercheDTO.setIdEvenement(idEvenement);

				List<MessageDTO> listMessageDTO = SolonMgppServiceLocator.getRechercheService().findMessage(
						critereRechercheDTO, documentManager);
				for (MessageDTO messageDTO : listMessageDTO) {
					navigationContext.setCurrentMessage(messageDTO);
					break;
				}

			} catch (ClientException e) {
				LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_MESSAGE_TEC,
						"Erreur de recuperation de la communication "
								+ historiqueDataNode.getEvenementDTO().getIdEvenement(), e);
				facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
				TransactionHelper.setTransactionRollbackOnly();
			}
			setCurrentItem(historiqueDataNode);
		}
		// M157404 - Dysfonctionnement historique EPP
		// il est nécessaire de restaurer le tab courant car le setCurrentMessage met à jour le document
		// qui est considéré comme différent car issu d'une session différente, ce qui reset les tabs
		// cf:
		// * DocumentModelImpl#getCacheKey qui exploite sid
		// * NavigationContextBean#checkIfUpdateNeeded(...)
		webActions.setCurrentTabAction(actionManager.getAction(SolonMgppActionConstant.TAB_MESSAGE_HISTORIQUE_EPP));
		return null;
	}

	@Override
	protected EvenementDTO getCurrentEvenement() {
		return navigationContext.getCurrentEvenement();
	}

	/**
	 * getWidgetMode
	 * 
	 * @param columnName
	 *            nom de la metadonnée
	 * @return 'edit' si le {@link PropertyDescriptor} de la metadonne est editable
	 */
	@Override
	public String getWidgetMode(String columnName) {
		return "view";
	}

	@Override
	protected void logErrorRecuperation(final EvenementDTO evt, ClientException e) {
		String message = resourcesAccessor.getMessages().get(METADONNEES_ERREUR_RECUPERATION);
		LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_META_DONNEE_TEC, message + evt.toString(), e);
		facesMessages.add(StatusMessage.Severity.WARN, message);
		TransactionHelper.setTransactionRollbackOnly();
	}

	public void setCurrentItem(HistoriqueDataNode currentItem) {
		this.currentItem = currentItem;
	}

	public HistoriqueDataNode getCurrentItem() {
		return currentItem;
	}

	public void setFicheDossierDTO(FicheDossierDTO ficheDossierDTO) {
		this.ficheDossierDTO = ficheDossierDTO;
	}

	public FicheDossierDTO getFicheDossierDTO() {
		getHistoriqueTree();

		return ficheDossierDTO;
	}

	public Boolean isColumnVisibleFicheDossier(String columnName) {

		getFicheDossierDTO();

		if (StringUtils.isNotEmpty(columnName)) {
			if (DOSSIER_PROPERTY.equals(columnName)) {
				return Boolean.TRUE;
			}

			final EvenementDTO evt = navigationContext.getCurrentEvenement();
			if (evt != null) {
				try {
					if (currentMap == null) {
						currentMap = new HashMap<String, Map<String, PropertyDescriptor>>();
					}

					for (String type : listHistoriqueType) {
						Map<String, PropertyDescriptor> map = currentMap.get(type);
						if (map == null) {
							map = SolonMgppServiceLocator.getMetaDonneesService().getMapProperty(type);
							currentMap.put(type, map);
						}
						PropertyDescriptor propertyDescriptor = map.get(columnName);
						if (propertyDescriptor != null && propertyDescriptor.isFicheDossier()) {
							if (propertyDescriptor.isVisibility()) {
								return Boolean.FALSE;
							}
							return Boolean.TRUE;
						}
					}

					return Boolean.FALSE;

				} catch (ClientException e) {
					logErrorRecuperation(evt, e);
				}
			}
		}

		return false;

	}

	public void reset() {
		currentItem = null;
		ficheDossierDTO = null;
		idDossier = null;

	}

	@Override
	protected ResourcesAccessor getResourcesAccessor() {
		return resourcesAccessor;
	}

	public void setMessageErreur(String messageErreur) {
		this.messageErreur = messageErreur;
	}

	public String getMessageErreur() {
		return messageErreur;
	}

	@Override
	protected String getCurrentProcedure() {
		Action action = navigationWebActions.getCurrentSecondMenuAction();
		if (action != null) {
			return action.getId();
		}
		return null;
	}

}
