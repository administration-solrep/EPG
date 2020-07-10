package fr.dila.solonepg.core.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;

import fr.dila.solonepg.api.administration.BulletinOfficiel;
import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.constant.SolonEpgBulletinOfficielConstants;
import fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants;
import fr.dila.solonepg.api.dto.VecteurPublicationDTO;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.migration.MigrationDetailModel;
import fr.dila.solonepg.api.migration.MigrationDiscriminatorConstants;
import fr.dila.solonepg.api.migration.MigrationLoggerModel;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.util.StringUtil;

public class BulletinOfficielServiceImpl implements BulletinOfficielService {

	/**
	 * Serial version UID
	 */
	private static final long		serialVersionUID				= 5029667528412231120L;

	public static final String		GET_ALL_ACTIVE_QUERY			= "SELECT * FROM BulletinOfficiel where boEtat != 'deleted' and ecm:isProxy = 0 ";

	public static final String		GET_ALL_ACTIVE_VECTORS_QUERY	= "SELECT v.ecm:uuid as id FROM VecteurPublication as v where (v.vp:vpDebut IS NULL OR v.vp:vpDebut <= ?) AND (v.vp:vpFin IS NULL OR v.vp:vpFin >= ? )";

	public static final String		GET_VECTORS_QUERY_FROM_POS		= "SELECT v.ecm:uuid as id FROM VecteurPublication as v where v.vp:vpPos = ?";

	public static final String		GET_VECTORS_QUERY_FROM_LABEL	= "SELECT v.ecm:uuid as id FROM VecteurPublication as v where v.vp:vpIntitule LIKE ?";

	private static final STLogger	LOGGER							= STLogFactory
																			.getLog(BulletinOfficielServiceImpl.class);

	@Override
	public DocumentModelList getAllActiveBulletinOfficielOrdered(final CoreSession session) throws ClientException {
		return session.query(GET_ALL_ACTIVE_QUERY);
	}

	@Override
	public void create(final CoreSession session, final String nor, final String intitule) throws ClientException {
		final DocumentModel modelDesired = new DocumentModelImpl(
				SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_PATH, UUID.randomUUID().toString(),
				SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_DOCUMENT_TYPE);
		DublincoreSchemaUtils.setTitle(modelDesired, intitule.toUpperCase());

		final BulletinOfficiel bulletinOfficiel = modelDesired.getAdapter(BulletinOfficiel.class);
		bulletinOfficiel.setNor(nor);
		bulletinOfficiel.setIntitule(intitule);
		bulletinOfficiel.setEtat("running");

		session.createDocument(bulletinOfficiel.getDocument());
		session.save();

	}

	@Override
	public void delete(final CoreSession session, final String identifier) throws ClientException {
		final DocumentModel docModel = session.getDocument(new IdRef(identifier));
		if (docModel != null) {
			final BulletinOfficiel bulletinOfficiel = docModel.getAdapter(BulletinOfficiel.class);
			// desactivation du document
			bulletinOfficiel.setEtat("deleted");
			session.saveDocument(bulletinOfficiel.getDocument());
			session.save();
		}
	}

	@Override
	public List<DocumentModel> getAllBulletinOfficielOrdered(final DocumentModel currentDocument,
			final CoreSession session) throws ClientException {
		if (currentDocument != null && currentDocument.getTitle() != null && currentDocument.getTitle().length() >= 3) {
			return getBulletinOfficielQueryFromNor(session, currentDocument.getTitle().substring(0, 3), Boolean.TRUE);
		} else {
			return new ArrayList<DocumentModel>();
		}
	}

