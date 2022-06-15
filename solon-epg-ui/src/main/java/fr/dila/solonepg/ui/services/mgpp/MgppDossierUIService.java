package fr.dila.solonepg.ui.services.mgpp;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.ui.bean.MgppFichePresentationOEPList;
import fr.dila.solonepg.ui.bean.MgppMessageDTO;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface MgppDossierUIService {
    /**
     * Retourne l'évènement courant depuis EPP à partir de son id et de sa version.
     *
     * @param context
     * @return
     */
    EvenementDTO getFreshCurrentEvenementDTO(SpecificContext context);

    /**
     * Retourne l'événement courant à partir de son id et de sa version
     *
     * @param context
     * @return
     */
    EvenementDTO getCurrentEvenementDTO(SpecificContext context);

    /**
     * Supprime l'événement courant de la UserSession
     *
     * @param context
     */
    void resetCurrentEvenementDTO(SpecificContext context);

    /**
     * Retourne le message courant à partir de l'id de l'événement
     *
     * @param context
     * @return
     */
    MgppMessageDTO getCurrentMessageDTO(SpecificContext context);

    /**
     * Supprime le message courant de la UserSession
     *
     * @param context
     */
    void resetCurrentMessageDTO(SpecificContext context);

    /**
     * Retourne le dossier courant à partir de l'id dossier ou du numéro NOR renseigné dans la fiche loi
     *
     * @param context
     * @return
     */
    Dossier getCurrentDossier(SpecificContext context);

    /**
     * Retourne le dossier courant à partir de l'id dossier ou de la fiche.
     *
     * @param context
     * @param currentFicheDoc
     * @return
     */
    Dossier getCurrentDossier(SpecificContext context, DocumentModel currentFicheDoc);

    /**
     * Récupère la liste des libellés des étapes en cours sous la forme 'Type étape - Poste'
     *
     * @param context
     * @return
     */
    List<String> getCurrentStepLabels(SpecificContext context);

    /**
     * Récupère la liste des libellés des étapes à venir sous la forme 'Type étape - Poste'
     *
     * @param context
     * @return
     */
    List<String> getNextStepLabels(SpecificContext context);

    MgppFichePresentationOEPList getFichePresentationOEP(SpecificContext context);

    void rattacherDossierPuisTraiter(SpecificContext context);

    File exportDossier(SpecificContext context);

    Dossier findDossierFromId(SpecificContext context);
}
