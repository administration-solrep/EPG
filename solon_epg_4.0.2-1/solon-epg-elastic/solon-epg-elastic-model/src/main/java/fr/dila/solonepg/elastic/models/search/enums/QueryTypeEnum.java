package fr.dila.solonepg.elastic.models.search.enums;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public enum QueryTypeEnum {
    BOOL(
        unmodifiableList(
            asList(ElasticOperatorEnum.EST_VIDE, ElasticOperatorEnum.N_EST_PAS_VIDE, ElasticOperatorEnum.EGAL)
        )
    ),
    DATE(
        unmodifiableList(
            asList(
                ElasticOperatorEnum.EST_VIDE,
                ElasticOperatorEnum.N_EST_PAS_VIDE,
                ElasticOperatorEnum.ENTRE,
                ElasticOperatorEnum.PAS_ENTRE,
                ElasticOperatorEnum.PLUS_PETIT,
                ElasticOperatorEnum.PLUS_GRAND,
                ElasticOperatorEnum.PLUS_GRAND_OU_EGAL,
                ElasticOperatorEnum.PLUS_PETIT_OU_EGAL
            )
        )
    ),
    LIST(unmodifiableList(asList(ElasticOperatorEnum.CONTIENT, ElasticOperatorEnum.NE_CONTIENT_PAS))),
    NONE(emptyList()),
    TEXT(
        unmodifiableList(
            asList(
                ElasticOperatorEnum.EST_VIDE,
                ElasticOperatorEnum.N_EST_PAS_VIDE,
                ElasticOperatorEnum.EGAL,
                ElasticOperatorEnum.EST_DIFFERENT_DE,
                ElasticOperatorEnum.TOUS_LES_MOTS,
                ElasticOperatorEnum.AUCUN_DES_MOTS,
                ElasticOperatorEnum.CONTIENT_PHRASE
            )
        )
    ),
    TEXT_EG(
        unmodifiableList(
            asList(ElasticOperatorEnum.EST_VIDE, ElasticOperatorEnum.N_EST_PAS_VIDE, ElasticOperatorEnum.EGAL)
        )
    ),
    TEXT_PART(unmodifiableList(asList(ElasticOperatorEnum.TOUS_LES_MOTS, ElasticOperatorEnum.AUCUN_DES_MOTS))),
    TEXT_CO(
        unmodifiableList(
            asList(ElasticOperatorEnum.EST_VIDE, ElasticOperatorEnum.N_EST_PAS_VIDE, ElasticOperatorEnum.CONTIENT)
        )
    );

    private List<ElasticOperatorEnum> possibleQueries;

    QueryTypeEnum(List<ElasticOperatorEnum> possibleQueries) {
        this.possibleQueries = possibleQueries;
    }

    public List<ElasticOperatorEnum> getPossibleQueries() {
        return possibleQueries;
    }
}
