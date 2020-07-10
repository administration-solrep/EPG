package fr.dila.solonepg.web.admin.parametres;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.administration.IndexationMotCle;
import fr.dila.solonepg.api.administration.IndexationRubrique;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgIndextionConstants;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.exception.IndexationException;
import fr.dila.solonepg.api.service.IndexationEpgService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.GouvernementNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.administration.organigramme.OrganigrammeDataTreeNode;

@Name("gestionIndexationActions")
@Scope(ScopeType.CONVERSATION)
public class GestionIndexationsActionsBean implements Serializable {

	private static final long				serialVersionUID	= 1L;

	@In(create = true, required = true)
	protected transient CoreSession			documentManager;

	@In(required = true, create = true)
	protected SSPrincipal					ssPrincipal;

	@In(create = true)
	protected transient FacesMessages		facesMessages;

	@In(create = true)
	protected transient ResourcesAccessor	resourcesAccessor;

	protected String						indexationRubrique;

	protected String						indexationMotCle;

	protected String						indexationMotCleList;

	protected DocumentModel					currentIndexationMotCle;

	private final static String				SEPARATOR			= ";";

	public Boolean isUpdater() {
		if (ssPrincipal != null) {
			return ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMINISTRATION_INDEXATION_UPDATER);
		}
		return false;

	}

	public String addRubrique() throws ClientException {
		IndexationEpgService indexationEpgService = SolonEpgServiceLocator.getIndexationEpgService();
		for (String rubrique : indexationRubrique.split(SEPARATOR)) {
			if (rubrique != null && !rubrique.isEmpty()) {
				try {
					indexationEpgService.createIndexationRubrique(documentManager, rubrique);
				} catch (IndexationException e) {
					// catch des messages du services en cas d'erreur car problème de renseignement utilisateur
					facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
				}
			}
		}
		indexationRubrique = "";

		return SolonEpgViewConstant.GESTION_INDEXATION_VIEW;
	}

	public String addMotCle() throws ClientException {
		if (currentIndexationMotCle != null && indexationMotCle != null) {
			if (indexationMotCle.isEmpty()) {
				String message = resourcesAccessor.getMessages().get("indexation.mot.cle.empty");
				facesMessages.add(StatusMessage.Severity.WARN, message);
			} else {
				IndexationMotCle indexation = currentIndexationMotCle.getAdapter(IndexationMotCle.class);
				Set<String> listMotCle = new HashSet<String>(indexation.getMotsCles());
				boolean update = Boolean.FALSE;
				for (String motCle : indexationMotCle.split(SEPARATOR)) {
					if (motCle != null && !motCle.isEmpty()) {
						if (!listMotCle.contains(motCle)) {
							listMotCle.add(motCle);
							update = Boolean.TRUE;
						} else {
							String message = resourcesAccessor.getMessages().get("indexation.mot.cle.present");
							facesMessages.add(StatusMessage.Severity.WARN, message);
						}
					}
				}

				if (update) {
					indexation.setMotsCles(new ArrayList<String>(listMotCle));
					IndexationEpgService indexationEpgService = SolonEpgServiceLocator.getIndexationEpgService();
					indexationEpgService.updateIndexationMotCle(documentManager, indexation);
					indexationMotCle = "";
				}
			}
		}

		return SolonEpgViewConstant.GESTION_INDEXATION_VIEW;
	}

	public void addMotCleList(String ministereId) throws ClientException {
		IndexationEpgService indexationEpgService = SolonEpgServiceLocator.getIndexationEpgService();

		if (ministereId == null) {
			String message = resourcesAccessor.getMessages().get("indexation.ministere.empty");
			facesMessages.add(StatusMessage.Severity.WARN, message);
		} else {
			List<String> idMinisteres = new ArrayList<String>();
			try {
				Integer.parseInt(ministereId);
				idMinisteres.add(ministereId);
			} catch (NumberFormatException e) {
				// L'utilisateur a selectionné "Tous les ministères" qui renvoi "Tous" en non un identifiant de node
				STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
				for (OrganigrammeNode node : ministeresService.getCurrentMinisteres()) {
					idMinisteres.add(node.getId());
				}
			}
			if (indexationMotCleList != null && !indexationMotCleList.isEmpty()) {
				try {
					indexationEpgService.createIndexationMotCle(documentManager, indexationMotCleList, idMinisteres);
				} catch (IndexationException e) {
					// catch des messages du services en cas d'erreur car problème de renseignement utilisateur
					facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
				}
			}

			indexationMotCleList = "";
			indexationMotCle = "";
		}
	}

	public String deleteIndexation(DocumentModel docModel) throws ClientException {
		if (docModel != null) {
			if (docModel.hasSchema(SolonEpgIndextionConstants.INDEXATION_RUBRIQUE_SCHEMA)) {
				SolonEpgServiceLocator.getIndexationEpgService().deleteIndexationRubrique(documentManager,
						docModel.getAdapter(IndexationRubrique.class));
			} else if (docModel.hasSchema(SolonEpgIndextionConstants.INDEXATION_MOT_CLE_SCHEMA)) {
				if (currentIndexationMotCle != null && currentIndexationMotCle.getId().equals(docModel.getId())) {
					currentIndexationMotCle = null;
				}
				SolonEpgServiceLocator.getIndexationEpgService().deleteIndexationMotCle(documentManager,
						docModel.getAdapter(IndexationMotCle.class));
			}
		}

		return SolonEpgViewConstant.GESTION_INDEXATION_VIEW;
	}

	public DocumentModelList getListIndexationMotCle() throws ClientException {
		return SolonEpgServiceLocator.getIndexationEpgService().findAllIndexationMotCle(documentManager);
	}

	public DocumentModelList getListIndexationRubrique() throws ClientException {
		return SolonEpgServiceLocator.getIndexationEpgService().findAllIndexationRubrique(documentManager);
	}

	public List<String> getListMotCle() {
		if (currentIndexationMotCle != null) {
			List<String> list = currentIndexationMotCle.getAdapter(IndexationMotCle.class).getMotsCles();
			Collections.sort(list, new Comparator<String>() {

				@Override
				public int compare(String obj1, String obj2) {
					return obj1.compareToIgnoreCase(obj2.toUpperCase());
				}
			});
			return list;
		}
		return new ArrayList<String>();
	}

	public String removeMotCle(String motCle) throws ClientException {
		if (currentIndexationMotCle != null && motCle != null) {
			IndexationMotCle indexation = currentIndexationMotCle.getAdapter(IndexationMotCle.class);
			if (indexation != null) {
				List<String> listMotCle = indexation.getMotsCles();
				if (listMotCle.contains(motCle)) {
					listMotCle.remove(motCle);
					indexation.setMotsCles(listMotCle);
					SolonEpgServiceLocator.getIndexationEpgService()
							.updateIndexationMotCle(documentManager, indexation);
				}
			}
		}

		return SolonEpgViewConstant.GESTION_INDEXATION_VIEW;
	}

	public String getIndexationRubrique() {
		return indexationRubrique;
	}

	public void setIndexationRubrique(String indexationRubrique) {
		this.indexationRubrique = indexationRubrique;
	}

	public String getIndexationMotCle() {
		return indexationMotCle;
	}

	public void setIndexationMotCle(String indexationMotCle) {
		this.indexationMotCle = indexationMotCle;
	}

	public String getIndexationMotCleList() {
		return indexationMotCleList;
	}

	public void setIndexationMotCleList(String indexationMotCleList) {
		this.indexationMotCleList = indexationMotCleList;
	}

	public DocumentModel getCurrentIndexationMotCle() {
		return currentIndexationMotCle;
	}

	public String setCurrentIndexationMotCle(DocumentModel currentIndexationMotCle) {
		this.currentIndexationMotCle = currentIndexationMotCle;
		indexationMotCle = "";
		return SolonEpgViewConstant.GESTION_INDEXATION_VIEW;
	}

	public List<OrganigrammeDataTreeNode> getOrganigramme() throws ClientException {

		List<OrganigrammeDataTreeNode> rootNodes = new ArrayList<OrganigrammeDataTreeNode>();

		if (isUpdater()) {
			// "super" admin
			List<GouvernementNode> baseGroups = STServiceLocator.getSTGouvernementService().getGouvernementList();

			Date today = new Date();
			String ministereId = null;
			for (OrganigrammeNode groupNode : baseGroups) {
				if (groupNode.isActive()
						&& (groupNode.getDateFin() == null || groupNode.getDateFin().compareTo(today) > 0)) {
					if (groupNode instanceof EntiteNode) {
						ministereId = groupNode.getId();
					}
					OrganigrammeDataTreeNode dataTreeNode = new OrganigrammeDataTreeNode(groupNode, ministereId, null);
					dataTreeNode.setOpened(true);
					addSubGroups(dataTreeNode);
					rootNodes.add(dataTreeNode);
				}
			}
		} else {
			// admin ministeriel
			final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
			for (String idMinistere : ssPrincipal.getMinistereIdSet()) {
				OrganigrammeNode node = ministeresService.getEntiteNode(idMinistere);
				if (node instanceof EntiteNode) {
					if (node.isActive()) {
						OrganigrammeDataTreeNode dataTreeNode = new OrganigrammeDataTreeNode(node, node.getId(), null);
						dataTreeNode.setOpened(true);
						rootNodes.add(dataTreeNode);
					}
				}
			}
		}

		return rootNodes;

	}

	protected void addSubGroups(OrganigrammeDataTreeNode dataTreeNode) throws ClientException {
		List<OrganigrammeNode> subGroups;

		OrganigrammeNode group = dataTreeNode.getNode();

		if (group != null) {

			subGroups = STServiceLocator.getOrganigrammeService().getChildrenList(documentManager, group, Boolean.TRUE);

			if (subGroups != null && !subGroups.isEmpty()) {
				Date today = new Date();

				for (OrganigrammeNode childGroup : subGroups) {
					if (childGroup.isActive()
							&& (childGroup.getDateFin() == null || childGroup.getDateFin().compareTo(today) > 0)) {
						String ministereId = dataTreeNode.getMinistereId();
						if (childGroup instanceof EntiteNode) {
							ministereId = childGroup.getId();
						}
						dataTreeNode.addChild(new OrganigrammeDataTreeNode(childGroup, ministereId, dataTreeNode));
					}
				}
			}
		}
	}

	public Boolean isInMinistere(DocumentModel indexationModel) {
		if (indexationModel == null) {
			return false;
		}
		if (isUpdater()) {
			return Boolean.TRUE;
		} else {
			IndexationMotCle imc = indexationModel.getAdapter(IndexationMotCle.class);
			if (imc.getIntitule().startsWith("Tous - ")) {
				return Boolean.FALSE;
			}
			for (String idMinistere : imc.getMinistereIds()) {
				if (ssPrincipal.getMinistereIdSet().contains(idMinistere)) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	public Boolean isInMinistereForChange(DocumentModel indexationModel) {
		if (!isCurrentModel(indexationModel)) {
			return Boolean.FALSE;
		}
		if (isUpdater()) {
			return Boolean.TRUE;
		} else {
			IndexationMotCle imc = indexationModel.getAdapter(IndexationMotCle.class);
			if (imc.getIntitule().startsWith("Tous - ")) {
				return Boolean.FALSE;
			}
			for (String idMinistere : imc.getMinistereIds()) {
				if (ssPrincipal.getMinistereIdSet().contains(idMinistere)) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	public void changeMotCleList(String idMinistere) throws ClientException {
		if (currentIndexationMotCle != null) {
			try {
				if (idMinistere == null) {
					String message = resourcesAccessor.getMessages().get("indexation.ministere.empty");
					facesMessages.add(StatusMessage.Severity.WARN, message);
				} else {
					IndexationMotCle indexationMC = currentIndexationMotCle.getAdapter(IndexationMotCle.class);
					if (indexationMC != null) {
						String[] title = indexationMC.getIntitule().split(" - ");

						List<String> idMinisteres = new ArrayList<String>();
						try {
							Integer.parseInt(idMinistere);
							idMinisteres.add(idMinistere);
							EntiteNode node = STServiceLocator.getSTMinisteresService().getEntiteNode(idMinistere);

							indexationMC.setIntitule(node.getNorMinistere() + " - " + title[1]);

						} catch (NumberFormatException e) {
							// L'utilisateur a selectionné "Tous les ministères" qui renvoi "Tous" en non un identifiant
							// de node
							final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
							for (OrganigrammeNode node : ministeresService.getCurrentMinisteres()) {
								idMinisteres.add(node.getId());
							}
							indexationMC.setIntitule("Tous - " + title[1]);
						}

						indexationMC.setMinistereIds(idMinisteres);
						SolonEpgServiceLocator.getIndexationEpgService().updateIndexationMotCleAfterChange(
								documentManager, indexationMC);
					}
					indexationMotCleList = "";
					indexationMotCle = "";
				}
			} catch (IndexationException e) {
				// catch des messages du services en cas d'erreur car problème de renseignement utilisateur
				facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			}
		}
	}

	public void indexationMotCleListValueListener(ValueChangeEvent event) throws Exception {
		if (event != null && event.getNewValue() != null && event.getNewValue().toString() != null) {
			indexationMotCleList = event.getNewValue().toString();
		}
	}

	public void indexationMotCleValueListener(ValueChangeEvent event) throws Exception {
		if (event != null && event.getNewValue() != null && event.getNewValue().toString() != null) {
			indexationMotCle = event.getNewValue().toString();
		}
	}

	public void indexationRubriqueValueListener(ValueChangeEvent event) throws Exception {
		if (event != null && event.getNewValue() != null && event.getNewValue().toString() != null) {
			indexationRubrique = event.getNewValue().toString();
		}
	}

	public String getIndexationMotCleStyle(DocumentModel docModel) {
		if (isCurrentModel(docModel)) {
			return "font-weight:bold !important; text-decoration:underline !important;";
		} else {
			return "";
		}
	}

	private boolean isCurrentModel(DocumentModel docModel) {
		return docModel != null && currentIndexationMotCle != null && currentIndexationMotCle.getId() != null
				&& currentIndexationMotCle.getId().equals(docModel.getId());
	}

	/**
	 * 
	 * @return l'id du ministere si l'utilisateur courant n'a qu'un ministere
	 */
	public String getIdMinistere() {
		if (!isUpdater()) {
			Set<String> ministereSet = ssPrincipal.getMinistereIdSet();
			if (ministereSet != null && ministereSet.size() == 1) {
				return ministereSet.iterator().next();
			}
		}
		return null;
	}

	/**
	 * Controle l'accès à la vue correspondante
	 * 
	 */
	public boolean isAccessAuthorized() {
		SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
		return (ssPrincipal.isAdministrator() || ssPrincipal
				.isMemberOf(SolonEpgBaseFunctionConstant.ADMINISTRATION_INDEXATION_READER));
	}

}
