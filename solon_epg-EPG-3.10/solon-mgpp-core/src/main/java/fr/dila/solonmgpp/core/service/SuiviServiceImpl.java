package fr.dila.solonmgpp.core.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.query.sql.model.DateLiteral;

import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonmgpp.api.domain.ActiviteParlementaire;
import fr.dila.solonmgpp.api.domain.EcheancierPromulgation;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.domain.SemaineParlementaire;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.SuiviService;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.util.DateUtil;

public class SuiviServiceImpl implements SuiviService {

	/**
	 * Logger formalisé en surcouche du logger apache/log4j
	 */
	private static final STLogger	LOGGER	= STLogFactory.getLog(SuiviServiceImpl.class);

	@Override
	public List<DocumentModel> getTexteLoiEnCoursDiscussionNonAdoptee(final CoreSession session) throws ClientException {
		final StringBuilder queryBuilder = new StringBuilder("SELECT fl.ecm:uuid as id FROM ");
		queryBuilder.append(FicheLoi.DOC_TYPE + " as fl");
		// ABI retiré pour optimisation car le champ est trop rarement renseigné
		// queryBuilder.append(" WHERE fl.");
		// queryBuilder.append(FicheLoi.PREFIX);
		// queryBuilder.append(":");
		// queryBuilder.append(FicheLoi.DATE_RECEPTION);
		// queryBuilder.append(" IS NULL ");
		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, FicheLoi.DOC_TYPE, queryBuilder.toString(),
				new Object[] {});

	}

	@Override
	public List<DocumentModel> getAllEcheancierPromulgation(final CoreSession session) throws ClientException {
		final String query = "SELECT fl.ecm:uuid as id  FROM " + FicheLoi.DOC_TYPE + " as fl WHERE fl."
				+ FicheLoi.PREFIX + ":" + FicheLoi.ECHEANCIER_PROMULGATION + " = 1";
		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, FicheLoi.DOC_TYPE, query, new Object[] {});
	}

	@Override
	public Long getEcheancierPromulgationCount(final CoreSession session) throws ClientException {
		final String query = "SELECT fl.ecm:uuid as id  FROM " + FicheLoi.DOC_TYPE + " as fl WHERE fl."
				+ FicheLoi.PREFIX + ":" + FicheLoi.ECHEANCIER_PROMULGATION + " = 1";
		return QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(query));
	}

	@Override
	public List<DocumentModel> getEcheancierPromulgationPage(final CoreSession session, final int limit,
			final int offset) throws ClientException {
		final String query = "SELECT fl.ecm:uuid as id  FROM " + FicheLoi.DOC_TYPE + " as fl WHERE fl."
				+ FicheLoi.PREFIX + ":" + FicheLoi.ECHEANCIER_PROMULGATION + " = 1";
		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, FicheLoi.DOC_TYPE, query, new Object[] {}, limit,
				offset);
	}

	@Override
	public EcheancierPromulgation creerEcheancierPromulgation(final CoreSession session, final String idDossier)
			throws ClientException {
		final DocumentModel echeancierPromulgationModel = session.createDocumentModel(
				"/case-management/echeancier-promulgation", idDossier + "_AP",
				EcheancierPromulgation.ECHANCIER_PROMULGATION_DOC_TYPE);
		final EcheancierPromulgation echeancierPromulgation = echeancierPromulgationModel
				.getAdapter(EcheancierPromulgation.class);
		echeancierPromulgation.setIdDossier(idDossier);
		session.createDocument(echeancierPromulgation.getDocument());
		session.save();
		return echeancierPromulgation;
	}

	@Override
	public EcheancierPromulgation findOrCreateEcheancierPromulgation(final CoreSession session, final String idDossier)
			throws ClientException {
		synchronized (this) {

			final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id  FROM "
					+ EcheancierPromulgation.ECHANCIER_PROMULGATION_DOC_TYPE + " as d");
			queryBuilder.append(" WHERE d.");
			queryBuilder.append(EcheancierPromulgation.ECHANCIER_PROMULGATION_PREFIX);
			queryBuilder.append(":");
			queryBuilder.append(EcheancierPromulgation.ID_DOSSIER);
			queryBuilder.append(" = ? ");

			final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
					EcheancierPromulgation.ECHANCIER_PROMULGATION_DOC_TYPE, queryBuilder.toString(),
					new Object[] { idDossier });

			if (docs == null || docs.isEmpty()) {
				return creerEcheancierPromulgation(session, idDossier);
			} else if (docs.size() > 1) {
				throw new ClientException("Plusieurs Echeancier de Promulgation trouvées pour le meme dossier "
						+ idDossier + ".");
			} else {
				return docs.get(0).getAdapter(EcheancierPromulgation.class);
			}

		}

	}

	@Override
	public void creerActiviteParlementaire(final CoreSession session, final String idDossier, final String activite,
			final String assemblee, final Date date) throws ClientException {
		final SimpleDateFormat formatter = DateUtil.simpleDateFormat("ddMMyyyy");
		final String dateStr = formatter.format(date);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		DocumentModel activiteParlementaireModel = getActiviteParlementaireByDossierIdDateAct(session, idDossier, cal,
				activite, assemblee);
		if (activiteParlementaireModel == null) {
			activiteParlementaireModel = session.createDocumentModel("/case-management/activite-parlementaire",
					idDossier + "_AP_" + dateStr + "_" + activite + "_" + assemblee,
					ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_DOC_TYPE);
			session.createDocument(activiteParlementaireModel);
		}
		final ActiviteParlementaire activiteParlementaire = activiteParlementaireModel
				.getAdapter(ActiviteParlementaire.class);
		activiteParlementaire.setIdDossier(idDossier);
		activiteParlementaire.setActivite(activite);
		activiteParlementaire.setAssemblee(assemblee);
		activiteParlementaire.setDate(cal);
		session.saveDocument(activiteParlementaire.getDocument());
		session.save();
		LOGGER.info(session, MgppLogEnumImpl.CREATE_ACT_PARLEMENTAIRE_TEC, activiteParlementaire);

	}

	@Override
	public void removeActiviteParlementaire(final CoreSession session, final String activiteId) throws ClientException {
		synchronized (this) {
			final DocumentModel doc = session.getDocument(new IdRef(activiteId));
			if (doc != null) {
				LOGGER.info(session, MgppLogEnumImpl.DEL_ACT_PARLEMENTAIRE_TEC, doc);
				session.removeDocument(doc.getRef());
			}
			session.save();
		}
	}

	@Override
	public void removeSemaineParlementaire(final CoreSession session, final String activiteId) throws ClientException {
		synchronized (this) {
			final DocumentModel doc = session.getDocument(new IdRef(activiteId));
			if (doc != null) {
				LOGGER.info(session, MgppLogEnumImpl.DEL_SEM_PARLEMENTAIRE_TEC, doc);
				session.removeDocument(doc.getRef());
			}
			session.save();
		}
	}

	@Override
	public List<DocumentModel> getAllTexteLoiPourCalendrierParlementaire(final CoreSession session,
			final Date dateDeDebutFiltre, final Date dateDeFinFiltre) throws ClientException {

		List<DocumentModel> list = new ArrayList<DocumentModel>();

		List<String> listeDossierId = getAllDossierIdsForActiviteParlementaire(session, dateDeDebutFiltre,
				dateDeFinFiltre);

		list = getAllFicheLoiByActivitesId(session, listeDossierId);

		return list;
	}

	private List<DocumentModel> getAllFicheLoiByActivitesId(CoreSession session, List<String> listDossierId)
			throws ClientException {
		final StringBuilder queryBuilder = new StringBuilder("SELECT fl.ecm:uuid as id FROM ");
		queryBuilder.append(FicheLoi.DOC_TYPE + " as fl");
		queryBuilder.append(" WHERE fl.");
		queryBuilder.append(FicheLoi.PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(FicheLoi.ID_DOSSIER);
		queryBuilder.append(" IN (");
		for (String id : listDossierId) {
			queryBuilder.append("'").append(id).append("',");
		}
		queryBuilder.append("'vide'").append(")");

		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, FicheLoi.DOC_TYPE, queryBuilder.toString(),
				new Object[] {});
	}

	@Override
	public HashMap<String, String> getTexteLoiEnCoursDiscussionNonAdoptee2(final CoreSession session)
			throws ClientException {
		HashMap<String, String> toReturn = new HashMap<String, String>();
		final String query = "SELECT Distinct fl." + FicheLoi.PREFIX + ":" + FicheLoi.ID_DOSSIER + " as idDossier"
				+ " , " + "fl." + FicheLoi.PREFIX + ":" + FicheLoi.INTITULE + " as intitule" + " FROM "
				+ FicheLoi.DOC_TYPE + " as fl";
		IterableQueryResult res = null;
		try {
			res = QueryUtils.doUFNXQLQuery(session, query, null);
			final Iterator<Map<String, Serializable>> iterator = res.iterator();
			while (iterator.hasNext()) {
				final Map<String, Serializable> map = iterator.next();
				final Serializable idDossier = map.get("idDossier");
				final Serializable intitule = map.get("intitule");
				toReturn.put((String) idDossier, (String) intitule);
			}
		} finally {
			if (res != null) {
				res.close();
			}
		}
		return toReturn;
	}

	@Override
	public List<Calendar> getAllDateActiviteParlementaire(final CoreSession session) throws ClientException {
		final String today = DateLiteral.dateFormatter.print(new Date().getTime());
		final String query = "SELECT Distinct a." + ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX + ":"
				+ ActiviteParlementaire.DATE + " as dateActivite" + " FROM "
				+ ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_DOC_TYPE + " as a" + " WHERE a."
				+ ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX + ":" + ActiviteParlementaire.DATE + " >= DATE '"
				+ today + "'" + " ORDER BY a." + ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX + ":"
				+ ActiviteParlementaire.DATE;
		IterableQueryResult res = null;
		final List<Calendar> dateList = new ArrayList<Calendar>();
		try {
			res = QueryUtils.doUFNXQLQuery(session, query, null);
			final Iterator<Map<String, Serializable>> iterator = res.iterator();
			while (iterator.hasNext()) {
				final Map<String, Serializable> map = iterator.next();
				final Serializable dateActivite = map.get("dateActivite");
				dateList.add((Calendar) dateActivite);
			}
		} finally {
			if (res != null) {
				res.close();
			}
		}
		return dateList;
	}

	@Override
	public List<Calendar> getAllDateActiviteParlementaire(final CoreSession session, final Date startDate,
			final Date endDate) throws ClientException {

		// String today = DateLiteral.dateFormatter.print((new Date()).getTime());
		final StringBuilder whereClause = new StringBuilder();
		String startDt = "";
		String endDt = "";

		if (startDate != null) {
			startDt = DateLiteral.dateFormatter.print(startDate.getTime());
		}

		if (endDate != null) {
			endDt = DateLiteral.dateFormatter.print(endDate.getTime());
		}

		final StringBuilder query = new StringBuilder("SELECT Distinct a.")
				.append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX).append(":")
				.append(ActiviteParlementaire.DATE).append(" as dateActivite").append(" FROM ")
				.append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_DOC_TYPE).append(" as a");

		if (startDate != null) {
			if (endDate != null) {

				whereClause.append(" WHERE a.").append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX).append(":")
						.append(ActiviteParlementaire.DATE).append(" >= DATE '").append(startDt).append("'")
						.append(" AND a.").append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX).append(":")
						.append(ActiviteParlementaire.DATE).append(" <= DATE '").append(endDt).append("'");

			} else {
				whereClause.append(" WHERE a.").append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX).append(":")
						.append(ActiviteParlementaire.DATE).append(" >= DATE '").append(startDt).append("'");
			}

		} else {
			if (endDate != null) {

				whereClause.append(" WHERE a.").append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX).append(":")
						.append(ActiviteParlementaire.DATE).append(" <= DATE '").append(endDt).append("'");
			}

		}

		query.append(whereClause.toString()).append(" ORDER BY a.")
				.append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX).append(":")
				.append(ActiviteParlementaire.DATE);

		IterableQueryResult res = null;
		final List<Calendar> dateList = new ArrayList<Calendar>();
		try {
			res = QueryUtils.doUFNXQLQuery(session, query.toString(), null);
			final Iterator<Map<String, Serializable>> iterator = res.iterator();
			while (iterator.hasNext()) {
				final Map<String, Serializable> map = iterator.next();
				final Serializable dateActivite = map.get("dateActivite");
				dateList.add((Calendar) dateActivite);
			}
		} finally {
			if (res != null) {
				res.close();
			}
		}

		return dateList;
	}

	@Override
	public List<DocumentModel> getAllActiviteParlementaireByDossierId(final CoreSession session, final String DossierId)
			throws ClientException {
		final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
		queryBuilder.append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_DOC_TYPE + " as d");
		queryBuilder.append(" WHERE d.");
		queryBuilder.append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(ActiviteParlementaire.ID_DOSSIER);
		queryBuilder.append(" = '" + DossierId + "' ORDER BY d." + ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX
				+ ":date");
		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_DOC_TYPE, queryBuilder.toString(), new Object[] {});
	}

	@Override
	public List<DocumentModel> getAllSemaineParlementaire(final CoreSession session, final String assemble)
			throws ClientException {
		final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_DOC_TYPE + " as d");
		queryBuilder.append(" WHERE d.");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(SemaineParlementaire.ASSEMBLEE);
		queryBuilder.append(" = '" + assemble + "' ORDER BY d." + SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX
				+ ":dateDebut");
		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SemaineParlementaire.SEMAINE_PARLEMENTAIRE_DOC_TYPE, queryBuilder.toString(), new Object[] {});
	}

	@Override
	public void creerSemaineParlementaire(final CoreSession session, final String orientation, final String assemblee,
			final Calendar dateDebut, final Calendar dateFin) throws ClientException {
		final SimpleDateFormat formatter = DateUtil.simpleDateFormat("ddMMyyyy");
		final String dateDebutStr = formatter.format(dateDebut.getTime());
		final String dateFinStr = formatter.format(dateFin.getTime());
		DocumentModel doc = getAllSemainesAvantDateDebutEtAvantDateFin(session, assemblee, dateDebut, dateFin);
		if (doc != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(dateDebut.getTimeInMillis());
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			final SemaineParlementaire semaineParlementaire = doc.getAdapter(SemaineParlementaire.class);
			semaineParlementaire.setDateFin(calendar);
			session.saveDocument(semaineParlementaire.getDocument());
			creerNouvelleSemaineParlementaire(session, orientation, assemblee, dateDebut, dateFin);
		} else {
			doc = getAllSemainesApresDateDebutEtApresDateFin(session, assemblee, dateDebut, dateFin);
			if (doc != null) {
				final Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(dateDebut.getTimeInMillis());
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				final SemaineParlementaire semaineParlementaire = doc.getAdapter(SemaineParlementaire.class);
				semaineParlementaire.setDateDebut(calendar);
				session.saveDocument(semaineParlementaire.getDocument());
				creerNouvelleSemaineParlementaire(session, orientation, assemblee, dateDebut, dateFin);
			} else {
				doc = getAllSemainesAvantDateDebutEtApresDateFin(session, assemblee, dateDebut, dateFin);
				if (doc != null) {
					final Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(dateDebut.getTimeInMillis());
					calendar.add(Calendar.DAY_OF_YEAR, -1);
					final SemaineParlementaire semaineParlementaire = doc.getAdapter(SemaineParlementaire.class);
					semaineParlementaire.setDateFin(calendar);
					session.saveDocument(semaineParlementaire.getDocument());

					creerNouvelleSemaineParlementaire(session, orientation, assemblee, dateDebut, dateFin);

					final Calendar calendarB = Calendar.getInstance();
					calendarB.setTimeInMillis(dateFin.getTimeInMillis());
					calendarB.add(Calendar.DAY_OF_YEAR, +1);
					creerNouvelleSemaineParlementaire(session, orientation, assemblee, calendarB,
							semaineParlementaire.getDateFin());
				} else {

					final List<DocumentModel> documentModelList = getAllSemaineParlemetaireIncluDansLaPeriode(session,
							assemblee, dateDebut, dateFin);
					// // on lance les suppressions des semaines parlementaire
					// new UnrestrictedSessionRunner(session) {
					// @Override
					// public void run() throws ClientException {
					// for (int i = 0; i < documentModelList.size(); i++) {
					// DocumentModel doc = documentModelList.get(i);
					// session.removeDocument(doc.getRef());
					// }
					// }
					// }.runUnrestricted();
					LOGGER.info(session, MgppLogEnumImpl.DEL_SEM_PARLEMENTAIRE_TEC, documentModelList);
					for (int i = 0; i < documentModelList.size(); i++) {
						final DocumentModel docToremove = documentModelList.get(i);
						session.removeDocument(docToremove.getRef());
					}

					creerNouvelleSemaineParlementaire(session, orientation, assemblee, dateDebut, dateFin);

				}
			}
		}
		final String message = "dateDebut=" + dateDebutStr + "; dateFin=" + dateFinStr + "; orientation=" + orientation
				+ "; assemblee=" + assemblee;
		LOGGER.info(session, MgppLogEnumImpl.CREATE_SEM_PARLEMENTAIRE_TEC, message);
	}

	private List<DocumentModel> getAllSemaineParlemetaireIncluDansLaPeriode(final CoreSession session,
			final String assemblee, final Calendar dateDebut, final Calendar dateFin) throws ClientException {

		final String literalDateDebut = DateLiteral.dateFormatter.print(dateDebut.getTimeInMillis());
		final String literalDateFin = DateLiteral.dateFormatter.print(dateFin.getTimeInMillis());

		final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_DOC_TYPE + " as d");
		queryBuilder.append(" WHERE d.");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(SemaineParlementaire.ASSEMBLEE);
		queryBuilder.append(" = '" + assemblee + "'");
		queryBuilder.append(" AND d.");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(SemaineParlementaire.DATE_DEBUT);
		queryBuilder.append(" >= DATE '" + literalDateDebut + "'");
		queryBuilder.append(" AND d.");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(SemaineParlementaire.DATE_FIN);
		queryBuilder.append(" <= DATE '" + literalDateFin + "'");
		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SemaineParlementaire.SEMAINE_PARLEMENTAIRE_DOC_TYPE, queryBuilder.toString(), new Object[] {});
	}

	private DocumentModel getAllSemainesAvantDateDebutEtAvantDateFin(final CoreSession session, final String assemblee,
			final Calendar dateDebut, final Calendar dateFin) throws ClientException {

		final String literalDateDebut = DateLiteral.dateFormatter.print(dateDebut.getTimeInMillis());
		final String literalDateFin = DateLiteral.dateFormatter.print(dateFin.getTimeInMillis());

		DocumentModel result = null;
		final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_DOC_TYPE + " as d");
		queryBuilder.append(" WHERE d.");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(SemaineParlementaire.ASSEMBLEE);
		queryBuilder.append(" = '" + assemblee + "'");
		queryBuilder.append(" AND d.");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(SemaineParlementaire.DATE_DEBUT);
		queryBuilder.append(" < DATE '" + literalDateDebut + "'");
		queryBuilder.append(" AND d.");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(SemaineParlementaire.DATE_FIN);
		queryBuilder.append(" >= DATE '" + literalDateDebut + "'");
		queryBuilder.append(" AND d.");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(SemaineParlementaire.DATE_FIN);
		queryBuilder.append(" < DATE '" + literalDateFin + "'");
		final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SemaineParlementaire.SEMAINE_PARLEMENTAIRE_DOC_TYPE, queryBuilder.toString(), new Object[] {});
		if (docs.size() > 0) {
			result = docs.get(0);
		}
		return result;
	}

	private DocumentModel getAllSemainesAvantDateDebutEtApresDateFin(final CoreSession session, final String assemblee,
			final Calendar dateDebut, final Calendar dateFin) throws ClientException {

		final String literalDateDebut = DateLiteral.dateFormatter.print(dateDebut.getTimeInMillis());
		final String literalDateFin = DateLiteral.dateFormatter.print(dateFin.getTimeInMillis());

		DocumentModel result = null;
		final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_DOC_TYPE + " as d");
		queryBuilder.append(" WHERE d.");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(SemaineParlementaire.ASSEMBLEE);
		queryBuilder.append(" = '" + assemblee + "'");
		queryBuilder.append(" AND d.");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(SemaineParlementaire.DATE_DEBUT);
		queryBuilder.append(" < DATE '" + literalDateDebut + "'");
		queryBuilder.append(" AND d.");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(SemaineParlementaire.DATE_FIN);
		queryBuilder.append(" > DATE '" + literalDateFin + "'");
		final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SemaineParlementaire.SEMAINE_PARLEMENTAIRE_DOC_TYPE, queryBuilder.toString(), new Object[] {});
		if (docs.size() > 0) {
			result = docs.get(0);
		}
		return result;
	}

	private DocumentModel getAllSemainesApresDateDebutEtApresDateFin(final CoreSession session, final String assemblee,
			final Calendar dateDebut, final Calendar dateFin) throws ClientException {

		final String literalDateDebut = DateLiteral.dateFormatter.print(dateDebut.getTimeInMillis());
		final String literalDateFin = DateLiteral.dateFormatter.print(dateFin.getTimeInMillis());

		DocumentModel result = null;
		final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_DOC_TYPE + " as d");
		queryBuilder.append(" WHERE d.");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(SemaineParlementaire.ASSEMBLEE);
		queryBuilder.append(" = '" + assemblee + "'");
		queryBuilder.append(" AND d.");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(SemaineParlementaire.DATE_DEBUT);
		queryBuilder.append(" >  DATE'" + literalDateDebut + "'");
		queryBuilder.append(" AND d.");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(SemaineParlementaire.DATE_DEBUT);
		queryBuilder.append(" <= DATE '" + literalDateFin + "'");
		queryBuilder.append(" AND d.");
		queryBuilder.append(SemaineParlementaire.SEMAINE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(SemaineParlementaire.DATE_FIN);
		queryBuilder.append(" > DATE '" + literalDateFin + "'");
		final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SemaineParlementaire.SEMAINE_PARLEMENTAIRE_DOC_TYPE, queryBuilder.toString(), new Object[] {});
		if (docs.size() > 0) {
			result = docs.get(0);
		}
		return result;
	}

	private DocumentModel getActiviteParlementaireByDossierIdDateAct(final CoreSession session, final String idDossier,
			final Calendar date, final String activite, String assemblee) throws ClientException {

		final String literalDate = DateLiteral.dateFormatter.print(date.getTimeInMillis());

		DocumentModel result = null;
		final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
		queryBuilder.append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_DOC_TYPE + " as d");
		queryBuilder.append(" WHERE d.");
		queryBuilder.append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(ActiviteParlementaire.ID_DOSSIER);
		queryBuilder.append(" = '" + idDossier + "'");
		queryBuilder.append(" AND d.");
		queryBuilder.append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(ActiviteParlementaire.DATE);
		queryBuilder.append(" = DATE '" + literalDate + "'");
		queryBuilder.append(" AND d.");
		queryBuilder.append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(ActiviteParlementaire.ACTIVITE);
		queryBuilder.append(" = '" + activite + "'");

		queryBuilder.append(" AND d.");
		queryBuilder.append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(ActiviteParlementaire.ASSEMBLEE);
		queryBuilder.append(" = '" + assemblee + "'");

		final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_DOC_TYPE, queryBuilder.toString(), new Object[] {});
		if (docs.size() > 0) {
			result = docs.get(0);
		}
		return result;
	}

	@Override
	public List<String> getListIdsDossierPublie(final CoreSession session) throws ClientException {
		if (session == null) {
			return null;
		}

		final String query = "SELECT d." + FichePresentationDR.PREFIX + ":" + FichePresentationDR.ID_DOSSIER
				+ " as idDossier from " + FichePresentationDR.DOC_TYPE + " as d";
		final List<String> idDossierList = QueryUtils.doUFNXQLQueryAndMapping(session, query, null,
				new QueryUtils.RowMapper<String>() {

					@Override
					public String doMapping(final Map<String, Serializable> rowData) throws ClientException {
						return (String) rowData.get("idDossier");
					}

				});

		return idDossierList;
	}

	private List<String> getAllDossierIdsForActiviteParlementaire(final CoreSession session,
			final Date dateDeDebutFiltre, final Date dateDeFinFiltre) throws ClientException {

		String startDt = "";
		String endDt = "";
		final StringBuilder whereClause = new StringBuilder();

		if (dateDeDebutFiltre != null) {
			startDt = DateLiteral.dateFormatter.print(dateDeDebutFiltre.getTime());
		}

		if (dateDeFinFiltre != null) {
			endDt = DateLiteral.dateFormatter.print(dateDeFinFiltre.getTime());
		}

		if (dateDeDebutFiltre != null) {
			if (dateDeFinFiltre != null) {

				whereClause.append(" WHERE a.").append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX).append(":")
						.append(ActiviteParlementaire.DATE).append(" >= DATE '").append(startDt).append("'")
						.append(" AND a.").append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX).append(":")
						.append(ActiviteParlementaire.DATE).append(" <= DATE '").append(endDt).append("'");

			} else {

				whereClause.append(" WHERE a.").append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX).append(":")
						.append(ActiviteParlementaire.DATE).append(" >= DATE '").append(startDt).append("'");
			}

		} else {

			if (dateDeFinFiltre != null) {
				whereClause.append(" WHERE a.").append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX).append(":")
						.append(ActiviteParlementaire.DATE).append(" <= DATE '").append(endDt).append("'");
			}
		}

		final StringBuilder query = new StringBuilder("SELECT Distinct a.")
				.append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_PREFIX).append(":")
				.append(ActiviteParlementaire.ID_DOSSIER).append(" as idDossier").append(" FROM ")
				.append(ActiviteParlementaire.ACTIVITE_PARLEMENTAIRE_DOC_TYPE).append(" as a").append(whereClause);

		IterableQueryResult res = null;
		final List<String> list = new ArrayList<String>();
		try {
			res = QueryUtils.doUFNXQLQuery(session, query.toString(), null);
			final Iterator<Map<String, Serializable>> iterator = res.iterator();
			while (iterator.hasNext()) {
				final Map<String, Serializable> nextIterator = iterator.next();
				final Serializable idDossier = nextIterator.get("idDossier");
				list.add((String) idDossier);
			}
		} finally {
			if (res != null) {
				res.close();
			}
		}
		return list;
	}

	public StringBuffer getEcheancePrHtml(final List<String[]> listEcheancier, final String[] entetesDesColonnes) {
		final String emptyCell = "&nbsp;";
		final StringBuffer script = new StringBuffer();
		script.append("<html>");

		script.append("<head>");
		script.append("<title>Echeancier de promulgation</title>");
		script.append("<style>");
		script.append("th {background: none repeat scroll 0 0 #F3EFDD;border-bottom: 1px solid #C7C7C7; border-top: 2px solid #B5B1A6; color: #000000; padding: 5px 3px;text-align: left; white-space: nowrap;}");
		script.append(".dataRowOdd {background: none repeat scroll 0 0 #F7F7F7;}");
		script.append(".dataRowEven {background: none repeat scroll 0 0 #FFFFFF;}");
		script.append("table {font: 11px/1.25em 'Lucida Grande',Verdana,Arial,sans-serif;}");
		script.append("</style>");
		script.append("</head>");

		script.append("<table>");
		script.append("<thead>");
		script.append("<tr>");
		script.append("<th>" + entetesDesColonnes[0] + "</th>");
		script.append("<th>" + entetesDesColonnes[1] + "</th>");
		script.append("<th>" + entetesDesColonnes[2] + "</th>");
		script.append("<th>" + entetesDesColonnes[3] + "</th>");
		script.append("<th>" + entetesDesColonnes[4] + "</th>");
		script.append("<th>" + entetesDesColonnes[5] + "</th>");
		script.append("</tr>");
		script.append("</thead>");
		script.append("<tbody>");
		int count = 0;
		for (final String[] echancier : listEcheancier) {
			script.append("<tr class='" + (count % 2 == 0 ? "dataRowEven" : "dataRowOdd") + "'>");
			script.append("<td>" + (!"".equals(echancier[0]) ? echancier[0] : emptyCell) + "</td>");
			script.append("<td>" + (!"".equals(echancier[1]) ? echancier[1] : emptyCell) + "</td>");
			script.append("<td>" + (!"".equals(echancier[2]) ? echancier[2] : emptyCell) + "</td>");
			script.append("<td>" + (!"".equals(echancier[3]) ? echancier[3] : emptyCell) + "</td>");
			script.append("<td>" + (!"".equals(echancier[4]) ? echancier[4] : emptyCell) + "</td>");
			script.append("<td>" + (!"".equals(echancier[5]) ? echancier[5] : emptyCell) + "</td>");
			script.append("</tr>");
			count++;
		}
		script.append("</tbody>");
		script.append("</table>");

		script.append("</html>");

		return script;
	}

	@Override
	public void diffuserEcheancierPromulgation(final List<String[]> listEcheancier, final String[] entetesDesColonnes) {
		final StringBuffer html = getEcheancePrHtml(listEcheancier, entetesDesColonnes);
		try {

			final File outputFile = File.createTempFile("CalendrierParlementaire.html", ".html");
			final OutputStream outputStream = new FileOutputStream(outputFile);
			final PrintStream printStream = new PrintStream(outputStream);
			printStream.print(html);
			printStream.close();
			outputStream.close();
		} catch (final IOException e) {
			LOGGER.error(EpgLogEnumImpl.FAIL_DIFFUSER_ECHEANCIER_PROMULGATION_TEC);
		}
	}

	@Override
	public void diffuserCalendrierParlementaire(final Map<String, List<Map<String, String>>> listedeSemainesParTypes,
			final List<Date> listeDeToutesLesDatesDesActivites, final List<FicheLoi> listeDeToutLesLois,
			final Map<String, Collection<List<ActiviteParlementaire>>> mapDeToutesLesActivitesParlementaireDeCeLoi)
			throws ClientException {
		final StringBuffer html = getHtml(listedeSemainesParTypes, listeDeToutesLesDatesDesActivites,
				listeDeToutLesLois, mapDeToutesLesActivitesParlementaireDeCeLoi);

		try {

			final File outputFile = File.createTempFile("CalendrierParlementaire.html", ".html");
			final OutputStream outputStream = new FileOutputStream(outputFile);
			final PrintStream printStream = new PrintStream(outputStream);
			printStream.print(html);
			printStream.close();
			outputStream.close();
		} catch (final IOException e) {
			LOGGER.error(EpgLogEnumImpl.FAIL_DIFFUSER_CALENDRIER_PARLEMENTAIRE_TEC);
		}
	}

	private StringBuffer getHtml(final Map<String, List<Map<String, String>>> listedeSemainesParTypes,
			final List<Date> listeDeToutesLesDatesDesActivites, final List<FicheLoi> listeDeToutLesLois,
			final Map<String, Collection<List<ActiviteParlementaire>>> mapDeToutesLesActivitesParlementaireDeCeLoi) {
		final StringBuffer script = new StringBuffer();

		script.append("<html>");

		script.append(getHead(listedeSemainesParTypes, listeDeToutLesLois));
		script.append(getBody(listedeSemainesParTypes, listeDeToutesLesDatesDesActivites, listeDeToutLesLois,
				mapDeToutesLesActivitesParlementaireDeCeLoi));

		script.append("</html>");

		return script;
	}

	private StringBuffer getHead(final Map<String, List<Map<String, String>>> listedeSemainesParTypes,
			final List<FicheLoi> listeDeToutLesLois) {
		final StringBuffer script = new StringBuffer();

		script.append("<head>");
		script.append("<title>Calendrier parlementaire</title>");
		script.append(getStyle());
		script.append(getJavaScript(listedeSemainesParTypes, listeDeToutLesLois));
		script.append("</head>");

		return script;
	}

	private StringBuffer getStyle() {
		final StringBuffer script = new StringBuffer();

		script.append("<style>");
		script.append(".mainTable{");
		script.append("border-collapse: collapse;");
		script.append("}");

		script.append(".fixedTable{");
		script.append("border-collapse: collapse;");
		script.append("border: 0px;");
		script.append("}");

		script.append(".scrollableTable{");
		script.append("border-collapse: collapse;");
		script.append("border: 0px;");
		script.append("}");

		script.append(".fixedTableCellule{");
		script.append("border-top: 1px solid black;");
		script.append("border-bottom: 1px solid black;");
		script.append("}");

		script.append(".scrollableTableCellule{");
		script.append("border: 1px solid black;");
		script.append("}");

		script.append(".supprimerActivite{");
		script.append("opacity:0.4;");
		script.append("filter:alpha(opacity=40);");
		script.append("cursor:default;");
		script.append("}");

		script.append(".supprimerActivite:hover{");
		script.append("opacity:1;");
		script.append("filter:alpha(opacity=100);");
		script.append("cursor: pointer;");
		script.append("}");

		script.append("</style>");

		return script;
	}

	private StringBuffer getJavaScript(final Map<String, List<Map<String, String>>> listedeSemainesParTypes,
			final List<FicheLoi> listeDeToutLesLois) {
		final StringBuffer script = new StringBuffer();

		script.append("<script>");
		script.append("var j = jQuery.noConflict();");
		script.append("j(document).ready(function() {");

		for (final Map.Entry<String, List<Map<String, String>>> assemblee : listedeSemainesParTypes.entrySet()) {
			script.append("setSameHeightToTheseRows(\"fixedTable_Upper_Header_" + assemblee.getKey()
					+ "\", \"scrollableTable_Upper_Header_" + assemblee.getKey() + "\");");
		}

		script.append("setSameHeightToTheseRows(\"fixedTable_Row_Header\", \"scrollableTable_Row_Header\");");
		script.append("var fixedTableRowHeight = j(\"#fixedTable_Row_Header\").height();");
		script.append("var scrollableTableRowHeight = j(\"#scrollableTable_Row_Header\").height();");
		script.append("j(\"#fixedTable_Row_Header\").height(j(\"#fixedTable_Row_Header\").height() + 10);");
		script.append("j(\"#scrollableTable_Row_Header\").height(j(\"#scrollableTable_Row_Header\").height() + 10);");

		for (int index = 0; index < listeDeToutLesLois.size(); index++) {
			script.append("setSameHeightToTheseRows(\"fixedTable_Row_" + index + "\", \"scrollableTable_Row_" + index
					+ "\");");
		}
		script.append("});");

		script.append("function setSameHeightToTheseRows(row1Id, row2Id)");
		script.append("{");
		script.append("var row1Height = j(\"#\" + row1Id).height();");
		script.append("var row2Height = j(\"#\" + row2Id).height();");
		script.append("var rowHeightShouldBe = 0;");

		script.append("if(row1Height > row2Height)");
		script.append("{");
		script.append("rowHeightShouldBe = row1Height;");
		script.append("}else");
		script.append("{");
		script.append("rowHeightShouldBe = row2Height;");
		script.append("}");

		script.append("j(\"#\" + row1Id).height(rowHeightShouldBe);");
		script.append("j(\"#\" + row2Id).height(rowHeightShouldBe);");
		script.append("}");
		script.append("</script>");

		return script;
	}

	private StringBuffer getBody(final Map<String, List<Map<String, String>>> listedeSemainesParTypes,
			final List<Date> listeDeToutesLesDatesDesActivites, final List<FicheLoi> listeDeToutLesLois,
			final Map<String, Collection<List<ActiviteParlementaire>>> mapDeToutesLesActivitesParlementaireDeCeLoi) {

		final SimpleDateFormat formatter = DateUtil.simpleDateFormat("dd/MM/yyyy");

		final int widthOfMainTable = 1100;
		final int widthOfFirstColumn = 150;
		final int widthOfSecondColumn = 30;

		final StringBuffer script = new StringBuffer();

		script.append("<body>");

		script.append("<form id=\"calendrierParlementaireMainForm\" enctype=\"multipart/form-data\">");
		script.append("<table style=\"width: " + widthOfMainTable + "px; height: 100%;\" class=\"mainTable\">");
		script.append("<tr>");
		script.append("<td align=\"left\" valign=\"middle\">");

		if (listeDeToutLesLois.size() > 0) {
			script.append("<table width=\"100%\">");
			script.append("<tr>");
			script.append("<td valign=\"top\" style=\"width: " + (widthOfFirstColumn + widthOfSecondColumn)
					+ "px;; padding: 0px; border: 0px;\">");
			script.append("<div id=\"fixedDiv\" class=\"fixedDiv\" style=\"width: "
					+ (widthOfFirstColumn + widthOfSecondColumn) + "px; margin-bottom: 2000px;\">");
			script.append("<table id=\"fixedTable\" class=\"dataOutput\" style=\"margin-bottom: 10px ! important;\">");
			script.append("<thead>");

			for (final Map.Entry<String, List<Map<String, String>>> assemblee : listedeSemainesParTypes.entrySet()) {
				script.append("<tr id=\"fixedTable_Upper_Header_" + assemblee.getKey() + "\">");
				script.append("<td colspan=\"2\" align=\"right\">");
				script.append(assemblee.getKey());
				script.append("</td>");
				script.append("</tr>");
			}

			script.append("<tr id=\"fixedTable_Row_Header\">");
			script.append("<th colspan=\"2\"> Lois </th>");
			script.append("</tr>");
			script.append("</thead>");

			String classTR = "dataRowEven";
			for (int ficheLoiIndex = 0; ficheLoiIndex < listeDeToutLesLois.size(); ficheLoiIndex++) {
				final FicheLoi ficheLoi = listeDeToutLesLois.get(ficheLoiIndex);

				if (ficheLoiIndex % 2 == 0) {
					classTR = "dataRowEven";
				} else {
					classTR = "dataRowOdd";
				}
				script.append("<tr id=\"fixedTable_Row_" + ficheLoiIndex + "\" class=\"" + classTR + "\" idDossier=\""
						+ ficheLoi.getIdDossier() + "\">");
				script.append("<td valign=\"middle\" align=\"left\" style=\"width: " + widthOfFirstColumn + "px;\">");
				script.append(ficheLoi.getIntitule());
				script.append("</td>");
				script.append("<td valign=\"middle\" align=\"left\" style=\"width: " + widthOfSecondColumn + "px;\">");
				script.append("&nbsp;");
				script.append("</td>");
				script.append("</tr>");
			}
			script.append("</table>");
			script.append("</div>");
			script.append("</td>");
			script.append("<td valign=\"top\" style=\"padding: 0px; border: 0px;\">");
			script.append("<div id=\"scrollableDiv\" style=\"overflow: auto; width: "
					+ (widthOfMainTable - widthOfFirstColumn - widthOfSecondColumn) + "px; margin-bottom: 2000px;\">");
			script.append("<table id=\"scrollableTable\" class=\"scrollableTable\" class=\"dataOutput\" style=\"width: "
					+ listeDeToutesLesDatesDesActivites.size() * 150 + "px; margin-bottom: 10px ! important;\">");
			script.append("<thead>");

			for (final Map.Entry<String, List<Map<String, String>>> assemblee : listedeSemainesParTypes.entrySet()) {
				script.append("<tr id=\"scrollableTable_Upper_Header_" + assemblee.getKey() + "\">");

				for (final Map<String, String> semaineParAssemblee : assemblee.getValue()) {
					if (semaineParAssemblee == null) {
						script.append("<td>");
						script.append("&nbsp;");
						script.append("<td>");
					} else {
						script.append("<td colspan=\"" + semaineParAssemblee.get("colspan")
								+ "\" align=\"center\" style=\"background-color: #E7DECE;\">");
						script.append("<table class=\"celluleDeSemaine\" height=\"100%\" width=\"100%\">");
						script.append("<tr>");
						script.append("<td valign=\"middle\" align=\"center\">");
						script.append(semaineParAssemblee.get("label"));
						script.append("</td>");

						script.append("<td valign=\"top\" align=\"left\" height=\"16\" width=\"16\" style=\"padding: 0px;\">");
						script.append("&nbsp;");
						script.append("</td>");
						script.append("</tr>");
						script.append("</table>");
						script.append("</td>");

					}
				}
				script.append("</tr>");
			}
			script.append("<tr id=\"scrollableTable_Row_Header\">");
			for (final Date dateDuneActivite : listeDeToutesLesDatesDesActivites) {
				script.append("<th>");
				script.append(formatter.format(dateDuneActivite));
				script.append("</th>");
			}
			script.append("</tr>");
			script.append("</thead>");

			for (int ficheLoiIndex = 0; ficheLoiIndex < listeDeToutLesLois.size(); ficheLoiIndex++) {
				final FicheLoi ficheLoi = listeDeToutLesLois.get(ficheLoiIndex);
				if (ficheLoiIndex % 2 == 0) {
					classTR = "dataRowEven";
				} else {
					classTR = "dataRowOdd";
				}
				script.append("<tr id=\"scrollableTable_Row_" + ficheLoiIndex + "\" class=\"" + classTR + "\">");

				final Collection<List<ActiviteParlementaire>> toutesLesActivitesParlementaireDeCeLoi = mapDeToutesLesActivitesParlementaireDeCeLoi
						.get(ficheLoi.getIdDossier());
				for (final List<ActiviteParlementaire> activiteParlementaireList : toutesLesActivitesParlementaireDeCeLoi) {

					script.append("<td style=\"padding: 0px;\" valign=\"middle\" align=\"left\">");

					if (activiteParlementaireList == null) {
						script.append("&nbsp;");
					} else {
						script.append("<table class=\"celluleDactivite\" height=\"100%\" width=\"100%\">");
						for (final ActiviteParlementaire activiteParlementaire : activiteParlementaireList) {
							script.append("<tr>");
							script.append("<td valign=\"middle\" align=\"left\">");
							script.append(activiteParlementaire.getActivite());
							script.append("</td>");
							script.append("<td valign=\"top\" align=\"left\" height=\"16\" width=\"16\" style=\"padding: 0px;\">");
							script.append("&nbsp;");
							script.append("</td>");
							script.append("</tr>");
						}
						script.append("</table>");
					}

					script.append("</td>");
				}
				script.append("</tr>");
			}
			script.append("</table>");
			script.append("</div>");
			script.append("</td>");
			script.append("</tr>");
			script.append("</table>");
		}

		script.append("</td>");
		script.append("</tr>");
		script.append("</table>");
		script.append("</form>");
		script.append("</body>");

		return script;
	}

	private void creerNouvelleSemaineParlementaire(final CoreSession session, final String orientation,
			final String assemblee, final Calendar dateDebut, final Calendar dateFin) throws ClientException {
		final SimpleDateFormat formatter = DateUtil.simpleDateFormat("ddMMyyyy");
		final String dateDebutStr = formatter.format(dateDebut.getTime());
		final String dateFinStr = formatter.format(dateFin.getTime());
		final DocumentModel semaineParlementaireModel = session.createDocumentModel(
				"/case-management/semaine-parlementaire", assemblee + "_SP_" + dateDebutStr + "_" + dateFinStr,
				SemaineParlementaire.SEMAINE_PARLEMENTAIRE_DOC_TYPE);
		final SemaineParlementaire semaineParlementaireNew = semaineParlementaireModel
				.getAdapter(SemaineParlementaire.class);
		semaineParlementaireNew.setOrientation(orientation);
		semaineParlementaireNew.setAssemblee(assemblee);
		semaineParlementaireNew.setDateDebut(dateDebut);
		semaineParlementaireNew.setDateFin(dateFin);
		session.createDocument(semaineParlementaireNew.getDocument());
		session.save();
	}

}
