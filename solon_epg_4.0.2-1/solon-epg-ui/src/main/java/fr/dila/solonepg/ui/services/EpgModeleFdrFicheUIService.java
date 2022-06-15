package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.th.model.bean.EpgModeleFdrForm;
import fr.dila.ss.ui.bean.FdrDTO;
import fr.dila.st.ui.th.model.SpecificContext;

public interface EpgModeleFdrFicheUIService {
    EpgModeleFdrForm getModeleFdrForm(SpecificContext context, EpgModeleFdrForm form);

    FdrDTO getFeuileRouteModele(SpecificContext context);

    void updateModele(SpecificContext context, EpgModeleFdrForm modeleForm);

    EpgModeleFdrForm createModele(SpecificContext context, EpgModeleFdrForm modeleForm);

    /*
     * Récupère le formulaire de consultation d'un modèle pour la substitution
     */
    EpgModeleFdrForm consultModeleSubstitution(SpecificContext context, EpgModeleFdrForm form);

    /*
     * Récupérer le dto de la feuille de route du modèle pour substitution
     */
    FdrDTO getFeuileRouteModeleSubstitution(SpecificContext context);
}
