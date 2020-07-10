package fr.dila.solonepg.api.service;

import java.util.List;

import org.nuxeo.ecm.platform.usermanager.UserManager;

public interface SolonEpgUserManager extends UserManager {

    List<String> getAdministratorIds();
     
    boolean isExistAndNotActive(String username);
}
