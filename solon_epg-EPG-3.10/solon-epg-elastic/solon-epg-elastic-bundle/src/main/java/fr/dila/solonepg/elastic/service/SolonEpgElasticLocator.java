package fr.dila.solonepg.elastic.service;

import fr.dila.solonepg.elastic.services.RequeteurService;

import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.util.ServiceUtil;

public class SolonEpgElasticLocator extends SolonEpgServiceLocator {

	public static RequeteurService getElasticRequeteurService() {
		return ServiceUtil.getService(RequeteurService.class);
	}

	private SolonEpgElasticLocator() {
		super();
	}

}
