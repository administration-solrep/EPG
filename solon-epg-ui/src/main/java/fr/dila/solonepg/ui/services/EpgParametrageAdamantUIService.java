package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.th.bean.ParametreAdamantForm;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface EpgParametrageAdamantUIService {
    /**
     * Sauvegarde une nouvelle version des paramètrage Adamant
     * @param context
     */
    void save(SpecificContext context);

    /**
     * Récupère le paramètrage Adamant
     * @param context
     * @return le paramètrage Adamant
     */
    ParametreAdamantForm getParametreAdamantDocument(SpecificContext context);

    /**
     * Récupère les différents vecteurs éligibles
     * @param context
     * @return les différents vecteurs éligibles
     */
    List<String> getVecteurPublicationEligibilite(SpecificContext context);

    /**
     * Récupère les différentes suggestions pour les vecteurs de publication et les types d'acte
     * @param context
     * @return les différentes suggestions sous forme de string
     */
    List<String> getSuggestion(SpecificContext context);
}
