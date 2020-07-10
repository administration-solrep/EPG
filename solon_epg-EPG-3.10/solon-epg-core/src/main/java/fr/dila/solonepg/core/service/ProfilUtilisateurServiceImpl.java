package fr.dila.solonepg.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.platform.ui.web.directory.VocabularyEntry;

import fr.dila.solonepg.api.administration.ProfilUtilisateur;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgProfilUtilisateurConstants;
import fr.dila.solonepg.api.constant.TypeActe;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.filter.RouteStepFilter;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STProfilUtilisateurConstants;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.feuilleroute.STRouteStep;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.AbstractSTProfilUtilisateurServiceImpl;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;

/**
 * implémentation de l'interface ProfilUtilisateurService
 * 
 * @author arolin
 */
public class ProfilUtilisateurServiceImpl extends AbstractSTProfilUtilisateurServiceImpl implements
		ProfilUtilisateurService {

	/**
	 * nom de la propriété qui renvoie la raison de la notification dans le mail.
	 */
	private static final String		RAISON_NOTIFICATION	= "raisonNotification";

	/**
	 * nom de la propriété qui renvoie le titre de l'acte dans le mail.
	 */
	private static final String		TITRE_ACTE			= "titreActe";

	/**
	 * nom de la propriété qui renvoie le nor dans le mail.
	 */
	private static final String		NOR_VARIABLE_MAIL	= "nor";

	/**
	 * Nombre d'éléments max dans la liste des derniers dossiers consultés
	 */
	private static final int		NB_DOSSIERS_MAX		= 5;

	/**
	 * Séparateur dans la liste des dossiers dernièrement consultés
	 */
	private static final String		IDS_DOSSIERS_SEP	= ",";

	/**
	 * Serial version UID
	 */
	private static final long		serialVersionUID	= 2769161505411104805L;
	/**
	 * Log
	 */
	private static final STLogger	LOGGER				= STLogFactory.getLog(ProfilUtilisateur.class);

	@Override
	public void checkAndSendMailNotificationValidationEtape(final STRouteStep routeStep,
			final DocumentModel dossierDoc, final CoreSession session) throws ClientException {
		// on récupère le poste des utilisateurs
		final String mailboxId = routeStep.getDistributionMailboxId();
		if (mailboxId == null) {
			return;
		}

		final String posteId = SSServiceLocator.getMailboxPosteService().getPosteIdFromMailboxId(mailboxId);
		if (StringUtils.isEmpty(posteId)) {
			return;
		}
		// on récupère la liste des utilisateurs
		final List<STUser> userList = STServiceLocator.getSTPostesService().getUserFromPoste(posteId);

		// si le poste ne contient pas d'utilisateur aucun mail n'est envoyé
		if (userList == null || userList.isEmpty()) {
			return;
		}

		// pour chaque type de notification et pour chaque utilisateur, on détermine si l'on envoie un mail
		final List<STUser> userListMailActe = new ArrayList<STUser>();
		final List<STUser> userListMailUrgent = new ArrayList<STUser>();
		final List<STUser> userListMailMesureNominative = new ArrayList<STUser>();
		final List<STUser> userListMailRetourSgg = new ArrayList<STUser>();
		final List<STUser> userListMailFranchissementAutomatique = new ArrayList<STUser>();
		final Set<String> userNameList = new HashSet<String>();
		// récupération des informations du dossier et de la fdr
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		final String typeActeDossier = dossier.getTypeActe();
		final Boolean dossierUrgent = dossier.getIsUrgent();
		final boolean dossierMesureNominative = dossier.isMesureNominative();
		final boolean automaticValidated = routeStep.isAutomaticValidated();
		// on envoie une notification retour sgg si l'étape qui a été validé l'a été dans un poste du sgg
		final boolean retourSgg = isRetourSgg(session, routeStep);

		// on récupère les profils utilisateurs des utilisateurs du poste
		final StringBuilder builder = new StringBuilder("SELECT p.pusr:notificationDossierUrgent AS notifUrgent, ");
		builder.append("p.pusr:notificationMailSiFranchissementAutomatique AS notifFranchissement, ");
		builder.append("p.pusr:notificationRetourSgg AS notifRetourSgg, ");
		builder.append("p.pusr:notificationMesureNominative AS notifMesureNominative, ");
		builder.append("p.pusr:notificationTypeActes AS typeActe, ");
		builder.append("w.ecm:name as userName FROM ");
		builder.append(SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_DOCUMENT_TYPE);
		builder.append(" AS p, ").append("Workspace");
		builder.append(" AS w  WHERE ").append("w.ecm:name IN (");
		builder.append(StringUtil.getQuestionMark(userList.size()));
		builder.append(") AND p.ecm:parentId = w.ecm:uuid");

		final List<String> paramList = new ArrayList<String>();
		for (final STUser stUser : userList) {
			paramList.add(stUser.getUsername());
		}

		IterableQueryResult res = null;
		try {
			res = QueryUtils.doUFNXQLQuery(session, builder.toString(), paramList.toArray(new String[0]));
			final Iterator<Map<String, Serializable>> iterator = res.iterator();
			// pour chaque utilisateur, on vérifie si l'on envoie ou non de mail
			while (iterator.hasNext()) {
				final Map<String, Serializable> row = iterator.next();
				final String userName = (String) row.get("userName");
				if (StringUtils.isNotEmpty(userName)) {
					STUser currentUser = null;

					// verification de l'envoi de mail dans le cas d'un type d'acte
					final String notificationTypeActe = (String) row.get("typeActe");
					if (StringUtils.isNotEmpty(notificationTypeActe) && notificationTypeActe.equals(typeActeDossier)) {
						currentUser = getCurrentUser(userList, userName, null);
						userListMailActe.add(currentUser);
					}

					// note : il y a autant de ligne que de type d'acte pour un utilisateur, on ne teste qu'une fois les
					// propriétés concernant les données hors type d'acte.
					if (!userNameList.contains(userName)) {
						userNameList.add(userName);
						// verification de l'envoi de mail dans le cas de franchissement d'une étape automatique
						Boolean notifFranchissement = (Boolean) row.get("notifFranchissement");
						notifFranchissement = notifFranchissement && automaticValidated;
						if (notifFranchissement) {
							currentUser = getCurrentUser(userList, userName, currentUser);
							userListMailFranchissementAutomatique.add(currentUser);
						}

						// vérification des conditions concernant l'urgence du dossier
						Boolean notifDossierUrgent = (Boolean) row.get("notifUrgent");
						notifDossierUrgent = notifDossierUrgent && dossierUrgent;
						if (notifDossierUrgent) {
							currentUser = getCurrentUser(userList, userName, currentUser);
							userListMailUrgent.add(currentUser);
						}

						// vérification des conditions concernant le retour sgg
						Boolean notifDossierRetourneSgg = (Boolean) row.get("notifRetourSgg");
						notifDossierRetourneSgg = notifDossierRetourneSgg && retourSgg;
						if (notifDossierRetourneSgg) {
							currentUser = getCurrentUser(userList, userName, currentUser);
							userListMailRetourSgg.add(currentUser);
						}

						// vérification des conditions concernant les mesures nominatives
						Boolean notifDossierMesureNominative = (Boolean) row.get("notifMesureNominative");
						notifDossierMesureNominative = notifDossierMesureNominative && dossierMesureNominative;
						if (notifDossierMesureNominative) {
							currentUser = getCurrentUser(userList, userName, currentUser);
							final List<String> groups = currentUser.getGroups();
							// on envoie un mail sur les mesures nominatives uniquement si l'utilisateur a le doit de
							// voir des mesures nominatives
							if (groups.contains(SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_READER)) {
								userListMailMesureNominative.add(currentUser);
							}
						}
					}
				}
			}
		} finally {
			if (res != null) {
				res.close();
			}
		}

		final STMailService stMailService = STServiceLocator.getSTMailService();
		final Map<String, Object> variablesMap = new HashMap<String, Object>();
		String titreActe = dossier.getTitreActe();
		if (titreActe == null) {
			titreActe = "";
		}
		String nor = dossier.getNumeroNor();
		if (nor == null) {
			nor = "";
		}
		variablesMap.put(NOR_VARIABLE_MAIL, nor);
		variablesMap.put(TITRE_ACTE, titreActe);

		// récupère le lien sur le dossier
		final String dossierId = dossier.getDocument().getId();
		final String linkToDossier = stMailService.getLinkHtmlToDossiers(session, Collections.singletonList(dossierId));
		// TODO objet & texte du mail

		// envoi de mail si le type d'acte correspond
		final ActeService acteService = SolonEpgServiceLocator.getActeService();
		final TypeActe acte = acteService.getActe(typeActeDossier);
		sendMailNotification(session, userListMailActe, stMailService, variablesMap, acte.getLabel(), linkToDossier);

		// envoi de mail si le dossier est urgent
		sendMailNotification(session, userListMailUrgent, stMailService, variablesMap, "Urgent", linkToDossier);

		// envoi de mail si le dossier est une mesure nominative
		sendMailNotification(session, userListMailMesureNominative, stMailService, variablesMap,
				"de mesure nominative", linkToDossier);

		// envoi de mail si le dossier est à l'étape retour du sgg
		sendMailNotification(session, userListMailRetourSgg, stMailService, variablesMap, "renvoyé au SGG",
				linkToDossier);

		// envoi de mail si le dossier a été validé automatiquement
		sendMailNotification(session, userListMailFranchissementAutomatique, stMailService, variablesMap,
				"validé automatiquement", linkToDossier);
	}

	/**
	 * @param session
	 * @param userListMail
	 * @param stMailService
	 * @param variablesMap
	 * @throws ClientException
	 */
	protected void sendMailNotification(final CoreSession session, final List<STUser> userListMail,
			final STMailService stMailService, final Map<String, Object> variablesMap, final String raisonNotification,
			final String linkToDossier) throws ClientException {
		if (!userListMail.isEmpty()) {
			variablesMap.put(RAISON_NOTIFICATION, raisonNotification);
			for (final STUser userToMail : userListMail) {
				final String objetMail = "Un nouveau dossier est disponible dans l'espace de traitement";
				final StringBuilder textMailSb = new StringBuilder();
				textMailSb.append("<html><head></head><body>");
				textMailSb.append("<br/><br/>Bonjour");
				textMailSb.append("<br/>Vous avez un nouveau dossier dans l'espace de traitement.");
				textMailSb.append("<br/>NOR : ${");
				textMailSb.append(NOR_VARIABLE_MAIL);
				textMailSb.append("}<br/>Titre de l'acte : ${");
				textMailSb.append(TITRE_ACTE);
				textMailSb.append("}<br/>Dossier ${");
				textMailSb.append(RAISON_NOTIFICATION);
				textMailSb.append("} ");
				textMailSb.append(linkToDossier);
				textMailSb.append("</body></html>");
				final String texteMail = textMailSb.toString();
				stMailService.sendTemplateHtmlMailToUserList(session, Collections.singletonList(userToMail), objetMail,
						texteMail, variablesMap);
			}
		}
	}

	/**
	 * Renvoie vrai si l'étape qui a été validé l'a été par un poste du SGG.
	 * 
	 * @param session
	 *            session
	 * @param routeStep
	 *            routeStep
	 * @return vrai si l'étape qui a été validé l'a été par un poste du SGG.
	 * @throws ClientException
	 */
	protected boolean isRetourSgg(final CoreSession session, final STRouteStep routeStep) throws ClientException {
		// on récupère l'étape précédente
		final Filter routeStepFilter = new RouteStepFilter();
		final DocumentModel previousStep = SolonEpgServiceLocator.getFeuilleRouteService().findPreviousStepInFolder(
				session, routeStep.getDocument(), routeStepFilter, false);
		if (previousStep == null) {
			return false;
		}
		final STRouteStep previousStepDoc = previousStep.getAdapter(STRouteStep.class);

		// on récupère le poste des utilisateurs
		final String stepMailboxId = previousStepDoc.getDistributionMailboxId();
		if (StringUtils.isEmpty(stepMailboxId)) {
			return false;
		}
		final String mailboxPosteId = SSServiceLocator.getMailboxPosteService().getPosteIdFromMailboxId(stepMailboxId);
		if (StringUtils.isEmpty(mailboxPosteId)) {
			return false;
		}

		// on récupère les ministeres parent du poste
		final List<EntiteNode> ministereParentList = STServiceLocator.getSTMinisteresService()
				.getMinistereParentFromPoste(mailboxPosteId);
		if (ministereParentList != null && !ministereParentList.isEmpty()) {
			// on récupère le ministère du SGG (Premier Ministre)
			final TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
			for (final EntiteNode entiteNode : ministereParentList) {
				final String idMinistere = tableReferenceService.getMinisterePrm(session);
				if (entiteNode.getId().equals(idMinistere)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param userList
	 * @param userName
	 * @param currentUser
	 * @return
	 */
	protected STUser getCurrentUser(final List<STUser> userList, final String userName, STUser currentUser) {
		if (currentUser == null) {
			for (final STUser stUser : userList) {
				if (userName.equals(stUser.getUsername())) {
					currentUser = stUser;
				}
			}
		}
		return currentUser;
	}

	@Override
	public List<String> getAllowedDossierColumn(final CoreSession session) throws ClientException {
		final ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator
				.getParametreApplicationService();
		final List<String> allowedEspaceTraitementColumn = parametreApplicationService
				.getAvailablesColumnsIds(parametreApplicationService.getParametreApplicationDocument(session)
						.getDocument());
		// on enlève les colonnes déjà affichées
		allowedEspaceTraitementColumn.removeAll(getProfilUtilisateurIdsDisplayedColumnsEspaceTraitement(session));
		return allowedEspaceTraitementColumn;
	}

	@Override
	public List<String> getAllowedFeuilleRouteInstanceColumn(final CoreSession session) throws ClientException {
		final List<String> allowedFeuilleRouteInstanceColumn = new ArrayList<String>();
		allowedFeuilleRouteInstanceColumn
				.addAll(SolonEpgProfilUtilisateurConstants.PARAMETRAGE_APPLICATION_DEFAULT_FEUILLE_ROUTE_COLUMN_LIST);
		// on enlève les colonnes déjà affichées
		allowedFeuilleRouteInstanceColumn.removeAll(getUserFeuilleRouteInstanceColumn(session));
		return allowedFeuilleRouteInstanceColumn;
	}

	/**
	 * Récupère les colonnes du profil utilisateur destinées à être affichées.
	 * 
	 * @param session
	 */
	protected List<String> getProfilUtilisateurIdsDisplayedColumnsEspaceTraitement(final CoreSession session)
			throws ClientException {
		final DocumentModel userProfil = getOrCreateCurrentUserProfil(session);
		return userProfil.getAdapter(ProfilUtilisateur.class).getIdsDisplayedColumnsEspaceTraitement();
	}

	@Override
	public List<String> getUserEspaceTraitementColumn(final CoreSession session) throws ClientException {
		// on récupère les identifiants des colonnes stockée pour ce profil utilisateur
		final List<String> idsProfilUList = getProfilUtilisateurIdsDisplayedColumnsEspaceTraitement(session);

		// on masque les colonnes qui ne sont pas présentes dans la liste
		// note : optimisation passer en paramètre le ParametrageApplication DocumentModel
		final ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator
				.getParametreApplicationService();
		final List<String> listIdAutoriseAffichage = parametreApplicationService.getParametreApplicationDocument(
				session).getMetadonneDisponibleColonneCorbeille();
		if (listIdAutoriseAffichage == null || listIdAutoriseAffichage.isEmpty() || idsProfilUList == null
				|| idsProfilUList.isEmpty()) {
			return null;
		}

		// on vérifie l'affichage de chaque colonne du profil utilisateur
		final List<String> displayedColumnId = new ArrayList<String>();
		for (final String idColonneProfilU : idsProfilUList) {
			if (listIdAutoriseAffichage.contains(idColonneProfilU)) {
				displayedColumnId.add(idColonneProfilU);
			}
		}
		return displayedColumnId;
	}

	@Override
	public List<String> getUserFeuilleRouteInstanceColumn(final CoreSession session) throws ClientException {
		final DocumentModel userProfil = getOrCreateCurrentUserProfil(session);
		return userProfil.getAdapter(ProfilUtilisateur.class).getIdsDisplayedColumnsInstanceFeuilleRoute();
	}

	@Override
	public DocumentModel initDefaultAvailablesColumnsNames(final DocumentModel profilUtilisateur,
			final CoreSession session) throws ClientException {
		// on définit la liste des colonnes affichés par défaut dans les instances de feuille de route
		profilUtilisateur.setProperty(SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
				SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_ID_COLONNE_INSTANCE_FEUILLE_ROUTE_LIST_PROPERTY,
				SolonEpgProfilUtilisateurConstants.PARAMETRAGE_APPLICATION_DEFAULT_FEUILLE_ROUTE_COLUMN_LIST);

		// on récupère la liste des colonnes affichées par défaut dans l'espace de traitement
		List<String> idcolumnEspaceTraitementList = new ArrayList<String>();
		idcolumnEspaceTraitementList
				.addAll(SolonEpgProfilUtilisateurConstants.PARAMETRAGE_APPLICATION_DEFAULT_ESPACE_TRAITEMENT_COLUMN_LIST);

		// on récupère la liste des colonnes disponibles
		final ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator
				.getParametreApplicationService();
		final List<String> idcolumnDisponibleEspaceTraitementList = parametreApplicationService
				.getAvailablesColumnsIds(parametreApplicationService.getParametreApplicationDocument(session)
						.getDocument());

		// si les colonnes affichées par défaut ne sont pas disponible, on les enlève de la liste des colonnes affichées
		if (idcolumnDisponibleEspaceTraitementList != null && !idcolumnDisponibleEspaceTraitementList.isEmpty()) {
			final List<String> colonnesAEnlever = new ArrayList<String>();
			colonnesAEnlever.addAll(idcolumnEspaceTraitementList);
			colonnesAEnlever.removeAll(idcolumnDisponibleEspaceTraitementList);
			idcolumnEspaceTraitementList.removeAll(colonnesAEnlever);
		} else {
			idcolumnEspaceTraitementList = null;
		}

		// on definit la liste des colonnes affichées par défaut dans l'espace de traitement
		profilUtilisateur.setProperty(SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
				SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_ID_COLONNE_ESPACE_TRAITEMENT_LIST_PROPERTY,
				idcolumnEspaceTraitementList);

		return profilUtilisateur;
	}

	@Override
	public DocumentModel initDefaultvalues(final DocumentModel profilUtilisateur, final CoreSession session)
			throws ClientException {

		profilUtilisateur.setProperty(SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA,
				SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_NB_DOSSIERS_VISIBLES_MAX_PROPERTY,
				SolonEpgProfilUtilisateurConstants.PARAMETRAGE_APPLICATION_DEFAULT_NB_DOSSIERS_VISIBLES_MAX);

		return profilUtilisateur;
	}

	@Override
	public List<VocabularyEntry> getVocEntryAllowedEspaceTraitementColumn(final CoreSession session)
			throws ClientException {
		final List<String> idsList = getAllowedDossierColumn(session);
		final SolonEpgVocabularyService solonEpgVocabularyService = SolonEpgServiceLocator
				.getSolonEpgVocabularyService();
		return solonEpgVocabularyService.getVocabularyEntryListFromDirectory(STVocabularyConstants.BORDEREAU_LABEL,
				null, idsList);
	}

	@Override
	public List<VocabularyEntry> getVocEntryAllowedFeuilleRouteInstanceColumn(final CoreSession session)
			throws ClientException {
		final List<String> idsList = getAllowedFeuilleRouteInstanceColumn(session);
		final SolonEpgVocabularyService solonEpgVocabularyService = SolonEpgServiceLocator
				.getSolonEpgVocabularyService();
		return solonEpgVocabularyService.getVocabularyEntryListFromDirectory(VocabularyConstants.FDR_COLUMN, null,
				idsList);
	}

	@Override
	public List<VocabularyEntry> getVocEntryUserEspaceTraitementColumn(final CoreSession session)
			throws ClientException {
		// on récupère les identifiants des colonnes à afficher pour ce profil utilisateur
		final SolonEpgVocabularyService solonEpgVocabularyService = SolonEpgServiceLocator
				.getSolonEpgVocabularyService();
		return solonEpgVocabularyService.getVocabularyEntryListFromDirectory(STVocabularyConstants.BORDEREAU_LABEL,
				null, getUserEspaceTraitementColumn(session));
	}

	@Override
	public List<VocabularyEntry> getVocEntryUserFeuilleRouteInstanceColumn(final CoreSession session)
			throws ClientException {
		final List<String> idsList = getUserFeuilleRouteInstanceColumn(session);
		final SolonEpgVocabularyService solonEpgVocabularyService = SolonEpgServiceLocator
				.getSolonEpgVocabularyService();
		return solonEpgVocabularyService.getVocabularyEntryListFromDirectory(VocabularyConstants.FDR_COLUMN, null,
				idsList);
	}

	@Override
	protected String getReminderMDPQuery(String nxqlDateInf, String nxqlDateSup) {
		StringBuilder query = new StringBuilder("SELECT * FROM ")
				.append(STProfilUtilisateurConstants.PROFIL_UTILISATEUR_DOCUMENT_TYPE).append(" WHERE ")
				.append("(pusr:dernierChangementMotDePasse >= DATE '").append(nxqlDateInf).append("')")
				.append(" AND (pusr:dernierChangementMotDePasse <= DATE '").append(nxqlDateSup).append("')");
		return query.toString();
	}

	protected DocumentModel getProfilUtilisateurDocFromWorkspace(CoreSession session, DocumentModel userWorkspaceDoc)
			throws ClientException {
		if (userWorkspaceDoc != null) {
			final String userWorkspacePath = userWorkspaceDoc.getPathAsString();
			final PathRef profilUtilisateurRef = new PathRef(userWorkspacePath + "/"
					+ STProfilUtilisateurConstants.PROFIL_UTILISATEUR_PATH);
			return session.getDocument(profilUtilisateurRef);
		}
		return null;
	}

	@Override
	public void addDossierToListDerniersDossierIntervention(final CoreSession session, final String idDossier) {
		try {
			final DocumentModel userProfilDoc = getOrCreateCurrentUserProfil(session);
			ProfilUtilisateur userProfil = userProfilDoc.getAdapter(ProfilUtilisateur.class);
			String dossiersIds = userProfil.getDerniersDossiersIntervention();

			if (StringUtil.isBlank(dossiersIds)) {
				// la liste est pour l'instant vide donc on rajoute notre dossier courant
				userProfil.setDerniersDossiersIntervention(idDossier);
			} else {
				// Convertir la liste d'ids concaténés en list
				String[] dossiersIdsArray = dossiersIds.split(IDS_DOSSIERS_SEP);
				// Nouvelle liste pour gérer les ajouts
				List<String> dossiersIdsList = new ArrayList<String>(Arrays.asList(dossiersIdsArray));
				int index = dossiersIdsList.indexOf(idDossier);
				if (index >= 0) {
					// L'index est différent de -1, on a déjà l'élément dans la liste, on le remet au début
					dossiersIdsList.add(0, dossiersIdsList.remove(index));
				} else {
					// index à -1, on ajoute simplement l'élément en début de liste
					dossiersIdsList.add(0, idDossier);
				}

				// On vérifie la taille maximum de la liste
				if (dossiersIdsList.size() >= NB_DOSSIERS_MAX + 1) {
					dossiersIdsList.remove(NB_DOSSIERS_MAX);
				}
				userProfil.setDerniersDossiersIntervention(StringUtil.join(dossiersIdsList, IDS_DOSSIERS_SEP, ""));
			}
			session.saveDocument(userProfil.getDocument());
		} catch (ClientException e) {
			// là on logge que ça s'est mal passé, et c'est tout
			LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC,
					"Erreur lors de la récupération des derniers dossiers sur lesquels l'utilisateur est intervenu", e);
		}
	}

	@Override
	public Long getUtilisateurPageSize(CoreSession session) throws ClientException {
		final DocumentModel userProfil = getOrCreateCurrentUserProfil(session);
		Integer nbDossierPage = userProfil.getAdapter(ProfilUtilisateur.class).getNbDossiersVisiblesMax();

		if (nbDossierPage != null) {
			return nbDossierPage.longValue();
		} else {
			return Long
					.valueOf(SolonEpgProfilUtilisateurConstants.PARAMETRAGE_APPLICATION_DEFAULT_NB_DOSSIERS_VISIBLES_MAX);
		}
	}
}
