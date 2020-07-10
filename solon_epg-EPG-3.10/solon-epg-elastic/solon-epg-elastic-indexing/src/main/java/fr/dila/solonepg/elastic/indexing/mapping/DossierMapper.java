package fr.dila.solonepg.elastic.indexing.mapping;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.ACL;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.MappingException;
import fr.dila.solonepg.elastic.models.ElasticDossier;

public class DossierMapper extends DefaultComponent implements IDossierMapper {

	private IReferentielMapper	referentielMapper;

	@Override
	public void activate(ComponentContext context) throws Exception {
		referentielMapper = Framework.getService(IReferentielMapper.class);
	}

	@Override
	public void deactivate(ComponentContext context) throws Exception {
		referentielMapper = null;
	}

	@Override
	public ElasticDossier from(DocumentModel documentModel, CoreSession session) throws MappingException {
		try {
			return doFrom(documentModel, session);
		} catch (ClientException e) {
			throw new MappingException(String.format("Erreur de mapping du dossier %s", documentModel.getId()), e);
		}
	}

	@SuppressWarnings("unchecked")
	private ElasticDossier doFrom(DocumentModel documentModel, CoreSession session) throws ClientException,
			MappingException {
		ElasticDossier exportableDossier = new ElasticDossier();

		/* UID */
		exportableDossier.setUid(documentModel.getId()); // Identifiant du dossier

		/* case:... */
		exportableDossier.setCaseDocumentsId((List<String>) documentModel
				.getPropertyValue(CaseConstants.MAILBOX_DOCUMENTS_ID_PROPERTY_NAME));

		/* cmdist:... */
		exportableDossier.setCmdistAllActionParticipantMailboxes((List<String>) documentModel
				.getPropertyValue(CaseConstants.ALL_ACTION_PARTICIPANTS_PROPERTY_NAME));
		exportableDossier.setCmdistAllCopyParticipantMailboxes((List<String>) documentModel
				.getPropertyValue(CaseConstants.ALL_COPY_PARTICIPANTS_PROPERTY_NAME));
		exportableDossier.setCmdistInitialActionExternalParticipantMailboxes((List<String>) documentModel
				.getPropertyValue(CaseConstants.INITIAL_ACTION_INTERNAL_PARTICIPANTS_PROPERTY_NAME));
		exportableDossier.setCmdistInitialActionInternalParticipantMailboxes((List<String>) documentModel
				.getPropertyValue(CaseConstants.INITIAL_ACTION_INTERNAL_PARTICIPANTS_PROPERTY_NAME));
		exportableDossier.setCmdistInitialCopyExternalParticipantMailboxes((List<String>) documentModel
				.getPropertyValue(CaseConstants.INITIAL_COPY_EXTERNAL_PARTICIPANTS_PROPERTY_NAME));
		exportableDossier.setCmdistInitialCopyInternalParticipantMailboxes((List<String>) documentModel
				.getPropertyValue(CaseConstants.INITIAL_COPY_INTERNAL_PARTICIPANTS_PROPERTY_NAME));

		/* common:... */
		exportableDossier.setCommonIcon((String) documentModel.getPropertyValue(ElasticDossier.COMMON_ICON));
		exportableDossier.setCommonIconExpanded((String) documentModel
				.getPropertyValue(ElasticDossier.COMMON_ICON_EXPANDED));
		exportableDossier.setCommonSize((String) documentModel.getPropertyValue(ElasticDossier.COMMON_SIZE));

		/* consetat:... => Champs de la section CE */
		exportableDossier.setConsetatDateAgCe(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.CONSETAT_DATE_AG_CE)));
		exportableDossier.setConsetatDateSaisineCE(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.CONSETAT_DATE_SAISINE_CE)));
		exportableDossier.setConsetatDateSectionCe(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.CONSETAT_DATE_SECTION_CE)));
		exportableDossier.setConsetatDateSortieCE(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.CONSETAT_DATE_SORTIE_CE)));
		exportableDossier.setConsetatDateTransmissionSectionCe(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.CONSETAT_DATE_TRANSMISSION_SECTION_CE)));
		exportableDossier.setConsetatNumeroISA((String) documentModel
				.getPropertyValue(ElasticDossier.CONSETAT_NUMERO_ISA));
		exportableDossier.setConsetatRapporteurCe((String) documentModel
				.getPropertyValue(ElasticDossier.CONSETAT_RAPPORTEUR_CE));
		exportableDossier.setConsetatReceptionAvisCE((String) documentModel
				.getPropertyValue(ElasticDossier.CONSETAT_RECEPTION_AVIS_CE));
		exportableDossier.setConsetatSectionCe((String) documentModel
				.getPropertyValue(ElasticDossier.CONSETAT_SECTION_CE));

		/* dc:... */
		exportableDossier.setDcCoverage((String) documentModel.getPropertyValue(ElasticDossier.DC_COVERAGE));
		exportableDossier.setDcCreated(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.DC_CREATED)));
		exportableDossier.setDcCreator((String) documentModel.getPropertyValue(ElasticDossier.DC_CREATOR));
		exportableDossier.setDcExpired(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.DC_EXPIRED)));
		exportableDossier.setDcFormat((String) documentModel.getPropertyValue(ElasticDossier.DC_FORMAT));
		exportableDossier.setDcIssued((String) documentModel.getPropertyValue(ElasticDossier.DC_ISSUED));
		exportableDossier.setDcLanguage((String) documentModel.getPropertyValue(ElasticDossier.DC_LANGUAGE));
		exportableDossier.setDcLastContributor((String) documentModel
				.getPropertyValue(ElasticDossier.DC_LAST_CONTRIBUTOR));
		exportableDossier.setDcModified(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.DC_MODIFIED)));
		exportableDossier.setDcNature((String) documentModel.getPropertyValue(ElasticDossier.DC_NATURE));
		exportableDossier.setDcRights((String) documentModel.getPropertyValue(ElasticDossier.DC_RIGHTS));
		exportableDossier.setDcSource((String) documentModel.getPropertyValue(ElasticDossier.DC_SOURCE));
		exportableDossier.setDcTitle((String) documentModel.getTitle());
		exportableDossier.setDcValid((String) documentModel.getPropertyValue(ElasticDossier.DC_VALID));

		/* dos:... */
		exportableDossier.setDosAdoption((Boolean) documentModel.getPropertyValue(ElasticDossier.DOS_ADOPTION));
		exportableDossier.setDosApplicationLoi(referentielMapper.fromComplexeTypeList(documentModel
				.getPropertyValue(ElasticDossier.DOS_APPLICATION_LOI)));
		exportableDossier.setDosApplicationLoiRefs((List<String>) documentModel
				.getPropertyValue(ElasticDossier.DOS_APPLICATION_LOI_REFS));
		exportableDossier.setDosArchive((Boolean) documentModel.getPropertyValue(ElasticDossier.DOS_ARCHIVE));
		exportableDossier.setDosArriveeSolonTS((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_ARRIVEE_SOLON_TS));
		exportableDossier.setDosBaseLegaleActe((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_BASE_LEGALE_ACTE)); // Base légale
		exportableDossier.setDosBaseLegaleDate(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.DOS_BASE_LEGALE_DATE)));
		exportableDossier.setDosBaseLegaleNatureTexte((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_BASE_LEGALE_NATURE_TEXTE));
		exportableDossier.setDosBaseLegaleNumeroTexte((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_BASE_LEGALE_NUMERO_TEXTE));
		exportableDossier.setDosCandidat((String) documentModel.getPropertyValue(ElasticDossier.DOS_CANDIDAT));

		String categorieActeId = (String) documentModel.getPropertyValue(ElasticDossier.DOS_CATEGORIE_ACTE);
		exportableDossier.setDosCategorieActeId(categorieActeId);
		exportableDossier.setDosCategorieActe(referentielMapper.fromVocabulary(VocabularyConstants.CATEGORY_ACTE_CONVENTION_COLLECTIVE,
				categorieActeId)); // Catégorie d'acte

		exportableDossier.setDosCreated(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.DOS_CREATED))); // Date de création
		exportableDossier.setDosDateCandidature(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.DOS_DATE_CANDIDATURE)));
		exportableDossier.setDosDateDeMaintienEnProduction(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.DOS_DATE_DE_MAINTIEN_EN_PRODUCTION)));
		exportableDossier.setDosDateEnvoiJoTS(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.DOS_DATE_ENVOI_JO_TS)));
		exportableDossier.setDosDatePreciseePublication(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.DOS_DATE_PRECISEE_PUBLICATION))); // Date de publication souhaitée
		exportableDossier.setDosDatePublication(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.DOS_DATE_PUBLICATION))); // Date de publication
		exportableDossier.setDosDateSignature(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.DOS_DATE_SIGNATURE))); // Date de signature
		exportableDossier.setDosDateVersementArchivageIntermediaire(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.DOS_DATE_VERSEMENT_ARCHIVAGE_INTERMEDIAIRE)));
		exportableDossier.setDosDateVersementTS(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.DOS_DATE_VERSEMENT_TS)));
		exportableDossier.setDosDecretNumerote((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_DECRET_NUMEROTE));

		String delaiPublicationId = (String) documentModel.getPropertyValue(ElasticDossier.DOS_DELAI_PUBLICATION);
		exportableDossier.setDosDelaiPublication(referentielMapper.fromVocabulary(
				VocabularyConstants.DELAI_PUBLICATION, delaiPublicationId));

		exportableDossier.setDosDeleted((Boolean) documentModel.getPropertyValue(ElasticDossier.DOS_DELETED));

		String directionAttacheeId = (String) documentModel.getPropertyValue(ElasticDossier.DOS_DIRECTION_ATTACHE);
		exportableDossier.setDosDirectionAttacheId(referentielMapper
				.labelFromUniteStructurelleNode(directionAttacheeId)); // Direction de rattachement

		String directionRespId = (String) documentModel.getPropertyValue(ElasticDossier.DOS_DIRECTION_RESP);
		exportableDossier.setDosDirectionRespId(directionRespId);
		exportableDossier.setDosDirectionResp(referentielMapper.labelFromUniteStructurelleNode(directionRespId)); // Direction
																													// responsable

		exportableDossier.setDosDispositionHabilitation((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_DISPOSITION_HABILITATION));
		exportableDossier.setDosHabilitationCommentaire((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_HABILITATION_COMMENTAIRE));
		exportableDossier.setDosHabilitationDateTerme(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.DOS_HABILITATION_DATE_TERME)));
		exportableDossier.setDosHabilitationNumeroArticles((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_HABILITATION_NUMERO_ARTICLES));
		exportableDossier.setDosHabilitationNumeroOrdre((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_HABILITATION_NUMERO_ORDRE));
		exportableDossier.setDosHabilitationRefLoi((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_HABILITATION_REF_LOI));
		exportableDossier.setDosHabilitationTerme((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_HABILITATION_TERME));
		exportableDossier.setDosIdCreateurDossier((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_ID_CREATEUR_DOSSIER));
		exportableDossier.setDosIdDocumentFDD((String) documentModel
				.getPropertyValue(DossierSolonEpgConstants.DOSSIER_DOCUMENT_FOND_DOSSIER_ID));
		exportableDossier.setDosIdDocumentParapheur((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_ID_DOCUMENT_PARAPHEUR));
		exportableDossier.setDosIdDossier((String) documentModel.getPropertyValue(ElasticDossier.DOS_ID_DOSSIER));
		exportableDossier.setDosIndexationDir((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_INDEXATION_DIR));
		exportableDossier.setDosIndexationDirPub((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_INDEXATION_DIR_PUB));
		exportableDossier.setDosIndexationMin((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_INDEXATION_MIN));
		exportableDossier.setDosIndexationMinPub((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_INDEXATION_MIN_PUB));
		exportableDossier.setDosIndexationSgg((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_INDEXATION_SGG));
		exportableDossier.setDosIndexationSggPub((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_INDEXATION_SGG_PUB));
		exportableDossier.setDosIsActeReferenceForNumeroVersion((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_IS_ACTE_REFERENCE_FOR_NUMERO_VERSION));
		exportableDossier.setDosIsAfterDemandePublication((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_IS_AFTER_DEMANDE_PUBLICATION));
		exportableDossier.setDosIsDossierIssuInjection((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_IS_DOSSIER_ISSU_INJECTION));
		exportableDossier.setDosIsParapheurComplet((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_IS_PARAPHEUR_COMPLET));
		exportableDossier.setDosIsParapheurFichierInfoNonRecupere((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_IS_PARAPHEUR_FICHIER_INFO_NON_RECUPERE));
		exportableDossier.setDosIsParapheurTypeActeUpdated((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_IS_PARAPHEUR_TYPE_ACTE_UPDATED));
		exportableDossier.setDosIsUrgent((Boolean) documentModel.getPropertyValue(ElasticDossier.DOS_IS_URGENT));
		exportableDossier.setDosLastDocumentRoute((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_LAST_DOCUMENT_ROUTE));
		exportableDossier.setDosLibre((List<String>) documentModel.getPropertyValue(ElasticDossier.DOS_LIBRE)); // Champs
																												// libres
		exportableDossier.setDosMailRespDossier((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_MAIL_RESP_DOSSIER));
		exportableDossier.setDosMesureNominative((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_MESURE_NOMINATIVE));

		String ministereAttacheId = (String) (String) documentModel
				.getPropertyValue(ElasticDossier.DOS_MINISTERE_ATTACHE);
		exportableDossier.setDosMinistereAttacheId(ministereAttacheId);
		exportableDossier.setDosMinistereAttache(referentielMapper.labelFromMinistereNode(ministereAttacheId)); // Ministère de rattachement

		String ministereRespId = (String) (String) documentModel.getPropertyValue(ElasticDossier.DOS_MINISTERE_RESP);
		exportableDossier.setDosMinistereRespId(ministereRespId);
		exportableDossier.setDosMinistereResp(referentielMapper.labelFromMinistereNode(ministereRespId));	// Ministère responsable

		exportableDossier.setDosMotscles((List<String>) documentModel.getPropertyValue(ElasticDossier.DOS_MOTSCLES)); // Mots
																														// clés
		exportableDossier.setDosNbDossierRectifie((Long) documentModel
				.getPropertyValue(ElasticDossier.DOS_NB_DOSSIER_RECTIFIE));
		exportableDossier.setDosNomCompletChargesMissions((List<String>) documentModel
				.getPropertyValue(ElasticDossier.DOS_NOM_COMPLET_CHARGES_MISSIONS)); // Chargés de mission
		exportableDossier.setDosNomCompletConseillersPms((List<String>) documentModel
				.getPropertyValue(ElasticDossier.DOS_NOM_COMPLET_CONSEILLERS_PMS)); // Conseillers PMs
		exportableDossier.setDosNomCompletRespDossier((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_NOM_COMPLET_RESP_DOSSIER));
		exportableDossier.setDosNomRespDossier((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_NOM_RESP_DOSSIER));
		exportableDossier.setDosNorAttribue((Boolean) documentModel
				.getPropertyValue(DossierSolonEpgConstants.NOR_ATTRIBUE_DOSSIER_PROPERTY));
		exportableDossier.setDosNumeroNor((String) documentModel.getPropertyValue(ElasticDossier.DOS_NUMERO_NOR)); // Numéro
																													// NOR
		exportableDossier.setDosNumeroVersionActeOuExtrait((Long) documentModel
				.getPropertyValue(ElasticDossier.DOS_NUMERO_VERSION_ACTE_OU_EXTRAIT));
		exportableDossier.setDosPeriodicite((String) documentModel.getPropertyValue(ElasticDossier.DOS_PERIODICITE));
		exportableDossier.setDosPeriodiciteRapport((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_PERIODICITE_RAPPORT));
		exportableDossier.setDosPosteCreator((String) documentModel.getPropertyValue(ElasticDossier.DOS_POSTE_CREATOR));
		exportableDossier.setDosPourFournitureEpreuve(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.DOS_POUR_FOURNITURE_EPREUVE)));
		exportableDossier.setDosPrenomRespDossier((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_PRENOM_RESP_DOSSIER));
		exportableDossier.setDosPublicationIntegraleOuExtrait((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_PUBLICATION_INTEGRALE_OU_EXTRAIT));
		exportableDossier.setDosPublicationRapportPresentation((Boolean) documentModel
				.getPropertyValue(ElasticDossier.DOS_PUBLICATION_RAPPORT_PRESENTATION));
		exportableDossier.setDosPublicationsConjointes((List<String>) documentModel
				.getPropertyValue(ElasticDossier.DOS_PUBLICATIONS_CONJOINTES));
		exportableDossier.setDosPublie((Boolean) documentModel
				.getPropertyValue(DossierSolonEpgConstants.PUBLIE_DOSSIER_PROPERTY));
		exportableDossier.setDosQualiteRespDossier((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_QUALITE_RESP_DOSSIER));
		exportableDossier.setDosRubriques((List<String>) documentModel.getPropertyValue(ElasticDossier.DOS_RUBRIQUES)); // Rubriques

		String statutId = (String) documentModel.getPropertyValue(ElasticDossier.DOS_STATUT);
		exportableDossier.setDosStatutId(statutId);
		exportableDossier.setDosStatut(referentielMapper.fromVocabulary(
				VocabularyConstants.STATUT_DOSSIER_DIRECTORY_NAME, statutId)); // Statut du dossier

		String statutArchivageId = (String) documentModel.getPropertyValue(ElasticDossier.DOS_STATUT_ARCHIVAGE);
		exportableDossier.setDosStatutArchivageId(statutArchivageId);
		exportableDossier.setDosStatutArchivage(referentielMapper.fromVocabulary(
				VocabularyConstants.STATUT_ARCHIVAGE_FACET_VOCABULARY, statutArchivageId));

		exportableDossier.setDosTelRespDossier((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_TEL_RESP_DOSSIER));
		exportableDossier.setDosTitreActe((String) documentModel.getPropertyValue(ElasticDossier.DOS_TITRE_ACTE)); // Titre
																													// du
																													// dossier

		exportableDossier.setDosTranspositionDirective(referentielMapper.fromComplexeTypeList(documentModel
				.getPropertyValue(ElasticDossier.DOS_TRANSPOSITION_DIRECTIVE)));

		exportableDossier.setDosTranspositionDirectiveNumero((List<String>) documentModel
				.getPropertyValue(ElasticDossier.DOS_TRANSPOSITION_DIRECTIVE_NUMERO));
		exportableDossier.setDosTranspositionOrdoRefs((List<String>) documentModel
				.getPropertyValue(ElasticDossier.DOS_TRANSPOSITION_ORDO_REFS));
		exportableDossier.setDosTranspositionOrdonnance(referentielMapper.fromComplexeTypeList(documentModel
				.getPropertyValue(ElasticDossier.DOS_TRANSPOSITION_ORDONNANCE)));
		exportableDossier.setDosTypeActe(referentielMapper.fromVocabulary(VocabularyConstants.TYPE_ACTE_VOCABULARY,
				(String) documentModel.getPropertyValue(ElasticDossier.DOS_TYPE_ACTE))); // Type d'acte

		exportableDossier.setDosVecteurPublication(referentielMapper.labelsFromVecteurIdList(session,
				documentModel.getPropertyValue(ElasticDossier.DOS_VECTEUR_PUBLICATION))); // Vecteur de publication

		exportableDossier.setDosZoneComSggDila((String) documentModel
				.getPropertyValue(ElasticDossier.DOS_ZONE_COM_SGG_DILA));

		exportableDossier.setDosTexteEntreprise(
				(Boolean) documentModel.getPropertyValue(ElasticDossier.DOS_TEXTE_ENTREPRISE));
		exportableDossier.setDosDateEffet(ElasticUtils.toDates(
				(List<Calendar>) documentModel.getPropertyValue(ElasticDossier.DOS_DATE_EFFET)));

		exportableDossier.setNoteMimeType((String) documentModel.getPropertyValue(ElasticDossier.NOTE_MIME_TYPE));
		exportableDossier.setNoteNote((String) documentModel.getPropertyValue(ElasticDossier.NOTE_NOTE));

		exportableDossier.setRetdilaDateParutionJorf(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.RETDILA_DATE_PARUTION_JORF)));
		exportableDossier.setRetdilaDatePromulgation(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.RETDILA_DATE_PROMULGATION)));
		exportableDossier.setRetdilaIsPublicationEpreuvageDemandeSuivante((Boolean) documentModel
				.getPropertyValue(ElasticDossier.RETDILA_IS_PUBLICATION_EPREUVAGE_DEMANDE_SUIVANTE));
		exportableDossier.setRetdilaLegislaturePublication((String) documentModel
				.getPropertyValue(ElasticDossier.RETDILA_LEGISLATURE_PUBLICATION));

		String modeParutionId = (String) documentModel.getPropertyValue(ElasticDossier.RETDILA_MODE_PARUTION);
		exportableDossier.setRetdilaModeParution(referentielMapper.labelFromModeParution(session, modeParutionId));

		exportableDossier.setRetdilaNumeroTexteParutionJorf((String) documentModel
				.getPropertyValue(ElasticDossier.RETDILA_NUMERO_TEXTE_PARUTION_JORF));
		exportableDossier.setRetdilaPageParutionJorf((Long) documentModel
				.getPropertyValue(ElasticDossier.RETDILA_PAGE_PARUTION_JORF));
		exportableDossier.setRetdilaParutionBo(referentielMapper.fromComplexeTypeList(documentModel
				.getPropertyValue(ElasticDossier.RETDILA_PARUTION_BO)));
		exportableDossier.setRetdilaTitreOfficiel((String) documentModel
				.getPropertyValue(ElasticDossier.RETDILA_TITRE_OFFICIEL));

		exportableDossier.setTpAmpliationDestinataireMails((List<String>) documentModel
				.getPropertyValue(ElasticDossier.TP_AMPLIATION_DESTINATAIRE_MAILS));
		exportableDossier.setTpAmpliationHistorique(referentielMapper.fromComplexeTypeList(documentModel
				.getPropertyValue(ElasticDossier.TP_AMPLIATION_HISTORIQUE)));
		exportableDossier.setTpAutresDestinatairesCommunication(referentielMapper.fromComplexeTypeList(documentModel
				.getPropertyValue(ElasticDossier.TP_AUTRES_DESTINATAIRES_COMMUNICATION)));
		exportableDossier.setTpCabinetPmCommunication(referentielMapper.fromComplexeTypeList(documentModel
				.getPropertyValue(ElasticDossier.TP_CABINET_PM_COMMUNICATION)));
		exportableDossier.setTpCabinetSgCommunication(referentielMapper.fromComplexeTypeList(documentModel
				.getPropertyValue(ElasticDossier.TP_CABINET_SG_COMMUNICATION)));
		exportableDossier.setTpChargeMissionCommunication(referentielMapper.fromComplexeTypeList(documentModel
				.getPropertyValue(ElasticDossier.TP_CHARGE_MISSION_COMMUNICATION)));
		exportableDossier.setTpCheminCroix((Boolean) documentModel.getPropertyValue(ElasticDossier.TP_CHEMIN_CROIX));
		exportableDossier.setTpCommentaireReference((String) documentModel
				.getPropertyValue(ElasticDossier.TP_COMMENTAIRE_REFERENCE));
		exportableDossier.setTpDateArrivePapier(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.TP_DATE_ARRIVE_PAPIER)));
		exportableDossier.setTpDateRetour(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.TP_DATE_RETOUR)));
		exportableDossier.setTpEpreuve(referentielMapper.fromComplexeType(documentModel
				.getPropertyValue(ElasticDossier.TP_EPREUVE)));

		exportableDossier.setTpMotifRetour((String) documentModel.getPropertyValue(ElasticDossier.TP_MOTIF_RETOUR));
		exportableDossier.setTpNomsSignatairesCommunication((String) documentModel
				.getPropertyValue(ElasticDossier.TP_NOMS_SIGNATAIRES_COMMUNICATION));
		exportableDossier.setTpNomsSignatairesRetour((String) documentModel
				.getPropertyValue(ElasticDossier.TP_NOMS_SIGNATAIRES_RETOUR));
		exportableDossier.setTpNouvelleDemandeEpreuves(referentielMapper.fromComplexeType(documentModel
				.getPropertyValue(ElasticDossier.TP_NOUVELLE_DEMANDE_EPREUVES)));
		exportableDossier.setTpNumerosListeSignatureField(referentielMapper.fromComplexeTypeList(documentModel
				.getPropertyValue(ElasticDossier.TP_NUMEROS_LISTE_SIGNATURE_FIELD)));
		exportableDossier
				.setTpPapierArchive((Boolean) documentModel.getPropertyValue(ElasticDossier.TP_PAPIER_ARCHIVE));
		exportableDossier.setTpPersonnesNommees((String) documentModel
				.getPropertyValue(ElasticDossier.TP_PERSONNES_NOMMEES));
		exportableDossier.setTpPriorite((String) documentModel.getPropertyValue(ElasticDossier.TP_PRIORITE));
		exportableDossier.setTpPublicationDate(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.TP_PUBLICATION_DATE)));
		exportableDossier.setTpPublicationDateDemande(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.TP_PUBLICATION_DATE_DEMANDE)));
		exportableDossier.setTpPublicationDateEnvoiJo(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.TP_PUBLICATION_DATE_ENVOI_JO)));
		exportableDossier.setTpPublicationDelai((String) documentModel
				.getPropertyValue(ElasticDossier.TP_PUBLICATION_DELAI));
		exportableDossier.setTpPublicationEpreuveEnRetour((Boolean) documentModel
				.getPropertyValue(ElasticDossier.TP_PUBLICATION_EPREUVE_EN_RETOUR));
		exportableDossier.setTpPublicationModePublication((String) documentModel
				.getPropertyValue(ElasticDossier.TP_PUBLICATION_MODE_PUBLICATION));
		exportableDossier.setTpPublicationNomPublication((String) documentModel
				.getPropertyValue(ElasticDossier.TP_PUBLICATION_NOM_PUBLICATION));
		exportableDossier.setTpPublicationNumeroListe((String) documentModel
				.getPropertyValue(ElasticDossier.TP_PUBLICATION_NUMERO_LISTE));
		exportableDossier.setTpRetourA((String) documentModel.getPropertyValue(ElasticDossier.TP_RETOUR_A));
		exportableDossier.setTpRetourDuBonaTitrerLe(ElasticUtils.toDate((Calendar) documentModel
				.getPropertyValue(ElasticDossier.TP_RETOUR_DU_BONA_TITRER_LE)));
		exportableDossier.setTpSignataire((String) documentModel.getPropertyValue(ElasticDossier.TP_SIGNATAIRE));
		exportableDossier.setTpSignatureDestinataireCPM((String) documentModel
				.getPropertyValue(ElasticDossier.TP_SIGNATURE_DESTINATAIRE_CPM));
		exportableDossier.setTpSignatureDestinataireSGG((String) documentModel
				.getPropertyValue(ElasticDossier.TP_SIGNATURE_DESTINATAIRE_SGG));

		exportableDossier.setTpSignaturePm(referentielMapper.fromComplexeType(documentModel.getPropertyValue(ElasticDossier.TP_SIGNATURE_PM)));
		exportableDossier.setTpSignaturePr(referentielMapper.fromComplexeType(documentModel.getPropertyValue(ElasticDossier.TP_SIGNATURE_PR)));
		exportableDossier.setTpSignatureSgg(referentielMapper.fromComplexeType(documentModel.getPropertyValue(ElasticDossier.TP_SIGNATURE_SGG)));
		exportableDossier
				.setTpTexteAPublier((Boolean) documentModel.getPropertyValue(ElasticDossier.TP_TEXTE_APUBLIER));
		exportableDossier.setTpTexteSoumisAValidation((Boolean) documentModel
				.getPropertyValue(ElasticDossier.TP_TEXTE_SOUMIS_AVALIDATION));

		// droits
		exportableDossier.setPerms(getDossierPermsAsString(documentModel));

		return exportableDossier;
	}

	private Collection<String> getDossierPermsAsString(DocumentModel dossier) throws ClientException {

		// avant de me taper, y a pas de constant pour ça dans le code
		final String acpPostePrefix = "mailbox_poste-";
		final String acpDirectionPrefix = "dossier_dist_dir-";
		final String acpMinisterePrefix = "dossier_dist_min-";
		final String acpMinistereRattachPrefix = "dossier_rattach_min-";
		final String acpDirRattachPrefix = "dossier_rattach_dir-";
		final String acpUserPrefix = "mailbox_user-";

		Collection<String> permsToReturn = new ArrayList<String>();

		ACL perms = dossier.getACP().getMergedACLs("mailboxes");
		for (ACE currAce : perms) {
			permsToReturn.add(currAce.getUsername().replace(acpPostePrefix, "").replace(acpDirectionPrefix, "")
					.replace(acpMinisterePrefix, "").replace(acpUserPrefix, "").replace(acpDirRattachPrefix, "")
					.replace(acpMinistereRattachPrefix, ""));
		}
		if (!permsToReturn.contains(SolonEpgConstant.ES_DROITS_ADMINISTRATORS)) {
			permsToReturn.add(SolonEpgConstant.ES_DROITS_ADMINISTRATORS);
		}

		return permsToReturn;
	}
}
