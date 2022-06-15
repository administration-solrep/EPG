package fr.dila.solonepg.ui.services;

import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;

public interface EpgModeleFdrListUIService {
    /*
     * Récupérer la liste des modèles à afficher pour la substitution
     */
    ModeleFDRList getModelesFDRSubstitution(SpecificContext context);

    ModeleFDRList getModelesFDRByIds(SpecificContext context);

    ModeleFDRList getDernierModelFDRConsulte(SpecificContext context);

    File listeModeleToCsv(SpecificContext context);
}
