package fr.dila.solonepg.ui.services.pan;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants.MAJ_TARGET;
import fr.dila.solonepg.ui.bean.pan.LigneHistoriquePanUnsortedList;
import fr.dila.st.ui.th.model.SpecificContext;

public interface HistoriqueMajMinUIService {
    /**
     * Retourne la cible sur laquelle porte l'historique ( application des lois,
     * ordonnances, transposition)
     *
     * @return la cible
     */
    MAJ_TARGET getTarget(SpecificContext context);

    /**
     *
     * Supprime les mises à jour minitérielles renseignées par leur id
     */
    String targetedDelete(SpecificContext context);
    /**
     * Supprime toutes les mises à jour ministérielles de la cible courante
     * (transposition, directive, ordonnance)
     */
    String massDelete(SpecificContext context);

    LigneHistoriquePanUnsortedList getHistoriquePaginated(SpecificContext context, MAJ_TARGET majTarget);
}
