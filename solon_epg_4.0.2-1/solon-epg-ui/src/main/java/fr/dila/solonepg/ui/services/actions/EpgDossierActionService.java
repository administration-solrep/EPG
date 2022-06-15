package fr.dila.solonepg.ui.services.actions;

import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface EpgDossierActionService {
    void terminerDossierSansPublication(SpecificContext context);

    void ajouterUrgenceDossier(SpecificContext context);

    void supprimerUrgenceDossier(SpecificContext context);

    void annulerMesureNominative(SpecificContext context);

    void redonnerMesureNominative(SpecificContext context);

    boolean canExecuteStep(SpecificContext context);

    void addToDossiersSuivis(SpecificContext context);

    void createListeSignatureDossiers(SpecificContext context);

    boolean checkShowModalSubstitutionPeriodicite(SpecificContext context);

    List<SelectValueDTO> loadModeleFdrSubstitutionRapport(SpecificContext context);

    boolean isFeuilleRouteRestartable(SpecificContext context);
}
