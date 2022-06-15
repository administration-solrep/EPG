package fr.dila.solonmgpp.api.service;

import fr.dila.solonmgpp.api.domain.CommunicationCourrier;
import java.io.File;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface CourrierProcessorService {
    File generateCourrierUnrestrictedFromDossierAndFiche(
        CoreSession session,
        String templateName,
        String idDossier,
        DocumentModel fiche
    );

    File generateCourrierUnrestricted(CoreSession session, String modeleName, CommunicationCourrier courrier);
}
