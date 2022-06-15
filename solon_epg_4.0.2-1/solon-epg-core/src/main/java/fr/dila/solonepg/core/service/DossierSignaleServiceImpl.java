package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.api.cases.DossiersSignales;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.service.DossierSignaleService;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.naiad.nuxeo.commons.core.util.StringUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.platform.userworkspace.api.UserWorkspaceService;

/**
 * @author ARN
 */
public class DossierSignaleServiceImpl implements DossierSignaleService {
    private static final long serialVersionUID = 1L;

    private static final STLogger LOGGER = STLogFactory.getLog(DossierSignaleServiceImpl.class);

    public DossierSignaleServiceImpl() {
        super();
    }

    /**
     *
     * Le répertoire courant
     */
    protected String currentRepertoire;

    protected UserWorkspaceService userWorkspaceService;

    @Override
    public int verserDossiers(final CoreSession session, final List<DocumentModel> docs) {
        final String userworkspacePath = getUserSpace(session);
        return addToDossierSignales(session, userworkspacePath, docs);
    }

    @Override
    public int verserDossier(final CoreSession session, final DocumentModel document) {
        final List<DocumentModel> documentToAddList = new ArrayList<>();
        documentToAddList.add(document);
        final String userworkspacePath = getUserSpace(session);
        return addToDossierSignales(session, userworkspacePath, documentToAddList);
    }

    @Override
    public void retirer(final CoreSession session, final List<DocumentModel> docs) {
        if (docs == null) {
            LOGGER.warn(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, "La liste des dossiers signalés est vide");
        }
        final String userworkspacePath = getUserSpace(session);
        // récupération du répertoire des dossiers signales de l'utilisateurs
        final DocumentModel dossiersSignales = getDossiersSignales(userworkspacePath, session);
        if (dossiersSignales == null) {
            LOGGER.warn(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, "document des dossiers signales");
        }
        if (docs != null) {
            for (final DocumentModel docDossier : docs) {
                // on récupère les dossiers, on doit récuèrer les dossiersSignale et les supprimer
                retirer(session, docDossier, dossiersSignales);
            }
        }
    }

    @Override
    public void retirer(final CoreSession session, final DocumentModel doc, final DocumentModel dossierSignalesDoc) {
        if (dossierSignalesDoc == null) {
            LOGGER.warn(
                session,
                STLogEnumImpl.FAIL_GET_DOCUMENT_FONC,
                "pas de dossier signalé trouvé pour le dossier !"
            );
            return;
        }

        final DossiersSignales dossiersSignales = dossierSignalesDoc.getAdapter(DossiersSignales.class);
        final List<String> dossiersSignalesId = dossiersSignales.getDossiersIds();
        if (dossiersSignalesId == null || dossiersSignalesId.size() < 1) {
            LOGGER.warn(
                session,
                STLogEnumImpl.FAIL_GET_DOCUMENT_FONC,
                "pas de dossiers trouvés pour les dossiers signalés !"
            );
            return;
        }
        dossiersSignalesId.remove(doc.getId());
        dossiersSignales.setDossiersIds(dossiersSignalesId);
        dossiersSignales.save(session);
    }

    @Override
    public void viderCorbeilleDossiersSignales(final CoreSession session) {
        final String userworkspacePath = getUserSpace(session);
        this.getDossiersSignales(userworkspacePath, session);
        final DocumentModel dossiersSignalesDoc = getDossiersSignales(getUserSpace(session), session);
        final DossiersSignales dossiersSignales = dossiersSignalesDoc.getAdapter(DossiersSignales.class);
        dossiersSignales.setDossiersIds(new ArrayList<String>());
        dossiersSignales.save(session);
    }

    /**
     * Initialise l'espace utilisateur et retourne l'emplacement de cet espace
     *
     * @param session
     */
    private String getUserSpace(final CoreSession session) {
        userWorkspaceService = STServiceLocator.getUserWorkspaceService();
        final DocumentModel docModel = userWorkspaceService.getCurrentUserPersonalWorkspace(session);
        return docModel.getPathAsString();
    }

