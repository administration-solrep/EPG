package fr.dila.solonepg.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;

import fr.dila.solonepg.api.administration.IndexationMotCle;
import fr.dila.solonepg.api.administration.IndexationRubrique;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgIndextionConstants;
import fr.dila.solonepg.api.exception.IndexationException;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.migration.MigrationDetailModel;
import fr.dila.solonepg.api.migration.MigrationDiscriminatorConstants;
import fr.dila.solonepg.api.migration.MigrationLoggerModel;
import fr.dila.solonepg.api.service.ChangementGouvernementService;
import fr.dila.solonepg.api.service.IndexationEpgService;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.organigramme.OrganigrammeService;
import fr.dila.st.api.service.organigramme.STGouvernementService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.UnrestrictedQueryRunner;

public class IndexationEpgServiceImpl implements IndexationEpgService {

	private static final long		serialVersionUID				= -9108441366872553209L;

	public static final String		FIND_ALL_INDEXATION_RUBRIQUE	= "SELECT * FROM IndexationRubrique  WHERE ecm:isProxy = 0 ORDER BY dc:title, ecm:uuid";

	private static final Log		log								= LogFactory.getLog(IndexationEpgServiceImpl.class);
	/**
	 * Logger formalisé en surcouche du logger apache/log4j
	 */
	private static final STLogger	LOGGER							= STLogFactory
																			.getLog(IndexationEpgServiceImpl.class);

	private enum TypeIndexation {
		RUBRIQUE("IndexationRubrique", "indexation_rubrique_solon_epg"), MOT_CLE("IndexationMotCle",
				"indexation_mot_cle_solon_epg");

		private final String	classRef;
		private final String	schema;

		TypeIndexation(final String classRef, final String schema) {
			this.classRef = classRef;
			this.schema = schema;
		}

		public String getClassRef() {
			return classRef;
		}

		public String getSchema() {
			return schema;
		}
	}

	@Override
	public DocumentModelList findAllIndexationRubrique(final CoreSession session) throws ClientException {
		return new UnrestrictedQueryRunner(session, FIND_ALL_INDEXATION_RUBRIQUE).findAll();
	}

	@Override
	public DocumentModelList findAllIndexationMotCle(final CoreSession session) throws ClientException {

		final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();

		final StringBuilder queryBuilder = new StringBuilder();

		queryBuilder.append("SELECT * FROM IndexationMotCle WHERE  ecm:isProxy = 0 ");
		final SSPrincipal principal = (SSPrincipal) session.getPrincipal();

		if (principal != null && !principal.isMemberOf(SolonEpgBaseFunctionConstant.ADMINISTRATION_BULLETIN_READER)) {

			final Set<String> ministereSet = new HashSet<String>();
			for (final String idMinistere : principal.getMinistereIdSet()) {
				final OrganigrammeNode node = ministeresService.getEntiteNode(idMinistere);
				if (node.isActive()) {
					ministereSet.add(idMinistere);
				}
			}

			// si admin on ne limite pas les ministeres
			queryBuilder.append(" AND ministereIds IN ('");
			queryBuilder.append(StringUtils.join(ministereSet, "','"));
			queryBuilder.append("') ");
		}

		queryBuilder.append(" ORDER BY dc:title, ecm:uuid ");

		return new UnrestrictedQueryRunner(session, queryBuilder.toString()).findAll();
	}

	@Override
	public void createIndexationRubrique(final CoreSession session, final String intitule) throws ClientException {

		checkIndexationCount(session, TypeIndexation.RUBRIQUE, intitule);
		final DocumentModel modelDesired = new DocumentModelImpl(SolonEpgIndextionConstants.INDEXATION_PATH, UUID
				.randomUUID().toString(), SolonEpgIndextionConstants.INDEXATION_RUBRIQUE_DOCUMENT_TYPE);
		DublincoreSchemaUtils.setTitle(modelDesired, intitule.toUpperCase());
		final IndexationRubrique indexationMotCle = modelDesired.getAdapter(IndexationRubrique.class);
		indexationMotCle.setIntitule(intitule);

		session.createDocument(modelDesired);
		session.save();
	}

	private void checkIndexationCount(final CoreSession session, final TypeIndexation type, final String intitule)
			throws ClientException {
		if (intitule == null || intitule.isEmpty()) {
			throw new IndexationException("L'intitulé ne doit pas être vide.");
		}

		final StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT ind.ecm:uuid FROM ");
		queryBuilder.append(type.getClassRef());
		queryBuilder.append(" as ind WHERE ind.");
		queryBuilder.append(type.getSchema());
		queryBuilder.append(":intitule = ? ");

		final Object[] params = new Object[] { intitule };

		try {
			final Long result = QueryUtils.doCountQuery(session,
					FlexibleQueryMaker.KeyCode.UFXNQL.key + queryBuilder.toString(), params);
			if (result > 0) {
				throw new IndexationException("L'intitulé (" + intitule + ") existe déjà.");
			}
		} catch (final ClientException e) {
			throw new IndexationException("L'intitulé (" + intitule + ") n'est pas valide.");
		}

	}

