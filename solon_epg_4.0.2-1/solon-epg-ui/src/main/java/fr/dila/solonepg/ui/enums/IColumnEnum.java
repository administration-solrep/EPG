package fr.dila.solonepg.ui.enums;

import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.th.bean.AbstractSortablePaginationForm;
import java.util.function.Function;

public interface IColumnEnum<T extends AbstractSortablePaginationForm, U extends IFiltreEnum> {
    String getLabel();

    String getNxPropName();

    Function<T, SortOrder> getSortOrderGetter();

    Function<T, Integer> getOrderGetter();

    U getFiltre();
}
