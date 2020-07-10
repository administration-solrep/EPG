package fr.dila.solonepg.web.suivi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;

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
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.platform.contentview.jsf.ContentView;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.richfaces.component.UITree;
import org.richfaces.event.NodeExpandedEvent;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.recherche.TableauDynamique;
import fr.dila.solonepg.api.service.CorbeilleService;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.api.service.TableauDynamiqueService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.espace.EspaceSuiviActionsBean;
import fr.dila.solonepg.web.recherche.TableauDynamiqueActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.constant.STContentViewConstant;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.api.dossier.STDossier.DossierState;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.GouvernementNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Classe de gestion de l'arbre de suivi.
 * 
 * @author asatre
 */
@Name("espaceSuiviTree")
@Scope(ScopeType.CONVERSATION)
public class EspaceSuiviTreeBean implements Serializable {

	/**
	 * requete pour rien recuperer
	 */
	public static final String							DEFAULT_QUERY		= STContentViewConstant.DEFAULT_EMPTY_QUERY;

	private static final long							serialVersionUID	= 1L;

	private List<EspaceSuiviTreeNode>					rootNodes;

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(required = true, create = true)
	protected SSPrincipal								ssPrincipal;

	@In(create = true)
	protected transient ResourcesAccessor				resourcesAccessor;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean	routingWebActions;

	@In(create = true, required = false)
	protected transient TableauDynamiqueActionsBean		tableauDynamiqueActions;

	@In(create = true)
	protected transient EspaceSuiviActionsBean			espaceSuiviActions;

	@In(create = true)
	protected transient NavigationContext				navigationContext;

	@In(create = true, required = true)
	protected ContentViewActions						contentViewActions;

	@In(create = true)
	protected transient DocumentsListsManager			documentsListsManager;

	@In(create = true)
	protected transient FacesMessages					facesMessages;

	private String										query;

	private Map<String, Set<String>>					mapMailboxDocIds;

	private Map<String, Set<String>>					mapMailboxIds;

	private String										currentNodeId;

	private Map<String, EspaceSuiviTreeNode>			tdNodesMap;

	private EspaceSuiviTreeNode							currentItem;

	private Set<String>									currentUserMailboxDocIds;

	private Set<String>									currentUserMailboxIds;

	public List<EspaceSuiviTreeNode> getEspaceSuivi() {
		if (rootNodes == null) {
			load();
		}
		return rootNodes;
	}

	private void load() {
		rootNodes = new ArrayList<EspaceSuiviTreeNode>();
		mapMailboxDocIds = new HashMap<String, Set<String>>();
		mapMailboxIds = new HashMap<String, Set<String>>();

		for (final EspaceSuiviEnum espaceSuiviEnum : EspaceSuiviEnum.values()) {

			if (espaceSuiviEnum.getRight() == null || ssPrincipal.isMemberOf(espaceSuiviEnum.getRight())) {
				final String label = resourcesAccessor.getMessages().get(espaceSuiviEnum.getLabel());
				final EspaceSuiviTreeNode treeNode = new EspaceSuiviTreeNode(label, espaceSuiviEnum.getId(),
						DEFAULT_QUERY, espaceSuiviEnum.getType(), null);
				rootNodes.add(treeNode);

				if (!OrganigrammeType.OTHER.equals(espaceSuiviEnum.getType())) {
					// sinon l'image "+" n'apparaît pas
					treeNode.addChild(new EspaceSuiviTreeNode("fake", espaceSuiviEnum.getId(), DEFAULT_QUERY,
							OrganigrammeType.USER, null));
				}
			}

		}

	}

	public Boolean adviseNodeOpened(final UITree treeComponent) {
		final Object value = treeComponent.getRowData();
		if (value instanceof EspaceSuiviTreeNode) {
			final EspaceSuiviTreeNode minNode = (EspaceSuiviTreeNode) value;
			return minNode.isOpened();
		}
		return null;
	}

