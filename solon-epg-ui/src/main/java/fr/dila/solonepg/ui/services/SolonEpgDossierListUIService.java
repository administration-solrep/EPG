package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.Serializable;
import java.util.Map;
import org.apache.commons.cli.MissingArgumentException;

public interface SolonEpgDossierListUIService {
    EpgDossierList getDossiersFromPosteCorbeille(SpecificContext context) throws MissingArgumentException;

    EpgDossierList getDossiersFromTypeActeCorbeille(SpecificContext context) throws MissingArgumentException;

    EpgDossierList getDossiersFromTypeEtapeCorbeille(SpecificContext context) throws MissingArgumentException;

    EpgDossierList getDossiersFromEnCoursDeCreationCorbeille(SpecificContext context);

    EpgDossierList getDernierDossierConsultee(SpecificContext context);

    EpgDossierList getDossiersFromCrees(SpecificContext context);

    EpgDossierList getDossiersFromATraiter(SpecificContext context);

    EpgDossierList getDossiersFromSuivi(SpecificContext context);

    EpgDossierList getDossiersFromHistoriqueValidation(SpecificContext context);

    EpgDossierList getDossiersFromByMailBoxAndState(SpecificContext context);

    EpgDossierList getDossiersCandidatToArchivageIntermediaire(SpecificContext context);

    EpgDossierList getDossiersAbandon(SpecificContext context);

    EpgDossierList getDossiersCandidatElimination(SpecificContext context);

    EpgDossierList getDossiersCandidatToArchivageDefinitive(SpecificContext context);

    EpgDossierList getDossiersFavoris(SpecificContext context);

    EpgDossierList getDossiersValidesElimination(SpecificContext context);

    void exportDossiers(SpecificContext context);

    void exportDossiersRechercheRapide(SpecificContext context);

    void exportDossiersRechercheExperte(SpecificContext context);

    void removeDossiersFromDossiersSuivis(SpecificContext context);

    void emptyDossiersSuivis(SpecificContext context);

    /**
     * Calcule et applique les filtres sélectionnés sur le provider courant (nom,
     * params, numéro de page, nb d'éléments par page, tris), renvoie la liste de
     * résultats.
     */
    EpgDossierList applyFilters(
        String jsonSearch,
        DossierListForm dossierlistForm,
        Map<String, Serializable> mapSearch,
        SpecificContext context
    );

    void exportDossiersTabDyn(SpecificContext context);
}
