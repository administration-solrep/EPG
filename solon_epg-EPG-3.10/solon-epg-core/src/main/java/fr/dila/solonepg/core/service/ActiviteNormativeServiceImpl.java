package fr.dila.solonepg.core.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.schema.PrefetchInfo;
import org.nuxeo.runtime.transaction.TransactionHelper;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.Habilitation;
import fr.dila.solonepg.api.cases.LigneProgrammation;
import fr.dila.solonepg.api.cases.LoiDeRatification;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.Ordonnance;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.cases.Traite;
import fr.dila.solonepg.api.cases.typescomplexe.LigneProgrammationHabilitation;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.constant.ActiviteNormativeProgrammationConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.LigneProgrammationConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.dto.DecretApplicationDTO;
import fr.dila.solonepg.api.dto.DecretDTO;
import fr.dila.solonepg.api.dto.HabilitationDTO;
import fr.dila.solonepg.api.dto.LoiDeRatificationDTO;
import fr.dila.solonepg.api.dto.MesureApplicativeDTO;
import fr.dila.solonepg.api.dto.OrdonnanceDTO;
import fr.dila.solonepg.api.dto.OrdonnanceHabilitationDTO;
import fr.dila.solonepg.api.dto.TexteMaitreLoiDTO;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.api.exception.ActiviteNormativeException;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.api.service.HistoriqueMajMinisterielleService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.core.dto.activitenormative.LigneProgrammationDTO;
import fr.dila.solonepg.core.dto.activitenormative.TableauDeProgrammationLoiDTO;
import fr.dila.solonepg.core.dto.activitenormative.injectionbdj.BilanSemestrielDTO;
import fr.dila.solonepg.core.dto.activitenormative.injectionbdj.BilanSemestrielDTO.BilanSemestrielType;
import fr.dila.solonepg.core.dto.activitenormative.injectionbdj.DecretApplicatifDTO;
import fr.dila.solonepg.core.dto.activitenormative.injectionbdj.EcheancierLoisDTO;
import fr.dila.solonepg.core.dto.activitenormative.injectionbdj.EcheancierWrapper;
import fr.dila.solonepg.core.dto.activitenormative.injectionbdj.MesureApplicationDTO;
import fr.dila.solonepg.core.dto.activitenormative.injectionbdj.TexteBilanSemestrielDTO;
import fr.dila.solonepg.core.dto.activitenormative.injectionbdj.TexteMaitreDTO;
import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STConfigConstants;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.StringUtil;

/**
 * Service de gestion de l'activite normative
 * 
 * @author asatre
 * 
 */
public class ActiviteNormativeServiceImpl implements ActiviteNormativeService {
    
	private static final long			serialVersionUID							= -2349302307195733123L;
	private static final Pattern		datePattern									= Pattern
																							.compile(
																									"\\b(([ 012]\\d|3[01])\\s{1}\\p{L}*\\s{1}\\d{4})\\b",
																									Pattern.CANON_EQ);
	private static final Double			MILISECOND_PER_MONTH						= 24 * 60 * 60 * 1000 * 30.0;

	/**
	 * Logger formalisé en surcouche du logger apache/log4j
	 */
	private static final STLogger		LOGGER										= STLogFactory
																							.getLog(ActiviteNormativeServiceImpl.class);
	
	/** Logger utilisé pour l'injection BDJ */
	private static final Logger LOGGER_INJECTION_BDJ = Logger.getLogger("LOGGER-INJECTION-BDJ");

	private static final String			SELECT_UUID_FROM_ACT_NORM_AS_A				= "SELECT a.ecm:uuid as id FROM "
																							+ ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE
																							+ " as a ";

	private static final String			QUERY_ACT_NORMATIVE_BY_NUMERO				= SELECT_UUID_FROM_ACT_NORM_AS_A
																							+ " WHERE a."
																							+ TexteMaitreConstants.TEXTE_MAITRE_PREFIX
																							+ ":"
																							+ TexteMaitreConstants.NUMERO
																							+ " = ? ";

	private static final String			QUERY_ACT_NORMATIVE_BY_TITRE_AND_DATE_SIGN	= SELECT_UUID_FROM_ACT_NORM_AS_A
																							+ " WHERE a."
																							+ TexteMaitreConstants.TEXTE_MAITRE_PREFIX
																							+ ":"
																							+ TexteMaitreConstants.TITRE_ACTE
																							+ " = ? AND a."
																							+ TexteMaitreConstants.TEXTE_MAITRE_PREFIX
																							+ ":"
																							+ TexteMaitreConstants.DATE_SIGNATURE
																							+ " = ? ";

	private static final String			QUERY_ACT_NORMATIVE_BY_NOR					= SELECT_UUID_FROM_ACT_NORM_AS_A
																							+ " WHERE a."
																							+ TexteMaitreConstants.TEXTE_MAITRE_PREFIX
																							+ ":"
																							+ TexteMaitreConstants.NUMERO_NOR
																							+ " = ? ";

	private static final PrefetchInfo	PREFECTH_INFO_CHGT_GVT						= new PrefetchInfo(StringUtil.join(
																							new String[] {
			TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA,
			ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA }, ","));

	private static final PrefetchInfo	PREFETCH_INFO_TEXTE_MAITRE					= new PrefetchInfo(
																							TexteMaitreConstants.TEXTE_MAITRE_SCHEMA);

	private static final SimpleDateFormat formaterXML = new SimpleDateFormat("yyyy-MM-dd"); // 2018-12-31
	
	private static final SimpleDateFormat formaterFR = new SimpleDateFormat("dd/MM/yyyy"); // 31/12/2018
	
