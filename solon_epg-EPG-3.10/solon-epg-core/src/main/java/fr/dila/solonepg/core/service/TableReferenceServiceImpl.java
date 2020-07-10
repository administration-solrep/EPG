package fr.dila.solonepg.core.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mortbay.log.Log;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.platform.ui.web.directory.VocabularyEntry;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.constant.SolonEpgTableReferenceConstants;
import fr.dila.solonepg.api.dto.tablereference.ModeParutionDTO;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;

/**
 * Implémentation du service pour la partie Table de référence
 * 
 */
public class TableReferenceServiceImpl implements TableReferenceService {

	/**
	 * Serial version UID
	 */
	private static final long	serialVersionUID						= -5834878799760207202L;

	private static final String	QUERY									= "SELECT tr.ecm:uuid as id FROM TableReference as tr";

	private static final String	MODE_PARUTION_QUERY						= "SELECT * FROM ModeParution";

	private static final String	MODE_PARUTION_ACTIFS					= "SELECT m.ecm:uuid as id FROM ModeParution as m WHERE (m.mod:dateDebut <= ? OR m.mod:dateDebut is NULL) AND (m.mod:dateFin >= ? OR m.mod:dateFin is NULL)";

	private static final String	MODE_PARUTION_ACTIFS_RECHERCHE_SUR_NOM	= "SELECT m.ecm:uuid as id FROM ModeParution as m WHERE (m.mod:dateDebut <= ? OR m.mod:dateDebut is NULL) AND (m.mod:dateFin >= ? OR m.mod:dateFin is NULL) AND m.mod:mode LIKE '";

