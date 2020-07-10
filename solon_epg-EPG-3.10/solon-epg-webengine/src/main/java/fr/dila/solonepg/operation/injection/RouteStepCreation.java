package fr.dila.solonepg.operation.injection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.util.DocumentHelper;
import org.nuxeo.ecm.automation.core.util.Properties;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.directory.DirectoryException;
import org.nuxeo.ecm.platform.comment.api.CommentableDocument;
import org.nuxeo.ecm.platform.comment.workflow.utils.CommentsConstants;

import fr.dila.cm.caselink.CaseLinkType;
import fr.dila.cm.cases.CaseConstants;
import fr.dila.cm.mailbox.Mailbox;
import fr.dila.ecm.platform.routing.api.DocumentRoutingConstants;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.CorbeilleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.feuilleroute.STRouteStep;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.core.service.STServiceLocator;

@Operation(id = RouteStepCreation.ID, category = Constants.CAT_DOCUMENT, label = "Create Route Step", description = "Create Route Step.")
public class RouteStepCreation {

	private static final Log	repriseLog	= LogFactory.getLog("reprise-log");

	public static final String	ID			= "EPG.CreateRouteStep";

	@Context
	protected OperationContext	ctx;

	@Context
	protected CoreSession		session;

	@Param(name = "name", required = true, order = 1)
	protected String			name;

	@Param(name = "parentPath", required = true, order = 2)
	protected String			parentPath;

	@Param(name = "destinataire", required = true, order = 3)
	protected String			destinataire;

	@Param(name = "ministereResp", required = false, order = 4)
	protected String			ministereResp;

	@Param(name = "destinataireLabel", required = true, order = 5)
	protected String			destinataireLabel;

	@Param(name = "userId", required = false, order = 5)
	protected String			userId;

	@Param(name = "statut", required = true, order = 6)
	protected String			statut;

	@Param(name = "auteur", required = false, order = 7)
	protected String			auteur;

	@Param(name = "dateFinEtape", required = false, order = 8)
	protected String			dateFinEtape;

	@Param(name = "properties")
	protected Properties		properties;

