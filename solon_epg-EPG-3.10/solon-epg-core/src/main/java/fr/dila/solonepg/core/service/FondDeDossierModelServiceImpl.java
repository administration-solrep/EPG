package fr.dila.solonepg.core.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.Path;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFolder;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModel;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModelNode;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModelRoot;
import fr.dila.solonepg.api.fonddossier.FondDeDossierModelType;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.FondDeDossierModelService;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.api.service.TreeService;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.schema.DublincoreSchemaUtils;

public class FondDeDossierModelServiceImpl implements FondDeDossierModelService {

	private static final long	serialVersionUID			= 8292406959483724550L;

	private static final Log	log							= LogFactory.getLog(FondDeDossierModelServiceImpl.class);

	/**
	 * Requete utilise pour récupérer la racine des modèles
	 */
	private static final String	GET_ROOT_MODELE_FDD_QUERY	= "SELECT * FROM %s";

	/**
	 * Identifiant de la racine des modèles de fond de dossier : ce document n'est pas sensé changer, on le garde en
	 * cache.
	 */
	private String				rootModelFddId;

	@Override
	public DocumentModel getFondDossierModelRoot(final CoreSession session) throws ClientException {
		log.debug("récupération de la racine des modèles de fond de dossier");
		if (StringUtils.isEmpty(rootModelFddId)) {
			// on récupère la racine modèle de fond de dossier en faisant une requête
			final String query = String.format(GET_ROOT_MODELE_FDD_QUERY,
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_ROOT_DOCUMENT_TYPE);
			final DocumentModelList rootModeleFddList = session.query(query);
			if (rootModeleFddList == null || rootModeleFddList.isEmpty()) {
				final String erreur = "Erreur lors de la racine des modèles de fond de dossier : pas de Document trouvé !";
				log.error(erreur);
				throw new ClientException(erreur);
			} else if (rootModeleFddList.size() > 1) {
				final String erreur = "Erreur lors de la racine des modèles de fond de dossier : plusieurs document trouvés !";
				log.error(erreur);
				throw new ClientException(erreur);
			}
			final DocumentModel rootModeleFdd = rootModeleFddList.get(0);
			// il n'y a qu'une seule racine qui contient les modèles de feuille de route.
			rootModelFddId = rootModeleFdd.getId();
			return rootModeleFdd;
		}
		// on récupère la racine modèle de fond de dossier à partir de son identifiant
		return session.getDocument(new IdRef(rootModelFddId));
	}

	@Override
	public List<String> getFormatsAutorises(final CoreSession session) {
		DocumentModel fondDossierModelRoot;
		try {
			fondDossierModelRoot = getFondDossierModelRoot(session);
			return fondDossierModelRoot.getAdapter(FondDeDossierModelRoot.class).getFormatsAutorises();
		} catch (final ClientException e) {
			throw new RuntimeException("Erreur lors de de la récupération de la racine du fond de dossier", e);
		}
	}

	@Override
	public DocumentModel updateFondDossierModelRoot(final CoreSession session, final DocumentModel docModel) {
		DocumentModel fondDossierModelFolderUpdated = null;
		// on définit dans la racine des modèles de fond de dossier la liste des formats autorisés par defaut
		try {
			final FondDeDossierModelRoot fddModelRoot = docModel.getAdapter(FondDeDossierModelRoot.class);
			fddModelRoot.setFormatsAutorises(DossierSolonEpgConstants.FormatAutoriseListExtendFolderFondDeDossier);
			fondDossierModelFolderUpdated = session.saveDocument(fddModelRoot.getDocument());
		} catch (final ClientException e) {
			throw new RuntimeException(
					"La mise à jour de la racine des modèles de fond de dossier n'a pas pu être effectuée");
		}
		return fondDossierModelFolderUpdated;
	}