	private static final SimpleDateFormat formaterFREtendu = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH); // 31 décembre 2018

	@Override
	public void addDossierToTableauMaitre(final String numeroNorDossier, final String type, final CoreSession session)
			throws ClientException {
		addDossierToTableau(numeroNorDossier, type, session);
	}

	private void addDossierToTableau(final String numeroNorDossier, final String type, final CoreSession session)
			throws ClientException, ActiviteNormativeException {

		ActiviteNormativeEnum activiteNormativeEnum = ActiviteNormativeEnum.getByType(type);
		TexteMaitre texteMaitre = null;

		switch (activiteNormativeEnum) {
			case TRAITES_ET_ACCORDS:
				throw new ActiviteNormativeException("Ajout impossible par ce menu.");
			case TRANSPOSITION:
				texteMaitre = createActiviteNormative(activiteNormativeEnum, null, session, numeroNorDossier);
				texteMaitre.setNumero(numeroNorDossier);
				texteMaitre.save(session);
				// Ajout dans le journal du PAN
				STServiceLocator.getJournalService().journaliserActionPAN(session, null,
						SolonEpgEventConstant.AJOUT_TM_EVENT,
						SolonEpgEventConstant.AJOUT_TM_COMMENT + " [" + texteMaitre.getNumero() + "]",
						SolonEpgEventConstant.CATEGORY_LOG_PAN_TRANSPO_DIRECTIVES);
				break;
			default:
				final Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(session,
						numeroNorDossier);
				if (dossier == null) {
					throw new ActiviteNormativeException("activite.normative.dossier.not.found");
				}
				activiteNormativeEnum = checkCompatibility(dossier, type);
				texteMaitre = createActiviteNormative(activiteNormativeEnum, dossier, session, numeroNorDossier);
				texteMaitre.save(session);
				// Ajout dans le journal du PAN
				String category;
				if (ActiviteNormativeEnum.APPICATION_DES_LOIS.getTypeActiviteNormative().equals(type)
						|| ActiviteNormativeEnum.APPLICATION_DES_LOIS_MINISTERE.getTypeActiviteNormative().equals(type)) {
					category = SolonEpgEventConstant.CATEGORY_LOG_PAN_APPLICATION_LOIS;
				} else if (ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES.getTypeActiviteNormative().equals(type)
						|| ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES_MINISTERE.getTypeActiviteNormative()
								.equals(type)) {
					category = SolonEpgEventConstant.CATEGORY_LOG_PAN_APPLICATION_ORDO;
				} else if (ActiviteNormativeEnum.ORDONNANCES_38C.getTypeActiviteNormative().equals(type)) {
					category = SolonEpgEventConstant.CATEGORY_LOG_PAN_SUIVI_HABILITATIONS;
				} else if (ActiviteNormativeEnum.ORDONNANCES.getTypeActiviteNormative().equals(type)) {
					category = SolonEpgEventConstant.CATEGORY_LOG_PAN_RATIFICATION_ORDO;
				} else {
					break;
				}
				STServiceLocator.getJournalService().journaliserActionPAN(session,
						dossier.getDocument().getRef().toString(), SolonEpgEventConstant.AJOUT_TM_EVENT,
						SolonEpgEventConstant.AJOUT_TM_COMMENT, category);
				break;
		}

		session.save();
	}

	@Override
	public void addTraiteToTableauMaitre(final String titre, final Date dateSignature, final Boolean publication,
			final CoreSession session) throws ClientException {

		final Calendar cal = Calendar.getInstance();
		cal.setTime(dateSignature);

		final ActiviteNormative activiteNormative = findOrCreateActiviteNormativeByTitreAndDateSignature(session,
				titre, cal);

		activiteNormative.setTraite(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE);

		final TexteMaitre texteMaitre = createTexteMaitreDefault(activiteNormative, null);
		final Traite traite = texteMaitre.getAdapter(Traite.class);

		traite.setDateSignature(cal);
		traite.setPublication(publication);

		texteMaitre.save(session);

	}

	private TexteMaitre createActiviteNormative(final ActiviteNormativeEnum activiteNormativeEnum,
			final Dossier dossier, final CoreSession session, final String numeroNor) throws ClientException {

		ActiviteNormative activiteNormative = findOrCreateActiviteNormativeByNor(session, numeroNor);

		switch (activiteNormativeEnum) {
			case APPICATION_DES_LOIS:
				if (ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE.equals(activiteNormative.getApplicationLoi())) {
					throw new ActiviteNormativeException("activite.normative.dossier.alreadypresent");
				} else {
					activiteNormative.setApplicationLoi(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE);
				}
				break;
			case APPLICATION_DES_ORDONNANCES:
				if (ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE.equals(activiteNormative
						.getApplicationOrdonnance())) {
					throw new ActiviteNormativeException("activite.normative.dossier.alreadypresent");
				} else {
					activiteNormative.setApplicationOrdonnance(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE);
					final TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);
					if (dossier.isDispositionHabilitation() == null || dossier.isDispositionHabilitation()) {
						texteMaitre.setDispositionHabilitation(true);
					} else {
						texteMaitre.setDispositionHabilitation(false);
					}
				}
				break;
			case ORDONNANCES:
				if (ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE.equals(activiteNormative.getOrdonnance())) {
					throw new ActiviteNormativeException("activite.normative.dossier.alreadypresent");
				} else {
					activiteNormative.setOrdonnance(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE);
				}
				break;
			case TRAITES_ET_ACCORDS:
				if (ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE.equals(activiteNormative.getOrdonnance())) {
					throw new ActiviteNormativeException("activite.normative.dossier.alreadypresent");
				} else {
					activiteNormative.setOrdonnance(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE);
				}
				break;
			case TRANSPOSITION:
				if (ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE.equals(activiteNormative.getTransposition())) {
					throw new ActiviteNormativeException("activite.normative.dossier.alreadypresent");
				} else {
					activiteNormative.setTransposition(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE);
				}
				break;
			case ORDONNANCES_38C:
				if (ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE.equals(activiteNormative.getOrdonnance38C())) {
					throw new ActiviteNormativeException("activite.normative.dossier.alreadypresent");
				} else {
					activiteNormative.setOrdonnance38C(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE);
				}
				break;
			default:
				// par defaut exception car le type n'est pas bon
				throw new ActiviteNormativeException("activite.normative.type.not.found");
		}

		return createTexteMaitreDefault(activiteNormative, dossier);

	}

	/**
	 * créé un document ActiviteNormative non encore persisté (nécessite un session.createDocument par la suite)
	 * 
	 * @param session
	 * @return DocumentModel le document de type ActiviteNormative non encore persisté
	 * @throws ClientException
	 */
	private DocumentModel createBareActiviteNormative(final CoreSession session) throws ClientException {
		final DocumentModel modelDesired = session
				.createDocumentModel(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE);
		modelDesired.setPathInfo(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_PATH, UUID.randomUUID().toString());
		return modelDesired;
	}

	private ActiviteNormative findOrCreateActiviteNormativeByTitreAndDateSignature(final CoreSession session,
			final String titre, final Calendar dateSignature) throws ClientException {

		DocumentModel modelDesired = null;

		final List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE,
				QUERY_ACT_NORMATIVE_BY_TITRE_AND_DATE_SIGN, new Object[] { titre, dateSignature }, 1, 0);

		if (list == null || list.isEmpty()) {
			modelDesired = session.createDocumentModel(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE);
			modelDesired.setPathInfo(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_PATH, UUID.randomUUID().toString());

			final TexteMaitre texteMaitre = modelDesired.getAdapter(TexteMaitre.class);
			texteMaitre.setTitreActe(titre);

			modelDesired = session.createDocument(texteMaitre.getDocument());
		} else {
			modelDesired = list.get(0);
		}

		return modelDesired.getAdapter(ActiviteNormative.class);
	}

	@Override
	public DocumentModel findActiviteNormativeDocByNumero(final CoreSession session, final String numero)
			throws ClientException {
		final List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE, QUERY_ACT_NORMATIVE_BY_NUMERO,
				new Object[] { numero }, 1, 0);
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

	private ActiviteNormative findOrCreateActiviteNormativeByNumero(final CoreSession session, final String numero)
			throws ClientException {
		DocumentModel modelDesired = null;
		modelDesired = findActiviteNormativeDocByNumero(session, numero);
		if (modelDesired == null) {
			modelDesired = session.createDocumentModel(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE);
			modelDesired.setPathInfo(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_PATH, UUID.randomUUID().toString());

			final TexteMaitre texteMaitre = modelDesired.getAdapter(TexteMaitre.class);
			texteMaitre.setNumero(numero);

			modelDesired = session.createDocument(texteMaitre.getDocument());

			session.save();
		}
		return modelDesired.getAdapter(ActiviteNormative.class);
	}

	@Override
	public ActiviteNormative findOrCreateActiviteNormativeByNor(final CoreSession session, final String numeroNor)
			throws ClientException {
		DocumentModel modelDesired = null;

		final List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE, QUERY_ACT_NORMATIVE_BY_NOR,
				new Object[] { numeroNor }, 1, 0);

		if (list == null || list.isEmpty()) {
			modelDesired = session.createDocumentModel(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE);
			modelDesired.setPathInfo(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_PATH, UUID.randomUUID().toString());

			final TexteMaitre texteMaitre = modelDesired.getAdapter(TexteMaitre.class);
			texteMaitre.setNumeroNor(numeroNor);
			texteMaitre.setValidation(Boolean.TRUE);

			modelDesired = session.createDocument(texteMaitre.getDocument());

			session.save();
		} else {
			modelDesired = list.get(0);
		}

		return modelDesired.getAdapter(ActiviteNormative.class);
	}

	/**
	 * Récupère une mesure d'habilitation pour une loi donnée (idActiviteNormative)
	 * 
	 * @param session
	 * @param numeroOrdre
	 *            : le numero d'ordre de la mesure : peut être null
	 * @param article
	 *            : l'article liée à la mesure obligatoire
	 * @param idActiviteNormative
	 *            : l'id du doc activiteNormative representant la loi à laquelle la mesure est rattachée
	 * @return
	 * @throws ClientException
	 */
	private Habilitation findMesureHabilitationForLoi(final CoreSession session, final String numeroOrdre,
			final String idActiviteNormative) throws ClientException {

		final StringBuilder queryBuilder = new StringBuilder(SELECT_UUID_FROM_ACT_NORM_AS_A);
		queryBuilder.append(" WHERE a.").append(TexteMaitreConstants.TEXTE_MAITRE_PREFIX).append(":")
				.append(TexteMaitreConstants.NUMERO_ORDRE).append(" = ? AND a.")
				.append(TexteMaitreConstants.TEXTE_MAITRE_PREFIX).append(":").append(TexteMaitreConstants.ID_DOSSIER)
				.append(" = ? AND a.").append(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_PREFIX).append(":")
				.append(ActiviteNormativeConstants.ATTRIBUT_ORDONNANCE_38C).append(" = ? ");

		DocumentModel modelDesired = null;

		// Create Query Paramerter
		final Object[] parameters = new Object[3];
		parameters[0] = numeroOrdre;
		parameters[1] = idActiviteNormative;
		parameters[2] = ActiviteNormativeConstants.MESURE_FILTER;

		final List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE, queryBuilder.toString(), parameters, 2, 0);

		if (list == null || list.isEmpty() || list.size() > 1) {
			return null;
		} else {
			modelDesired = list.get(0);
		}

		return modelDesired.getAdapter(Habilitation.class);
	}

	/**
	 * Recherche un document de l'activite normative par rapport au numero d'ordre, et de l'id dossier Utilisé pour
	 * retrouver les mesures d'application d'une loi
	 * 
	 * @param session
	 * @param numeroOrdre
	 * @param idActiviteNormative
	 * @return le modèle de la mesure si elle existe. Si plusieurs mesures existent pour le numero d'ordre et l'id
	 *         donné, on renvoit null. On renvoit null également si rien n'existe
	 * @throws ClientException
	 */
	private MesureApplicative findMesureApplicativeForLoi(final CoreSession session, final String numeroOrdre,
			final String idActiviteNormative) throws ClientException {
		final StringBuilder queryBuilder = new StringBuilder(SELECT_UUID_FROM_ACT_NORM_AS_A);
		queryBuilder.append(" WHERE a.").append(TexteMaitreConstants.TEXTE_MAITRE_PREFIX).append(":")
				.append(TexteMaitreConstants.NUMERO_ORDRE).append(" = ? AND a.")
				.append(TexteMaitreConstants.TEXTE_MAITRE_PREFIX).append(":").append(TexteMaitreConstants.ID_DOSSIER)
				.append(" = ? AND a.").append(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_PREFIX).append(":")
				.append(ActiviteNormativeConstants.ATTRIBUT_APPLICATION_LOIS).append(" = ? ");

		DocumentModel modelDesired = null;

		final List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE, queryBuilder.toString(), new Object[] {
						numeroOrdre, idActiviteNormative, ActiviteNormativeConstants.MESURE_FILTER }, 2, 0);

		if (list == null || list.isEmpty() || list.size() > 1) {
			return null;
		} else {
			modelDesired = list.get(0);
		}

		return modelDesired.getAdapter(MesureApplicative.class);
	}

	/**
	 * Recherche un document de l'activite normative par rapport au numero d'ordre, et de l'id dossier Utilisé pour
	 * retrouver les mesures d'application d'une ordonnance
	 * 
	 * @param session
	 * @param numeroOrdre
	 * @param idActiviteNormative
	 * @return le modèle de la mesure si elle existe. Si plusieurs mesures existent pour le numero d'ordre et l'id
	 *         donné, on renvoit null. On renvoit null également si rien n'existe
	 * @throws ClientException
	 */
	private MesureApplicative findMesureApplicativeForOrdonnance(final CoreSession session, final String numeroOrdre,
			final String idActiviteNormative) throws ClientException {
		final StringBuilder queryBuilder = new StringBuilder(SELECT_UUID_FROM_ACT_NORM_AS_A);
		queryBuilder.append(" WHERE a.").append(TexteMaitreConstants.TEXTE_MAITRE_PREFIX).append(":")
				.append(TexteMaitreConstants.NUMERO_ORDRE).append(" = ? AND a.")
				.append(TexteMaitreConstants.TEXTE_MAITRE_PREFIX).append(":").append(TexteMaitreConstants.ID_DOSSIER)
				.append(" = ? AND a.").append(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_PREFIX).append(":")
				.append(ActiviteNormativeConstants.ATTRIBUT_APPLICATION_ORDONNANCES).append(" = ? ");

		DocumentModel modelDesired = null;

		final List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE, queryBuilder.toString(), new Object[] {
						numeroOrdre, idActiviteNormative, ActiviteNormativeConstants.MESURE_FILTER }, 2, 0);

		if (list == null || list.isEmpty() || list.size() > 1) {
			return null;
		} else {
			modelDesired = list.get(0);
		}

		return modelDesired.getAdapter(MesureApplicative.class);
	}

	private TexteMaitre createTexteMaitreDefault(final ActiviteNormative activiteNormative, final Dossier dossier) {
		final TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);

		if (dossier != null) {
			texteMaitre.setTitreActe(dossier.getTitreActe());
			texteMaitre.setMinisterePilote(dossier.getMinistereResp());

			final RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
			texteMaitre.setDatePublication(retourDila.getDateParutionJorf());
			texteMaitre.setLegislaturePublication(retourDila.getLegislaturePublication());
			texteMaitre.setTitreOfficiel(retourDila.getTitreOfficiel());
			texteMaitre.setNumero(retourDila.getNumeroTexteParutionJorf());
			if (dossier.isDispositionHabilitation() == null) {
				texteMaitre.setDispositionHabilitation(true);
			} else {
				texteMaitre.setDispositionHabilitation(dossier.isDispositionHabilitation());
			}

			final Decret decret = activiteNormative.getAdapter(Decret.class);
			decret.setNumerosTextes(retourDila.getNumeroTexteParutionJorf());

			texteMaitre.setDateCreation(dossier.getDateCreation());
		}

		return texteMaitre;
	}

	/**
	 * Verifie que le dossier est du bon type pour être associé avec le type d'activite normative selectionné
	 * 
	 * @param dossier
	 * @param type
	 */
	private ActiviteNormativeEnum checkCompatibility(final Dossier dossier, final String type) throws ClientException {
		final ActiviteNormativeEnum activiteNormativeEnum = ActiviteNormativeEnum.getByType(type);
		if (activiteNormativeEnum == null) {
			// si pas de type
			throw new ActiviteNormativeException("activite.normative.type.not.found");
		} else {
			// recuperation du type d'activite normative du type d'acte du
			// dossier
			final DocumentModel doc = SolonEpgServiceLocator.getSolonEpgVocabularyService().getDocumentModelFromId(
					VocabularyConstants.VOCABULARY_TYPE_ACTE_DIRECTORY, dossier.getTypeActe());
			final String normative = (String) doc.getProperty(VocabularyConstants.VOCABULARY_TYPE_ACTE_SCHEMA,
					VocabularyConstants.VOCABULARY_TYPE_ACTE_ACTIVITE_NORMATIVE);

			switch (activiteNormativeEnum) {
				case APPICATION_DES_LOIS:
					if (!ActiviteNormativeConstants.TYPE_APPLICATION_LOIS.equals(normative)) {
						throw new ActiviteNormativeException("activite.normative.type.incompatible");
					}
					break;
				case APPLICATION_DES_ORDONNANCES:
				case ORDONNANCES:
					if (!ActiviteNormativeConstants.TYPE_ORDONNANCES.equals(normative)) {
						throw new ActiviteNormativeException("activite.normative.type.incompatible");
					}
					break;
				case TRAITES_ET_ACCORDS:
					if (!ActiviteNormativeConstants.TYPE_OTHER.equals(normative)) {
						throw new ActiviteNormativeException("activite.normative.type.incompatible");
					}
					break;
				case TRANSPOSITION:
					break;
				case ORDONNANCES_38C:
					if (!ActiviteNormativeConstants.TYPE_APPLICATION_LOIS.equals(normative)) {
						throw new ActiviteNormativeException("activite.normative.type.incompatible");
					}
					break;
				default:
					// par defaut exception car le type n'est pas bon
					throw new ActiviteNormativeException("activite.normative.type.not.found");
			}
		}
		return activiteNormativeEnum;
	}

	@Override
	public Dossier checkIsLoi(final String numeroNor, final CoreSession session) throws ClientException {
		final Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(session, numeroNor);

		if (dossier == null) {
			throw new ActiviteNormativeException("activite.normative.dossier.not.found");
		}

		final DocumentModel doc = SolonEpgServiceLocator.getSolonEpgVocabularyService().getDocumentModelFromId(
				VocabularyConstants.VOCABULARY_TYPE_ACTE_DIRECTORY, dossier.getTypeActe());
		final String normative = (String) doc.getProperty(VocabularyConstants.VOCABULARY_TYPE_ACTE_SCHEMA,
				VocabularyConstants.VOCABULARY_TYPE_ACTE_ACTIVITE_NORMATIVE);

		if (!ActiviteNormativeConstants.TYPE_APPLICATION_LOIS.equals(normative)) {
			throw new ActiviteNormativeException("activite.normative.not.loi");
		}

		return dossier;
	}

	@Override
	public DocumentModel saveTexteMaitre(final TexteMaitre texteMaitre,
			final CoreSession session)
			throws ClientException {
		if (texteMaitre != null) {
			// on recalcul les dates de promulgation
			final Calendar cal2 = extractDateFromTitre(texteMaitre.getTitreOfficiel());
			texteMaitre.setDatePromulgation(cal2);

			updateAndSaveTexteMaitreDateModification(session, texteMaitre);

			session.save();

			return texteMaitre.getDocument();
		}
		return null;
	}

	@Override
	public void removeActiviteNormativeFrom(final DocumentModel doc, final String type, final CoreSession session)
			throws ClientException {
		final ActiviteNormativeEnum activiteNormativeEnum = ActiviteNormativeEnum.getByType(type);
		if (activiteNormativeEnum != null) {
			removeDocumentFrom(doc, session, activiteNormativeEnum);
		}
	}

	private void removeDocumentFrom(final DocumentModel doc, final CoreSession session,
			final ActiviteNormativeEnum activiteNormativeEnum) throws ClientException {
		final ActiviteNormative activiteNormative = doc.getAdapter(ActiviteNormative.class);

		switch (activiteNormativeEnum) {
			case APPICATION_DES_LOIS:
				activiteNormative.setApplicationLoi(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DISABLE);
				break;
			case ORDONNANCES_38C:
				activiteNormative.setOrdonnance38C(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DISABLE);
				break;
			case ORDONNANCES:
				activiteNormative.setOrdonnance(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DISABLE);
				break;
			case TRAITES_ET_ACCORDS:
				activiteNormative.setTraite(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DISABLE);
				break;
			case TRANSPOSITION:
				activiteNormative.setTransposition(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DISABLE);
				break;
			case APPLICATION_DES_ORDONNANCES:
				activiteNormative.setApplicationOrdonnance(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DISABLE);
				break;
			default:
				break;
		}

		// suppression du numeroInterne cf RG-LOI-MAI-12
		final TexteMaitre texteMaitre = doc.getAdapter(TexteMaitre.class);
		texteMaitre.setNumeroInterne(null);

		activiteNormative.save(session);

		session.save();
	}

	@Override
	public Calendar extractDateFromTitre(String titre) {
		Calendar result = null;
		if (StringUtil.isNotEmpty(titre)) {
			// pour regler le probleme du 1er et 2eme du mois
			titre = titre.replaceAll("1er", "1").replaceAll("ème", "").replaceAll("eme", "");
			titre = titre.replaceAll("\u00a0", " ");
			final Matcher matcher = datePattern.matcher(titre);
			if (matcher.find()) {
				// une date a été trouvée on prend la premiere
				final String matchingDate = matcher.group(1);
				final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMM yyyy", Locale.FRENCH);
				try {
					final Date date = sdf.parse(matchingDate);
					result = Calendar.getInstance();
					result.setTime(date);
				} catch (final ParseException e) {
					LOGGER.warn(null, STLogEnumImpl.FAIL_GET_META_DONNEE_TEC,
							"Impossible de recupérer la date à partir du titre de la loi", e);
					// retry sans matchingDate qui est pas bonne
					result = extractDateFromTitre(titre.replace(matchingDate, ""));
				}
			}
		}

		return result;
	}

	@Override
	public List<MesureApplicative> fetchMesure(final List<String> listIds, final CoreSession session)
			throws ClientException {

		final List<MesureApplicative> list = new ArrayList<MesureApplicative>();
		for (final String id : listIds) {
			try {
				final DocumentModel doc = session.getDocument(new IdRef(id));
				list.add(doc.getAdapter(MesureApplicative.class));
			} catch (final Exception e) {
				// probleme de droit / mesure supprimée /etc...
				LOGGER.warn(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, "Impossible de recuperer la mesure " + id, e);
			}
		}

		return list;
	}

	@Override
	public List<Decret> fetchDecrets(final List<String> listIds, final CoreSession session) throws ClientException {

		final List<Decret> list = new ArrayList<Decret>();
		for (final String id : listIds) {
			try {
				final DocumentModel doc = session.getDocument(new IdRef(id));
				list.add(doc.getAdapter(Decret.class));
			} catch (final Exception e) {
				// probleme de droit / mesure supprimée /etc...
				LOGGER.warn(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, "Impossible de recuperer le decret " + id, e);
			}
		}

		// tri par defaut sur le nor
		Collections.sort(list, new Comparator<Decret>() {

			@Override
			public int compare(final Decret decret1, final Decret decret2) {
				if (decret1.getNumeroNor() == null || decret2.getNumeroNor() == null) {
					return -1;
				} else {
					return decret1.getNumeroNor().compareTo(decret2.getNumeroNor());
				}
			}

		});

		return list;
	}

	@Override
	public Dossier checkIsDecretFromNumeroNOR(final String numeroNorDossier, final CoreSession session)
			throws ClientException {
		final Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(session, numeroNorDossier);
		if (dossier == null) {
			throw new ActiviteNormativeException("activite.normative.dossier.not.found");
		}
		final DocumentModel doc = SolonEpgServiceLocator.getSolonEpgVocabularyService().getDocumentModelFromId(
				VocabularyConstants.VOCABULARY_TYPE_ACTE_DIRECTORY, dossier.getTypeActe());
		final String decret = (String) doc.getProperty(VocabularyConstants.VOCABULARY_TYPE_ACTE_SCHEMA,
				VocabularyConstants.VOCABULARY_TYPE_ACTE_IS_DECRET);
		if (!VocabularyConstants.TYPE_ACTE_IS_DECRET.equals(decret)) {
			throw new ActiviteNormativeException("activite.normative.dossier.not.decret");
		}

		return dossier;
	}

	@Override
	public ActiviteNormative saveProjetsLoiDeRatification(final List<LoiDeRatificationDTO> listLoiDeRatification,
			final CoreSession session, final ActiviteNormative activiteNormative) throws ClientException {

		final Set<String> error = new HashSet<String>();
		final Set<String> set = new HashSet<String>();

		final NORService norService = SolonEpgServiceLocator.getNORService();
		for (final LoiDeRatificationDTO loiDeRatificationDTO : listLoiDeRatification) {
			final String numeroNor = loiDeRatificationDTO.getNumeroNor();

			if (StringUtil.isEmpty(numeroNor)) {
				error.add("Le NOR est obligatoire");
				continue;
			}

			final Dossier dossier = norService.findDossierFromNOR(session, numeroNor);

			// verification si la loiDeRatification existe deja ie même
			// numeroNor
			final ActiviteNormative activiteNormative2 = findOrCreateActiviteNormativeByNor(session, numeroNor);
			LoiDeRatification loiDeRatification = activiteNormative2.getAdapter(LoiDeRatification.class);

			if (dossier != null) {
				loiDeRatification.setIdDossier(activiteNormative.getDocument().getId());
			}

			loiDeRatification = loiDeRatificationDTO.remapField(loiDeRatification);
			loiDeRatification.setValidation(Boolean.TRUE);

			session.saveDocument(loiDeRatification.getDocument());

			set.add(activiteNormative2.getDocument().getId());

			// Ajout dans le journal du PAN
			DocumentModel dossierDoc = SolonEpgServiceLocator.getNORService().getDossierFromNOR(session,
					activiteNormative.getAdapter(TexteMaitre.class).getNumeroNor());
			STServiceLocator.getJournalService().journaliserActionPAN(session, dossierDoc.getRef().toString(),
					SolonEpgEventConstant.MODIF_RATIF_EVENT,
					SolonEpgEventConstant.MODIF_RATIF_COMMENT + " [" + loiDeRatificationDTO.getNumeroNor() + "]",
					SolonEpgEventConstant.CATEGORY_LOG_PAN_RATIFICATION_ORDO);
		}

		if (!error.isEmpty()) {
			TransactionHelper.setTransactionRollbackOnly();
			throw new ActiviteNormativeException(error);
		}

		final TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);

		texteMaitre.setLoiRatificationIds(new ArrayList<String>(set));
		final DocumentModel doc = session.saveDocument(texteMaitre.getDocument());

		session.save();

		return doc.getAdapter(ActiviteNormative.class);

	}

	@Override
	public ActiviteNormative saveProjetsLoiDeRatificationReprise(final LoiDeRatificationDTO loiDeRatificationDTO,
			final CoreSession session, final ActiviteNormative activiteNormative) throws ClientException {

		final Set<String> error = new HashSet<String>();
		ActiviteNormative result = null;
		final String numeroNor = loiDeRatificationDTO.getNumeroNor();

		if (StringUtil.isEmpty(numeroNor)) {
			error.add("Le NOR est obligatoire");
		} else {
			final Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(session, numeroNor);

			// verification si la loiDeRatification existe deja ie même
			// numeroNor
			final ActiviteNormative activiteNormative2 = findOrCreateActiviteNormativeByNor(session, numeroNor);
			LoiDeRatification loiDeRatification = activiteNormative2.getAdapter(LoiDeRatification.class);

			if (dossier != null) {
				loiDeRatification.setIdDossier(activiteNormative.getDocument().getId());
			}

			loiDeRatification = loiDeRatificationDTO.remapField(loiDeRatification);
			loiDeRatification.setValidation(Boolean.TRUE);

			session.saveDocument(loiDeRatification.getDocument());

			if (!error.isEmpty()) {
				TransactionHelper.setTransactionRollbackOnly();
				throw new ActiviteNormativeException(error);
			}

			final TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);

			final List<String> list = texteMaitre.getLoiRatificationIds();

			if (!list.contains(activiteNormative2.getDocument().getId())) {
				list.add(activiteNormative2.getDocument().getId());
			}

			texteMaitre.setLoiRatificationIds(list);
			final DocumentModel doc = session.saveDocument(texteMaitre.getDocument());
			session.save();
			result = doc.getAdapter(ActiviteNormative.class);
		}

		return result;

	}

	@Override
	public String createLienJORFLegifrance(final String numeroNor) {
		if (StringUtil.isNotEmpty(numeroNor)) {
			final StringBuilder urlLegifrance = new StringBuilder(STServiceLocator.getConfigService().getValue(
					STConfigConstants.LEGIFRANCE_JORF_URL));
			urlLegifrance.append(numeroNor);
			return urlLegifrance.toString();
		}
		return null;
	}

	@Override
	public void saveDecrets(final String idCurrentMesure, final List<DecretDTO> listDecret,
			final String idCurrentTexteMaitre, final CoreSession session) throws ClientException {

		final Set<String> decretIds = new HashSet<String>();
		final Set<String> error = new HashSet<String>();

		final MesureApplicative mesureApplicative = session.getDocument(new IdRef(idCurrentMesure)).getAdapter(
				MesureApplicative.class);

		// parcours des décrets IHM pour la mesure en cours
		for (final DecretDTO decretDTO : listDecret) {

			if (StringUtil.isEmpty(decretDTO.getNumeroNor())) {
				error.add("Le NOR est obligatoire.");
			} else {
				// Vu qu'on contourne le lancement d'exception, on vérifie d'une
				// autre façon
				// si on doit sauvegarder le décret
				boolean norOk = true;

				try {
					// Vérif de l'existence du NOR ; une exception est levée
					// s'il n'existe pas
					checkIsDecretFromNumeroNOR(decretDTO.getNumeroNor(), session);
				} catch (ActiviteNormativeException e) {
					error.add(decretDTO.getNumeroNor());
					norOk = false;
				}

				if (norOk) {
					// récupération ou création du décret
					ActiviteNormative activiteNormative = findOrCreateActiviteNormativeByNor(session,
							decretDTO.getNumeroNor());

					Decret decret = activiteNormative.getAdapter(Decret.class);
					// On enregistre les modifications seulement si la case est cochée
					if (decretDTO.getValidate()) {
						// récupération des données IHM dans l'objet
						decret = decretDTO.remapField(decret);
					}

					// ajout de la mesure à la liste présente dans le décret
					final Set<String> setMesures = new HashSet<String>(decret.getMesureIds());
					setMesures.add(idCurrentMesure);
					decret.setMesureIds(new ArrayList<String>(setMesures));

					decret.setIdDossier(activiteNormative.getDocument().getId());

					// Validation du décret
					if (!decret.hasValidation() && (decretDTO.getValidate() || decretDTO.getId() == null)) {
						decret.setValidation(Boolean.TRUE);
					}

					decretDTO.setValidationLink(Boolean.TRUE);
					decret.save(session);
					decretIds.add(decret.getId());
				}
			}

		}

		if (error.isEmpty()) {
			// delinkage des decrets precedents
			List<Decret> decrets = fetchDecrets(mesureApplicative.getDecretIds(), session);
			for (final Decret decret : decrets) {
				// Si un décret existe en bdd mais n'était plus présent en IHM,
				// on le supprime de la liste
				if (!decretIds.contains(decret.getId())) {
					final Set<String> listMesure = new HashSet<String>(decret.getMesureIds());
					listMesure.remove(idCurrentMesure);
					decret.setMesureIds(new ArrayList<String>(listMesure));
					decret.save(session);
				}
			}

			mesureApplicative.setDecretIds(new ArrayList<String>(decretIds));
			mesureApplicative.setDecretsIdsInvalidated(null);
			mesureApplicative.save(session);

			final TexteMaitre texteMaitre = session.getDocument(new IdRef(idCurrentTexteMaitre))
					.getAdapter(TexteMaitre.class);

			session.save();
			
			updateAndSaveTexteMaitreDateModification(session, texteMaitre);

			// Ajout dans le journal du PAN
			DocumentModel dossierDoc = SolonEpgServiceLocator.getNORService().getDossierFromNOR(session,
					texteMaitre.getNumeroNor());
			String category = null;
			if (texteMaitre.getAdapter(ActiviteNormative.class).getApplicationLoi().equals("1"))
				category = SolonEpgEventConstant.CATEGORY_LOG_PAN_APPLICATION_LOIS;
			else if (texteMaitre.getAdapter(ActiviteNormative.class).getApplicationOrdonnance().equals("1"))
				category = SolonEpgEventConstant.CATEGORY_LOG_PAN_APPLICATION_ORDO;
			for (DecretDTO decretDTO : listDecret) {
				if (decretDTO.getValidate())
					STServiceLocator.getJournalService().journaliserActionPAN(session, dossierDoc.getRef().toString(),
							SolonEpgEventConstant.MODIF_DECRET_APP_EVENT,
							SolonEpgEventConstant.MODIF_DECRET_APP_COMMENT + " [" + decretDTO.getNumeroNor() + "]",
							category);
			}
		} else {
			TransactionHelper.setTransactionRollbackOnly();
			throw new ActiviteNormativeException(error);
		}
	}

	private void updateAndSaveTexteMaitreDateModification(final CoreSession session, final TexteMaitre texteMaitre)
			throws ClientException {

		final ActiviteNormativeProgrammation activiteNormativeProgrammation = session
				.getDocument(new IdRef(texteMaitre.getId())).getAdapter(ActiviteNormativeProgrammation.class);
		if (isPublicationEcheancierBdjActivated(session) && activiteNormativeProgrammation != null
				&& activiteNormativeProgrammation.getTableauSuiviPublicationDate() != null) {
			texteMaitre.setDateModification(Calendar.getInstance());
		}

		texteMaitre.save(session);
	}

	@Override
	public void saveMesure(final ActiviteNormative activiteNormative, final List<MesureApplicativeDTO> listMesure,
			final CoreSession session) throws ClientException {
		final Set<String> idsMesure = new HashSet<String>();
		final Set<String> error = new HashSet<String>();

		final TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);

		final SSPrincipal principal = (SSPrincipal) session.getPrincipal();
		final List<String> groups = principal.getGroups();
		final boolean isAdminFonctionnel = groups.contains(STBaseFunctionConstant.ADMIN_FONCTIONNEL_GROUP_NAME);
		final boolean isProfilMinisteriel = (principal
				.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_LOI_MINISTERE) || principal
				.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_APP_ORDONNANCE_MINISTERE))
				&& !isAdminFonctionnel;
		final HistoriqueMajMinisterielleService majMinService = SolonEpgServiceLocator
				.getHistoriqueMajMinisterielleService();

		for (final MesureApplicativeDTO mesureApplicativeDTO : listMesure) {
			String identifier = null;
			DocumentModel mesureApplicativeDoc = null;
			MesureApplicative mesureApplicative = null;

			if (StringUtil.isNotEmpty(mesureApplicativeDTO.getId())) {
				mesureApplicativeDoc = session.getDocument(new IdRef(mesureApplicativeDTO.getId()));
				mesureApplicative = mesureApplicativeDoc.getAdapter(MesureApplicative.class);
				identifier = updateMesure(session, texteMaitre, mesureApplicative, mesureApplicativeDTO, isProfilMinisteriel, false, majMinService);
			} else {
				mesureApplicativeDoc = session
						.createDocumentModel(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE);
				mesureApplicativeDoc.setPathInfo(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_PATH, UUID.randomUUID()
						.toString());
				mesureApplicative = mesureApplicativeDoc.getAdapter(MesureApplicative.class);
				identifier = updateMesure(session, texteMaitre, mesureApplicative, mesureApplicativeDTO, isProfilMinisteriel, true, majMinService);
			}
			mesureApplicative.setIdDossier(activiteNormative.getDocument().getId());
			mesureApplicative.save(session);

			if (identifier == null) {
				error.add("Impossible de générer un id pour la mesure d'ordre " + mesureApplicativeDTO.getNumeroOrdre());
				LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_DOCUMENT_TEC,
						"Impossible de générer un id pour la mesure d'ordre " + mesureApplicativeDTO.getNumeroOrdre());
			} else {
				idsMesure.add(identifier);
			}
		}

		if (error.isEmpty()) {
			// delinkage des mesures
			for (final String id : texteMaitre.getMesuresIds()) {
				if (!idsMesure.contains(id)) {
					final MesureApplicative mesure = session.getDocument(new IdRef(id)).getAdapter(
							MesureApplicative.class);
					for (final String idDect : mesure.getDecretIds()) {
						final Decret decret = session.getDocument(new IdRef(idDect)).getAdapter(Decret.class);
						final List<String> mesures = decret.getMesureIds();
						mesures.remove(id);
						decret.setMesureIds(mesures);
						decret.save(session);
					}
					// delete des mesures supprimées
					LOGGER.info(session, EpgLogEnumImpl.DEL_MESURE_TEC, mesure.getDocument());
					session.removeDocument(new IdRef(id));
				}
			}
			texteMaitre.setMesuresIds(new ArrayList<String>(idsMesure));
			session.save();

			updateAndSaveTexteMaitreDateModification(session, texteMaitre);
		} else {
			TransactionHelper.setTransactionRollbackOnly();
			throw new ActiviteNormativeException(error);
		}

	}
	
	/**
	 * Met à jour la MesureApplicative à partir des données du DTO, les données d'origine de la mesure sont remise à jour
	 * (cas de mesures sans données existantes en prod), et créé ou met à jour la mesure
	 * @param session
	 * @param texteMaitre
	 * @param mesureApplicative
	 * @param mesureApplicativeDTO
	 * @param isProfilMinisteriel
	 * @param create
	 * @param majMinService
	 * @return
	 * @throws ClientException
	 */
	private String updateMesure(final CoreSession session, final TexteMaitre texteMaitre, MesureApplicative mesureApplicative,
			final MesureApplicativeDTO mesureApplicativeDTO, final boolean isProfilMinisteriel, final boolean create, 
			final HistoriqueMajMinisterielleService majMinService) throws ClientException {
		ActiviteNormative actNormTexteMaitre = texteMaitre.getAdapter(ActiviteNormative.class);
		if (ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE.equals(actNormTexteMaitre.getApplicationLoi())) {
			mesureApplicative.getDocument().getAdapter(ActiviteNormative.class).setApplicationLoi(
					ActiviteNormativeConstants.MESURE_FILTER);
		} else if (ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE.equals(actNormTexteMaitre.getApplicationOrdonnance())) {
			mesureApplicative.getDocument().getAdapter(ActiviteNormative.class).setApplicationOrdonnance(
					ActiviteNormativeConstants.MESURE_FILTER);
		}
		// Pour les profils ministériels accédant au pan, on ajoute une entrée dans l'historique des mises à
		// jour ministérielles
		if (isProfilMinisteriel) {
			majMinService.registerMajMesure(session, texteMaitre, mesureApplicative, mesureApplicativeDTO);
		}
		// La validation se fait dans le remap
		mesureApplicative = mesureApplicativeDTO.remapField(mesureApplicative);
		DocumentModel mesureApplicativeDoc;
		if (create) {
			mesureApplicativeDoc = session.createDocument(mesureApplicative.getDocument());
		} else {
			mesureApplicativeDoc = mesureApplicative.save(session);
		}
		return mesureApplicativeDoc.getId();
	}

	@Override
	public TexteMaitre saveOrdonnance(final OrdonnanceDTO ordonnanceDTO, final TexteMaitre texteMaitre,
			final CoreSession session) throws ClientException {
		DocumentModel doc = ordonnanceDTO.remapField(texteMaitre);
		doc = session.saveDocument(doc);
		session.save();
		return doc.getAdapter(TexteMaitre.class);
	}

	@Override
	public void attachDecretToLoi(final Dossier dossierDecret, final CoreSession session) throws ClientException {
		final String idDecret = dossierDecret.getDocument().getId();
		final Set<String> error = new HashSet<String>();
		Boolean hasMesure = Boolean.FALSE;

		if (StringUtil.isEmpty(idDecret)) {
			error.add("Le decret n'est pas encore sauvegardé.");
		} else {
			for (final ComplexeType complexeType : dossierDecret.getApplicationLoi()) {
				String result = attachDecretToApplication(session, dossierDecret, complexeType, 1);
				if ("OK".equals(result)) {
					hasMesure = true;
				} else if (StringUtil.isNotBlank(result)) {
					error.add(result);
				}
			}
		}

		if (hasMesure && error.isEmpty()) {
			session.save();
		} else if (!error.isEmpty()) {
			TransactionHelper.setTransactionRollbackOnly();
			throw new ActiviteNormativeException(error);
		}
	}

	@Override
	public void attachDecretToOrdonnance(final Dossier dossierDecret, final CoreSession session) throws ClientException {
		final String idDecret = dossierDecret.getDocument().getId();
		final Set<String> error = new HashSet<String>();
		Boolean hasMesure = Boolean.FALSE;

		if (StringUtil.isEmpty(idDecret)) {
			error.add("Le decret n'est pas encore sauvegardé.");
		} else {
			for (final ComplexeType complexeType : dossierDecret.getTranspositionOrdonnance()) {
				String result = attachDecretToApplication(session, dossierDecret, complexeType, 2);
				if ("OK".equals(result)) {
					hasMesure = true;
				} else if (StringUtil.isNotBlank(result)) {
					error.add(result);
				}
			}
		}

		if (hasMesure && error.isEmpty()) {
			session.save();
		} else if (!error.isEmpty()) {
			TransactionHelper.setTransactionRollbackOnly();
			throw new ActiviteNormativeException(error);
		}
	}

	/**
	 * Attache un décret à la mesure
	 * 
	 * @param session
	 * @param dossierDecret
	 * @param complexeType
	 * @param applicationType
	 *            : 1 pour application des lois, 2 pour application des ordonnances
	 * @return
	 * @throws ClientException
	 */
	private String attachDecretToApplication(final CoreSession session, final Dossier dossierDecret,
			final ComplexeType complexeType, int applicationType) throws ClientException {
		final String numeroInterne = (String) complexeType.getSerializableMap().get(
				DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY);

		// recherche du dossier correspondant par numeroInterne
		DocumentModel activiteNormativeDoc = findActiviteNormativeDocByNumero(session, numeroInterne);

		if (activiteNormativeDoc != null) {
			final String idActiviteNormative = activiteNormativeDoc.getId();
			final String numeroOrdre = (String) complexeType.getSerializableMap().get(
					DossierSolonEpgConstants.DOSSIER_LOI_APPLIQUEE_REF_MESURE_PROPERTY);
			MesureApplicative mesureApplicative = null;
			if (applicationType == 1) {
				mesureApplicative = findMesureApplicativeForLoi(session, numeroOrdre, idActiviteNormative);
			} else if (applicationType == 2) {
				mesureApplicative = findMesureApplicativeForOrdonnance(session, numeroOrdre, idActiviteNormative);
			}

			if (mesureApplicative != null) {

				final ActiviteNormative an2 = findOrCreateActiviteNormativeByNor(session, dossierDecret.getNumeroNor());
				Decret decret = an2.getAdapter(Decret.class);
				final TexteMaitre texteMaitreDecret = an2.getAdapter(TexteMaitre.class);

				final Set<String> setMesures = new HashSet<String>(texteMaitreDecret.getMesuresIds());
				String mesureId = mesureApplicative.getId();
				if (!setMesures.contains(mesureId)) {
					// On a créé le décret via EPG, et on le rattache au PAN dans une mesure qui ne le contenait
					// pas encore.
					// Il est nécessaire que l'utilisateur PAN valide le rattachement
					final Set<String> setDecrets = new HashSet<String>(mesureApplicative.getDecretsIdsInvalidated());
					setDecrets.add(decret.getId());
					mesureApplicative.setDecretsIdsInvalidated(new ArrayList<String>(setDecrets));
					if (setMesures.isEmpty() && decret.hasValidation()) {
						// Si aucune mesure n'était rattachée au décret et qu'il était validé côté PAN, on
						// l'invalide pour que l'utilisatrice le valide
						// avant sa prise en compte (cas notamment des nouveaux texte maitre des décrets)
						decret.setValidation(Boolean.FALSE);
					}
					setMesures.add(mesureApplicative.getId());
					texteMaitreDecret.setMesuresIds(new ArrayList<String>(setMesures));
					texteMaitreDecret.save(session);
				}

				final TexteMaitre texteMaitreLoi = activiteNormativeDoc.getAdapter(TexteMaitre.class);
				final Set<String> listMesure = new HashSet<String>(texteMaitreLoi.getMesuresIds());
				listMesure.add(mesureApplicative.getId());
				texteMaitreLoi.setMesuresIds(new ArrayList<String>(listMesure));
				texteMaitreLoi.save(session);

				decret = remapDecret(dossierDecret, idActiviteNormative, complexeType, mesureApplicative.getId(),
						decret);

				decret.save(session);

				final Set<String> listDecret = new HashSet<String>(mesureApplicative.getDecretIds());
				listDecret.add(decret.getId());
				mesureApplicative.setDecretIds(new ArrayList<String>(listDecret));

				mesureApplicative.save(session);
				return "OK";
			}
		}
		return "";
	}

	/**
	 * remapage du decret par rapport au complexe type
	 * 
	 * @param dossierDecret
	 * @param idDecret
	 * @param complexeType
	 * @param idMesure
	 * @param decret
	 * @return
	 * @throws ClientException
	 */
	private Decret remapDecret(final Dossier dossierDecret, final String idDecret, final ComplexeType complexeType,
			final String idMesure, final Decret decret) throws ClientException {
		decret.setIdDossier(idDecret);

		// remap du dossier sur le decret
		final RetourDila retourDila = dossierDecret.getDocument().getAdapter(RetourDila.class);
		final ConseilEtat conseilEtat = dossierDecret.getDocument().getAdapter(ConseilEtat.class);

		if (retourDila != null) {

			if (!decret.isDatePublicationLocked()) {
				decret.setDatePublication(retourDila.getDateParutionJorf());
			}

			if (!decret.isNumeroPageLocked()) {
				decret.setNumeroPage(retourDila.getPageParutionJorf());
			}

			if (!decret.isTitreOfficielLocked()) {
				decret.setTitreOfficiel(retourDila.getTitreOfficiel());
			}
		}

		if (conseilEtat != null) {

			if (!decret.isDateSectionCeLocked()) {
				decret.setDateSectionCe(conseilEtat.getDateSectionCe());
			}

			if (!decret.isDateSortieCELocked()) {
				decret.setDateSortieCE(conseilEtat.getDateSortieCE());
			}

			if (!decret.isRapporteurCeLocked()) {
				decret.setRapporteurCe(conseilEtat.getRapporteurCe());
			}

			if (!decret.isSectionCeLocked()) {
				decret.setSectionCe(conseilEtat.getSectionCe());
			}
		}

		if (dossierDecret.getDateSignature() != null) {
			decret.setDateSignature(dossierDecret.getDateSignature());
		} else if (retourDila != null) {
			decret.setDateSignature(extractDateFromTitre(retourDila.getTitreOfficiel()));
		}

		decret.setNumeroNor(dossierDecret.getNumeroNor());

		if (!decret.isTypeActeLocked()) {
			decret.setTypeActe(dossierDecret.getTypeActe());
		}

		final Set<String> listMesure = new HashSet<String>(decret.getMesureIds());
		listMesure.add(idMesure);
		decret.setMesureIds(new ArrayList<String>(listMesure));

		return decret;
	}

	@Override
	public Map<String, Long> buildMesuresForFicheSignaletique(final CoreSession session,
			final ActiviteNormative activiteNormative) throws ClientException {
		final Map<String, Long> mapMesures = new LinkedHashMap<String, Long>();

		final List<DocumentModel> listResult = STServiceLocator.getVocabularyService().getEntries(
				VocabularyConstants.TYPE_MESURE_VOCABULARY, VocabularyConstants.LIST_MESURE_STATS);
		final Map<String, String> mapConvertion = new HashMap<String, String>();
		for (final DocumentModel documentModel : listResult) {
			final String identifier = (String) documentModel.getProperty(STVocabularyConstants.COLUMN_ID).getValue();
			final String label = "activite.normative.fiche.signaletique."
					+ (String) documentModel.getProperty(STVocabularyConstants.COLUMN_LABEL).getValue();
			mapMesures.put(label, 0L);
			mapConvertion.put(identifier, label);
		}

		final List<String> params = new ArrayList<String>();
		params.addAll(activiteNormative.getAdapter(TexteMaitre.class).getMesuresIds());

		if (params.isEmpty()) {
			return mapMesures;
		}

		final StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(" SELECT m.");
		queryBuilder.append(TexteMaitreConstants.TEXTE_MAITRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(TexteMaitreConstants.TYPE_MESURE);
		queryBuilder.append(", count() ");
		queryBuilder.append(" FROM ");
		queryBuilder.append(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE);
		queryBuilder.append(" as m WHERE m.ecm:uuid IN (");
		queryBuilder.append(StringUtil.getQuestionMark(params.size()));
		queryBuilder.append(" ) ");
		queryBuilder.append(" GROUP BY m.");
		queryBuilder.append(TexteMaitreConstants.TEXTE_MAITRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(TexteMaitreConstants.TYPE_MESURE);

		IterableQueryResult res = null;
		try {
			res = QueryUtils.doUFNXQLQuery(session, queryBuilder.toString(), params.toArray());
			final Iterator<Map<String, Serializable>> iterator = res.iterator();
			Long total = 0L;
			while (iterator.hasNext()) {
				final Map<String, Serializable> map = iterator.next();
				int index = 0;
				String type = "";
				for (final Entry<String, Serializable> entry : map.entrySet()) {
					if (index < 1) {
						type = (String) entry.getValue();
					} else {
						final Long count = (Long) entry.getValue();
						if (!StringUtil.isBlank(type) && !StringUtil.isBlank(mapConvertion.get(type))) {
							mapMesures.put(mapConvertion.get(type), count);
						}
						total += count;
					}
					index++;
				}
			}
		} finally {
			if (res != null) {
				res.close();
			}
		}

		return mapMesures;
	}

	@Override
	public Map<String, String> buildMinistereForFicheSignaletique(final CoreSession session,
			final ActiviteNormative activiteNormative) throws ClientException {
		final Map<String, Long> mapApplication = new HashMap<String, Long>();
		final Map<String, Long> mapTotal = new HashMap<String, Long>();

		final List<MesureApplicative> listMesure = fetchMesure(activiteNormative.getAdapter(TexteMaitre.class)
				.getMesuresIds(), session);

		for (final MesureApplicative mesureApplicative : listMesure) {

			final String min = mesureApplicative.getMinisterePilote();
			if (mesureApplicative.getTypeMesure() == null
					|| !VocabularyConstants.TYPE_MESURE_ACTIVE.equals(mesureApplicative.getTypeMesure())) {
				continue;
			}
			if (StringUtil.isNotBlank(min)) {
				if (mesureApplicative.getApplicationRecu()) {
					Long count = mapApplication.get(min);
					mapApplication.put(min, count == null ? 1L : ++count);
				}
				Long count = mapTotal.get(min);
				mapTotal.put(min, count == null ? 1L : ++count);
			}
		}

		final Map<String, String> repartititionMinisteres = new LinkedHashMap<String, String>();
		final Set<String> list = mapTotal.keySet();
		final List<String> ministereList = new ArrayList<String>();
		final Map<String, String> ministereConvertor = new HashMap<String, String>();

		final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
		for (final String idMinistere : list) {
			EntiteNode node;
			try {
				node = ministeresService.getEntiteNode(idMinistere);
			} catch (final ClientException e) {
				// node non trouvé
				node = null;
			}
			final String nor = node == null ? idMinistere : node.getNorMinistere();
			ministereList.add(nor);
			ministereConvertor.put(nor, idMinistere);
		}

		Collections.sort(ministereList, new Comparator<String>() {

			@Override
			public int compare(final String obj1, final String obj2) {
				return obj1.compareTo(obj2);
			}
		});

		for (final String nor : ministereList) {
			final String idMinistere = ministereConvertor.get(nor);
			Long countApplication = mapApplication.get(idMinistere);
			countApplication = countApplication == null ? 0L : countApplication;
			Long countTotal = mapTotal.get(idMinistere);
			countTotal = countTotal == null ? 0L : countTotal;
			repartititionMinisteres.put(nor, countApplication + "/" + countTotal);
		}

		return repartititionMinisteres;
	}

	@Override
	public Integer buildTauxApplicationForFicheSignaletique(final CoreSession session,
			final ActiviteNormative activiteNormative) throws ClientException {

		final List<MesureApplicative> listMesure = fetchMesure(activiteNormative.getAdapter(TexteMaitre.class)
				.getMesuresIds(), session);

		int cptHasApplication = 0;
		int cptActive = 0;
		for (final MesureApplicative mesureApplicative : listMesure) {
			if (VocabularyConstants.TYPE_MESURE_ACTIVE.equals(mesureApplicative.getTypeMesure())) {
				// mesure active
				cptActive++;
				if (mesureApplicative.getApplicationRecu()) {
					cptHasApplication++;
				}
			}
		}

		if (cptActive > 0) {
			final float taux = cptHasApplication * 100 / (float) cptActive;
			return Math.round(taux);
		}

		return 0;
	}

	@Override
	public Map<String, String> buildDelaiPublicationForFicheSignaletique(final CoreSession session,
			final ActiviteNormative activiteNormative, final TexteMaitre texteMaitre) throws ClientException {
		final Map<String, String> delaiPublication = new LinkedHashMap<String, String>();
		delaiPublication.put("Moyenne", null);
		delaiPublication.put("Plus petit délai", null);
		delaiPublication.put("Plus grand délai", null);

		long datePlusPetitDelai = Long.MAX_VALUE;
		long datePlusGrandDelai = 0;
		Double diff, sumDelai = 0.00;

		Boolean allHasApplication = Boolean.TRUE;

		final Calendar dateLoi = texteMaitre.getDateEntreeEnVigueur() == null ? texteMaitre.getDatePublication()
				: texteMaitre.getDateEntreeEnVigueur();
		Date differe;
		final Date dateL = dateLoi == null ? null : new Date(dateLoi.getTimeInMillis());

		int count = 0;
		final List<String> mesuresId = activiteNormative.getAdapter(TexteMaitre.class).getMesuresIds();
		if (mesuresId != null && !mesuresId.isEmpty()) {
			final List<MesureApplicative> listMesure = fetchMesure(mesuresId, session);
			long plusGrandDelaiDate;
			long delaiDecret;
			Boolean hasApplication;
			for (final MesureApplicative mesureApplicative : listMesure) {
				if (VocabularyConstants.TYPE_MESURE_ACTIVE.equals(mesureApplicative.getTypeMesure())) {
					hasApplication = Boolean.TRUE;
					differe = mesureApplicative.isDiffere() && mesureApplicative.getDateEntreeEnVigueur() != null ? mesureApplicative
							.getDateEntreeEnVigueur().getTime() : dateL;
					if (differe == null) {
						return delaiPublication;
					}
					plusGrandDelaiDate = -1;
					// String ministere =
					// mesureApplicative.getMinisterePilote();
					final List<String> decretsId = mesureApplicative.getDecretIds();
					if (decretsId != null && !decretsId.isEmpty()) {
						final List<Decret> listDecret = fetchDecrets(decretsId, session);
						for (final Decret decret : listDecret) {
							if (decret.getDatePublication() == null) {
								hasApplication = Boolean.FALSE;
							} else {
								if (decret.getDatePublication() != null) {
									diff = (decret.getDatePublication().getTimeInMillis() - differe.getTime())
											/ MILISECOND_PER_MONTH;
									delaiDecret = diff < 0 ? 0 : Math.round(diff);
									plusGrandDelaiDate = Math.max(plusGrandDelaiDate, delaiDecret);

								}
							}
						}
					}

					if (hasApplication) {
						if (plusGrandDelaiDate > -1) {
							count++;
							sumDelai += plusGrandDelaiDate;
							datePlusPetitDelai = Math.min(plusGrandDelaiDate, datePlusPetitDelai);
							datePlusGrandDelai = Math.max(plusGrandDelaiDate, datePlusGrandDelai);
						}
					} else {
						allHasApplication = Boolean.FALSE;
					}
				}
			}

			if (datePlusPetitDelai < Long.MAX_VALUE) {
				delaiPublication.put("Plus petit délai", String.valueOf(datePlusPetitDelai));
			}
			if (allHasApplication) {
				delaiPublication.put("Plus grand délai", String.valueOf(datePlusGrandDelai));
				delaiPublication.put("Moyenne", (sumDelai > 0 ? String.valueOf(Math.round(sumDelai / count)) : "0"));
			}
		}

		return delaiPublication;
	}

	@Override
	public void saveCurrentProgrammationLoi(final List<LigneProgrammation> lignesProgrammations,
			final ActiviteNormative activiteNormative, final CoreSession session) throws ClientException {
		final String userFullName = STServiceLocator.getSTUserService().getUserFullName(
				session.getPrincipal().getName());

		final ActiviteNormativeProgrammation activiteNormativeProgrammation = activiteNormative
				.getAdapter(ActiviteNormativeProgrammation.class);
		activiteNormativeProgrammation.setLockDate(Calendar.getInstance());
		activiteNormativeProgrammation.setLockUser(userFullName);
		activiteNormativeProgrammation.save(session);
		session.save();

	}

	@Override
	public void removeCurrentProgrammationLoi(final ActiviteNormative activiteNormative, final CoreSession session)
			throws ClientException {
		final ActiviteNormativeProgrammation activiteNormativeProgrammation = activiteNormative
				.getAdapter(ActiviteNormativeProgrammation.class);

		activiteNormativeProgrammation.emptyLigneProgrammation(session,
				LigneProgrammationConstants.TYPE_TABLEAU_VALUE_PROGRAMMATION);
		activiteNormativeProgrammation.setLockDate(null);
		activiteNormativeProgrammation.setLockUser(null);
		activiteNormativeProgrammation.save(session);
		session.save();

	}

	@Override
	public void publierTableauSuivi(final List<LigneProgrammation> lignesProgrammations,
			final ActiviteNormative activiteNormative, final CoreSession session) throws ClientException {

		final ActiviteNormativeProgrammation activiteNormativeProgrammation = activiteNormative
				.getAdapter(ActiviteNormativeProgrammation.class);

		final String userFullName = STServiceLocator.getSTUserService().getUserFullName(
				session.getPrincipal().getName());

		activiteNormativeProgrammation.emptyLigneProgrammation(session,
				LigneProgrammationConstants.TYPE_TABLEAU_VALUE_SUIVI);
		for (LigneProgrammation ligneProg : lignesProgrammations) {
			String titreDoc = "LigneSuivi-" + DateUtil.formatDDMMYYYY(Calendar.getInstance());

			DocumentModel docCopy = session.createDocumentModel(activiteNormative.getDocument().getPath().toString(),
					titreDoc, LigneProgrammationConstants.LIGNE_PROGRAMMATION_DOC_TYPE);
			ligneProg.getDocument().copyContent(docCopy);
			PropertyUtil.setProperty(docCopy, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
					LigneProgrammationConstants.TYPE_TABLEAU, LigneProgrammationConstants.TYPE_TABLEAU_VALUE_SUIVI);
			session.createDocument(docCopy);
			session.save();
		}

		activiteNormativeProgrammation.setTableauSuiviPublicationDate(Calendar.getInstance());
		activiteNormativeProgrammation.setTableauSuiviPublicationUser(userFullName);
		activiteNormativeProgrammation.save(session);
		session.save();

		// Ajout dans le journal du PAN
		TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);
		DocumentModel dossierDoc = SolonEpgServiceLocator.getNORService().getDossierFromNOR(session,
				texteMaitre.getNumeroNor());
		STServiceLocator.getJournalService().journaliserActionPAN(session, dossierDoc.getRef().toString(),
				SolonEpgEventConstant.PUBLI_STAT_EVENT,
				SolonEpgEventConstant.PUBLI_STAT_COMMENT + " [Tableau de suivi]",
				SolonEpgEventConstant.CATEGORY_LOG_PAN_GENERAL);
	}

	@Override
	public void publierTableauSuiviHTML(final DocumentModel currentDocument, final CoreSession session,
			final boolean masquerApplique, final boolean fromBatch, boolean legislatureEnCours) throws ClientException {

		final ActiviteNormativeProgrammation activiteNormativeProgrammation = currentDocument
				.getAdapter(ActiviteNormativeProgrammation.class);
		final TableauDeProgrammationLoiDTO tableauDeProgrammationLoiDTO = new TableauDeProgrammationLoiDTO(
				activiteNormativeProgrammation, session, Boolean.FALSE, masquerApplique);

		TexteMaitre texteMaitre = currentDocument.getAdapter(TexteMaitre.class);
		
		if (StringUtil.isEmpty(texteMaitre.getLegislaturePublication())) {
			// On ne publie pas si aucune législature n'est renseignée.
			return;
		}

		ActiviteNormative activiteNormative = currentDocument.getAdapter(ActiviteNormative.class);
		boolean isApplicationOrdonnance = "1".equals(activiteNormative.getApplicationOrdonnance());

		final String loiNb = texteMaitre.getNumeroNor();
		final String generatedReportPath;
		try {
			if (legislatureEnCours) {
				generatedReportPath = getPathDirANStatistiquesPublie();
			} else {
				generatedReportPath = getPathDirANStatistiquesLegisPrecPublie(session);
			}
			final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			final SimpleDateFormat sdfLong = new SimpleDateFormat("MMMM yyyy");

			final File htmlReport = new File(generatedReportPath + "/" + "tableauDeSuivi-" + loiNb + ".html");
			if (!htmlReport.exists()) {
				if (fromBatch && legislatureEnCours) {
					// Si du batch et de la législature courante, on ne publie pas
					// si le rapport n'a jamais été publié manuellement avant
					return;
				}
				htmlReport.createNewFile();
			}
			final FileOutputStream outputStream = new FileOutputStream(htmlReport);
			outputStream
					.write("<style>.suiviTbl{font: normal 12px \"Lucida Grande\", sans-serif;border-collapse:collapse;width:2000px}\n.suiviTbl td,.suiviTbl th{border:thin solid black;}</style>"
							.getBytes());
			outputStream
					.write(("<table class='suiviTbl' cellpadding='2' cellspacing='0'>"
							+ "<colgroup><col style='width:20px'/><col style='width:100px'/><col style='width:120px'/><col style='width:250px'/><col style='width:100px'/>"
							+ "<col style='width:100px'/><col style='width:100px'/><col style='width:100px'/><col style='width:80px'/><col style='width:80px'/>"
							+ "<col style='width:80px'/><col style='width:80px'/><col style='width:80px'/><col style='width:300px'/><col style='width:100px'/>"
							+ "<col style='width:100px'/><col style='width:120px'/><col style='width:80px'/></colgroup>"
							+ "<thead><tr>").getBytes());
			outputStream.write("<th>N° ordre</th>".getBytes());
			outputStream.write(isApplicationOrdonnance ? "<th>Article de l'ordonnance</th>".getBytes() : "<th>Article de la loi</th>".getBytes());
			outputStream.write("<th>Base légale</th>".getBytes());
			outputStream.write(isApplicationOrdonnance ? "<th>Objet RIM</th>".getBytes() : "<th>Objet RIM</th>".getBytes());
			outputStream.write("<th>Ministère pilote</th>".getBytes());
			outputStream.write("<th>Direction responsable</th>".getBytes());
			if (!isApplicationOrdonnance) {
				outputStream.write("<th>Catégorie de texte</th>".getBytes());
			}
			outputStream.write("<th>Consultation obligatoire</th>".getBytes());
			outputStream.write("<th>Calendrier des consultations</th>".getBytes());
			outputStream.write("<th>Date prévisionnelle de saisine CE</th>".getBytes());
			outputStream.write("<th>Date saisine CE</th>".getBytes());
			outputStream.write("<th>Date de sortie CE</th>".getBytes());
			outputStream.write("<th>Objectif de publication</th>".getBytes());
			outputStream.write("<th>Observation</th>".getBytes());
			outputStream.write("<th>Type de mesure</th>".getBytes());
			outputStream.write("<th>Nor du decret</th>".getBytes());
			outputStream.write("<th>Titre du decret</th>".getBytes());
			outputStream.write("<th>Date de publication du decret</th>".getBytes());
			outputStream.write("<th>Date de mise en application</th>".getBytes());
			outputStream.write("</tr></thead><tbody>".getBytes());

			for (final LigneProgrammationDTO lp : tableauDeProgrammationLoiDTO.getListProgrammation()) {
				outputStream.write("<tr>".getBytes());
				outputStream.write(("<td>" + (lp.getNumeroOrdre() == null ? "" : lp.getNumeroOrdre()) + "</td>")
						.getBytes());
				outputStream.write(("<td>" + (lp.getArticle() == null ? "" : lp.getArticle()) + "</td>").getBytes());
				outputStream.write(("<td>" + (lp.getBaseLegale() == null ? "" : lp.getBaseLegale()) + "</td>")
						.getBytes());
				outputStream.write(("<td>" + (lp.getObjet() == null ? "" : lp.getObjet()) + "</td>").getBytes());
				outputStream
						.write(("<td>" + (lp.getMinisterePilote() == null ? "" : lp.getMinisterePilote()) + "</td>")
								.getBytes());
				outputStream
						.write(("<td>" + (lp.getDirectionResponsable() == null ? "" : lp.getDirectionResponsable()) + "</td>")
								.getBytes());
				if (!isApplicationOrdonnance) {
					outputStream.write(("<td>" + (lp.getCategorieTexte() == null ? "" : lp.getCategorieTexte()) + "</td>")
						.getBytes());
				}
				outputStream
						.write(("<td>"
								+ (lp.getConsultationObligatoireHCE() == null ? "" : lp.getConsultationObligatoireHCE()) + "</td>")
								.getBytes());
				outputStream.write(("<td>"
						+ (lp.getCalendrierConsultationObligatoireHCE() == null ? "" : lp
								.getCalendrierConsultationObligatoireHCE()) + "</td>").getBytes());
				outputStream.write(("<td>"
						+ (lp.getDatePrevisionnelleSaisineCE() == null ? "" : sdfLong.format(lp
								.getDatePrevisionnelleSaisineCE())) + "</td>").getBytes());
				outputStream
						.write(("<td>" + (lp.getDateSaisineCE() == null ? "" : sdf.format(lp.getDateSaisineCE())) + "</td>")
								.getBytes());
				outputStream
						.write(("<td>" + (lp.getDateSortieCE() == null ? "" : sdf.format(lp.getDateSortieCE())) + "</td>")
								.getBytes());
				outputStream
						.write(("<td>"
								+ (lp.getObjectifPublication() == null ? "" : sdfLong.format(lp
										.getObjectifPublication())) + "</td>").getBytes());
				outputStream.write(("<td>" + (lp.getObservation() == null ? "" : lp.getObservation()) + "</td>")
						.getBytes());
				outputStream.write(("<td>" + (lp.getTypeMesure() == null ? "" : lp.getTypeMesure()) + "</td>")
						.getBytes());
				outputStream
						.write(("<td>" + (lp.getNorDecret() == null ? "" : lp.getNorDecret()) + "</td>").getBytes());
				outputStream.write(("<td>" + (lp.getTitreDecret() == null ? "" : lp.getTitreDecret()) + "</td>")
						.getBytes());
				outputStream
						.write(("<td>"
								+ (lp.getDatePublicationDecret() == null ? "" : sdf.format(lp
										.getDatePublicationDecret())) + "</td>").getBytes());
				outputStream
						.write(("<td>"
								+ (lp.getDateMiseApplication() == null ? "" : sdf.format(lp.getDateMiseApplication())) + "</td>")
								.getBytes());
				outputStream.write("</tr>".getBytes());
			}
			outputStream.write("</tbody></table>".getBytes());
			outputStream.close();
		} catch (final IOException ioe) {
			LOGGER.warn(session, EpgLogEnumImpl.FAIL_PUBLISH_TABLEAU_SUIVI_TEC,
					"Impossible de publier le Tableau de Suivi HTML", ioe);
		}
	}

	@Override
	public void publierEcheancierBDJ(DocumentModel currentDocument, CoreSession documentManager)
			throws ClientException {
		ArrayList<TexteMaitre> textesMaitres = new ArrayList<TexteMaitre>();
		textesMaitres.add(currentDocument.getAdapter(TexteMaitre.class));

		publierEcheancierBDJ(textesMaitres, documentManager);
	}
	
	private BilanSemestrielDTO getBilanSemestrielLoi(final CoreSession session, Date DatePromulBorneInf, 
			Date DatePromulBorneSup, Date DatePubliBorneInf, Date DatePubliBorneSup)
			throws ClientException {
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
		String strDatePromulBorneInf = formater.format(DatePromulBorneInf);
		String strDatePromulBorneSup = formater.format(DatePromulBorneSup);
		String strDatePubliBorneInf = formater.format(DatePubliBorneInf);
		String strDatePubliBorneSup = formater.format(DatePubliBorneSup);
		
		final StringBuilder query = new StringBuilder(
				"SELECT A.LOI_ID as " + FlexibleQueryMaker.COL_ID + ", D.TITREOFFICIEL AS title, nvl(A.NBR_MESURES_APPELANT_DECRETS, 0) AS " + FlexibleQueryMaker.COL_COUNT);
		query.append(", nvl(B.NBR_MESURES_RECU_APPLICATION, 0) AS language, (nvl(A.NBR_MESURES_APPELANT_DECRETS, 0) - nvl(B.NBR_MESURES_RECU_APPLICATION, 0)) AS coverage");
		query.append(" from");
		query.append(" (" + 
				" SELECT V_1.LOI_ID, COUNT(DISTINCT V_1.MESURE_ID) NBR_MESURES_APPELANT_DECRETS" + 
				" FROM" + 
				" V_AN_MESURES_APPELANT_DECRET V_1" + 
				" WHERE V_1.LOI_DATEPROMULGATION BETWEEN TO_DATE ('"+ strDatePromulBorneInf + "', 'DD/MM/YYYY') AND TO_DATE ('"+ strDatePromulBorneSup + "', 'DD/MM/YYYY')" +
				" GROUP BY V_1.LOI_ID" + 
				") A,");
		query.append("(" + 
				" SELECT V_1.LOI_ID, COUNT(DISTINCT V_1.MESURE_ID) NBR_MESURES_RECU_APPLICATION" + 
				" FROM" + 
				" V_AN_MESURES_RECU_APPLICATION V_1" + 
				" WHERE V_1.LOI_DATEPROMULGATION BETWEEN TO_DATE ('"+ strDatePromulBorneInf + "', 'DD/MM/YYYY') AND TO_DATE ('"+ strDatePromulBorneSup + "', 'DD/MM/YYYY')" +
				" AND   V_1.DECRET_DATEPUBLICATION BETWEEN TO_DATE ('"+ strDatePubliBorneInf +"', 'DD/MM/YYYY') AND TO_DATE ('"+ strDatePubliBorneSup +"', 'DD/MM/YYYY')" +
				" GROUP BY V_1.LOI_ID" + 
				") B,");
		query.append(" TEXTE_MAITRE D");
		query.append(" WHERE A.LOI_ID = B.LOI_ID (+)" + 
				" AND   A.LOI_ID = D.ID (+)" + 
				" ORDER BY D.DATEPROMULGATION, D.NUMERO");
		
		IterableQueryResult res = null;
		BilanSemestrielDTO bilanDTO = null;
		
		try {
			res = QueryUtils.doSqlQuery(session, new String[] { FlexibleQueryMaker.COL_ID, "dc:title", FlexibleQueryMaker.COL_COUNT, "dc:language","dc:coverage"},
					query.toString(), new Object[] {});
			final Iterator<Map<String, Serializable>> itr = res.iterator();
			bilanDTO = new BilanSemestrielDTO(); 
			bilanDTO.setType(BilanSemestrielType.LOI);
			bilanDTO.setDateDebutIntervalleTextesPublies(DatePromulBorneInf);
			bilanDTO.setDateFinIntervalleTextesPublies(DatePromulBorneSup);
			bilanDTO.setDateDebutIntervalleMesures(DatePubliBorneInf);
			bilanDTO.setDateFinIntervalleMesures(DatePubliBorneSup);
			
			Long totalNBMesuresAppelant = 0l;
			Long totalNBMesuresAppliquee = 0l;
			Long totalNBMesuresEnAttente = 0l;
			
			while (itr.hasNext()) {
				final Map<String, Serializable> map = itr.next();
				final Serializable loi_id = map.get(FlexibleQueryMaker.COL_ID);
				final Serializable titreofficiel = map.get("dc:title");
				final Serializable nbMesureAppelant =  map.get(FlexibleQueryMaker.COL_COUNT);
				final Serializable nbMesureAppliquee =  map.get("dc:language");
				final Serializable nbMesureEnAttente =  map.get("dc:coverage");
				
				
				totalNBMesuresAppelant = totalNBMesuresAppelant + (Long)nbMesureAppelant;
				totalNBMesuresAppliquee = totalNBMesuresAppliquee + Long.parseLong((String)nbMesureAppliquee);
				totalNBMesuresEnAttente = totalNBMesuresEnAttente + Long.parseLong((String)nbMesureEnAttente);
				
				bilanDTO.addTexte((String)loi_id, (String)titreofficiel,((Long)nbMesureAppelant).toString(),(String)nbMesureAppliquee,(String)nbMesureEnAttente);
			}
			
			bilanDTO.addTexte("0","Total",totalNBMesuresAppelant.toString(),totalNBMesuresAppliquee.toString(),totalNBMesuresEnAttente.toString());
				
			} finally {
			if (res != null) {
				res.close();
			}
		}
		return bilanDTO;
	}
	
	private BilanSemestrielDTO getBilanSemestrielOrdonnance(final CoreSession session, Date DatePromulBorneInf, 
			Date DatePromulBorneSup, Date DatePubliBorneInf, Date DatePubliBorneSup)
			throws ClientException {
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
		String strDatePromulBorneInf = formater.format(DatePromulBorneInf);
		String strDatePromulBorneSup = formater.format(DatePromulBorneSup);
		String strDatePubliBorneInf = formater.format(DatePubliBorneInf);
		String strDatePubliBorneSup = formater.format(DatePubliBorneSup);
		
		final StringBuilder query = new StringBuilder(
				"SELECT A.ORDO_ID as " + FlexibleQueryMaker.COL_ID + ", D.TITREOFFICIEL AS title, nvl(A.NBR_MESURES_APPELANT_DECRETS, 0) AS " + FlexibleQueryMaker.COL_COUNT);
		query.append(", nvl(B.NBR_MESURES_RECU_APPLICATION, 0) AS language, (nvl(A.NBR_MESURES_APPELANT_DECRETS, 0) - nvl(B.NBR_MESURES_RECU_APPLICATION, 0)) AS coverage");
		query.append(" from");
		query.append(" (" + 
				" SELECT V_1.ORDO_ID, COUNT(DISTINCT V_1.MESURE_ID) NBR_MESURES_APPELANT_DECRETS" + 
				" FROM" + 
				" V_AN_MESURES_APPEL_DECRET_ORDO V_1" + 
				" WHERE V_1.ORDO_DATEPUBLICATION BETWEEN TO_DATE ('"+ strDatePromulBorneInf + "', 'DD/MM/YYYY') AND TO_DATE ('"+ strDatePromulBorneSup + "', 'DD/MM/YYYY')" +
				" GROUP BY V_1.ORDO_ID" + 
				") A,");
		query.append("(" + 
				" SELECT V_1.ORDO_ID, COUNT(DISTINCT V_1.MESURE_ID) NBR_MESURES_RECU_APPLICATION" + 
				" FROM" + 
				" V_AN_MESURES_RECU_APP_ORDO V_1" + 
				" WHERE V_1.ORDO_DATEPROMULGATION BETWEEN TO_DATE ('"+ strDatePromulBorneInf + "', 'DD/MM/YYYY') AND TO_DATE ('"+ strDatePromulBorneSup + "', 'DD/MM/YYYY')" +
				" AND   V_1.DECRET_DATEPUBLICATION BETWEEN TO_DATE ('"+ strDatePubliBorneInf +"', 'DD/MM/YYYY') AND TO_DATE ('"+ strDatePubliBorneSup +"', 'DD/MM/YYYY')" +
				" GROUP BY V_1.ORDO_ID" + 
				") B,");
		query.append(" TEXTE_MAITRE D");
		query.append(" WHERE A.ORDO_ID = B.ORDO_ID (+)" + 
				" AND   A.ORDO_ID = D.ID (+)" + 
				" ORDER BY D.DATEPROMULGATION, D.NUMERO");
		
		IterableQueryResult res = null;
		BilanSemestrielDTO bilanDTO = null;
		
		try {
			// res = QueryUtils.doUFNXQLQuery(session, query.toString(), null);
			res = QueryUtils.doSqlQuery(session, new String[] { FlexibleQueryMaker.COL_ID, "dc:title", FlexibleQueryMaker.COL_COUNT, "dc:language","dc:coverage"},
					query.toString(), new Object[] {});
			//RESULT = new SQLQuery(select, from, where, group, null, order);
			final Iterator<Map<String, Serializable>> itr = res.iterator();
			bilanDTO = new BilanSemestrielDTO(); 
			bilanDTO.setType(BilanSemestrielType.ORDONNANCE);
			bilanDTO.setDateDebutIntervalleTextesPublies(DatePromulBorneInf);
			bilanDTO.setDateFinIntervalleTextesPublies(DatePromulBorneSup);
			bilanDTO.setDateDebutIntervalleMesures(DatePubliBorneInf);
			bilanDTO.setDateFinIntervalleMesures(DatePubliBorneSup);
			
			Long totalNBMesuresAppelant = 0l;
			Long totalNBMesuresAppliquee = 0l;
			Long totalNBMesuresEnAttente = 0l;
			
			while (itr.hasNext()) {
				final Map<String, Serializable> map = itr.next();
				final Serializable id = map.get(FlexibleQueryMaker.COL_ID);
				final Serializable titreofficiel = map.get("dc:title");
				final Serializable nbMesureAppelant =  map.get(FlexibleQueryMaker.COL_COUNT);
				final Serializable nbMesureAppliquee =  map.get("dc:language");
				final Serializable nbMesureEnAttente =  map.get("dc:coverage");
				
				
				totalNBMesuresAppelant = totalNBMesuresAppelant + (Long)nbMesureAppelant;
				totalNBMesuresAppliquee = totalNBMesuresAppliquee + Long.parseLong((String)nbMesureAppliquee);
				totalNBMesuresEnAttente = totalNBMesuresEnAttente + Long.parseLong((String)nbMesureEnAttente);
				
				bilanDTO.addTexte((String)id, (String)titreofficiel,((Long)nbMesureAppelant).toString(),(String)nbMesureAppliquee,(String)nbMesureEnAttente);
			}
			
			bilanDTO.addTexte("0","Total",totalNBMesuresAppelant.toString(),totalNBMesuresAppliquee.toString(),totalNBMesuresEnAttente.toString());
				
			} finally {
			if (res != null) {
				res.close();
			}
		}
		return bilanDTO;
	}
	
	@Override
	public boolean isPublicationEcheancierBdjActivated(CoreSession session) throws ClientException {
		STParametreService paramService = STServiceLocator.getSTParametreService();
	    String flagAffichagePublierDossier = paramService.getParametreValue(session, SolonEpgParametreConstant.ACTIVATION_PUBLICATION_ECHEANCIER_BDJ);
	    return "true".equalsIgnoreCase(flagAffichagePublierDossier) ;
	}
	
	private String getUrlWebservicePublicationBdj(CoreSession session) throws ClientException{
		STParametreService paramService = STServiceLocator.getSTParametreService();
	    String urlWebservice = paramService.getParametreValue(session, SolonEpgParametreConstant.URL_WEBSERVICE_PUBLICATION_ECHEANCIER_BDJ);
	    return urlWebservice;
	}

	@Override
	public boolean isPublicationBilanSemestrielsBdjActivated(CoreSession session) throws ClientException {
		STParametreService paramService = STServiceLocator.getSTParametreService();
	    String flagAffichagePublierDossier = paramService.getParametreValue(session, SolonEpgParametreConstant.ACTIVATION_PUBLICATION_BILAN_SEMESTRIEL_BDJ);
	    return "true".equalsIgnoreCase(flagAffichagePublierDossier) ;
	}
	
	private String getUrlWebservicePublicationBilanSemestrielBdj(CoreSession session) throws ClientException{
		STParametreService paramService = STServiceLocator.getSTParametreService();
		String urlWebservice = paramService.getParametreValue(session, SolonEpgParametreConstant.URL_WEBSERVICE_PUBLICATION_BILAN_SEMESTRIEL_BDJ);
		return urlWebservice;
	}
	
	@Override
	public void publierEcheancierBDJ(List<TexteMaitre> textesMaitres, CoreSession session) throws ClientException {
		// Générer le modèle objet injectionbdj
		String echeancierXmlStr = buildEcheancierXmlStr(session, textesMaitres);

		if (echeancierXmlStr != null) {
			StringBuilder loggerStr = new StringBuilder();
			
			try {
				// Injecter le XML dans du json
				String echeancierJson = buildEcheancierJsonStr(echeancierXmlStr);

				// Envoyer le json vers BDJ
				String bdjUrl = getUrlWebservicePublicationBdj(session);
				CloseableHttpClient httpClient = HttpClients.createDefault();
				HttpPost httpPost = new HttpPost(bdjUrl);
				InputStreamEntity reqEntity = new InputStreamEntity(
						new ByteArrayInputStream(echeancierJson.getBytes()));
				httpPost.setEntity(reqEntity);
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");
				
				loggerStr.append("[" + this.getClass().getName() + "]\n");
				loggerStr.append("---BEGIN OUT TRANSACTION url=(" + bdjUrl + ")---\n");
				
				loggerStr.append("--REQUEST--\n");
				loggerStr.append(echeancierJson);
				loggerStr.append("\n\n--RESPONSE");
				
				HttpResponse response = httpClient.execute(httpPost);
				
				loggerStr.append(" (Success)--\n" + response.toString());
			} catch (IOException e) {
				loggerStr.append(" (Failure)--\n" + e.getMessage());
			}
			
			LOGGER_INJECTION_BDJ.info(loggerStr);
			
		}
	}

	protected String buildEcheancierJsonStr(String echeancierXml) throws IOException {
		EcheancierWrapper wrapper = new EcheancierWrapper(echeancierXml);
		ObjectMapper jsonMapper = new ObjectMapper();
		String json = jsonMapper.writeValueAsString(wrapper);

		return json;
	}
	
	protected String buildEcheancierXmlStr(CoreSession documentManager, List<TexteMaitre> textesMaitres)
			throws ClientException {
		EcheancierLoisDTO echeancier = new EcheancierLoisDTO();
		
		// Récupération de la législature en cours et de la précédente
		SolonEpgParametreService paramService = SolonEpgServiceLocator.getSolonEpgParametreService();
		ParametrageAN param = paramService.getDocAnParametre(documentManager);
		String legislatureEnCours = param.getLegislatureEnCours();
		String legislaturePrecPublication = param.getLegislaturePrecPublication();
		
		for (TexteMaitre texteMaitre : textesMaitres) {
			// Filtrage des textes maitres selon la législature : ne sont
			// publiés que les textes maitres de la législature en cours ou de
			// la précédente
			String legislature = texteMaitre.getLegislaturePublication();
			if (StringUtil.isNotEmpty(legislature)
					&& (legislature.equals(legislatureEnCours) || (StringUtil.isNotEmpty(legislaturePrecPublication)
							&& legislature.equals(legislaturePrecPublication)))) {

				TexteMaitreDTO texteMaitreDTO = new TexteMaitreDTO(texteMaitre.getNumero(), texteMaitre.getNumeroNor());

				List<MesureApplicative> listMesures = fetchMesure(texteMaitre.getMesuresIds(), documentManager);
				
				for (MesureApplicative mesure : listMesures) {
					String numOrdreStr = mesure.getNumeroOrdre();
					if (NumberUtils.isNumber(numOrdreStr)) {
						int numOrdre = Integer.parseInt(numOrdreStr);

						if (numOrdre != 0) {
							MesureApplicationDTO mesureDTO = new MesureApplicationDTO();

							mesureDTO.setNumero(numOrdre);

							mesureDTO.setArticleTexte(mesure.getArticle());
							mesureDTO.setBaseLegale(mesure.getBaseLegale());

							Calendar dateObjPub = mesure.getDateObjectifPublication();
							if (dateObjPub != null) {
								mesureDTO.setObjectif(DateUtil.formatMMMMYYYY(dateObjPub.getTime()));
							}

							mesureDTO.setObjet(mesure.getObjetRIM());

							List<Decret> listDecret = fetchDecrets(mesure.getDecretIds(), documentManager);
							for (Decret decret : listDecret) {
								DecretApplicatifDTO decretDTO = new DecretApplicatifDTO();
								decretDTO.setNor(decret.getNumeroNor());
								decretDTO.setTitreLong(decret.getTitreOfficiel());

								mesureDTO.getDecrets().add(decretDTO);
							}

							texteMaitreDTO.getMesures().add(mesureDTO);
						}
					}
				}
				
				// Classer les mesures par ordre croissant du numéro d'ordre
				Collections.sort(texteMaitreDTO.getMesures(), new Comparator<MesureApplicationDTO>() {
					@Override
					public int compare(MesureApplicationDTO mesure1, MesureApplicationDTO mesure2) {
						return mesure1.getNumero().compareTo(mesure2.getNumero());
					}
				});

				echeancier.getTextes().add(texteMaitreDTO);
			}
			
			// Mettre a jour la date d'injection du texte maitre
			texteMaitre.setDateInjection(Calendar.getInstance());
			texteMaitre.save(documentManager);
		}
		
		if(echeancier.getTextes().isEmpty()) {
			return null;
		}

		// Générer le XML à partir du texte maitre
		String echeancierStr = mapToXml(echeancier);
		return echeancierStr;
	}
	
	private String mapToXml(EcheancierLoisDTO echeancier) {
		StringBuilder builder = new StringBuilder();

		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		builder.append("<textes xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");

		for(TexteMaitreDTO texteMaitre : echeancier.getTextes()) {
			builder.append("<Texte id=\"" + StringUtil.defaultString(texteMaitre.getId()) + "\" nor=\""
					+ texteMaitre.getNor() + "\">");

			for (MesureApplicationDTO mesure : texteMaitre.getMesures()) {
				builder.append(
						"<mesure" + (mesure.getNumero() != null ? (" num=\"" + mesure.getNumero() + "\"") : "") + ">");

				builder.append(
						"<articleTexte>" + StringUtil.defaultString(mesure.getArticleTexte()) + "</articleTexte>");
				builder.append("<baseLegale>" + StringUtil.defaultString(mesure.getBaseLegale()) + "</baseLegale>");
				builder.append("<objet>" + StringUtil.defaultString(mesure.getObjet()) + "</objet>");
				builder.append("<objectif>" + StringUtil.defaultString(mesure.getObjectif()) + "</objectif>");

				for (DecretApplicatifDTO decret : mesure.getDecrets()) {
					builder.append("<decret>");
					builder.append("<nor>" + decret.getNor() + "</nor>");

					if (decret.getTitreLong() != null) {
						builder.append("<titre_long>" + decret.getTitreLong() + "</titre_long>");
					} else {
						builder.append("<titre_long/>");
					}

					builder.append("</decret>");
				}

				builder.append("</mesure>");
			}
			
			builder.append("</Texte>");
		}
		
		builder.append("</textes>");

		return builder.toString();
	}

	protected String buildBilanXmlStr(BilanSemestrielDTO bilanSemestriel, String legislatureEnCours) {
		
		
		String strXmlDateDebutLois = formaterXML.format(bilanSemestriel.getDateDebutIntervalleTextesPublies());
		String strXmlDatestrFinLois = formaterXML.format(bilanSemestriel.getDateFinIntervalleTextesPublies());
		String strXmlDateFinMesures = formaterXML.format(bilanSemestriel.getDateFinIntervalleMesures());
		
		
		String strHDateDebutLois = formaterFR.format(bilanSemestriel.getDateDebutIntervalleTextesPublies());
		String strHDateDebutMesures = formaterFR.format(bilanSemestriel.getDateDebutIntervalleMesures());
		String strHDatestrFinLois = formaterFR.format(bilanSemestriel.getDateFinIntervalleTextesPublies());
		String strHDateFinMesures = formaterFR.format(bilanSemestriel.getDateFinIntervalleMesures());
		String strDateBilan = formaterFR.format(bilanSemestriel.getDateBilan());
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		builder.append("<bilan texte_type=\"");
		builder.append(bilanSemestriel.getType().getLabel());
		builder.append("\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		
		if (BilanSemestrielDTO.BilanSemestrielType.LOI.equals(bilanSemestriel.getType())) {
			builder.append("<texte_pan> <p> Bilan semestriel à la date du " + strDateBilan + " pour les lois promulguées entre le " +
					strHDateDebutLois + " et le " + strHDatestrFinLois +", et les mesures ayant reçu application entre le " +
					strHDateDebutMesures +" et le " + strHDateFinMesures +"</p></texte_pan>");
		} else {
			builder.append("<texte_pan> <p> Bilan semestriel à la date du " + strDateBilan + " pour les ordonnances publiées entre le " +
					strHDateDebutLois + " et le " + strHDatestrFinLois +", et les mesures ayant reçu application entre le " +
					strHDateDebutMesures +" et le " + strHDateFinMesures +"</p></texte_pan>");
		}
		
		builder.append("<date_debut_intervalle>" + strXmlDateDebutLois + "</date_debut_intervalle>");
		builder.append("<date_fin_intervalle_textes_publies>" + strXmlDatestrFinLois + "</date_fin_intervalle_textes_publies>");
		builder.append("<date_fin_intervalle_mesures>" + strXmlDateFinMesures + "</date_fin_intervalle_mesures>");
		
		builder.append("<titre_court_lgf>Bilan semestriel au ");
		builder.append(formaterFREtendu.format(bilanSemestriel.getDateBilan()));
		builder.append("</titre_court_lgf>");
		
		builder.append("<texte_edito_lgf>");
		if (BilanSemestrielDTO.BilanSemestrielType.LOI.equals(bilanSemestriel.getType())) {
			builder.append(buildTexteEditolgfLoi(legislatureEnCours, bilanSemestriel.getDateFinIntervalleMesures(), 
					bilanSemestriel.getDateDebutIntervalleMesures(), bilanSemestriel.getDateFinIntervalleTextesPublies()));
		} else {
			builder.append(buildTexteEditolgfOrdonnance(legislatureEnCours, bilanSemestriel.getDateFinIntervalleMesures(), 
					bilanSemestriel.getDateDebutIntervalleMesures(), bilanSemestriel.getDateFinIntervalleTextesPublies()));
		}
		builder.append("</texte_edito_lgf>");
		
		
		if (BilanSemestrielDTO.BilanSemestrielType.LOI.equals(bilanSemestriel.getType())) {
			builder.append("<titre_col_1>");
			builder.append("Titre de la loi");
			builder.append("</titre_col_1>");
		} else {
			builder.append("<titre_col_1>");
			builder.append("Titre de l'ordonnance");
			builder.append("</titre_col_1>");
		}
		builder.append("<titre_col_2>");
		builder.append("Nombre de mesures appelant un décret d’application");
		builder.append("</titre_col_2>");
		
		builder.append("<titre_col_3>");
		builder.append("Nombre de mesures ayant reçu application");
		builder.append("</titre_col_3>");
		
		builder.append("<titre_col_4>");
		builder.append("Taux d’application");
		builder.append("</titre_col_4>");
		
		builder.append("<titre_col_5>");
		builder.append("Nombre de mesures en attente de décret d‘application");
		builder.append("</titre_col_5>");
		
		
		builder.append("<textes>");
		List<TexteBilanSemestrielDTO> ListeTextes = bilanSemestriel.getListeTextes();
		Iterator itr = ListeTextes.iterator();
		
		while (itr.hasNext()) {
			TexteBilanSemestrielDTO bilan = (TexteBilanSemestrielDTO)itr.next();
			builder.append(buildTexteXmlStr(bilan));
		}
		
		builder.append("</textes>");
		builder.append("</bilan>");
		return builder.toString();
		
	}
	
	
	private String buildTexteXmlStr(TexteBilanSemestrielDTO texteBilanSemestriel) {
		
		final NumberFormat tauxFormater = NumberFormat.getInstance(Locale.ENGLISH);
		tauxFormater.setMaximumFractionDigits(2);
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("<texte texte_type=\"" + texteBilanSemestriel.getType() + "\"");
		builder.append(" titre=\"" + texteBilanSemestriel.getTitre() + "\"");
		builder.append(" mesures_attendues=\"" + texteBilanSemestriel.getMesuresAttendues() + "\"");
		builder.append(" mesures_appliquees=\"" + texteBilanSemestriel.getMesuresAppliquees() + "\"");
		builder.append(" taux=\"" + tauxFormater.format(texteBilanSemestriel.getTaux()) + "\"");
		builder.append(" mesures_en_attente=\"" + texteBilanSemestriel.getMesuresEnAttente() + "\"/>");
		
		return builder.toString();
	}
	
	/**
	 * Calcul le tag texte_edito_lgf pour l'export des bilans semestriels lois xml à la BDJ
	 * @return
	 */
	private String buildTexteEditolgfLoi(String numeroLegislatureCourante, Date dateFinIntervalleMesures, 
			Date dateDebutIntervalle, Date dateFinIntervalleTextesPublies) {
		
		StringBuilder builder = new StringBuilder(); 
		
		String rangBilan = calculRangBilanLegislature(dateDebutIntervalle, dateFinIntervalleTextesPublies);
		
		builder.append("<p>Le " + rangBilan + " bilan semestriel des lois de la " + numeroLegislatureCourante);
		builder.append("ème législature, présenté ici, fait apparaître le taux d'exécution au " + formaterFREtendu.format(dateFinIntervalleMesures));
		builder.append(" de l'ensemble des lois qui, parmi celles promulguées entre le " + formaterFREtendu.format(dateDebutIntervalle));
		builder.append(" et " + formaterFREtendu.format(dateFinIntervalleTextesPublies) + ", appellent des décrets d'application.</p>");
		
		builder.append("<p>On entend par taux d'exécution la proportion des dispositions de la loi appelant un décret d'application pour lesquelles les décrets attendus avaient été pris au ");
		builder.append(formaterFREtendu.format(dateFinIntervalleMesures) + ".</p>");
		
		builder.append("<p>Pour prendre connaissance de l'évolution de la situation au-delà du ");
		builder.append(formaterFREtendu.format(dateFinIntervalleMesures) + ", il convient de consulter les échéanciers mis à jour dès qu'une mesure d'application est publiée (échéanciers des lois publiées sous la ");
		builder.append(numeroLegislatureCourante + "ème législature).</p>");
		
		return builder.toString();
	}
	
	/**
	 * Calcul le tag texte_edito_lgf pour l'export des bilans semestriels ordonnance xml à la BDJ
	 * @return
	 */
	private String buildTexteEditolgfOrdonnance(String numeroLegislatureCourante, Date dateFinIntervalleMesures, 
			Date dateDebutIntervalle, Date dateFinIntervalleTextesPublies) {
		
		StringBuilder builder = new StringBuilder(); 
		
		String rangBilan = calculRangBilanLegislature(dateDebutIntervalle, dateFinIntervalleTextesPublies);
		
		builder.append("<p>Le " + rangBilan + " bilan semestriel des ordonnances de la " + numeroLegislatureCourante);
		builder.append("ème législature, présenté ici, fait apparaître le taux d'exécution au " + formaterFREtendu.format(dateFinIntervalleMesures));
		builder.append(" de l'ensemble des ordonnances qui, parmi celles promulguées entre le " + formaterFREtendu.format(dateDebutIntervalle));
		builder.append(" et " + formaterFREtendu.format(dateFinIntervalleTextesPublies) + ", appellent des décrets d'application.</p>");
		
		builder.append("<p>On entend par taux d'exécution la proportion des dispositions de l'ordonnance appelant un décret d'application pour lesquelles les décrets attendus avaient été pris au ");
		builder.append(formaterFREtendu.format(dateFinIntervalleMesures) + ".</p>");
		
		builder.append("<p>Pour prendre connaissance de l'évolution de la situation au-delà du ");
		builder.append(formaterFREtendu.format(dateFinIntervalleMesures) + ", il convient de consulter les échéanciers mis à jour dès qu'une mesure d'application est publiée (échéanciers des lois publiées sous la ");
		builder.append(numeroLegislatureCourante + "ème législature).</p>");
		
		return builder.toString();
	}
	
	/**
	 * Calcule le rang de la législature.
	 * @param dateDebutIntervalle
	 * @param dateFinIntervalleTextesPublies
	 * @return
	 */
	protected String calculRangBilanLegislature(Date dateDebutIntervalle, Date dateFinIntervalleTextesPublies) {
		Calendar calDebutIntervalle = Calendar.getInstance();
		calDebutIntervalle.setTime(dateDebutIntervalle);
		
		Calendar calFinIntervalleTextesPublies = Calendar.getInstance();
		calFinIntervalleTextesPublies.setTime(dateFinIntervalleTextesPublies);
		
		calDebutIntervalle.add(Calendar.MONTH, 6);
		for (int rangBilan = 0; rangBilan <= 9; rangBilan++) {
			if (calDebutIntervalle.after(calFinIntervalleTextesPublies)) {
				int rangReturn = (rangBilan-1);
				if (rangReturn == 1) {
					return "1er";
				} else {
					return String.valueOf(rangReturn) + "ème";
				}
			}
			calDebutIntervalle.add(Calendar.MONTH, 6);
		}
		
		return "0";
	}
	
	@Override
	public void publierBilanSemestrielLoiBDJ(CoreSession session, String legislatureEnCours, Date DatePromulBorneInf, 
			Date DatePromulBorneSup, Date DatePubliBorneInf, Date DatePubliBorneSup) throws ClientException {
		LOGGER_INJECTION_BDJ.info("Publication bilan semestriel loi. Creation du bilan XML.");
		BilanSemestrielDTO bilansSemestriels = getBilanSemestrielLoi(session, DatePromulBorneInf, DatePromulBorneSup, DatePubliBorneInf, DatePubliBorneSup);
		LOGGER_INJECTION_BDJ.info("Publication bilan semestriel loi. Envoi par Webservice.");
		publierBilanSemestrielBDJ(bilansSemestriels, legislatureEnCours, session);
	}
	
	@Override
	public void publierBilanSemestrielOrdonnanceBDJ(CoreSession session, String legislatureEnCours, Date DatePromulBorneInf, 
			Date DatePromulBorneSup, Date DatePubliBorneInf, Date DatePubliBorneSup) throws ClientException {
		LOGGER_INJECTION_BDJ.info("Publication bilan semestriel ordonnance. Creation du bilan XML.");
		BilanSemestrielDTO bilansSemestriels = getBilanSemestrielOrdonnance(session, DatePromulBorneInf, DatePromulBorneSup, DatePubliBorneInf, DatePubliBorneSup);
		LOGGER_INJECTION_BDJ.info("Publication bilan semestriel ordonnance. Envoi par Webservice.");
		publierBilanSemestrielBDJ(bilansSemestriels, legislatureEnCours, session);
	}
	
	
	private void publierBilanSemestrielBDJ(BilanSemestrielDTO bilanSemestriel, String legislatureEnCours, CoreSession session) {
		
			final StringBuilder loggerStr = new StringBuilder();
			
			String bilanSemestrielXml = buildBilanXmlStr(bilanSemestriel, legislatureEnCours);
			
			try {
				// Injecter le XML dans du json
				String bilanJson = buildEcheancierJsonStr(bilanSemestrielXml); // TODO : renommer cette méthode
				
				// Envoyer le json vers BDJ
				String bdjUrl = getUrlWebservicePublicationBilanSemestrielBdj(session);
				final CloseableHttpClient httpClient = HttpClients.createDefault();
				final HttpPost httpPost = new HttpPost(bdjUrl);
				InputStreamEntity reqEntity = new InputStreamEntity(
						new ByteArrayInputStream(bilanJson.getBytes()));
				httpPost.setEntity(reqEntity);
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");
				
				loggerStr.append("[" + this.getClass().getName() + "]\n");
				loggerStr.append("---BEGIN OUT TRANSACTION url=(" + bdjUrl + ")---\n");
				
				loggerStr.append("--REQUEST--\n");
				loggerStr.append(bilanJson);
				loggerStr.append("\n\n--RESPONSE");
				
				HttpResponse response = httpClient.execute(httpPost);
				loggerStr.append(" (Success)--\n" + response.toString());
				
			} catch (IOException e) {
				loggerStr.append(" (Failure)--\n" + e.getMessage());
			} catch (ClientException e) {
				loggerStr.append(" (Failure)--\n" + e.getMessage());
			}
			
			LOGGER_INJECTION_BDJ.info(loggerStr);
	}
	
	@Override
	public ActiviteNormative findActiviteNormativeByNor(final String numeroNor, final CoreSession session)
			throws ClientException {
		final StringBuilder queryBuilder = new StringBuilder(SELECT_UUID_FROM_ACT_NORM_AS_A);
		queryBuilder.append(" WHERE a.");
		queryBuilder.append(TexteMaitreConstants.TEXTE_MAITRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(TexteMaitreConstants.NUMERO_NOR);
		queryBuilder.append(" = ? ");

		final Object[] params = new Object[] { numeroNor };

		final List<DocumentModel> res = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE, queryBuilder.toString(), params, 1, 0);
		if (res != null && res.size() == 1) {
			final DocumentModel doc = res.get(0);
			return doc.getAdapter(ActiviteNormative.class);
		}

		return null;
	}

	@Override
	public Map<String, ActiviteNormative> findActiviteNormativeByNors(final List<String> numeroNors,
			final CoreSession session) throws ClientException {
		Map<String, ActiviteNormative> byNors = new HashMap<String, ActiviteNormative>();

		if (numeroNors.isEmpty()) {
			return byNors;
		}
		final StringBuilder queryBuilder = new StringBuilder(SELECT_UUID_FROM_ACT_NORM_AS_A);
		queryBuilder.append(" WHERE ");
		queryBuilder.append(" a.");
		queryBuilder.append(TexteMaitreConstants.TEXTE_MAITRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(TexteMaitreConstants.NUMERO_NOR);
		queryBuilder.append(" IN (").append(StringUtil.getQuestionMark(numeroNors.size())).append(") ");

		Object[] params = numeroNors.toArray(new Object[numeroNors.size()]);

		final List<DocumentModel> res = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, queryBuilder.toString(),
				params, 0, 0, PREFECTH_INFO_CHGT_GVT);

		if (res != null) {
			for (DocumentModel doc : res) {
				final String nor = PropertyUtil.getStringProperty(doc, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
						TexteMaitreConstants.NUMERO_NOR);
				byNors.put(nor, doc.getAdapter(ActiviteNormative.class));
			}
		}

		return byNors;
	}

	@Override
	public List<LoiDeRatification> fetchLoiDeRatification(final List<String> loiRatificationIds,
			final CoreSession session) {
		final List<LoiDeRatification> list = new ArrayList<LoiDeRatification>();
		for (final String id : loiRatificationIds) {
			try {
				final DocumentModel doc = session.getDocument(new IdRef(id));
				list.add(doc.getAdapter(LoiDeRatification.class));
			} catch (final Exception e) {
				// probleme de droit / mesure supprimée /etc...
				LOGGER.warn(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC,
						"Impossible de recuperer la LoiDeRatification " + id, e);
			}
		}

		return list;
	}

	@Override
	public void saveDecretsOrdonnance(final String idCurrentOrdonnance, final List<DecretApplicationDTO> listDecret,
			final CoreSession session) throws ClientException {

		final Set<String> decretIds = new HashSet<String>();
		final Set<String> error = new HashSet<String>();

		for (final DecretApplicationDTO decretApplicationDTO : listDecret) {
			if (StringUtil.isEmpty(decretApplicationDTO.getNumeroNor())) {
				error.add("Le NOR est obligatoire.");
			} else {
				String identifier = null;
				Decret decret = null;
				boolean norOk = true;
				try {
					// Vérif de l'existence du NOR ; une exception est levée
					// s'ill n'existe pas
					checkIsDecretFromNumeroNOR(decretApplicationDTO.getNumeroNor(), session);
				} catch (ActiviteNormativeException e) {
					error.add(decretApplicationDTO.getNumeroNor());
					norOk = false;
				}

				if (norOk) {
					if (StringUtil.isNotEmpty(decretApplicationDTO.getId())) {
						decret = session.getDocument(new IdRef(decretApplicationDTO.getId())).getAdapter(Decret.class);

						final ActiviteNormative activiteNormative = findActiviteNormativeByNor(
								decretApplicationDTO.getNumeroNor(), session);
						if (activiteNormative == null
								|| activiteNormative.getDocument().getId().equals(decretApplicationDTO.getId())) {
							decret = session.getDocument(new IdRef(decretApplicationDTO.getId())).getAdapter(
									Decret.class);
						} else {
							// si on remplace par un numeroNor d'un autre decret
							// on recupere cet autre decret
							final Set<String> setOrdonnance = new HashSet<String>(decret.getOrdonnanceIds());
							setOrdonnance.remove(idCurrentOrdonnance);

							decret.setMesureIds(new ArrayList<String>(setOrdonnance));
							decret.save(session);
							decret = activiteNormative.getAdapter(Decret.class);
						}

						if (activiteNormative != null) {
							decret.setIdDossier(idCurrentOrdonnance);
						}

						decret = decretApplicationDTO.remapField(decret);

						final Set<String> setOrdonnance = new HashSet<String>(decret.getOrdonnanceIds());
						setOrdonnance.add(idCurrentOrdonnance);
						decret.setOrdonnanceIds(new ArrayList<String>(setOrdonnance));
						if (!decret.hasValidation()) {
							decret.setValidation(Boolean.TRUE);
						}

						decret.save(session);
						identifier = decret.getId();

					} else {

						// recherche du decret par numeroNor
						final ActiviteNormative activiteNormative = findOrCreateActiviteNormativeByNor(session,
								decretApplicationDTO.getNumeroNor());
						decret = activiteNormative.getAdapter(Decret.class);

						decret.setIdDossier(idCurrentOrdonnance);

						decret = decretApplicationDTO.remapField(decret);
						if (!decret.hasValidation()) {
							decret.setValidation(Boolean.TRUE);
						}

						final Set<String> setOrdonnance = new HashSet<String>(decret.getOrdonnanceIds());
						setOrdonnance.add(idCurrentOrdonnance);
						decret.setOrdonnanceIds(new ArrayList<String>(setOrdonnance));

						session.saveDocument(decret.getDocument());
						identifier = decret.getId();
					}
					decretApplicationDTO.setValidationLink(Boolean.TRUE);
				}

				if (identifier == null) {
					TransactionHelper.setTransactionRollbackOnly();
					LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_DOCUMENT_TEC,
							"Impossible de generer un id pour le decret " + decretApplicationDTO.getNumeroNor());
				} else {
					decretIds.add(identifier);
				}

			}

		}
		if (error.isEmpty()) {
			final Ordonnance ordonnance = session.getDocument(new IdRef(idCurrentOrdonnance)).getAdapter(
					Ordonnance.class);

			// delinkage des decrets precedents
			for (final Decret decret : fetchDecrets(ordonnance.getDecretIds(), session)) {
				if (!decretIds.contains(decret.getId())) {
					final Set<String> setOrdonnance = new HashSet<String>(decret.getOrdonnanceIds());
					setOrdonnance.remove(idCurrentOrdonnance);

					decret.setOrdonnanceIds(new ArrayList<String>(setOrdonnance));
					decret.save(session);
				}
			}
			ordonnance.setDecretIds(new ArrayList<String>(decretIds));
			ordonnance.setDecretsIdsInvalidated(null);
			ordonnance.save(session);

			session.save();
		} else {
			TransactionHelper.setTransactionRollbackOnly();
			throw new ActiviteNormativeException(error);
		}

	}

	@Override
	public void attachOrdonnanceToLoiHabilitation(final Dossier dossierOrdonnance, final CoreSession session)
			throws ClientException {
		final String idOrdonnance = dossierOrdonnance.getDocument().getId();

		if (StringUtil.isNotEmpty(idOrdonnance)) {

			final String numeroLoi = dossierOrdonnance.getHabilitationRefLoi();
			final String numeroOrdre = dossierOrdonnance.getHabilitationNumeroOrdre();

			if (StringUtil.isNotBlank(numeroOrdre) && StringUtil.isNotBlank(numeroLoi)) {
				LOGGER.debug(STLogEnumImpl.DEFAULT, "On débute le rajout de l'ordonnance aux mesures d'habilitations");

				dossierOrdonnance.setDispositionHabilitation(Boolean.TRUE);
				dossierOrdonnance.save(session);

				// Récupération de la loi
				DocumentModel activiteNormativeDoc = findActiviteNormativeDocByNumero(session, numeroLoi);
				if (activiteNormativeDoc != null) {
					// creation de l'habilitation
					final Habilitation mesureHabilitation = findMesureHabilitationForLoi(session, numeroOrdre,
							activiteNormativeDoc.getId());
					if (mesureHabilitation != null) {

						final ActiviteNormative activiteNormativeOrdonnance = findOrCreateActiviteNormativeByNor(
								session, dossierOrdonnance.getNumeroNor());
						final Ordonnance ordonnance = activiteNormativeOrdonnance.getAdapter(Ordonnance.class);

						final List<String> listIdOrdonnance = mesureHabilitation.getOrdonnanceIds();
						final Set<String> setIdsMesureHab = new HashSet<String>(ordonnance.getHabilitationIds());
						final Set<String> setIdsOrdonnanceInvalidated = new HashSet<String>(
								mesureHabilitation.getOrdonnanceIdsInvalidated());

						if (!listIdOrdonnance.contains(ordonnance.getId())) {
							String ordonnanceId = ordonnance.getId();
							listIdOrdonnance.add(ordonnanceId);
							mesureHabilitation.setOrdonnanceIds(listIdOrdonnance);
							// Si l'ordonnance est rattachée à aucune habilitation mais validée, il faut l'invalider
							if (setIdsMesureHab.isEmpty() && ordonnance.hasValidation()) {
								ordonnance.setValidation(Boolean.FALSE);
							}
							setIdsOrdonnanceInvalidated.add(ordonnanceId);
							setIdsMesureHab.add(mesureHabilitation.getId());
							ordonnance.setHabilitationIds(new ArrayList<String>(setIdsMesureHab));
							mesureHabilitation.setOrdonnanceIdsInvalidated(new ArrayList<String>(
									setIdsOrdonnanceInvalidated));
							session.saveDocument(ordonnance.getDocument());
							session.saveDocument(mesureHabilitation.getDocument());
						}

						final TexteMaitre texteMaitre = activiteNormativeDoc.getAdapter(TexteMaitre.class);

						List<String> habilitationsIds = texteMaitre.getHabilitationIds();

						if (habilitationsIds == null) {
							habilitationsIds = new ArrayList<String>();
						}

						if (!habilitationsIds.contains(mesureHabilitation.getId())) {
							habilitationsIds.add(mesureHabilitation.getId());
							texteMaitre.setHabilitationIds(new ArrayList<String>(habilitationsIds));
							session.saveDocument(texteMaitre.getDocument());
						}

						session.save();
					}
				}
			} else {
				if (dossierOrdonnance.isDispositionHabilitation() == null) {
					// On set seulement si dispositionHabilitation est null. Car sinon ça écrase la valeur d'avant
					dossierOrdonnance.setDispositionHabilitation(Boolean.FALSE);
				}
				dossierOrdonnance.save(session);
			}
		}
	}

	@Override
	public List<DocumentModel> getAllAplicationLoiDossiers(final CoreSession session, boolean legislatureEnCours)
			throws ClientException {

		final StringBuilder query = new StringBuilder(
				"SELECT d.ecm:uuid as id FROM ActiviteNormative as d WHERE d.norma:applicationLoi = '1'");
		String legislature = getLegislatureParam(session, legislatureEnCours);
		Object[] queryParams = new Object[1];

		if (legislature != null) {
			query.append(" AND d.texm:legislaturePublication = ?");
			queryParams[0] = legislature;
		} else {
			queryParams = null;
		}

		query.append(" ORDER BY d.texm:datePromulgation DESC");

		final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE, query.toString(), queryParams);
		return docs;
	}

	private String getLegislatureParam(CoreSession session, boolean legislatureEnCours) throws ClientException {
		String legis = null;
		SolonEpgParametreService paramService = SolonEpgServiceLocator.getSolonEpgParametreService();
		ParametrageAN parametre = paramService.getDocAnParametre(session);

		if (legislatureEnCours) {

			legis = parametre.getLegislatureEnCours();
		} else {
			if (parametre.getLegislatures() != null && parametre.getLegislatures().size() - 2 >= 0) {
				int nbLegis = parametre.getLegislatures().indexOf(parametre.getLegislatureEnCours()) - 1;
				legis = parametre.getLegislatures().get(nbLegis);
			}
		}

		return legis;
	}

	@Override
	public List<DocumentModel> getAllLoiHabilitationDossiers(final CoreSession session, boolean curLegis)
			throws ClientException {
		final StringBuilder query = new StringBuilder(
				"ufnxql: SELECT d.ecm:uuid as id FROM ActiviteNormative as d WHERE d.norma:ordonnance38C = '1'");
		String legislature = getLegislatureParam(session, curLegis);
		Object[] queryParams = new Object[1];

		if (legislature != null) {
			query.append(" AND d.texm:legislaturePublication = ?");
			queryParams[0] = legislature;
		} else {
			queryParams = null;
		}

		query.append(" ORDER BY d.texm:datePromulgation DESC");

		final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE, query.toString(), queryParams);
		return docs;
	}

	@Override
	public List<String> getAllAplicationMinisteresList(final CoreSession session, boolean curLegis)
			throws ClientException {
		final StringBuilder query = new StringBuilder(
				"SELECT Distinct d.texm:ministerePilote as ministere FROM ActiviteNormative as d WHERE d.norma:applicationLoi = '1'");
		IterableQueryResult res = null;
		final List<String> ministereIdsList = new ArrayList<String>();
		String legislature = getLegislatureParam(session, curLegis);
		Object[] queryParams = new Object[1];

		if (legislature != null) {
			query.append(" AND d.texm:legislaturePublication = ?");
			queryParams[0] = legislature;
		} else {
			queryParams = null;
		}
		query.append(" group by d.texm:ministerePilote ");

		try {
			res = QueryUtils.doUFNXQLQuery(session, query.toString(), queryParams);
			final Iterator<Map<String, Serializable>> itr = res.iterator();
			while (itr.hasNext()) {
				final Map<String, Serializable> map = itr.next();
				final Serializable identifier = map.get("ministere");
				if (identifier == null) {
					LOGGER.warn(session, STLogEnumImpl.FAIL_GET_PARAM_TEC,
							"Result map should contains 'ministere' (probably 'AS ministere' missing)");
				} else {
					ministereIdsList.add((String) identifier);
				}
			}
		} finally {
			if (res != null) {
				res.close();
			}
		}
		return ministereIdsList;
	}

	@Override
	public List<String> getAllHabilitationMinisteresList(final CoreSession session, boolean curLegis)
			throws ClientException {
		final StringBuilder query = new StringBuilder(
				"SELECT Distinct d.texm:ministerePilote as ministere FROM ActiviteNormative as d WHERE d.norma:ordonnance38C = '1'");
		IterableQueryResult res = null;
		final List<String> ministereIdsList = new ArrayList<String>();
		String legislature = getLegislatureParam(session, curLegis);
		Object[] queryParams = new Object[1];

		if (legislature != null) {
			query.append(" AND d.texm:legislaturePublication = ?");
			queryParams[0] = legislature;
		} else {
			queryParams = null;
		}
		query.append(" group by d.texm:ministerePilote ");

		try {
			res = QueryUtils.doUFNXQLQuery(session, query.toString(), queryParams);
			final Iterator<Map<String, Serializable>> itr = res.iterator();
			while (itr.hasNext()) {
				final Map<String, Serializable> map = itr.next();
				final Serializable identifier = map.get("ministere");
				if (identifier == null) {
					LOGGER.warn(session, STLogEnumImpl.FAIL_GET_PARAM_TEC,
							"Result map should contains 'ministere' (probably 'AS ministere' missing)");
				} else {
					ministereIdsList.add((String) identifier);
				}
			}
		} finally {
			if (res != null) {
				res.close();
			}
		}
		return ministereIdsList;
	}

	@Override
	public void setPublicationInfo(final RetourDila retourDila, final CoreSession session) throws ClientException {

		final Dossier dossier = retourDila.getDocument().getAdapter(Dossier.class);

		final ActiviteNormative activiteNormative = findOrCreateActiviteNormativeByNor(session, dossier.getNumeroNor());

		final Decret decret = activiteNormative.getAdapter(Decret.class);

		if (decret != null) {
			if (!decret.isDatePublicationLocked()) {
				decret.setDatePublication(retourDila.getDateParutionJorf());
			}
			if (!decret.isTitreOfficielLocked()) {
				decret.setTitreOfficiel(retourDila.getTitreOfficiel());
			}
			decret.setNumerosTextes(retourDila.getNumeroTexteParutionJorf());
			decret.setDateSignature(dossier.getDateSignature());
		}

		final TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);

		if (texteMaitre != null) {
			if (!texteMaitre.isTitreOfficielLocked()) {
				texteMaitre.setDatePromulgation(extractDateFromTitre(retourDila.getTitreOfficiel()));
				texteMaitre.setTitreOfficiel(retourDila.getTitreOfficiel());
			}
			if (!texteMaitre.isDatePublicationLocked()) {
				texteMaitre.setDatePublication(retourDila.getDateParutionJorf());
			}
			if (!texteMaitre.isLegislaturePublicationLocked()) {
				texteMaitre.setLegislaturePublication(retourDila.getLegislaturePublication());
			}
			if (!texteMaitre.isNumeroLocked()) {
				texteMaitre.setNumero(retourDila.getNumeroTexteParutionJorf());
			}
			
			updateAndSaveTexteMaitreDateModification(session, texteMaitre);

		}

		session.saveDocument(activiteNormative.getDocument());
		session.save();

		// ajout des ordonnances dans le tableau des ratifications d'ordonnances
		final SolonEpgVocabularyService vocabularyService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
		final DocumentModel doc = vocabularyService.getDocumentModelFromId(
				VocabularyConstants.VOCABULARY_TYPE_ACTE_DIRECTORY, dossier.getTypeActe());
		final String normative = (String) doc.getProperty(VocabularyConstants.VOCABULARY_TYPE_ACTE_SCHEMA,
				VocabularyConstants.VOCABULARY_TYPE_ACTE_ACTIVITE_NORMATIVE);

		if (ActiviteNormativeConstants.TYPE_ORDONNANCES.equals(normative)) {
			addDossierToTableau(dossier.getNumeroNor(), ActiviteNormativeEnum.ORDONNANCES.getTypeActiviteNormative(),
					session);
		}

	}

	@Override
	public void checkNumeroInterne(final TexteMaitre texteMaitre, final String numeroInterne, final CoreSession session)
			throws ClientException {
		ActiviteNormative an = texteMaitre.getAdapter(ActiviteNormative.class);
		boolean fromAppLoi = ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE.equals(an.getApplicationLoi());
		boolean fromAppOrdo = ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE
				.equals(an.getApplicationOrdonnance());
		if (fromAppLoi || fromAppOrdo) {
			final StringBuilder queryBuilder = new StringBuilder(SELECT_UUID_FROM_ACT_NORM_AS_A);
			queryBuilder.append(" WHERE a.ecm:uuid != ? AND a.");
			queryBuilder.append(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_PREFIX);
			queryBuilder.append(":");
			if (fromAppLoi) {
				queryBuilder.append(ActiviteNormativeConstants.P_APPLICATION_LOI);
			} else if (fromAppOrdo) {
				queryBuilder.append(ActiviteNormativeConstants.P_APPLICATION_ORDONNANCE);
			}
			queryBuilder.append(" = ? AND a.");
			queryBuilder.append(TexteMaitreConstants.TEXTE_MAITRE_PREFIX);
			queryBuilder.append(":");
			queryBuilder.append(TexteMaitreConstants.NUMERO_INTERNE);
			queryBuilder.append(" = ? ");

			final Object[] params = { texteMaitre.getDocument().getId(),
					ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE, numeroInterne };

			final List<String> otherTexteMaitreIds = QueryUtils.doUFNXQLQueryForIdsList(session,
					queryBuilder.toString(), params, 1, 0);
			if (!otherTexteMaitreIds.isEmpty()) {
				throw new ActiviteNormativeException("Le numéro interne " + numeroInterne
						+ " existe deja pour un autre texte maitre");
			}
		}
	}

	@Override
	public List<Habilitation> fetchListHabilitation(final List<String> idsHabilitation, final CoreSession session)
			throws ClientException {

		final List<Habilitation> list = new ArrayList<Habilitation>();
		for (final String id : idsHabilitation) {
			try {
				final DocumentModel doc = session.getDocument(new IdRef(id));
				list.add(doc.getAdapter(Habilitation.class));
			} catch (final Exception e) {
				// probleme de droit / mesure supprimée /etc...
				LOGGER.warn(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC,
						"Impossible de recuperer l'habilitation" + id, e);
			}
		}

		return list;

	}

	@Override
	public List<Ordonnance> fetchListOrdonnance(final List<String> ordonnanceIds, final CoreSession session) {
		final List<Ordonnance> list = new ArrayList<Ordonnance>();
		for (final String id : ordonnanceIds) {
			try {
				final DocumentModel doc = session.getDocument(new IdRef(id));
				list.add(doc.getAdapter(Ordonnance.class));
			} catch (final Exception e) {
				// probleme de droit / mesure supprimée /etc...
				LOGGER.warn(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, "Impossible de recuperer l'ordonnance" + id,
						e);
			}
		}

		return list;
	}

	@Override
	public Ordonnance checkIsOrdonnance38CFromNumeroNOR(final String numeroNorDossier, final CoreSession session)
			throws ClientException {
		final Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(session, numeroNorDossier);
		if (dossier == null) {
			throw new ActiviteNormativeException("activite.normative.dossier.not.found");
		}

		if (!TypeActeConstants.TYPE_ACTE_ORDONNANCE.equals(dossier.getTypeActe())) {
			throw new ActiviteNormativeException("activite.normative.not.ordonnance");
		}

		final ActiviteNormative activiteNormative = findOrCreateActiviteNormativeByNor(session, dossier.getNumeroNor());
		return activiteNormative.getAdapter(Ordonnance.class);
	}

	@Override
	public Ordonnance saveOrdonnanceHabilitationReprise(final OrdonnanceHabilitationDTO ordonnanceHabilitationDTO,
			final String article, final String idActiviteNormative, final CoreSession session, final boolean ratifie)
			throws ClientException {

		Ordonnance ordonnance = null;

		final Habilitation habilitation = findMesureHabilitationForLoi(session, null, idActiviteNormative);

		if (habilitation == null) {
			throw new ClientException("Impossible de retrouver l'habilitation d'article : " + article);
		} else {
			final DocumentModel doc = session.getDocument(new IdRef(idActiviteNormative));
			final TexteMaitre texteMaitre = doc.getAdapter(TexteMaitre.class);

			final List<String> habilitationIds = texteMaitre.getHabilitationIds();
			if (!habilitationIds.contains(habilitation.getId())) {
				habilitationIds.add(habilitation.getId());
				texteMaitre.setHabilitationIds(habilitationIds);
				texteMaitre.save(session);
			}

			final String numeroNor = ordonnanceHabilitationDTO.getNumeroNor();
			if (StringUtil.isNotBlank(numeroNor)) {
				final ActiviteNormative activiteNormativeOrd = findOrCreateActiviteNormativeByNor(session, numeroNor);
				activiteNormativeOrd.setOrdonnance(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE);
				activiteNormativeOrd.save(session);
				ordonnance = activiteNormativeOrd.getAdapter(Ordonnance.class);
				ordonnance = ordonnanceHabilitationDTO.remapField(ordonnance);
				ordonnance.save(session);

				final TexteMaitre texteMaitreOrd = activiteNormativeOrd.getAdapter(TexteMaitre.class);
				texteMaitreOrd.setRatifie(ratifie);
				texteMaitreOrd.setDispositionHabilitation(true);
				session.saveDocument(texteMaitreOrd.getDocument());

				// habilitation.setArticle(article);
				final List<String> listIdOrdonnances = habilitation.getOrdonnanceIds();
				if (listIdOrdonnances != null && !listIdOrdonnances.contains(ordonnance.getId())) {
					listIdOrdonnances.add(ordonnance.getId());
					habilitation.setOrdonnanceIds(listIdOrdonnances);
					habilitation.save(session);
				}

			}
			session.save();
			return ordonnance;
		}

	}

	@Override
	public TexteMaitre saveHabilitation(final List<HabilitationDTO> listHabilitation, final String idActiviteNormative,
			final CoreSession session) throws ClientException {

		final DocumentModel doc = session.getDocument(new IdRef(idActiviteNormative));
		final TexteMaitre texteMaitre = doc.getAdapter(TexteMaitre.class);

		final List<Habilitation> listOldHabilitation = fetchListHabilitation(texteMaitre.getHabilitationIds(), session);
		final Map<String, Habilitation> mapExistant = new HashMap<String, Habilitation>();

		for (final Habilitation habilitation : listOldHabilitation) {
			mapExistant.put(habilitation.getId(), habilitation);
		}

		final Set<String> newHabilitationIds = new HashSet<String>();

		for (final HabilitationDTO habilitationDTO : listHabilitation) {
			final String habilitationId = habilitationDTO.getId();
			Habilitation habilitation = null;
			if (mapExistant.get(habilitationId) == null) {
				// nouvelle habilitation
				habilitation = createHabilitationForArticle(session, idActiviteNormative, habilitationDTO.getArticle());
				habilitation.getDocument().getAdapter(ActiviteNormative.class)
						.setOrdonnance38C(ActiviteNormativeConstants.MESURE_FILTER);
			} else {
				// habilitation existante
				habilitation = mapExistant.get(habilitationId);
			}

			if (habilitationDTO.isValidate()) {
				habilitation = habilitationDTO.remapField(habilitation);
			}
			session.saveDocument(habilitation.getDocument());
			newHabilitationIds.add(habilitation.getId());

			// Ajout dans le journal du PAN
			DocumentModel dossierDoc = SolonEpgServiceLocator.getNORService().getDossierFromNOR(session,
					texteMaitre.getNumeroNor());
			if (habilitationDTO.isValidate()) {
				STServiceLocator.getJournalService().journaliserActionPAN(session, dossierDoc.getRef().toString(),
						SolonEpgEventConstant.MODIF_HABIL_EVENT,
						SolonEpgEventConstant.MODIF_HABIL_COMMENT + " [" + habilitationDTO.getNumeroOrdre() + "]",
						SolonEpgEventConstant.CATEGORY_LOG_PAN_SUIVI_HABILITATIONS);
			}
		}

		texteMaitre.setHabilitationIds(new ArrayList<String>(newHabilitationIds));
		texteMaitre.save(session);
		session.save();

		return doc.getAdapter(TexteMaitre.class);
	}

	/**
	 * Créé une nouvelle habilitation pour un article donné et la rattache à un texte maitre passé en paramètre
	 * 
	 * @param session
	 * @param idDosActNormative
	 *            id du texte maitre auquel sera rattachée l'habilitation
	 * @return le nouvel objet Habilitation créé
	 * @throws ClientException
	 */
	protected Habilitation createHabilitationForArticle(final CoreSession session, final String idDosActNormative,
			final String article) throws ClientException {
		final Habilitation bareHabilitation = createBareHabilitation(session, idDosActNormative);
		if (StringUtil.isNotBlank(article)) {
			bareHabilitation.setArticle(article);
		}
		bareHabilitation.setValidation(Boolean.TRUE);
		final DocumentModel modelDesired = session.createDocument(bareHabilitation.getDocument());
		return modelDesired.getAdapter(Habilitation.class);
	}

	/**
	 * créé un nouveau document pour une habilitation non encore persistée nécessite un id de dossier auquel
	 * l'habilitation sera rattachée
	 * 
	 * @param session
	 * @param idDosActNormative
	 * @return Habilitation l'habilitation non encore persistée
	 * @throws ClientException
	 */
	protected Habilitation createBareHabilitation(final CoreSession session, final String idDosActNormative)
			throws ClientException {
		Habilitation habilitation = null;
		final DocumentModel modelDesired = createBareActiviteNormative(session);
		habilitation = modelDesired.getAdapter(Habilitation.class);
		if (StringUtil.isNotBlank(idDosActNormative)) {
			habilitation.setIdDossier(idDosActNormative);
			return habilitation;
		} else {
			throw new ActiviteNormativeException("Id dossier maitre obligatoire en création d'habilitation");
		}
	}

	@Override
	public List<String> saveOrdonnanceHabilitation(final List<OrdonnanceHabilitationDTO> listOrdonnanceHabilitation,
			final String idHabilitation, final String idtexteMaitre, final CoreSession session) throws ClientException {

		final List<String> listIdOrdonnances = new ArrayList<String>();
		Set<String> errors = new HashSet<String>();

		for (final OrdonnanceHabilitationDTO ordonnanceHabilitationDTO : listOrdonnanceHabilitation) {
			final String numeroNor = ordonnanceHabilitationDTO.getNumeroNor();
			if (StringUtil.isNotBlank(numeroNor)) {
				boolean norOk = true;

				try {
					checkIsOrdonnance38CFromNumeroNOR(numeroNor, session);
				} catch (ActiviteNormativeException e) {
					errors.add(numeroNor);
					norOk = false;
				}

				if (norOk) {
					final ActiviteNormative activiteNormative = findOrCreateActiviteNormativeByNor(session, numeroNor);
					Ordonnance ordonnance = activiteNormative.getAdapter(Ordonnance.class);
					if (ordonnanceHabilitationDTO.isValidate()) {
						ordonnance = ordonnanceHabilitationDTO.remapField(ordonnance);
					}
					ordonnanceHabilitationDTO.setValidationLink(Boolean.TRUE);
					session.saveDocument(ordonnance.getDocument());

					final TexteMaitre txm = activiteNormative.getAdapter(TexteMaitre.class);
					final List<String> loiRatList = txm.getLoiRatificationIds();
					if (loiRatList != null) {
						final List<LoiDeRatification> listLoiRatification = fetchLoiDeRatification(loiRatList, session);
						for (final LoiDeRatification loiDeRatification : listLoiRatification) {
							loiDeRatification.setDateLimiteDepot(ordonnance.getDateLimiteDepot());
							session.saveDocument(loiDeRatification.getDocument());
						}
					}
					listIdOrdonnances.add(ordonnance.getDocument().getId());
				}
			}
		}

		if (errors.isEmpty()) {
			final DocumentModel activiteNormativeDoc = session.getDocument(new IdRef(idHabilitation));
			final Habilitation habilitation = activiteNormativeDoc.getAdapter(Habilitation.class);
			final DocumentModel texteMaitreDoc = session.getDocument(new IdRef(idtexteMaitre));

			if (habilitation == null) {
				throw new ActiviteNormativeException("Impossible de retrouver l'habilitation d'id : " + idHabilitation);
			} else {
				habilitation.setOrdonnanceIds(listIdOrdonnances);
				habilitation.setOrdonnanceIdsInvalidated(null);
				session.saveDocument(habilitation.getDocument());
			}
			session.save();

			// Ajout dans le journal du PAN
			DocumentModel dossierDoc = SolonEpgServiceLocator.getNORService().getDossierFromNOR(session,
					texteMaitreDoc.getAdapter(TexteMaitre.class).getNumeroNor());
			for (OrdonnanceHabilitationDTO ordonnanceHabilitationDTO : listOrdonnanceHabilitation) {
				if (ordonnanceHabilitationDTO.isValidate())
					STServiceLocator.getJournalService().journaliserActionPAN(session, dossierDoc.getRef().toString(),
							SolonEpgEventConstant.MODIF_ORDO_EVENT,
							SolonEpgEventConstant.MODIF_ORDO_COMMENT + " [" + ordonnanceHabilitationDTO.getNumeroNor()
									+ "] de l'habilitation [" + habilitation.getNumeroOrdre() + "]",
							SolonEpgEventConstant.CATEGORY_LOG_PAN_SUIVI_HABILITATIONS);
			}
		} else {
			throw new ActiviteNormativeException(errors);
		}
		return listIdOrdonnances;
	}

	@Override
	public void saveCurrentProgrammationHabilitation(final List<LigneProgrammationHabilitation> lignesProgrammations,
			final ActiviteNormative activiteNormative, final CoreSession session) throws ClientException {

		final ActiviteNormativeProgrammation activiteNormativeProgrammation = activiteNormative
				.getAdapter(ActiviteNormativeProgrammation.class);

		activiteNormativeProgrammation.setLigneProgrammationHabilitation(lignesProgrammations);
		activiteNormativeProgrammation.save(session);
		session.save();
	}

	@Override
	public void lockCurrentProgrammationHabilitation(final ActiviteNormative activiteNormative,
			final CoreSession session) throws ClientException {

		final String userFullName = STServiceLocator.getSTUserService().getUserFullName(
				session.getPrincipal().getName());

		final ActiviteNormativeProgrammation activiteNormativeProgrammation = activiteNormative
				.getAdapter(ActiviteNormativeProgrammation.class);

		activiteNormativeProgrammation.setLockHabDate(Calendar.getInstance());
		activiteNormativeProgrammation.setLockHabUser(userFullName);
		activiteNormativeProgrammation.save(session);
		session.save();
	}

	@Override
	public void removeCurrentProgrammationHabilitation(final ActiviteNormative activiteNormative,
			final CoreSession session) throws ClientException {
		final ActiviteNormativeProgrammation activiteNormativeProgrammation = activiteNormative
				.getAdapter(ActiviteNormativeProgrammation.class);

		activiteNormativeProgrammation
				.setLigneProgrammationHabilitation(new ArrayList<LigneProgrammationHabilitation>());
		activiteNormativeProgrammation.save(session);
		session.save();
	}

	@Override
	public void unlockCurrentProgrammationHabilitation(final ActiviteNormative activiteNormative,
			final CoreSession session) throws ClientException {
		final ActiviteNormativeProgrammation activiteNormativeProgrammation = activiteNormative
				.getAdapter(ActiviteNormativeProgrammation.class);
		activiteNormativeProgrammation.setLockHabDate(null);
		activiteNormativeProgrammation.setLockHabUser(null);
		activiteNormativeProgrammation.save(session);
		session.save();
	}

	@Override
	public void publierTableauSuiviHab(final List<LigneProgrammationHabilitation> lignesProgrammations,
			final ActiviteNormative activiteNormative, final CoreSession session) throws ClientException {
		final String userFullName = STServiceLocator.getSTUserService().getUserFullName(
				session.getPrincipal().getName());

		final ActiviteNormativeProgrammation activiteNormativeProgrammation = activiteNormative
				.getAdapter(ActiviteNormativeProgrammation.class);

		activiteNormativeProgrammation.setTableauSuiviHab(lignesProgrammations);
		activiteNormativeProgrammation.setTableauSuiviHabPublicationDate(Calendar.getInstance());
		activiteNormativeProgrammation.setTableauSuiviHabPublicationUser(userFullName);
		activiteNormativeProgrammation.save(session);
		session.save();

		// Ajout dans le journal du PAN
		TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);
		DocumentModel dossierDoc = SolonEpgServiceLocator.getNORService().getDossierFromNOR(session,
				texteMaitre.getNumeroNor());
		STServiceLocator.getJournalService().journaliserActionPAN(session, dossierDoc.getRef().toString(),
				SolonEpgEventConstant.PUBLI_STAT_EVENT,
				SolonEpgEventConstant.PUBLI_STAT_COMMENT + " [Tableau de suivi]",
				SolonEpgEventConstant.CATEGORY_LOG_PAN_GENERAL);
	}

	@Override
	public Boolean isTexteMaitreLockByCurrentUser(final TexteMaitre texteMaitre, final CoreSession session) {
		if (texteMaitre != null) {
			return session.getPrincipal().getName().equals(texteMaitre.getDocLockUser());
		}
		return Boolean.FALSE;
	}

	@Override
	public TexteMaitre lockTexteMaitre(final TexteMaitre texteMaitre, final CoreSession session) throws ClientException {
		if (texteMaitre != null) {
			texteMaitre.setDocLockDate(Calendar.getInstance());
			texteMaitre.setDocLockUser(session.getPrincipal().getName());
			final DocumentModel doc = session.saveDocument(texteMaitre.getDocument());
			return doc.getAdapter(TexteMaitre.class);
		}
		return null;

	}

	@Override
	public TexteMaitre unlockTexteMaitre(final TexteMaitre texteMaitre, final CoreSession session)
			throws ClientException {
		if (texteMaitre != null) {
			texteMaitre.setDocLockDate(null);
			texteMaitre.setDocLockUser(null);
			final DocumentModel doc = session.saveDocument(texteMaitre.getDocument());
			return doc.getAdapter(TexteMaitre.class);
		}
		return null;

	}

	@Override
	public TexteMaitre addLoiToTableauMaitreReprise(final TexteMaitreLoiDTO texteMaitreLoiDTO, final CoreSession session)
			throws ClientException {
		final ActiviteNormative activiteNormative = findOrCreateActiviteNormativeByNumero(session,
				texteMaitreLoiDTO.getNumero());
		activiteNormative.setApplicationLoi(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE);
		activiteNormative.save(session);
		final TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);

		texteMaitre.setNumeroNor(texteMaitreLoiDTO.getNumeroNor());
		texteMaitre.setTitreOfficiel(texteMaitreLoiDTO.getTitreOfficiel());
		texteMaitre.setMinisterePilote(texteMaitreLoiDTO.getMinisterePilote());
		texteMaitre.setMotCle(texteMaitreLoiDTO.getMotCle());
		if (texteMaitreLoiDTO.getDatePublication() != null) {
			final Calendar datePublication = Calendar.getInstance();
			datePublication.setTime(texteMaitreLoiDTO.getDatePublication());
			texteMaitre.setDatePublication(datePublication);
		}
		if (texteMaitreLoiDTO.getDatePromulgation() == null) {
			if (texteMaitreLoiDTO.getTitreActe() != null) {
				texteMaitre.setDatePromulgation(extractDateFromTitre(texteMaitreLoiDTO.getTitreActe()));
			}
		} else {
			final Calendar datePromulgation = Calendar.getInstance();
			datePromulgation.setTime(texteMaitreLoiDTO.getDatePromulgation());
			texteMaitre.setDatePromulgation(datePromulgation);
		}
		texteMaitre.setNatureTexte(texteMaitreLoiDTO.getNatureTexte());
		texteMaitre.setProcedureVote(texteMaitreLoiDTO.getProcedureVote());
		texteMaitre.setCommissionSenat(texteMaitreLoiDTO.getCommissionSenat());
		texteMaitre.setNumero(texteMaitreLoiDTO.getNumero());
		texteMaitre.save(session);
		return texteMaitre;

	}

	@Override
	public MesureApplicative saveMesureReprise(final String idActiviteNormative,
			final MesureApplicativeDTO mesureApplicativeDTO, final CoreSession session) throws ClientException {

		final Set<String> error = new HashSet<String>();
		if (StringUtil.isEmpty(mesureApplicativeDTO.getNumeroOrdre())) {
			error.add("Le numéro d'ordre est obligatoire");
		}

		MesureApplicative mesureApplicative = findMesureApplicativeForLoi(session,
				mesureApplicativeDTO.getNumeroOrdre(), idActiviteNormative);

		if (mesureApplicative != null) {
			mesureApplicative = mesureApplicativeDTO.remapField(mesureApplicative);
			mesureApplicative.setValidation(Boolean.TRUE);
			mesureApplicative.setIdDossier(idActiviteNormative);
			mesureApplicative.save(session);

			final TexteMaitre texteMaitre = session.getDocument(new IdRef(idActiviteNormative)).getAdapter(
					TexteMaitre.class);
			final Set<String> idsMesure = new HashSet<String>(texteMaitre.getMesuresIds());
			idsMesure.add(mesureApplicative.getId());
			texteMaitre.setMesuresIds(new ArrayList<String>(idsMesure));
			texteMaitre.save(session);
			session.save();
			return mesureApplicative;
		} else {
			throw new ActiviteNormativeException("Pour la loi d'id : " + idActiviteNormative + " et le numero d'ordre "
					+ mesureApplicativeDTO.getNumeroOrdre() + ", "
					+ "il existe 0, ou plusieurs mesures correspondantes.");
		}
	}

	@Override
	public void addDecretToMesureReprise(final String texteMaitreLoiId, final String mesureId,
			final DecretDTO decretDTO, final CoreSession session) throws ClientException {
		if (!StringUtil.isEmpty(decretDTO.getNumeroNor())) {
			final ActiviteNormative activiteNormative = findOrCreateActiviteNormativeByNor(session,
					decretDTO.getNumeroNor());
			Decret decret = activiteNormative.getAdapter(Decret.class);
			decret = decretDTO.remapFieldReprise(decret);
			final Set<String> setMesures = new HashSet<String>(decret.getMesureIds());
			setMesures.add(mesureId);
			decret.setMesureIds(new ArrayList<String>(setMesures));
			decret.setIdDossier(activiteNormative.getDocument().getId());
			if (decretDTO.getValidate() || decretDTO.getId() == null) {
				decret.setValidation(Boolean.TRUE);
			}
			decret.save(session);
			final MesureApplicative mesureApplicative = session.getDocument(new IdRef(mesureId)).getAdapter(
					MesureApplicative.class);
			final Set<String> decretIds = new HashSet<String>(mesureApplicative.getDecretIds());
			decretIds.add(decret.getId());
			mesureApplicative.setDecretIds(new ArrayList<String>(decretIds));
			mesureApplicative.save(session);
		}
	}

	/**
	 * get le path du répertoire qui va contenir le stat publie de l'activite normative
	 * 
	 * @return
	 */
	@Override
	public String getPathDirANStatistiquesPublie() {
		final String generatedReportPath = STServiceLocator.getConfigService().getValue(
				SolonEpgConfigConstant.SOLONEPG_AN_STATS_PUBLIE_DIRECTORY)
				+ "/legislature-courante";

		final File generatedReportDir = new File(generatedReportPath);
		if (generatedReportDir.exists() == false) {
			generatedReportDir.mkdirs();
		}
		return generatedReportPath;
	}

	@Override
	public String getPathDirANStatistiquesLegisPrecPublie(CoreSession session) {
		String generatedReportPath = null;
		SolonEpgParametreService paramService = SolonEpgServiceLocator.getSolonEpgParametreService();
		try {
			ParametrageAN param = paramService.getDocAnParametre(session);
			if (param != null) {
				generatedReportPath = STServiceLocator.getConfigService().getValue(
						SolonEpgConfigConstant.SOLONEPG_AN_STATS_PUBLIE_DIRECTORY)
						+ "/" + DateUtil.formatDDMMYYYY(param.getLegislaturePrecedenteDateDebut());

				final File generatedReportDir = new File(generatedReportPath);
				if (generatedReportDir.exists() == false) {
					generatedReportDir.mkdirs();
				}
			}
		} catch (ClientException e) {
			LOGGER.debug(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, "Echec de récupération paramètrage", e);
			LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC,
					"Echec de récupération paramètrage pour la création du répertoire");
		}
		return generatedReportPath;
	}

	@Override
	public void generateANRepartitionMinistereHtml(final CoreSession session,
			final ActiviteNormative activiteNormative, boolean legislatureEnCours) {
		// Save to html file
		String generatedReportPath;
		String numeroNor = "";
		FileOutputStream outputStream = null;
		try {
			final TexteMaitre textMaitre = activiteNormative.getDocument().getAdapter(TexteMaitre.class);
			if (textMaitre != null) {
				numeroNor = textMaitre.getNumeroNor();
				if (legislatureEnCours) {
					generatedReportPath = getPathDirANStatistiquesPublie();
				} else {
					generatedReportPath = getPathDirANStatistiquesLegisPrecPublie(session);
				}

				final File htmlReport = new File(generatedReportPath + System.getProperty("file.separator")
						+ "repartitionParMinistere-" + numeroNor + ".html");
				if (!htmlReport.exists()) {
					htmlReport.createNewFile();
				}
				final Map<String, String> repartitionMinistere = buildMinistereForFicheSignaletique(session,
						activiteNormative);
				outputStream = new FileOutputStream(htmlReport);
				outputStream
						.write("<style>.repMinTbl{font: normal 12px \"Lucida Grande\", sans-serif;border-collapse: collapse;}\n.repMinTbl td,.repMinTbl th{border:thin solid black}</style>"
								.getBytes());
				outputStream.write("<table class='repMinTbl' cellpadding='2' cellspacing='0'>".getBytes());
				for (final Map.Entry<String, String> entry : repartitionMinistere.entrySet()) {
					outputStream.write(("<tr><td>" + entry.getKey() + "</td><td>" + entry.getValue() + "</td></tr>")
							.getBytes());
				}
				outputStream.write("</table>".getBytes());
				outputStream.close();
			}
		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_GENERATE_HTML_TEC, "repartition ministeriel mal genere", e);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}

	@Override
	public void updateLoiListePubliee(final CoreSession session, boolean curLegis) {
		String generatedReportPath = "";
		FileOutputStream outputStream1 = null;
		FileOutputStream outputStream2 = null;
		try {
			final List<DocumentModel> documentModelList = getAllAplicationLoiDossiers(session, curLegis);

			if (curLegis) {
				generatedReportPath = getPathDirANStatistiquesPublie();
			} else {
				generatedReportPath = getPathDirANStatistiquesLegisPrecPublie(session);
			}

			File htmlReport = new File(generatedReportPath + System.getProperty("file.separator") + "listeDesLois.html");
			if (!htmlReport.exists()) {
				htmlReport.createNewFile();
			}
			outputStream1 = new FileOutputStream(htmlReport);
			outputStream1.write("<table cellpadding='0' cellspacing='0'>".getBytes());
			TexteMaitre texteMaitre;
			String urlJOFRLegiFrance, numeroNor, titreActe;

			// trier les lois par ordre decroissant
			final Comparator<DocumentModel> tmComparator = new Comparator<DocumentModel>() {
				@Override
				public int compare(final DocumentModel t1, final DocumentModel t2) {
					final TexteMaitre tm1 = t1.getAdapter(TexteMaitre.class);
					final TexteMaitre tm2 = t2.getAdapter(TexteMaitre.class);
					final String numero1 = tm1 == null || tm1.getNumero() == null ? "" : tm1.getNumero().trim();
					final String numero2 = tm2 == null || tm2.getNumero() == null ? "" : tm2.getNumero().trim();

					try {
						if (numero1.indexOf("-") == -1 || numero2.indexOf("-") == -1) {
							return Integer.parseInt(numero2.replace("-", ""))
									- Integer.parseInt(numero1.replaceAll("-", ""));
						} else {
							final String year1 = numero1.substring(0, numero1.indexOf("-")).trim();
							final String year2 = numero2.substring(0, numero2.indexOf("-")).trim();
							final String num1 = numero1.substring(numero1.indexOf("-") + 1).trim();
							final String num2 = numero2.substring(numero2.indexOf("-") + 1).trim();

							if (year1.equals(year2)) {
								return Integer.parseInt(num2) - Integer.parseInt(num1);
							} else if (Integer.parseInt(year2) > Integer.parseInt(year1)) {
								return 1;
							} else {
								return -1;
							}
						}
					} catch (final Exception e) {
						return numero2.compareTo(numero1);
					}
				}
			};
			Collections.sort(documentModelList, tmComparator);

			for (final DocumentModel documentModel : documentModelList) {
				texteMaitre = documentModel.getAdapter(TexteMaitre.class);
				if (texteMaitre.isApplicationDirecte()) {
					continue;
				}
				numeroNor = texteMaitre.getNumeroNor();
				titreActe = texteMaitre.getTitreOfficiel();
				if (titreActe == null) {
					titreActe = numeroNor;
				}
				urlJOFRLegiFrance = createLienJORFLegifrance(numeroNor);
				outputStream1
						.write(("<tr><td><span onclick=\"eltDeSuivi('" + numeroNor + "', this)\" class='titreSpan' >"
								+ titreActe + "</span>&nbsp;" + "<a href='" + urlJOFRLegiFrance + "' target='_blank' ><img src='view.png' style='border:none;cursor:pointer' alt='more' /></a></td></tr>")
								.getBytes());
			}
			outputStream1.write("</table>".getBytes());

			// Par Ministere

			final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
			htmlReport = new File(generatedReportPath + System.getProperty("file.separator")
					+ "listeDesMinistereLoi.html");
			if (!htmlReport.exists()) {
				htmlReport.createNewFile();
			}
			outputStream2 = new FileOutputStream(htmlReport);
			outputStream2.write("<table cellpadding='5' cellspacing='0'>".getBytes());
			outputStream2
					.write("<tr><th>Ministère</th><th>Taux d’application par ministère</th><th>Mesures en attente de décrets</th></tr>"
							.getBytes());

			// order ministeres par ordre protocolaire
			final List<EntiteNode> ministeresListNodes = ministeresService.getCurrentMinisteres();

			final TreeMap<Long, OrganigrammeNode> tmMinisteres = new TreeMap<Long, OrganigrammeNode>();
			for (final EntiteNode entity : ministeresListNodes) {

				if (entity != null && entity.getOrdre() != null && entity.getId() != null
						&& entity.isSuiviActiviteNormative()) {
					tmMinisteres.put(entity.getOrdre(), entity);
				}
			}
			for (final OrganigrammeNode ministereNode : tmMinisteres.values()) {

				// node = ministeresService.getEntiteNode(ministere);
				String realLabel = ministereNode.getLabel();
				outputStream2.write(("<tr><td>" + realLabel + "</td>" + "<td><span onclick=\"eltDeSuiviParMin('"
						+ ministereNode.getId() + "',this)\"><img src='percent.png' alt=\"Détail mesures pour '"
						+ realLabel + "'\"></span></td>" + "<td><span onclick=\"parMinMesure('" + ministereNode.getId()
						+ "',this)\"><img src='index_new.png' alt=\"Détail mesures pour '" + realLabel
						+ "'\"></span></td>" + "</tr>").getBytes());
			}
			outputStream2.write("</table>".getBytes());

		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_GENERATE_HTML_TEC,
					"Erreur lors de l'update de la liste des ministeres pour la page d'accueil -> generatedReportPath ="
							+ generatedReportPath, e);
		} finally {
			IOUtils.closeQuietly(outputStream1);
			IOUtils.closeQuietly(outputStream2);
		}
	}

	@Override
	public void updateHabilitationListePubliee(final CoreSession session, boolean curLegis) {
		String generatedReportPath = "";
		FileOutputStream outputStream1 = null;
		FileOutputStream outputStream2 = null;
		try {
			final List<DocumentModel> documentModelList = getAllLoiHabilitationDossiers(session, curLegis);

			if (curLegis) {
				generatedReportPath = getPathDirANStatistiquesPublie();
			} else {
				generatedReportPath = getPathDirANStatistiquesLegisPrecPublie(session);
			}

			File htmlReport = new File(generatedReportPath + System.getProperty("file.separator")
					+ "listeDesLoisHabilitation.html");
			if (!htmlReport.exists()) {
				htmlReport.createNewFile();
			}
			outputStream1 = new FileOutputStream(htmlReport);
			outputStream1.write("<table cellpadding='0' cellspacing='0'>".getBytes());
			TexteMaitre texteMaitre;
			String urlJOFRLegiFrance, numeroNor, titreActe;

			// trier les lois par ordre decroissant
			final Comparator<DocumentModel> tmComparator = new Comparator<DocumentModel>() {
				@Override
				public int compare(final DocumentModel t1, final DocumentModel t2) {
					final TexteMaitre tm1 = t1.getAdapter(TexteMaitre.class);
					final TexteMaitre tm2 = t2.getAdapter(TexteMaitre.class);

					final String numero1 = tm1 != null && tm1.getNumero() != null ? tm1.getNumero().trim() : "";
					final String numero2 = tm2 != null && tm2.getNumero() != null ? tm2.getNumero().trim() : "";

					try {
						if (numero1.indexOf("-") != -1 && numero2.indexOf("-") != -1) {
							final String year1 = numero1.substring(0, numero1.indexOf("-")).trim();
							final String year2 = numero2.substring(0, numero2.indexOf("-")).trim();
							final String num1 = numero1.substring(numero1.indexOf("-") + 1).trim();
							final String num2 = numero2.substring(numero2.indexOf("-") + 1).trim();

							if (year1.equals(year2)) {
								return Integer.parseInt(num2) - Integer.parseInt(num1);
							} else if (Integer.parseInt(year2) > Integer.parseInt(year1)) {
								return 1;
							} else {
								return -1;
							}
						} else {
							return Integer.parseInt(numero2.replace("-", ""))
									- Integer.parseInt(numero1.replaceAll("-", ""));
						}
					} catch (final Exception e) {
						return numero2.compareTo(numero1);
					}
				}
			};
			Collections.sort(documentModelList, tmComparator);

			for (final DocumentModel documentModel : documentModelList) {
				texteMaitre = documentModel.getAdapter(TexteMaitre.class);
				numeroNor = texteMaitre.getNumeroNor();
				titreActe = texteMaitre.getTitreOfficiel();
				if (titreActe == null) {
					titreActe = numeroNor;
				}
				urlJOFRLegiFrance = createLienJORFLegifrance(numeroNor);
				outputStream1
						.write(("<tr><td><span onclick=\"eltDeSuivi('" + numeroNor + "', this)\" class='titreSpan'>"
								+ titreActe + "</span>&nbsp;" + "<a href='" + urlJOFRLegiFrance + "' target='_blank'><img src='view.png' style='border:none;cursor:pointer' alt='more'/></a></td></tr>")
								.getBytes());
			}
			outputStream1.write("</table>".getBytes());

			// Par Ministere
			List<String> ministereList = getAllHabilitationMinisteresList(session, curLegis);
			final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
			OrganigrammeNode node;
			htmlReport = new File(generatedReportPath + System.getProperty("file.separator")
					+ "listeDesMinistereHabilitation.html");
			if (!htmlReport.exists()) {
				htmlReport.createNewFile();
			}
			outputStream2 = new FileOutputStream(htmlReport);
			outputStream2.write("<table cellpadding='5' cellspacing='0'>".getBytes());
			outputStream2
					.write("<tr><th>Ratification article 74-1</th><th>Ratification article 38</th><th>Habilitations par ministères</th></tr>"
							.getBytes());

			// order ministeres par ordre protocolaire
			final TreeMap<Long, String> tmMinisteres = new TreeMap<Long, String>();
			final List<EntiteNode> ministeresListNodes = ministeresService.getEntiteNodes(ministereList);
			for (final EntiteNode entity : ministeresListNodes) {

				if (entity != null && entity.getOrdre() != null && entity.getId() != null) {
					tmMinisteres.put(entity.getOrdre(), entity.getId());
				}
			}
			ministereList = new ArrayList<String>(tmMinisteres.values());

			for (final String ministere : ministereList) {
				node = ministeresService.getEntiteNode(ministere);
				outputStream2.write(("<tr><td><span onclick=\"parMinRatification741('" + ministere
						+ "',this)\">...</span></td><td><span onclick=\"parMinRatification38c('" + ministere
						+ "',this)\">...</span></td><td><span onclick=\"eltDeSuiviParMin('" + ministere + "',this)\">"
						+ (node instanceof EntiteNode ? node.getLabel() : ministere) + "</span></td></tr>").getBytes());
			}
			outputStream2.write("</table>".getBytes());

		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_GENERATE_HTML_TEC,
					"Erreur lors de l'update de la liste des Lois d'habilitation pour la page d'accueil -> generatedReportPath ="
							+ generatedReportPath, e);
		} finally {
			IOUtils.closeQuietly(outputStream1);
			IOUtils.closeQuietly(outputStream2);
		}
	}

	@Override
	public List<DocumentModel> findTexteMaitreByMinisterePilote(final String idMinistere, final CoreSession session)
			throws ClientException {
		final StringBuilder queryBuilder = new StringBuilder("SELECT a.ecm:uuid as id FROM ");
		queryBuilder.append(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE);
		queryBuilder.append(" as a WHERE a.");
		queryBuilder.append(TexteMaitreConstants.TEXTE_MAITRE_PREFIX);
		queryBuilder.append(":");
		queryBuilder.append(TexteMaitreConstants.MINISTERE_PILOTE);
		queryBuilder.append(" = ? ");

		return QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, queryBuilder.toString(),
				new Object[] { idMinistere }, 0, 0, PREFETCH_INFO_TEXTE_MAITRE);
	}

	/**
	 * create Ordonnance pour la Reprise
	 * 
	 * @param ordonnanceDTO
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	@Override
	public Ordonnance createOrdonnanceReprise(final OrdonnanceDTO ordonnanceDTO, final CoreSession session)
			throws ClientException {

		final ActiviteNormative activiteNormative = findOrCreateActiviteNormativeByNor(session,
				ordonnanceDTO.getNumeroNor());
		activiteNormative.setOrdonnance(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE);
		session.saveDocument(activiteNormative.getDocument());

		final TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);
		texteMaitre.setObservation(ordonnanceDTO.getObservation());
		texteMaitre.setDispositionHabilitation(ordonnanceDTO.isDispositionHabilitation());
		texteMaitre.setRatifie(ordonnanceDTO.getRatifie());
		session.saveDocument(texteMaitre.getDocument());

		final Ordonnance ordonnance = activiteNormative.getAdapter(Ordonnance.class);

		ordonnance.setMinisterePilote(ordonnanceDTO.getMinisterePilote());
		ordonnance.setNumero(ordonnanceDTO.getNumero());
		ordonnance.setTitreOfficiel(ordonnanceDTO.getTitreOfficiel());

		if (ordonnanceDTO.getDatePublication() != null) {
			final Calendar datePublication = Calendar.getInstance();
			datePublication.setTime(ordonnanceDTO.getDatePublication());
			ordonnance.setDatePublication(datePublication);
		}
		session.saveDocument(ordonnance.getDocument());
		session.save();
		return ordonnance;
	}

	@Override
	public void updateDecretFromFeuilleDeRoute(final DocumentModel dossierDoc, final CoreSession session,
			final boolean fluxRetour) throws ClientException {
		final ActiviteNormative activiteNormative = findActiviteNormativeByNor(dossierDoc.getAdapter(Dossier.class)
				.getNumeroNor(), session);
		if (activiteNormative != null) {
			final Decret decret = activiteNormative.getDocument().getAdapter(Decret.class);
			final ConseilEtat conseilEtat = dossierDoc.getAdapter(ConseilEtat.class);
			if (fluxRetour) {
				if (!decret.isDateSectionCeLocked()) {
					decret.setDateSectionCe(conseilEtat.getDateSectionCe());
				}
				if (!decret.isRapporteurCeLocked()) {
					decret.setRapporteurCe(conseilEtat.getRapporteurCe());
				}
				if (decret.getDateSectionCe() == null) {
					decret.setDateSectionCe(Calendar.getInstance());
				}
			} else {
				if (!decret.isDateSaisineCELocked()) {
					decret.setDateSaisineCE(conseilEtat.getDateSaisineCE());
				}
				if (!decret.isSectionCeLocked()) {
					decret.setSectionCe(conseilEtat.getSectionCe());
				}
				if (decret.getDateSectionCe() == null) {
					decret.setDateSectionCe(Calendar.getInstance());
				}
			}
			decret.save(session);
		}
	}

	@Override
	public void updateLoiFromFeuilleDeRoute(final DocumentModel dossierDoc, final CoreSession session,
			final boolean fluxRetour) throws ClientException {
		final ActiviteNormative activiteNormative = findActiviteNormativeByNor(dossierDoc.getAdapter(Dossier.class)
				.getNumeroNor(), session);
		if (activiteNormative != null) {
			final LoiDeRatification loiDeRatification = activiteNormative.getDocument().getAdapter(
					LoiDeRatification.class);
			final ConseilEtat conseilEtat = dossierDoc.getAdapter(ConseilEtat.class);
			if (!fluxRetour) {
				if (!loiDeRatification.isDateSaisineCELocked()) {
					loiDeRatification.setDateSaisineCE(conseilEtat.getDateSaisineCE());
				}
				if (!loiDeRatification.isSectionCELocked()) {
					loiDeRatification.setSectionCE(conseilEtat.getSectionCe());
				}
			}
			loiDeRatification.save(session);
		}
	}

	@Override
	public boolean checkDecretsValidationForMesure(final CoreSession session, final MesureApplicative mesure)
			throws ClientException {
		boolean areDecretsValidatedForStats = checkValidationForDocuments(session, mesure.getDecretIds());
		if (areDecretsValidatedForStats) {
			return mesure.getDecretsIdsInvalidated().isEmpty();
		} else {
			return false;
		}
	}

	@Override
	public boolean checkOrdonnancesValidationForHabilitation(final CoreSession session, final Habilitation habilitation)
			throws ClientException {
		boolean areOrdonnanceValidatedForStats = checkValidationForDocuments(session, habilitation.getOrdonnanceIds());
		if (areOrdonnanceValidatedForStats) {
			return habilitation.getOrdonnanceIdsInvalidated().isEmpty();
		} else {
			return false;
		}
	}

	@Override
	public boolean checkDecretsValidationForOrdonnance(final CoreSession session, final Ordonnance ordonnance)
			throws ClientException {
		boolean areDecretsValidatedForStats = checkValidationForDocuments(session, ordonnance.getDecretIds());
		if (areDecretsValidatedForStats) {
			return ordonnance.getDecretsIdsInvalidated().isEmpty();
		} else {
			return false;
		}
	}

	/**
	 * Vérifie s'il existe pour une liste d'ids au moins un document dont la validation n'a pas été confirmée
	 * 
	 * @param session
	 * @param documentsIdsList
	 * @return true si tous les documents sont valides, faux sinon
	 * @throws ClientException
	 */
	private boolean checkValidationForDocuments(final CoreSession session, final List<String> documentsIdsList)
			throws ClientException {
		boolean result = true;
		if (documentsIdsList != null && !documentsIdsList.isEmpty()) {
			final StringBuilder query = new StringBuilder(SELECT_UUID_FROM_ACT_NORM_AS_A).append(" WHERE a.")
					.append(TexteMaitreConstants.TEXTE_MAITRE_PREFIX).append(":")
					.append(TexteMaitreConstants.HAS_VALIDATION).append(" = 0 AND a.ecm:uuid IN (")
					.append(StringUtil.getQuestionMark(documentsIdsList.size())).append(")");

			final Object[] params = documentsIdsList.toArray();
			final Long count = QueryUtils
					.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(query.toString()), params);
			if (count > 0) {
				result = false;
			}
		}
		return result;
	}

	@Override
	public List<DocumentModel> getAllAplicationOrdonnanceDossiers(final CoreSession session, boolean legislatureEnCours)
			throws ClientException {

		final StringBuilder query = new StringBuilder(
				"SELECT d.ecm:uuid as id FROM ActiviteNormative as d WHERE d.norma:applicationOrdonnance = '1'");
		String legislature = getLegislatureParam(session, legislatureEnCours);
		Object[] queryParams = new Object[1];

		if (legislature != null) {
			query.append(" AND d.texm:legislaturePublication = ?");
			queryParams[0] = legislature;
		} else {
			queryParams = null;
		}

		query.append(" ORDER BY d.texm:datePublication DESC");

		final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE, query.toString(), queryParams);
		return docs;
	}

	@Override
	public void updateOrdonnancesListePubliee(final CoreSession session, boolean curLegis) {
		String generatedReportPath = "";
		FileOutputStream outputStream1 = null;
		FileOutputStream outputStream2 = null;
		try {
			final List<DocumentModel> documentModelList = getAllAplicationOrdonnanceDossiers(session, curLegis);
			if (curLegis) {
				generatedReportPath = getPathDirANStatistiquesPublie();
			} else {
				generatedReportPath = getPathDirANStatistiquesLegisPrecPublie(session);
			}

			File htmlReport = new File(generatedReportPath + System.getProperty("file.separator")
					+ "listeDesOrdonnances.html");
			if (!htmlReport.exists()) {
				htmlReport.createNewFile();
			}
			outputStream1 = new FileOutputStream(htmlReport);
			outputStream1.write("<table cellpadding='0' cellspacing='0'>".getBytes());
			TexteMaitre texteMaitre;
			String urlJOFRLegiFrance, numeroNor, titreActe;

			// trier les ordonnances par ordre decroissant
			final Comparator<DocumentModel> tmComparator = new Comparator<DocumentModel>() {
				@Override
				public int compare(final DocumentModel t1, final DocumentModel t2) {
					final TexteMaitre tm1 = t1.getAdapter(TexteMaitre.class);
					final TexteMaitre tm2 = t2.getAdapter(TexteMaitre.class);
					final String numero1 = tm1 == null || tm1.getNumero() == null ? "" : tm1.getNumero().trim();
					final String numero2 = tm2 == null || tm2.getNumero() == null ? "" : tm2.getNumero().trim();

					try {
						if (numero1.indexOf("-") == -1 || numero2.indexOf("-") == -1) {
							return Integer.parseInt(numero2.replace("-", ""))
									- Integer.parseInt(numero1.replaceAll("-", ""));
						} else {
							final String year1 = numero1.substring(0, numero1.indexOf("-")).trim();
							final String year2 = numero2.substring(0, numero2.indexOf("-")).trim();
							final String num1 = numero1.substring(numero1.indexOf("-") + 1).trim();
							final String num2 = numero2.substring(numero2.indexOf("-") + 1).trim();

							if (year1.equals(year2)) {
								return Integer.parseInt(num2) - Integer.parseInt(num1);
							} else if (Integer.parseInt(year2) > Integer.parseInt(year1)) {
								return 1;
							} else {
								return -1;
							}
						}
					} catch (final Exception e) {
						return numero2.compareTo(numero1);
					}
				}
			};
			Collections.sort(documentModelList, tmComparator);

			for (final DocumentModel documentModel : documentModelList) {
				texteMaitre = documentModel.getAdapter(TexteMaitre.class);
				if (texteMaitre.isApplicationDirecte()) {
					continue;
				}
				numeroNor = texteMaitre.getNumeroNor();
				titreActe = texteMaitre.getTitreOfficiel();
				if (titreActe == null) {
					titreActe = numeroNor;
				}
				urlJOFRLegiFrance = createLienJORFLegifrance(numeroNor);
				outputStream1
						.write(("<tr><td><span onclick=\"eltDeSuivi('" + numeroNor + "', this)\" class='titreSpan' >"
								+ titreActe + "</span>&nbsp;" + "<a href='" + urlJOFRLegiFrance + "' target='_blank' ><img src='view.png' style='border:none;cursor:pointer' alt='more' /></a></td></tr>")
								.getBytes());
			}
			outputStream1.write("</table>".getBytes());

			// Par Ministere

			final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
			htmlReport = new File(generatedReportPath + System.getProperty("file.separator")
					+ "listeDesMinistereOrdonnances.html");
			if (!htmlReport.exists()) {
				htmlReport.createNewFile();
			}
			outputStream2 = new FileOutputStream(htmlReport);
			outputStream2.write("<table cellpadding='5' cellspacing='0'>".getBytes());
			outputStream2
					.write("<tr><th>Ministère</th><th>Taux d’application par ministère</th><th>Mesures en attente de décrets</th></tr>"
							.getBytes());

			// order ministeres par ordre protocolaire
			final List<EntiteNode> ministeresListNodes = ministeresService.getCurrentMinisteres();

			final TreeMap<Long, OrganigrammeNode> tmMinisteres = new TreeMap<Long, OrganigrammeNode>();
			for (final EntiteNode entity : ministeresListNodes) {

				if (entity != null && entity.getOrdre() != null && entity.getId() != null
						&& entity.isSuiviActiviteNormative()) {
					tmMinisteres.put(entity.getOrdre(), entity);
				}
			}
			for (final OrganigrammeNode ministereNode : tmMinisteres.values()) {

				// node = ministeresService.getEntiteNode(ministere);
				String realLabel = ministereNode.getLabel();
				outputStream2.write(("<tr><td>" + realLabel + "</td>" + "<td><span onclick=\"eltDeSuiviParMin('"
						+ ministereNode.getId() + "',this)\"><img src='percent.png' alt=\"Détail mesures pour '"
						+ realLabel + "'\"></span></td>" + "<td><span onclick=\"parMinMesure('" + ministereNode.getId()
						+ "',this)\"><img src='index_new.png' alt=\"Détail mesures pour '" + realLabel
						+ "'\"></span></td>" + "</tr>").getBytes());
			}
			outputStream2.write("</table>".getBytes());

		} catch (final Exception e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_GENERATE_HTML_TEC,
					"Erreur lors de l'update de la liste des ministeres pour la page d'accueil -> generatedReportPath ="
							+ generatedReportPath, e);
		} finally {
			IOUtils.closeQuietly(outputStream1);
			IOUtils.closeQuietly(outputStream2);
		}
	}
}