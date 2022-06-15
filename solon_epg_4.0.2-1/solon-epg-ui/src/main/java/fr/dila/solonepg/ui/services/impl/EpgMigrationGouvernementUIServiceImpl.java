package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgHistoriqueMigrationDTO;
import fr.dila.solonepg.ui.jaxrs.webobject.page.admin.EpgMigration;
import fr.dila.solonepg.ui.services.EpgMigrationGouvernementUIService;
import fr.dila.ss.api.migration.MigrationLoggerModel;
import fr.dila.ss.api.service.SSChangementGouvernementService;
import fr.dila.ss.api.service.SSOrganigrammeService;
import fr.dila.ss.ui.services.organigramme.SSMigrationGouvernementUIServiceImpl;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EpgMigrationGouvernementUIServiceImpl
    extends SSMigrationGouvernementUIServiceImpl
    implements EpgMigrationGouvernementUIService {

    @Override
    public SSChangementGouvernementService getChangementGouvernementService() {
        return SolonEpgServiceLocator.getChangementGouvernementService();
    }

    @Override
    public Map<String, List<String>> getActions() {
        return EpgMigration.ACTIONS;
    }

    @Override
    protected SSOrganigrammeService getOrganigrammeService() {
        return SolonEpgServiceLocator.getEpgOrganigrammeService();
    }

    @Override
    public List<EpgHistoriqueMigrationDTO> getListHistoriqueMigrationDTO() {
        return new ArrayList<>(
            getMigrationLoggerModelList()
                .stream()
                .map(this::toEpgHistoriqueMigrationDTO)
                .sorted(Comparator.comparing(EpgHistoriqueMigrationDTO::getDateDebut).reversed())
                .collect(Collectors.toList())
        );
    }

    private EpgHistoriqueMigrationDTO toEpgHistoriqueMigrationDTO(MigrationLoggerModel migrationLoggerModel) {
        EpgHistoriqueMigrationDTO epgHistoriqueMigrationDTO = new EpgHistoriqueMigrationDTO();

        toSSHistoriqueMigrationDTO(migrationLoggerModel, epgHistoriqueMigrationDTO);

        String ministereRattachement =
            migrationLoggerModel.getNorDossierClosCurrent() + "/" + migrationLoggerModel.getNorDossierClosCount();
        epgHistoriqueMigrationDTO.setMinistereRattachement(ministereRattachement);

        String reattributionDossiers =
            migrationLoggerModel.getNorDossierLanceIniteCurrent() +
            "/" +
            migrationLoggerModel.getNorDossierLanceIniteCount();
        epgHistoriqueMigrationDTO.setReattributionDossiers(reattributionDossiers);

        String bulletinOfficiel =
            migrationLoggerModel.getBulletinOfficielCurrent() + "/" + migrationLoggerModel.getBulletinOfficielCount();
        epgHistoriqueMigrationDTO.setBulletinOfficiel(bulletinOfficiel);

        String tablesReference =
            migrationLoggerModel.getTableRefCurrent() + "/" + migrationLoggerModel.getTableRefCount();
        epgHistoriqueMigrationDTO.setTablesReference(tablesReference);

        return epgHistoriqueMigrationDTO;
    }
}
