package fr.dila.solonepg.api.enums;

import java.util.stream.Stream;
import org.nuxeo.ecm.core.api.NuxeoException;

public enum FavorisRechercheType {
    ES_LIBRE("ES_LIBRE"),
    ES_EXPERTE("ES_EXPERTE"),
    ACTIVITE_NORMATIVE("ActiviteNormative"),
    USER("User"),
    MODELE_FEUILLE_ROUTE("ModeleFeuilleRoute");

    private final String type;

    FavorisRechercheType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static FavorisRechercheType fromType(String type) {
        return Stream
            .of(FavorisRechercheType.values())
            .filter(f -> f.getType().equals(type))
            .findFirst()
            .orElseThrow(() -> new NuxeoException("Type inconnu : " + type));
    }
}
