package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.EpgHistoriqueMigrationDTO;
import fr.dila.ss.ui.services.organigramme.SSMigrationGouvernementUIService;
import java.util.List;

public interface EpgMigrationGouvernementUIService extends SSMigrationGouvernementUIService {
    /**
     * Renvoie l'historique des migrations au complet.
     * @return une liste d'objets EpgHistoriqueMigrationDTO
     */
    List<EpgHistoriqueMigrationDTO> getListHistoriqueMigrationDTO();
}
