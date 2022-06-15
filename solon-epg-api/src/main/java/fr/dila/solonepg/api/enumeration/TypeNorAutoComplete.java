package fr.dila.solonepg.api.enumeration;

import java.util.stream.Stream;

public enum TypeNorAutoComplete {
    TOUS,
    LOI,
    ORDONNANCE,
    DECRET;

    public static TypeNorAutoComplete findValueOf(String key) {
        return Stream.of(values()).filter(type -> type.name().equalsIgnoreCase(key)).findFirst().orElse(null);
    }
}
