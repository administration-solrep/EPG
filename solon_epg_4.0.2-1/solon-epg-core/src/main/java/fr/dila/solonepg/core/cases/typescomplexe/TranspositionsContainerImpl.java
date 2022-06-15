package fr.dila.solonepg.core.cases.typescomplexe;

import fr.dila.solonepg.api.cases.typescomplexe.DossierTransposition;
import fr.dila.solonepg.api.cases.typescomplexe.TranspositionsContainer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TranspositionsContainerImpl implements TranspositionsContainer {
    private List<DossierTransposition> transpositions = Collections.emptyList();

    public TranspositionsContainerImpl() {
        this(Collections.emptyList());
    }

    public TranspositionsContainerImpl(List<DossierTransposition> transpositions) {
        super();
        this.transpositions = transpositions;
    }

    @Override
    public List<DossierTransposition> getTranspositions() {
        return Collections.unmodifiableList(transpositions);
    }

    @Override
    public void setTranspositions(List<DossierTransposition> transpositions) {
        this.transpositions = new ArrayList<>(transpositions);
    }
}
