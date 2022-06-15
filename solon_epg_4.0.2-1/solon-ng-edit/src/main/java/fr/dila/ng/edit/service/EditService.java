package fr.dila.ng.edit.service;

import java.io.InputStream;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.core.api.CoreSession;

public interface EditService {
    Response getSolonEditVersion(CoreSession session);

    Response telechargerFichier(CoreSession session, String idFichier);

    Response fichierEnCoursModification(CoreSession session, String idFichier, Long version);

    Response modifierFichier(
        CoreSession session,
        String idFichier,
        long version,
        String fileName,
        InputStream uploadedInputStream
    );

    Response abandonnerModificationFichier(CoreSession session, String idFichier);
}