	public void changeExpandListener(final NodeExpandedEvent event) throws ClientException {
		final UIComponent component = event.getComponent();
		if (component instanceof UITree) {
			final UITree treeComponent = (UITree) component;
			final Object value = treeComponent.getRowData();
			if (value instanceof EspaceSuiviTreeNode) {
				final EspaceSuiviTreeNode node = (EspaceSuiviTreeNode) value;
				if (!node.isOpened()) {
					addChildrenNodes(node);
					node.setOpened(Boolean.TRUE);
					query = node.getQuery();
					if (StringUtils.isBlank(node.getCount()) && StringUtils.isNotBlank(node.getQuery())
							&& !DEFAULT_QUERY.equals(node.getQuery())) {
						node.setCount(QueryUtils.doCountQuery(documentManager,
								QueryUtils.ufnxqlToFnxqlQuery(node.getQuery())).toString());
					}
				} else {
					node.setOpened(Boolean.FALSE);
				}
			}
		}
	}

	private void addChildrenNodes(final EspaceSuiviTreeNode node) throws ClientException {
		if (node.isLoaded()) {
			return;
		} else {
			node.setChildren(new ArrayList<EspaceSuiviTreeNode>());
		}

		switch (node.getType()) {
			case BASE:
				addChildrenRootNodes(node);
				break;
			case GOUVERNEMENT:
				addMin(node);
				break;
			case MINISTERE:
				addDir(node);
				break;
			case DIRECTION:
				addPst(node);
				break;
			case UNITE_STRUCTURELLE:
				addPst(node);
				break;
			default:
				break;
		}

		node.setLoaded(true);

	}

	private void addPst(final EspaceSuiviTreeNode nodeParent) throws ClientException {
		final EpgOrganigrammeService epgOrganigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
		final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();

		new UnrestrictedSessionRunner(documentManager) {
			@Override
			public void run() throws ClientException {
				final OrganigrammeNode organigrammeNode = nodeParent.getNode();
				if (organigrammeNode != null) {
					for (final OrganigrammeNode entiteNode : epgOrganigrammeService.getChildrenList(session,
							organigrammeNode, Boolean.TRUE)) {
						if (!OrganigrammeType.USER.equals(entiteNode.getType())) {

							final String query = getDossierCountQuery(session, nodeParent.getDossierState(),
									mailboxPosteService, entiteNode);

							final EspaceSuiviTreeNode treeNode = new EspaceSuiviTreeNode(entiteNode.getLabel(),
									entiteNode.getId(), query, entiteNode.getType(), nodeParent.getDossierState());
							treeNode.setNode(entiteNode);
							treeNode.setParent(nodeParent);
							nodeParent.addChild(treeNode);

							if (!OrganigrammeType.POSTE.equals(entiteNode.getType())) {
								// sinon l'image "+" n'apparaît pas
								treeNode.addChild(new EspaceSuiviTreeNode("fake", entiteNode.getId(), query, entiteNode
										.getType(), nodeParent.getDossierState()));
							}
						}
					}
				}
			}
		}.runUnrestricted();

		for (EspaceSuiviTreeNode node : nodeParent.getChildren()) {
			if (DEFAULT_QUERY.equals(node.getQuery())) {
				node.setCount("");
			} else {
				node.setCount(QueryUtils.doCountQuery(documentManager, QueryUtils.ufnxqlToFnxqlQuery(node.getQuery()))
						.toString());
			}

		}
	}

