package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.client.InjectionEpgGvtDTO;
import fr.dila.ss.api.client.InjectionGvtDTO;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Service d'injection de gouvernement de SOLON EPG
 *
 * @author jbrunet
 *
 */
public interface EpgInjectionGouvernementService extends Serializable {
    /**
     * Prépare l'injection en récupérant les données nécessaires dans un DTO
     *
     * @param session
     */
    List<InjectionGvtDTO> prepareInjection(final CoreSession session, final File file);

    /**
     * Réalise l'injection en créant les entités nécessaires
     *
     * @param session
     * @param listInjection
     */
    void executeInjection(final CoreSession session, List<InjectionGvtDTO> listInjection);

    /**
     * Crée le DTO de comparaison des entités
     *
     * @param documentManager
     * @param listInjection
     * @return
     */
    List<InjectionEpgGvtDTO> createComparedDTO(CoreSession documentManager, List<InjectionGvtDTO> listInjection);

    /**
     * Crée le DTO à partir d'un NOR existant
     *
     * @param nor
     * @return
     */
    InjectionGvtDTO createFromNor(String nor);

    /**
     * Retourne une map ayant pour clé le NOR de l'injection du gouvernement, pour tous les gouvernements inchangés et qui ont été modifiés
     * @param documentManager
     * @param listInjection
     * @return
     */
    Map<String, InjectionGvtDTO> createFromInjectionDto(
        CoreSession documentManager,
        List<InjectionGvtDTO> listInjection
    );
}