	@Override
	public DocumentModel getTableReference(final CoreSession session) throws ClientException {

		final List<DocumentModel> documentList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE, QUERY, null);
		DocumentModel document = null;
		if (documentList == null || documentList.isEmpty()) {
			// creation d'une table de reference par defaut
			document = new DocumentModelImpl(SolonEpgTableReferenceConstants.TABLE_REFERENCE_PATH,
					SolonEpgTableReferenceConstants.TABLE_REFERENCE_ID,
					SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE);
			document = session.createDocument(document);
			session.save();
		} else {
			// il n'y a qu'un seul document Table de référence : on prend le premier élément de la liste
			document = documentList.get(0);
		}
		return document;
	}

	@Override
	public String getMinisterePrm(final CoreSession session) throws ClientException {
		final DocumentModel tableRef = getTableReference(session);
		final TableReference tableRefDoc = tableRef.getAdapter(TableReference.class);

		return tableRefDoc.getMinisterePM();
	}

	@Override
	public List<VocabularyEntry> getNorDirectionsForCreation(final CoreSession session) throws ClientException {
		final DocumentModel tableRef = getTableReference(session);
		final TableReference tableRefDoc = tableRef.getAdapter(TableReference.class);
		final List<String> directionsPM = tableRefDoc.getDirectionsPM();
		if (directionsPM == null || directionsPM.isEmpty()) {
			return null;
		}
		// récupération des 3 premières lettres : par défaut PRM
		String ministereNor = null;
		final String ministereId = getMinisterePrm(session);

		if (ministereId == null || ministereId.isEmpty()) {
			return null;
		}

		final EntiteNode ministere = STServiceLocator.getSTMinisteresService().getEntiteNode(ministereId);

		ministereNor = ministere.getNorMinistere();
		if (ministereNor == null) {
			return null;
		}

		final List<VocabularyEntry> norDirectionList = new ArrayList<VocabularyEntry>();

		// récupération de la lettre du nor

		final STUsAndDirectionService usService = STServiceLocator.getSTUsAndDirectionService();
		for (final String idDirection : directionsPM) {
			final UniteStructurelleNode usEpg = usService.getUniteStructurelleNode(idDirection);

			final String norDirectionFull = ministereNor + usEpg.getNorDirectionForMinistereId(ministereId);
			norDirectionList.add(new VocabularyEntry(idDirection, norDirectionFull));

		}
		return norDirectionList;
	}

	@Override
	public List<VocabularyEntry> getRetoursDAN(final CoreSession session) throws ClientException {
		final DocumentModel tableRef = getTableReference(session);
		final TableReference tableRefDoc = tableRef.getAdapter(TableReference.class);
		final List<String> retoursDANID = tableRefDoc.getRetourDAN();
		
		if (retoursDANID == null) {
			return null;
		}
		System.out.println("retoursDAN Size : " + retoursDANID.size());
		final List<VocabularyEntry> vocRetoursDAN = new ArrayList<VocabularyEntry>();
		STPostesService stPostesService = SolonEpgServiceLocator.getSTPostesService();
		
		for (final String idPosteDAN : retoursDANID) {
			final PosteNode posteNode = stPostesService.getPoste(idPosteDAN);
			
			System.out.println("Poste ID : " + idPosteDAN + ", posteNode : " + posteNode);
			
			if (posteNode!=null) {
				final String posteLabel = posteNode.getLabel();
				System.out.println("Poste ID : " + idPosteDAN + ", label  : " + posteLabel);
				vocRetoursDAN.add(new VocabularyEntry(idPosteDAN, posteLabel));
			} else {
				Log.warn("Le poste " + idPosteDAN + " n'a pas été trouvé");
			}
		}
		
		return vocRetoursDAN;
	}

		
	@Override
	public List<DocumentModel> getModesParutionList(CoreSession session) throws ClientException {
		// On récupère tous les modes de parution existants en base
		return new ArrayList<DocumentModel>(QueryUtils.doQuery(session, MODE_PARUTION_QUERY, 0, 0));
	}

	@Override
	public List<DocumentModel> getModesParutionActifsList(CoreSession session) throws ClientException {
		StringBuilder query = new StringBuilder(MODE_PARUTION_ACTIFS);
		Calendar today = Calendar.getInstance();
		Object[] params = { today, today };
		// On récupère tous les modes de parution existants en base
		List<String> modesParutionsIdsActifs = QueryUtils.doUFNXQLQueryForIdsList(session, query.toString(), params);
		return QueryUtils.retrieveDocumentsUnrestricted(session,
				SolonEpgTableReferenceConstants.MODE_PARUTION_DOCUMENT_TYPE, modesParutionsIdsActifs);
	}

	@Override
	public void createModeParution(CoreSession session, ModeParutionDTO mode) throws ClientException {
		// Récupération des données du DTO
		String modeParutionStr = mode.getMode();
		Date dateDebut = mode.getDateDebut();
		Date dateFin = mode.getDateFin();

		// On enregistre uniquement si au moins un champ a été renseigné
		if (!StringUtil.isEmpty(modeParutionStr) || dateDebut != null || dateFin != null) {
			// Création du documentModel
			DocumentModel modelDesired = session
					.createDocumentModel(SolonEpgTableReferenceConstants.MODE_PARUTION_DOCUMENT_TYPE);
			modelDesired
					.setPathInfo(SolonEpgTableReferenceConstants.TABLE_REFERENCE_PATH, UUID.randomUUID().toString());
			ModeParution modeParution = modelDesired.getAdapter(ModeParution.class);
			modeParution.setMode(modeParutionStr);
			if (dateDebut != null) {
				Calendar dateDebutCal = Calendar.getInstance();
				dateDebutCal.setTime(dateDebut);
				modeParution.setDateDebut(dateDebutCal);
			}
			if (dateFin != null) {
				Calendar dateFinCal = Calendar.getInstance();
				dateFinCal.setTime(dateFin);
				modeParution.setDateFin(dateFinCal);
			}
			// Enregistrement définitive du document model
			DocumentModel documentSaved = session.createDocument(modeParution.getDocument());
			session.save();
			mode.setId(documentSaved.getId());
		}
	}

	@Override
	public void updateModeParution(CoreSession session, ModeParutionDTO mode) throws ClientException {
		// Récupération des données du DTO
		String modeParutionStr = mode.getMode();
		Date dateDebut = mode.getDateDebut();
		Date dateFin = mode.getDateFin();
		String id = mode.getId();

		// Récupération du document model existant
		DocumentModel modeParutionDoc = session.getDocument(new IdRef(id));
		ModeParution modeParution = modeParutionDoc.getAdapter(ModeParution.class);

		// Mise à jour des données dans le document model
		modeParution.setMode(modeParutionStr);
		if (dateDebut != null) {
			Calendar dateDebutCal = Calendar.getInstance();
			dateDebutCal.setTime(dateDebut);
			modeParution.setDateDebut(dateDebutCal);
		} else {
			modeParution.setDateDebut(null);
		}
		if (dateFin != null) {
			Calendar dateFinCal = Calendar.getInstance();
			dateFinCal.setTime(dateFin);
			modeParution.setDateFin(dateFinCal);
		} else {
			modeParution.setDateFin(null);
		}
		// Enregistrement des modifications
		modeParution.save(session);
	}

	@Override
	public String getPostePublicationId(CoreSession session) throws ClientException {
		final DocumentModel tableRef = getTableReference(session);
		final TableReference tableRefDoc = tableRef.getAdapter(TableReference.class);
		return tableRefDoc.getPostePublicationId();
	}

	@Override
	public String getPosteDanId(CoreSession session) throws ClientException {
		final DocumentModel tableRef = getTableReference(session);
		final TableReference tableRefDoc = tableRef.getAdapter(TableReference.class);
		return tableRefDoc.getPosteDanId();
	}
	
	@Override
	public String getPosteDanIdAvisRectificatifCE(CoreSession session) throws ClientException {
		final DocumentModel tableRef = getTableReference(session);
		final TableReference tableRefDoc = tableRef.getAdapter(TableReference.class);
		return tableRefDoc.getPosteDanIdAvisRectificatifCE();
	}

	@Override
	public List<String> getModesParutionListFromName(CoreSession session, String name) throws ClientException {
		// On récupère tous les modes de parution existants en base qui ont comme nom le name qu'on passe dans la
		// méthode
		final String query = MODE_PARUTION_ACTIFS_RECHERCHE_SUR_NOM + name + "'";
		Calendar today = Calendar.getInstance();
		Object[] params = { today, today };
		// On récupère tous les modes de parution existants en base
		List<String> modesParutionsIdsActifs = QueryUtils.doUFNXQLQueryForIdsList(session, query.toString(), params);
		return modesParutionsIdsActifs;
	}
}
