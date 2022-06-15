package fr.dila.solonepg.ui.services.pan;

import fr.dila.solonepg.ui.bean.pan.AbstractPanSortedList;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.ui.th.model.SpecificContext;

public interface PanRequeteUIService {
    <T extends AbstractMapDTO> AbstractPanSortedList<T> doRechercheExperte(SpecificContext context);
}