    @Override
    public String getIdsDossierSignale(final CoreSession session) {
        try {
            final DocumentModel dossiersSignalesDoc = getDossiersSignales(getUserSpace(session), session);
            final DossiersSignales dossiersSignales = dossiersSignalesDoc.getAdapter(DossiersSignales.class);
            final List<String> list = dossiersSignales.getDossiersIds();

            return CollectionUtils.isEmpty(list) ? "''" : StringUtil.join(list, ",", "'");
        } catch (final NuxeoException e) {
            throw new NuxeoException("Impossible de trouver le répertoire contenant les dossiers signales");
        }
    }

    private int addToDossierSignales(
        final CoreSession session,
        final String userworkspacePath,
        final List<DocumentModel> documentToAddList
    ) {
        if (documentToAddList == null || documentToAddList.isEmpty()) {
            throw new NuxeoException("dossier ajoute vide");
        }

        final DocumentModel dossiersSignalesDoc = getDossiersSignales(userworkspacePath, session);
        final DossiersSignales dossiersSignales = dossiersSignalesDoc.getAdapter(DossiersSignales.class);
        final DocumentModel documentModel = documentToAddList.get(0);
        List<String> currentDocumentIdList = null;
        List<String> newDocumentIdList = null;
        if (documentModel.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
            currentDocumentIdList = dossiersSignales.getDossiersIds();

            final int number = currentDocumentIdList.size() + documentToAddList.size();
            // récupération du nombre maximal de dossiersSignales
            final ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
            final ParametrageApplication parametrageApplication = parametreApplicationService.getParametreApplicationDocument(
                session
            );

            final Long nbDossierMax = parametrageApplication.getNbDossiersSignales();
            if (nbDossierMax != null && number > nbDossierMax) {
                LOGGER.warn(
                    session,
                    STLogEnumImpl.FAIL_GET_DOCUMENT_FONC,
                    "nombre de dossier signalés maximum atteint !"
                );
                return -1;
            }

            newDocumentIdList = addDocumentToDossierSignales(session, currentDocumentIdList, documentToAddList);
            dossiersSignales.setDossiersIds(newDocumentIdList);
        } else {
            throw new NuxeoException("Type de document inconnu : " + documentModel.getType());
        }
        dossiersSignales.save(session);

        return newDocumentIdList.size() - currentDocumentIdList.size();
    }

    @Override
    public DocumentModel getDossiersSignales(final String userworkspacePath, final CoreSession session) {
        final DocumentRef refToDossier = new PathRef(userworkspacePath);

        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT ds.ecm:uuid as id FROM DossiersSignales as ds");
        queryBuilder.append(" WHERE ds.ecm:parentId = ? ");

        final Object params[] = new Object[] { session.getDocument(refToDossier).getId() };

        final List<DocumentModel> docList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            queryBuilder.toString(),
            params
        );

        DocumentModel doc = null;
        if (docList == null || docList.isEmpty()) {
            doc = session.createDocumentModel(DossierSolonEpgConstants.DOSSIERS_SIGNALES_DOCUMENT_TYPE);
            doc.setPathInfo(userworkspacePath, DossierSolonEpgConstants.DOSSIERS_SIGNALES_TITLE);
            doc = session.createDocument(doc);
        } else {
            doc = docList.get(0);
        }
        return doc;
    }

    /**
     *
     * @param session
     * @param currentDocumentIdList
     * @param documentModelList
     * @return
     */
    private List<String> addDocumentToDossierSignales(
        final CoreSession session,
        List<String> currentDocumentIdList,
        final List<DocumentModel> documentModelList
    ) {
        for (final DocumentModel documentModel : documentModelList) {
            final Set<String> set = new LinkedHashSet<>(currentDocumentIdList);
            final String idDossier = documentModel.getId();

            // on enleve le dossier
            set.remove(idDossier);

            currentDocumentIdList = new ArrayList<>();
            currentDocumentIdList.add(idDossier);
            currentDocumentIdList.addAll(set);
        }

        return currentDocumentIdList;
    }
}
