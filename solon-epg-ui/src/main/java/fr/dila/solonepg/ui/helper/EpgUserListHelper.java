package fr.dila.solonepg.ui.helper;

import fr.dila.solonepg.core.recherche.EpgUserListingDTO;
import fr.dila.solonepg.ui.bean.EpgUserList;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class EpgUserListHelper {

    public EpgUserListHelper() {}

    /**
     * Construit un objet EpgUserList à partir de la liste de DTO docList.
     *
     * @return un objet EpgUserList
     */
    public static EpgUserList buildUserList(List<Map<String, Serializable>> docList, int total) {
        EpgUserList lstResults = new EpgUserList();

        lstResults.setNbTotal(total);

        // On fait le mapping des documents vers notre DTO
        for (Map<String, Serializable> doc : docList) {
            if (doc instanceof EpgUserListingDTO) {
                EpgUserListingDTO dto = (EpgUserListingDTO) doc;
                lstResults.getListe().add(dto);
            }
        }
        return lstResults;
    }
}
