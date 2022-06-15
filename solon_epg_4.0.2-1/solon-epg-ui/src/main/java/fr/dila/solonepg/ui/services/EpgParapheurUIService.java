package fr.dila.solonepg.ui.services;

import fr.dila.ss.ui.bean.FondDTO;
import fr.dila.ss.ui.services.actions.SSTreeManagerActionService;
import fr.dila.st.ui.th.model.SpecificContext;

/**
 * Action Service de gestion de l'arborescence du fond de dossier.
 *
 */
public interface EpgParapheurUIService extends SSTreeManagerActionService {
    /**
     * Construit le DTO associé au parapheur du dossier courant.
     *
     * @param context SpecificContext
     * @return Objet FondDTO
     */
    FondDTO getParapheurDTO(SpecificContext context);

    boolean canUserAddFileInFolder(SpecificContext context, String folderName);

    void moveFileToAnotherFolder(SpecificContext context);
}
