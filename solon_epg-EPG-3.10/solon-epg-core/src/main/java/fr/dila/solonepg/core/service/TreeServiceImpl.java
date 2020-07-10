package fr.dila.solonepg.core.service;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.Path;
import org.nuxeo.common.utils.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.VersioningOption;
import org.nuxeo.ecm.core.schema.FacetNames;
import org.nuxeo.ecm.core.versioning.VersioningService;
import org.nuxeo.runtime.api.Framework;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierInstance;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.api.service.TreeService;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.ss.core.service.SSTreeServiceImpl;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.constant.STLifeCycleConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Implémentation de l'Interface générique pour gérer les arborescences de document.
 * 
 * @author ARN
 */
public class TreeServiceImpl extends SSTreeServiceImpl implements TreeService {

	private static final long		serialVersionUID	= -7158091610115314307L;

	private static final Log		log					= LogFactory.getLog(TreeServiceImpl.class);
	private static final STLogger	LOGGER				= STLogFactory.getLog(TreeServiceImpl.class);

	// /////////////////
	// methode concernant l'ajout et la suppression de fichier
	// ////////////////

	@Override
	public DocumentModel createOrUpdateFileInFolder(final CoreSession session, final DocumentModel dossierParentDoc,
			final DocumentModel fileDoc, final String fileName, final Blob content, final String documentType,
			final NuxeoPrincipal currentUser) throws ClientException {
		// on vérifie que le type de document est valide ou null
		DocumentModel docModel = null;
		if (documentType != null) {
			// La vérification du type parapheur ou fdd n'est pas à faire ici
			if (!documentType.equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE)
					&& !documentType.equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE)
					&& !documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)
					&& !documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE)) {
				throw new RuntimeException("type du documentModel inconnu !");
			}
		}

		String documentTypeFichier = documentType;
		if (documentTypeFichier != null) {
			// redéfinition du type à créer
			// Pas à faire ici non plus
			if (documentTypeFichier.equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE)) {
				documentTypeFichier = SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE;
			} else if (documentTypeFichier.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)) {
				documentTypeFichier = SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE;
			}
		}

		if (fileDoc.hasFacet(FacetNames.FOLDERISH)) {
			// si le document est un répertoire, on créé le document
			docModel = createFileInFolder(fileDoc, fileName, content, session, documentTypeFichier, currentUser,
					dossierParentDoc);
		} else if (fileDoc.hasFacet(FacetNames.VERSIONABLE)) {
			// si le document est de type versionable (fichier) , on le met jour et on versionne
			updateFile(session, fileDoc, fileName, content, currentUser, dossierParentDoc);
		} else {
			throw new RuntimeException("le document n'est ni un répertoire ni un fichier !");
		}

		return docModel;
	}

	@Override
	public DocumentModel createOrUpdateFileInFolder(final String documentId, final String fileName, final Blob blob,
			final CoreSession session, final String documentType, final NuxeoPrincipal currentUser,
			final DocumentModel dossierDocument) throws ClientException {
		// récupération du document
		final DocumentModel document = session.getDocument(new IdRef(documentId));
		if (document == null) {
			throw new ClientException("document introuvable");
		}

		return createOrUpdateFileInFolder(session, dossierDocument, document, fileName, blob, documentType, currentUser);
	}

	@Override
	public String getFormatsAutorises(final DocumentModel currentDossier, final DocumentModel repositoryOrFileSelected,
			final CoreSession session, final String documentType) throws ClientException {
		String formatsAutorises = null;
		// récupération des formats autorisés selon le type de document
		if (documentType == null) {
			log.debug("type de document vide pour methode format autorise");
		} else if (documentType.equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE)
				|| documentType.equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE)) {
			// dans le cas du parapheur, il faut récupérer les formats autorisés sur le répertoire courant
			formatsAutorises = getFormatsAutorisesParapheur(repositoryOrFileSelected, session);
		} else if (documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE)
				|| documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)) {
			// récupération des formats autorisés sur le Fond de Dossier
			formatsAutorises = getFormatsAutorisesFondDossier(currentDossier, session);
		}
		return formatsAutorises;
	}

	/**
	 * récupération des types foramts autorisés pour le parapheur.
	 * 
	 * @param repositoryOrFileSelected
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	private String getFormatsAutorisesParapheur(DocumentModel repositoryOrFileSelected, final CoreSession session)
			throws ClientException {
		String formatsAutorises = null;
		// dans le cas du parapheur, il faut récupérer les formats autorisés sur le répertoire courant
		if (repositoryOrFileSelected == null) {
			log.debug(" repertoire ou fichier vide pour methode format autorise");
		} else {
			if (repositoryOrFileSelected.hasFacet(FacetNames.VERSIONABLE)
					|| SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE.equals(repositoryOrFileSelected
							.getType())) {
				// récupération du répertoire parent du fichier
				repositoryOrFileSelected = session.getParentDocument(repositoryOrFileSelected.getRef());
			}
			// récupération des formats autorisés pour le répertoire
			final ParapheurFolder parapheurFolder = repositoryOrFileSelected.getAdapter(ParapheurFolder.class);
			final List<String> formatsAutorisesList = parapheurFolder.getFormatsAutorises();
			formatsAutorises = StringUtils.join(formatsAutorisesList, ",");
		}
		return formatsAutorises;
	}

	/**
	 * récupération des types formats autorisés pour le fond de dossier.
	 * 
	 * @param repositoryOrFileSelected
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	private String getFormatsAutorisesFondDossier(final DocumentModel currentDossier, final CoreSession session)
			throws ClientException {
		String formatsAutorises = null;
		// dans le cas du parapheur, il faut récupérer les formats autorisés sur le répertoire courant
		if (currentDossier == null) {
			log.debug(" repertoire ou fichier vide pour methode format autorise");
		} else {

			// récupération du fond de dossier
			final Dossier dossier = currentDossier.getAdapter(Dossier.class);
			final FondDeDossierInstance fondDossierDoc = dossier.getFondDeDossier(session);

			// récupératoin formats autorisés
			final List<String> formatsAutorisesList = fondDossierDoc.getFormatsAutorises();
			formatsAutorises = StringUtils.join(formatsAutorisesList, ",");
		}
		return formatsAutorises;
	}

	@Override
	public DocumentModel createFileInFolder(final DocumentModel repertoireParent, final String fileName,
			final Blob blob, final CoreSession session, final String documentType, final NuxeoPrincipal currentUser,
			DocumentModel dossierDocument) throws ClientException {
		if (documentType == null) {
			throw new RuntimeException("pas de type de document spécifié !");
		}
		// on récupère le nom du fichier
		final String docName = fileName;
		DocumentModel docModel = null;
		// création du DocumentModel
		try {
			docModel = session.createDocumentModel(repertoireParent.getPathAsString(), docName, documentType);
			// set document name
			DublincoreSchemaUtils.setTitle(docModel, docName);
			// set document file properties
			docModel.setProperty(STSchemaConstant.FILE_SCHEMA, STSchemaConstant.FILE_FILENAME_PROPERTY, fileName);
			docModel.setProperty(STSchemaConstant.FILE_SCHEMA, STSchemaConstant.FILE_CONTENT_PROPERTY, blob);
			// set entite
			docModel.setProperty(DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA,
					DossierSolonEpgConstants.FILE_SOLON_EPG_ENTITE_PROPERTY, getEntite(currentUser));
			// creation du document en session
			docModel = session.createDocument(docModel);
			// sauvegarde du document en session pour avoir une version du document
			// incrementation du numero de version
			docModel.putContextData(VersioningService.VERSIONING_OPTION, VersioningOption.MAJOR);
			docModel = session.saveDocument(docModel);
			// RG-DOS-TRT-29 : si on ajoute un fichier dans le répertoire "extrait" du parapheur, on met à jour le champ
			// "Publication intégrale ou par extrait" du bordereau
			if (documentType.equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE)
					&& repertoireParent.getTitle().equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EXTRAIT_NAME)) {
				final Dossier dossier = dossierDocument.getAdapter(Dossier.class);
				dossier.setPublicationIntegraleOuExtrait(DossierSolonEpgConstants.DOSSIER_EXTRAIT_PROPERTY_VALUE);
				dossierDocument = dossier.getDocument();
				session.saveDocument(dossierDocument);
			}
			// commit creation
			session.save();
			// journalisation de l'action dans les logs
			journalisationAction(SolonEpgEventConstant.ACTION_CREATE, docModel.getType(), session, dossierDocument,
					fileName, repertoireParent);
			// dossier : on vérifie si le parapheur est complet et on met à jour le numéro de version du dossier
			if (documentType.equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE)) {
				final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
				parapheurService.checkParapheurComplet(dossierDocument, session);
				updateDossierNumeroVersion(repertoireParent, docModel, dossierDocument, session, false);
			}
		} catch (final ClientException e) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_FILE_TEC, e);
			throw new RuntimeException("erreur lors de la création du fichier", e);
		}
		return docModel;
	}

	/**
	 * Met à jour le numéro de version du dossier.
	 * 
	 * @param parent
	 *            répertoire contenant le fichier
	 * @param fichier
	 *            fichier
	 * @param dossier
	 *            dossier
	 * @param session
	 *            session
	 * @param fichierToDelete
	 *            vrai si le fichier va être supprimé
	 * @throws ClientException
	 */
	private void updateDossierNumeroVersion(final DocumentModel repertoireParent, final DocumentModel fichier,
			final DocumentModel dossierDoc, final CoreSession session, final boolean fichierToDelete)
			throws ClientException {
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		final String repParentName = repertoireParent.getTitle();
		final boolean acteReference = dossier.getIsActeReferenceForNumeroVersion();
		// test si l'on doit mettre à jour le numéro de version
		if (!acteReference && repParentName.equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EXTRAIT_NAME)
				|| acteReference && repParentName.equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME)) {
			Long numVersion = 0L;
			if (!fichierToDelete) {
				// récupération version
				numVersion = (Long) fichier.getProperty(STSchemaConstant.UID_SCHEMA,
						STSchemaConstant.UID_MAJOR_VERSION_PROPERTY);
				if (numVersion == null || numVersion == 0L) {
					numVersion = 1L;
				}
			}
			dossier.setNumeroVersionActeOuExtrait(numVersion);
			dossier.save(session);
		}
	}

	@Override
	public void updateFile(final CoreSession session, DocumentModel fichier, final String fileName, final Blob blob,
			final NuxeoPrincipal currentUser, final DocumentModel dossierDocument) throws ClientException {

		// force unlock liveEdit
		STServiceLocator.getSTLockService().unlockDocUnrestricted(session, fichier);

		// on récupère le nom du fichier
		final String docName = fileName;
		// set document name
		DublincoreSchemaUtils.setTitle(fichier, docName);
		// set document file properties
		fichier.setProperty(STSchemaConstant.FILE_SCHEMA, STSchemaConstant.FILE_FILENAME_PROPERTY, fileName);
		fichier.setProperty(STSchemaConstant.FILE_SCHEMA, STSchemaConstant.FILE_CONTENT_PROPERTY, blob);
		// set entite
		fichier.setProperty(DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA,
				DossierSolonEpgConstants.FILE_SOLON_EPG_ENTITE_PROPERTY, getEntite(currentUser));
		// incrementation du numero de version
		fichier.putContextData(VersioningService.VERSIONING_OPTION, VersioningOption.MAJOR);
		// create document in session
		fichier = session.saveDocument(fichier);
		// commit modification
		session.save();
		// récupération du répertoire parent
		final DocumentModel repertoireParent = session.getParentDocument(fichier.getRef());
		// journalisation de l'action dans les logs
		journalisationAction(SolonEpgEventConstant.ACTION_UPDATE, fichier.getType(), session, dossierDocument,
				fileName, repertoireParent);
		// mise à jour du numéro de version du dossier
		updateDossierNumeroVersion(repertoireParent, fichier, dossierDocument, session, false);
	}

	/**
	 * Journalise l'action de création/modification de fichier.
	 * 
	 * @param actionType
	 * @param documentType
	 * @param session
	 * @param dossierDocument
	 * @param docName
	 * @param repertoireParent
	 * @throws ClientException
	 */
	private void journalisationAction(final String actionType, final String documentType, final CoreSession session,
			final DocumentModel dossierDocument, final String docName, final DocumentModel repertoireParent,
			final String typeActe) throws ClientException {
		if (org.apache.commons.lang.StringUtils.isNotEmpty(typeActe)) {
			// log des actions d'administration (modèles de fond de dossier et de parapheur)
			if (documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_FOLDER_DOCUMENT_TYPE)) {
				// log pour les modèles de répertoire du fond de dossier
				if (actionType.equals(SolonEpgEventConstant.ACTION_CREATE)) {
					journalisationAdministrationAction(session, SolonEpgEventConstant.CREATE_MODELE_FDD_EVENT, docName,
							typeActe);
				} else if (actionType.equals(SolonEpgEventConstant.ACTION_UPDATE)) {
					journalisationAdministrationAction(session, SolonEpgEventConstant.UPDATE_MODELE_FDD_EVENT, docName,
							typeActe);
				} else if (actionType.equals(SolonEpgEventConstant.ACTION_DELETE)) {
					journalisationAdministrationAction(session, SolonEpgEventConstant.DELETE_MODELE_FDD_EVENT, docName,
							typeActe);
				}
			} else if (documentType.equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE)) {
				// log pour les modèles de répertoire du fond de dossier
				if (actionType.equals(SolonEpgEventConstant.ACTION_UPDATE)) {
					journalisationAdministrationAction(session, SolonEpgEventConstant.UPDATE_MODELE_PARAPHEUR_EVENT,
							docName, typeActe);
				}
			} else if (documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)) {
				// log pour les modèles de répertoire du fond de dossier
				if (actionType.equals(SolonEpgEventConstant.ACTION_DELETE)) {
					journalisationAdministrationAction(session, SolonEpgEventConstant.DELETE_MODELE_FDD_EVENT, docName,
							typeActe);
				}
			}

			return;
		}

		String path = "";
		String repertoireParentTitle = "";
		if (repertoireParent != null) {
			path = repertoireParent.getPathAsString();
			repertoireParentTitle = repertoireParent.getTitle();
		}

		String eventCategory = STEventConstant.CATEGORY_PARAPHEUR;

		if (path.contains(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG)) {
			eventCategory = SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG;
		} else if (path.contains(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR)) {
			eventCategory = SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR;
		} else if (path.contains(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG)) {
			eventCategory = SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG;
		} else if (path.contains(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER)) {
			eventCategory = SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER;
		}

		// journalisation de l'action dans les logs en fonction du type d'action et du type de document
		if (documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE)) {
			// log pour les fichier du fond de dossier
			if (actionType.equals(SolonEpgEventConstant.ACTION_CREATE)) {
				journalisationAction(session, dossierDocument, SolonEpgEventConstant.CREATE_FILE_FDD,
						SolonEpgEventConstant.COMMENT_CREATE_FILE_FDD + docName + "' dans le répertoire '"
								+ repertoireParentTitle + "'", eventCategory);
			} else if (actionType.equals(SolonEpgEventConstant.ACTION_UPDATE)) {
				journalisationAction(session, dossierDocument, SolonEpgEventConstant.UPDATE_FILE_FDD,
						SolonEpgEventConstant.COMMENT_UPDATE_FILE_FDD + docName + "' dans le répertoire '"
								+ repertoireParentTitle + "'", eventCategory);
			} else if (actionType.equals(SolonEpgEventConstant.ACTION_DELETE)) {
				journalisationAction(session, dossierDocument, SolonEpgEventConstant.DELETE_FILE_FDD,
						SolonEpgEventConstant.COMMENT_DELETE_FILE_FDD + docName + "' dans le répertoire '"
								+ repertoireParentTitle + "'", eventCategory);
			}
		} else if (documentType.equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE)) {

			// log pour les fichier du parapheur
			if (actionType.equals(SolonEpgEventConstant.ACTION_CREATE)) {
				// RG-DOS-TRT-29
				journalisationAction(session, dossierDocument, SolonEpgEventConstant.CREATE_FILE_PARAPHEUR,
						SolonEpgEventConstant.COMMENT_CREATE_FILE_PARAPHEUR + docName + "' dans le répertoire '"
								+ repertoireParentTitle + "'", eventCategory);
			} else if (actionType.equals(SolonEpgEventConstant.ACTION_UPDATE)) {
				journalisationAction(session, dossierDocument, SolonEpgEventConstant.CREATE_FILE_PARAPHEUR,
						SolonEpgEventConstant.COMMENT_UPDATE_FILE_PARAPHEUR + docName + "' dans le répertoire '"
								+ repertoireParentTitle + "'", eventCategory);
			} else if (actionType.equals(SolonEpgEventConstant.ACTION_DELETE)) {
				journalisationAction(session, dossierDocument, SolonEpgEventConstant.DELETE_FILE_PARAPHEUR,
						SolonEpgEventConstant.COMMENT_DELETE_FILE_PARAPHEUR + docName + "' dans le répertoire '"
								+ repertoireParentTitle + "'", eventCategory);
			}
		} else if (documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)) {
			// log pour les répertoire du fond de dossier
			if (actionType.equals(SolonEpgEventConstant.ACTION_CREATE)) {
				journalisationAction(session, dossierDocument, SolonEpgEventConstant.CREATE_FOLDER_FDD,
						SolonEpgEventConstant.COMMENT_CREATE_FOLDER_FDD, eventCategory);
			} else if (actionType.equals(SolonEpgEventConstant.ACTION_UPDATE)) {
				journalisationAction(session, dossierDocument, SolonEpgEventConstant.UPDATE_FOLDER_FDD,
						SolonEpgEventConstant.COMMENT_UPDATE_FOLDER_FDD + docName, eventCategory);
			} else if (actionType.equals(SolonEpgEventConstant.ACTION_DELETE)) {
				journalisationAction(session, dossierDocument, SolonEpgEventConstant.DELETE_FOLDER_FDD,
						SolonEpgEventConstant.COMMENT_DELETE_FOLDER_FDD + docName, eventCategory);
			}
		}
	}

	private void journalisationAction(final String actionType, final String documentType, final CoreSession session,
			final DocumentModel dossierDocument, final String docName, final DocumentModel repertoireParent)
			throws ClientException {
		journalisationAction(actionType, documentType, session, dossierDocument, docName, repertoireParent, null);
	}

	/**
	 * Journalise un evenement d'un dossier
	 * 
	 * @param session
	 * @param docModel
	 * @param eventName
	 * @param commentaire
	 * @param categorie
	 * @throws ClientException
	 */
	private void journalisationAction(final CoreSession session, final DocumentModel docModel, final String eventName,
			final String commentaire, final String categorie) throws ClientException {
		final JournalService journalService = STServiceLocator.getJournalService();
		journalService.journaliserAction(session, docModel, eventName, commentaire, categorie);
	}

	/**
	 * Journalise un evenement de l'espace d'administration (événement des modèles de fond de dossier et de parapheur)
	 * 
	 * @param session
	 * @param eventName
	 * @param commentaire
	 * @param categorie
	 * @throws ClientException
	 */
	private void journalisationAdministrationAction(final CoreSession session, final String eventName,
			final String docName, final String typeActe) throws ClientException {
		final JournalService journalService = STServiceLocator.getJournalService();

		StringBuilder commentaire = new StringBuilder();
		if (SolonEpgEventConstant.CREATE_MODELE_FDD_EVENT.equals(eventName)) {
			commentaire.append("Création d'un nouveau répertoire dans les modèles de fond de dossier '");
		} else if (SolonEpgEventConstant.UPDATE_MODELE_FDD_EVENT.equals(eventName)) {
			commentaire.append("Mise à jour d'un répertoire dans les modèles de fond de dossier '");
		} else if (SolonEpgEventConstant.DELETE_MODELE_FDD_EVENT.equals(eventName)) {
			commentaire.append("Suppression du répertoire dans les modèles de fond de dossier '");
		} else if (SolonEpgEventConstant.UPDATE_MODELE_PARAPHEUR_EVENT.equals(eventName)) {
			commentaire.append("Mise à jour d'un répertoire dans les modèles de parapheur '");
		}
		if (!SolonEpgEventConstant.CREATE_MODELE_FDD_EVENT.equals(eventName)) {
			commentaire.append(docName).append("'");
		}
		commentaire.append(" pour le type d'acte ").append(typeActe);
		journalService.journaliserActionAdministration(session, eventName, commentaire.toString());
	}

	/**
	 * Log technique d'une action de suppression de document
	 */
	private void logDeleteAction(final String documentType, final CoreSession session,
			final DocumentModel dossierDocument) {
		if (documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_FOLDER_DOCUMENT_TYPE)) {
			// log pour les modèles de répertoire du fond de dossier
			LOGGER.info(session, EpgLogEnumImpl.DEL_MOD_REP_FDD_TEC, dossierDocument);
		} else if (documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)) {
			// log pour les répertoires du fond de dossier
			LOGGER.info(session, EpgLogEnumImpl.DEL_REP_FDD_TEC, dossierDocument);
		} else if (documentType.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE)) {
			// log pour les fichier du fond de dossier
			LOGGER.info(session, SSLogEnumImpl.DEL_FILE_FDD_TEC, dossierDocument);
		} else if (documentType.equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE)) {
			// log pour les fichier du parapheur
			LOGGER.info(session, SSLogEnumImpl.DEL_PARAPHEUR_FILE_TEC, dossierDocument);
		}
	}

	// /////////////////
	// methodes concernant l'ajout et la suppression de répertoire
	// ////////////////

	@Override
	public void createNewDefaultFolderInTree(final DocumentModel documentParent, final CoreSession session,
			final String documentType, final DocumentModel referenceDoc, final String typeActe) throws ClientException {
		createAndInitDocument(documentParent, session, documentType);
		// journalisation de l'action dans les logs
		journalisationAction(SolonEpgEventConstant.ACTION_CREATE, documentType, session, referenceDoc, null,
				documentParent, typeActe);
		// commit de la creation
		session.save();
	}

	@Override
	public void createNewDefaultFolderInTree(final DocumentModel documentParent, final CoreSession session,
			final String documentType, final DocumentModel referenceDoc) throws ClientException {
		createNewDefaultFolderInTree(documentParent, session, documentType, referenceDoc, null);
	}

	@Override
	public void createNewFolderNodeBefore(final DocumentModel documentCourant, final CoreSession session,
			final String documentType, final DocumentModel referenceDoc, final String typeActe) throws ClientException {
		// on récupère le document parent à l'aide de sa référence
		final DocumentModel documentParent = session.getDocument(documentCourant.getParentRef());
		// on cree le nouveau repertoire
		final DocumentModel newDocument = createAndInitDocument(documentParent, session, documentType);
		// on place le document avant le document Courant
		session.orderBefore(documentParent.getRef(), newDocument.getName(), documentCourant.getName());
		// journalisation de l'action dans les logs
		journalisationAction(SolonEpgEventConstant.ACTION_CREATE, documentType, session, referenceDoc, null, null,
				typeActe);
		// commit de la creation
		session.save();
	}

	@Override
	public void createNewFolderNodeBefore(final DocumentModel documentCourant, final CoreSession session,
			final String documentType, final DocumentModel referenceDoc) throws ClientException {
		createNewFolderNodeBefore(documentCourant, session, documentType, referenceDoc, null);
	}

	@Override
	public void createNewFolderNodeAfter(final DocumentModel documentCourant, final CoreSession session,
			final String documentType, final DocumentModel referenceDoc, final String typeActe) throws ClientException {
		// on récupère le document parent à l'aide de sa référence
		final DocumentModel documentParent = session.getDocument(documentCourant.getParentRef());
		// on récupère la liste ordonnées par défaut des document fils du parent
		final DocumentModelList orderedChildren = session.getChildren(documentCourant.getParentRef());
		// on récupère l'index du document courant
		final int selectedDocumentIndex = orderedChildren.indexOf(documentCourant);
		// on récupère l'index suivant si il en existe un et on réucpère son nom
		String nomDocumentSource = null;
		final int nextIndex = selectedDocumentIndex + 1;
		if (nextIndex < orderedChildren.size()) {
			nomDocumentSource = orderedChildren.get(nextIndex).getName();
		}
		// on cree le nouveau repertoire
		final DocumentModel newDocument = createAndInitDocument(documentParent, session, documentType);
		// on place le document avant l'élement suivant du document courant : si le document courant est le dernier
		// document de la lsite on place le nouveau document en fin de liste
		session.orderBefore(documentParent.getRef(), newDocument.getName(), nomDocumentSource);
		// journalisation de l'action dans les logs
		journalisationAction(SolonEpgEventConstant.ACTION_CREATE, documentType, session, referenceDoc, null, null,
				typeActe);
		// commit de la creation
		session.save();
	}

	@Override
	public void createNewFolderNodeAfter(final DocumentModel documentCourant, final CoreSession session,
			final String documentType, final DocumentModel referenceDoc) throws ClientException {
		createNewFolderNodeAfter(documentCourant, session, documentType, referenceDoc, null);
	}

	@Override
	public DocumentModel updateFolder(final DocumentModel documentCourant, final CoreSession session,
			final String typeActe) throws ClientException {
		// journalisation de l'action dans les logs
		journalisationAction(SolonEpgEventConstant.ACTION_UPDATE, documentCourant.getType(), session, null,
				documentCourant.getTitle(), null, typeActe);
		// on sauvegarde le document et on enregistre
		return saveFolder(documentCourant, session);
	}

	@Override
	public DocumentModel updateFolder(final DocumentModel documentCourant, final CoreSession session)
			throws ClientException {
		return updateFolder(documentCourant, session, null);
	}

	private DocumentModel saveFolder(DocumentModel documentCourant, final CoreSession session) throws ClientException {
		// on sauvegarde le document et on enregistre
		documentCourant = session.saveDocument(documentCourant);
		return documentCourant;
	}

	@Override
	public DocumentModel renameFolder(final DocumentModel documentCourant, final CoreSession session,
			final String newTitle, final DocumentModel referenceDoc) throws ClientException {
		// on modifie le nom du document
		DublincoreSchemaUtils.setTitle(documentCourant, newTitle);
		// on sauvegarde le document et on enregistre
		return updateFolder(documentCourant, session);
	}

	@Override
	public void deleteDocument(final DocumentModel documentCourant, final CoreSession session,
			final DocumentModel referenceDoc, final String typeActe) throws ClientException {

		// note : la regle de gestion concernant l'interdiction de supprimer un répertoire si cela rend l'arborescence
		// vide est gérée dans le bean d'affichage
		DocumentModel repertoireParent = null;
		final String typeDoc = documentCourant.getType();
		if (typeDoc.equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE)
				|| typeDoc.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE)) {
			repertoireParent = session.getDocument(documentCourant.getParentRef());
		}

		// journalisation de l'action dans les logs
		journalisationAction(SolonEpgEventConstant.ACTION_DELETE, typeDoc, session, referenceDoc,
				documentCourant.getTitle(), repertoireParent, typeActe);
		logDeleteAction(typeDoc, session, documentCourant);
		if (repertoireParent == null) {
			// Pas de répertoire parent => on n'est pas en train de supprimer un fichier
			// suppression du document et de ses fils
			session.removeDocument(documentCourant.getRef());
			// commit de la creation
			session.save();
		} else {
			// Soft delete vrai par défaut
			if ("true".equals(Framework.getProperty("solonepg.file.soft.delete", "true"))) {
				try {
					session.followTransition(documentCourant.getRef(), STLifeCycleConstant.TO_DELETE_TRANSITION);
					session.save();
				} catch (ClientException ce) {
					if (typeDoc.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE)) {
						LOGGER.error(session, SSLogEnumImpl.FAIL_UPDATE_FDD_TEC, documentCourant, ce);
					} else if (typeDoc.equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE)) {
						LOGGER.error(session, SSLogEnumImpl.FAIL_UPDATE_PARAPHEUR_FILE_TEC, documentCourant, ce);
					}
				}
			} else {
				try {
					if (typeDoc.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE)) {
						LOGGER.info(session, SSLogEnumImpl.DEL_FILE_FDD_TEC, documentCourant);
					} else if (typeDoc.equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE)) {
						LOGGER.info(session, SSLogEnumImpl.DEL_PARAPHEUR_FILE_TEC, documentCourant);
					}
					session.removeDocument(documentCourant.getRef());
					session.save();
				} catch (ClientException ce) {
					if (typeDoc.equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE)) {
						LOGGER.error(session, SSLogEnumImpl.FAIL_DEL_FILE_FDD_TEC, documentCourant, ce);
					} else if (typeDoc.equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE)) {
						LOGGER.error(session, SSLogEnumImpl.FAIL_DEL_PARAPHEUR_FILE_TEC, documentCourant, ce);
					}
				}
			}
		}

		if (typeDoc.equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE)) {
			// dossier : on vérifie si le parapheur est complet après la suppression du fichier
			final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
			parapheurService.checkParapheurComplet(referenceDoc, session);
			// on réinitialise le numéro de version du dossier
			updateDossierNumeroVersion(repertoireParent, documentCourant, referenceDoc, session, true);
		}
	}

	@Override
	public void deleteDocument(final DocumentModel documentCourant, final CoreSession session,
			final DocumentModel referenceDoc) throws ClientException {
		deleteDocument(documentCourant, session, referenceDoc, null);
	}

	/**
	 * créé et initialise un document du type choisi.
	 * 
	 * @author antoine Rolin
	 * 
	 * @param
	 * 
	 * @return DocumentModel
	 */
	private DocumentModel createAndInitDocument(final DocumentModel documentParent, final CoreSession session,
			final String documentType) {
		DocumentModel newDocumentModel = null;
		if (documentType != null) {
			if (SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_TYPE.equals(documentType)) {
				// on cree le nouveau repertoire
				newDocumentModel = createParapheurModelFolderElement(documentParent, session,
						SolonEpgParapheurConstants.DEFAULT_PARAPHEUR_FOLDER_NAME, true, null, null, null);
			} else if (SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE.equals(documentType)) {
				// on cree le nouveau repertoire
				newDocumentModel = createFondDossierFolderElement(documentParent, session,
						SolonEpgFondDeDossierConstants.DEFAULT_FDD_FOLDER_NAME);
			} else if (SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_FOLDER_DOCUMENT_TYPE.equals(documentType)) {
				// on cree le nouveau repertoire
				newDocumentModel = createFondDossierModelFolderElement(documentParent, session,
						SolonEpgFondDeDossierConstants.DEFAULT_FDD_FOLDER_NAME);
			}
		}
		return newDocumentModel;
	}

	// methode private liée au document
	/**
	 * création des répertoires par defaut du modele du parapheur
	 * 
	 * @author antoine Rolin
	 * 
	 * @param
	 * 
	 * @return DocumentModel
	 */
	private DocumentModel createParapheurModelFolderElement(final DocumentModel documentRacine,
			final CoreSession session, final String name, final Boolean estVide, final Long nbMaxDoc,
			final List<File> feuilleStyleFiles, final List<String> formatAutoriseDoc) {
		try {
			// titre du document dans le classement nuxeo
			final String titre = name;
			// create document model
			DocumentModel parapheurModelFolder = session.createDocumentModel(documentRacine.getPathAsString(), titre,
					SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_TYPE);
			// on définit le titre du document
			DublincoreSchemaUtils.setTitle(parapheurModelFolder, name);

			final ParapheurFolder parapeurModelFolderDoc = parapheurModelFolder.getAdapter(ParapheurFolder.class);
			// récupération des propriétés du répertoire
			parapeurModelFolderDoc.setEstNonVide(estVide);
			parapeurModelFolderDoc.setNbDocAccepteMax(nbMaxDoc);
			// parapeurModelFolderDoc.setFeuilleStyleFiles(feuilleStyleFiles);
			parapeurModelFolderDoc.setFormatsAutorises(formatAutoriseDoc);

			// create document in session
			parapheurModelFolder = session.createDocument(parapeurModelFolderDoc.getDocument());
			return parapheurModelFolder;
		} catch (final ClientException e) {
			throw new RuntimeException(" erreur lors de la creation d'un modele de répertoire du parapheur ", e);
		}
	}

	/**
	 * création des répertoires par defaut du modele du fond de dossier
	 * 
	 * @author antoine Rolin
	 * 
	 * @param
	 * 
	 * @return DocumentModel
	 */
	@Override
	public DocumentModel createFondDossierModelFolderElement(final DocumentModel documentRacine,
			final CoreSession session, final String name) {
		try {
			// titre du document dans le classement nuxeo
			final String titre = name;
			// create document model
			DocumentModel fddModelFolder = session.createDocumentModel(documentRacine.getPathAsString(), titre,
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE);
			// on définit le titre du document
			DublincoreSchemaUtils.setTitle(fddModelFolder, name);
			// on définit le répertoire comme supprimable
			fddModelFolder.setProperty(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE,
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_IS_SUPPRIMABLE_PROPERTY, true);
			// create document in session
			fddModelFolder = session.createDocument(fddModelFolder);
			return fddModelFolder;
		} catch (final ClientException e) {
			throw new RuntimeException(" erreur lors de la creation d'un modele de répertoire fond de dossier ", e);
		}
	}

	/**
	 * création des répertoires par defaut du fond de dossier
	 * 
	 * @author antoine Rolin
	 * 
	 * @param
	 * 
	 * @return DocumentModel
	 */
	private DocumentModel createFondDossierFolderElement(final DocumentModel documentRacine, final CoreSession session,
			final String name) {
		try {
			// titre du document dans le classement nuxeo
			final String titre = name;
			// create document model
			DocumentModel fddModelFolder = session.createDocumentModel(documentRacine.getPathAsString(), titre,
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE);
			// on définit le titre du document
			DublincoreSchemaUtils.setTitle(fddModelFolder, name);
			// on définit le répertoire comme non supprimable
			fddModelFolder.setProperty(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE,
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_IS_SUPPRIMABLE_PROPERTY, false);
			// create document in session
			fddModelFolder = session.createDocument(fddModelFolder);
			return fddModelFolder;
		} catch (final ClientException e) {
			throw new RuntimeException(" erreur lors de la creation d'un répertoire fond de dossier ", e);
		}
	}

	@Override
	public String getFileFolderPath(final DocumentModel folderModel, final String folderRootName) {
		String docPath = folderModel.getPathAsString();
		if (!org.apache.commons.lang.StringUtils.isEmpty(folderRootName)) {
			Path path = folderModel.getPath();
			path = path.removeLastSegments(1);
			final String docPathWithoutLastsegment = path.toString();
			final int lastIndex = docPathWithoutLastsegment.lastIndexOf(folderRootName);
			docPath = docPath.substring(lastIndex);
		}
		return docPath;
	}

	@Override
	public void logAction(final DocumentModel dm, final CoreSession session, final String filename)
			throws ClientException {
		final DocumentModel repertoireParent = session.getParentDocument(dm.getRef());

		if (repertoireParent != null) {
			boolean found = false;
			DocumentModel dossier = repertoireParent;
			// remonte sur le dossier
			while (!found) {
				dossier = session.getParentDocument(dossier.getRef());
				if (dossier == null) {
					break;
				} else {
					found = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE.equals(dossier.getType());
				}
			}
			if (found) {
				journalisationAction(SolonEpgEventConstant.ACTION_UPDATE, dm.getType(), session, dossier, filename,
						repertoireParent);
			}
		}
	}
}
