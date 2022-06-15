package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.parapheur.ParapheurFile;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.parapheur.ParapheurInstance;
import fr.dila.solonepg.api.service.ParapheurModelService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.ss.api.exception.SSException;
import fr.dila.ss.api.tree.SSTreeFile;
import fr.dila.ss.api.tree.SSTreeFolder;
import fr.dila.ss.core.service.SSTreeServiceImpl;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.schema.FileSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringHelper;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.Path;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.VersioningOption;
import org.nuxeo.ecm.core.api.impl.blob.ByteArrayBlob;
import org.nuxeo.ecm.core.api.versioning.VersioningService;
import org.nuxeo.ecm.core.schema.PrefetchInfo;

/**
 * Implémentation des services de gestion du parapheur.
 *
 * @author ARN
 */
public class ParapheurServiceImpl extends SSTreeServiceImpl implements ParapheurService {
    /**
     * Serial UID
     */
    private static final long serialVersionUID = -8252083715021755029L;

    private static final Log log = LogFactory.getLog(ParapheurServiceImpl.class);
    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(ParapheurServiceImpl.class);

    /**
     * Default constructor
     */
    public ParapheurServiceImpl() {
        super();
    }

    @Override
    public void createAndFillParapheur(final Dossier dossier, final CoreSession session) {
        // récupération du type d'acte via le dossier
        final String typeActe = dossier.getTypeActe();
        if (typeActe == null) {
            throw new NuxeoException("Le type d'acte du dossier est nul !");
        }
        createAndFillParapheur(dossier, session, typeActe);
    }

    @Override
    public void createAndFillParapheurInjection(final Dossier dossier, final CoreSession session) {
        createAndFillParapheur(dossier, session, TypeActeConstants.TYPE_ACTE_INJECTION);
    }

