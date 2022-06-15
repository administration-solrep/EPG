package fr.dila.solonepg.ui.services.mgpp;

import fr.dila.solonepg.ui.bean.CorbeilleUIDTO;
import fr.dila.solonepg.ui.bean.MessageList;
import fr.dila.solonepg.ui.bean.MgppCorbeilleContentList;
import fr.dila.solonepg.ui.bean.RapidSearchDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface MgppCorbeilleUIService {
    /**
     * Retourne une liste de corbeilles
     * DOSSIERS_PARLEMENTAIRES_SELECTED dossier parlementaire auquel appartiennent les corbeilles
     *
     * @param context
     * @return
     */
    List<CorbeilleUIDTO> getCorbeilles(SpecificContext context);

    MessageList getMessageListForCorbeille(SpecificContext context);

    RapidSearchDTO buildFilters(SpecificContext context);

    MgppCorbeilleContentList getFicheListForCorbeille(SpecificContext context);

    MgppCorbeilleContentList getFicheListForRechercheExperte(SpecificContext context);
}
