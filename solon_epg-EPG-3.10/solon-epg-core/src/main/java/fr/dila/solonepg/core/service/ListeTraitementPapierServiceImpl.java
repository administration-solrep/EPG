package fr.dila.solonepg.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.nuxeo.ecm.core.query.sql.model.DateLiteral;

import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.DonneesSignataire;
import fr.dila.solonepg.api.cases.typescomplexe.InfoEpreuve;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants;
import fr.dila.solonepg.api.constant.SolonEpgTraitementPapierConstants;
import fr.dila.solonepg.api.documentmodel.ListeTraitementPapier;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ListeTraitementPapierService;
import fr.dila.solonepg.core.cases.typescomplexe.DonneesSignataireImpl;
import fr.dila.solonepg.core.cases.typescomplexe.InfoEpreuveImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;

/**
 * classe d'implemetation de la liste de traitement Papier
 * 
 * @author admin
 */
public class ListeTraitementPapierServiceImpl implements ListeTraitementPapierService {

	private static final STLogger	LOGGER	= STLogFactory.getLog(ListeTraitementPapierServiceImpl.class);

	/**
	 * recuperer le nombre de liste de traitement papier
	 * 
	 * @param session
	 *            la session
	 * @param typeListe
	 *            type de la liste
	 * @param typeSignature
	 *            type de signature
	 * @return le nombre de liste de traitement papier
	 * @throws ClientException
	 */
	private Long getListeTraitementPapierCount(final CoreSession session, final String typeListe,
			final String typeSignature) throws ClientException {
		Calendar dua = Calendar.getInstance();
		final String currentDate = DateLiteral.dateFormatter.print(dua.getTimeInMillis());
		dua = Calendar.getInstance();
		dua.add(Calendar.DAY_OF_YEAR, +1);
		final String tomorrowLiteralDate = DateLiteral.dateFormatter.print(dua.getTimeInMillis());

		final StringBuilder query = new StringBuilder("SELECT * FROM ListeTraitementPapier WHERE ");

		query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
		query.append(":");
		query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_TYPE_LISTE_PROPERTY);
		query.append("='");
		query.append(typeListe);
		query.append("' and ");
		query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
		query.append(":");
		query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DATE_GENERATION_LISTE_PROPERTY);
		query.append("< TIMESTAMP '");
		query.append(tomorrowLiteralDate);
		query.append(" ' and ");
		query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
		query.append(":");
		query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DATE_GENERATION_LISTE_PROPERTY);
		query.append(">= TIMESTAMP '");
		query.append(currentDate);
		query.append(" ' ");
		query.append(" and ecm:isProxy = 0 ");

		if (StringUtils.isNotBlank(typeSignature)) {
			query.append(" and ");
			query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
			query.append(":");
			query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_TYPE_SIGNATURE);
			query.append("='");
			query.append(typeSignature);
			query.append("' ");
		}
		return QueryUtils.doCountQuery(session, query.toString());
	}

	/**
	 * recuperer la liste de traitement papier an basant sur le dossier et la type
	 * 
	 * @param session
	 *            la session
	 * @param docId
	 *            le dossier Id
	 * @param typeListe
	 *            type de la liste
	 * @return
	 * @throws ClientException
	 */
	@Override
	public DocumentModelList getListeTraitementPapierOfDossierAndType(final CoreSession session, final String docId,
			final String typeListe) throws ClientException {

		final StringBuilder query = new StringBuilder("SELECT * FROM ListeTraitementPapier WHERE ");
		query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
		query.append(":");
		query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_TYPE_LISTE_PROPERTY);
		query.append("='");
		query.append(typeListe);
		query.append("' and ");
		query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
		query.append(":");
		query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_IDS_DOSSIER_PROPERTY);
		query.append("='" + docId);
		query.append("'");
		query.append(" and ecm:isProxy = 0 ");

		return session.query(query.toString());
	}

	/**
	 * recuperer la liste de traitement papier an basant sur la liste dossiers et la type
	 * 
	 * @param session
	 *            la session
	 * @param docsId
	 *            la liste des dossier Id
	 * @param typeListe
	 *            type de la liste
	 * @return
	 * @throws ClientException
	 */
	@Override
	public DocumentModelList getListeTraitementPapierOfDossierListAndType(final CoreSession session,
			final List<String> docsId, final String typeListe) throws ClientException {

		final StringBuilder query = new StringBuilder("SELECT * FROM ListeTraitementPapier WHERE ");
		query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
		query.append(":");
		query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_TYPE_LISTE_PROPERTY);
		query.append("='");
		query.append(typeListe);
		query.append("' and ");
		query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
		query.append(":");
		query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_IDS_DOSSIER_PROPERTY);
		query.append("  IN ('");
		query.append(StringUtils.join(docsId, "','"));
		query.append("')");
		query.append(" and ecm:isProxy = 0 ");

		return session.query(query.toString());
	}

	/**
	 * creer une Nouvelle Liste de Traitement Papier pour Mise en signature
	 * 
	 * @param session
	 *            la session
	 * @param documentIdsList
	 *            la liste des dossiers de la lite a creer
	 * @param typeSignature
	 *            type de signataire
	 * @throws ClientException
	 */
	@Override
	public void creerNouvelleListeTraitementPapierMiseEnSignature(final CoreSession session,
			final String typeSignataire, final List<DocumentModel> documentsList) throws ClientException {

		final List<String> docIdsList = new ArrayList<String>();

		for (final DocumentModel dossierDoc : documentsList) {
			docIdsList.add(dossierDoc.getId());
		}

		creerNouvelleListeTraitementPapier(session,
				SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_MISE_EN_SIGNATURE, typeSignataire,
				docIdsList);
		initDonneesSignataireUnrestricted(session, documentsList, typeSignataire);
	}

	public void suprimerDossiersDeListeTraitementPapier(final CoreSession session, final String listId) {

	}

	/**
	 * creer une Nouvelle Liste de Traitement Papier pour Demandes D'epreuves
	 * 
	 * @param session
	 *            la session
	 * @param documentIdsList
	 *            la liste des dossiers de la lite a creer
	 * @throws ClientException
	 */
	@Override
	public void creerNouvelleListeTraitementPapierDemandesEpreuve(final CoreSession session,
			final List<DocumentModel> documentsList) throws ClientException {

		final List<String> docIdsList = new ArrayList<String>();
		for (final DocumentModel dossierDoc : documentsList) {
			docIdsList.add(dossierDoc.getId());
		}
		final long numeroDeListe = creerNouvelleListeTraitementPapier(session,
				SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE, "", docIdsList);
		initDonneesDemandesEpreuvesUnrestricted(session, documentsList,
				SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE_LABEL + numeroDeListe);
	}

	/**
	 * creer une Nouvelle Liste de Traitement Papier pour Demandes De Publication
	 * 
	 * @param session
	 *            la session
	 * @param documentIdsList
	 *            la liste des dossiers de la lite a creer
	 * @throws ClientException
	 */
	@Override
	public void creerNouvelleListeTraitementPapierDemandesDePublication(final CoreSession session,
			final List<DocumentModel> documentsList) throws ClientException {
		final List<String> docIdsList = new ArrayList<String>();

		for (final DocumentModel dossierDoc : documentsList) {
			docIdsList.add(dossierDoc.getId());
		}
		final long numeroDeListe = creerNouvelleListeTraitementPapier(session,
				SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION, "", docIdsList);
		initDonneesDemandePublicationUnrestricted(session, documentsList,
				SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION_LABEL
						+ numeroDeListe);
	}

	/**
	 * Traiter en serie les dates des dossiers pour la liste de traitement de papiers En signature SGG
	 * 
	 * @param session
	 *            la session
	 * @param docList
	 *            the list de dossirs a traiter
	 * @param property
	 *            le nom de la propreiete a traiter en serie
	 * @param value
	 *            la nouvelle date
	 * @throws ClientException
	 */
	@Override
	public void traiterEnSerieSignataireSGGUnrestricted(final CoreSession session, final List<DocumentModel> docList,
			final Calendar value) throws ClientException {
		new UnrestrictedSessionRunner(session) {
			@Override
			public void run() throws ClientException {
				for (final DocumentModel documentModel : docList) {
					final TraitementPapier traitementPapier = documentModel.getAdapter(TraitementPapier.class);
					final DonneesSignataire donneesSignataire = traitementPapier.getSignatureSgg();
					donneesSignataire.setDateRetourSignature(value);
					traitementPapier.setSignatureSgg(donneesSignataire);
					traitementPapier.save(session);
				}
				session.save();
			}
		}.runUnrestricted();
	}

	/**
	 * Traiter en serie les dates des dossiers pour la liste de traitement de papiers En signature PM
	 * 
	 * @param session
	 *            la session
	 * @param docList
	 *            the list de dossirs a traiter
	 * @param property
	 *            le nom de la propreiete a traiter en serie
	 * @param value
	 *            la nouvelle date
	 * @throws ClientException
	 */
	@Override
	public void traiterEnSerieSignatairePMUnrestricted(final CoreSession session, final List<DocumentModel> docList,
			final Calendar value) throws ClientException {
		new UnrestrictedSessionRunner(session) {
			@Override
			public void run() throws ClientException {
				for (final DocumentModel documentModel : docList) {
					final TraitementPapier traitementPapier = documentModel.getAdapter(TraitementPapier.class);
					final DonneesSignataire donneesSignataire = traitementPapier.getSignaturePm();
					donneesSignataire.setDateRetourSignature(value);
					traitementPapier.setSignaturePm(donneesSignataire);
					traitementPapier.save(session);
				}
				session.save();
			}
		}.runUnrestricted();
	}

	/**
	 * Traiter en serie les dates des dossiers pour la liste de traitement de papiers En signature PR
	 * 
	 * @param session
	 *            la session
	 * @param docList
	 *            the list de dossirs a traiter
	 * @param property
	 *            le nom de la propreiete a traiter en serie
	 * @param value
	 *            la nouvelle date
	 * @throws ClientException
	 */
	@Override
	public void traiterEnSerieSignatairePRUnrestricted(final CoreSession session, final List<DocumentModel> docList,
			final Calendar dateRetour, final Calendar dateEnvoi) throws ClientException {
		new UnrestrictedSessionRunner(session) {
			@Override
			public void run() throws ClientException {
				for (final DocumentModel documentModel : docList) {
					final TraitementPapier traitementPapier = documentModel.getAdapter(TraitementPapier.class);
					final DonneesSignataire donneesSignataire = traitementPapier.getSignaturePr();
					if (dateRetour != null) {
						donneesSignataire.setDateRetourSignature(dateRetour);
					}
					if (dateEnvoi != null) {
						donneesSignataire.setDateEnvoiSignature(dateEnvoi);
					}
					traitementPapier.setSignaturePr(donneesSignataire);
					traitementPapier.save(session);

					final EventProducer eventProducer = STServiceLocator.getEventProducer();
					final Map<String, Serializable> eventPropertiesFicheDR = new HashMap<String, Serializable>();
					eventPropertiesFicheDR.put(SolonEpgEventConstant.DOSSIER_EVENT_PARAM, documentModel);
					eventPropertiesFicheDR.put(SolonEpgEventConstant.TYPE_ACTE_EVENT_PARAM,
							SolonEpgEventConstant.TYPE_ACTE_FL_EVENT_VALUE);

					final InlineEventContext inlineEventContext = new InlineEventContext(session,
							session.getPrincipal(), eventPropertiesFicheDR);

					eventProducer.fireEvent(inlineEventContext
							.newEvent(SolonEpgEventConstant.BATCH_EVENT_UPDATE_FICHE_AFTER_UPDATE_BORDEREAU));

				}
				session.save();

			}
		}.runUnrestricted();
	}

	/**
	 * Traiter en serie les dates des dossiers pour la liste de traitement de papiers demande d'epreuve
	 * 
	 * @param session
	 *            la session
	 * @param docList
	 *            the list de dossirs a traiter
	 * @param property
	 *            le nom de la propreiete a traiter en serie
	 * @param value
	 *            la nouvelle date pour le
	 * @throws ClientException
	 */
	@Override
	public void traiterEnSerieDemandeEpreuveUnrestricted(final CoreSession session, final List<DocumentModel> docList,
			final Calendar value) throws ClientException {
		new UnrestrictedSessionRunner(session) {
			@Override
			public void run() throws ClientException {
				for (final DocumentModel documentModel : docList) {
					final TraitementPapier traitementPapier = documentModel.getAdapter(TraitementPapier.class);
					InfoEpreuve infoEpreuve = traitementPapier.getEpreuve();
					final InfoEpreuve infoNouvelleDemandeEpreuve = traitementPapier.getNouvelleDemandeEpreuves();

					if (infoEpreuve == null) {
						infoEpreuve = new InfoEpreuveImpl();
					}
					if (infoNouvelleDemandeEpreuve == null || infoNouvelleDemandeEpreuve.getEpreuveDemandeLe() == null) {
						infoEpreuve.setEpreuvePourLe(value);
						traitementPapier.setEpreuve(infoEpreuve);
						traitementPapier.save(session);
					} else {
						infoNouvelleDemandeEpreuve.setEpreuvePourLe(value);
						traitementPapier.setNouvelleDemandeEpreuves(infoNouvelleDemandeEpreuve);
						traitementPapier.save(session);
					}
				}
				session.save();
			}
		}.runUnrestricted();
	}

	/**
	 * creer une Nouvelle Liste de Traitement Papier
	 * 
	 * @param session
	 * @param typeListe
	 * @param typeSignataire
	 * @param documentIdsList
	 * @throws ClientException
	 */
	private long creerNouvelleListeTraitementPapier(final CoreSession session, final String typeListe,
			String typeSignataire, final List<String> documentIdsList) throws ClientException {

		if (typeSignataire != null && !typeSignataire.trim().equals("")) {
			if (typeSignataire.equals(SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_SGG)) {
				typeSignataire = "SGG";
			} else if (typeSignataire.equals(SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_PM)) {
				typeSignataire = "PM";
			} else if (typeSignataire.equals(SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_PR)) {
				typeSignataire = "PR";
			} else if (typeSignataire.equals(SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_AUCUN)) {
				typeSignataire = "";
			}
		}
		final Long listeTraitementPapierCount = getListeTraitementPapierCount(session, typeListe, typeSignataire);
		final Long numeroDeListe = listeTraitementPapierCount + 1;

		final DocumentModel listeTraitementPapierModel = new DocumentModelImpl(
				SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_PATH, UUID.randomUUID().toString(),
				SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DOCUMENT_TYPE);

		final ListeTraitementPapier listeTraitementPapier = listeTraitementPapierModel
				.getAdapter(ListeTraitementPapier.class);

		final Calendar calendar = Calendar.getInstance();

		listeTraitementPapier.setDateGenerationListe(calendar);
		listeTraitementPapier.setNumeroListe(numeroDeListe);
		listeTraitementPapier.setTypeListe(typeListe);
		listeTraitementPapier.setTypeSignature(typeSignataire);
		listeTraitementPapier.setIdsDossier(documentIdsList);
		session.createDocument(listeTraitementPapier.getDocument());
		session.save();
		return numeroDeListe;
	}

	/**
	 * initialises donnees signatures
	 * 
	 * @param session
	 * @param docList
	 * @param typeSignataire
	 * @throws ClientException
	 */
	private void initDonneesSignataireUnrestricted(final CoreSession session, final List<DocumentModel> docList,
			final String typeSignataire) throws ClientException {
		new UnrestrictedSessionRunner(session) {
			@Override
			public void run() throws ClientException {
				final DonneesSignataire donneesSignataire = new DonneesSignataireImpl();
				donneesSignataire.setDateEnvoiSignature(Calendar.getInstance());

				if (typeSignataire.equals(SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_SGG)) {
					for (final DocumentModel dossierDoc : docList) {
						final TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
						traitementPapier.setSignatureSgg(donneesSignataire);
						traitementPapier.save(session);
					}
				} else if (typeSignataire.equals(SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_PM)) {
					for (final DocumentModel dossierDoc : docList) {
						final TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
						traitementPapier.setSignaturePm(donneesSignataire);
						traitementPapier.save(session);
					}
				}
				session.save();
			}
		}.runUnrestricted();
	}

	/**
	 * initialiser les donnes pour demandes epreuves
	 * 
	 * @param session
	 * @param docList
	 * @param numeroDeListe
	 * @throws ClientException
	 */
	private void initDonneesDemandesEpreuvesUnrestricted(final CoreSession session, final List<DocumentModel> docList,
			final String numeroDeListe) throws ClientException {

		new UnrestrictedSessionRunner(session) {
			@Override
			public void run() throws ClientException {
				for (final DocumentModel dossierDoc : docList) {
					final TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
					final InfoEpreuve infoEpreuve = new InfoEpreuveImpl();
					infoEpreuve.setEpreuveDemandeLe(Calendar.getInstance());
					infoEpreuve.setNumeroListe(numeroDeListe);
					if (traitementPapier.getEpreuve() == null
							|| traitementPapier.getEpreuve().getEpreuveDemandeLe() == null) {
						traitementPapier.setEpreuve(infoEpreuve);
						traitementPapier.save(session);
					} else {
						traitementPapier.setNouvelleDemandeEpreuves(infoEpreuve);
						traitementPapier.save(session);
					}
				}
				session.save();
			}
		}.runUnrestricted();
		// infoEpreuve.setDestinataireEntete(DestinataireEntete);
	}

	/**
	 * initialiser les donnes pour demandes de publication
	 * 
	 * @param session
	 * @param docList
	 * @param numeroDeListe
	 * @throws ClientException
	 */
	private void initDonneesDemandePublicationUnrestricted(final CoreSession session,
			final List<DocumentModel> docList, final String numeroDeListe) throws ClientException {

		new UnrestrictedSessionRunner(session) {
			@Override
			public void run() throws ClientException {
				for (final DocumentModel dossierDoc : docList) {
					final TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
					traitementPapier.setPublicationDateEnvoiJo(Calendar.getInstance());
					traitementPapier.setPublicationNumeroListe(numeroDeListe);
					traitementPapier.save(session);
				}
				session.save();
			}
		}.runUnrestricted();
		// infoEpreuve.setDestinataireEntete(DestinataireEntete);
	}

	/**
	 * Supprimer des dossiers de la liste, supprimer la liste si tous les dossiers sont supprimer
	 * 
	 * @param listeTraitementPapier
	 * @param docs
	 * @param session
	 * @throws ClientException
	 */
	@Override
	public void supprimerDossiersDeLaListeUnrestricted(final ListeTraitementPapier listeTraitementPapier,
			final List<DocumentModel> docs, final CoreSession session) throws ClientException {
		new UnrestrictedSessionRunner(session) {
			@Override
			public void run() throws ClientException {
				final List<String> idsDossier = listeTraitementPapier.getIdsDossier();
				final List<String> newDocsList = new ArrayList<String>();
				boolean remove = false;
				for (final String dossierId : idsDossier) {
					for (final DocumentModel dossier : docs) {
						if (dossier.getId().equals(dossierId)) {
							remove = true;
							break;
						}
					}
					if (!remove) {
						newDocsList.add(dossierId);
					}
					remove = false;
				}
				listeTraitementPapier.setIdsDossier(newDocsList);
				if (newDocsList.size() == 0) {
					LOGGER.info(session, EpgLogEnumImpl.DEL_LST_TRAITEMENT_PAPIER_TEC,
							listeTraitementPapier.getDocument());
					session.removeDocument(listeTraitementPapier.getDocument().getRef());

				} else {
					session.saveDocument(listeTraitementPapier.getDocument());
				}
				session.save();
			}
		}.runUnrestricted();
	}
}
