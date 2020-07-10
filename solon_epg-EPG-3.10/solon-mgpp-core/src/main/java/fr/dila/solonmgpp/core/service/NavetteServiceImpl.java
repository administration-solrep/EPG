package fr.dila.solonmgpp.core.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;

import fr.dila.solonepp.rest.api.WSEvenement;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.Navette;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.NavetteService;
import fr.dila.solonmgpp.core.util.WSErrorHelper;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.rest.client.HttpTransactionException;
import fr.dila.st.rest.client.WSProxyFactoryException;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epp.ChercherEvenementRequest;
import fr.sword.xsd.solon.epp.ChercherEvenementResponse;
import fr.sword.xsd.solon.epp.EppEvt01;
import fr.sword.xsd.solon.epp.EppEvt12;
import fr.sword.xsd.solon.epp.EppEvt13;
import fr.sword.xsd.solon.epp.EppEvt14;
import fr.sword.xsd.solon.epp.EppEvt19;
import fr.sword.xsd.solon.epp.EppEvt21;
import fr.sword.xsd.solon.epp.EppEvt22;
import fr.sword.xsd.solon.epp.EppEvt23Bis;
import fr.sword.xsd.solon.epp.EppEvt23Quater;
import fr.sword.xsd.solon.epp.EppEvt24;
import fr.sword.xsd.solon.epp.EppEvt25;
import fr.sword.xsd.solon.epp.EppEvtContainer;
import fr.sword.xsd.solon.epp.EtatVersion;
import fr.sword.xsd.solon.epp.EvenementType;
import fr.sword.xsd.solon.epp.EvtId;
import fr.sword.xsd.solon.epp.Institution;
import fr.sword.xsd.solon.epp.NiveauLecture;
import fr.sword.xsd.solon.epp.NiveauLectureCode;
import fr.sword.xsd.solon.epp.PieceJointe;

/**
 * Implémentation du service des navettes
 */
public class NavetteServiceImpl implements NavetteService {

	/**
	 * Logger surcouche socle de log4j
	 */
	private static final STLogger	LOGGER	= STLogFactory.getLog(NavetteServiceImpl.class);

	private class NavetteFicheLoi {

		private final String		idDossier;
		private final NiveauLecture	niveauLecture;
		private PieceJointe			texteAdopte;
		private String				numeroTexte;
		private List<Calendar>		dateCMP;
		private String				resultatCMP;
		private Calendar			dateAdoption;
		private String				sortAdoption;
		private Calendar			dateAdoptionAN;
		private String				sortAdoptionAN;
		private Calendar			dateAdoptionSE;
		private String				sortAdoptionSE;

		public NavetteFicheLoi(final String idDossier, final NiveauLecture niveauLecture) {
			this.idDossier = idDossier;
			this.niveauLecture = niveauLecture;
			this.dateAdoption = Calendar.getInstance();
		}

		@Override
		public String toString() {
			return idDossier;
		}

	}

