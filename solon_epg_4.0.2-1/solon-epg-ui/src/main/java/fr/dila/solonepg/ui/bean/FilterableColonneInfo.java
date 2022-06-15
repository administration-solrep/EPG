package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.ui.enums.IFiltreEnum;
import fr.dila.st.ui.bean.ColonneInfo;
import fr.dila.st.ui.bean.IColonneInfo;

public class FilterableColonneInfo implements IColonneInfo {
    private ColonneInfo colonneInfo;

    private IFiltreEnum filtre;

    public FilterableColonneInfo(ColonneInfo colonneInfo, IFiltreEnum filtre) {
        this.colonneInfo = colonneInfo;
        this.filtre = filtre;
    }

    @Override
    public String getLabel() {
        return colonneInfo.getLabel();
    }

    @Override
    public boolean isSortable() {
        return colonneInfo.isSortable();
    }

    @Override
    public boolean isVisible() {
        return colonneInfo.isVisible();
    }

    @Override
    public boolean isInverseSort() {
        return colonneInfo.isInverseSort();
    }

    @Override
    public boolean isLabelVisible() {
        return colonneInfo.isLabelVisible();
    }

    @Override
    public String getSortValue() {
        return colonneInfo.getSortValue();
    }

    @Override
    public String getSortName() {
        return colonneInfo.getSortName();
    }

    @Override
    public String getSortId() {
        return colonneInfo.getSortId();
    }

    @Override
    public String getSortOrder() {
        return colonneInfo.getSortOrder();
    }

    public IFiltreEnum getFiltre() {
        return filtre;
    }

    @Override
    public void setSortId(String sortId) {
        colonneInfo.setSortId(sortId);
    }

    @Override
    public void setSortOrder(String sortOrder) {
        colonneInfo.setSortOrder(sortOrder);
    }

    @Override
    public void setSortValue(String sortValue) {
        colonneInfo.setSortValue(sortValue);
    }

    @Override
    public void setSortName(String sortName) {
        colonneInfo.setSortName(sortName);
    }

    @Override
    public String toString() {
        return "FilterableColonneInfo [colonneInfo=" + colonneInfo + ", filtre=" + filtre + "]";
    }
}