	private List<DocumentModel> getBulletinOfficielQueryFromNor(final CoreSession session, final String ministereNor,
			final Boolean ordered) throws ClientException {
		final List<Object> params = new ArrayList<Object>();

		final StringBuffer queryBuilder = new StringBuffer();
		queryBuilder.append("SELECT b.ecm:uuid as id FROM BulletinOfficiel as b where b.bulletin_officiel:boEtat = ? ");
		params.add("running");

		if (ministereNor != null && ministereNor.length() >= 3) {
			queryBuilder.append("AND b.bulletin_officiel:boNor = ? ");
			params.add(ministereNor);
		} else {
			return new ArrayList<DocumentModel>();
		}

		if (ordered) {
			queryBuilder.append("ORDER BY b.bulletin_officiel:boNor, b.bulletin_officiel:boIntitule");
		}

		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, "BulletinOfficiel", queryBuilder.toString(),
				params.toArray());
	}

	@Override
	public void migrerBulletinOfficiel(final CoreSession session, final String ancienNorMinistere,
			final String nouveauNorMinistere, final MigrationLoggerModel migrationLoggerModel) throws ClientException {

		migrationLoggerModel.setBulletinOfficiel(0);
		SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationLogger(migrationLoggerModel);

		final List<DocumentModel> bulletinOfficielToRename = getBulletinOfficielQueryFromNor(session,
				ancienNorMinistere);

		if (bulletinOfficielToRename == null || bulletinOfficielToRename.isEmpty()) {
			migrationLoggerModel.setBulletinOfficielCount(0);
			migrationLoggerModel.setBulletinOfficielCurrent(0);
			SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationLogger(migrationLoggerModel);
			final MigrationDetailModel migrationDetailModel = SolonEpgServiceLocator.getChangementGouvernementService()
					.createMigrationDetailFor(migrationLoggerModel, MigrationDiscriminatorConstants.BO,
							"Aucun bulletin officiel à migrer");
			migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
			SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationDetail(migrationDetailModel);
		} else {
			LOGGER.info(session, EpgLogEnumImpl.UPDATE_BULLETIN_OFF_TEC, bulletinOfficielToRename.size()
					+ " bulletin officiel à mettre à jour.");

			migrationLoggerModel.setBulletinOfficielCount(bulletinOfficielToRename.size());

			int index = 0;
			migrationLoggerModel.setBulletinOfficielCurrent(index);
			SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationLogger(migrationLoggerModel);

			for (final DocumentModel documentModel : bulletinOfficielToRename) {

				final BulletinOfficiel bulletinOfficiel = documentModel.getAdapter(BulletinOfficiel.class);

				final String nor = bulletinOfficiel.getNor();
				final String intitule = bulletinOfficiel.getIntitule();
				
				final MigrationDetailModel migrationDetailModel = SolonEpgServiceLocator.getChangementGouvernementService().createMigrationDetailFor(
						migrationLoggerModel,
						MigrationDiscriminatorConstants.BO,
						"Migration du bulletin officiel " + nor + " " + intitule + " (Nouveau nor : "
								+ nouveauNorMinistere + ")");

				bulletinOfficiel.setNor(nouveauNorMinistere);
				bulletinOfficiel.save(session);

				migrationLoggerModel.setBulletinOfficielCurrent(++index);
				SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationLogger(migrationLoggerModel);

				migrationDetailModel.setEndDate(Calendar.getInstance().getTime());
				SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationDetail(migrationDetailModel);
			}
		}

		migrationLoggerModel.setBulletinOfficiel(1);
		SolonEpgServiceLocator.getChangementGouvernementService().flushMigrationLogger(migrationLoggerModel);
	}

	@Override
	public List<DocumentModel> getBulletinOfficielQueryFromNor(final CoreSession session, final String ministereNor)
			throws ClientException {
		return getBulletinOfficielQueryFromNor(session, ministereNor, Boolean.FALSE);
	}

	@Override
	public DocumentModel initVecteurPublication(CoreSession session) throws ClientException {
		LOGGER.info(session, EpgLogEnumImpl.INIT_VECTEUR_PUB_TEC);
		DocumentModel modelDesired = session
				.createDocumentModel(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE);
		modelDesired.setPathInfo(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_PATH, UUID.randomUUID()
				.toString());
		VecteurPublication vecteurPublication = modelDesired.getAdapter(VecteurPublication.class);
		vecteurPublication.setIntitule("");
		return modelDesired;
	}

	@Override
	public void saveVecteurPublication(CoreSession session, List<VecteurPublicationDTO> listVecteur)
			throws ClientException {

		for (VecteurPublicationDTO dto : listVecteur) {
			// Récupération des données du DTO
			String intitule = dto.getIntitule();
			Date dateDebut = dto.getDateDebut();
			Date dateFin = dto.getDateFin();
			String id = dto.getId();
			IdRef docRef = new IdRef(id);
			if (id != null && session.exists(docRef)) {
				// Récupération du document model existant
				DocumentModel vecteurPublicationDoc = session.getDocument(docRef);
				VecteurPublication vecteurPublication = vecteurPublicationDoc.getAdapter(VecteurPublication.class);

				// Mise à jour des données dans le document model
				vecteurPublication.setIntitule(intitule);
				if (dateDebut != null) {
					Calendar dateDebutCal = Calendar.getInstance();
					dateDebutCal.setTime(dateDebut);
					vecteurPublication.setDateDebut(dateDebutCal);
				} else {
					vecteurPublication.setDateDebut(null);
				}
				if (dateFin != null) {
					Calendar dateFinCal = Calendar.getInstance();
					dateFinCal.setTime(dateFin);
					vecteurPublication.setDateFin(dateFinCal);
				} else {
					vecteurPublication.setDateFin(null);
				}
				// Enregistrement des modifications
				vecteurPublicationDoc = vecteurPublication.save(session);
				dto.setId(vecteurPublicationDoc.getId());
				LOGGER.info(session, EpgLogEnumImpl.UPDATE_VECTEUR_PUB_TEC, vecteurPublicationDoc);
			} else {
				// On enregistre uniquement si au moins un champ a été renseigné
				if (!StringUtil.isEmpty(intitule) || dateDebut != null || dateFin != null) {
					// Création du documentModel
					DocumentModel modelDesired = session
							.createDocumentModel(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE);
					modelDesired.setPathInfo(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_PATH, UUID
							.randomUUID().toString());
					VecteurPublication vecteurPublication = modelDesired.getAdapter(VecteurPublication.class);
					vecteurPublication.setIntitule(intitule);
					if (dateDebut != null) {
						Calendar dateDebutCal = Calendar.getInstance();
						dateDebutCal.setTime(dateDebut);
						vecteurPublication.setDateDebut(dateDebutCal);
					}
					if (dateFin != null) {
						Calendar dateFinCal = Calendar.getInstance();
						dateFinCal.setTime(dateFin);
						vecteurPublication.setDateFin(dateFinCal);
					}
					// Enregistrement définitive du document model
					modelDesired = session.createDocument(vecteurPublication.getDocument());
					LOGGER.info(session, EpgLogEnumImpl.CREATE_VECTEUR_PUB_TEC, modelDesired);
				}
			}
		}

		// On sauve la session une fois la modification terminée
		session.save();
	}

	@Override
	public List<DocumentModel> getAllVecteurPublication(final CoreSession session) throws ClientException {
		final StringBuffer queryBuilder = new StringBuffer();
		queryBuilder.append("SELECT v.ecm:uuid as id FROM VecteurPublication as v");

		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE, queryBuilder.toString(), null);
	}

	@Override
	public List<DocumentModel> getAllActifVecteurPublication(CoreSession documentManager) throws ClientException {

		final StringBuffer queryBuilder = new StringBuffer();
		queryBuilder.append(GET_ALL_ACTIVE_VECTORS_QUERY);

		// On veut 2 variables qui correspondent à maintenant car la date de début doit être antérieur à maintenant et
		// celle de fin doit être postérieure
		Calendar today = Calendar.getInstance();

		Object[] params = { today, today };

		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(documentManager,
				SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE, queryBuilder.toString(), params);
	}

	@Override
	public boolean isAllVecteurPublicationActif(CoreSession documentManager, List<String> idsVecteur)
			throws ClientException {
		final StringBuilder queryBuilder = new StringBuilder();
		final List<Object> params = new ArrayList<Object>();

		// On cherche les vecteurs dont la période de validité inclut la date courante
		Calendar dateCourante = Calendar.getInstance();

		params.add(dateCourante);
		params.add(dateCourante);
		queryBuilder.append(GET_ALL_ACTIVE_VECTORS_QUERY);
		queryBuilder.append("AND v.ecm:uuid IN (");
		queryBuilder.append(StringUtil.join(idsVecteur, ",", "'"));
		queryBuilder.append(")");

		List<DocumentModel> lstVecteursPublication = QueryUtils.doUFNXQLQueryAndFetchForDocuments(documentManager,
				SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE, queryBuilder.toString(),
				params.toArray());

		return lstVecteursPublication.size() == idsVecteur.size();
	}

	@Override
	public boolean isAllBulletinOfficielActif(CoreSession documentManager, List<String> names) throws ClientException {
		DocumentModelList docModelList = getAllActiveBulletinOfficielOrdered(documentManager);
		List<String> bulletins = new ArrayList<String>(names);
		// On enlève les bulletins de la liste au fur et à mesure qu'on les trouve dans la liste des bulletins actifs
		for (DocumentModel docModel : docModelList) {
			String intitule = docModel.getAdapter(BulletinOfficiel.class).getIntitule();
			bulletins.remove(intitule);
		}
		return bulletins.isEmpty();
	}

	@Override
	public String getLibelleForJO(CoreSession documentManager) throws ClientException {
		return getLibelleFromPos(documentManager, 1);
	}

	private String getLibelleFromPos(CoreSession documentManager, Integer pos) throws ClientException {
		final StringBuffer queryBuilder = new StringBuffer();
		final List<Object> params = new ArrayList<Object>();

		params.add(pos);
		queryBuilder.append(GET_VECTORS_QUERY_FROM_POS);

		List<DocumentModel> lstDoc = QueryUtils.doUFNXQLQueryAndFetchForDocuments(documentManager,
				SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE, queryBuilder.toString(),
				params.toArray());
		if (lstDoc.isEmpty()) {
			return "";
		} else {
			return lstDoc.get(0).getId();
		}
	}

	@Override
	public String getIdForLibelle(CoreSession documentManager, String libelle) throws ClientException {
		final StringBuffer queryBuilder = new StringBuffer();
		final List<Object> params = new ArrayList<Object>();

		params.add(libelle);
		queryBuilder.append(GET_VECTORS_QUERY_FROM_LABEL);

		List<DocumentModel> lstDoc = QueryUtils.doUFNXQLQueryAndFetchForDocuments(documentManager,
				SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DOCUMENT_TYPE, queryBuilder.toString(),
				params.toArray());
		if (lstDoc.isEmpty()) {
			return "";
		} else {
			return lstDoc.get(0).getId();
		}
	}

	@Override
	public List<String> convertVecteurIdListToLabels(CoreSession documentManager, List<String> idVecteurs) {
		List<String> lstVecteursConvertis = new ArrayList<String>();

		if (idVecteurs != null && !idVecteurs.isEmpty()) {
			for (String idVecteur : idVecteurs) {
				try {
					VecteurPublication vecteur = documentManager.getDocument(new IdRef(idVecteur)).getAdapter(
							VecteurPublication.class);
					if (vecteur != null) {
						lstVecteursConvertis.add(vecteur.getIntitule());
					} else {
						lstVecteursConvertis.add(idVecteur);
					}
				} catch (Exception e) {
					lstVecteursConvertis.add(idVecteur);
				}
			}
		}
		return lstVecteursConvertis;
	}

	@Override
	public String convertVecteurIdToLabel(CoreSession documentManager, String idVecteur) {
		if (idVecteur != null) {
			try {
				VecteurPublication vecteur = documentManager.getDocument(new IdRef(idVecteur)).getAdapter(
						VecteurPublication.class);
				if (vecteur != null) {
					return vecteur.getIntitule();
				} else {
					return idVecteur;
				}
			} catch (Exception e) {
				return idVecteur;
			}
		}
		return idVecteur;
	}

}
