package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.administration.ParametrageAdamant;
import fr.dila.st.api.service.LogDocumentUpdateService;
import java.io.Serializable;
import org.nuxeo.ecm.core.api.CoreSession;

public interface ParametrageAdamantService extends LogDocumentUpdateService, Serializable {
    /**
     * Récupère le document contenant les informations sur le paramètrage Adamant.
     *
     * @param session
     * @return
     */
    ParametrageAdamant getParametrageAdamantDocument(CoreSession session);
}
