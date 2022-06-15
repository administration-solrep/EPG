package fr.dila.solonmgpp.api.service;

import fr.dila.solonepg.api.client.InjectionEpgGvtDTO;
import fr.dila.ss.api.client.InjectionGvtDTO;
import java.io.Serializable;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Service d'injection de gouvernement de SOLON EPG
 *
 * @author jbrunet
 *
 */
public interface MGPPInjectionGouvernementService extends Serializable {
    /**
     * Réalise l'injection en créant les entités nécessaires
     *
     * @param session
     * @param listInjection
     */
    void executeInjection(final CoreSession session, List<InjectionGvtDTO> listInjection);

    List<InjectionEpgGvtDTO> createComparedDTO(
        CoreSession documentManager,
        List<InjectionGvtDTO> listInjection,
        List<InjectionEpgGvtDTO> listCompared
    );

    InjectionGvtDTO getNewGovernment(List<InjectionGvtDTO> listInjection);
}
