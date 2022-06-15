package fr.dila.solonmgpp.core.service;

import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_SCHEMA;
import static fr.dila.solonmgpp.core.service.SolonMgppServiceLocator.getCommunicationCourrierService;
import static fr.dila.solonmgpp.core.service.SolonMgppServiceLocator.getModeleCourrierService;
import static fr.dila.st.core.query.QueryHelper.getDocument;
import static java.util.Objects.requireNonNull;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonmgpp.api.domain.CommunicationCourrier;
import fr.dila.solonmgpp.api.domain.ModeleCourrier;
import fr.dila.solonmgpp.api.service.CommunicationCourrierService;
import fr.dila.solonmgpp.api.service.CourrierProcessorService;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import java.io.File;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreInstance;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.template.api.adapters.TemplateBasedDocument;

public class CourrierProcessorServiceImpl implements CourrierProcessorService {
    private static final STLogger LOGGER = STLogFactory.getLog(CourrierProcessorServiceImpl.class);

    @Override
    public File generateCourrierUnrestrictedFromDossierAndFiche(
        CoreSession session,
        String modeleName,
        String idDossier,
        DocumentModel fiche
    ) {
        requireNonNull(session);
        requireNonNull(modeleName);

        return CoreInstance.doPrivileged(
            session,
            uSession -> {
                return generateCourrierFromDossierAndFiche(
                    uSession,
                    modeleName,
                    idDossier,
                    fiche,
                    session.getPrincipal()
                );
            }
        );
    }

    @Override
    public File generateCourrierUnrestricted(CoreSession session, String modeleName, CommunicationCourrier courrier) {
        requireNonNull(session);
        requireNonNull(modeleName);

        return CoreInstance.doPrivileged(
            session,
            uSession -> {
                return generateCourrier(uSession, modeleName, courrier.getDocument());
            }
        );
    }

    private File generateCourrier(CoreSession session, String templateName, DocumentModel courrierDoc) {
        try {
            TemplateBasedDocument templateBasedDoc = courrierDoc.getAdapter(TemplateBasedDocument.class);
            ModeleCourrier modele = getModeleCourrierService()
                .getModeleCourrierFromTemplateNameOrThrow(session, templateName);
            templateBasedDoc.setTemplate(modele.getDocument(), false);

            LOGGER.info(session, STLogEnumImpl.LOG_INFO_TEC, "Génération du fichier à télécharger");
            return templateBasedDoc.renderWithTemplate(templateName).getFile();
        } finally {
            if (courrierDoc.getId() != null) {
                session.removeDocument(courrierDoc.getRef());
            }
        }
    }

    private File generateCourrierFromDossierAndFiche(
        CoreSession session,
        String modeleName,
        String idDossier,
        DocumentModel fiche,
        NuxeoPrincipal actingUser
    ) {
        Dossier dossier = null;

        if (StringUtils.isNotBlank(idDossier)) {
            dossier = getDocument(session, idDossier, DOSSIER_SCHEMA).getAdapter(Dossier.class);
        }

        CommunicationCourrierService ccService = getCommunicationCourrierService();
        CommunicationCourrier commCourrier = ccService.createCommunicationCourrier(session, dossier, fiche, actingUser);
        DocumentModel commCourrierDoc = commCourrier.getDocument();

        commCourrierDoc = session.createDocument(commCourrierDoc);
        return generateCourrier(session, modeleName, commCourrierDoc);
    }
}
