package fr.dila.solonmgpp.api.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonmgpp.api.descriptor.MetaDonneesDescriptor;
import fr.dila.solonmgpp.api.descriptor.PropertyDescriptor;
import fr.sword.xsd.solon.epp.EtatMessage;

/**
 * Service permettant de gérer les metadonnées.
 * 
 * @author asatre
 */
public interface MetaDonneesService extends Serializable {

    /**
     * Retourne les metadonnees correspondant au type d'evenement.
     * 
     * @param evenementType Type d'événement
     * @return Description du type d'événement
     * @throws ClientException
     */
    MetaDonneesDescriptor getEvenementType(final String evenementType) throws ClientException;

    /**
     * 
     * @param evenementType
     * @return la map des {@link PropertyDescriptor} correspondant au type d'evenement
     * @throws ClientException
     */
    Map<String, PropertyDescriptor> getMapProperty(final String evenementType) throws ClientException;

    /**
     * retourne la liste de tous les descriptors
     * 
     * @return
     */
    List<MetaDonneesDescriptor> getAll();

    /**
     * Liste des {@link EtatMessage} possible par rapport au type de corbeille
     * 
     * @param isHistorique
     * @return
     */
    List<String> findAllEtatMessage(Boolean isHistorique);
    
    /**
     * 
     * @return
     */
     List<String> findAllEtatEvenement();

}
