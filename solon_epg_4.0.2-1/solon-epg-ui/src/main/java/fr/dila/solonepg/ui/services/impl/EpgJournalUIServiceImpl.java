package fr.dila.solonepg.ui.services.impl;

import static fr.dila.st.api.constant.STEventConstant.CATEGORY_ADMINISTRATION;
import static fr.dila.st.api.constant.STEventConstant.CATEGORY_BORDEREAU;
import static fr.dila.st.api.constant.STEventConstant.CATEGORY_FDD;
import static fr.dila.st.api.constant.STEventConstant.CATEGORY_FEUILLE_ROUTE;
import static fr.dila.st.api.constant.STEventConstant.CATEGORY_PARAPHEUR;
import static fr.dila.st.api.constant.STEventConstant.CATEGORY_REPRISE;

import com.google.common.collect.ImmutableSet;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.ss.ui.services.impl.SSJournalUIServiceImpl;
import java.util.Set;

public class EpgJournalUIServiceImpl extends SSJournalUIServiceImpl {

    @Override
    public Set<String> getCategoryList() {
        return ImmutableSet.of(
            CATEGORY_FEUILLE_ROUTE,
            CATEGORY_BORDEREAU,
            CATEGORY_FDD,
            SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG,
            SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR,
            SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG,
            SolonEpgEventConstant.CATEGORY_FDD_FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER,
            SolonEpgEventConstant.CATEGORY_PROCEDURE_PARLEMENT,
            SolonEpgEventConstant.CATEGORY_TRAITEMENT_PAPIER,
            CATEGORY_REPRISE,
            CATEGORY_PARAPHEUR,
            CATEGORY_ADMINISTRATION
        );
    }
}
