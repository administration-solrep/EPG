package fr.dila.solonepg.ui.services.mgpp;

import fr.dila.solonepg.ui.bean.MgppDossierCommunicationConsultationFiche;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.st.ui.bean.SelectValueGroupDTO;
import fr.dila.st.ui.bean.VersionSelectDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface MgppEvenementDetailsUIService {
    MgppDossierCommunicationConsultationFiche getDetails(SpecificContext context);

    EvenementDTO initFicheDetailCommunication(SpecificContext context);

    /**
     * Retourne la liste des communications successives d'un evenement
     *
     * @param context
     * @return
     */
    List<SelectValueGroupDTO> getCommunicationsSuccessivesList(SpecificContext context);

    /**
     * Retourne true si le bloc de création de communication successive doit être affiché
     *
     * @param context
     * @return
     */
    boolean displayCreerCommunicationSuccessive(SpecificContext context);

    /**
     * Retourne une liste de noms des actions possibles sur la communication
     *
     * @param context
     * @return
     */
    List<String> getActionsSuivantes(SpecificContext context);

    /**
     * Retourne la nature de la version
     *
     * @param context
     * @return
     */
    String getNatureVersion(SpecificContext context);

    /**
     * Retourne la liste des versions de la communication
     *
     * @param context
     * @return
     */
    List<VersionSelectDTO> getListVersions(SpecificContext context);

    boolean peutMettreEnAttente(SpecificContext context);

    boolean peutLierUnOEP(SpecificContext context);

    void lierOEP(SpecificContext context);
}
