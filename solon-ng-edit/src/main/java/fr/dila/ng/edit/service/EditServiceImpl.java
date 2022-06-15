package fr.dila.ng.edit.service;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.documentmodel.FileSolonEpg;
import fr.dila.ss.api.service.DocumentRoutingService;
import fr.dila.ss.api.tree.SSTreeFile;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.services.actions.STActionsServiceLocator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.Blobs;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentNotFoundException;
import org.nuxeo.ecm.core.api.IdRef;

public class EditServiceImpl implements EditService {
    private static final STLogger LOGGER = STLogFactory.getLog(EditServiceImpl.class);

    @Override
    public Response getSolonEditVersion(CoreSession session) {
        STParametreService parametreService = STServiceLocator.getSTParametreService();
        String version = parametreService.getParametreValue(session, SolonEpgParametreConstant.SOLON_EDIT_VERSION_NAME);

        return Response.ok(new Gson().toJson(ImmutableMap.of("version", version))).build();
    }

    @Override
    public Response telechargerFichier(CoreSession session, String idFichier) {
        Optional<Response> response = checkIdFichier(session, idFichier, null);
        if (response.isPresent()) {
            return response.get();
        }

        DocumentModel fichierDoc = session.getDocument(new IdRef(idFichier));
        FileSolonEpg fichier = fichierDoc.getAdapter(FileSolonEpg.class);
        setFichierEnCoursModification(session, fichier);

        ImmutableMap<String, String> fileMap = ImmutableMap.of(
            "id",
            idFichier,
            "filename",
            fichier.getFilename(),
            "version",
            String.valueOf(getFileVersion(fichierDoc)),
            "data",
            encodeFileToBase64Binary(session, fichier.getContent().getFile())
        );
        ImmutableMap<String, ImmutableMap<String, String>> json = ImmutableMap.of("file", fileMap);

        return Response.ok(new Gson().toJson(json)).build();
    }

    private FileSolonEpg getFichier(CoreSession session, String idFichier) {
        DocumentModel fichierDoc = session.getDocument(new IdRef(idFichier));
        return fichierDoc.getAdapter(FileSolonEpg.class);
    }

    @Override
    public Response fichierEnCoursModification(CoreSession session, String idFichier, Long version) {
        Optional<Response> response = checkIdFichier(session, idFichier, version);
        if (response.isPresent()) {
            return response.get();
        }

        FileSolonEpg fichier = getFichier(session, idFichier);
        setFichierEnCoursModification(session, fichier);

        return Response.ok().build();
    }

    private void setFichierEnCoursModification(CoreSession session, FileSolonEpg fichier) {
        DocumentModel fichierDoc = fichier.getDocument();

        fichier.setEditing(true, true, true);
        session.saveDocument(fichierDoc);
    }

    @Override
    public Response abandonnerModificationFichier(CoreSession session, String idFichier) {
        Optional<Response> response = checkIdFichier(session, idFichier, null);
        if (response.isPresent()) {
            return response.get();
        }

        FileSolonEpg fichier = getFichier(session, idFichier);
        DocumentModel fichierDoc = fichier.getDocument();

        fichier.setEditing(false, true, true);
        session.saveDocument(fichierDoc);

        return Response.ok().build();
    }

    @Override
    public Response modifierFichier(
        CoreSession session,
        String idFichier,
        long version,
        String fileName,
        InputStream uploadedInputStream
    ) {
        Optional<Response> response = checkIdFichier(session, idFichier, version);
        if (response.isPresent()) {
            return response.get();
        }

        IdRef idFichierRef = new IdRef(idFichier);
        FileSolonEpg fichier = getFichier(session, idFichier);
        DocumentModel folderDoc = session.getParentDocument(idFichierRef);
        DocumentModel dossierDoc = session.getDocument(new IdRef(fichier.getRelatedDocument()));

        String nomFichier = fichier.getFilename();
        try {
            fichier.setContent(Blobs.createBlob(uploadedInputStream));
            fichier.setFilename(nomFichier);
        } catch (IOException e) {
            LOGGER.error(session, STLogEnumImpl.LOG_INFO_TEC, e);
            return Response
                .serverError()
                .entity(
                    toJson(
                        Status.INTERNAL_SERVER_ERROR,
                        String.format(
                            "Impossible de mettre à jour, dans EPG, le fichier %s, contactez un administrateur fonctionnel.",
                            nomFichier
                        )
                    )
                )
                .build();
        }

        fichier.setEditing(false);
        session.saveDocument(fichier.getDocument());
        final JournalService journalService = STServiceLocator.getJournalService();
        String message = String.format(
            "Modification du fichier : '%s' dans le répertoire '%s'",
            nomFichier,
            folderDoc.getTitle()
        );
        boolean isFondDossierFile = VocabularyConstants.FILETYPEID_FONDDOSSIER == fichier.getFiletypeId();
        String category = isFondDossierFile ? STEventConstant.CATEGORY_FDD : STEventConstant.CATEGORY_PARAPHEUR;
        String eventName = isFondDossierFile
            ? SolonEpgEventConstant.UPDATE_FILE_FDD
            : SolonEpgEventConstant.UPDATE_FILE_PARAPHEUR;
        journalService.journaliserAction(session, dossierDoc, eventName, message, category);

        return Response.status(Status.OK).entity(toJson(Status.OK, "Le fichier a été correctement uploadé.")).build();
    }

