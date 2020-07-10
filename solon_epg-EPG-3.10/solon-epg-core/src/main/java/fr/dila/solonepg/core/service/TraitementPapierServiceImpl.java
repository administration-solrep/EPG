package fr.dila.solonepg.core.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.directory.DirectoryException;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.directory.api.DirectoryService;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.api.service.TraitementPapierService;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.LogDocumentUpdateServiceImpl;
import fr.dila.st.core.service.STServiceLocator;

public class TraitementPapierServiceImpl extends LogDocumentUpdateServiceImpl implements TraitementPapierService {

	private static final long		serialVersionUID	= -2682137841441174481L;

	private static final Log		log					= LogFactory.getLog(TraitementPapierServiceImpl.class);
	private static final STLogger	LOGGER				= STLogFactory.getLog(TraitementPapierServiceImpl.class);

	@Override
	protected Set<String> getUnLoggableEntry() throws ClientException {
		Set<String> unLoggableEntryList = new HashSet<String>();
		// note : toutes les valeurs du traitement papier sont loggées
		return unLoggableEntryList;
	}

	@Override
	protected Map<String, Object> getMap(DocumentModel dossierDocument) throws ClientException {
		// on récupère toutes les propriétés liées au traitement papier
		return dossierDocument.getProperties(TraitementPapierConstants.TRAITEMENT_PAPIER_SCHEMA);
	}

	@Override
	protected void fireEvent(CoreSession session, DocumentModel ancienDossier, Entry<String, Object> entry,
			Object nouveauDossierValue, String ancienDossierValueLabel) throws ClientException {
		final JournalService journalService = STServiceLocator.getJournalService();
		// journalisation de l'action dans les logs
		String entryKey = entry.getKey();
		SolonEpgVocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
		// récupération du label du traitement papier
		String traitementPapierLabel = vocService.getLabelFromId(STVocabularyConstants.TRAITEMENT_PAPIER_LABEL,
				entryKey.substring(3), STVocabularyConstants.COLUMN_LABEL);

		// récupération de la valeur des enumération grâce au vocabulaire
		ancienDossierValueLabel = (String) getVocabularyValue(session, entryKey, ancienDossierValueLabel, vocService);
		nouveauDossierValue = getVocabularyValue(session, entryKey, nouveauDossierValue, vocService);

		String comment = traitementPapierLabel + " : '" + (nouveauDossierValue != null ? nouveauDossierValue : "")
				+ "' remplace '" + (ancienDossierValueLabel != null ? ancienDossierValueLabel : "") + "'";
		journalService.journaliserAction(session, ancienDossier, SolonEpgEventConstant.TRAITEMENT_PAPIER_UPDATE,
				comment, SolonEpgEventConstant.CATEGORY_TRAITEMENT_PAPIER);
	}

	/**
	 * Récupére le label associé à l'id du vocabulaire
	 * 
	 * @param value
	 * @return
	 */
	protected Object getVocabularyValue(CoreSession session, String entryKey, Object id,
			SolonEpgVocabularyService vocService) {
		Object value = id;
		if (id != null && id.toString() != null && !id.toString().isEmpty()) {
			if ("sensAvisCommunication".equals(entryKey)) {
				value = vocService.getLabelFromId(VocabularyConstants.TYPE_AVIS_TP_DIRECTORY_NAME, id.toString(),
						STVocabularyConstants.COLUMN_LABEL);
			} else if ("tp:priorite".equals(entryKey)) {
				value = vocService.getLabelFromId(VocabularyConstants.NIVEAU_PRIORITE_TP_DIRECTORY_NAME, id.toString(),
						STVocabularyConstants.COLUMN_LABEL);
			} else if ("tp:signataire".equals(entryKey)) {
				value = vocService.getLabelFromId(VocabularyConstants.TYPE_SIGNATAIRE_TP_DIRECTORY_NAME, id.toString(),
						STVocabularyConstants.COLUMN_LABEL);
			} else if ("tp:publicationDelai".equals(entryKey)) {
				value = vocService.getLabelFromId(VocabularyConstants.DELAI_PUBLICATION, id.toString(),
						STVocabularyConstants.COLUMN_LABEL);
			} else if ("tp:publicationModePublication".equals(entryKey)) {
				final IdRef docRef = new IdRef(id.toString());
				try {
					if (session.exists(docRef)) {
						ModeParution mode = session.getDocument(docRef).getAdapter(ModeParution.class);
						value = mode.getMode();
					}
				} catch (ClientException exc) {
					LOGGER.error(session, EpgLogEnumImpl.FAIL_GET_MODE_PARUTION_TEC, id, exc);
				}
			}
		}
		return value;
	}