	@Override
	public Boolean hasNavette(final CoreSession session, final String idFicheLoi) throws ClientException {

		final StringBuilder query = new StringBuilder(" SELECT n.ecm:uuid as id FROM ");
		query.append(Navette.DOC_TYPE);
		query.append(" as n WHERE n.");
		query.append(Navette.PREFIX);
		query.append(":");
		query.append(Navette.ID_FICHE);
		query.append(" = ? ");

		final Long count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(query.toString()),
				new Object[] { idFicheLoi });
		return count > 0;

	}

	@Override
	public List<DocumentModel> fetchNavette(final CoreSession session, final String idFicheLoi) throws ClientException {

		final StringBuilder query = new StringBuilder(" SELECT n.ecm:uuid as id FROM ");
		query.append(Navette.DOC_TYPE);
		query.append(" as n WHERE n.");
		query.append(Navette.PREFIX);
		query.append(":");
		query.append(Navette.ID_FICHE);
		query.append(" = ? ");
		query.append(" order by n.");
		query.append(Navette.PREFIX);
		query.append(":");
		query.append(Navette.DATE_NAVETTE);
		query.append(" asc ");

		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, Navette.DOC_TYPE, query.toString(),
				new Object[] { idFicheLoi });

	}

	@Override
	public void addNavetteToFicheLoi(final CoreSession session, final String idEvenement,
			final EvenementType typeEvenement) throws ClientException {
		WSEvenement wsEvenement = null;

		try {
			wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
		} catch (final WSProxyFactoryException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		final ChercherEvenementRequest chercherEvenementRequest = new ChercherEvenementRequest();

		final EvtId evtId = new EvtId();
		evtId.setId(idEvenement);

		chercherEvenementRequest.getIdEvenement().add(evtId);

		ChercherEvenementResponse chercherEvenementResponse = null;

		try {
			chercherEvenementResponse = wsEvenement.chercherEvenement(chercherEvenementRequest);
		} catch (final HttpTransactionException e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
			throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
		} catch (final Exception e) {
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
			throw new ClientException(e);
		}

		if (chercherEvenementResponse == null) {
			throw new ClientException(
					"Erreur de communication avec SOLON EPP, la récupération de la communication a échoué.");
		} else if (chercherEvenementResponse.getStatut() == null
				|| !TraitementStatut.OK.equals(chercherEvenementResponse.getStatut())) {
			final ClientException clientExc = new ClientException(
					"Erreur de communication avec SOLON EPP, la récupération de la communication a échoué."
							+ WSErrorHelper.buildCleanMessage(chercherEvenementResponse.getMessageErreur()));
			LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, clientExc);
			throw clientExc;
		}

		if (chercherEvenementResponse.getEvenement().size() > 1) {
			throw new ClientException(
					"La récupération de la version courante de la communication a retourné plusieurs résultats.");
		}

		addNavetteToFicheLoi(session, chercherEvenementResponse.getEvenement().iterator().next());
	}

	@Override
	public void addNavetteToFicheLoi(final CoreSession session, final EppEvtContainer eppEvtContainer)
			throws ClientException {
		final EvenementType typeEvenement = eppEvtContainer.getType();

		if (EvenementType.EVT_01.equals(typeEvenement)) {
			final EppEvt01 eppEvt01 = eppEvtContainer.getEvt01();

			if (eppEvt01 != null && EtatVersion.PUBLIE.equals(eppEvt01.getVersionCourante().getEtat())) {
				createNavette(session, new NavetteFicheLoi(eppEvt01.getIdDossier(), eppEvt01.getNiveauLecture()));
				return;
			}
		} else if (EvenementType.EVT_12.equals(typeEvenement)) {
			final EppEvt12 eppEvt12 = eppEvtContainer.getEvt12();
			if (eppEvt12 != null) {

				final NavetteFicheLoi navetteFicheLoi = new NavetteFicheLoi(eppEvt12.getIdDossier(),
						eppEvt12.getNiveauLecture());
				navetteFicheLoi.sortAdoption = eppEvt12.getSortAdoption() == null ? null : eppEvt12.getSortAdoption()
						.value();
				navetteFicheLoi.texteAdopte = eppEvt12.getTexteAdopte();
				navetteFicheLoi.numeroTexte = eppEvt12.getNumeroTexteAdopte();
				navetteFicheLoi.dateAdoption = DateUtil.xmlGregorianCalendarToCalendar(eppEvt12.getDateAdoption());

				if (Institution.ASSEMBLEE_NATIONALE.equals(eppEvt12.getEmetteur())) {
					navetteFicheLoi.dateAdoptionAN = DateUtil
							.xmlGregorianCalendarToCalendar(eppEvt12.getDateAdoption());
					navetteFicheLoi.sortAdoptionAN = eppEvt12.getSortAdoption() == null ? null : eppEvt12
							.getSortAdoption().value();
				} else if (Institution.SENAT.equals(eppEvt12.getEmetteur())) {
					navetteFicheLoi.dateAdoptionSE = DateUtil
							.xmlGregorianCalendarToCalendar(eppEvt12.getDateAdoption());
					navetteFicheLoi.sortAdoptionSE = eppEvt12.getSortAdoption() == null ? null : eppEvt12
							.getSortAdoption().value();
				}

				Navette navette = updateNavette(session, navetteFicheLoi);
				if (eppEvt12.getVersionCourante() != null && eppEvt12.getVersionCourante().getMajeur() == 1
						&& eppEvt12.getVersionCourante().getMineur() == 0
						&& eppEvt12.getVersionCourante().getHorodatage() != null) {
					navette.setDateTransmission(eppEvt12.getVersionCourante().getHorodatage().toGregorianCalendar());
					session.saveDocument(navette.getDocument());
					session.save();
				}
				return;
			}
		} else if (EvenementType.EVT_24.equals(typeEvenement)) {
			final EppEvt24 eppEvt24 = eppEvtContainer.getEvt24();
			if (eppEvt24 != null) {
				final NavetteFicheLoi navetteFicheLoi = new NavetteFicheLoi(eppEvt24.getIdDossier(),
						eppEvt24.getNiveauLecture());
				navetteFicheLoi.sortAdoption = eppEvt24.getSortAdoption() == null ? null : eppEvt24.getSortAdoption()
						.value();
				navetteFicheLoi.texteAdopte = eppEvt24.getTexteAdopte();
				navetteFicheLoi.numeroTexte = eppEvt24.getNumeroTexteAdopte();
				navetteFicheLoi.dateAdoption = DateUtil.xmlGregorianCalendarToCalendar(eppEvt24.getDateAdoption());

				if (Institution.ASSEMBLEE_NATIONALE.equals(eppEvt24.getEmetteur())) {
					navetteFicheLoi.dateAdoptionAN = DateUtil
							.xmlGregorianCalendarToCalendar(eppEvt24.getDateAdoption());
					navetteFicheLoi.sortAdoptionAN = eppEvt24.getSortAdoption() == null ? null : eppEvt24
							.getSortAdoption().value();
				} else if (Institution.SENAT.equals(eppEvt24.getEmetteur())) {
					navetteFicheLoi.dateAdoptionSE = DateUtil
							.xmlGregorianCalendarToCalendar(eppEvt24.getDateAdoption());
					navetteFicheLoi.sortAdoptionSE = eppEvt24.getSortAdoption() == null ? null : eppEvt24
							.getSortAdoption().value();
				}

				Navette navette = updateNavette(session, navetteFicheLoi);

				if (eppEvt24.getVersionCourante() != null && eppEvt24.getVersionCourante().getMajeur() == 1
						&& eppEvt24.getVersionCourante().getMineur() == 0
						&& eppEvt24.getVersionCourante().getHorodatage() != null) {
					navette.setDateTransmission(Calendar.getInstance());
					session.saveDocument(navette.getDocument());
					session.save();
				}
				return;
			}
		} else if (EvenementType.EVT_13.equals(typeEvenement)) {
			final EppEvt13 eppEvt13 = eppEvtContainer.getEvt13();
			if (eppEvt13 != null) {
				Navette navette = createNavette(session,
						new NavetteFicheLoi(eppEvt13.getIdDossier(), eppEvt13.getNiveauLecture()));
				if (eppEvt13.getVersionCourante() != null && eppEvt13.getVersionCourante().getMajeur() == 1
						&& eppEvt13.getVersionCourante().getMineur() == 0
						&& eppEvt13.getVersionCourante().getHorodatage() != null) {
					navette.setDateTransmission(Calendar.getInstance());
					session.saveDocument(navette.getDocument());
					session.save();
				}

				return;
			}
		} else if (EvenementType.EVT_14.equals(typeEvenement)) {
			final EppEvt14 eppEvt14 = eppEvtContainer.getEvt14();
			if (eppEvt14 != null) {

				final NavetteFicheLoi navetteFicheLoi = new NavetteFicheLoi(eppEvt14.getIdDossier(),
						eppEvt14.getNiveauLecture());

				navetteFicheLoi.sortAdoption = eppEvt14.getSortAdoption() == null ? null : eppEvt14.getSortAdoption()
						.value();
				navetteFicheLoi.texteAdopte = eppEvt14.getTexteAdopte();
				navetteFicheLoi.numeroTexte = eppEvt14.getNumeroTexteAdopte();
				navetteFicheLoi.dateAdoption = DateUtil.xmlGregorianCalendarToCalendar(eppEvt14.getDateAdoption());
				if (Institution.ASSEMBLEE_NATIONALE.equals(eppEvt14.getEmetteur())) {
					navetteFicheLoi.dateAdoptionAN = DateUtil
							.xmlGregorianCalendarToCalendar(eppEvt14.getDateAdoption());
					navetteFicheLoi.sortAdoptionAN = eppEvt14.getSortAdoption() == null ? null : eppEvt14
							.getSortAdoption().value();
				} else if (Institution.SENAT.equals(eppEvt14.getEmetteur())) {
					navetteFicheLoi.dateAdoptionSE = DateUtil
							.xmlGregorianCalendarToCalendar(eppEvt14.getDateAdoption());
					navetteFicheLoi.sortAdoptionSE = eppEvt14.getSortAdoption() == null ? null : eppEvt14
							.getSortAdoption().value();
				}

				Navette navette = updateNavette(session, navetteFicheLoi);
				if (eppEvt14.getVersionCourante() != null && eppEvt14.getVersionCourante().getMajeur() == 1
						&& eppEvt14.getVersionCourante().getMineur() == 0
						&& eppEvt14.getVersionCourante().getHorodatage() != null) {
					navette.setDateNavette(eppEvt14.getVersionCourante().getHorodatage().toGregorianCalendar());
					session.saveDocument(navette.getDocument());
					session.save();
				}
				return;
			}
		} else if (EvenementType.EVT_19.equals(typeEvenement)) {
			final EppEvt19 eppEvt19 = eppEvtContainer.getEvt19();
			if (eppEvt19 != null && EtatVersion.PUBLIE.equals(eppEvt19.getVersionCourante().getEtat())) {
				createNavette(session, new NavetteFicheLoi(eppEvt19.getIdDossier(), eppEvt19.getNiveauLecture()));
				return;
			}
		} else if (EvenementType.EVT_21.equals(typeEvenement)) {
			final EppEvt21 eppEvt21 = eppEvtContainer.getEvt21();
			if (eppEvt21 != null) {

				final NavetteFicheLoi navetteFicheLoi = new NavetteFicheLoi(eppEvt21.getIdDossier(),
						eppEvt21.getNiveauLecture());

				navetteFicheLoi.dateCMP = new ArrayList<Calendar>();

				for (final XMLGregorianCalendar dateCMP : eppEvt21.getDateCMP()) {
					navetteFicheLoi.dateCMP.add(DateUtil.xmlGregorianCalendarToCalendar(dateCMP));
				}

				updateNavette(session, navetteFicheLoi);
				return;
			}
		} else if (EvenementType.EVT_22.equals(typeEvenement)) {
			final EppEvt22 eppEvt22 = eppEvtContainer.getEvt22();
			if (eppEvt22 != null) {
				final NavetteFicheLoi navetteFicheLoi = new NavetteFicheLoi(eppEvt22.getIdDossier(),
						eppEvt22.getNiveauLecture());

				navetteFicheLoi.resultatCMP = eppEvt22.getResultatCmp().value();

				updateNavette(session, navetteFicheLoi);
				return;
			}
		} else if (EvenementType.EVT_23_BIS.equals(typeEvenement)) {
			final EppEvt23Bis eppEvt23B = eppEvtContainer.getEvt23Bis();
			if (eppEvt23B != null && EtatVersion.PUBLIE.equals(eppEvt23B.getVersionCourante().getEtat())) {
				createNavette(session, new NavetteFicheLoi(eppEvt23B.getIdDossier(), eppEvt23B.getNiveauLecture()));
				return;
			}
		} else if (EvenementType.EVT_23_QUATER.equals(typeEvenement)) {
			final EppEvt23Quater eppEvt23Q = eppEvtContainer.getEvt23Quater();
			if (eppEvt23Q != null && EtatVersion.PUBLIE.equals(eppEvt23Q.getVersionCourante().getEtat())) {
				createNavette(session, new NavetteFicheLoi(eppEvt23Q.getIdDossier(), eppEvt23Q.getNiveauLecture()));
				return;
			}
		} else if (EvenementType.EVT_25.equals(typeEvenement)) {
			final EppEvt25 eppEvt25 = eppEvtContainer.getEvt25();
			if (eppEvt25 != null && EtatVersion.PUBLIE.equals(eppEvt25.getVersionCourante().getEtat())) {
				createNavette(session, new NavetteFicheLoi(eppEvt25.getIdDossier(), eppEvt25.getNiveauLecture()));
				return;
			}
		}
	}

	private Navette updateNavette(final CoreSession session, final NavetteFicheLoi navetteFicheLoi)
			throws ClientException {
		final FicheLoi ficheLoi = SolonMgppServiceLocator.getDossierService().findOrCreateFicheLoi(session,
				navetteFicheLoi.idDossier);

		Navette navette = findNavette(session, navetteFicheLoi.niveauLecture, ficheLoi);

		if (navette == null) {

			navette = createNavette(ficheLoi);

			if (navetteFicheLoi.niveauLecture != null) {
				final NiveauLectureCode code = navetteFicheLoi.niveauLecture.getCode();
				if (code != null) {
					navette.setCodeLecture(code.value());
				}

				final Integer niveau = navetteFicheLoi.niveauLecture.getNiveau();
				if (niveau != null) {
					navette.setNiveauLecture(niveau.longValue());
				}
			}

		}

		navette.setDateAdoption(navetteFicheLoi.dateAdoption);
		navette.setSortAdoption(navetteFicheLoi.sortAdoption == null ? null : navetteFicheLoi.sortAdoption);

		navette.setNumeroTexte(navetteFicheLoi.numeroTexte);

		if (navetteFicheLoi.texteAdopte != null) {
			navette.setUrl(navetteFicheLoi.texteAdopte.getUrl());
		}

		if (navetteFicheLoi.dateCMP != null && navette.getDateCMP() == null) {
			navette.setDateCMP(navetteFicheLoi.dateCMP);
		}

		if (navetteFicheLoi.resultatCMP != null) {
			navette.setResultatCMP(navetteFicheLoi.resultatCMP);
		}

		if (navetteFicheLoi.dateAdoptionAN != null) {
			navette.setDateAdoptionAN(navetteFicheLoi.dateAdoptionAN);
		}

		if (navetteFicheLoi.sortAdoptionAN != null) {
			navette.setSortAdoptionAN(navetteFicheLoi.sortAdoptionAN);
		}

		if (navetteFicheLoi.dateAdoptionSE != null) {
			navette.setDateAdoptionSE(navetteFicheLoi.dateAdoptionSE);
		}

		if (navetteFicheLoi.sortAdoptionSE != null) {
			navette.setSortAdoptionSE(navetteFicheLoi.sortAdoptionSE);
		}

		if (StringUtils.isBlank(navette.getDocument().getId())) {
			LOGGER.info(session, MgppLogEnumImpl.UPDATE_NAVETTE_TEC,
					"Navette ajoutée pour " + navetteFicheLoi.toString());
			session.createDocument(navette.getDocument());
		} else {
			LOGGER.info(session, MgppLogEnumImpl.UPDATE_NAVETTE_TEC,
					"Navette mise à jour pour " + navetteFicheLoi.toString());
			session.saveDocument(navette.getDocument());
		}

		session.save();

		return navette;

	}

	private Navette findNavette(final CoreSession session, final NiveauLecture niveauLecture, final FicheLoi ficheLoi)
			throws ClientException {
		if (niveauLecture == null) {
			return null;

		} else {
			final List<Object> params = new ArrayList<Object>();

			final StringBuilder query = new StringBuilder("SELECT n.ecm:uuid as id FROM ");
			query.append(Navette.DOC_TYPE);
			query.append(" as n WHERE n.");
			query.append(Navette.PREFIX);
			query.append(":");
			query.append(Navette.ID_FICHE);
			query.append(" = ? ");

			params.add(ficheLoi.getDocument().getId());

			if (niveauLecture.getCode() != null) {
				query.append(" AND n.");
				query.append(Navette.PREFIX);
				query.append(":");
				query.append(Navette.CODE_LECTURE);
				query.append(" = ? ");

				params.add(niveauLecture.getCode().value());
			}

			if (niveauLecture.getNiveau() != null) {
				query.append(" AND n.");
				query.append(Navette.PREFIX);
				query.append(":");
				query.append(Navette.NIVEAU_LECTURE);
				query.append(" = ? ");

				params.add(niveauLecture.getNiveau());
			}

			final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, Navette.DOC_TYPE,
					query.toString(), params.toArray(), 1, 0);

			if (docs.isEmpty()) {
				return null;
			}

			return docs.get(0).getAdapter(Navette.class);

		}

	}

	private Navette createNavette(final CoreSession session, final NavetteFicheLoi navetteFicheLoi)
			throws ClientException {

		final FicheLoi ficheLoi = SolonMgppServiceLocator.getDossierService().findOrCreateFicheLoi(session,
				navetteFicheLoi.idDossier);

		Navette navette = findNavette(session, navetteFicheLoi.niveauLecture, ficheLoi);

		if (navette == null) {
			navette = createNavette(ficheLoi);
		}

		if (navetteFicheLoi.niveauLecture != null) {
			final NiveauLectureCode code = navetteFicheLoi.niveauLecture.getCode();
			if (code != null) {
				navette.setCodeLecture(code.value());
			}

			final Integer niveau = navetteFicheLoi.niveauLecture.getNiveau();
			if (niveau != null) {
				navette.setNiveauLecture(niveau.longValue());
			}
		}

		if (navette.getDateNavette() == null) {
			navette.setDateNavette(Calendar.getInstance());
		}

		if (StringUtils.isBlank(navette.getDocument().getId())) {
			LOGGER.info(session, MgppLogEnumImpl.UPDATE_NAVETTE_TEC,
					"Navette ajoutée pour " + navetteFicheLoi.toString());
			session.createDocument(navette.getDocument());
		} else {
			LOGGER.info(session, MgppLogEnumImpl.UPDATE_NAVETTE_TEC,
					"Navette mise à jour pour " + navetteFicheLoi.toString());
			session.saveDocument(navette.getDocument());
		}

		session.save();

		return navette;

	}

	private Navette createNavette(final FicheLoi ficheLoi) {
		final DocumentModel modelDesired = new DocumentModelImpl(ficheLoi.getDocument().getPathAsString(), UUID
				.randomUUID().toString(), Navette.DOC_TYPE);

		final Navette navette = modelDesired.getAdapter(Navette.class);

		navette.setIdFiche(ficheLoi.getDocument().getId());

		return navette;
	}

}