    protected void createAndFillParapheur(final Dossier dossier, final CoreSession session, final String typeActe) {
        // création du parapheur
        final ParapheurInstance parapheur = createParapheurDocument(dossier.getDocument(), session);
        final DocumentModel parapheurModelDoc = parapheur.getDocument();
        final DocumentRef parapheurRef = parapheurModelDoc.getRef();
        // on définit l'id du parapheur depuis le dossier
        dossier.setDocumentParapheurId(parapheurModelDoc.getId());

        // récupération du modèle de parapheur associé au document
        final ParapheurModelService parapheurModelService = SolonEpgServiceLocator.getParapheurModelService();
        final DocumentModel parapheurModel = parapheurModelService.getParapheurModelFromTypeActe(session, typeActe);

        // récupération des répertoires racines du parapheur
        final DocumentModelList parapheurModelFolderRoots = session.getChildren(parapheurModel.getRef());

        // parcourt des répertoires racines et recopie des répertoires
        // si tout les répertoires du parapheur peuvent être vide, on le définit dans le champ du dossier prévu à cet
        // effet
        boolean parapheurComplet = true;
        // si le répertoire "Acte" doit être non vide et que le répertoire "Extrait" peut être vide,
        // alors l' "Acte intégral" est considéré comme référence pour le numéro de version, dans les autres cas c'est
        // l' "Extrait"
        boolean acteObligatoire = false;
        boolean extraitObligatoire = false;

        for (final DocumentModel parapheurModelFolderRoot : parapheurModelFolderRoots) {
            final ParapheurFolder paraFolderDoc = parapheurModelFolderRoot.getAdapter(ParapheurFolder.class);
            if (paraFolderDoc.getEstNonVide()) {
                parapheurComplet = false;
                final String folderName = paraFolderDoc.getDocument().getTitle();
                if (SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EXTRAIT_NAME.equals(folderName)) {
                    extraitObligatoire = true;
                } else if (SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME.equals(folderName)) {
                    acteObligatoire = true;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("copie repertoire parapheur : " + parapheurModelFolderRoot.getTitle());
            }

            session.copy(parapheurModelFolderRoot.getRef(), parapheurRef, null);
        }
        // on définit si le parapheur est complet
        dossier.setIsParapheurComplet(parapheurComplet);
        // on définit le type de fichier de référence (acte ou extrait)
        dossier.setIsActeReferenceForNumeroVersion((acteObligatoire && !extraitObligatoire));
    }

    /**
     * Création du document parapheur à partir du dossier.
     *
     * @param dossier
     * @param session
     * @return ParapheurInstance
     */
    private ParapheurInstance createParapheurDocument(final DocumentModel dossier, final CoreSession session) {
        if (log.isDebugEnabled()) {
            log.debug("creation du document parapheur");
        }

        final String parapheurtitre = "Parapheur";
        final DocumentModel newparapheurModel = session.createDocumentModel(
            dossier.getPath().toString(),
            parapheurtitre,
            SolonEpgParapheurConstants.PARAPHEUR_DOCUMENT_TYPE
        );

        // On affecte un titre par défaut au parapheur
        final Dossier dossierDoc = dossier.getAdapter(Dossier.class);

        final String identifiantQuestion = dossierDoc.getNumeroNor();
        if (identifiantQuestion == null) {
            DublincoreSchemaUtils.setTitle(newparapheurModel, "dossier sans numéro NOR");
        } else {
            final String title = "Parapheur n°" + identifiantQuestion;
            DublincoreSchemaUtils.setTitle(newparapheurModel, title);
        }

        // sauvegarde du parapheur
        final DocumentModel parapheurModel = session.createDocument(newparapheurModel);

        return parapheurModel.getAdapter(ParapheurInstance.class);
    }

    @Override
    public List<ParapheurFolder> getParapheurRootNode(final CoreSession session, final DocumentModel parapheur) {
        try {
            // récupération des fils du parapheur
            return getChildrenFolder(session, parapheur);
        } catch (final NuxeoException e) {
            throw new NuxeoException("Erreur lors de la récupération des répertoires du modèle de parapheur", e);
        }
    }

    @Override
    public List<ParapheurFolder> getChildrenParapheurNode(final String documentId, final CoreSession session) {
        // initialisation des variables
        // on récupère les fils du document à partir de son identifiant
        try {
            return getChildrenFolder(session, session.getDocument(new IdRef(documentId)));
        } catch (final NuxeoException e) {
            throw new NuxeoException("Erreur lors de la récupération des répertoires fils du modèle de parapheur", e);
        }
    }

    @Override
    public DocumentModel checkParapheurComplet(DocumentModel dossierdocModel, final CoreSession session) {
        final Dossier dossier = dossierdocModel.getAdapter(Dossier.class);

        // récupération des répertoires du parapheur qui doivent être non vide
        final StringBuilder query = new StringBuilder("SELECT l.ecm:uuid AS id FROM ");
        query.append(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE);
        query.append(" AS l WHERE l.pf:estNonVide = 1 and l.ecm:parentId = ?");

        IterableQueryResult res = null;
        final String queryCount = QueryUtils.ufnxqlToFnxqlQuery(
            "SELECT p.ecm:uuid as id FROM " +
            SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE +
            " as p WHERE p.ecm:parentId = ? and p." +
            NOT_DELETED
        );
        try {
            res =
                QueryUtils.doUFNXQLQuery(session, query.toString(), new String[] { dossier.getDocumentParapheurId() });
            final Iterator<Map<String, Serializable>> iterator = res.iterator();
            while (iterator.hasNext()) {
                final Map<String, Serializable> map = iterator.next();

                final Long nbDocInfolder = QueryUtils.doCountQuery(
                    session,
                    queryCount,
                    new Object[] { (String) map.get("id") }
                );
                if (nbDocInfolder == null || nbDocInfolder < 1) {
                    // si un répertoire du parapheur doit être non vide et qu'il ne contient pas de fichier, le
                    // parapheur n'est pas complet
                    dossier.setIsParapheurComplet(false);
                    dossierdocModel = dossier.save(session);
                    return dossierdocModel;
                }
            }
        } finally {
            if (res != null) {
                res.close();
            }
        }

        // tous les répertoires du parapheur devant être non vide sont non vide, le parapheur est complet
        dossier.setIsParapheurComplet(true);
        return dossier.save(session);
    }

    @Override
    public Boolean isFolderExtraitOrFolderActe(final DocumentModel dossierdocModel, final CoreSession session) {
        if (
            dossierdocModel == null ||
            !SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_TYPE.equals(dossierdocModel.getType())
        ) {
            return false;
        }
        final String title = dossierdocModel.getTitle();
        if (
            title != null &&
            !title.isEmpty() &&
            (
                SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME.equals(title) ||
                SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EXTRAIT_NAME.equals(title)
            )
        ) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isActeIntegral(final ParapheurFolder parapheurFolder, final CoreSession session) {
        return SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME.equals(parapheurFolder.getTitle());
    }

    @Override
    public DocumentModel getParapheurFolder(
        final DocumentModel dossierdocModel,
        final CoreSession session,
        final String folderName
    ) {
        // on récupère le répertoire du Parapheur via une requete FNXQL
        final StringBuilder getParapheurFolderQuery = new StringBuilder("SELECT l.ecm:uuid AS id FROM ")
            .append(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE)
            .append(" AS l WHERE l.ecm:name = ? and isChildOf(l.ecm:uuid, select r.ecm:uuid from ")
            .append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE)
            .append(" AS r WHERE r.ecm:uuid = ? ) = 1");

        final List<String> paramList = new ArrayList<>();
        paramList.add(folderName);
        paramList.add(dossierdocModel.getId());

        final List<DocumentModel> docList = QueryHelper.doUFNXQLQueryAndFetchForDocuments(
            session,
            getParapheurFolderQuery.toString(),
            paramList.toArray(new String[0]),
            1,
            0,
            new PrefetchInfo(
                STSchemaConstant.DUBLINCORE_SCHEMA +
                "," +
                SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_SCHEMA
            )
        );

        if (docList == null || docList.isEmpty()) {
            throw new EPGException("Aucun répertoire dans le parapheur avec le nom : " + folderName);
        }

        return docList.get(0);
    }

    @Override
    public List<DocumentModel> getParapheurFiles(final CoreSession session, final Dossier dossier, String... prefetch) {
        final DocumentModel parapheurDoc = dossier.getParapheur(session).getDocument();

        // on récupère tous les documents fils des répertoires racines visibles
        final StringBuilder getAllFileQuery = new StringBuilder("SELECT l.ecm:uuid AS id FROM ")
            .append(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE)
            .append(
                " AS l WHERE isChildOf(l.ecm:uuid, select r.ecm:uuid from Parapheur AS r WHERE r.ecm:uuid = ? ) = 1 and l." +
                NOT_DELETED
            );

        final List<String> paramList = new ArrayList<>();
        paramList.add(parapheurDoc.getId());

        return QueryHelper.doUnrestrictedUFNXQLQueryAndFetchForDocuments(
            session,
            getAllFileQuery.toString(),
            paramList.toArray(new String[0]),
            0L,
            0L,
            prefetch
        );
    }

    @Override
    public List<DocumentModel> getPieceParapheur(
        final CoreSession session,
        final Dossier dossier,
        final boolean piecesComplementaires,
        String... prefetch
    ) {
        final String publicationIntegraleOuExtrait = dossier.getPublicationIntegraleOuExtrait();
        String pieceParaName = null;
        boolean isInformationsParlementaires = dossier
            .getTypeActe()
            .equals(TypeActeConstants.TYPE_ACTE_INFORMATIONS_PARLEMENTAIRES);
        if (
            VocabularyConstants.TYPE_PUBLICATION_EXTRAIT.equals(publicationIntegraleOuExtrait) &&
            !isInformationsParlementaires
        ) {
            pieceParaName = SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EXTRAIT_NAME;
        } else if (
            VocabularyConstants.TYPE_PUBLICATION_INTEGRAL.equals(publicationIntegraleOuExtrait) ||
            isInformationsParlementaires
        ) {
            pieceParaName = SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME;
        }

        // récupération du répertoire de la pièce (acte ou extrait) à partir du chemin
        final Path pathToParapheur = dossier
            .getDocument()
            .getPath()
            .append(SolonEpgParapheurConstants.DEFAULT_PARAPHEUR_NAME);
        final Path pathToPiece = pathToParapheur.append(pieceParaName);
        final DocumentModel repertoireParapheur = session.getDocument(new PathRef(pathToPiece.toString()));

        // on récupère tous les documents fils des répertoires racines visibles
        final StringBuilder getPieceParapheurQuery = new StringBuilder("SELECT l.ecm:uuid AS id FROM ");
        getPieceParapheurQuery.append(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE);
        getPieceParapheurQuery.append(" AS l WHERE isChildOf(l.ecm:uuid, select r.ecm:uuid from ");
        getPieceParapheurQuery.append(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE);
        getPieceParapheurQuery.append(" AS r WHERE r.ecm:uuid IN(?,?) ) = 1 and l." + NOT_DELETED);

        final List<String> paramList = new ArrayList<>();
        paramList.add(repertoireParapheur.getId());

        // récupération du répertoire des pièces complémentaires à partir du chemin
        if (!isInformationsParlementaires && piecesComplementaires) {
            final Path pathToPieceComplementaire = pathToParapheur.append(
                SolonEpgParapheurConstants.PARAPHEUR_FOLDER_PIECE_COMPLEMENTAIRE_NAME
            );
            final DocumentModel repertoirePieceComplementaire = session.getDocument(
                new PathRef(pathToPieceComplementaire.toString())
            );
            paramList.add(repertoirePieceComplementaire.getId());
        } else {
            paramList.add("");
        }

        return QueryHelper.doUnrestrictedUFNXQLQueryAndFetchForDocuments(
            session,
            getPieceParapheurQuery.toString(),
            paramList.toArray(new String[0]),
            0L,
            0L,
            prefetch
        );
    }

    @Override
    public DocumentModel createBareParapheurFile(final CoreSession session) throws SSException {
        DocumentModel parapheurFile;
        try {
            parapheurFile = session.createDocumentModel(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE);
        } catch (final NuxeoException exc) {
            throw new SSException("Impossible de créer le fichier de parapheur", exc);
        }
        return parapheurFile;
    }

    @Override
    public void createParapheurFile(
        final CoreSession session,
        final String fileName,
        final byte[] content,
        final String folderName,
        final DocumentModel dossier
    ) {
        // récupération de l'utilisateur courant
        final NuxeoPrincipal nuxeoPrincipal = session.getPrincipal();
        // récupération du contenu du fichier
        final ByteArrayBlob blobArray = new ByteArrayBlob(content);
        blobArray.setFilename(fileName);
        // récupération du répertoire parent
        final DocumentModel repertoireParent = getParapheurFolder(dossier, session, folderName);

        // création du fichier
        createFileInFolder(
            repertoireParent,
            blobArray,
            session,
            SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE,
            nuxeoPrincipal,
            dossier
        );
    }

    @Override
    public void createParapheurFile(
        final CoreSession session,
        final Blob content,
        final String folderName,
        final DocumentModel dossier
    ) {
        // récupération de l'utilisateur courant
        final NuxeoPrincipal nuxeoPrincipal = session.getPrincipal();
        // récupération du répertoire parent
        final DocumentModel repertoireParent = getParapheurFolder(dossier, session, folderName);
        // création du fichier
        createFileInFolder(
            repertoireParent,
            content,
            session,
            SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE,
            nuxeoPrincipal,
            dossier
        );
    }

    @Override
    public DocumentModel createFileInFolder(
        final DocumentModel repertoireParent,
        final Blob blob,
        final CoreSession session,
        final String documentType,
        final NuxeoPrincipal currentUser,
        DocumentModel dossierDocument
    ) {
        if (documentType == null) {
            throw new NuxeoException("pas de type de document spécifié !");
        }
        // on récupère le nom du fichier
        final String fileName = blob.getFilename();
        final String docName = fileName;
        DocumentModel docModel = null;
        // création du DocumentModel
        try {
            docModel = session.createDocumentModel(repertoireParent.getPathAsString(), docName, documentType);
            // set document name
            DublincoreSchemaUtils.setTitle(docModel, docName);
            // set document file properties
            FileSchemaUtils.setContent(docModel, blob);
            // set entite
            docModel.setProperty(
                DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA,
                DossierSolonEpgConstants.FILE_SOLON_EPG_ENTITE_PROPERTY,
                getEntite(currentUser)
            );
            // creation du document en session
            docModel = session.createDocument(docModel);
            // sauvegarde du document en session pour avoir une version du document
            // incrementation du numero de version
            docModel.putContextData(VersioningService.VERSIONING_OPTION, VersioningOption.MAJOR);
            docModel = session.saveDocument(docModel);
            // RG-DOS-TRT-29 : si on ajoute un fichier dans le répertoire "extrait" du parapheur, on met à jour le champ
            // "Publication intégrale ou par extrait" du bordereau
            if (
                SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE.equals(documentType) &&
                SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EXTRAIT_NAME.equals(repertoireParent.getTitle())
            ) {
                final Dossier dossier = dossierDocument.getAdapter(Dossier.class);
                dossier.setPublicationIntegraleOuExtrait(DossierSolonEpgConstants.DOSSIER_EXTRAIT_PROPERTY_VALUE);
                dossierDocument = dossier.save(session);
            }
            // commit creation
            session.save();
            // journalisation de l'action dans les logs
            journalisationAction(
                SolonEpgEventConstant.ACTION_CREATE,
                docModel.getType(),
                session,
                dossierDocument,
                fileName,
                repertoireParent
            );
            // dossier : on vérifie si le parapheur est complet et on met à jour le numéro de version du dossier
            if (SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE.equals(documentType)) {
                dossierDocument = checkParapheurComplet(dossierDocument, session);
                updateDossierNumeroVersion(repertoireParent, docModel, dossierDocument, session, false);
            }
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_FILE_TEC, e);
            throw new NuxeoException("Erreur lors de la création du fichier", e);
        }
        return docModel;
    }

