package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.core.recherche.EpgDossierListingDTOImpl;
import fr.dila.solonepg.ui.enums.EpgColumnEnum;
import fr.dila.solonepg.ui.helper.EpgDossierListHelper;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.solonepg.ui.utils.FiltreUtils;
import fr.dila.ss.ui.bean.SSDossierList;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.bean.ColonneInfo;
import fr.dila.st.ui.bean.IColonneInfo;
import fr.dila.st.ui.enums.SortOrder;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class EpgDossierList extends SSDossierList<EpgDossierListingDTOImpl> implements IFilterableList {

    public EpgDossierList() {
        super();
    }

    private void addColonne(
        EpgColumnEnum userColumn,
        List<String> lstUserColonnes,
        DossierListForm form,
        boolean sortable
    ) {
        IColonneInfo colonneInfo;
        if (userColumn.isInfoComplementaire()) {
            colonneInfo =
                FiltreUtils.decorateFilterable(
                    buildColonne(userColumn.getLabel(), userColumn.getNxPropName(), lstUserColonnes, false),
                    userColumn
                );
        } else {
            colonneInfo =
                FiltreUtils.decorateFilterable(
                    buildColonne(
                        userColumn.getLabel(),
                        userColumn.getNxPropName(),
                        lstUserColonnes,
                        sortable,
                        userColumn.getSortName(),
                        userColumn.getSortOrder(form),
                        true,
                        userColumn.getOrder(form)
                    ),
                    userColumn
                );
        }
        getListeColonnes().add(colonneInfo);
    }

    public void buildColonnes(DossierListForm form, List<String> lstUserColonnes) {
        buildColonnes(form, lstUserColonnes, true, true);
    }

    public void buildColonnes(
        DossierListForm form,
        List<String> lstUserColonnes,
        boolean sortable,
        boolean clearColonnes
    ) {
        if (clearColonnes) {
            getListeColonnes().clear();
        }

        final DossierListForm finalForm = ObjectHelper.requireNonNullElseGet(form, DossierListForm::newForm);
        getStreamColumn()
            .forEach(col -> addColonne(col, lstUserColonnes, finalForm, sortable && col.getSortName() != null));
    }

    public void buildColonnesWithoutTri(List<String> lstUserColonnes) {
        buildColonnes(null, lstUserColonnes, false, true);
    }

    private Stream<EpgColumnEnum> getStreamColumn() {
        return Stream.of(EpgColumnEnum.values()).sorted(Comparator.comparing(EpgColumnEnum::getDisplayOrder));
    }

    @Override
    public List<IColonneInfo> getFilterableColonnes() {
        return getListeSortableAndVisibleColonnes();
    }

    @Override
    public ColonneInfo buildColonne(
        String label,
        String userColumn,
        List<String> lstUserVisibleColumns,
        boolean sortable,
        String name,
        SortOrder value,
        Boolean isLabelVisible,
        Integer order
    ) {
        return EpgDossierListHelper.buildColonne(
            label,
            userColumn,
            lstUserVisibleColumns,
            sortable,
            name,
            value,
            isLabelVisible,
            order
        );
    }
}
