package fr.dila.solonepg.elastic.models.search.enums;

import fr.dila.ss.core.enumeration.OperatorEnum;
import fr.dila.st.core.util.ObjectHelper;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public enum ElasticOperatorEnum {
    ENTRE(OperatorEnum.ENTRE),
    PAS_ENTRE(OperatorEnum.PAS_ENTRE),
    PLUS_PETIT(OperatorEnum.PLUS_PETIT),
    PLUS_GRAND(OperatorEnum.PLUS_GRAND),
    PLUS_PETIT_OU_EGAL(OperatorEnum.PLUS_PETIT_EGAL),
    PLUS_GRAND_OU_EGAL(OperatorEnum.PLUS_GRAND_EGAL),
    EGAL(OperatorEnum.EGAL),
    TOUS_LES_MOTS(OperatorEnum.COMME),
    AUCUN_DES_MOTS(OperatorEnum.PAS_COMME),
    CONTIENT_PHRASE(OperatorEnum.CONTIENT),
    EST_DIFFERENT_DE(OperatorEnum.DIFFERENT),
    EST_VIDE(OperatorEnum.VIDE),
    N_EST_PAS_VIDE(OperatorEnum.PAS_VIDE),
    CONTIENT(OperatorEnum.DANS),
    NE_CONTIENT_PAS(OperatorEnum.PAS_DANS);

    private static final Map<OperatorEnum, ElasticOperatorEnum> ENUM_TO_ENUM = new EnumMap<>(OperatorEnum.class);

    private final OperatorEnum conversionOperator;

    static {
        Arrays
            .stream(values())
            .filter(e -> e.getConversionOperator() != null)
            .forEach(e -> ENUM_TO_ENUM.put(e.getConversionOperator(), e));
    }

    ElasticOperatorEnum() {
        this(null);
    }

    ElasticOperatorEnum(OperatorEnum conversionOperator) {
        this.conversionOperator = conversionOperator;
    }

    public OperatorEnum getConversionOperator() {
        return conversionOperator;
    }

    public static ElasticOperatorEnum toOperatorEnum(OperatorEnum operator) {
        return ObjectHelper.requireNonNullElseGet(
            ENUM_TO_ENUM.get(operator),
            () -> {
                throw new UnsupportedOperationException(String.format("Valeur %s non mappé", operator));
            }
        );
    }
}