    @Override
    public void updateFile(
        final CoreSession session,
        DocumentModel fichier,
        final Blob blob,
        final NuxeoPrincipal currentUser,
        final DocumentModel dossierDocument
    ) {
        // force unlock liveEdit
        STServiceLocator.getSTLockService().unlockDocUnrestricted(session, fichier);

        // on récupère le nom du fichier
        final String fileName = blob.getFilename();
        final String docName = fileName;
        // set document name
        DublincoreSchemaUtils.setTitle(fichier, docName);
        DublincoreSchemaUtils.setCreator(fichier, session.getPrincipal().getName());

        // set document file properties
        FileSchemaUtils.setContent(fichier, blob);
        // set entite
        fichier.setProperty(
            DossierSolonEpgConstants.FILE_SOLON_EPG_DOCUMENT_SCHEMA,
            DossierSolonEpgConstants.FILE_SOLON_EPG_ENTITE_PROPERTY,
            getEntite(currentUser)
        );
        // incrementation du numero de version
        fichier.putContextData(VersioningService.VERSIONING_OPTION, VersioningOption.MAJOR);
        // create document in session
        session.move(fichier.getRef(), fichier.getParentRef(), docName);
        fichier = session.saveDocument(fichier);
        // commit modification
        session.save();
        // récupération du répertoire parent
        final DocumentModel repertoireParent = session.getParentDocument(fichier.getRef());
        // journalisation de l'action dans les logs
        journalisationAction(
            SolonEpgEventConstant.ACTION_UPDATE,
            fichier.getType(),
            session,
            dossierDocument,
            fileName,
            repertoireParent
        );
        // mise à jour du numéro de version du dossier
        updateDossierNumeroVersion(repertoireParent, fichier, dossierDocument, session, false);
    }

