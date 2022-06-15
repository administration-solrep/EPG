package fr.dila.solonepg.ui.bean.pan;

import fr.dila.solonepg.ui.bean.IFilterableList;
import fr.dila.solonepg.ui.enums.pan.PanColumnEnum;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.ui.bean.ColonneInfo;
import fr.dila.st.ui.bean.IColonneInfo;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public abstract class AbstractPanSortedList<T extends AbstractMapDTO>
    extends AbstractPanList<T>
    implements IFilterableList {

    public AbstractPanSortedList() {
        super();
    }

    public AbstractPanSortedList(SpecificContext context, List<T> dtoList) {
        super(context, dtoList);
    }

    public abstract void buildColonnes(LoisSuiviesForm form);

    @Override
    public void buildColonnes(SpecificContext context) {
        getListeColonnes().clear();
        LoisSuiviesForm fromContextData = context.getFromContextData(PanContextDataKey.PAN_FORM);
        buildColonnes(fromContextData);
    }

    @Override
    public List<IColonneInfo> getFilterableColonnes() {
        return getListeColonnes();
    }

    protected void addColonne(PanColumnEnum panColumnEnum, LoisSuiviesForm form) {
        IColonneInfo colonne = FiltreUtils.decorateFilterable(buildColonne(panColumnEnum, form), panColumnEnum);
        getListeColonnes().add(colonne);
    }

    private ColonneInfo buildColonne(PanColumnEnum panColumnEnum, LoisSuiviesForm form) {
        return buildColonne(
            panColumnEnum.getLabel(),
            null,
            null,
            true,
            panColumnEnum.getNxPropName(),
            panColumnEnum.getSortOrderGetter().apply(form),
            true,
            panColumnEnum.getOrderGetter().apply(form)
        );
    }
}
