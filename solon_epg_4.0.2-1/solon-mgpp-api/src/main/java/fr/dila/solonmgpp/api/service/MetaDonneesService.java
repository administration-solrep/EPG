package fr.dila.solonmgpp.api.service;

import fr.dila.solonmgpp.api.descriptor.MetaDonneesDescriptor;
import fr.dila.st.api.descriptor.parlement.PropertyDescriptor;
import fr.sword.xsd.solon.epp.EtatMessage;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
     */
    MetaDonneesDescriptor getEvenementType(final String evenementType);

    /**
     *
     * @param evenementType
     * @return la map des {@link PropertyDescriptor} correspondant au type d'evenement
     */
    Map<String, PropertyDescriptor> getMapProperty(final String evenementType);

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