	@OperationMethod
	public DocumentModel run(final DocumentModel doc) throws Exception {
		repriseLog.debug("Ajout de l'etape " + name);
		String poste = null;
		DocumentModel step = null;
		try {
			String comment = null;
			step = session.createDocumentModel(parentPath, name, STConstant.ROUTE_STEP_DOCUMENT_TYPE);
			step = session.createDocument(step);
			DocumentHelper.setProperties(session, step, properties);

			if (destinataire.startsWith("uniteStructurelle-")) {
				final String identifier = destinataire.replace("uniteStructurelle-", "");
				final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
				final List<EntiteNode> nouveauMinistereList = ministeresService
						.getMinistereParentFromUniteStructurelle(identifier);
				if (nouveauMinistereList.size() > 0) {
					destinataire = "poste-reprise-" + nouveauMinistereList.get(0).getId();
					// String destination = organigrammeService.getUniteStructurelleNode(destinataire).getLabel();
					comment = destinataireLabel + "(type = Unité Structurelle)";
				} else {
					if (ministeresService.getEntiteNode(identifier) != null) {
						destinataire = "poste-reprise-" + identifier;
						comment = destinataireLabel + " (type = Ministere)";
					} else {
						destinataire = "poste-reprise-" + ministereResp;
						comment = destinataireLabel + " (type = Ministere)";
						// throw new Exception("pas de poste de reprise trouvee pour l'uniteStructurelle: " + id);
					}
				}
				poste = destinataire;
			} else if (destinataire.startsWith("agent-")) {
				final String agent = destinataire.replace("agent-", "");
				poste = getPosteFromUser(agent, ministereResp);
				comment = destinataireLabel + " (type = Agent)";
			} else {
				PosteNode posteDoc = STServiceLocator.getSTPostesService().getPoste(destinataire);
				if (posteDoc == null && !"validated".equals(statut) && !"validatedAndFdrDone".equals(statut)) {
					posteDoc = SolonEpgServiceLocator.getEpgOrganigrammeService().createDesactivatePoste(session,
							destinataire, destinataireLabel);
					poste = destinataire;
				} else {
					poste = destinataire;
				}
			}

			try {

				if ("validated".equals(statut) || "validatedAndFdrDone".equals(statut)) {
					final Dossier dossier = doc.getAdapter(Dossier.class);
					final String userMailboxId = STConstant.PREFIX_POSTE + poste;
					if (StringUtils.isBlank(dossier.getCreatorPoste())) {
						dossier.setCreatorPoste(userMailboxId);
						dossier.save(session);
					}
					step.setPropertyValue("rtsk:validationUserId", userId);
					step.setPropertyValue("rtsk:posteLabel", destinataireLabel);
					step.setPropertyValue(CaseConstants.STEP_DISTRIBUTION_MAILBOX_ID_PROPERTY_NAME, userMailboxId);
					step.followTransition("toReadyByCopy");
					session.saveDocument(step);
					step.followTransition("toRunning");
					session.saveDocument(step);
					step.followTransition("toDone");
					session.saveDocument(step);

					Date date = null;
					try {
						final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
						date = formatter.parse(dateFinEtape);
						final Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);
						calendar.add(Calendar.DATE, 1);
						step.setPropertyValue(STConstant.STEP_DATE_FIN_ETAPE_PROPERTY_NAME, calendar);
					} catch (Exception e) {
						repriseLog.warn("date fin d'etape estmal formatte " + date);
					}
					step.setPropertyValue(STConstant.STEP_VALIDATION_USER_LABEL_PROPERTY_NAME, auteur);

					final Map<String, List<String>> userMailboxMap = new HashMap<String, List<String>>();
					final List<String> userMailboxList = new ArrayList<String>();
					userMailboxList.add(userMailboxId);
					userMailboxMap.put(CaseLinkType.FOR_ACTION.toString(), userMailboxList);

					dossier.addParticipants(userMailboxMap);
					dossier.save(session);

					if (doc.getCurrentLifeCycleState().equals(Dossier.DossierState.init.toString())) {
						// Passage du dossier à l'état lancé
						doc.followTransition(Dossier.DossierTransition.toRunning.toString());
					}

					if (step.getParentRef() != null) {
						final DocumentModel stepParent = session.getDocument(step.getParentRef());
						final String type = stepParent.getType();
						if (DocumentRoutingConstants.STEP_FOLDER_DOCUMENT_TYPE.equals(type)
								|| STConstant.FEUILLE_ROUTE_DOCUMENT_TYPE.equals(type)
								&& "validatedAndFdrDone".equals(statut)) {
							boolean allChildrenIsDone = true;
							final DocumentModelList children = session.getChildren(step.getParentRef());
							if (children != null) {
								for (final DocumentModel child : children) {
									if (child.getCurrentLifeCycleState().equals("done") == false) {
										allChildrenIsDone = false;
										break;
									}
								}
							}

							if (allChildrenIsDone && stepParent.getCurrentLifeCycleState().equals("ready")) {
								// Passage du dossier à l'état lancé
								stepParent.followTransition("toRunning");
								stepParent.followTransition("toDone");
							}

							else if (allChildrenIsDone && stepParent.getCurrentLifeCycleState().equals("running")) {
								stepParent.followTransition("toDone");
							}
						}
					}
				} else {
					final Mailbox userMailbox = SSServiceLocator.getMailboxPosteService().getOrCreateMailboxPoste(
							session, poste);
					step.setPropertyValue(CaseConstants.STEP_DISTRIBUTION_MAILBOX_ID_PROPERTY_NAME, userMailbox.getId());

					final Dossier dossier = doc.getAdapter(Dossier.class);

					if (StringUtils.isBlank(dossier.getCreatorPoste())) {
						dossier.setCreatorPoste(userMailbox.getId());
						dossier.save(session);
					}
					if ("running".equals(statut)) {

						step.followTransition("toReadyByCopy");
						final STRouteStep stRouteInstance = step.getAdapter(STRouteStep.class);

						new UnrestrictedSessionRunner(session) {
							@Override
							public void run() throws ClientException {
								stRouteInstance.run(session);
							}
						}.runUnrestricted();

						if (!doc.getCurrentLifeCycleState().equals(Dossier.DossierState.running.toString())) {
							// Passage du dossier à l'état lancé
							doc.followTransition(Dossier.DossierTransition.toRunning.toString());
							// Changement du statut du dossier
							dossier.setStatut(VocabularyConstants.STATUT_LANCE);

							final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
							final DocumentModel dossierLinkDoc = corbeilleService.getDossierLink(session, step.getId());

							final DossierLink dossierLink = dossierLinkDoc.getAdapter(DossierLink.class);
							dossierLink.setCaseLifeCycleState(Dossier.DossierState.running.toString());
							dossierLink.save(session);
						}

						if (step.getParentRef() != null) {
							final DocumentModel stepParent = session.getDocument(step.getParentRef());

							if (stepParent.getCurrentLifeCycleState().equals("ready")) {
								// Passage du dossier à l'état lancé
								stepParent.followTransition("toRunning");
							}
						}

					} else if ("runningInit".equals(statut)) {

						step.followTransition("toReadyByCopy");
						final STRouteStep stRouteInstance = step.getAdapter(STRouteStep.class);

						new UnrestrictedSessionRunner(session) {
							@Override
							public void run() throws ClientException {
								stRouteInstance.run(session);
							}
						}.runUnrestricted();

						if (step.getParentRef() != null) {
							final DocumentModel stepParent = session.getDocument(step.getParentRef());

							if (stepParent.getCurrentLifeCycleState().equals("ready")) {
								// Passage du dossier à l'état lancé
								stepParent.followTransition("toRunning");
							}
						}
					} else {
						step.followTransition("toReadyByCopy");
					}
				}
				session.saveDocument(step);
			} catch (final NullPointerException e) {
				throw new Exception("pas de mail Box trouve: " + destinataire);
			}

			if (comment != null) {
				DocumentModel myComment = session.createDocumentModel("Comment");
				myComment.setProperty("comment", "author", "reprise");
				myComment.setProperty("comment", "text", "Destination : " + comment);
				myComment.setProperty("comment", "creationDate", Calendar.getInstance());
				final CommentableDocument commentableDoc = step.getAdapter(CommentableDocument.class);
				myComment = commentableDoc.addComment(myComment);
				session.followTransition(myComment.getRef(), CommentsConstants.TRANSITION_TO_PUBLISHED_STATE);
			}

			session.save();
			repriseLog.debug("Ajout de l'etape " + name + " -> OK");
		} catch (Exception e) {
			repriseLog.error("Ajout de l'etape " + name + " -> KO", e);
			throw new Exception("Erreur lors de l'ajout de l'etape " + name, e);
		}
		return step;
	}

	private Set<String> getMinistereSet(final String userId) throws DirectoryException, ClientException {
		// Récupère l'utilisateur
		final DocumentModel userModel = STServiceLocator.getUserManager().getUserModel(userId);
		if (userModel == null) {
			repriseLog.debug(String.format("No User by that name. Maybe a wrong id or virtual user"));
			return Collections.emptySet();
		}

		// Récupère la liste des postes de l'utilisateur
		final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
		final List<String> posteNameList = PropertyUtil.getStringListProperty(userModel, STSchemaConstant.USER_SCHEMA,
				STSchemaConstant.USER_POSTES);
		final Set<String> groupSet = new HashSet<String>();
		for (final String posteName : posteNameList) {
			// Recherche l'ensemble des ministères associés au poste
			final List<EntiteNode> ministereNodeList = ministeresService.getMinistereParentFromPoste(posteName);
			for (final OrganigrammeNode ministereNode : ministereNodeList) {
				// TODO Est-on sur de ne pas avoir de caractères bizarres dans ministereId ? S'en assurer, dans la
				// gestion de l'organigramme
				final String ministereId = ministereNode.getId();
				groupSet.add(ministereId);
			}
		}

		return groupSet;
	}

	/**
	 * get poste from user
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private String getPosteFromUser(final String user, final String ministereResponsable) throws Exception {
		String ministere = null;
		String poste = null;
		final Set<String> ministereSet = this.getMinistereSet(user);
		if (ministereSet.size() == 1) {
			ministere = ministereSet.iterator().next();
		} else {
			for (final String ministereId : ministereSet) {
				if (ministereId.equals(ministereResponsable)) {
					ministere = ministereId;
					break;
				}
			}
			if (ministereSet.size() > 0 && ministere == null) {
				ministere = ministereSet.iterator().next();
			}
		}
		if (ministere != null) {
			poste = "poste-reprise-" + ministere;
		} else {
			poste = "poste-reprise-" + ministereResponsable;
		}
		return poste;
	}

}
