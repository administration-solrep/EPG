package fr.dila.solonepg.core.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.platform.userworkspace.api.UserWorkspaceService;

import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.api.cases.DossiersSignales;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.service.DossierSignaleService;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;

/**
 * @author ARN
 */
public class DossierSignaleServiceImpl implements DossierSignaleService {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(DossierSignaleServiceImpl.class);

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
    public int verserDossiers(final CoreSession session, final List<DocumentModel> docs) throws ClientException {
        final String userworkspacePath = getUserSpace(session);
        return addToDossierSignles(session, userworkspacePath, docs);
    }

    @Override
    public int verserDossier(final CoreSession session, final DocumentModel document) throws ClientException {

        final List<DocumentModel> documentToAddList = new ArrayList<DocumentModel>();
        documentToAddList.add(document);
        final String userworkspacePath = getUserSpace(session);
        return addToDossierSignles(session, userworkspacePath, documentToAddList);
    }

    @Override
    public void retirer(final CoreSession session, final List<DocumentModel> docs) throws ClientException {
        if (docs == null) {
            log.warn("dossiers signales list is empty");
        }
        final String userworkspacePath = getUserSpace(session);
        // récupération du répertoire des dossiers signales de l'utilisateurs
        final DocumentModel dossiersSignales = getDossiersSignales(userworkspacePath, session);
        if (dossiersSignales == null) {
            log.warn("document des dossiers signales list not found");
        }
        if (docs != null) {
            for (final DocumentModel docDossier : docs) {
                // on récupère les dossiers, on doit récuèrer les dossiersSignale et les supprimer
                retirer(session, docDossier, dossiersSignales);
            }
        }
    }

    @Override
    public void retirer(final CoreSession session, final DocumentModel doc, final DocumentModel dossierSignalesDoc) throws ClientException {

        if (dossierSignalesDoc == null) {
            log.warn("pas de dossier signalé trouvé pour le dossier !");
            return;
        }

        final DossiersSignales dossiersSignales = dossierSignalesDoc.getAdapter(DossiersSignales.class);
        final List<String> dossiersSignalesId = dossiersSignales.getDossiersIds();
        if (dossiersSignalesId == null || dossiersSignalesId.size() < 1) {
            log.warn("pas de dossier signalé trouvé pour le dossier !");
            return;
        }
        dossiersSignalesId.remove(doc.getId());
        dossiersSignales.setDossiersIds(dossiersSignalesId);
        dossiersSignales.save(session);

    }

    //    // TODO à utiliser pour clore tous les dossiers signalés lorsque le dossier passe à l'état publié
    //    @Override
    //    public void deleteAllDossierSignaleUnrestricted(CoreSession session, DocumentModel doc) throws ClientException {
    //        // on récupère le dossier signale
    //        String query = String.format(GET_ALL_DOSSIER_SIGNALE_QUERY, DossierSolonEpgConstants.DOSSIER_SIGNALE_DOCUMENT_TYPE, doc.getId());
    //        DocumentModelList dossierProxy = new UnrestrictedQueryRunner(session, query).findAll();
    //        if (dossierProxy != null && dossierProxy.size() > 0) {
    //            for (DocumentModel dossierSignale : dossierProxy) {
    //                session.removeDocument(dossierSignale.getRef());
    //            }
    //        } else {
    //            log.warn("dossier signalé not found");
    //        }
    //    }

    @Override
    public void viderCorbeilleDossiersSignales(final CoreSession session) throws ClientException {
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
     * @throws ClientException
     */
    private String getUserSpace(final CoreSession session) throws ClientException {
        userWorkspaceService = STServiceLocator.getUserWorkspaceService();
        final DocumentModel docModel = userWorkspaceService.getCurrentUserPersonalWorkspace(session, null);
        return docModel.getPathAsString();
    }

    @Override
    public String getQueryDossierSignale(final CoreSession session) throws ClientException {
        try {

            final DocumentModel dossiersSignalesDoc = getDossiersSignales(getUserSpace(session), session);
            final DossiersSignales dossiersSignales = dossiersSignalesDoc.getAdapter(DossiersSignales.class);
            final List<String> list = dossiersSignales.getDossiersIds();

            final StringBuilder query = new StringBuilder("SELECT d.ecm:uuid as id FROM Dossier as d");
            query.append(" WHERE d.ecm:uuid");
            query.append(" IN ('");
            query.append(StringUtils.join(list, "','"));
            query.append("') ");
            query.append("and testAcl(d.ecm:uuid) = 1");

            final List<String> ids = QueryUtils.doUFNXQLQueryForIdsList(session, query.toString(), null);
            dossiersSignales.setDossiersIds(ids);
            dossiersSignales.save(session);

            return query.toString();
        } catch (final ClientException e) {
            throw new ClientException("Impossible de trouver le répertoire contenant les dossiers signales");
        }
    }

    public int addToDossierSignles(final CoreSession session, final String userworkspacePath, final List<DocumentModel> documentToAddList)
            throws ClientException {
        if (documentToAddList == null || documentToAddList.isEmpty()) {
            throw new ClientException("dossier ajoute vide");
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
            final ParametrageApplication parametrageApplication = parametreApplicationService.getParametreApplicationDocument(session);

            final Long nbDossierMax = parametrageApplication.getNbDossiersSignales();
            if (nbDossierMax != null && number >= nbDossierMax) {
                log.warn("nombre de dossier signalés maximum atteint !");
                return -1;
            }

            newDocumentIdList = addDocumentToDossierSignales(session, currentDocumentIdList, documentToAddList);
            dossiersSignales.setDossiersIds(newDocumentIdList);
        } else {
            throw new ClientException("Type de document inconnu : " + documentModel.getType());
        }
        dossiersSignales.save(session);

        return newDocumentIdList.size() - currentDocumentIdList.size();

    }

    @Override
    public DocumentModel getDossiersSignales(final String userworkspacePath, final CoreSession session) throws ClientException {

        final DocumentRef refToDossier = new PathRef(userworkspacePath);

        final StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT ds.ecm:uuid as id FROM DossiersSignales as ds");
        queryBuilder.append(" WHERE ds.ecm:parentId = ? ");

        final Object params[] = new Object[] { session.getDocument(refToDossier).getId() };

        final List<DocumentModel> docList = QueryUtils.doUnrestrictedUFNXQLQueryAndFetchForDocuments(session, "DossiersSignales",
                queryBuilder.toString(), params);

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
     * @throws ClientException
     */
    private List<String> addDocumentToDossierSignales(final CoreSession session, List<String> currentDocumentIdList,
            final List<DocumentModel> documentModelList) throws ClientException {

        for (final DocumentModel documentModel : documentModelList) {

            final Set<String> set = new LinkedHashSet<String>(currentDocumentIdList);
            final String idDossier = documentModel.getId();

            // on enleve le dossier
            set.remove(idDossier);

            currentDocumentIdList = new ArrayList<String>();
            currentDocumentIdList.add(idDossier);
            currentDocumentIdList.addAll(set);

        }

        return currentDocumentIdList;
    }

}
