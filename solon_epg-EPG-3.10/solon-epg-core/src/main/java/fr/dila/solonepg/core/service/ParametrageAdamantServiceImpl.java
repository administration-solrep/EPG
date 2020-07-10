package fr.dila.solonepg.core.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;

import fr.dila.solonepg.api.administration.ParametrageAdamant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgParametrageAdamantConstants;
import fr.dila.solonepg.api.service.ParametrageAdamantService;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.service.LogDocumentUpdateServiceImpl;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Implémentation du service de gestion des paramètres Adamant.
 * 
 * @author BBE
 */
public class ParametrageAdamantServiceImpl extends LogDocumentUpdateServiceImpl implements ParametrageAdamantService {

	private static final long serialVersionUID = -3444289354122138431L;

	/**
	 * Requete permettant de récupérer le document contenant les informations sur le paramètrage Adamant.
	 */
	private static final String GET_PARAMETRAGE_ADAMANT_DOCUMENT_QUERY = "select * from "
			+ SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_TYPE + " WHERE " + "ecm:isProxy = 0 ";

	private static final Set<String> UNLOGGABLE_ENTRY_LIST = Collections.unmodifiableSet(initUnloggableEntryList());
	
	private static Map<String, String> INIT_ENTRY_LABEL_LIST = new HashMap<String, String>();

    static {
        // TODO mettre dans un fichier de conf
        INIT_ENTRY_LABEL_LIST.put(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_NUMERO_SOLON_PROPERTY,
                "Numero SOLON");
        INIT_ENTRY_LABEL_LIST.put(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_ORIGINATING_AGENCY_BLOC_IDENTIFIER_PROPERTY,
                "Bloc OriginatingAgency Identifier");
        INIT_ENTRY_LABEL_LIST.put(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SUBMISSION_AGENCY_BLOC_IDENTIFIER_PROPERTY,
                "Bloc SubmissionAgency Identifier");
        INIT_ENTRY_LABEL_LIST.put(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_ARCHIVAL_PROFILE_PROPERTY,
                "ArchivalProfile");
        INIT_ENTRY_LABEL_LIST.put(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_ORIGINATING_AGENCY_IDENTIFIER_PROPERTY,
                "OriginatingAgencyIdentifier");
        INIT_ENTRY_LABEL_LIST.put(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SUBMISSION_AGENCY_IDENTIFIER_PROPERTY,
                "SubmissionAgencyIdentifier");
        INIT_ENTRY_LABEL_LIST.put(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DELAI_ELIGIBILITE_PROPERTY,
                "Délai pour éligibilité (en mois)");
        INIT_ENTRY_LABEL_LIST.put(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_NB_DOSSIER_EXTRACTION_PROPERTY,
                "Nombre de dossiers traités par une exécution du batch");
    }

	/**
	 * Liste des valeurs xpath des champs qui n'ont pas à être loggé.
	 * 
	 * @return
	 * @throws ClientException
	 */
	private static Set<String> initUnloggableEntryList() {
		return new HashSet<String>();
	}

	private String paramAdamantDocId = null;

	@Override
	public ParametrageAdamant getParametrageAdamantDocument(CoreSession session) throws ClientException {
		DocumentModel parametrageAdamantDocument = null;
		synchronized (this) {
			if (paramAdamantDocId == null) {
				final DocumentModelList dml = session.query(GET_PARAMETRAGE_ADAMANT_DOCUMENT_QUERY);
				if (dml == null || dml.isEmpty()) {
					throw new ClientException("Document " + SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_TYPE
							+ "introuvable");
				} else if (dml.size() > 1) {
					throw new ClientException("Plusieurs instances pour "
							+ SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_TYPE);
				}
				parametrageAdamantDocument = dml.get(0);
				paramAdamantDocId = parametrageAdamantDocument.getId();
			}
		}

		if (parametrageAdamantDocument == null) {
			// id connu : charger la dernière vesion du document
			parametrageAdamantDocument = session.getDocument(new IdRef(paramAdamantDocId));
		}

		if (parametrageAdamantDocument != null) {
			return parametrageAdamantDocument.getAdapter(ParametrageAdamant.class);
		} else {
			return null;
		}
	}

	@Override
	protected void fireEvent(CoreSession session, final DocumentModel ancienDossier, final Entry<String, Object> entry,
			final Object nouveauDocumentValue, final String ancienDossierValueLabel) throws ClientException {
		final String entryKey = entry.getKey();
		final String propertyName = entryKey.substring(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_PREFIX.length() + 1);
		// journalisation de l'action dans les logs
		fireEvent(session, ancienDossier, propertyName, nouveauDocumentValue, ancienDossierValueLabel);
	}

	protected void fireEvent(final CoreSession session, final DocumentModel ancienDossier, final String propertyName,
			final Object nouveauDocumentValue, final String ancienDossierValueLabel) throws ClientException {
		// journalisation de l'action dans les logs
		final JournalService journalService = STServiceLocator.getJournalService();
		// récupération du label de l'élément modifié pour le commentaire
		final String bordereauLabel = INIT_ENTRY_LABEL_LIST.get(propertyName);
		String comment = bordereauLabel + " : '" + (nouveauDocumentValue != null ? nouveauDocumentValue : "") + "' remplace '"
				+ (ancienDossierValueLabel != null ? ancienDossierValueLabel : "") + "'";

		// envoie l'événement
		journalService.journaliserActionAdministration(session, SolonEpgEventConstant.PARAM_ADAMANT_UPDATE_EVENT, comment);
	}

	@Override
	protected Map<String, Object> getMap(DocumentModel dossierDocument) throws ClientException {
		// on récupère toutes les propriétés liées au paramètrage Adamant
		return dossierDocument.getProperties(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA);
	}

	@Override
	protected Set<String> getUnLoggableEntry() throws ClientException {
		return UNLOGGABLE_ENTRY_LIST;
	}
}
