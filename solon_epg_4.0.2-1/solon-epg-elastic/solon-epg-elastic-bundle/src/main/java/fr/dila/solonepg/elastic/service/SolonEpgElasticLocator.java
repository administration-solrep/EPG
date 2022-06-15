package fr.dila.solonepg.elastic.service;

import fr.dila.solonepg.elastic.services.RequeteurService;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;

public class SolonEpgElasticLocator {

    public static RequeteurService getElasticRequeteurService() {
        return ServiceUtil.getRequiredService(RequeteurService.class);
    }

    private SolonEpgElasticLocator() {
        super();
    }
}
