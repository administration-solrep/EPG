package fr.dila.solonmgpp.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

import fr.dila.solonmgpp.api.descriptor.EvenementMetaDonneesDescriptor;
import fr.dila.solonmgpp.api.descriptor.MetaDonneesDescriptor;
import fr.dila.solonmgpp.api.descriptor.PropertyDescriptor;
import fr.dila.solonmgpp.api.descriptor.VersionMetaDonneesDescriptor;
import fr.dila.solonmgpp.api.service.MetaDonneesService;
import fr.sword.xsd.solon.epp.EtatEvenement;
import fr.sword.xsd.solon.epp.EtatMessage;

/**
 * Implémentation du service permettant de gérer les schemas.
 * 
 * @author asatre
 */
public class MetaDonneesServiceImpl extends DefaultComponent implements MetaDonneesService {

    private static final long serialVersionUID = 8266232282826240132L;

    /**
     * Point d'extention des schemas des versions.
     */
    public static final String METADONNEES_SCHEMA_EXTENSION_POINT = "metadonnees";

    /**
     * Map des metadonnees.
     */
    private Map<String, MetaDonneesDescriptor> metaDonneesMap;

    /**
     * Cache des metadonnees.
     */
    private Map<String, Map<String, PropertyDescriptor>> metaDonneesCache;

    private static List<String> listEtatMessageNonHistorique;

    private static List<String> listEtatMessageHistorique;
    
    private static List<String> listEtatEvenement;

    static {
        listEtatMessageNonHistorique = new ArrayList<String>();
        listEtatMessageHistorique = new ArrayList<String>();
        listEtatEvenement= new ArrayList<String>();

        for (EtatMessage etatMessage : EtatMessage.values()) {

            if (EtatMessage.TRAITE.equals(etatMessage) || EtatMessage.EMIS.equals(etatMessage) || EtatMessage.AR_RECU.equals(etatMessage)) {
                listEtatMessageHistorique.add(etatMessage.name());
            } else {
                listEtatMessageNonHistorique.add(etatMessage.name());
            }

        }
        
        for (EtatEvenement etatEvenement : EtatEvenement.values()) {
            listEtatEvenement.add(etatEvenement.name());
            }
    }

    @Override
    public void activate(ComponentContext context) {
        metaDonneesMap = new HashMap<String, MetaDonneesDescriptor>();
        metaDonneesCache = new HashMap<String, Map<String, PropertyDescriptor>>();
    }

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) throws Exception {
        if (extensionPoint.equals(METADONNEES_SCHEMA_EXTENSION_POINT)) {
            MetaDonneesDescriptor descriptor = (MetaDonneesDescriptor) contribution;
            metaDonneesMap.put(descriptor.getName(), descriptor);
        } else {
            throw new IllegalArgumentException("Unknown extension point: " + extensionPoint);
        }
    }

    @Override
    public MetaDonneesDescriptor getEvenementType(final String evenementType) throws ClientException {
        MetaDonneesDescriptor metaDonneesDescriptor = metaDonneesMap.get(evenementType);
        if (metaDonneesDescriptor == null) {
            throw new ClientException("Type d'événement inconnu : " + evenementType);
        }
        return metaDonneesDescriptor;
    }

    @Override
    public Map<String, PropertyDescriptor> getMapProperty(final String evenementType) throws ClientException {
        Map<String, PropertyDescriptor> mapResult = metaDonneesCache.get(evenementType);
        if (mapResult == null) {
            final MetaDonneesDescriptor metaDonneesDescriptor = getEvenementType(evenementType);
            mapResult = new HashMap<String, PropertyDescriptor>();

            if (metaDonneesDescriptor != null) {
                final EvenementMetaDonneesDescriptor evt = metaDonneesDescriptor.getEvenement();
                if (evt != null) {
                    mapResult.putAll(evt.getProperty());
                }

                final VersionMetaDonneesDescriptor version = metaDonneesDescriptor.getVersion();
                if (version != null) {
                    mapResult.putAll(version.getProperty());
                }

                metaDonneesCache.put(evenementType, mapResult);
            }
        }

        return mapResult;
    }

    @Override
    public List<MetaDonneesDescriptor> getAll() {
        List<MetaDonneesDescriptor> listResult = new ArrayList<MetaDonneesDescriptor>();

        List<String> listKey = new ArrayList<String>(metaDonneesMap.keySet());
        Collections.sort(listKey);

        for (String key : listKey) {
            listResult.add(metaDonneesMap.get(key));
        }

        return listResult;
    }

    @Override
    public List<String> findAllEtatMessage(Boolean isHistorique) {
        if (isHistorique == null) {
            return new ArrayList<String>();
        } else if (isHistorique) {
            return listEtatMessageHistorique;
        } else {
            return listEtatMessageNonHistorique;
        }
    }
    
    @Override
    public List<String> findAllEtatEvenement() {
        return listEtatEvenement;
    }

}
