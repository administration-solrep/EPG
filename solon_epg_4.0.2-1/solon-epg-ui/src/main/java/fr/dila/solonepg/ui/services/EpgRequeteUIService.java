package fr.dila.solonepg.ui.services;

import fr.dila.st.ui.th.model.SpecificContext;

/**
 * Service UI dédié aux fonctionnalités de requêtage.
 */
public interface EpgRequeteUIService {
    /**
     * Création d'une requete libre Elastic Search
     */
    String saveRequeteLibreES(SpecificContext context);

    /**
     * Création d'une requete experte Elastic Search
     */
    String saveRequeteExperteES(SpecificContext context);
}