    @Override
    public void deleteFile(final CoreSession session, final DocumentModel document, final DocumentModel dossierDoc) {
        super.deleteFile(session, document);
        journalisationAction(
            SolonEpgEventConstant.ACTION_DELETE,
            document.getType(),
            session,
            dossierDoc,
            document.getTitle(),
            session.getDocument(document.getParentRef())
        );
    }

    @Override
    public void restoreToPreviousVersion(
        final CoreSession session,
        final String currentDocId,
        final DocumentModel dossierDoc
    ) {
        DocumentRef currentDocRef = new IdRef(currentDocId);
        DocumentModel currentFileDoc = session.getDocument(currentDocRef);
        super.restoreToPreviousVersion(session, currentDocId, dossierDoc);
        journalisationAction(
            SolonEpgEventConstant.ACTION_DELETE,
            currentFileDoc.getType(),
            session,
            dossierDoc,
            currentFileDoc.getTitle(),
            session.getDocument(currentFileDoc.getParentRef())
        );
    }

    /**
     **
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
     */
    private void updateDossierNumeroVersion(
        final DocumentModel repertoireParent,
        final DocumentModel fichier,
        final DocumentModel dossierDoc,
        final CoreSession session,
        final boolean fichierToDelete
    ) {
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        final String repParentName = repertoireParent.getTitle();
        final boolean acteReference = dossier.getIsActeReferenceForNumeroVersion();
        // test si l'on doit mettre à jour le numéro de version
        if (
            !acteReference &&
            SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EXTRAIT_NAME.equals(repParentName) ||
            acteReference &&
            SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME.equals(repParentName)
        ) {
            Long numVersion = 0L;
            if (!fichierToDelete) {
                // récupération version
                numVersion =
                    (Long) fichier.getProperty(
                        STSchemaConstant.UID_SCHEMA,
                        STSchemaConstant.UID_MAJOR_VERSION_PROPERTY
                    );
                if (numVersion == null || numVersion == 0L) {
                    numVersion = 1L;
                }
            }
            dossier.setNumeroVersionActeOuExtrait(numVersion);
            dossier.save(session);
        }
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
     */
    private void journalisationAction(
        final String actionType,
        final String documentType,
        final CoreSession session,
        final DocumentModel dossierDocument,
        final String docName,
        final DocumentModel repertoireParent,
        final String typeActe
    ) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(typeActe)) {
            // log des actions d'administration (modèles de fond de dossier et de parapheur)
            if (SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE.equals(documentType)) {
                // log pour les modèles de répertoire du fond de dossier
                if (SolonEpgEventConstant.ACTION_UPDATE.equals(actionType)) {
                    journalisationAdministrationAction(
                        session,
                        SolonEpgEventConstant.UPDATE_MODELE_PARAPHEUR_EVENT,
                        docName,
                        typeActe
                    );
                }
            }
            return;
        }

