package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.ReattributionNorSummaryList;
import fr.dila.solonepg.ui.bean.SelectionSummaryList;
import fr.dila.solonepg.ui.bean.SubstitutionMassSummaryList;
import fr.dila.solonepg.ui.bean.TransfertSummaryList;
import fr.dila.st.ui.th.model.SpecificContext;

public interface EpgSelectionSummaryUIService {
    SelectionSummaryList getSelectionSummaryList(SpecificContext context);

    ReattributionNorSummaryList getReattributionSummaryList(SpecificContext context);

    TransfertSummaryList getTransfertSummaryList(SpecificContext context);

    SubstitutionMassSummaryList getDossierSubstitutionSummaryList(SpecificContext context);
}
