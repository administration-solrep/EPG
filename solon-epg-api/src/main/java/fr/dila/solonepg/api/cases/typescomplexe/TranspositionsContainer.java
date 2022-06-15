package fr.dila.solonepg.api.cases.typescomplexe;

import java.util.List;

public interface TranspositionsContainer {
    List<DossierTransposition> getTranspositions();

    void setTranspositions(List<DossierTransposition> transpositions);
}
