package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.EpgDocumentDTO;
import fr.dila.solonepg.ui.bean.SaisineRectificativeDTO;
import fr.dila.solonepg.ui.bean.dossier.fdd.AdminSqueletteFondDTO;
import fr.dila.ss.ui.bean.FondDTO;
import fr.dila.ss.ui.services.actions.SSTreeManagerActionService;
import fr.dila.st.ui.bean.DossierDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

/**
 * Action Service de gestion de l'arborescence du fond de dossier.
 *
 */
public interface EpgFondDeDossierUIService extends SSTreeManagerActionService {
    /**
     * Construit le DTO associé au fdd du dossier courant.
     *
     * @param context SpecificContext
     * @return Objet FondDTO
     */
    FondDTO getFondDTO(SpecificContext context);

    void createFolder(SpecificContext context);

    DossierDTO getBordereauVersementDTO(SpecificContext context);

    void editFile(SpecificContext context);

    void renameFile(SpecificContext context);

    AdminSqueletteFondDTO getAdminFondDTO(SpecificContext context);

    List<String> getAdminFondFormatsAutorises(SpecificContext context);

    void addAdminFond(SpecificContext context);

    void updateAdminFond(SpecificContext context);

    void deleteAdminFond(SpecificContext context);

    void updateAdminFondFormats(SpecificContext context);

    List<EpgDocumentDTO> getFileVersionListDto(SpecificContext context);

    List<SaisineRectificativeDTO> getDocumentsSaisineRectificative(SpecificContext context);

    List<SaisineRectificativeDTO> getDocumentsPiecesComplementairesSaisine(SpecificContext context);

    void renameFolder(SpecificContext context);

    void deleteFolder(SpecificContext context);

    Boolean isDeletableFdd(SpecificContext context);
}
