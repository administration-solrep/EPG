package fr.dila.solonepg.core.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.st.api.constant.STConstant;

/**
 * Classe utilitaire pour extraire des propriétés des documents.
 * 
 * @author asatre
 */
public class PropertyUtil extends fr.dila.st.core.util.PropertyUtil {

    private static final Log LOGGER = LogFactory.getLog(PropertyUtil.class);

    /**
     * Extrait une map à partir des propriétés d'un document, avec le schéma donné.
     * Les propriétés extraites doivent toutes être des objects serializable.
     * La map obtenue possède une clé pour la sélection du document par les checkbox.
     * 
     * @param doc le document à partir duquel les propriétés sont extraites
     * @param schemaName le nom du schéma
     * @return une map utilisable par un provider, null si les propriétés ne sont pas extraites
     */
    public static Map<String, Serializable> getMapForProviderFromDocumentProps(DocumentModel doc, String schemaName) {
        Map<String, Object> props = null;
        
        try {
            props = doc.getProperties(schemaName);
        } catch (ClientException e) {
            LOGGER.error("Les propriétés n'ont pas pu être obtenues à partir du document " + doc);
            return null;
        }
        
        Map<String, Serializable> propsSerializabled = new HashMap<String, Serializable>();
        for (Entry<String, Object> entry : props.entrySet()) {
            String simplerKey = entry.getKey().split(":")[1];
            if (entry.getValue() instanceof Calendar) {
                Calendar cal = (Calendar) entry.getValue();
                propsSerializabled.put(simplerKey, cal.getTime());
            } else {
                propsSerializabled.put(simplerKey, (Serializable) entry.getValue());
            }
        }
        
        // Ajoute la clé pour la sélection des données.
        propsSerializabled.put(STConstant.KEY_ID_SELECTION, doc.getId());
        return propsSerializabled;
    }
}
