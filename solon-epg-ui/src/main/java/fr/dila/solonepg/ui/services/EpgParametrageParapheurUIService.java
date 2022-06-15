package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.th.bean.ParametreParapheurFormConsultation;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface EpgParametrageParapheurUIService {
    /**
     * Sauvegarde une nouvelle version des paramètrages Parapheur
     * @param context
     */
    void save(SpecificContext context);

    /**
     * Récupère l'ensemble des paramètrages parapheur en fonction d'un type d'acte
     * @param context
     * @return le paramètrage Parapheur
     */
    List<ParametreParapheurFormConsultation> getAllParametreParapheurDocument(SpecificContext context);
}