	@Override
	public DocumentModel createIndexationMotCle(final CoreSession session, final String intitule,
			final List<String> ministereIds) throws ClientException {

		String finalIntitule = null;
		if (intitule == null || intitule.isEmpty()) {
			throw new IndexationException("L'intitulé ne peut être vide.");
		}
		if (ministereIds == null || ministereIds.isEmpty()) {
			throw new IndexationException("Vous devez choisir un ministère.");
		} else if (ministereIds.size() == 1) {
			final String idMinistere = ministereIds.get(0);
			final OrganigrammeNode node = STServiceLocator.getSTMinisteresService().getEntiteNode(idMinistere);
			if (node instanceof EntiteNode) {
				finalIntitule = ((EntiteNode) node).getNorMinistere() + " - " + intitule;
			} else {
				throw new IndexationException("Ce ministère est inconnu.");
			}
		} else {
			finalIntitule = "Tous - " + intitule;
		}

		checkIndexationCount(session, TypeIndexation.MOT_CLE, finalIntitule);

		final DocumentModel modelDesired = new DocumentModelImpl(SolonEpgIndextionConstants.INDEXATION_PATH, UUID
				.randomUUID().toString(), SolonEpgIndextionConstants.INDEXATION_MOT_CLE_DOCUMENT_TYPE);
		DublincoreSchemaUtils.setTitle(modelDesired, finalIntitule.toUpperCase());
		final IndexationMotCle indexationMotCle = modelDesired.getAdapter(IndexationMotCle.class);
		indexationMotCle.setIntitule(finalIntitule);
		indexationMotCle.setMinistereIds(ministereIds);

		session.createDocument(modelDesired);
		session.save();

		return indexationMotCle.getDocument();
	}

	@Override
	public void updateIndexationMotCle(final CoreSession session, final IndexationMotCle indexationMotCle)
			throws ClientException {
		updateIndexation(session, indexationMotCle.getDocument());
	}

	@Override
	public void updateIndexationRubrique(final CoreSession session, final IndexationRubrique indexationRubrique)
			throws ClientException {
		updateIndexation(session, indexationRubrique.getDocument());
	}

	@Override
	public void deleteIndexationMotCle(final CoreSession session, final IndexationMotCle indexationMotCle)
			throws ClientException {
		removeIndexation(session, indexationMotCle.getDocument());
	}

	@Override
	public void deleteIndexationRubrique(final CoreSession session, final IndexationRubrique indexationRubrique)
			throws ClientException {
		removeIndexation(session, indexationRubrique.getDocument());
	}

	private void updateIndexation(final CoreSession session, final DocumentModel doc) throws ClientException {
		session.saveDocument(doc);
		session.save();
	}

	private void removeIndexation(final CoreSession session, final DocumentModel doc) throws ClientException {
		LOGGER.info(session, EpgLogEnumImpl.DEL_INDEXATION_TEC, doc);
		session.removeDocument(doc.getRef());
		session.save();
	}

	@Override
	public List<EntiteNode> findAllMinistereForIndexation(final CoreSession session) throws ClientException {

		final SSPrincipal principal = (SSPrincipal) session.getPrincipal();
		final List<EntiteNode> nodes = new ArrayList<EntiteNode>();
		final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
		final STGouvernementService gouvService = STServiceLocator.getSTGouvernementService();
		final OrganigrammeService organigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();

		if (!principal.isMemberOf(SolonEpgBaseFunctionConstant.ADMINISTRATION_INDEXATION_UPDATER)) {
			for (final String idMinistere : principal.getMinistereIdSet()) {
				final OrganigrammeNode node = ministeresService.getEntiteNode(idMinistere);
				if (node instanceof EntiteNode) {
					nodes.add((EntiteNode) node);
				}
			}
		} else {
			OrganigrammeNode currentGouv = gouvService.getCurrentGouvernement();
			List<OrganigrammeNode> currentGouvChildren = organigrammeService.getChildrenList(session, currentGouv,
					Boolean.TRUE);

			for (final OrganigrammeNode node : currentGouvChildren) {
				if (node instanceof EntiteNode) {
					nodes.add((EntiteNode) node);
				}
			}
		}
		return nodes;

	}

	@Override
	public List<String> findAllIndexationMotCleForDossier(final CoreSession session, final DocumentModel dossierDoc,
			final String indexation) throws ClientException {

		final StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT imc.indexation_mot_cle_solon_epg:mots_cles as mc FROM IndexationMotCle as imc");
		queryBuilder.append(" WHERE (imc.indexation_mot_cle_solon_epg:intitule LIKE ?");
		queryBuilder.append(" OR imc.indexation_mot_cle_solon_epg:intitule LIKE 'Tous - %')");
		queryBuilder.append(" ORDER BY imc.indexation_mot_cle_solon_epg:mots_cles");

		final Set<String> set = new HashSet<String>();

		if (dossierDoc != null) {
			// TODO récupérer le NOR du ministère de rattachement et pas du dossier !
			String title = dossierDoc.getTitle();

			if (title != null && title.length() > 3) {
				title = title.substring(0, 3);
			}
			final Object[] params = new Object[] { title + " - %" };

			IterableQueryResult res = null;
			try {
				res = QueryUtils.doUFNXQLQuery(session, queryBuilder.toString(), params);

				String mcStr = null;
				for (final Map<String, Serializable> result : res) {
					mcStr = (String) result.get("mc");
					if (result.get("mc") != null && !((String) result.get("mc")).isEmpty()) {
						set.add(mcStr);
					}
				}
			} finally {
				if (res != null) {
					res.close();
				}
			}
		}

		return new ArrayList<String>(set);

	}

