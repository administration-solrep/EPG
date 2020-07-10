package fr.dila.solonepg.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.platform.userworkspace.api.UserWorkspaceService;
import org.nuxeo.runtime.api.Framework;

import fr.dila.ecm.platform.routing.api.DocumentRouteElement.ElementLifeCycleState;
import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.criteria.FeuilleRouteCriteria;
import fr.dila.solonepg.api.recherche.FavorisConsultation;
import fr.dila.solonepg.api.recherche.FavorisRecherche;
import fr.dila.solonepg.api.recherche.ResultatConsulte;
import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;

/**
 * Implementation service pour les resultats consultes
 * 
 * @author asatre
 * 
 */
public class EspaceRechercheServiceImpl implements EspaceRechercheService {

	private static final Log	LOG								= LogFactory.getLog(EspaceRechercheServiceImpl.class);
	private static final long	serialVersionUID				= 4028504311494664171L;

	/**
	 * Contenu du paramètre d'activation de la page de recherche libre.
	 */
	private static String activationRechercheLibre;

	/**
	 * PARAM : creator
	 */
	private static final String	UFNXQL_QUERY_RESULTAT_CONSULTE	= "SELECT rc.ecm:uuid as id FROM ResultatConsulte as rc"
																		+ " WHERE rc.dc:creator = ? ";

	@Override
	public DocumentModel createBareFavorisRecherche(final CoreSession session, final String type)
			throws ClientException {
		final DocumentModel favoriRechercheDoc = session
				.createDocumentModel(SolonEpgConstant.FAVORIS_RECHERCHE_DOCUMENT_TYPE);
		final FavorisRecherche favorisRecherche = favoriRechercheDoc.getAdapter(FavorisRecherche.class);

		favorisRecherche.setType(type);

		return favoriRechercheDoc;
	}

	@Override
	public void addDocumentModelToResultatsConsultes(final CoreSession session, final String userworkspacePath,
			final String documentIdToAdd, final DocKind kind) throws ClientException {
		if (kind == null || documentIdToAdd == null) {
			return;
		}

		final ResultatConsulte resultatConsulte = getResultatConsulte(session, userworkspacePath);
		final int nbDerniersResultatsConsultes = getNbDerniersResultatsConsultes(session);

		switch (kind) {
			case DOSSIER:
				resultatConsulte.addNewDossier(documentIdToAdd, nbDerniersResultatsConsultes);
				break;

			case FEUILLE_ROUTE:
				resultatConsulte.addNewFdr(documentIdToAdd, nbDerniersResultatsConsultes);
				break;

			case USER:
				resultatConsulte.addNewUser(documentIdToAdd, nbDerniersResultatsConsultes);
				break;

			default:
				LOG.warn("kind not supported : " + kind);
				break;
		}

		resultatConsulte.save(session);
		session.save();
	}

	/**
	 * Recherche le document résultat consultés de l'utilisateur. Le crée si besoin
	 * 
	 * @param session
	 *            Session
	 * @param userworkspacePath
	 *            Workspace de l'utilisateur
	 * @return Document résultat consultés de l'utilisateur
	 * @throws ClientException
	 */
	private ResultatConsulte getResultatConsulte(final CoreSession session, final String userworkspacePath)
			throws ClientException {

		final List<DocumentModel> resultatConsulteDocList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				"ResultatConsulte", UFNXQL_QUERY_RESULTAT_CONSULTE, new Object[] { session.getPrincipal().getName() });

