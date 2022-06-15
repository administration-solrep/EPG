package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.dossier.bordereau.EpgDossierSimilaireBordereauDTO;
import fr.dila.solonepg.ui.bean.dossier.similaire.DossierSimilaireDTO;
import fr.dila.solonepg.ui.bean.dossier.similaire.DossierSimilaireList;
import fr.dila.st.ui.th.model.SpecificContext;

public interface DossiersSimilairesUIService {
    DossierSimilaireList getDossiersSimilaires(SpecificContext context);

    DossierSimilaireDTO getNextEntry(SpecificContext context);

    DossierSimilaireDTO getPreviousEntry(SpecificContext context);

    EpgDossierSimilaireBordereauDTO getCurrentBordereau(SpecificContext context);
}