	@Override
	public List<String> findAllIndexationRubrique(final CoreSession session, final String indexation)
			throws ClientException {

		final StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT ir.indexation_rubrique_solon_epg:intitule as mri FROM IndexationRubrique as ir");
		queryBuilder.append(" ORDER BY ir.indexation_rubrique_solon_epg:intitule");

		final Object[] params = new Object[] {};

		IterableQueryResult res = null;
		final Set<String> set = new HashSet<String>();
		try {
			res = QueryUtils.doUFNXQLQuery(session, queryBuilder.toString(), params);

			String mcStr = null;
			for (final Map<String, Serializable> result : res) {
				mcStr = (String) result.get("mri");
				if (result.get("mri") != null && !((String) result.get("mri")).isEmpty()) {
					set.add(mcStr);
				}
			}
		} finally {
			if (res != null) {
				res.close();
			}
		}

		return new ArrayList<String>(set);
	}

	@Override
	public void updateIndexationMotCleAfterChange(final CoreSession session, final IndexationMotCle indexationMotCle)
			throws ClientException {
		checkIndexationCount(session, TypeIndexation.MOT_CLE, indexationMotCle.getIntitule());
		updateIndexation(session, indexationMotCle.getDocument());
	}

	@Override
	public void migrerGestionIndexation(final CoreSession session, final String ancienMinistereId,
			final String nouveauMinistereId, final MigrationLoggerModel migrationLoggerModel) throws ClientException {

		migrationLoggerModel.setMotsCles(0);
		final ChangementGouvernementService chgtGouvService = SolonEpgServiceLocator.getChangementGouvernementService();
		chgtGouvService.flushMigrationLogger(migrationLoggerModel);

		// récupération de toutes les indexations de l'ancien ministère
		final List<DocumentModel> indexationToRename = getIndexationMotCleQueryFromIdMinistere(session,
				ancienMinistereId);
		if (indexationToRename == null) {
			migrationLoggerModel.setMotsClesCount(0);
			migrationLoggerModel.setMotsClesCurrent(0);
			chgtGouvService.flushMigrationLogger(migrationLoggerModel);
			final MigrationDetailModel migrationDetailModel = chgtGouvService.createMigrationDetailFor(
					migrationLoggerModel, MigrationDiscriminatorConstants.MOTSCLES, "Aucun mots-clés trouvé");
			migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
			chgtGouvService.flushMigrationDetail(migrationDetailModel);
		} else {
			final int size = indexationToRename.size();

			log.info(size + " mots clés indexation à mettre à jour.");
			migrationLoggerModel.setMotsClesCount(size);

			int index = 0;
			migrationLoggerModel.setMotsClesCurrent(index);
			chgtGouvService.flushMigrationLogger(migrationLoggerModel);

			for (final DocumentModel documentModel : indexationToRename) {

				// remplace l'ancien id par le nouveau
				final IndexationMotCle indexationMotsCles = documentModel.getAdapter(IndexationMotCle.class);

				final MigrationDetailModel migrationDetailModel = chgtGouvService.createMigrationDetailFor(
						migrationLoggerModel, MigrationDiscriminatorConstants.MOTSCLES, "Migration du mots-clés "
								+ indexationMotsCles.getIntitule());

				final List<String> ministeresIds = indexationMotsCles.getMinistereIds();
				ministeresIds.remove(ancienMinistereId);
				ministeresIds.add(nouveauMinistereId);
				indexationMotsCles.setMinistereIds(ministeresIds);
				indexationMotsCles.save(session);

				migrationLoggerModel.setMotsClesCurrent(++index);
				chgtGouvService.flushMigrationLogger(migrationLoggerModel);

				migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
				chgtGouvService.flushMigrationDetail(migrationDetailModel);
			}
		}

		migrationLoggerModel.setMotsCles(1);
		chgtGouvService.flushMigrationLogger(migrationLoggerModel);
	}

	@Override
	public List<DocumentModel> getIndexationMotCleQueryFromIdMinistere(final CoreSession session,
			final String idMinistere) throws ClientException {
		final List<String> params = new ArrayList<String>();
		params.add(idMinistere);

		final StringBuilder query = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
		query.append(IndexationMotCle.class.getSimpleName());
		query.append(" as d WHERE d.indexation_mot_cle_solon_epg:ministereIds = ? ");

		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, IndexationMotCle.class.getSimpleName(),
				query.toString(), params.toArray());
	}
}
