package fr.dila.solonepg.core.util;

import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.core.domain.ComplexeTypeImpl;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;

/**
 * Classe utilitaire pour extraire des propriétés des documents.
 *
 * @author asatre
 */
public class PropertyHelper {
    private static final Log LOGGER = LogFactory.getLog(PropertyHelper.class);

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
        } catch (NuxeoException e) {
            LOGGER.error("Les propriétés n'ont pas pu être obtenues à partir du document " + doc);
            return null;
        }

        Map<String, Serializable> propsSerializabled = new HashMap<>();
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

    /**
     * Extrait et retourne une propriété de type List<Calendar> d'un document.
     *
     * @param doc
     *            Document
     * @param schema
     *            Schéma
     * @param property
     *            Propriété
     * @return Valeur de la propriété
     */
    @SuppressWarnings("unchecked")
    public static List<Calendar> getCalendarListProperty(DocumentModel doc, String schema, String property) {
        // Afin d'éviter le npe
        List<Calendar> lstProp = (List<Calendar>) doc.getProperty(schema, property);
        if (lstProp == null) {
            lstProp = new ArrayList<>();
        }

        return lstProp;
    }

    public static List<ComplexeType> getListSerializableProperty(DocumentModel document, String schema, String value) {
        final List<ComplexeType> complexeTypeList = new ArrayList<>();
        final List<Map<String, Serializable>> complexeTypes = PropertyUtil.getMapStringSerializableListProperty(
            document,
            schema,
            value
        );
        if (complexeTypes != null) {
            for (Map<String, Serializable> complexeEntry : complexeTypes) {
                complexeTypeList.add(new ComplexeTypeImpl(complexeEntry));
            }
        }
        return complexeTypeList;
    }

    public static <T extends ComplexeType> List<T> getListSerializableProperty(
        DocumentModel document,
        String schema,
        String value,
        Class<T> complexeTypeImplClass
    ) {
        final List<T> complexeTypeList = new ArrayList<>();
        final List<Map<String, Serializable>> complexeTypes = PropertyUtil.getMapStringSerializableListProperty(
            document,
            schema,
            value
        );
        if (complexeTypes != null) {
            for (Map<String, Serializable> complexeEntry : complexeTypes) {
                T complexeType;
                try {
                    complexeType = complexeTypeImplClass.getDeclaredConstructor(Map.class).newInstance(complexeEntry);
                } catch (
                    InstantiationException
                    | IllegalAccessException
                    | IllegalArgumentException
                    | InvocationTargetException
                    | NoSuchMethodException
                    | SecurityException e
                ) {
                    throw new NuxeoException(
                        String.format(
                            "Impossible d'instancier une nouvelle instance de [%s]",
                            complexeTypeImplClass.getSimpleName()
                        ),
                        e
                    );
                }
                complexeTypeList.add(complexeType);
            }
        }
        return complexeTypeList;
    }

    public static void setListSerializableProperty(
        DocumentModel document,
        String schema,
        String property,
        List<? extends ComplexeType> listeToSerializable
    ) {
        // init serializable List
        final List<Map<String, Serializable>> listeSerializable = new ArrayList<>();
        // Hack : to refactor
        // note : si l'on récupère tel quel une liste de type complexe via un getProperty ou un getPropertyValue, les
        // serializableMap sont vide.
        for (ComplexeType serizableMap : listeToSerializable) {
            listeSerializable.add(serizableMap.getSerializableMap());
        }
        PropertyUtil.setProperty(document, schema, property, listeSerializable);
    }
}
