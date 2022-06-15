package fr.dila.solonepg.elastic.models.search;

import java.util.Collection;
import java.util.Collections;

public class ClauseOu {
    private Collection<ClauseEt> clausesEt = Collections.emptyList();

    public ClauseOu() {}

    public ClauseOu(Collection<ClauseEt> clausesEt) {
        this.clausesEt = clausesEt;
    }

    public Collection<ClauseEt> getClausesEt() {
        return clausesEt;
    }

    public void setClausesEt(Collection<ClauseEt> clausesEt) {
        this.clausesEt = clausesEt;
    }
}
