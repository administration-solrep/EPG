package fr.dila.solonmgpp.core.service;

import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.FileSolonMgpp;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.FondDossierService;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.solonmgpp.core.domain.FileSolonMgppImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implementation de {@link FondDossierService}
 *
 * @author asatre
 *
 */
public class FondDossierServiceImpl implements FondDossierService, Serializable {
    private static final long serialVersionUID = 7674749988961738312L;

    private static final String SUFFIXE_COMMUNICATION_0 = "_00000";

    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(FondDossierServiceImpl.class);

    @Override
    public List<DocumentModel> findFileFor(final CoreSession session, final DocumentModel doc) {
        if (doc != null) {
            final StringBuilder queryBuilder = new StringBuilder("SELECT f.ecm:uuid as id FROM ");
            queryBuilder.append(FileSolonMgppImpl.FILE_SOLON_MGPP_DOC_TYPE);
            queryBuilder.append(" as f WHERE f.");
            queryBuilder.append(FileSolonMgppImpl.FILE_SOLON_MGPP_PREFIX);
            queryBuilder.append(":");
            queryBuilder.append(FileSolonMgppImpl.ID_FICHE);
            queryBuilder.append(" = ? ");
            queryBuilder.append(" ORDER BY f.dc:created DESC ");

            return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
                session,
                FileSolonMgppImpl.FILE_SOLON_MGPP_DOC_TYPE,
                queryBuilder.toString(),
                new Object[] { doc.getId() }
            );
        }
        return Collections.emptyList();
    }

    @Override
    public void addFileFor(final CoreSession session, final DocumentModel doc, final Blob blob) {
        final DocumentModel docModel = session.createDocumentModel(
            doc.getPathAsString(),
            "file-" + UUID.randomUUID(),
            FileSolonMgppImpl.FILE_SOLON_MGPP_DOC_TYPE
        );

        final FileSolonMgpp file = docModel.getAdapter(FileSolonMgpp.class);

        file.setContent(blob);
        file.setFilename(blob.getFilename());
        file.setIdFiche(doc.getId());

        session.createDocument(docModel);

        if (
            doc.hasSchema(FichePresentationOEPImpl.SCHEMA) &&
            SolonMgppServiceLocator
                .getEvenementService()
                .findEvenement(
                    (doc.getAdapter(FichePresentationOEP.class)).getIdDossier() + SUFFIXE_COMMUNICATION_0,
                    null,
                    session,
                    false,
                    false
                ) ==
            null
        ) {
            // creation d'un EVT49 suite a l'ajout d'un fichier pour un OEP
            SolonMgppServiceLocator
                .getEvenementService()
                .createEvenementEppEvt49Brouillon(session, doc.getAdapter(FichePresentationOEP.class));
        } else if (
            doc.hasSchema(FichePresentationAVI.SCHEMA) &&
            SolonMgppServiceLocator
                .getEvenementService()
                .findEvenement(
                    (doc.getAdapter(FichePresentationAVI.class)).getIdDossier() + SUFFIXE_COMMUNICATION_0,
                    null,
                    session,
                    false,
                    false
                ) ==
            null
        ) {
            // creation d'un EVT32 suite a l'ajout d'un fichier pour un AVI
            SolonMgppServiceLocator
                .getEvenementService()
                .createEvenementEppEvt32Brouillon(session, doc.getAdapter(FichePresentationAVI.class));
        } else if (
            doc.hasSchema(FichePresentationAUD.SCHEMA) &&
            SolonMgppServiceLocator
                .getEvenementService()
                .findEvenement(
                    (doc.getAdapter(FichePresentationAUD.class)).getIdDossier() + SUFFIXE_COMMUNICATION_0,
                    null,
                    session,
                    false,
                    false
                ) ==
            null
        ) {
            //RG-AUD-CRE-06
            SolonMgppServiceLocator
                .getEvenementService()
                .createEvenementEppAUD01Brouillon(session, doc.getAdapter(FichePresentationAUD.class));
        } else if (
            doc.hasSchema(FichePresentationDOC.SCHEMA) &&
            SolonMgppServiceLocator
                .getEvenementService()
                .findEvenement(
                    (doc.getAdapter(FichePresentationDOC.class)).getIdDossier() + SUFFIXE_COMMUNICATION_0,
                    null,
                    session,
                    false,
                    false
                ) ==
            null
        ) {
            SolonMgppServiceLocator
                .getEvenementService()
                .createEvenementEppDOC01Brouillon(session, doc.getAdapter(FichePresentationDOC.class));
        }
    }

    @Override
    public void removeFileFor(final CoreSession session, final DocumentModel file) {
        LOGGER.info(session, MgppLogEnumImpl.DEL_FILE_MGPP_TEC, file);
        session.removeDocument(file.getRef());
    }
}
