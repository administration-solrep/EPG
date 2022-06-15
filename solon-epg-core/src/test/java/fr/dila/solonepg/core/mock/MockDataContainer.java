package fr.dila.solonepg.core.mock;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Field container générique pour un objet "bean" => les getters and setters
 * peuvent être mockés et les données sont stockées dans une map interne et
 * peuvent être restituées.
 */
public class MockDataContainer {
    /**
     * Map de stockage des données des mocks
     */
    private Map<Serializable, Map<String, Object>> dataMap = new HashMap<>();

    public String generateId() {
        String id = Integer.toString(dataMap.size());
        dataMap.put(id, new HashMap<>());
        return id;
    }

    /**
     * Prépare l'objet Answer pour mocker un getter précis (objet + référence de
     * méthode)
     *
     * @param <T>       type de retour du getter
     * @param getter    référence de méthode mockée
     * @param objectKey clé d'identification de l'objet courant dans dataMap
     * @param fieldKey  clé d'identification du field.
     */
    public <T> void doAnswerGetter(Supplier<T> getter, String objectKey, String fieldKey) {
        when(getter.get()).thenAnswer(invocation -> dataMap.get(objectKey).get(fieldKey));
    }

    /**
     * Prépare l'objet Answer pour mocker le setter d'un champ précis d'un objet.
     *
     * @param mock      objet mocké
     * @param objectKey clé d'identification de l'objet courant dans dataMap
     * @param fieldKey  clé d'identification du field.
     * @param <T>       type mocké
     */
    public <T> T doAnswerSetter(T mock, String objectKey, String fieldKey) {
        return doAnswer(
                invocation -> {
                    dataMap.get(objectKey).put(fieldKey, invocation.getArguments()[0]);
                    return null;
                }
            )
            .when(mock);
    }
}
