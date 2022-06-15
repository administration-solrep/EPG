package fr.dila.solonepg.ui.bean.pan;

import fr.dila.st.ui.bean.ColonneInfo;
import fr.dila.st.ui.bean.IColonneInfo;

public class MesureColonneInfo extends ColonneInfo {
    private boolean isHideable;

    public MesureColonneInfo(IColonneInfo col, boolean isHideable) {
        super(col.getLabel(), col.isSortable(), col.isVisible(), col.isInverseSort(), col.isLabelVisible());
        setSortName(col.getSortName());
        setSortId(col.getSortId());
        setSortValue(col.getSortValue());
        setSortOrder(col.getSortOrder());

        setHideable(isHideable);
    }

    public boolean isHideable() {
        return isHideable;
    }

    public void setHideable(boolean isHideable) {
        this.isHideable = isHideable;
    }
}