	@Override
	public void createFondDeDossierDefaultRepository(DocumentModel fddModelDoc, final CoreSession session) {
		try {
			// récupération du modèle de fond de dossier
			final FondDeDossierModel fondDeDossierModel = fddModelDoc.getAdapter(FondDeDossierModel.class);
			final String typeActe = fondDeDossierModel.getTypeActe();

			// on regarde les répertoires associé au type d'acte
			final ActeService acteService = SolonEpgServiceLocator.getActeService();
			final Boolean hasTypeActeExtendedFolder = acteService.hasTypeActeExtendedFolder(typeActe);
			final Boolean hasTypeActeSaisineRectificative = acteService.hasTypeActeSaisineRectificative(typeActe);
			if (typeActe.isEmpty()) {
				throw new RuntimeException(
						" erreur lors de la creation de l'arborescence du modele de fond de dossier => type acte null ");
			}

			if (acteService.isRapportAuParlement(typeActe)) {
				// création des répertoires racines (1er niveau) : répertoire non supprimable
				createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR);
				createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG);
				createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG);
				createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER);

			} else {

				// création des répertoires racines (1er niveau) : répertoire non supprimable
				createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR);
				createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG);
				createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG);
				fddModelDoc = createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER);

				// création des répertoire de 2eme niveau
				// createFondDossierModelFolderElement(fddModelDoc, session,
				// SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES);
				fddModelDoc = createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE);

				// création des répertoire de 3eme niveau
				createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ANNEXES_NON_PUBLIEES);
				createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_NOTE_MIN);
				createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RAPPORT_PRESENTATION);

				// création conditionnel suivant le type d'acte
				if (hasTypeActeExtendedFolder) {
					createFondDossierModelFolderElement(fddModelDoc, session,
							SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_lETTRES_ACCORD);
					createFondDossierModelFolderElement(fddModelDoc, session,
							SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_LETTRE_SAISINE_CE);
					createFondDossierModelFolderElement(fddModelDoc, session,
							SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_LISTE_COM_GOUV);
				}

				// création dans fond de dossier saisine rectificative pour dossiers concernés
				if (hasTypeActeSaisineRectificative) {
					createFondDossierModelFolderElement(fddModelDoc, session,
							SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_SAISINE_RECTIFICATIVE);
				}

				createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_AVIS_DIVERS);
				createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_TEXTE_REF);
				createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ETUDE_IMPACT);
				createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DIVERS);

				// création conditionnelle suivant le type d'acte : cas spécifique pour l'amnistie
				if (hasTypeActeExtendedFolder && !TypeActeConstants.TYPE_ACTE_AMNISTIE.equals(typeActe)) {
					createFondDossierModelFolderElement(fddModelDoc, session,
							SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_AVIS_CE);
				}
				createFondDossierModelFolderElement(fddModelDoc, session,
						SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_TEXTE_CONSOLIDES);
			}

			session.save();
		} catch (final ClientException e) {
			throw new RuntimeException(" erreur lors de la creation de l'arborescence du modele de fond de dossier ", e);
		}
	}

	@Override
	public DocumentModel getFondDossierModelFromTypeActe(final CoreSession session, final String typeActe)
			throws ClientException {

		final StringBuilder queryBuilder = new StringBuilder(
				"SELECT m.ecm:uuid as id FROM ModeleFondDeDossier as m WHERE m.mfdd:typeActe = ?");

		final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, "ModeleFondDeDossier",
				queryBuilder.toString(), new Object[] { typeActe }, 1, 0);
		if (docs.isEmpty()) {
			return null;
		} else {
			return docs.get(0);
		}
	}

	@Override
	public List<DocumentModel> getListFondDossierFolderModelFromTypeActe(final CoreSession session,
			final String typeActe) throws ClientException {
		// on récupère tous les répertoires du modèle du fond de dossier correspondant au type d'acte choisi
		final StringBuilder getListFondDossierFolderModelFromTypeActeQuery = new StringBuilder(
				"SELECT l.ecm:uuid AS id FROM ")
				.append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE).append(" AS l ")
				.append(" WHERE isChildOf(l.ecm:uuid, select r.ecm:uuid from ")
				.append(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_DOCUMENT_TYPE)
				.append(" AS r WHERE r.mfdd:typeActe = ?) = 1");

		final List<String> paramList = new ArrayList<String>();
		paramList.add(typeActe);
		final List<DocumentModel> docList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE,
				getListFondDossierFolderModelFromTypeActeQuery.toString(), paramList.toArray(new String[0]));

		return docList;
	}

	@Override
	public DocumentModel createFondDossierModele(final DocumentModel documentRacine, final CoreSession session,
			final String typeActe) {
		try {
			// on récupère le service de vocabulaire de solon epg
			final SolonEpgVocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();

			final String typeActeLabel = vocService.getLabelFromId(VocabularyConstants.TYPE_ACTE_VOCABULARY, typeActe,
					STVocabularyConstants.COLUMN_LABEL);
			// titre du document dans le classement nuxeo
			final String titre = "ModeleFondDeDossier " + typeActeLabel;

			// create document model
			DocumentModel fddModel = session.createDocumentModel(documentRacine.getPathAsString(), titre,
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_DOCUMENT_TYPE);

			// On affecte par les proprietes du mooodèle de fond de dossier
			fddModel.setProperty(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_DOCUMENT_SCHEMA,
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_TYPE_ACTE_PROPERTY, typeActe);

			// create document in session
			fddModel = session.createDocument(fddModel);
			return fddModel;
		} catch (final ClientException e) {
			throw new RuntimeException(" erreur lors de la creation d'un modele de fond de dossier ", e);
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
	protected DocumentModel createFondDossierModelFolderElement(final DocumentModel documentRacine,
			final CoreSession session, final String name) {
		try {
			// create document model
			DocumentModel fddModelFolder = session.createDocumentModel(documentRacine.getPathAsString(), name,
					SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE);
			// on définit le titre du document
			DublincoreSchemaUtils.setTitle(fddModelFolder, name);
			final FondDeDossierFolder fondDeDossierFolder = fddModelFolder.getAdapter(FondDeDossierFolder.class);
			// on définit le document comme non supprimable (utilisé lors de l'instanciation du répertoire)
			fondDeDossierFolder.setIsDeletable(false);
			// create document in session
			fddModelFolder = session.createDocument(fddModelFolder);
			return fddModelFolder;
		} catch (final ClientException e) {
			throw new RuntimeException(" erreur lors de la creation d'un modele de répertoire fond de dossier ", e);
		}
	}

	// /////////////////
	// Fond de dossier Model : tree method
	// ////////////////

	@Override
	public List<FondDeDossierModelNode> getFondDeDossierModelRootNode(final CoreSession session,
			final DocumentModel fondDeDossierModel) {
		final List<FondDeDossierModelNode> repertoireGroupsList = new ArrayList<FondDeDossierModelNode>();
		try {
			final List<DocumentModel> listRepertoire = session.getChildren(fondDeDossierModel.getRef());
			for (final DocumentModel repList : listRepertoire) {
				repertoireGroupsList.add(getFondDeDossierModelNodeFromDocumentModel(repList));
			}
		} catch (final ClientException e) {
			throw new RuntimeException(
					"erreur lors de la recuperation des repertoire racine du modele de fond de dossier", e);
		}
		return repertoireGroupsList;
	}

	private FondDeDossierModelNode getFondDeDossierModelNodeFromDocumentModel(final DocumentModel docModel) {
		final FondDeDossierModelNode node = new FondDeDossierModelNode();
		// on définit l'identifiant, le nom et le type du noeud
		node.setId(docModel.getId());
		final String name = DublincoreSchemaUtils.getTitle(docModel);
		node.setName(name);
		if (SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RAPPORT_PRESENTATION.equals(name)
				|| SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE.equals(name)
				|| SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES.equals(name)
				|| SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_AVIS_CE.equals(name)) {
			// on vérifie que le noeud en question fait parti des noeuds non supprimables à partir de son chemin
			Path path = docModel.getPath();
			final int segmentCount = path.segmentCount();
			// note : on teste les 2 derniers segments des répertoires publiques du fond de dossier que l'on ne doit pas
			// supprimer.
			int segmentToRemove = segmentCount - 2;
			if (segmentToRemove < 0) {
				segmentToRemove = 0;
			}
			path = path.removeFirstSegments(segmentToRemove);
			final String pathString = path.toString();
			if (SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_PATH_RAPPORT_PRESENTATION.equals(pathString)
					|| SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_PATH_DOC_JOINTE.equals(pathString)
					|| SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_PATH_EPREUVES.equals(pathString)
					|| SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_PATH_AVIS_CE.equals(pathString)) {
				node.setType(FondDeDossierModelType.FOND_DOSSIER_FOLDER_UNDELETABLE);
				return node;
			}
		}
		// par défaut le type de dossier est supprimable
		node.setType(FondDeDossierModelType.FOND_DOSSIER_FOLDER_DELETABLE);
		return node;
	}

	@Override
	public List<FondDeDossierModelNode> getChildrenFondDeDossierModelNode(final String documentId,
			final CoreSession session) {
		// initialisation des variables
		final List<FondDeDossierModelNode> subGroupsNodeList = new ArrayList<FondDeDossierModelNode>();

		// on récupère les fils du document à partir de son identifiant
		DocumentModelList childDocModelList;
		try {
			childDocModelList = session.getChildren(new IdRef(documentId));

			// on parcourt les fils : on récupère les fils sous forme de noeud à partir de DocumentModel
			for (final DocumentModel childDoc : childDocModelList) {
				final FondDeDossierModelNode node = getFondDeDossierModelNodeFromDocumentModel(childDoc);
				subGroupsNodeList.add(node);
			}
		} catch (final ClientException e) {
			throw new RuntimeException(
					"erreur lors de la recuperation de repertoire fils du modele de fond de dossier", e);
		}
		return subGroupsNodeList;
	}

	@Override
	public void createNewDefaultFolderInTree(final DocumentModel documentParent, final CoreSession session,
			final String typeActe) throws ClientException {
		// appel au service générique
		final TreeService treeService = SolonEpgServiceLocator.getTreeService();
		treeService.createNewDefaultFolderInTree(documentParent, session,
				SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_FOLDER_DOCUMENT_TYPE, null, typeActe);
	}

	@Override
	public void createNewFolderNodeBefore(final DocumentModel documentCourant, final CoreSession session,
			final String typeActe) throws ClientException {
		// appel au service générique
		final TreeService treeService = SolonEpgServiceLocator.getTreeService();
		treeService.createNewFolderNodeBefore(documentCourant, session,
				SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_FOLDER_DOCUMENT_TYPE, null, typeActe);
	}

	@Override
	public void createNewFolderNodeAfter(final DocumentModel documentCourant, final CoreSession session,
			final String typeActe) throws ClientException {
		// appel au service générique
		final TreeService treeService = SolonEpgServiceLocator.getTreeService();
		treeService.createNewFolderNodeAfter(documentCourant, session,
				SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_FOLDER_DOCUMENT_TYPE, null, typeActe);
	}

	@Override
	public DocumentModel editFolder(final DocumentModel documentCourant, final CoreSession session, final String name,
			final String typeActe) throws ClientException {
		// on fixe le nom du document
		DublincoreSchemaUtils.setTitle(documentCourant, name);

		// note : passer par le session.move pour renommer et le session.orderBefore pour changer le ecm:name... voir si
		// moyen plus efficace
		// on fixe le nom du document dans l'arborescence
		// documentCourant.setPathInfo(documentCourant.getPath().removeLastSegments(1).toString(), name);

		// appel au service générique
		final TreeService treeService = SolonEpgServiceLocator.getTreeService();
		return treeService.updateFolder(documentCourant, session, typeActe);
	}

	@Override
	public void deleteFolder(final DocumentModel documentCourant, final CoreSession session, final String typeActe)
			throws ClientException {
		// appel au service générique
		final TreeService treeService = SolonEpgServiceLocator.getTreeService();
		treeService.deleteDocument(documentCourant, session, null, typeActe);
	}

}
