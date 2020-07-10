package fr.dila.solonepg.api.service;

import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.cases.Dossier;

public interface InformationsParlementairesService {

	/**
	 * Appeler le webservice EPP TransmissionDatePublicationJOEpp
	 * 
	 * @param session
	 * @param dossier
	 * @throws Exception
	 */
	void callWsTransmissionDatePublicationJOEpp(CoreSession session, Dossier dossier) throws Exception;

	/**
	 * Appeler le webservice EPP AccuserReception pour accuser la réception d'une communication de type JO-01
	 * 
	 * @param session
	 * @param dossier
	 * @param wsEvenement
	 * @throws Exception
	 */
	void callWsAccuserReception(CoreSession session, Dossier dossier, Object wsEvenement) throws Exception;

	/**
	 * Appeler le webservice EPP CreerVersion pour créer une communication de type JO-02 sucessive à une communication J0-01
	 * 
	 * @param session
	 * @param dossier
	 * @throws Exception
	 */
	void callWsCreerVersionEvt48(CoreSession session, Dossier dossier) throws Exception;

}