        final String eventCategory = STEventConstant.CATEGORY_PARAPHEUR;

        // journalisation de l'action dans les logs en fonction du type d'action et du type de document
        if (SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE.equals(documentType)) {
            // log pour les fichier du parapheur
            if (SolonEpgEventConstant.ACTION_CREATE.equals(actionType)) {
                // RG-DOS-TRT-29
                journalisationAction(
                    session,
                    dossierDocument,
                    SolonEpgEventConstant.CREATE_FILE_PARAPHEUR,
                    SolonEpgEventConstant.COMMENT_CREATE_FILE_PARAPHEUR +
                    docName +
                    "' dans le répertoire '" +
                    repertoireParent.getTitle() +
                    "'",
                    eventCategory
                );
            } else if (SolonEpgEventConstant.ACTION_UPDATE.equals(actionType)) {
                journalisationAction(
                    session,
                    dossierDocument,
                    SolonEpgEventConstant.CREATE_FILE_PARAPHEUR,
                    SolonEpgEventConstant.COMMENT_UPDATE_FILE_PARAPHEUR +
                    docName +
                    "' dans le répertoire '" +
                    repertoireParent.getTitle() +
                    "'",
                    eventCategory
                );
            } else if (SolonEpgEventConstant.ACTION_DELETE.equals(actionType)) {
                journalisationAction(
                    session,
                    dossierDocument,
                    SolonEpgEventConstant.DELETE_FILE_PARAPHEUR,
                    SolonEpgEventConstant.COMMENT_DELETE_FILE_PARAPHEUR +
                    docName +
                    "' dans le répertoire '" +
                    repertoireParent.getTitle() +
                    "'",
                    eventCategory
                );
            }
        }
    }

    /**
     * Journalise un evenement de l'espace d'administration (événement des modèles de fond de dossier et de parapheur)
     *
     * @param session
     * @param eventName
     * @param commentaire
     * @param categorie
     */
    private void journalisationAdministrationAction(
        final CoreSession session,
        final String eventName,
        final String docName,
        final String typeActe
    ) {
        final JournalService journalService = STServiceLocator.getJournalService();

        final StringBuilder commentaire = new StringBuilder();
        if (SolonEpgEventConstant.UPDATE_MODELE_PARAPHEUR_EVENT.equals(eventName)) {
            commentaire.append("Mise à jour d'un répertoire dans les modèles de parapheur '");
        }
        if (!SolonEpgEventConstant.CREATE_MODELE_FDD_EVENT.equals(eventName)) {
            commentaire.append(docName).append("'");
        }
        commentaire.append(" pour le type d'acte ").append(typeActe);

        journalService.journaliserActionAdministration(session, eventName, commentaire.toString());
    }

    private void journalisationAction(
        final String actionType,
        final String documentType,
        final CoreSession session,
        final DocumentModel dossierDocument,
        final String docName,
        final DocumentModel repertoireParent
    ) {
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
     */
    private void journalisationAction(
        final CoreSession session,
        final DocumentModel docModel,
        final String eventName,
        final String commentaire,
        final String categorie
    ) {
        final JournalService journalService = STServiceLocator.getJournalService();
        journalService.journaliserAction(session, docModel, eventName, commentaire, categorie);
    }

    @Override
    public void updateParapheurTree(
        final DocumentModel dossierDoc,
        final String newTypeActe,
        final CoreSession session
    ) {
        // récupération des répertoires courants du parapheur courant
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        if (dossier.getDocument().getCoreSession() == null) {
            dossier.getDocument().detach(false);
            dossier.getDocument().attach(session.getSessionId());
        }
        final List<ParapheurFolder> paraphCurrentFolderList = getChildrenParapheurNode(
            dossier.getDocumentParapheurId(),
            session
        );

        // récupération de la liste des répertoires du modèle de parapheur correspondant au nouveau type d'acte
        final ParapheurModelService parapheurModelService = SolonEpgServiceLocator.getParapheurModelService();
        final DocumentModel parapheurModel = parapheurModelService.getParapheurModelFromTypeActe(session, newTypeActe);

        // récupération des répertoires racines du parapheur
        final DocumentModelList parapheurModelFolderRoots = session.getChildren(parapheurModel.getRef());

        // 1) on parcourt les répertoires du parapheur courant afin de supprimer ceux qui sont déjà présent

        // Liste des chemins des répertoires du modèle du parapheur
        final Set<String> paraphModelFolderPathNameList = new HashSet<>();
        // Liste des chemins des répertoires existants du parapheur courant qui ne vont pas être supprimés
        final Set<String> paraphCurrentFolderPathNameList = new HashSet<>();
        // Map contenant les DocumentRef et les chemins des répertoires existants du parapheur courant qui ne vont pas
        // être supprimés
        final Map<String, DocumentRef> paraphCurrentFolderDocRefList = new HashMap<>();

        // on parcourt les répertoire du modèle de parapheur et on enregistre leurs chemins en enlevant la partie
        // commune aux autres répertoires
        for (final DocumentModel paraphModelFolder : parapheurModelFolderRoots) {
            paraphModelFolderPathNameList.add(getParaphFolderModelIdentificationPath(paraphModelFolder));
        }

        // liste des références de répertoire à supprimer
        final List<DocumentRef> docRefParaphFolderToRemove = new ArrayList<>();

        // on parcourt les répertoires courants du parapheur
        for (final ParapheurFolder folder : paraphCurrentFolderList) {
            // on récupère le chemin du répertoire dans l'arborescence en enlevant la partie commune aux autres
            // répertoires
            final String docPathName = folder.getName();
            int lastIndex = 0;
            // si ce chemin n'est pas présent dans la liste on regarde le chemin de son répertoire parent
            if (!paraphModelFolderPathNameList.contains(docPathName)) {
                lastIndex = docPathName.lastIndexOf("/");
                if (lastIndex != -1) {
                    final String docParentPath = docPathName.substring(0, lastIndex);

                    // si le chemin de son répertoire parent est dans la liste, on l'ajoute dans la liste des
                    // répertoires à supprimer
                    if (paraphModelFolderPathNameList.contains(docParentPath)) {
                        docRefParaphFolderToRemove.add(new IdRef(folder.getId()));
                    }
                }
            } else {
                // sinon on note son chemin dans la liste des chemins de répertoires conservés
                paraphCurrentFolderPathNameList.add(docPathName);
                paraphCurrentFolderDocRefList.put(docPathName, new IdRef(folder.getId()));
            }
        }

        // on supprime les répertoires
        final DocumentRef[] docsRef = docRefParaphFolderToRemove.toArray(
            new DocumentRef[docRefParaphFolderToRemove.size()]
        );
        LOGGER.info(session, EpgLogEnumImpl.DEL_REP_PARAPHEUR_TEC, docsRef);
        for (final DocumentRef docRef : docRefParaphFolderToRemove) {
            session.removeDocument(docRef);
        }

        // 2) on parcourt les répertoires du parapheur correspondant au nouveau type d'acte afin de les ajouter

        Boolean extraitObligatoire = false;
        Boolean acteObligatoire = false;

        for (final DocumentModel modelFolder : parapheurModelFolderRoots) {
            final String docPath = getParaphFolderModelIdentificationPath(modelFolder);

            DocumentRef idNodeExistant = paraphCurrentFolderDocRefList.get(docPath);

            //On crée le répertoire epreuve du parapheur si nécessaire
            if (idNodeExistant == null && SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EPREUVES_NAME.equals(docPath)) {
                DocumentModel folderEpreuves = createNewFolder(
                    session,
                    SolonEpgParapheurConstants.PARAPHEUR_MODEL_FOLDER_DOCUMENT_TYPE,
                    dossier.getParapheur(session).getDocument(),
                    SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EPREUVES_NAME
                );

                ParapheurFolder parapheurFolderEpreuves = folderEpreuves.getAdapter(ParapheurFolder.class);
                parapheurFolderEpreuves.setNbDocAccepteMax(SolonEpgParapheurConstants.NB_DOC_MAX_REP_EPREUVE);
                parapheurFolderEpreuves.save(session);
                idNodeExistant = parapheurFolderEpreuves.getDocument().getRef();
                paraphCurrentFolderPathNameList.add(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EPREUVES_NAME);
            }

            final DocumentModel nodeDoc = session.getDocument(idNodeExistant);

            // si le chemin du répertoire n'existe pas dans la liste on regarde le chemin de son répertoire parent
            // sinon on change les propriétés des parapheurFolders existants pour s'adapter au nouveau modele
            if (!paraphCurrentFolderPathNameList.contains(docPath)) {
                final int lastIndex = docPath.lastIndexOf("/");
                final String docParentPath = docPath.substring(0, lastIndex);

                // si le chemin de son répertoire parent est dans la liste, on ajoute le nouveau répertoire et ses fils
                if (paraphCurrentFolderPathNameList.contains(docParentPath)) {
                    session.copy(modelFolder.getRef(), paraphCurrentFolderDocRefList.get(docParentPath), null);
                }
            } else {
                // ParapheurNode node = modelFolder.getAdapter(ParapheurNode.class);
                final String[] schemas = modelFolder.getSchemas();
                for (final String schema : schemas) {
                    final Map<String, Object> data = modelFolder.getProperties(schema);
                    nodeDoc.setProperties(schema, data);
                    session.saveDocument(nodeDoc);
                }
            }
            final ParapheurFolder paraFolder = nodeDoc.getAdapter(ParapheurFolder.class);
            if (paraFolder.getEstNonVide()) {
                final String folderName = nodeDoc.getTitle();
                if (SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EXTRAIT_NAME.equals(folderName)) {
                    extraitObligatoire = true;
                } else if (SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME.equals(folderName)) {
                    acteObligatoire = true;
                }
            }
        }

        // on définit le type de fichier de référence (acte ou extrait)
        dossier.setIsActeReferenceForNumeroVersion((acteObligatoire && !extraitObligatoire));
        // on enregistre les modifications
        dossier.save(session);
        session.save();
        checkParapheurComplet(dossierDoc, session);
    }

    /**
     * récupère le chemin du répertoire dans l'arborescence en enlevant la partie commune aux autres répertoires du
     * Modèle de Parapheur
     *
     * @param paraphFolderModel
     * @return le chemin du répertoire dans l'arborescence en enlevant la partie commune aux autres répertoires du
     *         Modèle de Parapheur
     */
    protected String getParaphFolderModelIdentificationPath(final DocumentModel paraphFolderModel) {
        String docPath = paraphFolderModel.getPathAsString();
        int lastIndex = docPath.lastIndexOf("ModeleParapheur");
        docPath = docPath.substring(lastIndex);
        lastIndex = docPath.indexOf("/") + 1;
        return docPath.substring(lastIndex);
    }

    @Override
    protected SSTreeFolder getFolderImplFromDoc(final DocumentModel doc) {
        return doc.getAdapter(ParapheurFolder.class);
    }

    @Override
    protected SSTreeFile getFileImplFromDoc(final DocumentModel doc) {
        return doc.getAdapter(ParapheurFile.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ParapheurFile> getChildrenFile(final CoreSession session, final DocumentModel repertoireParent) {
        return (List<ParapheurFile>) super.getChildrenFile(session, repertoireParent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ParapheurFile> getChildrenFile(final CoreSession session, final DocumentRef repertoireParentRef) {
        return (List<ParapheurFile>) super.getChildrenFile(session, repertoireParentRef);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ParapheurFolder> getChildrenFolder(final CoreSession session, final DocumentModel repertoireParent) {
        return (List<ParapheurFolder>) super.getChildrenFolder(session, repertoireParent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ParapheurFolder> getChildrenFolder(final CoreSession session, final DocumentRef repertoireParentRef) {
        return (List<ParapheurFolder>) super.getChildrenFolder(session, repertoireParentRef);
    }

    @Override
    public List<DocumentModel> getEpreuvesFiles(final CoreSession session, final Dossier dossier) {
        // récupération du répertoire "Epreuves" à partir du chemin
        List<ParapheurFolder> dossiersNiveau1 = getChildrenFolder(session, dossier.getParapheur(session).getDocument());
        for (ParapheurFolder noeud : dossiersNiveau1) {
            if (SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EPREUVES_NAME.equals(noeud.getName())) {
                return getFilesFromParapheurFolder(session, dossier, noeud.getDocument());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public boolean checkNameUnicity(CoreSession session, String filename, String folderName, DocumentModel dossierDoc) {
        DocumentModel folderDoc = getParapheurFolder(dossierDoc, session, folderName);
        if (folderDoc != null) {
            return !session.exists(new PathRef(folderDoc.getPathAsString() + "/" + filename));
        }
        return true;
    }

    /**
     * Renvoie les fichiers du répertoire du parapheur donné.
     *
     * @param session
     *            session
     * @param dossier
     *            dossier
     * @param folder
     *            répertoire dont on récupère les fichiers
     * @return les fichiers du répertoire du parapheur donné.
     */
    protected List<DocumentModel> getFilesFromParapheurFolder(
        final CoreSession session,
        final Dossier dossier,
        final DocumentModel folder
    ) {
        // on récupère tous les documents fils du répertoire
        final StringBuilder query = new StringBuilder("SELECT l.ecm:uuid AS id FROM ");
        query.append(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE);
        query.append(" AS l WHERE isChildOf(l.ecm:uuid, select r.ecm:uuid from ");
        query.append(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE);
        query.append(" AS r WHERE r.ecm:uuid = ? ) = 1 and l." + NOT_DELETED);

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE,
            query.toString(),
            new String[] { folder.getId() }
        );
    }

    /**
     * Renvoi l'arborescence des répertoires mais seulement avec ceux qui ne sont pas vides
     *
     * @param session
     * @param dossier
     * @return
     */
    @Override
    public List<DocumentModel> getParapheurDocumentsWithoutEmptyFolders(
        final CoreSession session,
        final Dossier dossier
    ) {
        List<DocumentModel> fddDocList = getParapheurDocuments(session, dossier);
        List<DocumentModel> listFDDTrie = new ArrayList<>();
        for (DocumentModel fdd : fddDocList) {
            if (fdd.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE)) {
                List<DocumentModel> listChildrenFolder = session.getChildren(fdd.getRef());
                for (DocumentModel child : listChildrenFolder) {
                    if (child.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE)) {
                        listFDDTrie.add(fdd);
                        break;
                    }
                }
            } else {
                listFDDTrie.add(fdd);
            }
        }
        return listFDDTrie;
    }

    @Override
    public List<DocumentModel> getParapheurDocuments(final CoreSession session, final Dossier dossier) {
        final DocumentModel parapheurDoc = dossier.getParapheur(session).getDocument();

        String query =
            "Select d.ecm:uuid AS id From Document AS d WHERE d.ecm:parentId = ? AND testAcl(d.ecm:uuid) = 1";
        List<String> folderIds = QueryUtils.doUFNXQLQueryForIdsList(
            session,
            query,
            new Object[] { parapheurDoc.getId() }
        );

        return getAllParapheurDocument(session, folderIds);
    }

    /**
     * Récupère tout les document du fond de dossier à partir da la liste des identifiants des répertoires racines
     * visible.
     *
     * @param session
     * @return
     */
    private List<DocumentModel> getAllParapheurDocument(final CoreSession session, final List<String> folderIds) {
        // on récupère tous les documents fils des répertoires racines visibles
        final StringBuilder getAllFolderAndFileQuery = new StringBuilder("SELECT l.ecm:uuid AS id FROM Document AS l ")
            .append("WHERE isChildOf(l.ecm:uuid, select r.ecm:uuid from ParapheurFolder AS r WHERE r.ecm:uuid IN (")
            .append(StringHelper.getQuestionMark(folderIds.size()))
            .append(")) = 1 and l." + NOT_DELETED);

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            "Document",
            getAllFolderAndFileQuery.toString(),
            folderIds.toArray(new String[folderIds.size()])
        );
    }

    @Override
    public List<DocumentModel> getAllFilesFromParapheurForArchivage(
        final CoreSession session,
        final Dossier dossier,
        final String folderName
    ) {
        DocumentModel folderParapheur = getParapheurFolder(dossier.getDocument(), session, folderName);
        List<DocumentModel> listDocuments = new ArrayList<>();
        // On vérifie qu'on a bien récupéré le bon répertoire (au cas où)
        if (folderParapheur.getName().equals(folderName)) {
            try {
                List<DocumentModel> listDocsParapheurFolder = session.getChildren(folderParapheur.getRef());
                for (DocumentModel docParapheur : listDocsParapheurFolder) {
                    if (
                        docParapheur.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE) &&
                        !listDocuments.contains(docParapheur)
                    ) {
                        listDocuments.add(docParapheur);
                    }
                }
            } catch (NuxeoException e) {
                return listDocuments;
            }
        }

        return listDocuments;
    }

    @Override
    public List<DocumentModel> getAllFilesFromParapheurForArchivageNotInFolders(
        final CoreSession session,
        final Dossier dossier,
        final List<String> listFolders
    ) {
        List<DocumentModel> listDocuments = new ArrayList<>();
        List<DocumentModel> foldersParaheur = getAllParapheurFolder(session, dossier);
        for (DocumentModel folder : foldersParaheur) {
            if (!listFolders.contains(folder.getName())) {
                DocumentModel folderDoc = getParapheurFolder(dossier.getDocument(), session, folder.getName());
                try {
                    List<DocumentModel> listDocsParapheurFolder = session.getChildren(folderDoc.getRef());
                    for (DocumentModel docParapheur : listDocsParapheurFolder) {
                        if (
                            docParapheur.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE) &&
                            !listDocuments.contains(docParapheur)
                        ) {
                            listDocuments.add(docParapheur);
                        }
                    }
                } catch (NuxeoException e) {
                    continue;
                }
            }
        }

        return listDocuments;
    }

    /**
     * Retourne les documents d'une arborescence de répertoires d'un dossier
     * @param dossier Dossier danslequel les documents sont cherchés
     * @param folderName : Répertoire racine de l'arborescence dans laquelle chercher les fichiers
     */
    @Override
    public List<DocumentModel> getAllFilesFromParapheurAllFolder(final CoreSession session, final Dossier dossier) {
        List<DocumentModel> foldersParapheur = getAllParapheurFolder(session, dossier); // tous les répertoires de haut niveau
        List<DocumentModel> listDocuments = new ArrayList<>();
        for (DocumentModel folder : foldersParapheur) {
            List<DocumentModel> listDocsParapheur = new ArrayList<>();
            try { // recherche de tous les documents qui sont à l'intérieur
                listDocsParapheur = session.getChildren(folder.getRef());
                for (DocumentModel docParapheur : listDocsParapheur) {
                    getFilesFromHierarchy(session, listDocuments, docParapheur);
                }
            } catch (NuxeoException e) {
                continue;
            }
        }
        return listDocuments;
    }

    /**
     * Rempli une liste avec les fichiers d'une hierarchie de DocumentModel
     * @param listDocuments : liste à remplir
     * @param DocumentModel à parcourir
     */
    private void getFilesFromHierarchy(
        final CoreSession session,
        List<DocumentModel> listDocuments,
        DocumentModel document
    ) {
        if (
            document.getType().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE) &&
            !listDocuments.contains(document)
        ) {
            listDocuments.add(document);
        } else if (document.getType().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)) {
            List<DocumentModel> listChildren = session.getChildren(document.getRef());
            for (DocumentModel child : listChildren) {
                getFilesFromHierarchy(session, listDocuments, child);
            }
        }
    }

    @Override
    public List<DocumentModel> getAllParapheurFolder(final CoreSession session, final Dossier dossier) {
        // on récupère tous les répertoires du fond de dossier correspondant au type d'acte choisi
        final StringBuilder query = new StringBuilder("SELECT l.ecm:uuid AS id FROM ");
        query.append(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE);
        query.append(" AS l WHERE isChildOf(l.ecm:uuid, select r.ecm:uuid from ");
        query.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
        query.append(" AS r WHERE r.ecm:uuid = ?) = 1");

        final List<DocumentModel> docList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE,
            query.toString(),
            new String[] { dossier.getDocument().getId() }
        );

        return docList;
    }

    @Override
    public boolean hasActeIntegral(final CoreSession session, final Dossier dossier) {
        List<DocumentModel> parapheurFolder = getAllParapheurFolder(session, dossier);
        return parapheurFolder
            .stream()
            .filter(folder -> isActeIntegral(folder.getAdapter(ParapheurFolder.class), session))
            .anyMatch(folder -> CollectionUtils.isNotEmpty(getChildrenFile(session, folder)));
    }
}
