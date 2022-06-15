package fr.dila.solonepg.ui.helper;

import fr.dila.solonepg.elastic.models.search.ClauseEt;
import fr.dila.solonepg.elastic.models.search.ClauseOu;
import fr.dila.solonepg.elastic.models.search.enums.ElasticOperatorEnum;
import fr.dila.ss.core.enumeration.OperatorEnum;
import fr.dila.ss.core.enumeration.TypeChampEnum;
import fr.dila.ss.ui.bean.RequeteLigneDTO;
import fr.dila.st.core.requete.recherchechamp.descriptor.ChampDescriptor;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.ObjectHelper;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

public final class EpgRequeteHelper {
    private static final String INFORMATION_ACTE_NUMERO_NOR = "dos:numeroNor";

    private EpgRequeteHelper() {
        // Default constructor
    }

    public static List<ClauseOu> toListClausesOu(List<RequeteLigneDTO> lignes) {
        final AtomicInteger index = new AtomicInteger();
        Collection<List<RequeteLigneDTO>> lignesGroupedByOr = lignes
            .stream()
            .collect(
                Collectors.groupingBy(
                    l -> {
                        if ("OU".equals(l.getAndOr())) {
                            return index.incrementAndGet();
                        }
                        return index.get();
                    }
                )
            )
            .values();

        List<ClauseOu> clausesOu = lignesGroupedByOr
            .stream()
            .map(ls -> new ClauseOu(ls.stream().map(EpgRequeteHelper::toClauseEt).collect(Collectors.toList())))
            .collect(Collectors.toList());

        processNumeroNorClauses(clausesOu);

        return clausesOu;
    }

    private static void processNumeroNorClauses(List<ClauseOu> clausesOu) {
        List<ClauseOu> multipleNorClausesOu = clausesOu
            .stream()
            .filter(
                c ->
                    c
                        .getClausesEt()
                        .stream()
                        .anyMatch(
                            cEt ->
                                INFORMATION_ACTE_NUMERO_NOR.equals(cEt.getField()) &&
                                cEt.getValeur() != null &&
                                cEt.getValeur().contains(";")
                        )
            )
            .collect(Collectors.toList());

        if (!multipleNorClausesOu.isEmpty()) {
            clausesOu.removeAll(multipleNorClausesOu);

            for (ClauseOu norClauseOu : multipleNorClausesOu) {
                List<ClauseEt> clausesEtWithoutNonEmptyNor = norClauseOu
                    .getClausesEt()
                    .stream()
                    .filter(
                        cEt ->
                            !INFORMATION_ACTE_NUMERO_NOR.equals(cEt.getField()) ||
                            INFORMATION_ACTE_NUMERO_NOR.equals(cEt.getField()) &&
                            cEt.getValeur() == null
                    )
                    .collect(Collectors.toList());
                List<ClauseEt> norClausesEt = norClauseOu
                    .getClausesEt()
                    .stream()
                    .filter(cEt -> INFORMATION_ACTE_NUMERO_NOR.equals(cEt.getField()))
                    .flatMap(
                        cEt ->
                            Stream
                                .of(cEt.getValeur().split(";"))
                                .map(value -> EpgRequeteHelper.newNorClauseEt(cEt, value))
                    )
                    .collect(Collectors.toList());

                norClausesEt.forEach(
                    cEt -> {
                        List<ClauseEt> clauses = Stream
                            .concat(
                                clausesEtWithoutNonEmptyNor.stream(),
                                norClausesEt.stream().filter(c -> c.getOperator() != cEt.getOperator())
                            )
                            .collect(Collectors.toList());
                        clauses.add(cEt);
                        clausesOu.add(new ClauseOu(clauses));
                    }
                );
            }
        }
    }

    private static ClauseEt newNorClauseEt(ClauseEt norClauseEt, String value) {
        ClauseEt clauseEt = new ClauseEt();
        clauseEt.setField(norClauseEt.getField());
        clauseEt.setOperator(norClauseEt.getOperator());
        clauseEt.setValeur(value);

        return clauseEt;
    }

    private static ClauseEt toClauseEt(RequeteLigneDTO ligne) {
        ClauseEt clauseEt = new ClauseEt();
        ChampDescriptor champ = ligne.getChamp();

        clauseEt.setField(champ.getField().substring(2));
        clauseEt.setOperator(ElasticOperatorEnum.toOperatorEnum(ligne.getOperator()));
        clauseEt.setNestedPath(champ.getNestedPath());

        List<String> values = getValues(ligne, champ);

        if (CollectionUtils.isNotEmpty(values)) {
            if (values.size() == 1) {
                clauseEt.setValeur(values.get(0));
            } else if (values.size() == 2 && ligne.getOperator().isRangeOperator()) {
                clauseEt.setValeurMin(values.get(0));
                clauseEt.setValeurMax(values.get(1));
            } else {
                clauseEt.setListeValeur(values);
            }
        }

        return clauseEt;
    }

    private static List<String> getValues(RequeteLigneDTO ligne, ChampDescriptor champ) {
        List<String> values = ObjectHelper.requireNonNullElse(ligne.getValue(), Collections.emptyList());
        String typeChamp = champ.getTypeChamp();
        if (TypeChampEnum.DATES_ES.name().equals(typeChamp)) {
            values = values.stream().map(DateUtil::convertToReverseDate).collect(Collectors.toList());
        } else if (
            TypeChampEnum.SIMPLE_SELECT_BOOLEAN_ES.name().equals(typeChamp) &&
            OperatorEnum.EGAL.equals(ligne.getOperator())
        ) {
            values =
                values
                    .stream()
                    .map(Integer::valueOf)
                    .map(BooleanUtils::toBoolean)
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }

        return values;
    }
}
