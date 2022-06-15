package fr.dila.solonepg.elastic.models.search.enums;

public enum OperatorQueryEnum {
    ENTRE(false, QueryEnum.RANGE, IncludeEnum.INCLUDE, IncludeEnum.EXCLUDE, QueryEnum.NONE),
    PAS_ENTRE(true, QueryEnum.RANGE, IncludeEnum.INCLUDE, IncludeEnum.EXCLUDE, QueryEnum.NONE),
    PLUS_PETIT(false, QueryEnum.RANGE, IncludeEnum.NO, IncludeEnum.EXCLUDE, QueryEnum.NONE),
    PLUS_GRAND(false, QueryEnum.RANGE, IncludeEnum.EXCLUDE, IncludeEnum.NO, QueryEnum.NONE),
    PLUS_PETIT_OU_EGAL(false, QueryEnum.RANGE, IncludeEnum.NO, IncludeEnum.INCLUDE, QueryEnum.NONE),
    PLUS_GRAND_OU_EGAL(false, QueryEnum.RANGE, IncludeEnum.INCLUDE, IncludeEnum.NO, QueryEnum.NONE),
    EGAL(false, QueryEnum.MATCH_PHRASE, IncludeEnum.NONE, IncludeEnum.NONE, QueryEnum.TERM),
    TOUS_LES_MOTS(false, QueryEnum.INTERVALS, IncludeEnum.NONE, IncludeEnum.NONE, QueryEnum.WILDCARD),
    AUCUN_DES_MOTS(true, QueryEnum.MATCH_AND, IncludeEnum.NONE, IncludeEnum.NONE, QueryEnum.WILDCARD),
    CONTIENT_PHRASE(false, QueryEnum.MATCH_PHRASE, IncludeEnum.NONE, IncludeEnum.NONE, QueryEnum.WILDCARD),
    EST_DIFFERENT_DE(true, QueryEnum.MATCH_PHRASE, IncludeEnum.NONE, IncludeEnum.NONE, QueryEnum.TERM),
    EST_VIDE(true, QueryEnum.EXIST, IncludeEnum.NONE, IncludeEnum.NONE, QueryEnum.NONE),
    N_EST_PAS_VIDE(false, QueryEnum.EXIST, IncludeEnum.NONE, IncludeEnum.NONE, QueryEnum.NONE),
    CONTIENT(false, QueryEnum.MATCH_PHRASE, IncludeEnum.NONE, IncludeEnum.NONE, QueryEnum.WILDCARD),
    COMME_PART(false, QueryEnum.MATCH_AND, IncludeEnum.NONE, IncludeEnum.NONE, QueryEnum.WILDCARD),
    PAS_COMME_PART(true, QueryEnum.MATCH, IncludeEnum.NONE, IncludeEnum.NONE, QueryEnum.WILDCARD),
    CONTIENT_LIST(false, QueryEnum.TERMS, IncludeEnum.NONE, IncludeEnum.NONE, QueryEnum.NONE),
    NE_CONTIENT_PAS(true, QueryEnum.TERMS, IncludeEnum.NONE, IncludeEnum.NONE, QueryEnum.NONE);

    private final boolean negative;
    private final QueryEnum query;
    private final IncludeEnum min;
    private final IncludeEnum max;
    private final QueryEnum keyword;

    OperatorQueryEnum(boolean negative, QueryEnum query, IncludeEnum min, IncludeEnum max, QueryEnum keyword) {
        this.negative = negative;
        this.query = query;
        this.min = min;
        this.max = max;
        this.keyword = keyword;
    }

    public static OperatorQueryEnum transposeOperator(ElasticOperatorEnum operator, QueryTypeEnum queryType) {
        OperatorQueryEnum res;
        if (operator == ElasticOperatorEnum.TOUS_LES_MOTS) {
            res = queryType.equals(QueryTypeEnum.TEXT_PART) ? COMME_PART : TOUS_LES_MOTS;
        } else if (operator == ElasticOperatorEnum.AUCUN_DES_MOTS) {
            res = queryType.equals(QueryTypeEnum.TEXT_PART) ? PAS_COMME_PART : AUCUN_DES_MOTS;
        } else if (operator == ElasticOperatorEnum.CONTIENT) {
            res = queryType.equals(QueryTypeEnum.LIST) ? CONTIENT_LIST : CONTIENT;
        } else {
            res = Enum.valueOf(OperatorQueryEnum.class, operator.toString());
        }
        return res;
    }

    public boolean isNegative() {
        return negative;
    }

    public QueryEnum getQuery() {
        return query;
    }

    public IncludeEnum getMin() {
        return min;
    }

    public IncludeEnum getMax() {
        return max;
    }

    public QueryEnum getKeyword() {
        return keyword;
    }
}
