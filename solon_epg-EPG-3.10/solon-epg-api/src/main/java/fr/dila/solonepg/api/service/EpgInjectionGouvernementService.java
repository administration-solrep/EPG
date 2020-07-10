package fr.dila.solonepg.api.service;

import java.io.File;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.client.InjectionEpgGvtDTO;
import fr.dila.ss.api.client.InjectionGvtDTO;

/**
 * Service d'injection de gouvernement de SOLON EPG
 * 
 * @author jbrunet
 *
 */
public interface EpgInjectionGouvernementService extends Serializable {

    /**
     * Prépare l'injection en récupérant les données nécessaires dans un DTO
     * 
     * @param session
     * @throws ClientException
     */
	List<InjectionGvtDTO> prepareInjection(final CoreSession session, final File file) throws ClientException;
	
	/**
	 * Réalise l'injection en créant les entités nécessaires
	 * 
	 * @param session
	 * @param listInjection 
	 * @throws ClientException
	 */
	void executeInjection(final CoreSession session, List<InjectionGvtDTO> listInjection) throws ClientException;

	/**
	 * Crée le DTO de comparaison des entités
	 * 
	 * @param documentManager
	 * @param listInjection
	 * @return
	 * @throws ClientException
	 */
	List<InjectionEpgGvtDTO> createComparedDTO(CoreSession documentManager,
			List<InjectionGvtDTO> listInjection) throws ClientException;

	/**
	 * Crée le DTO à partir d'un NOR existant
	 * 
	 * @param nor
	 * @return
	 * @throws ClientException
	 */
	InjectionGvtDTO createFromNor(String nor) throws ClientException;

	/**
	 * Génére un pdf dans l'outputstream reprenant les infos de la liste de comparaison
	 * 
	 * @param documentManager
	 * @param outputStream
	 * @param listCompared
	 * @throws DocumentException 
	 * @throws Exception 
	 */
	void generatePdf(CoreSession documentManager, OutputStream outputStream,
			List<InjectionEpgGvtDTO> listCompared) throws Exception;

	/**
	 * Retourne une map ayant pour clé le NOR de l'injection du gouvernement, pour tous les gouvernements inchangés et qui ont été modifiés
	 * @param documentManager
	 * @param listInjection
	 * @return
	 * @throws ClientException 
	 */
	Map<String, InjectionGvtDTO> createFromInjectionDto(CoreSession documentManager, List<InjectionGvtDTO> listInjection) throws ClientException;
	
}
