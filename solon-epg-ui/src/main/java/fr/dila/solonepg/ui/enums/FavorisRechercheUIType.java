package fr.dila.solonepg.ui.enums;

import fr.dila.solonepg.api.enums.FavorisRechercheType;
import java.util.stream.Stream;
import org.nuxeo.ecm.core.api.NuxeoException;

public enum FavorisRechercheUIType {
    LIBRE("libre", FavorisRechercheType.ES_LIBRE),
    EXPERT("experte", FavorisRechercheType.ES_EXPERTE),
    USER("utilisateur", FavorisRechercheType.USER),
    MODELE_FDR("fdr", FavorisRechercheType.MODELE_FEUILLE_ROUTE),
    PAN("pan", FavorisRechercheType.ACTIVITE_NORMATIVE);

    private final String uIValue;
    private final FavorisRechercheType type;

    FavorisRechercheUIType(String uIValue, FavorisRechercheType type) {
        this.uIValue = uIValue;
        this.type = type;
    }

    public String getuIValue() {
        return uIValue;
    }

    public FavorisRechercheType getType() {
        return type;
    }

    public static final FavorisRechercheType getTypeFromUIValue(String uIValue) {
        return Stream
            .of(FavorisRechercheUIType.values())
            .filter(v -> v.getuIValue().equals(uIValue))
            .findFirst()
            .map(FavorisRechercheUIType::getType)
            .orElseThrow(() -> new NuxeoException("Type inconnue : " + uIValue));
    }
}
