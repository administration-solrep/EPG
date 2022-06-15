package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.favoris.recherche.EpgFavorisRechercheList;
import fr.dila.ss.ui.bean.SSUsersList;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.st.ui.th.model.SpecificContext;

public interface FavorisRechercheUIService {
    EpgFavorisRechercheList getFavorisRechercheList(SpecificContext context);

    EpgDossierList getDossiers(SpecificContext context);

    SSUsersList getUsersList(SpecificContext context);

    ModeleFDRList getModelesFeuilleRoute(SpecificContext context);

    void saveFavoriDossier(SpecificContext context);

    void saveFavori(SpecificContext context);
}
