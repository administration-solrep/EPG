package fr.dila.solonepg.core.service;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgDragAndDropConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.dto.CopyFileStatusDTO;
import fr.dila.solonepg.api.parapheur.ParapheurFile;
import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.service.DndService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.ss.api.tree.SSTreeFile;
import fr.dila.ss.api.tree.SSTreeFolder;
import fr.dila.ss.core.service.SSTreeServiceImpl;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.service.STServiceLocator;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

public class DndServiceImpl extends SSTreeServiceImpl implements DndService {
    /**
     *
     */
    private static final long serialVersionUID = 6376858474634735227L;
    private static final List<String> PREFIXES = ImmutableList.of("docRef:", "docClipboardRef:", "docRefTarget:");
    private static final Log LOG = LogFactory.getLog(DndServiceImpl.class);

    public DndServiceImpl() {
        // do nothing
    }

    @Override
    public CopyFileStatusDTO processMove(
            CoreSession session,
            String dndDocId,
            String dndContainerId,
            NuxeoPrincipal currentUser,
            DocumentModel currentDocument
    ) {
        final IdRef docId = extractId(dndDocId);
        final IdRef newContainerId = extractId(dndContainerId);
        DocumentModel doc;
        DocumentModel newContainerDoc;
        CopyFileStatusDTO status = new CopyFileStatusDTO(false, SolonEpgDragAndDropConstants.ERREUR_DND);

        try {
            newContainerDoc = session.getDocument(newContainerId);
            doc = session.getDocument(docId);
            if (doc == null) {
                return new CopyFileStatusDTO(false, SolonEpgDragAndDropConstants.ERREUR_RECUPERATION_DOSSIER);
            }
            Dossier dossier = currentDocument.getAdapter(Dossier.class);
            // On regarde si le dossier de destination et de provenance ne sont pas le même
            if (doc.getParentRef().equals(newContainerId)) {
                return new CopyFileStatusDTO(false, SolonEpgDragAndDropConstants.ERREUR_REPERTOIRE_IDENTIQUE);
            }
            // On regarde si le document et le container sont compatibles
            final String compatibility = isCompatible(session, doc, newContainerDoc);
            if (!compatibility.equals(SolonEpgDragAndDropConstants.DND_COMPATIBILITE_ERROR)) {
                if (newContainerDoc != null) {
                    // Là on regarde dans quel folder est le document
                    SSTreeFile fichier = doc.getAdapter(SSTreeFile.class);
                    SSTreeFolder treeFoder = newContainerDoc.getAdapter(SSTreeFolder.class);
                    ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
                    FondDeDossierService fddService = SolonEpgServiceLocator.getFondDeDossierService();

                    if (newContainerDoc.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE)) {
                        ParapheurFolder fichierParentFolder = newContainerDoc.getAdapter(ParapheurFolder.class);
                        // calcul pour savoir si le dossier de destination est plein
                        if (isFolderFullParapheur(fichierParentFolder, session)) {
                            return new CopyFileStatusDTO(
                                    false,
                                    SolonEpgDragAndDropConstants.ERREUR_TRANSFERT_REPERTOIRE_PLEIN
                            );
                        }
                        List<String> formatsAcceptesParapheur = fichierParentFolder.getFormatsAutorises();
                        if (
                                !formatsAcceptesParapheur.isEmpty() &&
                                        !formatsAcceptesParapheur.contains(FilenameUtils.getExtension(fichier.getTitle()))
                        ) {
                            return new CopyFileStatusDTO(false, SolonEpgDragAndDropConstants.ERREUR_TYPE_FICHIER);
                        }

                        // On vérifie si le fichier existe déjà pour ne pas en ajouter un du même nom : contrainte STILA
                        if (
                                !parapheurService.checkNameUnicity(
                                        session,
                                        fichier.getName(),
                                        treeFoder.getName(),
                                        currentDocument
                                )
                        ) {
                            return new CopyFileStatusDTO(false, SolonEpgDragAndDropConstants.ERREUR_NOM_FICHIER);
                        }
                    }
                    if (
                            newContainerDoc
                                    .getType()
                                    .equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)
                    ) {
                        SSTreeFolder treeFolder = newContainerDoc.getAdapter(SSTreeFolder.class);
                        List<String> formatsAcceptesFDD = dossier.getFondDeDossier(session).getFormatsAutorises();
                        if (!isEpreuveAndUnauthorized(treeFolder, currentUser)) {
                            return new CopyFileStatusDTO(false, SolonEpgDragAndDropConstants.ERREUR_DROITS_REPERTOIRE);
                        }
                        // On vérifie si le fichier existe déjà pour ne pas en ajouter un du même nom : contrainte STILA
                        if (
                                !fddService.checkNameUnicity(session, fichier.getName(), treeFoder.getId(), currentDocument)
                        ) {
                            return new CopyFileStatusDTO(false, SolonEpgDragAndDropConstants.ERREUR_NOM_FICHIER);
                        }

                        if (
                                !formatsAcceptesFDD.isEmpty() &&
                                        !formatsAcceptesFDD.contains(FilenameUtils.getExtension(fichier.getTitle()))
                        ) {
                            return new CopyFileStatusDTO(false, SolonEpgDragAndDropConstants.ERREUR_TYPE_FICHIER);
                        }
                    }
                    String nomFichier = fichier.getTitle();
                    List<DocumentRef> listRefDocument = new ArrayList<DocumentRef>();
                    listRefDocument.add(doc.getRef());
                    // Déplacement
                    session.move(listRefDocument, newContainerDoc.getRef());
                    session.saveDocument(newContainerDoc);
                    session.saveDocument(doc);
                    // Mise a jour du parapheur
                    parapheurService.checkParapheurComplet(currentDocument, session);
                    // journalisation de l'action dans les logs
                    final JournalService journalService = STServiceLocator.getJournalService();
                    String eventName = "";
                    String commentaire = "";
                    String categorie = "";
                    if (newContainerDoc.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE)) {
                        eventName = SolonEpgEventConstant.MOVE_FILE_PARAPHEUR;
                        commentaire =
                                SolonEpgEventConstant.COMMENT_MOVE_FILE_PARAPHEUR +
                                        fichier.getName() +
                                        "' dans le répertoire '" +
                                        newContainerDoc.getTitle() +
                                        "'";
                        categorie = STEventConstant.CATEGORY_PARAPHEUR;
                    } else if (
                            newContainerDoc
                                    .getType()
                                    .equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)
                    ) {
                        eventName = SolonEpgEventConstant.MOVE_FILE_FDD;
                        commentaire =
                                SolonEpgEventConstant.COMMENT_MOVE_FILE_FDD +
                                        fichier.getName() +
                                        "' dans le répertoire '" +
                                        newContainerDoc.getTitle() +
                                        "'";
                        categorie = STEventConstant.CATEGORY_FDD;
                    }
                    journalService.journaliserAction(session, currentDocument, eventName, commentaire, categorie);
                    return new CopyFileStatusDTO(
                            true,
                            "Le document " +
                                    nomFichier +
                                    " a bien été déplacé vers le répertoire " +
                                    newContainerDoc.getTitle()
                    );
                } else {
                    if (newContainerDoc == null) {
                        return new CopyFileStatusDTO(false, SolonEpgDragAndDropConstants.ERREUR_RECUPERATION_ELEMENT);
                    }
                }
            } else {
                // Affichage d'un message pour dire que ce n'est pas possible
                return new CopyFileStatusDTO(false, SolonEpgDragAndDropConstants.DND_TYPES_NON_COMPATIBLES);
            }
        } catch (NuxeoException e) {
            LOG.error(
                    "Une erreur s'est produite lors du déplacement du document " +
                            dndDocId +
                            " vers le répertoire " +
                            dndContainerId,
                    e
            );
            return new CopyFileStatusDTO(false, SolonEpgDragAndDropConstants.ERREUR_DND);
        }
        return status;
    }

    private IdRef extractId(String input) {
        for (String prefix : PREFIXES) {
            if (input.startsWith(prefix)) {
                return new IdRef(input.substring(prefix.length()));
            }
        }
        return new IdRef(input);
    }

    private String isCompatible(CoreSession session, DocumentModel doc, DocumentModel container) {
        // Compatibilité vers FDD
        if (
                doc.getType().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE) &&
                        container.getType().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE)
        ) {
            return SolonEpgDragAndDropConstants.DND_COMPATIBILITE_SANS_CONVERSION;
        }
        // Compatibilité vers Parapheur
        if (
                doc.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE) &&
                        container.getType().equals(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE)
        ) {
            return SolonEpgDragAndDropConstants.DND_COMPATIBILITE_SANS_CONVERSION;
        }
        return SolonEpgDragAndDropConstants.DND_COMPATIBILITE_ERROR;
    }

    public Boolean isFolderFullParapheur(ParapheurFolder folder, CoreSession documentManager) {
        if (folder != null) {
            // on récupère le nombre maximum de fichiers acceptes
            Long ndDocAcceptemax = folder.getNbDocAccepteMax();
            int nbFile = 0;
            try {
                nbFile = getChildrenFile(folder.getDocument(), documentManager).size();
            } catch (NuxeoException e) {
                LOG.error(
                        "Une erreur s'est produire lors de la vérification de la place disponible dans le parapheur",
                        e
                );
            }
            // on vérifie la possibilité d'ajout de document
            return (ndDocAcceptemax != null && nbFile >= ndDocAcceptemax.intValue());
        }
        return false;
    }

    public Boolean isEpreuveAndUnauthorized(SSTreeFolder folder, NuxeoPrincipal currentUser) {
        if (folder == null) {
            return false;
        }
        // si l'étape n'est pas "pour fourniture d'épreuves" on ne se pose pas de question
        if (!folder.getTitle().equals(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES)) {
            return true;
        }
        // test si l'utilisateur est autorisé à créer l'étape pour fourniture d'épreuve
        final List<String> groups = currentUser.getGroups();
        return groups.contains(SolonEpgBaseFunctionConstant.ETAPE_FOURNITURE_EPREUVE_CREATOR);
    }

    @SuppressWarnings("unchecked")
    protected List<ParapheurFile> getChildrenFile(DocumentModel parentDoc, CoreSession documentManager) {
        return (List<ParapheurFile>) SolonEpgServiceLocator
                .getParapheurService()
                .getChildrenFile(documentManager, parentDoc);
    }
}