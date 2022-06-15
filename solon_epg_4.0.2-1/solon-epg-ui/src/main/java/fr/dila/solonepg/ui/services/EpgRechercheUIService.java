package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.elastic.models.search.RechercheLibre;
import fr.dila.solonepg.elastic.models.search.SearchCriteriaExp;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.recherchelibre.CritereRechercheForm;
import fr.dila.solonepg.ui.bean.recherchelibre.RechercheDossierList;
import fr.dila.st.ui.th.model.SpecificContext;

public interface EpgRechercheUIService {
    RechercheDossierList doRechercheLibre(SpecificContext context);

    RechercheLibre toRechercheLibre(SpecificContext context);

    EpgDossierList doRechercheExperte(SpecificContext context);

    SearchCriteriaExp toSearchCriteriaExp(SpecificContext context);

    EpgDossierList doRechercheRapide(SpecificContext context);

    String getJsonQuery(SpecificContext context);

    EpgDossierList getResultsForJsonQuery(SpecificContext context);

    CritereRechercheForm getUpdatedCritereRechercheForm(SpecificContext context);
}