	private String getDossierCountQuery(CoreSession session, final STDossier.DossierState dossierState,
			final MailboxPosteService mailboxPosteService, final OrganigrammeNode entiteNode) throws ClientException {

		Set<String> mailboxDocIds = new HashSet<String>();
		Set<String> mailboxIds = new HashSet<String>();

		GouvernementNode currentGouv = STServiceLocator.getSTGouvernementService().getCurrentGouvernement();

		if (OrganigrammeType.POSTE.equals(entiteNode.getType())) {
			final Mailbox mailbox = mailboxPosteService.getMailboxPoste(session, entiteNode.getId());
			if (mailbox != null && mailbox.getDocument() != null) {
				mailboxDocIds.add(mailbox.getDocument().getId());
				mailboxIds.add(mailbox.getId());
			}

		} else {
			if (mapMailboxDocIds.get(entiteNode.getId()) != null) {
				mailboxDocIds = mapMailboxDocIds.get(entiteNode.getId());
				mailboxIds = mapMailboxIds.get(entiteNode.getId());
			} else {
				List<String> list = null;
				if (entiteNode.getId().equals(currentGouv.getId())) {
					// noeud gouvernement
					if (ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.INFOCENTRE_GENERAL_FULL_READER)) {
						// admin sgg
						list = STServiceLocator.getSTPostesService().getPosteIdInSubNode(entiteNode);
					} else {
						list = new ArrayList<String>();
						final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
						final STPostesService postesService = STServiceLocator.getSTPostesService();
						final Set<String> idsMinistere = ssPrincipal.getMinistereIdSet();
						for (final String idMinistere : idsMinistere) {
							final EntiteNode node = ministeresService.getEntiteNode(idMinistere);
							if (node != null) {
								list.addAll(postesService.getPosteIdInSubNode(node));
							}
						}
					}
				} else {
					list = STServiceLocator.getSTPostesService().getPosteIdInSubNode(entiteNode);
				}

				final List<Mailbox> mailboxList = mailboxPosteService.getMailboxPosteList(session, list);
				for (Mailbox mailbox : mailboxList) {
					if (mailbox != null && mailbox.getDocument() != null) {
						mailboxDocIds.add(mailbox.getDocument().getId());
						mailboxIds.add(mailbox.getId());
					}
				}

				mapMailboxDocIds.put(entiteNode.getId(), mailboxDocIds);
				mapMailboxIds.put(entiteNode.getId(), mailboxIds);

			}
		}