    Optional<Response> checkIdFichier(CoreSession session, String idFichier, Long version) {
        IdRef fichierRef = new IdRef(idFichier);
        Optional<Response> repErreur = checkFichierExiste(session, fichierRef);

        if (!repErreur.isPresent()) {
            DocumentModel fichierDoc = session.getDocument(fichierRef);
            // Une étape pour avis CE est en cours
            try {
                DocumentModel dossierDoc = session.getDocument(getDossierDocRef(fichierDoc));
                repErreur = checkVerrouDossier(session, dossierDoc, fichierDoc, version);
                if (!repErreur.isPresent()) {
                    repErreur = checkPasEtapeAvisCe(session, dossierDoc);
                }
            } catch (DocumentNotFoundException exception) {
                LOGGER.warn(
                    session,
                    STLogEnumImpl.LOG_INFO_TEC,
                    String.format("Le fichier %s n'a pas de dossier", fichierRef.toString())
                );
                repErreur =
                    Optional.of(
                        Response
                            .serverError()
                            .entity(
                                toJson(
                                    Status.INTERNAL_SERVER_ERROR,
                                    String.format(
                                        "Le fichier %s n'est pas rattaché à un dossier d'EPG.",
                                        getFileName(fichierDoc)
                                    )
                                )
                            )
                            .build()
                    );
            }
        }

        return repErreur;
    }

    private String getFileName(DocumentModel fichierDoc) {
        return fichierDoc.getAdapter(FileSolonEpg.class).getFilename();
    }

    private Long getFileVersion(DocumentModel fichierDoc) {
        return fichierDoc.getAdapter(SSTreeFile.class).getMajorVersion();
    }

    private IdRef getDossierDocRef(DocumentModel fichierDoc) {
        return new IdRef(fichierDoc.getAdapter(FileSolonEpg.class).getRelatedDocument());
    }

    private Optional<Response> checkFichierExiste(CoreSession session, IdRef fichierRef) {
        if (session.exists(fichierRef)) {
            return Optional.empty();
        }

        return Optional.of(
            Response
                .status(Status.NOT_FOUND)
                .entity(toJson(Status.NOT_FOUND, "Le fichier en cours de traitement n'existe pas dans EPG."))
                .build()
        );
    }

    private Optional<Response> checkVerrouDossier(
        CoreSession session,
        DocumentModel dossierDoc,
        DocumentModel fichierDoc,
        Long version
    ) {
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        String lockOwnerName = STActionsServiceLocator.getSTLockActionService().getLockOwnerName(dossierDoc, session);

        boolean lockedByOwner = Objects.equals(lockOwnerName, session.getPrincipal().getName());

        boolean newVersionAvailable = false;
        if (version != null) {
            newVersionAvailable = fichierDoc.getAdapter(SSTreeFile.class).getMajorVersion() > version;
        }

        String errorMessage = "";
        if (!lockedByOwner) {
            errorMessage = "Vous ne possédez pas le verrou sur le dossier %s auquel est rattaché le fichier %s.";
        }
        if (newVersionAvailable) {
            errorMessage =
                Stream
                    .of(errorMessage, "Une nouvelle version du fichier existe dans le dossier.")
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.joining(" "));
        }

        if (errorMessage.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
            Response
                .status(Status.FORBIDDEN)
                .entity(
                    toJson(
                        Status.FORBIDDEN,
                        String.format(errorMessage, dossier.getNumeroNor(), getFileName(fichierDoc))
                    )
                )
                .build()
        );
    }

    private Optional<Response> checkPasEtapeAvisCe(CoreSession session, DocumentModel dossierDoc) {
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        DocumentRoutingService documentRoutingService = SSServiceLocator.getDocumentRoutingService();

        if (
            !documentRoutingService.hasEtapeEnCoursOfType(
                session,
                dossier,
                VocabularyConstants.ROUTING_TASK_TYPE_AVIS_CE
            )
        ) {
            return Optional.empty();
        }

        String errorMessage =
            "Une étape 'Pour avis CE' est en cours pour le dossier %s, la modification des fichiers du parapheur n'est pas autorisée.";

        return Optional.of(
            Response
                .status(Status.FORBIDDEN)
                .entity(toJson(Status.FORBIDDEN, String.format(errorMessage, dossier.getNumeroNor())))
                .build()
        );
    }

    private String toJson(Status status, String message) {
        return new Gson().toJson(ImmutableMap.of("status", String.valueOf(status.getStatusCode()), "message", message));
    }

    private String encodeFileToBase64Binary(CoreSession session, File file) {
        try {
            return Base64.encodeBase64String(FileUtils.readFileToByteArray(file));
        } catch (IOException e) {
            LOGGER.warn(session, STLogEnumImpl.LOG_INFO_TEC, e);
            return "";
        }
    }
}