	@Override
	public void saveTraitementPapier(CoreSession session, DocumentModel dossierDoc) throws ClientException {
		logAllDocumentUpdate(session, dossierDoc);
		updateChargeMissionAndConseillerPmValidation(session, dossierDoc);
		session.saveDocument(dossierDoc);
		session.save();

	}

	private void updateChargeMissionAndConseillerPmValidation(CoreSession session, DocumentModel dossierDoc)
			throws ClientException {
		final STUserService stUserService = STServiceLocator.getSTUserService();

		TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);

		// on ajoute l'utilisateur qui a validé le fichier dans les champs chargés de mission ou conseiller Premier
		// Ministre du Dossier
		Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		Set<String> chargeMission = new HashSet<String>(dossier.getNomCompletChargesMissions());

		List<DestinataireCommunication> listDestinataireCommunicationCM = traitementPapier
				.getChargeMissionCommunication();
		if (listDestinataireCommunicationCM != null) {
			for (DestinataireCommunication destinataireCommunication : listDestinataireCommunicationCM) {
				if (isAvisFavorable(destinataireCommunication.getSensAvis())) {
					chargeMission.add(stUserService.getUserInfo(
							destinataireCommunication.getNomDestinataireCommunication(),
							STUserService.CIVILITE_ABREGEE_PRENOM_NOM));
				} else {
					chargeMission.remove(stUserService.getUserInfo(
							destinataireCommunication.getNomDestinataireCommunication(),
							STUserService.CIVILITE_ABREGEE_PRENOM_NOM));
				}
			}
		}

		dossier.setNomCompletChargesMissions(new ArrayList<String>(chargeMission));
		dossier.save(session);
		session.save();

		Set<String> conseillerPm = new HashSet<String>(dossier.getNomCompletConseillersPms());

		List<DestinataireCommunication> listDestinataireCommunicationPM = traitementPapier.getCabinetPmCommunication();
		if (listDestinataireCommunicationPM != null) {
			for (DestinataireCommunication destinataireCommunication : listDestinataireCommunicationPM) {
				if (isAvisFavorable(destinataireCommunication.getSensAvis())) {
					conseillerPm.add(stUserService.getUserInfo(
							destinataireCommunication.getNomDestinataireCommunication(),
							STUserService.CIVILITE_ABREGEE_PRENOM_NOM));
				} else {
					conseillerPm.remove(stUserService.getUserInfo(
							destinataireCommunication.getNomDestinataireCommunication(),
							STUserService.CIVILITE_ABREGEE_PRENOM_NOM));
				}
			}
		}

		dossier.setNomCompletConseillersPms(new ArrayList<String>(conseillerPm));
		dossier.save(session);
		session.save();
	}

	/**
	 * Avis à partir du vocabulaire (ie type_avis_traitement_papier.csv)
	 * 
	 * @param avis
	 * @return
	 * @throws ClientException
	 */
	private Boolean isAvisFavorable(String avis) throws ClientException {
		final DirectoryService directoryService = STServiceLocator.getDirectoryService();
		Session session = null;
		DocumentModel documentModel = null;
		try {
			session = directoryService.open(VocabularyConstants.VOCABULARY_TYPE_AVIS_TP_DIRECTORY);
			documentModel = session.getEntry(avis);
			String type = (String) documentModel.getProperty(VocabularyConstants.VOCABULARY_TYPE_AVIS_TP_SCHEMA,
					VocabularyConstants.VOCABULARY_TYPE_AVIS_TP_TYPE);
			return type != null && type.equals("FAV");
		} catch (Exception e) {
			return false;
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (DirectoryException e) {
					log.error("Error while closing session");
				}
			}
		}
	}

}