		return SolonEpgServiceLocator.getCorbeilleService().getInfocentreQuery(documentManager, dossierState,
				mailboxDocIds, mailboxIds);

	}

	private void addDir(final EspaceSuiviTreeNode nodeParent) throws ClientException {
		final EpgOrganigrammeService epgOrganigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
		final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();

		new UnrestrictedSessionRunner(documentManager) {
			@Override
			public void run() throws ClientException {
				final OrganigrammeNode organigrammeNode = nodeParent.getNode();
				if (organigrammeNode != null) {

					for (final OrganigrammeNode entiteNode : epgOrganigrammeService.getChildrenList(session,
							organigrammeNode, Boolean.TRUE)) {
						final StringBuilder label = new StringBuilder();
						if (entiteNode instanceof UniteStructurelleNode) {
							label.append(((UniteStructurelleNode) entiteNode)
									.getNorDirectionForMinistereId(organigrammeNode.getId()));
							label.append(" - ");
						}
						label.append(entiteNode.getLabel());

						final String query = getDossierCountQuery(session, nodeParent.getDossierState(),
								mailboxPosteService, entiteNode);

						final EspaceSuiviTreeNode treeNode = new EspaceSuiviTreeNode(label.toString(),
								entiteNode.getId(), query, entiteNode.getType(), nodeParent.getDossierState());
						treeNode.setNode(entiteNode);
						treeNode.setParent(nodeParent);
						nodeParent.addChild(treeNode);

						if (!OrganigrammeType.POSTE.equals(entiteNode.getType())) {
							// sinon l'image "+" n'apparaît pas
							treeNode.addChild(new EspaceSuiviTreeNode("fake", entiteNode.getId(), query, entiteNode
									.getType(), nodeParent.getDossierState()));
						}

					}
				}
			}
		}.runUnrestricted();

		for (EspaceSuiviTreeNode node : nodeParent.getChildren()) {
			if (DEFAULT_QUERY.equals(node.getQuery())) {
				node.setCount("");
			} else {
				node.setCount(QueryUtils.doCountQuery(documentManager, QueryUtils.ufnxqlToFnxqlQuery(node.getQuery()))
						.toString());
			}

		}

	}

	private void addMin(final EspaceSuiviTreeNode nodeParent) throws ClientException {
		final List<OrganigrammeNode> listNode = new ArrayList<OrganigrammeNode>();

		if (ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.INFOCENTRE_GENERAL_FULL_READER)) {
			// admin sgg
			final OrganigrammeNode currentGouv = STServiceLocator.getSTGouvernementService().getCurrentGouvernement();
			listNode.addAll(STServiceLocator.getOrganigrammeService().getChildrenList(documentManager, currentGouv,
					Boolean.TRUE));
		} else {
			Set<String> idsMinistere = null;
			if (ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.INFOCENTRE_GENERAL_DIR_READER)) {
				idsMinistere = ssPrincipal.getDirectionIdSet();
				final STUsAndDirectionService usService = STServiceLocator.getSTUsAndDirectionService();
				for (final String idMinistere : idsMinistere) {
					final OrganigrammeNode node = usService.getUniteStructurelleNode(idMinistere);
					if (node != null) {
						listNode.add(node);
					}
				}
			} else {
				idsMinistere = ssPrincipal.getMinistereIdSet();
				final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
				for (final String idMinistere : idsMinistere) {
					final OrganigrammeNode node = ministeresService.getEntiteNode(idMinistere);
					if (node != null) {
						listNode.add(node);
					}
				}
			}
		}

		new UnrestrictedSessionRunner(documentManager) {
			@Override
			public void run() throws ClientException {
				final Date today = Calendar.getInstance().getTime();
				final EpgOrganigrammeService organigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
				final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
				for (final OrganigrammeNode entiteNode : listNode) {

					if (!OrganigrammeType.POSTE.equals(entiteNode.getType()) && entiteNode.isActive()
							&& (entiteNode.getDateFin() == null || entiteNode.getDateFin().compareTo(today) > 0)) {
						final StringBuilder label = new StringBuilder();
						if (entiteNode instanceof EntiteNode) {
							String nodeLabel = ((EntiteNode) entiteNode).getLabel();
							if (StringUtils.isNotBlank(nodeLabel) && nodeLabel.startsWith("NOR ")) {
								// skip des ministeres qui ne servent qu'a avoir un lien sur d'ancien NOR dont tous
								// les dossiers sont publiés
								continue;
							}
							label.append(((EntiteNode) entiteNode).getEdition());
						} else if (entiteNode instanceof UniteStructurelleNode) {
							final List<OrganigrammeNode> listParent = organigrammeService.getParentList(entiteNode);
							if (listParent != null && !listParent.isEmpty()) {
								label.append(((UniteStructurelleNode) entiteNode)
										.getNorDirectionForMinistereId(listParent.get(0).getId()));
								label.append(" - ");
								label.append(entiteNode.getLabel());
							}
						}

						final String query = getDossierCountQuery(session, nodeParent.getDossierState(),
								mailboxPosteService, entiteNode);

						final EspaceSuiviTreeNode treeNode = new EspaceSuiviTreeNode(label.toString(),
								entiteNode.getId(), query, entiteNode.getType(), nodeParent.getDossierState());
						treeNode.setNode(entiteNode);
						treeNode.setParent(nodeParent);
						nodeParent.addChild(treeNode);

						if (!OrganigrammeType.POSTE.equals(entiteNode.getType())) {
							// sinon l'image "+" n'apparaît pas
							treeNode.addChild(new EspaceSuiviTreeNode("fake", entiteNode.getId(), query, entiteNode
									.getType(), nodeParent.getDossierState()));
						}
					}
				}
			}
		}.runUnrestricted();

		for (EspaceSuiviTreeNode node : nodeParent.getChildren()) {
			if (DEFAULT_QUERY.equals(node.getQuery())) {
				node.setCount("");
			}
		}

	}

	private void addChildrenRootNodes(final EspaceSuiviTreeNode node) throws ClientException {

		if (node.isLoaded()) {
			return;
		}

		if (EspaceSuiviEnum.MON_INFOCENTRE.getId().equals(node.getKey())) {
			loadRootNode(node, OrganigrammeType.POSTE);
		} else if (EspaceSuiviEnum.INFOCENTRE_GENERAL.getId().equals(node.getKey())) {
			loadRootNode(node, OrganigrammeType.GOUVERNEMENT);
		} else if (EspaceSuiviEnum.INFOCENTRE_SGG.getId().equals(node.getKey())) {
			loadInfocentreSgg(node, OrganigrammeType.GOUVERNEMENT);
		} else if (EspaceSuiviEnum.TABLEAU_DYNAMIQUE.getId().equals(node.getKey())) {
			loadTableauDynamique(node);
		}

		node.setLoaded(true);
	}

	private void loadInfocentreSgg(final EspaceSuiviTreeNode nodeParent, final OrganigrammeType type) {
		for (final EspaceSuiviInfocentreSggEnum infocentreSggEnum : EspaceSuiviInfocentreSggEnum.values()) {

			if (infocentreSggEnum.getRight() == null || ssPrincipal.isMemberOf(infocentreSggEnum.getRight())) {
				final String label = resourcesAccessor.getMessages().get(infocentreSggEnum.getLabel());
				final EspaceSuiviTreeNode treeNode = new EspaceSuiviTreeNode(label, infocentreSggEnum.getId(),
						DEFAULT_QUERY, infocentreSggEnum.getType(), null);
				nodeParent.addChild(treeNode);

				if (!OrganigrammeType.OTHER.equals(infocentreSggEnum.getType())) {
					// sinon l'image "+" n'apparaît pas
					treeNode.addChild(new EspaceSuiviTreeNode("fake", infocentreSggEnum.getId(), DEFAULT_QUERY,
							OrganigrammeType.USER, null));
				}
			}
		}
	}

	private void loadRootNode(final EspaceSuiviTreeNode nodeParent, final OrganigrammeType type) throws ClientException {

		final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
		final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
		final Map<DossierState, String> map = new HashMap<DossierState, String>();

		new UnrestrictedSessionRunner(documentManager) {
			@Override
			public void run() throws ClientException {
				for (final DossierState dossierState : STDossier.DossierState.values()) {
					String query = DEFAULT_QUERY;
					switch (type) {
						case POSTE:

							if (currentUserMailboxDocIds == null || currentUserMailboxIds == null) {

								List<Mailbox> mailboxList = mailboxPosteService.getMailboxPosteList(documentManager);
								currentUserMailboxDocIds = new HashSet<String>();
								currentUserMailboxIds = new HashSet<String>();

								for (Mailbox mailbox : mailboxList) {
									if (mailbox != null && mailbox.getDocument() != null) {
										currentUserMailboxDocIds.add(mailbox.getDocument().getId());
										currentUserMailboxIds.add(mailbox.getId());
									}
								}
							}

							query = corbeilleService.getInfocentreQuery(documentManager, dossierState,
									currentUserMailboxDocIds, currentUserMailboxIds);
							map.put(dossierState, query);

							break;
						default:
							query = DEFAULT_QUERY;
							map.put(dossierState, DEFAULT_QUERY);
							break;
					}

					final StringBuilder builder = new StringBuilder();
					builder.append("espace.suivi.");
					builder.append(dossierState.name());
					final String label = resourcesAccessor.getMessages().get(builder.toString());

					final EspaceSuiviTreeNode treeNode = new EspaceSuiviTreeNode(label, dossierState.name(), query,
							type, dossierState);
					treeNode.setParent(nodeParent);
					if (!OrganigrammeType.POSTE.equals(type)) {
						// sinon l'image "+" n'apparaît pas
						treeNode.addChild(new EspaceSuiviTreeNode("fake", dossierState.name(), "", type, nodeParent
								.getDossierState()));
					}
					nodeParent.addChild(treeNode);
				}
			}
		}.runUnrestricted();

		for (EspaceSuiviTreeNode node : nodeParent.getChildren()) {
			if (DEFAULT_QUERY.equals(node.getQuery())) {
				node.setCount("");
			} else {
				node.setCount(QueryUtils.doCountQuery(documentManager, QueryUtils.ufnxqlToFnxqlQuery(node.getQuery()))
						.toString());
			}

		}
	}

	private void loadTableauDynamique(final EspaceSuiviTreeNode nodeParent) throws ClientException {
		nodeParent.setChildren(new ArrayList<EspaceSuiviTreeNode>());
		tdNodesMap = new HashMap<String, EspaceSuiviTreeNode>();
		final TableauDynamiqueService tableauDynamiqueService = SolonEpgServiceLocator.getTableauDynamiqueService();
		for (final DocumentModel docModel : tableauDynamiqueService.findAll(documentManager)) {
			final TableauDynamique tableauDynamique = docModel.getAdapter(TableauDynamique.class);
			final EspaceSuiviTreeNode treeNode = new EspaceSuiviTreeNode(docModel.getTitle(), docModel.getId(),
					tableauDynamique.getQuery(), OrganigrammeType.BASE, null);
			setRole(treeNode, docModel);
			treeNode.setParent(nodeParent);
			nodeParent.addChild(treeNode);
			tdNodesMap.put(docModel.getId(), treeNode);
		}
	}

	private void setRole(final EspaceSuiviTreeNode treeNode, final DocumentModel docModel) {

		treeNode.setRole(EspaceSuiviRoleEnum.EXECUTOR);
		if (ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMIN_FONCTIONNEL_TABLEAU_DYNAMIQUE_DESINATAIRE_VIEWER)) {
			// admin fonctionnel
			treeNode.setRole(EspaceSuiviRoleEnum.GOD);
		}
		if (ssPrincipal.getName().equals(DublincoreSchemaUtils.getCreator(docModel))) {
			treeNode.setRole(EspaceSuiviRoleEnum.GOD);
		}
	}

	public String setElement(final EspaceSuiviTreeNode item) throws ClientException {

		String result = null;
		navigationContext.setCurrentDocument(null);
		final ContentView contentView = contentViewActions.getCurrentContentView();
		if (contentView != null) {
			documentsListsManager.resetWorkingList(contentView.getSelectionListName());
		}
		if (item == null) {
			query = null;
			rootNodes = null;
		} else {
			query = FlexibleQueryMaker.KeyCode.UFXNQL.key + item.getQuery();
			if (StringUtils.isBlank(item.getCount()) && StringUtils.isNotBlank(item.getQuery())
					&& !DEFAULT_QUERY.equals(item.getQuery())) {
				item.setCount(QueryUtils.doCountQuery(documentManager, QueryUtils.ufnxqlToFnxqlQuery(item.getQuery()))
						.toString());
			}

			currentItem = item;
		}

		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

		if (item != null && EspaceSuiviEnum.DOSSIER_SIGNALE.getId().equals(item.getKey())) {
			routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_SUIVI_DOSSIER_SIGNALES_VIEW);
			result = SolonEpgViewConstant.ESPACE_SUIVI_DOSSIER_SIGNALES_VIEW;
		} else if (item != null && EspaceSuiviEnum.MES_ALERTES.getId().equals(item.getKey())) {
			routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_SUIVI_MES_ALERTES_VIEW);
			result = SolonEpgViewConstant.ESPACE_SUIVI_MES_ALERTES_VIEW;
		} else if (item != null && EspaceSuiviEnum.STATISTIQUES.getId().equals(item.getKey())) {
			routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_SUIVI_STATISTIQUES_VIEW);
			result = SolonEpgViewConstant.ESPACE_SUIVI_STATISTIQUES_VIEW;
		} else if (item != null && EspaceSuiviInfocentreSggEnum.GESTION_DELISTE.getId().equals(item.getKey())) {
			routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_SUIVI_INFOCENTRE_SGG);
			result = SolonEpgViewConstant.ESPACE_SUIVI_INFOCENTRE_SGG;
		} else if (item != null && EspaceSuiviInfocentreSggEnum.SIGNALE.getId().equals(item.getKey())) {
			routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_SUIVI_INFOCENTRE_SGG_TEXTE_SIGNALE);
			result = SolonEpgViewConstant.ESPACE_SUIVI_INFOCENTRE_SGG_TEXTE_SIGNALE;
		} else {
			routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_SUIVI_VIEW);
			result = espaceSuiviActions.navigateToWithDossierContentView();
		}

		return result;
	}

	public String getQuery() {
		if (query == null) {
			return DEFAULT_QUERY;
		}
		return query;
	}

	public String reset() throws ClientException {
		return setElement(null);
	}

	public String showTableauDynamique() {
		if (currentNodeId != null) {
			final EspaceSuiviTreeNode currentNode = getCurrentNode();
			query = currentNode.getQuery();
			routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_SUIVI_VIEW);
			setCurrentNodeId(null);
			return espaceSuiviActions.navigateToWithDossierContentView();
		}
		return null;
	}

	private EspaceSuiviTreeNode getCurrentNode() {
		return tdNodesMap.get(currentNodeId);
	}

	public String udpateTableauDynamique() throws ClientException {
		if (currentNodeId != null) {
			final EspaceSuiviTreeNode currentNode = getCurrentNode();
			final DocumentModel tabModel = documentManager.getDocument(new IdRef(currentNode.getKey()));
			if (tabModel != null) {
				tableauDynamiqueActions.setCurrentTableauDynamique(tabModel);
				setCurrentNodeId(null);
				return tableauDynamiqueActions.navigateToEditTableauDynamique();
			}
		}
		return null;
	}

	public String deleteTableauDynamique() throws ClientException {
		if (currentNodeId != null) {
			final EspaceSuiviTreeNode currentNode = getCurrentNode();
			final TableauDynamiqueService tableauDynamiqueService = SolonEpgServiceLocator.getTableauDynamiqueService();
			tableauDynamiqueService.deleteTableauDynamique(documentManager,
					documentManager.getDocument(new IdRef(currentNode.getKey())));
			Events.instance().raiseEvent(SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_CHANGED_EVENT);
			Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
			setCurrentNodeId(null);
			facesMessages.add(StatusMessage.Severity.INFO,
					resourcesAccessor.getMessages().get("Tableau dynamique supprimé"));
			return espaceSuiviActions.navigateToWithDossierContentView();
		}
		return null;
	}

	public void setCurrentNodeId(final String currentNodeId) {
		this.currentNodeId = currentNodeId;
	}

	public String getCurrentNodeId() {
		return currentNodeId;
	}

	@Observer(SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_CHANGED_EVENT)
	public void refreshTableauDynamique() throws ClientException {
		if (rootNodes != null) {
			for (final EspaceSuiviTreeNode node : rootNodes) {
				if (EspaceSuiviEnum.TABLEAU_DYNAMIQUE.getId().equals(node.getKey()) && node.isLoaded()) {
					loadTableauDynamique(node);
				}
			}
		}
	}

	public Boolean isCurrentItem(final EspaceSuiviTreeNode item) {
		if (item != null && currentItem != null) {
			return currentItem.getCompleteKey().equals(item.getCompleteKey());
		}
		return false;
	}

}