		DocumentModel resultatConsulte = null;
		if (resultatConsulteDocList == null || resultatConsulteDocList.isEmpty()) {
			resultatConsulte = session
					.createDocumentModel(SolonEpgEspaceRechercheConstants.RESULTAT_CONSULTE_DOCUMENT_TYPE);
			resultatConsulte.setPathInfo(userworkspacePath + SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_PATH,
					SolonEpgEspaceRechercheConstants.RESULTAT_CONSULTE_TITLE);
			resultatConsulte = session.createDocument(resultatConsulte);
		} else {
			resultatConsulte = resultatConsulteDocList.get(0);
		}
		return resultatConsulte.getAdapter(ResultatConsulte.class);
	}

	private int getNbDerniersResultatsConsultes(final CoreSession session) throws ClientException {
		final ParametrageApplication param = getParametrageApplication(session);
		final int nbDerniersResultatsConsultes = param != null && param.getNbDerniersResultatsConsultes() != null ? param
				.getNbDerniersResultatsConsultes().intValue() : 30;
		return nbDerniersResultatsConsultes;
	}

	@Override
	public int addToFavorisConsultation(final CoreSession session, final String userworkspacePath,
			final List<DocumentModel> documentToAddList) throws ClientException {
		if (documentToAddList == null || documentToAddList.isEmpty()) {
			return 0;
		}

		final DocumentModel favorisConsultationDoc = getFavorisConsultation(userworkspacePath, session);
		final FavorisConsultation favorisConsultation = favorisConsultationDoc.getAdapter(FavorisConsultation.class);
		final DocumentModel documentModel = documentToAddList.get(0);
		List<String> currentDocumentIdList = null;
		List<String> newDocumentIdList = null;
		if (documentModel.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
			currentDocumentIdList = favorisConsultation.getDossiersId();
			newDocumentIdList = addDocumentToFavorisConsultation(session, currentDocumentIdList, documentToAddList, false);
			favorisConsultation.setDossiersId(newDocumentIdList);
		} else if (documentModel.hasSchema(STSchemaConstant.FEUILLE_ROUTE_SCHEMA)) {
			currentDocumentIdList = favorisConsultation.getFdrsId();
			newDocumentIdList = addDocumentToFavorisConsultation(session, currentDocumentIdList, documentToAddList, false);
			favorisConsultation.setFdrsId(newDocumentIdList);
		} else if (documentModel.hasSchema(STSchemaConstant.USER_SCHEMA)) {
			currentDocumentIdList = favorisConsultation.getUsers();
			newDocumentIdList = addDocumentToFavorisConsultation(session, currentDocumentIdList, documentToAddList, true);
			favorisConsultation.setUsers(newDocumentIdList);
		} else {
			throw new ClientException("Type de document inconnu : " + documentModel.getType());
		}
		favorisConsultation.save(session);

		return newDocumentIdList.size() - currentDocumentIdList.size();

	}

	private DocumentModel getFavorisConsultation(final String userworkspacePath, final CoreSession session)
			throws ClientException {

		final StringBuilder builder = new StringBuilder();
		builder.append(" SELECT fv.ecm:uuid as id FROM FavorisConsultation as fv");
		builder.append(" WHERE fv.dc:creator = ? ");

		final Object params[] = new Object[] { session.getPrincipal().getName() };

		final List<DocumentModel> docList = QueryUtils.doUnrestrictedUFNXQLQueryAndFetchForDocuments(session,
				"FavorisConsultation", builder.toString(), params);

		DocumentModel doc = null;
		if (docList == null || docList.isEmpty()) {
			doc = session.createDocumentModel(SolonEpgEspaceRechercheConstants.FAVORIS_CONSULTATION_DOCUMENT_TYPE);
			doc.setPathInfo(userworkspacePath + SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_PATH,
					SolonEpgEspaceRechercheConstants.FAVORIS_CONSULTATION_TITLE);
			doc = session.createDocument(doc);
		} else {
			doc = docList.get(0);
		}
		return doc;

	}

	/**
	 * Ajoute un ensemble de documents aux favoris de consultation.
	 * 
	 * @param session
	 *            Session
	 * @param currentDocumentIdList
	 *            Liste des UUID des favoris de consultation actuels
	 * @param documentModelList
	 *            Liste des documents à ajouter aux favoris de consultation
	 * @return Liste des UUID des favoris de consultation après modification
	 * @throws ClientException
	 */
	private List<String> addDocumentToFavorisConsultation(final CoreSession session,
			List<String> currentDocumentIdList, final List<DocumentModel> documentModelList, boolean checkUser) throws ClientException {

		final ParametrageApplication param = getParametrageApplication(session);

		if (currentDocumentIdList == null) {
			currentDocumentIdList = new ArrayList<String>();
		}

		final int nbFavorisConsultation = param != null && param.getNbFavorisConsultation() != null ? param
				.getNbFavorisConsultation().intValue() : 30;

		if (currentDocumentIdList.size() >= nbFavorisConsultation) {
			return currentDocumentIdList;
		}
		
		final Set<String> actualSet = new LinkedHashSet<String>(currentDocumentIdList);
		final Calendar today = Calendar.getInstance();
		for (final DocumentModel documentModel : documentModelList) {
			if(checkUser){
            	STUser user = documentModel.getAdapter(STUser.class);
				if (user.getDateFin() != null && user.getDateFin().getTime() != null && user.getDateFin().getTime().compareTo(today.getTime()) < 0){
					continue;
				} 
				List<String> userPostes = user.getPostes();
				if (userPostes == null || userPostes.isEmpty()){
					continue;
				}
            }

			actualSet.add(documentModel.getId());
			if (actualSet.size() == nbFavorisConsultation) {
				break;
			}
		}
		return new ArrayList<String>(actualSet);
	}

	private ParametrageApplication getParametrageApplication(final CoreSession session) throws ClientException {
		final ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator
				.getParametreApplicationService();
		return parametreApplicationService.getParametreApplicationDocument(session);
	}

	@Override
	public void removeFromFavorisConsultation(final CoreSession session, final String userworkspacePath,
			final List<DocumentModel> documentToRemoveList) throws ClientException {
		if (documentToRemoveList == null || documentToRemoveList.isEmpty()) {
			return;
		}

		final DocumentModel favorisConsultationDoc = getFavorisConsultation(userworkspacePath, session);
		final FavorisConsultation favorisConsultation = favorisConsultationDoc.getAdapter(FavorisConsultation.class);
		final DocumentModel documenToRemove = documentToRemoveList.get(0);
		if (documenToRemove.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
			final List<String> currentDocumentIdList = favorisConsultation.getDossiersId();
			final List<String> newDocumentIdList = removeDocumentFromFavorisConsultation(currentDocumentIdList,
					documentToRemoveList);
			favorisConsultation.setDossiersId(newDocumentIdList);
		} else if (documenToRemove.hasSchema(STSchemaConstant.FEUILLE_ROUTE_SCHEMA)) {
			final List<String> currentDocumentIdList = favorisConsultation.getFdrsId();
			final List<String> newDocumentIdList = removeDocumentFromFavorisConsultation(currentDocumentIdList,
					documentToRemoveList);
			favorisConsultation.setFdrsId(newDocumentIdList);
		} else if (documenToRemove.hasSchema(STSchemaConstant.USER_SCHEMA)) {
			final List<String> currentDocumentIdList = favorisConsultation.getUsers();
			final List<String> newDocumentIdList = removeDocumentFromFavorisConsultation(currentDocumentIdList,
					documentToRemoveList);
			favorisConsultation.setUsers(newDocumentIdList);
		}
		session.saveDocument(favorisConsultationDoc);
		session.save();
	}

	/**
	 * Retire une liste de documents des favoris de consultation.
	 * 
	 * @param currentDocumentIdList
	 *            Identifiant technique des documents présents dans le favori de consultation
	 * @param documentToRemoveList
	 *            Documents à retirer de la liste
	 * @return Liste des identifiants techniques des favoris de consultation après le traitement
	 * @throws ClientException
	 */
	private List<String> removeDocumentFromFavorisConsultation(List<String> currentDocumentIdList,
			final List<DocumentModel> documentToRemoveList) throws ClientException {
		if (currentDocumentIdList == null) {
			currentDocumentIdList = new ArrayList<String>();
		}

		final Set<String> set = new LinkedHashSet<String>(currentDocumentIdList);
		for (final DocumentModel documentToRemove : documentToRemoveList) {
			// Tente d'enlever le document
			final String documentToRemoveId = documentToRemove.getId();
			set.remove(documentToRemoveId);
		}

		return new ArrayList<String>(set);
	}

	@Override
	public List<DocumentModel> addToFavorisRecherche(final CoreSession session, final List<String> postes,
			final String intitule, final String query, final String type) throws ClientException {
		final DocumentModel favorisRechercheDoc = session
				.createDocumentModel(SolonEpgConstant.FAVORIS_RECHERCHE_DOCUMENT_TYPE);
		final FavorisRecherche favorisRecherche = favorisRechercheDoc.getAdapter(FavorisRecherche.class);
		favorisRecherche.setTitle(intitule);
		favorisRecherche.setQueryPart(query);
		favorisRecherche.setType(type);

		return addToFavorisRecherche(session, postes, favorisRechercheDoc);
	}

	@Override
	public List<DocumentModel> addToFavorisRecherche(final CoreSession session, final List<String> postes,
			final DocumentModel favorisRechercheDoc) throws ClientException {
		final List<DocumentModel> listUserError = new ArrayList<DocumentModel>();
		final Set<String> userDocSet = new HashSet<String>();
		userDocSet.add(session.getPrincipal().getName());

		// en unrestricted pour avoir les droits de creation des documents dans les UserWorkspaces
		new UnrestrictedSessionRunner(session) {
			@Override
			public void run() throws ClientException {
				final UserWorkspaceService userWorkspaceService = Framework.getLocalService(UserWorkspaceService.class);
				final UserManager userManager = STServiceLocator.getUserManager();

				if (postes != null && !postes.isEmpty()) {
					final List<PosteNode> listNode = STServiceLocator.getSTPostesService().getPostesNodes(postes);

					for (final PosteNode posteNode : listNode) {
						userDocSet.addAll(posteNode.getMembers());
					}
				}
				// on ajoute le user courant

				final ParametrageApplication param = getParametrageApplication(session);
				final Long nbFavorisRecherche = param != null ? param.getNbFavorisRecherche() : 30L;

				final StringBuilder domainQuery = new StringBuilder();
				domainQuery.append(" SELECT d.ecm:uuid as id FROM Domain as d ");
				domainQuery.append(" where d.dc:title = ? ");

				final Object params[] = new Object[] { "Case Management" };

				IterableQueryResult docList = null;
				DocumentModel domainModel = null;
				try {
					docList = QueryUtils.doUFNXQLQuery(session, domainQuery.toString(), params);
					final Iterator<Map<String, Serializable>> iterator = docList.iterator();
					while (iterator.hasNext()) {
						final Map<String, Serializable> row = iterator.next();
						domainModel = session.getDocument(new IdRef((String) row.get("id")));
						break;
					}
				} finally {
					if (docList != null) {
						docList.close();
					}
				}

				DocumentModel favoriRechercheUserDoc = null;
				final FavorisRecherche favorisRecherche = favorisRechercheDoc.getAdapter(FavorisRecherche.class);
				for (final String userId : userDocSet) {
					final Long userNbFavorisRecherche = countFavorisRecherche(userId, session);

					final DocumentModel user = userManager.getUserModel(userId);
					if (userNbFavorisRecherche.compareTo(nbFavorisRecherche) > 0) {
						listUserError.add(user);
					} else {
						final DocumentModel userWorkspace = userWorkspaceService.getCurrentUserPersonalWorkspace(
								userId, domainModel);
						final String favorisRecherchePath = userWorkspace.getPathAsString()
								+ SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_PATH;
						if (favoriRechercheUserDoc == null) {
							favorisRechercheDoc.setPathInfo(favorisRecherchePath, favorisRecherche.getTitle());
							favoriRechercheUserDoc = session.createDocument(favorisRechercheDoc);
						} else {
							session.copy(favoriRechercheUserDoc.getRef(), new PathRef(favorisRecherchePath),
									favorisRecherche.getTitle());
						}
					}
				}
			}
		}.runUnrestricted();

		return listUserError;
	}

	private Long countFavorisRecherche(final String idUser, final CoreSession session) throws ClientException {
		final StringBuilder nxQuery = new StringBuilder();
		nxQuery.append(" SELECT fr.ecm:uuid as id FROM FavorisRecherche as fr ");
		nxQuery.append(" WHERE isChildOf(fr.ecm:uuid, ");
		nxQuery.append(" select w.ecm:uuid from Workspace AS w where w.dc:title ='");
		nxQuery.append(idUser);
		nxQuery.append("' ) = 1 ");

		return QueryUtils.doCountQuery(session, FlexibleQueryMaker.KeyCode.UFXNQL.key + nxQuery.toString());
	}

	@Override
	public void removeDossierFromFavorisConsultation(final CoreSession session, final String userworkspacePath)
			throws ClientException {
		final DocumentModel docRC = getFavorisConsultation(userworkspacePath, session);
		final FavorisConsultation favorisConsultation = docRC.getAdapter(FavorisConsultation.class);
		final List<String> dossiers = new LinkedList<String>(favorisConsultation.getDossiersId());

		if (!dossiers.isEmpty()) {
			final StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append("SELECT d.ecm:uuid as id FROM Dossier as d WHERE d.ecm:uuid IN (");
			queryBuilder.append(StringUtil.join(dossiers, ",", "'"));
			queryBuilder.append(") AND (testAcl(d.ecm:uuid)=1) ");

			final List<String> list = QueryUtils.doQueryForIds(session, FlexibleQueryMaker.KeyCode.UFXNQL.key
					+ queryBuilder.toString());

			favorisConsultation.setDossiersId(list);
			favorisConsultation.save(session);
		}
	}

	@Override
	public void removeDossierFromDerniersResultatsConsultes(final CoreSession session, final String userworkspacePath)
			throws ClientException {

		final ResultatConsulte resultatConsulte = getResultatConsulte(session, userworkspacePath);
		final List<String> dossiers = new LinkedList<String>(resultatConsulte.getDossiersId());

		if (!dossiers.isEmpty()) {
			final StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append("SELECT d.ecm:uuid as id FROM Dossier as d WHERE d.ecm:uuid IN (");
			queryBuilder.append(StringUtil.join(dossiers, ",", "'"));
			queryBuilder.append(") AND (testAcl(d.ecm:uuid)=1) ");

			final List<String> foundList = QueryUtils.doQueryForIds(session, FlexibleQueryMaker.KeyCode.UFXNQL.key
					+ queryBuilder.toString());

			// pour garder l'ordre...
			final List<String> dossiersToRemove = new ArrayList<String>();
			for (final String idDossier : dossiers) {
				if (!foundList.contains(idDossier)) {
					dossiersToRemove.add(idDossier);
				}
			}
			resultatConsulte.removeDossiers(dossiersToRemove);
			resultatConsulte.save(session);
		}
	}

	@Override
	public void removeUserFromDerniersResultatsConsultes(final CoreSession coreSession, final String userworkspacePath,
			final Set<String> userToRemove) throws ClientException {

		final ResultatConsulte resultatConsulte = getResultatConsulte(coreSession, userworkspacePath);
		resultatConsulte.removeUsers(userToRemove);
		resultatConsulte.save(coreSession);
	}

	@Override
	public void removeUserFromFavorisConsultations(final CoreSession coreSession, final String userworkspacePath,
			final Set<String> userToRemove) throws ClientException {
		final DocumentModel docRC = getFavorisConsultation(userworkspacePath, coreSession);
		final FavorisConsultation favorisConsultation = docRC.getAdapter(FavorisConsultation.class);
		final List<String> users = favorisConsultation.getUsers();
		for (final String user : userToRemove) {
			users.remove(user);
		}
		favorisConsultation.setUsers(users);
		favorisConsultation.save(coreSession);
	}

	@Override
	public FeuilleRouteCriteria getFeuilleRouteCriteria(final DocumentModel favorisRechercheDoc) throws ClientException {
		final FavorisRecherche favorisRecherche = favorisRechercheDoc.getAdapter(FavorisRecherche.class);

		final FeuilleRouteCriteria criteria = new FeuilleRouteCriteria();
		criteria.setCheckReadPermission(true);
		criteria.setIntitule(favorisRecherche.getFeuilleRouteTitle());
		criteria.setNumero(favorisRecherche.getFeuilleRouteNumero());
		criteria.setTypeActe(favorisRecherche.getFeuilleRouteTypeActe());
		criteria.setMinistere(favorisRecherche.getFeuilleRouteMinistere());
		criteria.setDirection(favorisRecherche.getFeuilleRouteDirection());
		criteria.setCreationUtilisateur(favorisRecherche.getFeuilleRouteCreationUtilisateur());
		criteria.setCreationDateMin(favorisRecherche.getFeuilleRouteCreationDateMin());
		criteria.setCreationDateMax(favorisRecherche.getFeuilleRouteCreationDateMax());
		if (StringUtils.isNotBlank(favorisRecherche.getFeuilleRouteValidee())) {
			if (VocabularyConstants.BOOLEAN_TRUE.equals(favorisRecherche.getFeuilleRouteValidee())) {
				criteria.setCurrentLifeCycleState(ElementLifeCycleState.validated.name());
			} else {
				criteria.setCurrentLifeCycleState(ElementLifeCycleState.draft.name());
			}
		}
		criteria.setRoutingTaskType(favorisRecherche.getRouteStepRoutingTaskType());
		criteria.setDistributionMailboxId(favorisRecherche.getRouteStepDistributionMailboxId());
		criteria.setDeadline(favorisRecherche.getRouteStepEcheanceIndicative());
		if (StringUtils.isNotBlank(favorisRecherche.getRouteStepAutomaticValidation())) {
			if (VocabularyConstants.BOOLEAN_TRUE.equals(favorisRecherche.getRouteStepAutomaticValidation())) {
				criteria.setAutomaticValidation(true);
			} else {
				criteria.setAutomaticValidation(false);
			}
		}
		if (StringUtils.isNotBlank(favorisRecherche.getRouteStepObligatoireSgg())) {
			if (VocabularyConstants.BOOLEAN_TRUE.equals(favorisRecherche.getRouteStepObligatoireSgg())) {
				criteria.setObligatoireSGG(true);
			} else {
				criteria.setObligatoireSGG(false);
			}
		}
		if (StringUtils.isNotBlank(favorisRecherche.getRouteStepObligatoireMinistere())) {
			if (VocabularyConstants.BOOLEAN_TRUE.equals(favorisRecherche.getRouteStepObligatoireMinistere())) {
				criteria.setObligatoireMinistere(true);
			} else {
				criteria.setObligatoireMinistere(false);
			}
		}

		return criteria;
	}

	@Override
	public boolean canUseRechercheLibre(CoreSession session) throws ClientException {
		if (activationRechercheLibre == null) {
			synchronized (this) {
				ConfigService configService = STServiceLocator.getConfigService();
				activationRechercheLibre = configService
						.getValue(SolonEpgConfigConstant.SOLONEPG_ELASTICSEARCH_ACTIVATION);
			}
		}

		if ("off".equals(activationRechercheLibre)) {
			return false;
		}

		if ("on".equals(activationRechercheLibre)) {
			return true;
		}

		// Le paramètre contient la liste des logins d'utilisateurs utilisés à
		// accéder à la recherche libre
		List<String> logins = Arrays.asList(activationRechercheLibre.split(","));
		String currentLogin = ((NuxeoPrincipal) session.getPrincipal()).getName();

		return StringUtil.containsIgnoreCase(logins, currentLogin);
	}
}
