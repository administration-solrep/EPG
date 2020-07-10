package fr.dila.solonepg.core.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.runtime.model.DefaultComponent;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.parapheur.ParapheurModel;
import fr.dila.solonepg.api.parapheur.ParapheurModelNode;
import fr.dila.solonepg.api.parapheur.ParapheurModelType;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.ParapheurModelService;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.api.service.TreeService;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.util.PropertyUtil;

/**
 * Services pour le modèle de parapheur.
 * 
 * @author antoine Rolin
 * 
 */
public class ParapheurModelServiceImpl extends DefaultComponent implements ParapheurModelService {

    private static final long serialVersionUID = -5238650995275702842L;

    @Override
    public DocumentModel getParapheurModelRoot(final CoreSession session) throws ClientException {
        // création requete
        final StringBuilder queryBuilder = new StringBuilder("SELECT m.ecm:uuid as id FROM ");
        queryBuilder.append(SolonEpgParapheurConstants.PARAPHEUR_MODEL_ROOT_DOCUMENT_TYPE);
        queryBuilder.append(" as m ");

        // execution requete
        final List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
                SolonEpgParapheurConstants.PARAPHEUR_MODEL_ROOT_DOCUMENT_TYPE, queryBuilder.toString(), null, 1, 0);
        if (list == null || list.isEmpty()) {
            throw new ClientException("Racine des modèles de fond de dossier non trouvée");
        } else if (list.size() > 1) {
            throw new ClientException("Plusieurs racines des modèles de fond de dossier trouvées");
        }
        return list.get(0);
    }

    @Override
    public void createParapheurDefaultRepository(final DocumentModel parapheurModel, final CoreSession session) {
        try {

            // récupération du modèle de fond de dossier
            final ParapheurModel parapheurModelDoc = parapheurModel.getAdapter(ParapheurModel.class);
            final String typeActe = parapheurModelDoc.getTypeActe();

            if (typeActe.isEmpty()) {
                throw new RuntimeException(" erreur lors de la creation de l'arborescence du modele de parapheur => type acte null ");
            }

            // création des répertoires racines du parapheur
            Boolean acteNonVide;
            Boolean extraitNonVide;
            final Boolean pieceComplementaireEstNonVide = false;
            final Long nbMaxDocActeEtExtrait = 1L;
            final Long nbMaxDocPieceCompl = null;
            // TODO pour faciliter les tests, on définit le format de fichier .doc pour tout les documents
            List<String> idsFormatAutoriseTypeActeEtExtrait = DossierSolonEpgConstants.FormatAutoriseListExtendFolderParapheur;

            final ActeService acteService = SolonEpgServiceLocator.getActeService();
            if (acteService.hasInjectionType(typeActe)) {
                extraitNonVide = false;
                acteNonVide = false;
                idsFormatAutoriseTypeActeEtExtrait = null;
            } else if (acteService.hasExtraitObligatoire(typeActe)) {
                extraitNonVide = true;
                acteNonVide = false;
            } else {
                extraitNonVide = false;
                acteNonVide = true;
            }

            if (acteService.isRapportAuParlement(typeActe)) {
                createParapheurModelFolderElement(parapheurModel, session, SolonEpgParapheurConstants.PARAPHEUR_FOLDER_RAPPORT_NAME, acteNonVide,
                        nbMaxDocActeEtExtrait, null);
            } else {
                createParapheurModelFolderElement(parapheurModel, session, SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME, acteNonVide,
                        nbMaxDocActeEtExtrait, idsFormatAutoriseTypeActeEtExtrait);
                createParapheurModelFolderElement(parapheurModel, session, SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EXTRAIT_NAME, extraitNonVide,
                        nbMaxDocActeEtExtrait, idsFormatAutoriseTypeActeEtExtrait);
                createParapheurModelFolderElement(parapheurModel, session, SolonEpgParapheurConstants.PARAPHEUR_FOLDER_PIECE_COMPLEMENTAIRE_NAME,
                        pieceComplementaireEstNonVide, nbMaxDocPieceCompl, null);
            }

            session.save();
        } catch (final ClientException e) {
            throw new RuntimeException(" erreur lors de la creation de l'arborescence du modele de parapheur ", e);
        }
    }

    @Override
    public DocumentModel createParapheurModele(final DocumentModel documentRacine, final CoreSession session, final String typeActe) {
        try {
            // on récupère le service de vocabulaire de solon epg
            final SolonEpgVocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
            final String typeActeLabel = vocService.getLabelFromId(VocabularyConstants.TYPE_ACTE_VOCABULARY, typeActe,
                    STVocabularyConstants.COLUMN_LABEL);

            // titre du document dans le classement nuxeo
            final String titre = "ModeleParapheur " + typeActeLabel;

            // create document model
            DocumentModel parapheurModel = session.createDocumentModel(documentRacine.getPathAsString(), titre,
                    SolonEpgParapheurConstants.PARAPHEUR_MODEL_DOCUMENT_TYPE);

            // On affecte par les proprietes du modèle de parapheur
            final ParapheurModel parapheurModelDoc = parapheurModel.getAdapter(ParapheurModel.class);
            parapheurModelDoc.setTypeActe(typeActe);

            // create document in session
            parapheurModel = session.createDocument(parapheurModelDoc.getDocument());
            return parapheurModel;
        } catch (final ClientException e) {
            throw new RuntimeException(" erreur lors de la creation d'un modele de fond de dossier ", e);
        }
    }

    /**
     * création des répertoires par defaut du modele du parapheur
     * 
     * @author antoine Rolin
     * 
     * @param
     * 
     * @return DocumentModel
     */
    protected DocumentModel createParapheurModelFolderElement(final DocumentModel documentRacine, final CoreSession session, final String name,
            final Boolean estVide, final Long nbMaxDoc, final List<String> formatAutoriseDoc) {
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

    @Override
    public DocumentModel getParapheurModelFromTypeActe(final CoreSession session, final String typeActe) throws ClientException {
        final String query = "SELECT m.ecm:uuid as id FROM ModeleParapheur as m WHERE m.mp:typeActe = ? ";

        final List<DocumentModel> modeleParapheurList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session, "ModeleParapheur", query,
                new Object[] { typeActe }, 1, 0);

        if (modeleParapheurList == null || modeleParapheurList.isEmpty()) {
            return null;
        }

        // on renvoie le premier resultat retourné : la requête doit retourner un seul résultat
        return modeleParapheurList.get(0);
    }

    // /////////////////
    // Parapheur Model : méthodes liées à l'affichage de l'arborescence
    // ////////////////

    @Override
    public List<ParapheurModelNode> getParapheurModelRootNode(final CoreSession session, final DocumentModel parapheurModel) {
        final List<ParapheurModelNode> repertoireGroupsList = new ArrayList<ParapheurModelNode>();
        try {
            final List<DocumentModel> listRepertoire = session.getChildren(parapheurModel.getRef());
            for (final DocumentModel repList : listRepertoire) {
                repertoireGroupsList.add(getParapheurModelNodeFromDocumentModel(repList, session));
            }
        } catch (final ClientException e) {
            throw new RuntimeException("erreur lors de la recuperation des repertoire  du modele de parapheur", e);
        }
        return repertoireGroupsList;
    }

    /**
     * Création d'un ParapheurModelNode à partir d'un DocumentModel.
     * 
     * @param docModel
     * 
     * @return ParapheurModelNode
     * 
     * @throws ClientException
     */
    private ParapheurModelNode getParapheurModelNodeFromDocumentModel(final DocumentModel docModel, final CoreSession session) throws ClientException {
        final ParapheurModelNode node = new ParapheurModelNode();
        // on définit l'identifiant, le nom et le type du noeud
        node.setId(docModel.getId());
        node.setName(DublincoreSchemaUtils.getTitle(docModel));
        final String documentType = docModel.getType();
        node.setType(ParapheurModelType.getEnum(documentType));
        // on récupère les propriééts spécifique aux répertoires de modèles de parapheur
        final ParapheurFolder parModelFolderdoc = docModel.getAdapter(ParapheurFolder.class);
        node.setNbDocAccepteMax(parModelFolderdoc.getNbDocAccepteMaxUnrestricted(session));
        // node.setFeuilleStyleFiles(parModelFolderdoc.getFeuilleStyleFiles());
        node.setFormatAutorise(parModelFolderdoc.getFormatsAutorisesUnrestricted(session));
        return node;
    }

    @Override
    public List<ParapheurModelNode> getChildrenParapheurModelNode(final String documentId, final CoreSession session) {
        // initialisation des variables
        final List<ParapheurModelNode> subGroupsNodeList = new ArrayList<ParapheurModelNode>();

        // on récupère les fils du document à partir de son identifiant
        DocumentModelList childDocModelList;
        try {
            childDocModelList = session.getChildren(new IdRef(documentId));

            // on parcourt les fils : on récupère les fils sous forme de noeud à partir de DocumentModel
            for (final DocumentModel childDoc : childDocModelList) {
                final ParapheurModelNode node = getParapheurModelNodeFromDocumentModel(childDoc, session);
                subGroupsNodeList.add(node);
            }
        } catch (final ClientException e) {
            throw new RuntimeException("erreur lors de la recuperation de repertoire fils du modele de parapheur", e);
        }
        return subGroupsNodeList;
    }

    @Override
    public DocumentModel editFolder(final DocumentModel documentCourant, final CoreSession session, final String name, final Boolean estNonVide,
            final Long NbMaxDoc, final List<File> feuilleStyleFiles, final List<String> formatsAutorisesIds) throws ClientException {
        // on fixe le nom du document
        DublincoreSchemaUtils.setTitle(documentCourant, name);
        // on fixe les propriétés spécifiques du document
        final ParapheurFolder parapheurModelFolder = documentCourant.getAdapter(ParapheurFolder.class);
        // note : on récupère le boolean estNonVide et on veut la valeur estVide
        parapheurModelFolder.setEstNonVide(estNonVide);
        parapheurModelFolder.setNbDocAccepteMax(NbMaxDoc);
        // parapheurModelFolder.setFeuilleStyleFiles(feuilleStyleFiles);
        parapheurModelFolder.setFormatsAutorises(formatsAutorisesIds);

        // TODO passer par le session.move pour renommer et le session.orderBefore pour changer le nom du document... voir si moyen plus efficace
        // on fixe le nom du document dans l'arborescence
        // documentCourant.setPathInfo(documentCourant.getPath().removeLastSegments(1).toString(), name);

        // sauvegarde du document
        return parapheurModelFolder.save(session);
    }

    @Override
    public DocumentModel saveFolder(final DocumentModel documentCourant, final CoreSession session, final String typeActe) throws ClientException {
        // appel au service générique
        final TreeService treeService = SolonEpgServiceLocator.getTreeService();
        return treeService.updateFolder(documentCourant, session, typeActe);
    }

    @Override
    public DocumentModel saveFolder(final DocumentModel documentCourant, final CoreSession session) throws ClientException {
        return saveFolder(documentCourant, session, null);
    }

    @Override
    public DocumentModel updateFormatFile(final DocumentModel documentCourant, final List<String> formatFichier) throws ClientException {
        documentCourant.setProperty(SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA,
                SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_FORMAT_AUTORISE_PROPERTY, formatFichier);
        return documentCourant;
    }

    @Override
    public List<String> getFormatFile(final DocumentModel documentCourant) throws ClientException {
        List<String> formatsFile = PropertyUtil.getStringListProperty(documentCourant,
                SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA,
                SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_FORMAT_AUTORISE_PROPERTY);
        if (formatsFile == null) {
            formatsFile = new ArrayList<String>();
        }
        return formatsFile;
    }

    // TODO unused method

    @Override
    public void deleteFolder(final DocumentModel documentCourant, final CoreSession session) throws ClientException {
        // note : la regle de gestion concernant l'interdiction de supprimer un répertoire si cela rend l'arboresence vide est gérée dans le bean d'affichage
        // appel au service générique
        final TreeService treeService = SolonEpgServiceLocator.getTreeService();
        treeService.deleteDocument(documentCourant, session, null);
    }

    @Override
    public void createNewDefaultFolderInTree(final DocumentModel documentParent, final CoreSession session) throws ClientException {
        // appel au service générique
        final TreeService treeService = SolonEpgServiceLocator.getTreeService();
        treeService.createNewDefaultFolderInTree(documentParent, session, SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_TYPE, null);
    }

    @Override
    public void createNewFolderNodeBefore(final DocumentModel documentCourant, final CoreSession session) throws ClientException {
        // appel au service générique
        final TreeService treeService = SolonEpgServiceLocator.getTreeService();
        treeService.createNewFolderNodeBefore(documentCourant, session, SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_TYPE, null);
    }

    @Override
    public void createNewFolderNodeAfter(final DocumentModel documentCourant, final CoreSession session) throws ClientException {
        // appel au service générique
        final TreeService treeService = SolonEpgServiceLocator.getTreeService();
        treeService.createNewFolderNodeAfter(documentCourant, session, SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_TYPE, null);
    }

}
