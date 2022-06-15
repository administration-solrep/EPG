package fr.dila.solonmgpp.api.service;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonmgpp.api.domain.CommunicationCourrier;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

public interface CommunicationCourrierService {
    CommunicationCourrier createCommunicationCourrier(
        CoreSession session,
        Dossier dossier,
        DocumentModel fiche,
        NuxeoPrincipal actingUser
    );
}
