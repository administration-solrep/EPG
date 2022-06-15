package fr.dila.solonepg.ui.services.actions;

import fr.dila.ss.ui.services.actions.ModeleFeuilleRouteActionService;
import fr.dila.st.ui.th.model.SpecificContext;

public interface EpgModeleFeuilleRouteActionService extends ModeleFeuilleRouteActionService {
    /*
     * Vérifie que les champs Ministère, Direction et Type acte sont cohérent
     */
    boolean checkBeforeSave(SpecificContext context);

    void updateSquelette(SpecificContext context);

    void validateSquelette(SpecificContext context);

    /**
     * Les intitules sont de la forme '*trigramme_ministère* - *lettre_direction* - *type_acte* - intitulé libre'
     * Decoupe un intitule en 2 parties, l'intitule fixe (XXX - X - xxxxxx) et l'intitule libre.
     *
     * @return Une table de 2 String. [0] Contient la partie fixe et [1] contient la partie libre.
     */
    String[] splitIntitule(String intitule, String ministereID, String directionID, String typeActeId);

    /*
     * CReéation en masse de modèles de feuille de route
     */
    void createModeleFDRMass(SpecificContext context);

    void deleteMassModele(SpecificContext context);
}
