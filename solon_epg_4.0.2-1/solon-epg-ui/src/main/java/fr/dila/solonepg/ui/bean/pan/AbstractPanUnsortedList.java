package fr.dila.solonepg.ui.bean.pan;

import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public abstract class AbstractPanUnsortedList<T extends AbstractMapDTO> extends AbstractPanList<T> {

    public AbstractPanUnsortedList() {
        super();
    }

    public AbstractPanUnsortedList(SpecificContext context, List<T> dtoList) {
        super(context, dtoList);
    }

    public abstract void buildColonnes();

    @Override
    public void buildColonnes(SpecificContext context) {
        getListeColonnes().clear();
        buildColonnes();
    }
}
