package fr.dila.solonepg.ui.helper;

import fr.dila.ss.ui.bean.fdr.FeuilleRouteDTO;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class EpgModelFDRListHelper {

    public EpgModelFDRListHelper() {}

    /**
     * Construit un objet ModeleFDRList à partir de la liste de DTO docList.
     *
     * @return un objet ModeleFDRList
     */
    public static ModeleFDRList buildModeleFDRList(List<Map<String, Serializable>> docList, int total) {
        ModeleFDRList lstResults = new ModeleFDRList();

        lstResults.setNbTotal(total);

        // On fait le mapping des documents vers notre DTO
        for (Map<String, Serializable> doc : docList) {
            if (doc instanceof FeuilleRouteDTO) {
                FeuilleRouteDTO dto = (FeuilleRouteDTO) doc;
                lstResults.getListe().add(dto);
            }
        }
        return lstResults;
    }
}
